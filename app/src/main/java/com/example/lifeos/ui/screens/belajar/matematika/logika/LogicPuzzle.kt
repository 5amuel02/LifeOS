package com.example.lifeos.ui.screens.belajar.matematika.logika

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance

enum class TokenShape { CIRCLE, SQUARE, TRIANGLE, DIAMOND, HEXAGON, STAR }

enum class TokenColor(val light: Color, val dark: Color) {
    RED(Color(0xFFE34948), Color(0xFFE66767)),
    BLUE(Color(0xFF2A78D6), Color(0xFF3987E5)),
    GREEN(Color(0xFF1BAF7A), Color(0xFF199E70)),
    YELLOW(Color(0xFFEDA100), Color(0xFFC98500)),
    PURPLE(Color(0xFF4A3AA7), Color(0xFF9085E9)),
}

@Composable
fun TokenColor.resolve(): Color {
    val isDarkSurface = MaterialTheme.colorScheme.surface.luminance() < 0.5f
    return if (isDarkSurface) dark else light
}

data class PuzzleToken(
    val shape: TokenShape,
    val color: TokenColor,
    val count: Int = 1,
    val scale: Float = 1f,
)

sealed class LogicPuzzle {
    abstract val id: Int
    abstract val explanation: String

    data class ShapePattern(
        override val id: Int,
        val instruction: String,
        val sequence: List<PuzzleToken>,
        val choices: List<PuzzleToken>,
        val correctIndex: Int,
        override val explanation: String,
    ) : LogicPuzzle()

    data class OddOneOut(
        override val id: Int,
        val instruction: String,
        val options: List<PuzzleToken>,
        val correctIndex: Int,
        override val explanation: String,
    ) : LogicPuzzle()

    data class TextLogic(
        override val id: Int,
        val prompt: String,
        val choices: List<String>,
        val correctIndex: Int,
        override val explanation: String,
    ) : LogicPuzzle()
}

val logicPuzzleBank: List<LogicPuzzle> = listOf(
    // Pola bentuk (lanjutkan urutan)
    LogicPuzzle.ShapePattern(
        id = 1,
        instruction = "Lanjutkan pola berikut:",
        sequence = listOf(
            PuzzleToken(TokenShape.CIRCLE, TokenColor.RED),
            PuzzleToken(TokenShape.SQUARE, TokenColor.BLUE),
            PuzzleToken(TokenShape.CIRCLE, TokenColor.RED),
            PuzzleToken(TokenShape.SQUARE, TokenColor.BLUE),
            PuzzleToken(TokenShape.CIRCLE, TokenColor.RED),
        ),
        choices = listOf(
            PuzzleToken(TokenShape.SQUARE, TokenColor.BLUE),
            PuzzleToken(TokenShape.CIRCLE, TokenColor.BLUE),
            PuzzleToken(TokenShape.TRIANGLE, TokenColor.RED),
            PuzzleToken(TokenShape.SQUARE, TokenColor.RED),
        ),
        correctIndex = 0,
        explanation = "Pola berulang: Lingkaran merah, Persegi biru, berulang terus. Setelah Lingkaran merah berikutnya adalah Persegi biru."
    ),
    LogicPuzzle.ShapePattern(
        id = 2,
        instruction = "Lanjutkan pola jumlah bintang berikut:",
        sequence = listOf(
            PuzzleToken(TokenShape.STAR, TokenColor.YELLOW, count = 1),
            PuzzleToken(TokenShape.STAR, TokenColor.YELLOW, count = 2),
            PuzzleToken(TokenShape.STAR, TokenColor.YELLOW, count = 3),
            PuzzleToken(TokenShape.STAR, TokenColor.YELLOW, count = 4),
        ),
        choices = listOf(
            PuzzleToken(TokenShape.STAR, TokenColor.YELLOW, count = 5),
            PuzzleToken(TokenShape.STAR, TokenColor.YELLOW, count = 4),
            PuzzleToken(TokenShape.STAR, TokenColor.YELLOW, count = 6),
            PuzzleToken(TokenShape.STAR, TokenColor.YELLOW, count = 3),
        ),
        correctIndex = 0,
        explanation = "Jumlah bintang bertambah 1 setiap langkah: 1, 2, 3, 4, lalu 5."
    ),
    LogicPuzzle.ShapePattern(
        id = 3,
        instruction = "Lanjutkan pola warna berikut:",
        sequence = listOf(
            PuzzleToken(TokenShape.CIRCLE, TokenColor.RED),
            PuzzleToken(TokenShape.CIRCLE, TokenColor.BLUE),
            PuzzleToken(TokenShape.CIRCLE, TokenColor.GREEN),
            PuzzleToken(TokenShape.CIRCLE, TokenColor.RED),
            PuzzleToken(TokenShape.CIRCLE, TokenColor.BLUE),
        ),
        choices = listOf(
            PuzzleToken(TokenShape.CIRCLE, TokenColor.GREEN),
            PuzzleToken(TokenShape.CIRCLE, TokenColor.RED),
            PuzzleToken(TokenShape.CIRCLE, TokenColor.BLUE),
            PuzzleToken(TokenShape.CIRCLE, TokenColor.YELLOW),
        ),
        correctIndex = 0,
        explanation = "Warna berulang tiap 3 langkah: Merah, Biru, Hijau. Setelah Biru berikutnya Hijau."
    ),
    LogicPuzzle.ShapePattern(
        id = 4,
        instruction = "Lanjutkan pola ukuran berikut:",
        sequence = listOf(
            PuzzleToken(TokenShape.TRIANGLE, TokenColor.PURPLE, scale = 0.6f),
            PuzzleToken(TokenShape.TRIANGLE, TokenColor.PURPLE, scale = 1.0f),
            PuzzleToken(TokenShape.TRIANGLE, TokenColor.PURPLE, scale = 1.4f),
            PuzzleToken(TokenShape.TRIANGLE, TokenColor.PURPLE, scale = 0.6f),
            PuzzleToken(TokenShape.TRIANGLE, TokenColor.PURPLE, scale = 1.0f),
        ),
        choices = listOf(
            PuzzleToken(TokenShape.TRIANGLE, TokenColor.PURPLE, scale = 1.4f),
            PuzzleToken(TokenShape.TRIANGLE, TokenColor.PURPLE, scale = 0.6f),
            PuzzleToken(TokenShape.TRIANGLE, TokenColor.PURPLE, scale = 1.0f),
            PuzzleToken(TokenShape.TRIANGLE, TokenColor.PURPLE, scale = 1.8f),
        ),
        correctIndex = 0,
        explanation = "Ukuran berulang tiap 3 langkah: kecil, sedang, besar. Setelah sedang berikutnya besar."
    ),
    LogicPuzzle.ShapePattern(
        id = 5,
        instruction = "Lanjutkan pola bentuk berikut:",
        sequence = listOf(
            PuzzleToken(TokenShape.SQUARE, TokenColor.RED),
            PuzzleToken(TokenShape.CIRCLE, TokenColor.RED),
            PuzzleToken(TokenShape.TRIANGLE, TokenColor.RED),
            PuzzleToken(TokenShape.SQUARE, TokenColor.RED),
            PuzzleToken(TokenShape.CIRCLE, TokenColor.RED),
        ),
        choices = listOf(
            PuzzleToken(TokenShape.TRIANGLE, TokenColor.RED),
            PuzzleToken(TokenShape.SQUARE, TokenColor.RED),
            PuzzleToken(TokenShape.CIRCLE, TokenColor.RED),
            PuzzleToken(TokenShape.STAR, TokenColor.RED),
        ),
        correctIndex = 0,
        explanation = "Bentuk berulang tiap 3 langkah: Persegi, Lingkaran, Segitiga. Setelah Lingkaran berikutnya Segitiga."
    ),
    LogicPuzzle.ShapePattern(
        id = 6,
        instruction = "Lanjutkan pola berikut:",
        sequence = listOf(
            PuzzleToken(TokenShape.HEXAGON, TokenColor.RED),
            PuzzleToken(TokenShape.HEXAGON, TokenColor.RED),
            PuzzleToken(TokenShape.HEXAGON, TokenColor.BLUE),
            PuzzleToken(TokenShape.HEXAGON, TokenColor.BLUE),
            PuzzleToken(TokenShape.HEXAGON, TokenColor.RED),
            PuzzleToken(TokenShape.HEXAGON, TokenColor.RED),
        ),
        choices = listOf(
            PuzzleToken(TokenShape.HEXAGON, TokenColor.BLUE),
            PuzzleToken(TokenShape.HEXAGON, TokenColor.RED),
            PuzzleToken(TokenShape.HEXAGON, TokenColor.GREEN),
            PuzzleToken(TokenShape.HEXAGON, TokenColor.YELLOW),
        ),
        correctIndex = 0,
        explanation = "Pola berulang tiap 4 langkah: merah, merah, biru, biru. Setelah merah, merah berikutnya biru."
    ),

    // Cari yang berbeda
    LogicPuzzle.OddOneOut(
        id = 7,
        instruction = "Satu dari bentuk ini berbeda dari yang lain. Pilih yang berbeda:",
        options = listOf(
            PuzzleToken(TokenShape.CIRCLE, TokenColor.RED),
            PuzzleToken(TokenShape.CIRCLE, TokenColor.RED),
            PuzzleToken(TokenShape.CIRCLE, TokenColor.RED),
            PuzzleToken(TokenShape.SQUARE, TokenColor.RED),
        ),
        correctIndex = 3,
        explanation = "Tiga bentuk adalah lingkaran, satu adalah persegi — persegi adalah yang berbeda."
    ),
    LogicPuzzle.OddOneOut(
        id = 8,
        instruction = "Satu dari bentuk ini berbeda dari yang lain. Pilih yang berbeda:",
        options = listOf(
            PuzzleToken(TokenShape.TRIANGLE, TokenColor.RED),
            PuzzleToken(TokenShape.TRIANGLE, TokenColor.BLUE),
            PuzzleToken(TokenShape.TRIANGLE, TokenColor.GREEN),
            PuzzleToken(TokenShape.CIRCLE, TokenColor.RED),
        ),
        correctIndex = 3,
        explanation = "Tiga bentuk adalah segitiga (warnanya boleh beda-beda), satu adalah lingkaran — lingkaran yang berbeda."
    ),
    LogicPuzzle.OddOneOut(
        id = 9,
        instruction = "Satu dari bentuk ini berbeda dari yang lain. Pilih yang berbeda:",
        options = listOf(
            PuzzleToken(TokenShape.STAR, TokenColor.BLUE, scale = 0.7f),
            PuzzleToken(TokenShape.STAR, TokenColor.BLUE, scale = 0.7f),
            PuzzleToken(TokenShape.STAR, TokenColor.BLUE, scale = 0.7f),
            PuzzleToken(TokenShape.STAR, TokenColor.BLUE, scale = 1.3f),
        ),
        correctIndex = 3,
        explanation = "Tiga bintang berukuran kecil, satu berukuran besar — yang besar adalah yang berbeda."
    ),
    LogicPuzzle.OddOneOut(
        id = 10,
        instruction = "Satu dari bentuk ini berbeda dari yang lain. Pilih yang berbeda:",
        options = listOf(
            PuzzleToken(TokenShape.HEXAGON, TokenColor.RED),
            PuzzleToken(TokenShape.HEXAGON, TokenColor.RED),
            PuzzleToken(TokenShape.HEXAGON, TokenColor.BLUE),
            PuzzleToken(TokenShape.HEXAGON, TokenColor.RED),
        ),
        correctIndex = 2,
        explanation = "Tiga heksagon berwarna merah, satu berwarna biru — yang biru berbeda."
    ),
    LogicPuzzle.OddOneOut(
        id = 11,
        instruction = "Satu dari bentuk ini berbeda dari yang lain. Pilih yang berbeda:",
        options = listOf(
            PuzzleToken(TokenShape.SQUARE, TokenColor.GREEN, count = 2),
            PuzzleToken(TokenShape.SQUARE, TokenColor.GREEN, count = 2),
            PuzzleToken(TokenShape.SQUARE, TokenColor.GREEN, count = 2),
            PuzzleToken(TokenShape.SQUARE, TokenColor.GREEN, count = 3),
        ),
        correctIndex = 3,
        explanation = "Tiga kotak berjumlah 2, satu berjumlah 3 — yang berjumlah 3 adalah yang berbeda."
    ),
    LogicPuzzle.OddOneOut(
        id = 12,
        instruction = "Satu dari bentuk ini berbeda dari yang lain. Pilih yang berbeda:",
        options = listOf(
            PuzzleToken(TokenShape.DIAMOND, TokenColor.PURPLE),
            PuzzleToken(TokenShape.DIAMOND, TokenColor.PURPLE),
            PuzzleToken(TokenShape.DIAMOND, TokenColor.YELLOW),
            PuzzleToken(TokenShape.DIAMOND, TokenColor.PURPLE),
        ),
        correctIndex = 2,
        explanation = "Tiga permata berwarna ungu, satu berwarna kuning — yang kuning berbeda."
    ),

    // Deduksi logika
    LogicPuzzle.TextLogic(
        id = 13,
        prompt = "Ani lebih tinggi dari Budi. Budi lebih tinggi dari Citra. Siapakah yang paling pendek di antara mereka?",
        choices = listOf("Ani", "Budi", "Citra", "Tidak bisa ditentukan"),
        correctIndex = 2,
        explanation = "Karena Ani > Budi > Citra dalam tinggi badan, Citra adalah yang paling pendek."
    ),
    LogicPuzzle.TextLogic(
        id = 14,
        prompt = "Di sebuah pulau, Ksatria selalu berkata jujur dan Penjahat selalu berbohong. A berkata, 'Aku dan B sama-sama Penjahat.' Apakah A seorang Ksatria atau Penjahat?",
        choices = listOf("Ksatria", "Penjahat", "Bisa jadi keduanya", "Tidak bisa ditentukan"),
        correctIndex = 1,
        explanation = "Jika A Ksatria (jujur), pernyataannya harus benar — tapi itu berarti A adalah Penjahat, kontradiksi. Jadi A pasti Penjahat. Karena Penjahat selalu bohong, pernyataannya salah, artinya tidak keduanya Penjahat, sehingga B adalah Ksatria."
    ),
    LogicPuzzle.TextLogic(
        id = 15,
        prompt = "Semua kucing adalah mamalia. Semua mamalia bernapas dengan paru-paru. Apakah semua kucing bernapas dengan paru-paru?",
        choices = listOf("Ya", "Tidak", "Tidak bisa ditentukan"),
        correctIndex = 0,
        explanation = "Ini silogisme yang valid: jika semua kucing mamalia, dan semua mamalia bernapas dengan paru-paru, maka pasti semua kucing bernapas dengan paru-paru."
    ),
    LogicPuzzle.TextLogic(
        id = 16,
        prompt = "Jika hari ini bukan hari Senin, maka besok adalah hari Rabu. Ternyata besok bukan hari Rabu. Apa yang bisa disimpulkan tentang hari ini?",
        choices = listOf("Hari ini Senin", "Hari ini Selasa", "Hari ini Rabu", "Tidak bisa ditentukan"),
        correctIndex = 0,
        explanation = "Dengan modus tollens: karena 'besok Rabu' terbukti salah, maka premis 'hari ini bukan Senin' juga harus salah. Jadi hari ini adalah Senin."
    ),
    LogicPuzzle.TextLogic(
        id = 17,
        prompt = "Di sebuah rak buku (dari kiri ke kanan): buku Bahasa paling kiri, sebelum buku Matematika. Buku Matematika di sebelah kiri buku IPA. Buku IPS di sebelah kanan buku IPA. Buku apa yang berada paling kanan?",
        choices = listOf("Bahasa", "Matematika", "IPA", "IPS"),
        correctIndex = 3,
        explanation = "Urutan dari kiri ke kanan: Bahasa, Matematika, IPA, IPS. Buku paling kanan adalah IPS."
    ),
    LogicPuzzle.TextLogic(
        id = 18,
        prompt = "Ada tiga kotak berlabel 'Apel', 'Jeruk', dan 'Campuran'. Semua label salah (tidak sesuai isinya). Kamu mengambil satu buah dari kotak berlabel 'Campuran' dan ternyata itu apel. Kotak manakah yang sebenarnya berisi campuran apel dan jeruk?",
        choices = listOf("Kotak berlabel Apel", "Kotak berlabel Jeruk", "Kotak berlabel Campuran", "Tidak bisa ditentukan"),
        correctIndex = 1,
        explanation = "Kotak berlabel 'Campuran' salah label dan berisi apel, jadi isinya pasti semua apel. Kotak berlabel 'Apel' juga salah label dan tidak mungkin semua apel (sudah dipakai), jadi berisi semua jeruk. Sisanya, kotak berlabel 'Jeruk', pastilah yang berisi campuran."
    ),
    LogicPuzzle.TextLogic(
        id = 19,
        prompt = "Ada dua orang, X dan Y. X berkata, 'Setidaknya salah satu dari kami berdua adalah Penjahat (pembohong).' Apakah X seorang Ksatria (jujur) atau Penjahat?",
        choices = listOf("Ksatria", "Penjahat", "Tidak bisa ditentukan"),
        correctIndex = 0,
        explanation = "Jika X Penjahat, pernyataannya salah, artinya tidak ada yang Penjahat di antara mereka — tapi X sendiri Penjahat, kontradiksi. Jadi X pasti Ksatria, dan pernyataannya benar: minimal satu dari mereka Penjahat, yaitu Y."
    ),
    LogicPuzzle.TextLogic(
        id = 20,
        prompt = "Semua siswa yang lulus ujian mendapat hadiah. Ani tidak mendapat hadiah. Apa yang bisa disimpulkan?",
        choices = listOf("Ani lulus ujian", "Ani tidak lulus ujian", "Ani mungkin lulus atau tidak", "Tidak ada kesimpulan yang bisa diambil"),
        correctIndex = 1,
        explanation = "Dengan kontraposisi dari 'lulus → dapat hadiah', diperoleh 'tidak dapat hadiah → tidak lulus'. Karena Ani tidak mendapat hadiah, Ani pasti tidak lulus ujian."
    ),
    LogicPuzzle.TextLogic(
        id = 21,
        prompt = "Tiga sahabat, Deni, Eka, dan Fajar, masing-masing menyukai satu warna berbeda: merah, biru, atau hijau. Deni tidak menyukai merah. Eka tidak menyukai merah maupun hijau. Warna apa yang disukai Fajar?",
        choices = listOf("Merah", "Biru", "Hijau", "Tidak bisa ditentukan"),
        correctIndex = 0,
        explanation = "Eka tidak suka merah maupun hijau, jadi Eka suka biru. Deni tidak suka merah, dan biru sudah milik Eka, jadi Deni suka hijau. Sisanya, Fajar, menyukai merah."
    ),
    LogicPuzzle.TextLogic(
        id = 22,
        prompt = "Dalam sebuah lomba lari, Wati finish sebelum Yanti. Yanti finish sebelum Zaki. Wati finish setelah Umar. Siapa yang finish paling awal?",
        choices = listOf("Umar", "Wati", "Yanti", "Zaki"),
        correctIndex = 0,
        explanation = "Urutan finish: Umar, Wati, Yanti, Zaki (dari Umar sebelum Wati, Wati sebelum Yanti, Yanti sebelum Zaki). Yang finish paling awal adalah Umar."
    ),
)
