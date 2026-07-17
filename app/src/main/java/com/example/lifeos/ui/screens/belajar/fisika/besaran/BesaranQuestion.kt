package com.example.lifeos.ui.screens.belajar.fisika.besaran

import com.example.lifeos.ui.screens.belajar.common.QuizQuestion

val besaranQuestionBank: List<QuizQuestion> = listOf(
    QuizQuestion(1, "Apa satuan SI (Standar Internasional) untuk besaran panjang?", listOf("Meter", "Kilogram", "Sekon", "Kelvin"), 0, "Panjang adalah besaran pokok dengan satuan SI meter (m)."),
    QuizQuestion(2, "Apa satuan SI untuk besaran massa?", listOf("Gram", "Kilogram", "Newton", "Liter"), 1, "Massa adalah besaran pokok dengan satuan SI kilogram (kg), bukan gram."),
    QuizQuestion(3, "Satuan SI untuk besaran waktu adalah...", listOf("Menit", "Jam", "Sekon", "Hari"), 2, "Waktu adalah besaran pokok dengan satuan SI sekon (s)."),
    QuizQuestion(4, "Berikut yang termasuk besaran turunan adalah...", listOf("Panjang", "Massa", "Waktu", "Kecepatan"), 3, "Kecepatan diturunkan dari panjang dibagi waktu (m/s), sehingga termasuk besaran turunan. Panjang, massa, dan waktu adalah besaran pokok."),
    QuizQuestion(5, "Apa satuan gaya dalam SI?", listOf("Joule", "Watt", "Newton", "Pascal"), 2, "Gaya diukur dalam newton (N), yaitu setara dengan kg·m/s²."),
    QuizQuestion(6, "Alat yang paling tepat untuk mengukur ketebalan selembar kertas adalah...", listOf("Mistar", "Mikrometer sekrup", "Meteran", "Jangka sorong"), 1, "Mikrometer sekrup memiliki ketelitian hingga 0,01 mm, paling teliti untuk benda sangat tipis seperti kertas."),
    QuizQuestion(7, "1 kilometer sama dengan ... meter.", listOf("10", "100", "1.000", "10.000"), 2, "Awalan 'kilo' berarti seribu, jadi 1 km = 1.000 m."),
    QuizQuestion(8, "Besaran yang memiliki nilai sekaligus arah disebut besaran...", listOf("Skalar", "Vektor", "Pokok", "Turunan"), 1, "Besaran vektor memiliki nilai dan arah (misal gaya dan kecepatan). Besaran skalar hanya memiliki nilai (misal massa dan waktu)."),
)
