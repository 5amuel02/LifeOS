package com.example.lifeos.ui.screens.belajar.bahasainggris.susunkalimat

data class SentenceQuestion(
    val id: Int,
    val words: List<String>,
    val translationHint: String,
)

val sentenceQuestionBank: List<SentenceQuestion> = listOf(
    SentenceQuestion(1, listOf("I", "like", "apples"), "Saya suka apel."),
    SentenceQuestion(2, listOf("She", "is", "my", "sister"), "Dia adalah kakak/adik perempuan saya."),
    SentenceQuestion(3, listOf("We", "go", "to", "school", "every", "day"), "Kami pergi ke sekolah setiap hari."),
    SentenceQuestion(4, listOf("The", "cat", "is", "sleeping"), "Kucing itu sedang tidur."),
    SentenceQuestion(5, listOf("He", "can", "play", "the", "guitar"), "Dia bisa bermain gitar."),
    SentenceQuestion(6, listOf("They", "are", "watching", "a", "movie"), "Mereka sedang menonton film."),
    SentenceQuestion(7, listOf("My", "mother", "cooks", "delicious", "food"), "Ibu saya memasak makanan yang lezat."),
    SentenceQuestion(8, listOf("I", "have", "never", "been", "to", "Bali"), "Saya belum pernah ke Bali."),
    SentenceQuestion(9, listOf("Where", "is", "the", "nearest", "hospital"), "Di mana rumah sakit terdekat?"),
    SentenceQuestion(10, listOf("This", "book", "is", "more", "interesting", "than", "that", "one"), "Buku ini lebih menarik daripada yang itu."),
    SentenceQuestion(11, listOf("Please", "close", "the", "door"), "Tolong tutup pintunya."),
    SentenceQuestion(12, listOf("What", "time", "does", "the", "train", "leave"), "Jam berapa kereta itu berangkat?"),
    SentenceQuestion(13, listOf("I", "am", "learning", "English", "every", "day"), "Saya belajar Bahasa Inggris setiap hari."),
    SentenceQuestion(14, listOf("She", "sings", "very", "beautifully"), "Dia bernyanyi dengan sangat indah."),
    SentenceQuestion(15, listOf("We", "should", "help", "each", "other"), "Kita harus saling membantu."),
)
