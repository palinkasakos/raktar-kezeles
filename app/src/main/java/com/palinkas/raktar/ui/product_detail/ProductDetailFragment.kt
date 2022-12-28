package com.palinkas.raktar.ui.product_detail

import android.os.Bundle
import android.view.*
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.palinkas.raktar.R
import com.palinkas.raktar.databinding.FragmentProductDetailBinding
import com.palinkas.raktar.db.entities.Product
import com.palinkas.raktar.ui.common.BaseBindingFragment
import com.palinkas.raktar.utils.setDebouncingOnClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductDetailFragment :
    BaseBindingFragment<FragmentProductDetailBinding, ProductDetailViewModel>() {
    private val viewModel by viewModels<ProductDetailViewModel>()
    private val args by navArgs<ProductDetailFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductDetailBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.vm = viewModel

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObservers()
        initListeners()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_save, menu)

        menu.findItem(R.id.action_save)?.let {
            it.setOnMenuItemClickListener {
                viewModel.insertOrUpdateProduct()

                return@setOnMenuItemClickListener true
            }
        }

        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun initListeners() {
        binding.saveButton.setDebouncingOnClickListener {
            viewModel.insertOrUpdateProduct()
        }
    }

    private fun initObservers() {
        args.productId.let {
            if (it != -1) {
                viewModel.queryProduct(it).observe(viewLifecycleOwner) { product ->
                    viewModel.setProduct(product)
                }
            } else {
                viewModel.setProduct(Product())
            }
        }
    }

    override fun setUpViewModel() = viewModel
}