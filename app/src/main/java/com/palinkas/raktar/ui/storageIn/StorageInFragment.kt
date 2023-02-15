package com.palinkas.raktar.ui.storageIn

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.palinkas.raktar.databinding.FragmentStorageInBinding

class StorageInFragment : Fragment() {

    private var _binding: FragmentStorageInBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val storageInViewModel =
            ViewModelProvider(this)[StorageInViewModel::class.java]

        _binding = FragmentStorageInBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}