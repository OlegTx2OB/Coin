package com.example.coin.usecase

import android.content.Context
import android.view.View
import android.widget.NumberPicker
import com.example.coin.R
import com.example.coin.repository.sharedprefs.spSaveMonth
import com.example.coin.repository.sharedprefs.spSaveYear
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.time.LocalDate

class ShowChoosePeriodDialogUseCase(private val layout: View, private val c: Context) {

    private var listener: DialogListener? = null

    fun setDialogListener(listener: DialogListener) {
        this.listener = listener
    }

    fun show() {
        val builder = MaterialAlertDialogBuilder(c)
        val monthPicker = layout.findViewById<NumberPicker>(R.id.month_picker)
        val yearPicker = layout.findViewById<NumberPicker>(R.id.year_picker)
        setViewsPresets(monthPicker, yearPicker)

        builder.setView(layout)
            .setTitle(c.getString(R.string.choose_date_period))
            .setPositiveButton("OK") { dialog, _ ->
                spSaveMonth(monthPicker.value, c)
                spSaveYear(yearPicker.value, c)
                listener?.onDialogPositiveClick()
                dialog.dismiss()
            }
            .setNegativeButton(c.getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }

        val alertDialog = builder.create()
        alertDialog.show()
    }

    private fun setViewsPresets(monthPicker: NumberPicker, yearPicker: NumberPicker) {
        val months = arrayOf("-",
            c.getString(R.string.jan), c.getString(R.string.feb),
            c.getString(R.string.mar), c.getString(R.string.apr),
            c.getString(R.string.may), c.getString(R.string.jun),
            c.getString(R.string.jul), c.getString(R.string.aug),
            c.getString(R.string.sep), c.getString(R.string.oct),
            c.getString(R.string.nov), c.getString(R.string.dec),
        )
        monthPicker.displayedValues = months
        monthPicker.maxValue = months.size - 1
        monthPicker.minValue = 0

        yearPicker.maxValue = 2040
        yearPicker.minValue = 2000
        yearPicker.value = LocalDate.now().year
    }

    interface DialogListener {
        fun onDialogPositiveClick()
    }
}