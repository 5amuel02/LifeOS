package com.example.lifeos.ui.screens.belajar.fisika.energi

import com.example.lifeos.ui.screens.belajar.common.QuizQuestion

val energiQuestionBank: List<QuizQuestion> = listOf(
    QuizQuestion(1, "Apa satuan energi dan usaha dalam SI?", listOf("Newton", "Joule", "Watt", "Pascal"), 1, "Energi dan usaha sama-sama diukur dalam joule (J)."),
    QuizQuestion(2, "Rumus usaha (W) yang benar adalah...", listOf("W = F × s", "W = F ÷ s", "W = m × a", "W = s ÷ t"), 0, "Usaha sama dengan gaya (F) dikali perpindahan (s) yang searah dengan gaya."),
    QuizQuestion(3, "Energi yang dimiliki benda karena posisi atau ketinggiannya disebut energi...", listOf("kinetik", "potensial", "panas", "listrik"), 1, "Energi potensial bergantung pada ketinggian benda, dengan rumus Ep = m·g·h."),
    QuizQuestion(4, "Energi yang dimiliki benda karena geraknya disebut energi...", listOf("kinetik", "potensial", "kimia", "bunyi"), 0, "Energi kinetik dimiliki oleh benda yang bergerak, dengan rumus Ek = ½·m·v²."),
    QuizQuestion(5, "Rumus energi kinetik adalah...", listOf("m·g·h", "½·m·v²", "F·s", "m·a"), 1, "Energi kinetik Ek = ½ × massa × kecepatan kuadrat, yaitu ½·m·v²."),
    QuizQuestion(6, "Daya (power) didefinisikan sebagai usaha yang dilakukan tiap satuan...", listOf("jarak", "massa", "waktu", "gaya"), 2, "Daya = usaha ÷ waktu (P = W/t), dengan satuan watt (W)."),
    QuizQuestion(7, "Sebuah gaya 10 N mendorong benda sejauh 3 m searah gaya. Berapa usaha yang dilakukan?", listOf("3 J", "13 J", "30 J", "300 J"), 2, "W = F × s = 10 N × 3 m = 30 J."),
    QuizQuestion(8, "Bunyi hukum kekekalan energi adalah energi...", listOf("dapat diciptakan dari nol", "tidak dapat diciptakan atau dimusnahkan, hanya berubah bentuk", "selalu berkurang menjadi nol", "hanya bisa berupa panas"), 1, "Energi tidak dapat diciptakan maupun dimusnahkan; energi hanya berubah dari satu bentuk ke bentuk lain."),
)
