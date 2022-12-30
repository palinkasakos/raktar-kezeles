package com.palinkas.raktar.ui.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView.OnQueryTextListener
import androidx.core.view.ViewCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.palinkas.raktar.R
import com.palinkas.raktar.databinding.FragmentProductListBinding
import com.palinkas.raktar.db.entities.Product
import com.palinkas.raktar.ui.common.BaseBindingFragment
import com.palinkas.raktar.utils.autoCleared
import com.palinkas.raktar.utils.navigateTo
import com.palinkas.raktar.utils.setDebouncingOnClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
        binding.searchView.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(s: String): Boolean {
                return false
            }

            override fun onQueryTextChange(s: String): Boolean {
//                adapter. {
//                    it is Product && (it.productNumber.contains(s) || it.name!!.contains(s))
//                }
                return false
            }
        })

        return binding.root
    }


    private fun setAppBarElevationZero() {
        val view = activity?.window?.decorView?.findViewById<View>(R.id.main_app_bar)
        view?.let {
            ViewCompat.setElevation(it, 0.0f)
        }
    }

    private fun goToDetailView(id: Int) {
        navigateTo(ProductListFragmentDirections.actionNavProductsToProductDetailFragment(id))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObservers()
        initListeners()
    }

    private fun initListeners() {
        binding.floatingActionButton.setDebouncingOnClickListener {
            goToDetailView(-1)
        }
    }

    private fun initObservers() {
        viewModel.list.observe(viewLifecycleOwner) {
            viewLifecycleOwner.lifecycleScope.launchWhenCreated {
                adapter.submitData(it)
            }
        }
    }

    override fun setUpViewModel() = viewModel
}