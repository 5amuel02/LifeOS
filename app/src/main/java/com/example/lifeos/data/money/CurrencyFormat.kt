package com.example.lifeos.data.money

import java.text.NumberFormat
import java.util.Locale

private val rupiahNumberFormat: NumberFormat =
    NumberFormat.getNumberInstance(Locale.Builder().setLanguage("in").setRegion("ID").build())

fun formatRupiah(amount: Long): String = "Rp ${rupiahNumberFormat.format(amount)}"
