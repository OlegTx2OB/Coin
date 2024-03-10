package com.example.coin.usecase

import java.time.LocalDate

fun getTimeBoundariesUseCase(month: Int, year: Int): Pair<Long, Long> {
    //spGetMonth == 0 means that month has not been chosen
    return if (month == 0) {
        LocalDate.of(year, 1, 1).toEpochDay() to
                LocalDate.of(year + 1, 1, 1).toEpochDay()
    } else {
        //spGetMonth == 12 means december, and month + 1 will means year + 1 fnd january
        if (month == 12) {
            LocalDate.of(year, month, 1).toEpochDay() to
                    LocalDate.of(year + 1, 1, 1).toEpochDay()
        } else {
            LocalDate.of(year, month, 1).toEpochDay() to
                    LocalDate.of(year, month + 1, 1).toEpochDay()
        }
    }
}