package com.example.coin.usecase

import android.content.Context
import com.example.coin.R
import com.example.coin.data.Note
import com.example.coin.repository.sharedprefs.spGetCurrencyName
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry

fun updatePieChartSectionData(
    notes: List<Note>?, maxCategoriesCount: Int, c: Context
): Pair<PieData, String> {
    val pieDataSet = PieDataSet(listOf(), "pie")
    pieDataSet.setDrawValues(false)

    return if (notes == null) {
        PieData(pieDataSet) to c.getString(R.string.clear_notes_list_text)
    } else {

        //creating Map<String, Pair<Float, Int>>, where String is Category name, Float is total sum of this category and Int is color name of background of this category
        val totalAmountMap = calculateTotalAmount(notes)
        //converting map into the list and sorting by descending by total sum
        val sortedTopCategoriesPairs =
            totalAmountMap.toList().sortedByDescending { it.second.first }

        //set colorArray with background colors of top categories
        val colorsList = mutableListOf<Int>()
        for (i in 0 until minOf(sortedTopCategoriesPairs.size, maxCategoriesCount)) {
            colorsList.add(sortedTopCategoriesPairs[i].second.second)
        }
        colorsList.add(c.getColor(R.color.transparent))

        pieDataSet.colors = colorsList

        //creating values which will be show in pie chart and description text with sums and category names
        val (entries, descriptionStr) = createPieChartEntries(
            sortedTopCategoriesPairs, maxCategoriesCount, c
        )
        pieDataSet.values = entries

        if (descriptionStr == "") {
            PieData(pieDataSet) to c.getString(R.string.clear_notes_list_text)
        } else {
            PieData(pieDataSet) to descriptionStr
        }
    }
}

private fun calculateTotalAmount(notes: List<Note>?): Map<String, Pair<Float, Int>> {
    val totalAmountMap = hashMapOf<String, Pair<Float, Int>>()
    for (note in notes!!) {
        val currentTotalPrice = totalAmountMap.getOrDefault(note.categoryName, 0f to 0)
        totalAmountMap[note.categoryName!!] =
            (currentTotalPrice.first + note.amount!!) to note.color!!
    }
    return totalAmountMap
}

private fun createPieChartEntries(
    pairs: List<Pair<String, Pair<Float, Int>>>, maxCategoriesCount: Int, c: Context
): Pair<List<PieEntry>, String> {
    val entries = mutableListOf<PieEntry>()
    var descriptionStr = ""
    val currencyName = spGetCurrencyName(c)
    if (pairs.isNotEmpty()) {
        for (i in 0 until minOf(pairs.size, maxCategoriesCount)) {

            entries.add(PieEntry(pairs[i].second.first, ""))

            if (descriptionStr != "") {
                descriptionStr += "\n\n"
            }
            descriptionStr += "${"%.2f".format(pairs[i].second.first)}$currencyName   ${pairs[i].first}"

        }
        if (pairs.size > maxCategoriesCount) {
            var sum = 0f
            for (i in 4 until pairs.size) {
                sum += pairs[i].second.first
            }
            entries.add(PieEntry(sum, ""))
            descriptionStr += "\n\n${"%.2f".format(sum)}$currencyName   ${c.getString(R.string.other)}"
        }
    }
    return Pair(entries, descriptionStr)
}
