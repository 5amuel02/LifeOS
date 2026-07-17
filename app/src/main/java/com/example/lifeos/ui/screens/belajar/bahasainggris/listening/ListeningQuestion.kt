package com.example.lifeos.ui.screens.belajar.bahasainggris.listening

data class ListeningQuestion(
    val id: Int,
    val spokenText: String,
    val choices: List<String>,
    val correctIndex: Int,
    val note: String,
)

val listeningQuestionBank: List<ListeningQuestion> = listOf(
    ListeningQuestion(1, "ship", listOf("ship", "sheep", "chip", "sip"), 0, "'Ship' (kapal) diucapkan dengan bunyi 'i' pendek, beda dengan 'sheep' (domba) yang bunyi 'ee' panjang."),
    ListeningQuestion(2, "sheep", listOf("ship", "sheep", "cheap", "sleep"), 1, "'Sheep' (domba) memiliki bunyi vokal panjang 'ee'."),
    ListeningQuestion(3, "bit", listOf("bit", "beat", "bat", "bet"), 0, "'Bit' (sedikit/potongan) memiliki bunyi vokal pendek."),
    ListeningQuestion(4, "beat", listOf("bit", "beat", "boat", "bet"), 1, "'Beat' (memukul/ketukan) memiliki bunyi vokal panjang 'ea'."),
    ListeningQuestion(5, "pen", listOf("pin", "pen", "pan", "pun"), 1, "'Pen' (pulpen) berbeda bunyi dengan 'pin' (peniti) dan 'pan' (wajan)."),
    ListeningQuestion(6, "pin", listOf("pin", "pen", "pan", "pun"), 0, "'Pin' (peniti) memiliki bunyi vokal 'i' pendek."),
    ListeningQuestion(7, "cat", listOf("cat", "cut", "cot", "kit"), 0, "'Cat' (kucing) berbeda bunyi dengan 'cut' (memotong) dan 'cot' (ranjang bayi)."),
    ListeningQuestion(8, "cut", listOf("cat", "cut", "cot", "kit"), 1, "'Cut' (memotong) memiliki bunyi vokal 'u' pendek."),
    ListeningQuestion(9, "thirty", listOf("thirty", "thirteen", "dirty", "thursday"), 0, "Perhatikan tekanan suku kata: 'THIR-ty' (30) berbeda dengan 'thir-TEEN' (13)."),
    ListeningQuestion(10, "thirteen", listOf("thirty", "thirteen", "fourteen", "fifteen"), 1, "'Thirteen' (13) memiliki tekanan di suku kata terakhir 'TEEN'."),
    ListeningQuestion(11, "live", listOf("live", "leave", "love", "lives"), 0, "'Live' (tinggal/hidup) sebagai kata kerja diucapkan dengan bunyi 'i' pendek."),
    ListeningQuestion(12, "leave", listOf("live", "leave", "love", "left"), 1, "'Leave' (pergi/meninggalkan) memiliki bunyi vokal panjang 'ea'."),
    ListeningQuestion(13, "work", listOf("work", "walk", "week", "word"), 0, "'Work' (bekerja) berbeda bunyi dengan 'walk' (berjalan)."),
    ListeningQuestion(14, "walk", listOf("work", "walk", "week", "talk"), 1, "'Walk' (berjalan) memiliki huruf 'l' yang tidak dilafalkan (silent l)."),
    ListeningQuestion(15, "Good morning", listOf("Good morning", "Good evening", "Good afternoon", "Good night"), 0, "'Good morning' diucapkan untuk menyapa di pagi hari."),
)
