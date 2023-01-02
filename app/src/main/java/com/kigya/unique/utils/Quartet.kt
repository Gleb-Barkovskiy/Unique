package com.kigya.unique.utils

data class Quartet<out A, out B, out C, out D>(
    val first: A,
    val second: B,
    val third: C,
    val fourth: D
)