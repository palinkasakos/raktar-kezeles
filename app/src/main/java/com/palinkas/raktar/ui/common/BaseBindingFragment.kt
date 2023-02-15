package com.palinkas.raktar.ui.common

import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.palinkas.raktar.utils.CommonUtils
import com.palinkas.raktar.utils.LoadingHelper
import com.palinkas.raktar.utils.PrefManager
import com.palinkas.raktar.utils.autoCleared
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

abstract class BaseBindingFragment<V : ViewDataBinding, VM : BaseViewModel> : Fragment(),
    CoroutineScope {
    private lateinit var baseViewModel: BaseViewModel
    protected var binding by autoCleared<V>()

    private val localJob: Job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + localJob

    @Inject
    lateinit var prefManager: PrefManager

    override fun onPause() {
        super.onPause()

        CommonUtils.hideKeyboard(requireContext(), view?.windowToken)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        CommonUtils.hideKeyboard(requireContext(), view.windowToken)

        baseViewModel = setUpViewModel()

        initFlows()
    }

    private fun initFlows() {
        viewLifecycleOwner.lifecycleScope.launch {
            baseViewModel.navigateBack.collect() {
                findNavController().navigateUp()
            }
        }

        viewLifecycleOwner.lifecycleScope.launch() {
            baseViewModel.snackBarSharedFlow.collect() {
                it.let { value ->
                    binding.root.let { view ->
                        when (value) {
                            is String -> Snackbar.make(view, value, Snackbar.LENGTH_SHORT).show()
                            is Int -> Snackbar.make(view, value, Snackbar.LENGTH_SHORT).show()
                            else -> {
                                Timber.e("invalid snackbar value")
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        baseViewModel.hideLoading()
    }


    fun clearFocusAndHideKeyboard() {
        binding.root.clearFocus()

        CommonUtils.hideKeyboard(binding.root.context, binding.root.windowToken)
    }

    abstract fun setUpViewModel(): BaseViewModel
}