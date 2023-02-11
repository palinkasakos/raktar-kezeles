package com.palinkas.raktar.ui.company

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.withCreated
import com.palinkas.raktar.databinding.FragmentCompanyBinding
import com.palinkas.raktar.ui.common.BaseBindingFragment
import com.palinkas.raktar.utils.autoCleared
import com.palinkas.raktar.utils.setDebouncingOnClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class CompanyFragment :
    BaseBindingFragment<FragmentCompanyBinding, CompanyViewModel>() {
    private val viewModel by viewModels<CompanyViewModel>()
    private var adapter by autoCleared<StorageListAdapter>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCompanyBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.vm = viewModel

        adapter = StorageListAdapter() { goToDetailView(it) }

        binding.recyclerView.adapter = adapter

        return binding.root
    }

    private fun goToDetailView(oid: String) {
        //navigateTo(ProductListFragmentDirections.actionNavProductsToProductDetailFragment(id))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObservers()
        initListeners()

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            withContext(Dispatchers.IO){
                viewModel.queryOwnCompany()
            }
        }
    }

    private fun initListeners() {
        binding.floatingActionButton.setDebouncingOnClickListener {
            goToDetailView("-1")
        }
    }

    private fun initObservers() {
        viewModel.storageList.observe(viewLifecycleOwner) {
            viewLifecycleOwner.lifecycleScope.launchWhenCreated {
                adapter.submitList(it)
            }
        }
    }

    override fun setUpViewModel() = viewModel
}