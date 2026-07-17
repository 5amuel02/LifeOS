package com.example.lifeos.ui.screens.belajar.matematika.tekateki

data class RiddleQuestion(
    val id: Int,
    val prompt: String,
    val hint: String,
    val displayAnswer: String,
    val acceptableAnswers: List<String>,
    val explanation: String,
)

val riddleBank: List<RiddleQuestion> = listOf(
    RiddleQuestion(
        id = 1,
        prompt = "Aku punya kota tapi tak berpenghuni, aku punya gunung tapi tiada pohonnya, aku punya sungai tapi tiada airnya. Siapakah aku?",
        hint = "Aku sering dilipat dan dibawa saat traveling.",
        displayAnswer = "Peta",
        acceptableAnswers = listOf("peta", "map"),
        explanation = "Peta menggambarkan kota, gunung, dan sungai, tapi semuanya hanya gambar, bukan yang asli."
    ),
    RiddleQuestion(
        id = 2,
        prompt = "Semakin banyak diambil dariku, semakin besar aku jadinya. Apakah aku?",
        hint = "Kamu bisa menggali aku dengan sekop.",
        displayAnswer = "Lubang",
        acceptableAnswers = listOf("lubang", "hole"),
        explanation = "Semakin banyak tanah diambil dari sebuah lubang, semakin besar lubang itu."
    ),
    RiddleQuestion(
        id = 3,
        prompt = "Aku bisa terbang tanpa sayap, aku bisa menangis tanpa mata. Ke mana pun aku pergi, kegelapan sering mengikutiku. Apakah aku?",
        hint = "Aku ada di langit dan membawa hujan.",
        displayAnswer = "Awan",
        acceptableAnswers = listOf("awan", "cloud"),
        explanation = "Awan bergerak (seolah terbang), menurunkan hujan (seolah menangis), dan bisa menggelapkan langit."
    ),
    RiddleQuestion(
        id = 4,
        prompt = "Ada berapa bulan dalam setahun yang memiliki 28 hari?",
        hint = "Pikirkan lagi — jangan cuma Februari.",
        displayAnswer = "12 (semua bulan)",
        acceptableAnswers = listOf("12", "dua belas", "semua", "semua bulan", "12 bulan"),
        explanation = "Semua 12 bulan memiliki setidaknya 28 hari, bukan hanya Februari."
    ),
    RiddleQuestion(
        id = 5,
        prompt = "Apa yang selalu datang tapi tidak pernah benar-benar sampai (tiba)?",
        hint = "Selalu ada tapi tak pernah jadi 'sekarang'.",
        displayAnswer = "Hari esok",
        acceptableAnswers = listOf("besok", "hari esok", "esok", "hari besok"),
        explanation = "Hari esok selalu 'akan datang' — begitu tiba, ia sudah berubah menjadi 'hari ini'."
    ),
    RiddleQuestion(
        id = 6,
        prompt = "Aku punya banyak 'kunci' tapi tak satupun bisa membuka pintu. Aku punya tombol 'spasi' tapi bukan ruang kosong. Apakah aku?",
        hint = "Kamu memakainya untuk mengetik.",
        displayAnswer = "Keyboard",
        acceptableAnswers = listOf("keyboard", "papan ketik"),
        explanation = "Keyboard punya banyak 'kunci' (tombol) dan tombol 'spasi', tapi bukan kunci pintu maupun ruang sungguhan."
    ),
    RiddleQuestion(
        id = 7,
        prompt = "Semakin sering aku dipakai untuk mengeringkan, semakin basah aku jadinya. Apakah aku?",
        hint = "Kamu memakainya setelah mandi.",
        displayAnswer = "Handuk",
        acceptableAnswers = listOf("handuk", "towel"),
        explanation = "Handuk dipakai untuk mengeringkan badan, tapi handuknya sendiri jadi makin basah."
    ),
    RiddleQuestion(
        id = 8,
        prompt = "Aku tidak bernyawa, tapi aku bisa tumbuh. Aku tidak punya paru-paru, tapi aku butuh udara. Aku tidak punya mulut, tapi air bisa membunuhku. Apakah aku?",
        hint = "Aku panas dan bisa membakar.",
        displayAnswer = "Api",
        acceptableAnswers = listOf("api", "fire"),
        explanation = "Api 'tumbuh' membesar, butuh oksigen (udara) untuk menyala, dan bisa padam kalau disiram air."
    ),
    RiddleQuestion(
        id = 9,
        prompt = "Nenek, ibu, dan anak perempuannya pergi memancing bersama. Ada yang bilang 'dua ibu dan dua anak perempuan pergi memancing', dan itu benar. Berapa orang sebenarnya yang pergi memancing?",
        hint = "Nenek adalah ibu dari si ibu, dan si ibu adalah ibu dari anaknya.",
        displayAnswer = "3 orang",
        acceptableAnswers = listOf("3", "tiga", "3 orang", "tiga orang"),
        explanation = "Mereka hanya bertiga: nenek, ibu, dan anak perempuan. Nenek adalah ibu dari si ibu, dan si ibu adalah ibu dari anak perempuan — sehingga ada '2 ibu' dan '2 anak perempuan' meski orangnya cuma 3."
    ),
    RiddleQuestion(
        id = 10,
        prompt = "Aku dibuka ketika hujan turun, tapi bagian dalamku tidak pernah basah. Apakah aku?",
        hint = "Kamu membawaku saat hujan.",
        displayAnswer = "Payung",
        acceptableAnswers = listOf("payung", "umbrella"),
        explanation = "Payung dibuka saat hujan turun, namun bagian dalamnya tetap kering."
    ),
    RiddleQuestion(
        id = 11,
        prompt = "Aku menangis tanpa mata, aku memberi cahaya tanpa listrik, dan tubuhku menyusut seiring waktu. Apakah aku?",
        hint = "Kamu menyalakan aku saat mati lampu atau saat ulang tahun.",
        displayAnswer = "Lilin",
        acceptableAnswers = listOf("lilin", "candle"),
        explanation = "Lilin meleleh (seperti menangis), memberi cahaya dari api tanpa listrik, dan tubuhnya makin pendek seiring terbakar."
    ),
    RiddleQuestion(
        id = 12,
        prompt = "Apa yang bisa kamu pegang di tangan kirimu, tapi tidak bisa kamu pegang di tangan kananmu?",
        hint = "Jawabannya adalah bagian dari tubuhmu sendiri.",
        displayAnswer = "Tangan kanan",
        acceptableAnswers = listOf("tangan kanan", "tangan kananmu", "tangan kanan sendiri"),
        explanation = "Kamu bisa memegang tangan kananmu dengan tangan kiri, tapi tidak bisa memegang tangan kananmu sendiri dengan tangan kanan."
    ),
    RiddleQuestion(
        id = 13,
        prompt = "Aku punya banyak gigi tapi tak pernah bisa menggigit. Apakah aku?",
        hint = "Kamu memakainya untuk merapikan rambut.",
        displayAnswer = "Sisir",
        acceptableAnswers = listOf("sisir", "comb"),
        explanation = "Sisir memiliki 'gigi-gigi' (gerigi) tapi tentu tidak untuk menggigit, melainkan untuk merapikan rambut."
    ),
    RiddleQuestion(
        id = 14,
        prompt = "Aku selalu ada di depanmu tapi tak pernah bisa kamu lihat langsung sebelum ia terjadi. Apakah aku?",
        hint = "Sesuatu yang belum terjadi.",
        displayAnswer = "Masa depan",
        acceptableAnswers = listOf("masa depan", "future"),
        explanation = "Masa depan selalu 'di depan' kita dalam artian waktu, tapi kita tidak pernah bisa benar-benar melihatnya sebelum terjadi."
    ),
    RiddleQuestion(
        id = 15,
        prompt = "Sebuah bus membawa 7 anak. Di halte pertama turun 2 anak, naik 3 anak. Di halte kedua turun 4 anak, naik 1 anak. Ada berapa halte yang telah dilewati bus itu?",
        hint = "Perhatikan baik-baik apa yang sebenarnya ditanyakan, bukan jumlah anaknya.",
        displayAnswer = "2 halte",
        acceptableAnswers = listOf("2", "dua", "2 halte", "dua halte"),
        explanation = "Ini teka-teki jebakan — pertanyaannya bukan soal jumlah penumpang, melainkan jumlah halte yang telah dilewati, yaitu 2 halte."
    ),
    RiddleQuestion(
        id = 16,
        prompt = "Aku mempunyai leher tapi tak punya kepala, aku mempunyai lengan tapi tak punya tangan. Apakah aku?",
        hint = "Kamu memakainya menutupi tubuh bagian atas.",
        displayAnswer = "Baju",
        acceptableAnswers = listOf("baju", "kemeja", "kaos"),
        explanation = "Baju/kemeja memiliki bagian leher dan lengan, tapi tentu saja tanpa kepala maupun tangan sungguhan."
    ),
    RiddleQuestion(
        id = 17,
        prompt = "Aku bisa dipecahkan, dibuat, diceritakan, dan dijaga. Apakah aku?",
        hint = "Sesuatu yang tidak semua orang boleh tahu.",
        displayAnswer = "Rahasia",
        acceptableAnswers = listOf("rahasia", "secret"),
        explanation = "Rahasia bisa 'dipecahkan' (dibongkar), 'dibuat', 'diceritakan', dan 'dijaga' — semua istilah ini cocok untuk kata 'rahasia'."
    ),
    RiddleQuestion(
        id = 18,
        prompt = "Ayah dan anak laki-lakinya mengalami kecelakaan mobil. Sang ayah meninggal di tempat, dan anaknya dilarikan ke rumah sakit dalam kondisi kritis. Di ruang operasi, dokter bedah melihat pasien itu dan berkata, 'Aku tidak bisa mengoperasi anak ini, dia anakku sendiri!' Siapakah dokter bedah itu?",
        hint = "Pikirkan siapa lagi yang bisa menjadi orang tua si anak selain ayahnya.",
        displayAnswer = "Ibunya",
        acceptableAnswers = listOf("ibunya", "ibu", "ibu si anak", "dokter itu ibunya"),
        explanation = "Dokter bedah itu adalah ibu dari anak tersebut. Teka-teki ini sering mengecoh karena asumsi bahwa dokter selalu laki-laki."
    ),
    RiddleQuestion(
        id = 19,
        prompt = "Aku tak bisa berbicara sendiri, tapi aku akan 'menjawab' ketika kamu berteriak kepadaku di lembah atau gua. Apakah aku?",
        hint = "Kamu sering mendengarku di gua atau lembah.",
        displayAnswer = "Gema",
        acceptableAnswers = listOf("gema", "gaung", "echo"),
        explanation = "Gema (echo) seolah 'menjawab' dengan mengulangi suara yang kamu ucapkan, meski sebenarnya ia tak bisa berbicara sendiri."
    ),
    RiddleQuestion(
        id = 20,
        prompt = "Aku terlahir dari air, tapi jika aku kembali ke air, wujudku sebagai 'aku' akan hilang. Apakah aku?",
        hint = "Kamu menaruhku di dalam freezer.",
        displayAnswer = "Es",
        acceptableAnswers = listOf("es", "es batu", "ice"),
        explanation = "Es terbentuk dari air yang membeku, tapi jika es itu mencair kembali menjadi air, wujud 'es'-nya hilang."
    ),
)

/** Compares a raw user answer against the accepted answers, tolerant of punctuation/case. */
fun isRiddleAnswerCorrect(rawInput: String, acceptableAnswers: List<String>): Boolean {
    val normalizedInput = normalizeRiddleAnswer(rawInput)
    if (normalizedInput.isEmpty()) return false
    return acceptableAnswers.any { normalizeRiddleAnswer(it) == normalizedInput }
}

private fun normalizeRiddleAnswer(text: String): String =
    text.trim()
        .lowercase()
        .replace(Regex("[^a-z0-9 ]"), "")
        .replace(Regex("\\s+"), " ")
        .trim()
