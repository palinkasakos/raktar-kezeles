package com.palinkas.raktar.utils

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.Spinner
import androidx.activity.OnBackPressedCallback
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.palinkas.raktar.R
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.math.BigDecimal
import java.math.BigInteger
import java.security.MessageDigest
import java.util.*
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

suspend fun <T> Call<T>.await(): Response<T> = suspendCoroutine { continuation ->
    val callback = object : Callback<T> {
        override fun onFailure(call: Call<T>, t: Throwable) {
            continuation.resumeWithException(t)
        }

        override fun onResponse(call: Call<T>, response: Response<T>) =
            continuation.resumeNormallyOrWithException {
                if (response.isSuccessful || response.code() == 404) {
                    return@resumeNormallyOrWithException response
                } else {
                    throw IllegalStateException("Http error ${response.code()}, request:${request().url()}")
                }
            }
    }
    enqueue(callback)
}

private inline fun <T> Continuation<T>.resumeNormallyOrWithException(getter: () -> T) =
    try {
        val result = getter()
        resume(result)
    } catch (exception: Throwable) {
        resumeWithException(exception)
    }

//PagingAdapter státusz figyelése
fun CombinedLoadStates.decideOnState(
    showLoading: ((Boolean) -> Unit)? = null,
    showEmptyState: ((Boolean) -> Unit)? = null,
    showError: ((String) -> Unit)? = null
) {
    showLoading?.invoke(refresh is LoadState.Loading)

    showEmptyState?.invoke(
        source.append is LoadState.NotLoading
                && source.append.endOfPaginationReached
    )

    val errorState = source.append as? LoadState.Error
        ?: source.prepend as? LoadState.Error
        ?: source.refresh as? LoadState.Error
        ?: append as? LoadState.Error
        ?: prepend as? LoadState.Error
        ?: refresh as? LoadState.Error

    errorState?.let { showError?.invoke(it.error.toString()) }
}

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}

fun View?.showHide() {
    this!!.visibility = if (this.isVisible) View.GONE else View.VISIBLE
}

fun EditText.enabledDisabled() {
    this.isEnabled = !this.isEnabled
}

suspend fun <A> Collection<Deferred<A>>.forEachParallel(): List<Pair<A?, Throwable?>> {
    val pairList = ArrayList<Pair<A?, Throwable?>>()

    map { deferred ->
        try {
            pairList.add(Pair(deferred.await(), null))
        } catch (e: Exception) {
            pairList.add(Pair(null, e))
        }
    }
    return pairList
}

fun File.md5(): String? {
    if (!this.exists()) return null
    return with(MessageDigest.getInstance("MD5")) {
        forEachBlock { buffer, bytesRead ->
            update(buffer, 0, bytesRead)
        }

        val i = BigInteger(1, digest()) // .toString(16)
        String.format("%032x", i)
    }
}

fun String?.toSqlLikeQuery(): String {
    return "%${this ?: ""}%"
}

fun Context.convertDpToFloat(valueInDp: Int): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, valueInDp.toFloat(), this.resources.displayMetrics
    )
}


fun Context.convertDpToInt(valueInDp: Int): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, valueInDp.toFloat(), this.resources.displayMetrics
    ).toInt()
}

fun String?.toBigDecimalOrZero(): BigDecimal {
    return try {
        BigDecimal(this)
    } catch (e: Exception) {
        BigDecimal.ZERO
    }
}

fun Fragment.showSnackBar(text: String, isLong: Boolean = false) {
//    this.view?.let { v->
//        Snackbar.make(v, text, if (isLong) Snackbar.LENGTH_LONG else Snackbar.LENGTH_SHORT)
//            .show()
//    }
}

fun Fragment.showSnackBar(@StringRes stringId: Int, isLong: Boolean = false) {
//    this.view?.let { v ->
//        Snackbar.make(v, stringId, if (isLong) Snackbar.LENGTH_LONG else Snackbar.LENGTH_SHORT)
//            .show()
//    }
}

fun EditText.textChangedFlow(): Flow<String> = callbackFlow {
    val textWatcher = object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            this@callbackFlow.trySend(editable?.toString() ?: "").isSuccess
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    }
    addTextChangedListener(textWatcher)
    awaitClose {
        removeTextChangedListener(textWatcher)
    }
}

fun View.hideKeyboard() {
    val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

fun View.requestFocusWihKeyboard(){
    this.requestFocus()
    val im = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
    if (this.visibility != View.VISIBLE) return
    im?.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
}

fun BigDecimal?.isNullOrZero(): Boolean {
    return (this ?: BigDecimal.ZERO).compareTo(BigDecimal.ZERO) == 0
}

fun BigDecimal?.greaterThanZero(): Boolean {
    return (this ?: BigDecimal.ZERO) > BigDecimal.ZERO
}

fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

fun EditText.applyWithDisabledTextWatcher(
    textWatcher: TextWatcher,
    codeBlock: EditText.() -> Unit
) {
    this.removeTextChangedListener(textWatcher)
    codeBlock()
    this.addTextChangedListener(textWatcher)
}

fun BigDecimal.roundTo(to: Int): BigDecimal {
    val isNegative = compareTo(BigDecimal.ZERO) < 0
    var rounded = setScale(to, BigDecimal.ROUND_HALF_UP)

    if (isNegative) rounded = rounded.negate()

    return rounded
}

fun Calendar.setTimeToZero(): Calendar {
    this.set(Calendar.HOUR_OF_DAY, 0)
    this.set(Calendar.MINUTE, 0)
    this.set(Calendar.SECOND, 0)
    this.set(Calendar.MILLISECOND, 0)

    return this
}

fun Calendar.setTimeToMax(): Calendar {
    this.set(Calendar.HOUR_OF_DAY, 23)
    this.set(Calendar.MINUTE, 59)
    this.set(Calendar.SECOND, 59)
    this.set(Calendar.MILLISECOND, 59)

    return this
}

fun Fragment.showSnackbar(
    stringId: Int,
    isLong: Boolean = false,
    actionText: String? = null,
    onClick: (() -> Unit)? = null
) {
    view?.let { view ->
        Snackbar.make(
            view,
            getString(stringId),
            if (!isLong) Snackbar.LENGTH_SHORT else Snackbar.LENGTH_LONG
        )
            .setAction(actionText) {
                onClick?.invoke()
            }
            .show()
    }
}

fun Context.isDeviceOnline(): Boolean {
    val connectivityManager =
        this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val nw = connectivityManager.activeNetwork ?: return false
        val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
        return when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            //for other device how are able to connect with Ethernet
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            //for check internet over Bluetooth
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
            else -> false
        }
    } else {
        val nwInfo = connectivityManager.activeNetworkInfo ?: return false
        return nwInfo.isConnected
    }
}

fun Spinner.getPositionAfterSelection(function: (position: Int) -> Unit) {
    this.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            function.invoke(position)
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
            //Required
        }
    }
}

fun AutoCompleteTextView.getPositionAfterSelection(function: (position: Int) -> Unit) {
    this.setOnItemClickListener { _, _, i, _ ->
        function.invoke(i)
    }
}

fun Activity?.setActionBarTitle(title: String) {
    (this as AppCompatActivity?)?.supportActionBar?.title = title
}

fun View?.setDebouncingOnClickListener(onClickEvent: ((view: View?) -> Unit)) {
    this?.setOnClickListener(DebouncingOnClickListener(onClickEvent))
}

fun <T> Fragment.getFragmentResult(resultKey: String): MutableLiveData<T>? {
    return this.findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData(resultKey)
}

fun <T> Fragment.clearFragmentResult(resultKey: String) {
    this.findNavController().currentBackStackEntry?.savedStateHandle?.remove<T>(resultKey)
}

fun <T> Fragment.setFragmentResult(resultKey: String, result: T) {
    this.findNavController().previousBackStackEntry?.savedStateHandle?.set(
        resultKey, result
    )
}

/*
* Letilja az ActionBar-ban lévő vissza nyilat
* Regisztrál egy vissza gomb listenert, és ha tiltott állapotban van, akkor csak a megadott fragmentre navigál vissza
* */
fun Fragment.customBackNavigation(
    navigateBackDirection: NavDirections,
    forbidBackNavigation: Boolean
) {
    (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
    (requireActivity() as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(null)

    requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object :
        OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (forbidBackNavigation)
                findNavController().navigate(navigateBackDirection)
            else
                findNavController().navigateUp()
        }
    })
}

fun String?.toNonNullable(): String {
    return this ?: ""
}

fun Context?.showOkDialog(
    title: CharSequence,
    message: CharSequence
) {
    if (this == null) return

    AlertDialog.Builder(this)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton("OK") { _, _ -> }
        .create()
        .show()
}

suspend fun Context.showSuspendedConfirmationDialog(
    title: String,
    message: String,
    positiveText: String,
    negativeText: String
) = suspendCancellableCoroutine<Boolean> { cont ->
    val al = AlertDialog.Builder(this)
        .setTitle(title)
        .setMessage(message).create()

    val listener = DialogInterface.OnClickListener { _, which ->
        if (which == AlertDialog.BUTTON_POSITIVE) cont.resume(true)
        else if (which == AlertDialog.BUTTON_NEGATIVE) cont.resume(false)
    }

    al.setButton(AlertDialog.BUTTON_POSITIVE, positiveText, listener)
    al.setButton(AlertDialog.BUTTON_NEGATIVE, negativeText, listener)

    // we can either decide to cancel the coroutine if the dialog
    // itself gets cancelled, or resume the coroutine with the
    // value [false]
    al.setOnCancelListener { cont.cancel() }

    // if we make this coroutine cancellable, we should also close the
    // dialog when the coroutine is cancelled
    cont.invokeOnCancellation { al.dismiss() }

    al.show()
}

fun Context?.showConfirmationDialog(
    cancellable: Boolean,
    title: String,
    message: String,
    ok: () -> Unit,
    negativeButtonRes: Int = R.string.cancel,
    cancelClickLister: DialogInterface.OnClickListener? = null
) {
    AlertDialog.Builder(this)
        .setTitle(title)
        .setMessage(message)
        .setNegativeButton(negativeButtonRes, cancelClickLister)
        .setPositiveButton(R.string.yes) { _, _ -> ok.invoke() }
        .setCancelable(cancellable)
        .create()
        .show()
}

suspend fun Context?.showConfirmationDialogAwait(
    cancellable: Boolean,
    title: String,
    message: String,
    negativeButtonRes: Int = R.string.cancel
): Boolean {
    return AlertDialog.Builder(this)
        .setTitle(title)
        .setMessage(message)
        .setCancelable(cancellable)
        .create()
        .await(this!!.getString(R.string.yes), getString(negativeButtonRes))
}

fun Fragment.navigateTo(direction: NavDirections) {
    findNavController().navigate(direction)
}

fun Activity.setToolbarTitle(resourceId: Int) {
    (this as AppCompatActivity).supportActionBar?.setTitle(resourceId)
}

fun String.addEscapeCharacter(): String {
    return this.replace("\\", "\\\\")
}

fun BigDecimal?.biggerThanZero(): Boolean {
    return (this ?: BigDecimal.ZERO) > BigDecimal.ZERO
}

fun EditText.enterPressedListener(pressed: () -> Unit) {
    this.setOnEditorActionListener { _, actionId, _: KeyEvent? ->

        if (actionId == EditorInfo.IME_ACTION_DONE) {
            pressed.invoke()
        }

        return@setOnEditorActionListener true
    }
}

fun RecyclerView.createDecoration() {
    this.addItemDecoration(
        DividerItemDecoration(
            this.context,
            (this.layoutManager as LinearLayoutManager).orientation
        )
    )
}

suspend fun AlertDialog.await(
    positiveText: String,
    negativeText: String
) = suspendCancellableCoroutine<Boolean> { cont ->
    val listener = DialogInterface.OnClickListener { _, which ->
        if (which == AlertDialog.BUTTON_POSITIVE) cont.resume(true)
        else if (which == AlertDialog.BUTTON_NEGATIVE) cont.resume(false)
    }

    setButton(AlertDialog.BUTTON_POSITIVE, positiveText, listener)
    setButton(AlertDialog.BUTTON_NEGATIVE, negativeText, listener)

    // we can either decide to cancel the coroutine if the dialog
    // itself gets cancelled, or resume the coroutine with the
    // value [false]
    setOnCancelListener {
        cont.cancel()
    }

    // if we make this coroutine cancellable, we should also close the
    // dialog when the coroutine is cancelled
    cont.invokeOnCancellation { dismiss() }

    // remember to show the dialog before returning from the block,
    // you won't be able to do it after this function is called!
    show()
}