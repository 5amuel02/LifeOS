package com.example.lifeos.ui.screens.belajar.fisika.listrikmagnet

import com.example.lifeos.ui.screens.belajar.common.QuizQuestion

val listrikMagnetQuestionBank: List<QuizQuestion> = listOf(
    QuizQuestion(1, "Apa satuan kuat arus listrik?", listOf("Volt", "Ampere", "Ohm", "Watt"), 1, "Kuat arus listrik diukur dalam ampere (A)."),
    QuizQuestion(2, "Hukum Ohm dinyatakan dengan rumus...", listOf("V = I × R", "V = I ÷ R", "V = R ÷ I", "V = I + R"), 0, "Hukum Ohm menyatakan tegangan (V) sama dengan kuat arus (I) dikali hambatan (R)."),
    QuizQuestion(3, "Alat yang digunakan untuk mengukur tegangan listrik adalah...", listOf("Amperemeter", "Voltmeter", "Barometer", "Termometer"), 1, "Voltmeter mengukur tegangan (beda potensial) dan dipasang paralel terhadap komponen."),
    QuizQuestion(4, "Apa satuan hambatan listrik?", listOf("Volt", "Ampere", "Ohm", "Joule"), 2, "Hambatan listrik diukur dalam ohm (Ω)."),
    QuizQuestion(5, "Dua kutub magnet yang senama (misal utara dengan utara) akan...", listOf("tarik-menarik", "tolak-menolak", "diam saja", "menghilang"), 1, "Kutub senama tolak-menolak, sedangkan kutub tak senama (utara dengan selatan) tarik-menarik."),
    QuizQuestion(6, "Sebuah rangkaian memiliki tegangan 12 V dan hambatan 4 Ω. Berapa kuat arusnya?", listOf("0,33 A", "2 A", "3 A", "48 A"), 2, "Dengan Hukum Ohm, I = V ÷ R = 12 V ÷ 4 Ω = 3 A."),
    QuizQuestion(7, "Bahan yang mudah menghantarkan arus listrik disebut...", listOf("konduktor", "isolator", "magnet", "dielektrik"), 0, "Konduktor (misal tembaga) mudah menghantarkan listrik, sedangkan isolator (misal plastik) sulit menghantarkan."),
    QuizQuestion(8, "Sebuah magnet selalu memiliki ... kutub.", listOf("satu", "dua (utara dan selatan)", "tiga", "empat"), 1, "Setiap magnet selalu memiliki dua kutub: utara dan selatan. Jika dipotong, tiap potongan tetap memiliki dua kutub."),
)
