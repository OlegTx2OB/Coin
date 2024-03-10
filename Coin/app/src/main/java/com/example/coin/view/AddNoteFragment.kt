package com.example.coin.view

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.view.children
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.coin.COLOR_ATTR_PRESSED_CARD
import com.example.coin.COLOR_ATTR_UNPRESSED_CARD
import com.example.coin.R
import com.example.coin.databinding.FragmentAddNoteBinding
import com.example.coin.paintCardViews
import com.example.coin.recyclerview.CategoryAdapter
import com.example.coin.recyclerview.NoteAdapter
import com.example.coin.recyclerview.SwipeToDeleteCallback
import com.example.coin.repository.room.CategoryRepository
import com.example.coin.viewmodel.AddNoteViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@AndroidEntryPoint
class AddNoteFragment : Fragment(R.layout.fragment_add_note), CategoryAdapter.ClickListener {
    @Inject
    lateinit var mCategoryRepository: CategoryRepository

    private val mVM: AddNoteViewModel by viewModels()
    private val mAdapter: CategoryAdapter = CategoryAdapter(this)
    private var mRecyclerView: RecyclerView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val binding: FragmentAddNoteBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_add_note, container, false)

        mRecyclerView = binding.cardCategories.recyclerView

        setViewsPresets(binding)
        setupClickListeners(binding, mVM)
        setupObservers(binding, mVM)
        return binding.root
    }

    override fun onClickCategory(view: CardView) {
        paintCardViews(
            mRecyclerView!!.children.toList() as List<CardView>,
            COLOR_ATTR_UNPRESSED_CARD,
            requireContext()
        )
        paintCardViews(listOf(view), COLOR_ATTR_PRESSED_CARD, requireContext())
        mVM.onCategory(view)
    }

    private fun setupClickListeners(
        binding: FragmentAddNoteBinding,
        mVM: AddNoteViewModel,
    ) {
        val swipeHandler = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = binding.cardCategories.recyclerView.adapter as CategoryAdapter
                adapter.removeAt(viewHolder.adapterPosition)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(binding.cardCategories.recyclerView)

        binding.cardviewIncExp.cardviewExpenses.setOnClickListener {
            mVM.onCardViewIncExp(false)
            paintCardViews(
                listOf(binding.cardviewIncExp.cardviewIncomes),
                COLOR_ATTR_UNPRESSED_CARD,
                requireContext()
            )
            paintCardViews(listOf(it as CardView), COLOR_ATTR_PRESSED_CARD, requireContext())
        }
        binding.cardviewIncExp.cardviewIncomes.setOnClickListener {
            mVM.onCardViewIncExp(true)
            paintCardViews(
                listOf(binding.cardviewIncExp.cardviewExpenses),
                COLOR_ATTR_UNPRESSED_CARD,
                requireContext()
            )
            paintCardViews(listOf(it as CardView), COLOR_ATTR_PRESSED_CARD, requireContext())
        }

        binding.addNoteButton.cardviewAdd.setOnClickListener {
            mVM.onAdd()
        }

        binding.cardCategories.cardviewAddCategory.setOnClickListener {
            findNavController().navigate(R.id.action_addNoteFragment_to_addCategoryFragment)
        }

        binding.cardviewDatepicker.cardviewToday.setOnClickListener {
            mVM.setTodayMinusDays(0)
            paintCardViews(
                listOf(
                    binding.cardviewDatepicker.cardviewYesterday,
                    binding.cardviewDatepicker.cardviewChoose,
                ), COLOR_ATTR_UNPRESSED_CARD, requireContext()
            )
            paintCardViews(listOf(it as CardView), COLOR_ATTR_PRESSED_CARD, requireContext())
        }

        binding.cardviewDatepicker.cardviewYesterday.setOnClickListener {
            mVM.setTodayMinusDays(1)
            paintCardViews(
                listOf(
                    binding.cardviewDatepicker.cardviewToday,
                    binding.cardviewDatepicker.cardviewChoose,
                ), COLOR_ATTR_UNPRESSED_CARD, requireContext()
            )
            paintCardViews(listOf(it as CardView), COLOR_ATTR_PRESSED_CARD, requireContext())
        }

        binding.cardviewDatepicker.cardviewChoose.setOnClickListener {
            val currT = LocalDate.now()
            val datePickerDialog = DatePickerDialog(
                requireContext(), { _, y, m, d ->
                    mVM.setEpochDay(LocalDate.of(y, m + 1, d).toEpochDay())
                }, currT.year, currT.monthValue - 1, currT.dayOfMonth
            )//m+1 выбери, месяц неправильно показывается
            datePickerDialog.show()
            paintCardViews(
                listOf(
                    binding.cardviewDatepicker.cardviewToday,
                    binding.cardviewDatepicker.cardviewYesterday,
                ), COLOR_ATTR_UNPRESSED_CARD, requireContext()
            )
            paintCardViews(listOf(it as CardView), COLOR_ATTR_PRESSED_CARD, requireContext())
        }

    }

    private fun setupObservers(binding: FragmentAddNoteBinding, mVM: AddNoteViewModel) {

        mAdapter.ldDeleteCategoryFromRoom.observe(viewLifecycleOwner) {
            CoroutineScope(Dispatchers.IO).launch {
                mCategoryRepository.deleteCategory(it)
            }
        }

        mCategoryRepository.getAllCategories().observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                mRecyclerView!!.visibility = View.VISIBLE
                binding.cardCategories.tvCategoriesListClear.visibility = View.GONE
            } else {
                mRecyclerView!!.visibility = View.GONE
                binding.cardCategories.tvCategoriesListClear.visibility = View.VISIBLE
            }
            mAdapter.addCategories(it)
        }

        mVM.ldGetAmount.observe(viewLifecycleOwner) {
            this.mVM.setAmount(binding.cardviewAmount.textInputEditText.text?.toString())
        }

        mVM.ldShowToast.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setViewsPresets(binding: FragmentAddNoteBinding) {
        mRecyclerView!!.layoutManager = GridLayoutManager(context, 4)
        mRecyclerView!!.adapter = mAdapter
        binding.cardviewAmount.textInputLayout.hint = getString(R.string.amount)
        binding.cardviewAmount.textInputEditText.inputType =
            InputType.TYPE_NUMBER_FLAG_DECIMAL or InputType.TYPE_CLASS_NUMBER
    }

}