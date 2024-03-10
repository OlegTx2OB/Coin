package com.example.coin.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.coin.COLOR_ATTR_PRESSED_CATEGORY
import com.example.coin.COLOR_ATTR_UNPRESSED_CATEGORY
import com.example.coin.R
import com.example.coin.databinding.FragmentAddCategoryBinding
import com.example.coin.paintCardViews
import com.example.coin.viewmodel.AddCategoryViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddCategoryFragment : Fragment(R.layout.fragment_add_category) {
    private val mVM: AddCategoryViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentAddCategoryBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_add_category, container, false)

        setViewsPresets(binding)
        setupClickListeners(binding, mVM)
        setupObservers(binding, mVM)

        return binding.root
    }

    private fun setupClickListeners(
        binding: FragmentAddCategoryBinding,
        mVM: AddCategoryViewModel
    ) {
        colorCardsListener(binding, mVM)
        categoriesCardsListener(binding, mVM)

        binding.addCategoryButton.cardviewAdd.setOnClickListener {
            val pairOfIsSavedAndToast = mVM.onAddCardviewAndReturnResult()
            Toast.makeText(requireContext(), pairOfIsSavedAndToast.second, Toast.LENGTH_SHORT).show()
            if (pairOfIsSavedAndToast.first == true) {
                findNavController().navigate(R.id.action_addCategoryFragment_to_addNoteFragment)
            }
        }

    }

    private fun categoriesCardsListener(
        binding: FragmentAddCategoryBinding,
        mVM: AddCategoryViewModel
    ) {
        val categoryCardsRoot = binding.cardviewCategories.cardsRootLayout
        val categoryCards = mutableListOf<View>()

        for (i in 0 until categoryCardsRoot.childCount) {
            val child = categoryCardsRoot.getChildAt(i)
            categoryCards.add(child)
        }
        categoryCards as List<CardView>

        val clickListener = View.OnClickListener {
            it as CardView
            paintCardViews(
                categoryCards, COLOR_ATTR_UNPRESSED_CATEGORY, requireContext()
            )

            paintCardViews(
                listOf(it), COLOR_ATTR_PRESSED_CATEGORY, requireContext()
            )
            mVM.onSetCategory(it)
        }

        for (child in categoryCards) {
            child.setOnClickListener(clickListener)
        }
    }

    private fun colorCardsListener(
        binding: FragmentAddCategoryBinding,
        mVM: AddCategoryViewModel
    ) {
        val colorCardsRoot = binding.cardviewColors.colorCardsParent
        val colorCards = mutableListOf<View>()

        for (i in 0 until colorCardsRoot.childCount) {
            val child = colorCardsRoot.getChildAt(i)
            colorCards.add(child)
        }

        val clickListener = View.OnClickListener {
            it as CardView
            for (colorCard in colorCards) {
                colorCard as CardView
                colorCard.getChildAt(0).visibility = View.INVISIBLE
            }
            it.getChildAt(0).visibility = View.VISIBLE
            mVM.onSetCategoryColor(it)
        }

        for (child in colorCards) {
            child.setOnClickListener(clickListener)
        }
    }

    private fun setupObservers(binding: FragmentAddCategoryBinding, mVM: AddCategoryViewModel) {
        mVM.ldGetAmount.observe(viewLifecycleOwner) {
            this.mVM.setAmount(binding.cardviewEnterText.textInputEditText.text?.toString())
        }
    }

    private fun setViewsPresets(binding: FragmentAddCategoryBinding) {
        binding.cardviewEnterText.textInputLayout.hint = getString(R.string.name)
    }
}
