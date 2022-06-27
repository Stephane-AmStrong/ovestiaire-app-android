package com.globodai.ovestiaire.ui.sell

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.globodai.ovestiaire.databinding.FragmentGalleryBinding
import com.globodai.ovestiaire.databinding.FragmentSellBinding

class SellFragment : Fragment() {

    private var _binding: FragmentSellBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val galleryViewModel =
            ViewModelProvider(this).get(GalleryViewModel::class.java)

        _binding = FragmentSellBinding.inflate(inflater, container, false)
        val root: View = binding.root


        binding.btnSell.setOnClickListener {
            val action = SellFragmentDirections.navigateToEditArticleFragment()
            it.findNavController().navigate(action)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}