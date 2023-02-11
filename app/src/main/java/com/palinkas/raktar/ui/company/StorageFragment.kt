package com.palinkas.raktar.ui.company

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.palinkas.raktar.R
import com.palinkas.raktar.databinding.FragmentCompanyBinding
import com.palinkas.raktar.databinding.FragmentStorageBinding
import com.palinkas.raktar.ui.common.BaseBindingFragment
import com.palinkas.raktar.utils.autoCleared
import com.palinkas.raktar.utils.setDebouncingOnClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StorageFragment :
    BaseBindingFragment<FragmentStorageBinding, StorageViewModel>() {
    private val viewModel by viewModels<StorageViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStorageBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.vm = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObservers()
        initListeners()
    }

    private fun initListeners() {
        binding.floatingActionButton.setDebouncingOnClickListener {

        }
    }

    private fun initObservers() {
    }

    override fun setUpViewModel() = viewModel
}