package com.palinkas.raktar.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.IBinder
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.palinkas.raktar.BuildConfig
import com.palinkas.raktar.R
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

object CommonUtils {
//    @JvmStatic
//    fun getInstallerFilePath(context: Context): File? {
//        return context.getExternalFilesDir(UpdaterWorker.INSTALLER_PATH)
//    }

    @JvmStatic
    fun getSimpleDateFormat(): SimpleDateFormat {
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    }

    @JvmStatic
    fun getSimpleDateTimeFormat(): SimpleDateFormat {
        return SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    }

    fun sqlLikeQuery(queryText: String?, inWord: Boolean): String {
        if (queryText.isNullOrEmpty()) return "%%"

        return if (inWord) "%$queryText%" else "$queryText%"
    }

    suspend fun wifiIsAvailable(context: Context): Boolean {
        val connManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?

        connManager?.let {
            it.activeNetwork?.let { activeNetwork ->
                it.getNetworkCapabilities(activeNetwork)?.let { nc ->
                    return nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                }
            }
        }

        return false
    }

    fun checkIfPermissionIsGranted(
        permission: Array<String>,
        activity: Activity,
        requestPermission: Boolean
    ): Boolean {
        val deniedPermission = mutableListOf<String>()

        permission.forEach {
            if (ContextCompat.checkSelfPermission(
                    activity,
                    it
                ) == PackageManager.PERMISSION_DENIED
            ) {
                deniedPermission.add(it)
            }
        }

        if (deniedPermission.isNotEmpty() && requestPermission)
            requestPermission(deniedPermission.toTypedArray(), activity)

        return deniedPermission.isNullOrEmpty()
    }

    @JvmStatic
    fun requestPermission(permission: Array<String>, activity: Activity) {
        if (Build.VERSION.SDK_INT >= 23) {
            activity.requestPermissions(permission, 1)
        }
    }

    @JvmStatic
    fun getVersionCode(): Int {
        return BuildConfig.VERSION_CODE
    }

    @JvmStatic
    fun getInstallApkIntent(file: File?, context: Context?): Intent? {
        if (file?.exists() == true) {
            val install = Intent(Intent.ACTION_VIEW)
            val uri = getUriForFile(context, file) ?: return null

            install.data = uri
            install.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            install.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true)

            install.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION

            return install
        }

        throw Exception("Fájl nem található!")
    }

    @JvmStatic
    private fun getUriForFile(context: Context?, file: File?): Uri? {
        if (context != null && file != null) {
            return FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID, file)
        }

        throw Exception("Context vagy fájl nem található!")
    }

    @JvmStatic
    fun hideKeyboard(context: Context?, windowToken: IBinder?) {
        if (context == null && windowToken == null) return
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm?.hideSoftInputFromWindow(windowToken, 0)
    }

    @JvmStatic
    fun setToDate(date: Date?): Date? {
        return setTime(date, 23, 59, 59, 999)
    }

    @JvmStatic
    fun setFromDate(date: Date?): Date? {
        return setTime(date, 0, 0, 0, 0)
    }

    @JvmStatic
    fun setTime(date: Date?, hour: Int, minute: Int, second: Int, milliSecond: Int): Date? {
        val calendar = Calendar.getInstance()
        calendar.time = date ?: Date()
        calendar[Calendar.HOUR_OF_DAY] = hour
        calendar[Calendar.MINUTE] = minute
        calendar[Calendar.SECOND] = second
        calendar[Calendar.MILLISECOND] = milliSecond
        return calendar.time
    }

    /**
     * Ékezetes karakterek lecserélése a megadott szövegben
     *
     * @param input String amin a karaktereket le kell cserélni
     * @return String a lecserélt karakterekkel
     */
    @JvmStatic
    fun replaceAccentChars(input: String?): String {
        if (input == null) return "%%"
        var result: String = input
        result = result.replace("á", "a")
        result = result.replace("Á", "A")
        result = result.replace("é", "e")
        result = result.replace("É", "E")
        result = result.replace("í", "i")
        result = result.replace("Í", "I")
        result = result.replace("ó", "o")
        result = result.replace("Ó", "O")
        result = result.replace("ö", "o")
        result = result.replace("Ö", "O")
        result = result.replace("ő", "o")
        result = result.replace("Ő", "O")
        result = result.replace("ú", "u")
        result = result.replace("Ú", "U")
        result = result.replace("ü", "u")
        result = result.replace("Ü", "U")
        result = result.replace("ű", "u")
        result = result.replace("Ű", "U")

        return result
    }

    @JvmStatic
    fun openAnotherApp(context: Context?, packageNames: List<String>) {
        if (packageNames.isEmpty() || context == null) return

        var launchIntent: Intent? = null


        for (it in packageNames) {
            val intent: Intent? = context.packageManager.getLaunchIntentForPackage(it)
            if (intent?.resolveActivity(context.packageManager) != null) {
                launchIntent = intent
                break
            }
        }

        if (launchIntent == null) {
            // Bring user to the market or let them choose an app?
            launchIntent = Intent(Intent.ACTION_VIEW)
            launchIntent.data = Uri.parse("market://details?id=${packageNames.first()}")
        }

        launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(launchIntent)
    }
}