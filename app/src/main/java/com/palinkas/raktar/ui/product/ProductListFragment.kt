package com.palinkas.raktar.ui.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView.OnQueryTextListener
import androidx.core.view.ViewCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.palinkas.raktar.R
import com.palinkas.raktar.databinding.FragmentProductListBinding
import com.palinkas.raktar.ui.common.BaseBindingFragment
import com.palinkas.raktar.utils.*
import dagger.hilt.android.AndroidEntryPoint
import hu.tandofer.android_kis_gep_szerviz.ui.common.adapter.ListChangedListener
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class ProductListFragment :
    BaseBindingFragment<FragmentProductListBinding, ProductListViewModel>() {
    private val viewModel by viewModels<ProductListViewModel>()
    private var adapter by autoCleared<ProductListAdapter2>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductListBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.vm = viewModel

        adapter = ProductListAdapter2() { goToDetailView(it) }

        setAppBarElevationZero()

        binding.recyclerView.adapter = adapter
        binding.searchBarLayout.searchEditText.textChangedFlow()
            .debounce(500)
            .onEach { viewModel.setFilter(it) }
            .launchIn(lifecycleScope)

        adapter.changedListener = object : ListChangedListener {
            override fun onChanged() {
                binding.recyclerView.scrollToPosition(0)
            }
        }

        return binding.root
    }


    private fun setAppBarElevationZero() {
        val view = activity?.window?.decorView?.findViewById<View>(R.id.main_app_bar)
        view?.let {
            ViewCompat.setElevation(it, 0.0f)
        }
    }

    private fun goToDetailView(oid: String) {
        navigateTo(ProductListFragmentDirections.actionNavProductsToProductDetailFragment(oid))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObservers()
        initListeners()
    }

    private fun initListeners() {
        binding.floatingActionButton.setDebouncingOnClickListener {
            goToDetailView("-1")
        }
    }

    private fun initObservers() {
        viewModel.list.observe(viewLifecycleOwner) {
            lifecycleScope.launchWhenCreated {
                adapter.submitData(it)
            }
        }
    }

    override fun setUpViewModel() = viewModel
}