package com.kigya.unique.utils.converters

import java.time.DayOfWeek
import java.time.Month

object LocaleConverter {

    object Russian {

        fun DayOfWeek.russianValue(): String {
            return when (this) {
                DayOfWeek.MONDAY -> Const.MONDAY_SHORT
                DayOfWeek.TUESDAY -> Const.TUESDAY_SHORT
                DayOfWeek.WEDNESDAY -> Const.WEDNESDAY_SHORT
                DayOfWeek.THURSDAY -> Const.THURSDAY_SHORT
                DayOfWeek.FRIDAY -> Const.FRIDAY_SHORT
                DayOfWeek.SATURDAY -> Const.SATURDAY_SHORT
                DayOfWeek.SUNDAY -> Const.SUNDAY_SHORT
            }
        }

        fun Month.russianValue(): String {
            return when (this) {
                Month.JANUARY -> Const.JANUARY
                Month.FEBRUARY -> Const.FEBRUARY
                Month.MARCH -> Const.MARCH
                Month.APRIL -> Const.APRIL
                Month.MAY -> Const.MAY
                Month.JUNE -> Const.JUNE
                Month.JULY -> Const.JULY
                Month.AUGUST -> Const.AUGUST
                Month.SEPTEMBER -> Const.SEPTEMBER
                Month.OCTOBER -> Const.OCTOBER
                Month.NOVEMBER -> Const.NOVEMBER
                Month.DECEMBER -> Const.DECEMBER
            }
        }

        object Const {
            const val MONDAY_SHORT = "ПН"
            const val TUESDAY_SHORT = "ВТ"
            const val WEDNESDAY_SHORT = "СР"
            const val THURSDAY_SHORT = "ЧТ"
            const val FRIDAY_SHORT = "ПТ"
            const val SATURDAY_SHORT = "СБ"
            const val SUNDAY_SHORT = "ВС"

            const val JANUARY = "Январь"
            const val FEBRUARY = "Февраль"
            const val MARCH = "Март"
            const val APRIL = "Апрель"
            const val MAY = "Май"
            const val JUNE = "Июнь"
            const val JULY = "Июль"
            const val AUGUST = "Август"
            const val SEPTEMBER = "Сентябрь"
            const val OCTOBER = "Октябрь"
            const val NOVEMBER = "Ноябрь"
            const val DECEMBER = "Декабрь"
        }
    }

}