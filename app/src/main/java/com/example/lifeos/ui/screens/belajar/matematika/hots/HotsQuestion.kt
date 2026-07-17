package com.example.lifeos.ui.screens.belajar.matematika.hots

import kotlin.math.abs

data class HotsQuestion(
    val id: Int,
    val prompt: String,
    val answer: String,
    val explanation: String,
)

val hotsQuestionBank: List<HotsQuestion> = listOf(
    HotsQuestion(
        id = 1,
        prompt = "Sebuah bilangan tiga angka habis dibagi 7, 8, dan 9 sekaligus. Berapakah bilangan tiga angka terkecil yang memenuhi syarat tersebut?",
        answer = "504",
        explanation = "KPK dari 7, 8, dan 9 adalah 504 (KPK(7,8)=56, lalu KPK(56,9)=504). Karena 504 sudah berupa bilangan tiga angka, itulah bilangan terkecil yang memenuhi syarat."
    ),
    HotsQuestion(
        id = 2,
        prompt = "Jumlah dari lima bilangan asli berurutan adalah 100. Berapakah bilangan terbesar di antara kelima bilangan tersebut?",
        answer = "22",
        explanation = "Misalkan bilangan tengah adalah n, maka jumlah lima bilangan berurutan = 5n = 100, sehingga n = 20. Kelima bilangan itu adalah 18, 19, 20, 21, 22. Bilangan terbesar adalah 22."
    ),
    HotsQuestion(
        id = 3,
        prompt = "Sebuah persegi panjang memiliki keliling 54 cm. Panjangnya 3 cm lebih dari dua kali lebarnya. Berapakah luas persegi panjang tersebut, dalam cm²?",
        answer = "152",
        explanation = "Keliling = 2(p+l) = 54, sehingga p+l = 27. Karena p = 2l+3, maka 2l+3+l = 27 → 3l = 24 → l = 8 dan p = 19. Luas = p×l = 19×8 = 152 cm²."
    ),
    HotsQuestion(
        id = 4,
        prompt = "Dalam sebuah kotak terdapat 5 bola merah, 4 bola biru, dan 3 bola hijau. Berapa banyak cara memilih 2 bola dengan warna yang sama?",
        answer = "19",
        explanation = "Cara memilih 2 bola sewarna = C(5,2) + C(4,2) + C(3,2) = 10 + 6 + 3 = 19 cara."
    ),
    HotsQuestion(
        id = 5,
        prompt = "Jika x + y = 10 dan xy = 21, berapakah nilai dari x² + y²?",
        answer = "58",
        explanation = "x² + y² = (x+y)² − 2xy = 10² − 2(21) = 100 − 42 = 58."
    ),
    HotsQuestion(
        id = 6,
        prompt = "Suku pertama suatu barisan aritmetika adalah 4 dan suku ke-10 adalah 40. Berapakah jumlah 10 suku pertama barisan tersebut?",
        answer = "220",
        explanation = "a₁₀ = a₁ + 9d → 40 = 4 + 9d → d = 4. Sₙ = n/2 × (a₁+aₙ) = 10/2 × (4+40) = 5 × 44 = 220."
    ),
    HotsQuestion(
        id = 7,
        prompt = "Berapa banyak bilangan bulat positif kurang dari 100 yang habis dibagi 3 atau 5?",
        answer = "46",
        explanation = "Kelipatan 3 di bawah 100 ada 33, kelipatan 5 ada 19, kelipatan 15 (irisan keduanya) ada 6. Dengan prinsip inklusi-eksklusi: 33 + 19 − 6 = 46."
    ),
    HotsQuestion(
        id = 8,
        prompt = "Sebuah kubus memiliki volume 216 cm³. Berapakah luas permukaan kubus tersebut, dalam cm²?",
        answer = "216",
        explanation = "Sisi kubus = akar pangkat tiga dari 216 = 6 cm. Luas permukaan = 6 × sisi² = 6 × 36 = 216 cm² — kebetulan sama dengan volumenya!"
    ),
    HotsQuestion(
        id = 9,
        prompt = "Berapakah digit satuan dari 7 pangkat 2024?",
        answer = "1",
        explanation = "Digit satuan 7 berulang dengan pola 7, 9, 3, 1 (periode 4). Karena 2024 habis dibagi 4, digit satuannya sama seperti 7⁴, yaitu 1."
    ),
    HotsQuestion(
        id = 10,
        prompt = "Ana, Budi, dan Citra membagi 120 kelereng dengan perbandingan 2:3:5. Berapakah selisih jumlah kelereng terbanyak dan tersedikit yang mereka dapatkan?",
        answer = "36",
        explanation = "Total perbandingan = 2+3+5 = 10 bagian, tiap bagian = 12 kelereng. Ana = 24, Budi = 36, Citra = 60. Selisih terbanyak (60) dan tersedikit (24) = 36."
    ),

    // Teori bilangan
    HotsQuestion(
        id = 11,
        prompt = "Berapakah FPB (faktor persekutuan terbesar) dari 84 dan 126?",
        answer = "42",
        explanation = "84 = 2²×3×7 dan 126 = 2×3²×7. FPB diambil dari pangkat terkecil tiap faktor: 2×3×7 = 42."
    ),
    HotsQuestion(
        id = 12,
        prompt = "Suatu bilangan jika dibagi 5 bersisa 3, dan jika dibagi 7 bersisa 4. Berapakah bilangan positif terkecil yang memenuhi kedua syarat tersebut?",
        answer = "18",
        explanation = "Bilangan berbentuk 5k+3: 3, 8, 13, 18, 23, ... Cek sisa dibagi 7: 3→3, 8→1, 13→6, 18→4 ✓. Jadi bilangan terkecil adalah 18."
    ),
    HotsQuestion(
        id = 13,
        prompt = "Jumlah semua faktor positif dari 36 adalah?",
        answer = "91",
        explanation = "36 = 2²×3². Jumlah faktor = (1+2+4)×(1+3+9) = 7×13 = 91."
    ),
    HotsQuestion(
        id = 14,
        prompt = "Berapakah sisa pembagian 2 pangkat 100 oleh 7?",
        answer = "2",
        explanation = "2³ = 8 ≡ 1 (mod 7), sehingga polanya berulang tiap 3 pangkat. 100 = 3×33 + 1, jadi 2¹⁰⁰ ≡ 2¹ = 2 (mod 7)."
    ),
    HotsQuestion(
        id = 15,
        prompt = "Bilangan tiga angka terbesar yang merupakan kelipatan 6 sekaligus kelipatan 8 adalah?",
        answer = "984",
        explanation = "KPK(6,8) = 24. Kelipatan 24 terbesar yang masih tiga angka: 24×41 = 984 (24×42 = 1008 sudah empat angka)."
    ),
    HotsQuestion(
        id = 16,
        prompt = "Berapa banyak bilangan bulat positif dari 1 sampai 200 yang merupakan kuadrat sempurna?",
        answer = "14",
        explanation = "Kuadrat sempurna ≤ 200: 1²,2²,...,14² = 196 (15² = 225 sudah lewat). Jadi ada 14 bilangan."
    ),
    HotsQuestion(
        id = 17,
        prompt = "Bilangan bulat positif a dan b memenuhi a×b = 48 dan FPB(a,b) = 4. Berapakah nilai a+b?",
        answer = "16",
        explanation = "Misalkan a=4m, b=4n dengan FPB(m,n)=1. Maka 16mn=48 → mn=3, sehingga (m,n)=(1,3) atau (3,1). Jadi {a,b}={4,12}, dan a+b = 16."
    ),
    HotsQuestion(
        id = 18,
        prompt = "Berapakah digit satuan dari 3 pangkat 2023 ditambah 7 pangkat 2023?",
        answer = "0",
        explanation = "Digit satuan 3 berpola 3,9,7,1 (periode 4); 2023 mod 4 = 3, jadi digit satuan 3²⁰²³ = 7. Digit satuan 7 berpola 7,9,3,1; pada posisi ke-3 digit satuannya 3. Jumlah 7+3=10, sehingga digit satuan totalnya 0."
    ),
    HotsQuestion(
        id = 19,
        prompt = "Sebuah bilangan asli n memenuhi n² − n = 42. Berapakah nilai n?",
        answer = "7",
        explanation = "n² − n − 42 = 0 → (n−7)(n+6) = 0. Karena n bilangan asli (positif), n = 7."
    ),
    HotsQuestion(
        id = 20,
        prompt = "Berapakah banyaknya angka nol di akhir dari 100 faktorial (100!)?",
        answer = "24",
        explanation = "Banyak nol = banyak faktor 5 dalam 100! = ⌊100/5⌋ + ⌊100/25⌋ + ⌊100/125⌋ = 20 + 4 + 0 = 24."
    ),

    // Aljabar
    HotsQuestion(
        id = 21,
        prompt = "Jika 2x − 3y = 7 dan x + y = 6, berapakah nilai x?",
        answer = "5",
        explanation = "Dari x+y=6 diperoleh x=6−y. Substitusi: 2(6−y)−3y=7 → 12−5y=7 → y=1, sehingga x=5."
    ),
    HotsQuestion(
        id = 22,
        prompt = "Jika x + 1/x = 5, berapakah nilai x² + 1/x²?",
        answer = "23",
        explanation = "(x+1/x)² = x² + 2 + 1/x² = 25, sehingga x² + 1/x² = 25 − 2 = 23."
    ),
    HotsQuestion(
        id = 23,
        prompt = "Suatu barisan geometri memiliki suku pertama 3 dan rasio 2. Berapakah suku ke-8 barisan tersebut?",
        answer = "384",
        explanation = "aₙ = a₁ × r^(n−1). a₈ = 3 × 2⁷ = 3 × 128 = 384."
    ),
    HotsQuestion(
        id = 24,
        prompt = "Jika f(x) = 2x² − 3x + 1, berapakah nilai f(4)?",
        answer = "21",
        explanation = "f(4) = 2(16) − 3(4) + 1 = 32 − 12 + 1 = 21."
    ),
    HotsQuestion(
        id = 25,
        prompt = "Diketahui x dan y bilangan positif dengan x² − y² = 45 dan x − y = 5. Berapakah nilai x+y?",
        answer = "9",
        explanation = "x² − y² = (x−y)(x+y) = 45. Karena x−y=5, maka 5(x+y)=45, sehingga x+y = 9."
    ),
    HotsQuestion(
        id = 26,
        prompt = "Jumlah tiga bilangan berurutan dalam barisan aritmetika adalah 27, dan hasil kali ketiganya 504. Berapakah bilangan terbesar di antara ketiganya?",
        answer = "14",
        explanation = "Misalkan bilangan (9−d), 9, (9+d) karena jumlahnya 3×9=27. Hasil kali: 9(81−d²)=504 → 81−d²=56 → d=5. Bilangan-bilangannya 4, 9, 14. Terbesar adalah 14."
    ),
    HotsQuestion(
        id = 27,
        prompt = "Jika 3 pangkat x sama dengan 81, berapakah nilai x²?",
        answer = "16",
        explanation = "3^x = 81 = 3⁴, sehingga x = 4. Maka x² = 16."
    ),
    HotsQuestion(
        id = 28,
        prompt = "Berapakah nilai dari 1 + 2 + 3 + ... + 50?",
        answer = "1275",
        explanation = "Jumlah deret 1 sampai n = n(n+1)/2. Untuk n=50: 50×51/2 = 1275."
    ),
    HotsQuestion(
        id = 29,
        prompt = "Jika p + q = 8 dan p² + q² = 34, berapakah nilai pq?",
        answer = "15",
        explanation = "(p+q)² = p² + 2pq + q² → 64 = 34 + 2pq → 2pq = 30, sehingga pq = 15."
    ),
    HotsQuestion(
        id = 30,
        prompt = "Persamaan kuadrat x² − 7x + 12 = 0 memiliki dua akar. Berapakah jumlah kuadrat dari kedua akar tersebut?",
        answer = "25",
        explanation = "Jumlah akar = 7, hasil kali akar = 12. Jumlah kuadrat akar = (jumlah akar)² − 2×(hasil kali akar) = 49 − 24 = 25."
    ),

    // Kombinatorika
    HotsQuestion(
        id = 31,
        prompt = "Berapa banyak cara menyusun huruf-huruf pada kata 'BUKU' (huruf U muncul dua kali)?",
        answer = "12",
        explanation = "Total huruf 4, dengan 1 huruf berulang 2 kali. Banyak susunan = 4!/2! = 24/2 = 12."
    ),
    HotsQuestion(
        id = 32,
        prompt = "Dari 6 orang akan dipilih 3 orang untuk menjadi ketua, sekretaris, dan bendahara (jabatan berbeda). Berapa banyak cara memilihnya?",
        answer = "120",
        explanation = "Karena jabatan berbeda, urutan penting: P(6,3) = 6×5×4 = 120."
    ),
    HotsQuestion(
        id = 33,
        prompt = "Sebuah dadu dilempar dua kali. Berapa banyak kemungkinan hasil di mana jumlah kedua mata dadu sama dengan 8?",
        answer = "5",
        explanation = "Pasangan (dadu1, dadu2) yang berjumlah 8: (2,6), (3,5), (4,4), (5,3), (6,2). Ada 5 kemungkinan."
    ),
    HotsQuestion(
        id = 34,
        prompt = "Berapa banyak cara memilih 3 siswa dari 10 siswa untuk menjadi anggota panitia (tanpa jabatan khusus)?",
        answer = "120",
        explanation = "Karena tanpa jabatan (urutan tidak penting), gunakan kombinasi: C(10,3) = 10!/(3!×7!) = 120."
    ),
    HotsQuestion(
        id = 35,
        prompt = "Dalam suatu kelas terdapat 30 siswa. Jika setiap siswa berjabat tangan tepat sekali dengan setiap siswa lainnya, berapa total jabat tangan yang terjadi?",
        answer = "435",
        explanation = "Banyak jabat tangan = C(30,2) = 30×29/2 = 435."
    ),
    HotsQuestion(
        id = 36,
        prompt = "Ada berapa bilangan 3 angka dengan semua angka berbeda dan semuanya ganjil (dipilih dari 1, 3, 5, 7, 9)?",
        answer = "60",
        explanation = "Pilih dan susun 3 dari 5 digit ganjil, urutan penting (posisi angka berbeda): P(5,3) = 5×4×3 = 60."
    ),
    HotsQuestion(
        id = 37,
        prompt = "Ada berapa cara menyusun 5 buku berbeda di rak jika 2 buku tertentu harus selalu berdampingan?",
        answer = "48",
        explanation = "Anggap 2 buku itu sebagai satu blok, sehingga ada 4 unit yang disusun: 4! = 24 cara, dikalikan 2 karena urutan di dalam blok bisa dibalik: 24×2 = 48."
    ),
    HotsQuestion(
        id = 38,
        prompt = "Sebuah kotak berisi 4 bola merah dan 6 bola putih. Jika diambil 3 bola sekaligus secara acak, berapa banyak cara agar terambil tepat 2 bola merah dan 1 bola putih?",
        answer = "36",
        explanation = "Banyak cara = C(4,2) × C(6,1) = 6 × 6 = 36."
    ),
    HotsQuestion(
        id = 39,
        prompt = "Berapa banyak bilangan bulat dari 1 sampai 1000 yang habis dibagi 4 tetapi tidak habis dibagi 6?",
        answer = "167",
        explanation = "Kelipatan 4 sampai 1000 ada 250. Kelipatan KPK(4,6)=12 ada 83. Yang habis dibagi 4 tapi tidak 6: 250 − 83 = 167."
    ),
    HotsQuestion(
        id = 40,
        prompt = "Dalam turnamen round-robin (setiap tim bertanding melawan setiap tim lain tepat sekali) terdapat 8 tim. Berapa total pertandingan yang dimainkan?",
        answer = "28",
        explanation = "Banyak pertandingan = C(8,2) = 8×7/2 = 28."
    ),

    // Geometri
    HotsQuestion(
        id = 41,
        prompt = "Sebuah segitiga siku-siku memiliki panjang kedua sisi siku-siku 9 cm dan 12 cm. Berapakah panjang sisi miringnya, dalam cm?",
        answer = "15",
        explanation = "Menurut teorema Pythagoras: sisi miring = √(9² + 12²) = √(81+144) = √225 = 15 cm."
    ),
    HotsQuestion(
        id = 42,
        prompt = "Luas sebuah lingkaran adalah 154 cm² (gunakan π = 22/7). Berapakah jari-jarinya, dalam cm?",
        answer = "7",
        explanation = "Luas = πr² → 154 = (22/7)r² → r² = 154×7/22 = 49 → r = 7 cm."
    ),
    HotsQuestion(
        id = 43,
        prompt = "Sebuah trapesium memiliki dua sisi sejajar 8 cm dan 14 cm, serta tinggi 6 cm. Berapakah luas trapesium tersebut, dalam cm²?",
        answer = "66",
        explanation = "Luas trapesium = ½ × (jumlah sisi sejajar) × tinggi = ½ × (8+14) × 6 = 11×6 = 66 cm²."
    ),
    HotsQuestion(
        id = 44,
        prompt = "Keliling sebuah lingkaran adalah 88 cm (gunakan π = 22/7). Berapakah luas lingkaran tersebut, dalam cm²?",
        answer = "616",
        explanation = "Keliling = 2πr → 88 = 2×(22/7)×r → r = 14 cm. Luas = πr² = (22/7)×196 = 616 cm²."
    ),
    HotsQuestion(
        id = 45,
        prompt = "Sebuah limas segiempat beraturan memiliki alas persegi bersisi 6 cm dan tinggi limas 4 cm. Berapakah volume limas tersebut, dalam cm³?",
        answer = "48",
        explanation = "Volume limas = ⅓ × luas alas × tinggi = ⅓ × 36 × 4 = 48 cm³."
    ),
    HotsQuestion(
        id = 46,
        prompt = "Dua sudut dalam suatu segitiga berukuran 50° dan 65°. Berapakah besar sudut ketiga, dalam derajat?",
        answer = "65",
        explanation = "Jumlah sudut segitiga = 180°. Sudut ketiga = 180 − 50 − 65 = 65°."
    ),
    HotsQuestion(
        id = 47,
        prompt = "Sebuah persegi memiliki panjang diagonal 10√2 cm. Berapakah luas persegi tersebut, dalam cm²?",
        answer = "100",
        explanation = "Diagonal persegi = sisi×√2, sehingga sisi = 10 cm. Luas = sisi² = 100 cm²."
    ),
    HotsQuestion(
        id = 48,
        prompt = "Sebuah tabung memiliki jari-jari alas 7 cm dan tinggi 10 cm. Berapakah volume tabung tersebut, dalam cm³ (gunakan π = 22/7)?",
        answer = "1540",
        explanation = "Volume tabung = πr²h = (22/7) × 49 × 10 = 22×7×10 = 1540 cm³."
    ),
    HotsQuestion(
        id = 49,
        prompt = "Pada segitiga ABC, titik D adalah titik tengah sisi BC. Jika luas segitiga ABC adalah 60 cm², berapakah luas segitiga ABD, dalam cm²?",
        answer = "30",
        explanation = "Garis AD adalah median, yang selalu membagi segitiga menjadi dua bagian dengan luas sama besar. Luas ABD = 60/2 = 30 cm²."
    ),
    HotsQuestion(
        id = 50,
        prompt = "Sebuah tangga bersandar pada tembok. Kaki tangga berjarak 6 meter dari tembok, dan ujung atas tangga berada 8 meter dari tanah. Berapakah panjang tangga tersebut, dalam meter?",
        answer = "10",
        explanation = "Menurut teorema Pythagoras: panjang tangga = √(6² + 8²) = √(36+64) = √100 = 10 meter."
    ),
)

/** Compares a raw user answer against the expected answer, tolerant of numeric formatting. */
fun isHotsAnswerCorrect(rawInput: String, expected: String): Boolean {
    val input = rawInput.trim()
    val expectedTrimmed = expected.trim()
    val inputNumber = input.toDoubleOrNull()
    val expectedNumber = expectedTrimmed.toDoubleOrNull()
    return if (inputNumber != null && expectedNumber != null) {
        abs(inputNumber - expectedNumber) < 1e-9
    } else {
        input.equals(expectedTrimmed, ignoreCase = true)
    }
}
