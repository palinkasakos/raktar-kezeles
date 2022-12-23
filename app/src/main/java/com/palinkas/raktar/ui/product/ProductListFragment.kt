package com.palinkas.raktar.ui.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView.OnQueryTextListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.withCreated
import com.palinkas.raktar.databinding.FragmentProductListBinding
import com.palinkas.raktar.db.entities.Product
import com.palinkas.raktar.utils.autoCleared
import com.palinkas.raktar.utils.setDebouncingOnClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class ProductListFragment : Fragment() {
    private val viewModel by viewModels<ProductListViewModel>()
    private var adapter by autoCleared<ProductListAdapter>()
    private var binding by autoCleared<FragmentProductListBinding>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductListBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.vm = viewModel

        adapter = ProductListAdapter(viewLifecycleOwner)

        binding.recyclerView.adapter = adapter
        binding.searchView.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(s: String): Boolean {
                return false
            }

            override fun onQueryTextChange(s: String): Boolean {
                adapter.items.filter {
                    it is Product && (it.productNumber.contains(s) || it.name!!.contains(s))
                }
                return false
            }
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObservers()
        initListeners()
    }

    private fun initListeners() {
        binding.floatingActionButton.setDebouncingOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    viewModel.insertProduct()
                }
            }
        }
    }

    private fun initObservers() {
        viewModel.list.observe(viewLifecycleOwner) {
            adapter.items = it
        }
    }
}