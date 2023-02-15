package com.palinkas.raktar.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.palinkas.raktar.databinding.FragmentHomeBinding
import com.palinkas.raktar.ui.common.BaseBindingFragment
import com.palinkas.raktar.utils.navigateTo
import com.palinkas.raktar.utils.setDebouncingOnClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseBindingFragment<FragmentHomeBinding, HomeViewModel>() {
    private val homeViewModel by viewModels<HomeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        initListeners()

        return binding.root
    }

    private fun initListeners() {
        binding.navigateStorageInButton.setDebouncingOnClickListener {
            navigateTo(HomeFragmentDirections.actionNavHomeToStorageInFragment())
        }

        binding.navigateStorageOutButton.setDebouncingOnClickListener {

        }
    }

    override fun setUpViewModel() = homeViewModel
}