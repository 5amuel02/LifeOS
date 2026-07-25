package com.example.lifeos.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.compose.foundation.layout.PaddingValues
import com.example.lifeos.ui.screens.belajar.BelajarScreen
import com.example.lifeos.ui.screens.belajar.BelajarSubject
import com.example.lifeos.ui.screens.belajar.BelajarSubjectScreen
import com.example.lifeos.ui.screens.belajar.bahasainggris.BahasaInggrisGame
import com.example.lifeos.ui.screens.belajar.bahasainggris.BahasaInggrisScreen
import com.example.lifeos.ui.screens.belajar.bahasainggris.kosakata.KosakataScreen
import com.example.lifeos.ui.screens.belajar.bahasainggris.listening.ListeningScreen
import com.example.lifeos.ui.screens.belajar.bahasainggris.susunkalimat.SusunKalimatScreen
import com.example.lifeos.ui.screens.belajar.bahasainggris.tatabahasa.TataBahasaScreen
import com.example.lifeos.ui.screens.belajar.fisika.FisikaGame
import com.example.lifeos.ui.screens.belajar.fisika.FisikaScreen
import com.example.lifeos.ui.screens.belajar.fisika.besaran.BesaranScreen
import com.example.lifeos.ui.screens.belajar.fisika.energi.EnergiScreen
import com.example.lifeos.ui.screens.belajar.fisika.gerak.GerakScreen
import com.example.lifeos.ui.screens.belajar.fisika.listrikmagnet.ListrikMagnetScreen
import com.example.lifeos.ui.screens.belajar.kimia.KimiaGame
import com.example.lifeos.ui.screens.belajar.kimia.KimiaScreen
import com.example.lifeos.ui.screens.belajar.kimia.asambasa.AsamBasaScreen
import com.example.lifeos.ui.screens.belajar.kimia.atom.AtomScreen
import com.example.lifeos.ui.screens.belajar.kimia.ikatan.IkatanScreen
import com.example.lifeos.ui.screens.belajar.kimia.wujud.WujudScreen
import com.example.lifeos.ui.screens.belajar.musik.MusikGame
import com.example.lifeos.ui.screens.belajar.musik.MusikScreen
import com.example.lifeos.ui.screens.belajar.musik.alatmusik.AlatMusikScreen
import com.example.lifeos.ui.screens.belajar.musik.notnada.NotNadaScreen
import com.example.lifeos.ui.screens.belajar.musik.ritmetempo.RitmeTempoScreen
import com.example.lifeos.ui.screens.belajar.musik.teorimusik.TeoriMusikScreen
import com.example.lifeos.ui.screens.belajar.seni.SeniGame
import com.example.lifeos.ui.screens.belajar.seni.SeniScreen
import com.example.lifeos.ui.screens.belajar.seni.aliran.AliranTokohScreen
import com.example.lifeos.ui.screens.belajar.seni.teknik.TeknikMediaScreen
import com.example.lifeos.ui.screens.belajar.seni.unsur.UnsurSeniScreen
import com.example.lifeos.ui.screens.belajar.seni.warna.WarnaScreen
import com.example.lifeos.ui.screens.belajar.matematika.MathGame
import com.example.lifeos.ui.screens.belajar.matematika.MatematikaScreen
import com.example.lifeos.ui.screens.belajar.matematika.hots.HotsScreen
import com.example.lifeos.ui.screens.belajar.matematika.logika.LogikaScreen
import com.example.lifeos.ui.screens.belajar.matematika.puzzle.PuzzleScreen
import com.example.lifeos.ui.screens.belajar.matematika.speedmath.SpeedMathScreen
import com.example.lifeos.ui.screens.belajar.matematika.tekateki.TekaTekiScreen
import com.example.lifeos.ui.screens.beranda.BerandaScreen
import com.example.lifeos.ui.screens.habit.HabitScreen
import com.example.lifeos.ui.screens.habit.HabitSummaryScreen
import com.example.lifeos.ui.screens.jadwal.JadwalScreen
import com.example.lifeos.ui.screens.lainnya.LainnyaScreen
import com.example.lifeos.ui.screens.statistik.StatistikScreen
import com.example.lifeos.ui.screens.targethidup.TargetHidupScreen
import com.example.lifeos.ui.screens.money.AddTransactionScreen
import com.example.lifeos.ui.screens.money.MoneyManagerScreen
import com.example.lifeos.ui.screens.notes.DrawingNoteScreen
import com.example.lifeos.ui.screens.notes.NoteEditorScreen
import com.example.lifeos.ui.screens.notes.NotesScreen
import com.example.lifeos.ui.screens.pomodoro.PomodoroScreen
import com.example.lifeos.ui.screens.pomodoro.PomodoroSummaryScreen
import com.example.lifeos.ui.screens.savings.AddSavingsGoalScreen
import com.example.lifeos.ui.screens.savings.SavingsGoalDetailScreen
import com.example.lifeos.ui.screens.settings.SettingsScreen

private const val NOTE_ID_ARG = "noteId"
private const val NOTE_EDITOR_ROUTE = "notes/editor"
private const val DRAWING_EDITOR_ROUTE = "notes/drawing"
private const val HABIT_SUMMARY_ROUTE = "habit/summary"
private const val POMODORO_SUMMARY_ROUTE = "pomodoro/summary"
private const val SETTINGS_ROUTE = "settings"
private const val JADWAL_ROUTE = "jadwal"
private const val TARGET_HIDUP_ROUTE = "target_hidup"
private const val STATISTIK_ROUTE = "statistik"
private const val MONEY_MANAGER_ROUTE = "money"
private const val ADD_TRANSACTION_ROUTE = "money/add"
private const val ADD_SAVINGS_GOAL_ROUTE = "savings/add"
private const val SAVINGS_GOAL_ID_ARG = "goalId"
private const val SAVINGS_GOAL_DETAIL_ROUTE = "savings/detail"
private const val BELAJAR_ROUTE = "belajar"
private const val BELAJAR_SUBJECT_ARG = "subject"
private const val BELAJAR_SUBJECT_ROUTE = "belajar/subject"
private const val MATEMATIKA_ROUTE = "belajar/matematika"
private const val SPEED_MATH_ROUTE = "belajar/matematika/speed_math"
private const val HOTS_ROUTE = "belajar/matematika/hots"
private const val LOGIKA_ROUTE = "belajar/matematika/logika"
private const val TEKA_TEKI_ROUTE = "belajar/matematika/teka_teki"
private const val PUZZLE_ROUTE = "belajar/matematika/puzzle"
private const val BAHASA_INGGRIS_ROUTE = "belajar/bahasa_inggris"
private const val KOSAKATA_ROUTE = "belajar/bahasa_inggris/kosakata"
private const val TATA_BAHASA_ROUTE = "belajar/bahasa_inggris/tata_bahasa"
private const val SUSUN_KALIMAT_ROUTE = "belajar/bahasa_inggris/susun_kalimat"
private const val LISTENING_ROUTE = "belajar/bahasa_inggris/listening"
private const val FISIKA_ROUTE = "belajar/fisika"
private const val FISIKA_BESARAN_ROUTE = "belajar/fisika/besaran"
private const val FISIKA_GERAK_ROUTE = "belajar/fisika/gerak"
private const val FISIKA_ENERGI_ROUTE = "belajar/fisika/energi"
private const val FISIKA_LISTRIK_ROUTE = "belajar/fisika/listrik"
private const val KIMIA_ROUTE = "belajar/kimia"
private const val KIMIA_ATOM_ROUTE = "belajar/kimia/atom"
private const val KIMIA_IKATAN_ROUTE = "belajar/kimia/ikatan"
private const val KIMIA_ASAM_BASA_ROUTE = "belajar/kimia/asam_basa"
private const val KIMIA_WUJUD_ROUTE = "belajar/kimia/wujud"
private const val MUSIK_ROUTE = "belajar/musik"
private const val MUSIK_NOT_NADA_ROUTE = "belajar/musik/not_nada"
private const val MUSIK_RITME_ROUTE = "belajar/musik/ritme_tempo"
private const val MUSIK_ALAT_ROUTE = "belajar/musik/alat_musik"
private const val MUSIK_TEORI_ROUTE = "belajar/musik/teori_musik"
private const val SENI_ROUTE = "belajar/seni"
private const val SENI_WARNA_ROUTE = "belajar/seni/warna"
private const val SENI_UNSUR_ROUTE = "belajar/seni/unsur"
private const val SENI_TEKNIK_ROUTE = "belajar/seni/teknik"
private const val SENI_ALIRAN_ROUTE = "belajar/seni/aliran"

@Composable
fun LifeOSNavHost(navController: NavHostController, innerPadding: PaddingValues) {
    NavHost(
        navController = navController,
        startDestination = Screen.Beranda.route,
        modifier = Modifier.padding(innerPadding)
    ) {
        composable(Screen.Beranda.route) {
            BerandaScreen(
                onOpenNotes = { navController.navigateToTab(Screen.Notes.route) },
                onOpenHabit = { navController.navigateToTab(Screen.Habit.route) },
                onOpenPomodoro = { navController.navigateToTab(Screen.Pomodoro.route) },
            )
        }
        composable(Screen.Notes.route) {
            NotesScreen(
                onAddNote = { navController.navigate(NOTE_EDITOR_ROUTE) },
                onAddDrawing = { navController.navigate(DRAWING_EDITOR_ROUTE) },
                onEditNote = { id -> navController.navigate("$NOTE_EDITOR_ROUTE?$NOTE_ID_ARG=$id") },
                onOpenDrawing = { id -> navController.navigate("$DRAWING_EDITOR_ROUTE?$NOTE_ID_ARG=$id") }
            )
        }
        composable(
            route = "$NOTE_EDITOR_ROUTE?$NOTE_ID_ARG={$NOTE_ID_ARG}",
            arguments = listOf(navArgument(NOTE_ID_ARG) { type = NavType.LongType; defaultValue = -1L })
        ) {
            // noteId flows into NoteEditorViewModel via Hilt's SavedStateHandle injection,
            // populated automatically from this route's nav argument.
            NoteEditorScreen(onBack = { navController.popBackStack() })
        }
        composable(
            route = "$DRAWING_EDITOR_ROUTE?$NOTE_ID_ARG={$NOTE_ID_ARG}",
            arguments = listOf(navArgument(NOTE_ID_ARG) { type = NavType.LongType; defaultValue = -1L })
        ) {
            // noteId flows into DrawingNoteViewModel via Hilt's SavedStateHandle injection,
            // populated automatically from this route's nav argument.
            DrawingNoteScreen(onBack = { navController.popBackStack() })
        }
        composable(Screen.Habit.route) {
            HabitScreen(onOpenSummary = { navController.navigate(HABIT_SUMMARY_ROUTE) })
        }
        composable(HABIT_SUMMARY_ROUTE) {
            HabitSummaryScreen(onBack = { navController.popBackStack() })
        }
        composable(Screen.Pomodoro.route) {
            PomodoroScreen(onOpenHistory = { navController.navigate(POMODORO_SUMMARY_ROUTE) })
        }
        composable(POMODORO_SUMMARY_ROUTE) {
            PomodoroSummaryScreen(onBack = { navController.popBackStack() })
        }
        composable(Screen.Lainnya.route) {
            LainnyaScreen(
                onOpenSettings = { navController.navigate(SETTINGS_ROUTE) },
                onOpenMoneyManager = { navController.navigate(MONEY_MANAGER_ROUTE) },
                onOpenBelajar = { navController.navigate(BELAJAR_ROUTE) },
                onOpenJadwal = { navController.navigate(JADWAL_ROUTE) },
                onOpenTargetHidup = { navController.navigate(TARGET_HIDUP_ROUTE) },
                onOpenStatistik = { navController.navigate(STATISTIK_ROUTE) }
            )
        }
        composable(BELAJAR_ROUTE) {
            BelajarScreen(
                onBack = { navController.popBackStack() },
                onOpenSubject = { subject ->
                    when (subject) {
                        BelajarSubject.MATEMATIKA -> navController.navigate(MATEMATIKA_ROUTE)
                        BelajarSubject.BAHASA_INGGRIS -> navController.navigate(BAHASA_INGGRIS_ROUTE)
                        BelajarSubject.FISIKA -> navController.navigate(FISIKA_ROUTE)
                        BelajarSubject.KIMIA -> navController.navigate(KIMIA_ROUTE)
                        BelajarSubject.MUSIK -> navController.navigate(MUSIK_ROUTE)
                        BelajarSubject.SENI -> navController.navigate(SENI_ROUTE)
                    }
                }
            )
        }
        composable(
            route = "$BELAJAR_SUBJECT_ROUTE/{$BELAJAR_SUBJECT_ARG}",
            arguments = listOf(navArgument(BELAJAR_SUBJECT_ARG) { type = NavType.StringType })
        ) { backStackEntry ->
            val subject = BelajarSubject.fromRoute(backStackEntry.arguments?.getString(BELAJAR_SUBJECT_ARG))
            BelajarSubjectScreen(
                subject = subject,
                onBack = { navController.popBackStack() }
            )
        }
        composable(MATEMATIKA_ROUTE) {
            MatematikaScreen(
                onBack = { navController.popBackStack() },
                onOpenGame = { game ->
                    when (game) {
                        MathGame.HITUNG_CEPAT -> navController.navigate(SPEED_MATH_ROUTE)
                        MathGame.HOTS -> navController.navigate(HOTS_ROUTE)
                        MathGame.LOGIKA -> navController.navigate(LOGIKA_ROUTE)
                        MathGame.TEKA_TEKI -> navController.navigate(TEKA_TEKI_ROUTE)
                        MathGame.PUZZLE -> navController.navigate(PUZZLE_ROUTE)
                    }
                }
            )
        }
        composable(SPEED_MATH_ROUTE) {
            SpeedMathScreen(onBack = { navController.popBackStack() })
        }
        composable(HOTS_ROUTE) {
            HotsScreen(onBack = { navController.popBackStack() })
        }
        composable(LOGIKA_ROUTE) {
            LogikaScreen(onBack = { navController.popBackStack() })
        }
        composable(TEKA_TEKI_ROUTE) {
            TekaTekiScreen(onBack = { navController.popBackStack() })
        }
        composable(PUZZLE_ROUTE) {
            PuzzleScreen(onBack = { navController.popBackStack() })
        }
        composable(BAHASA_INGGRIS_ROUTE) {
            BahasaInggrisScreen(
                onBack = { navController.popBackStack() },
                onOpenGame = { game ->
                    when (game) {
                        BahasaInggrisGame.KOSAKATA -> navController.navigate(KOSAKATA_ROUTE)
                        BahasaInggrisGame.TATA_BAHASA -> navController.navigate(TATA_BAHASA_ROUTE)
                        BahasaInggrisGame.SUSUN_KALIMAT -> navController.navigate(SUSUN_KALIMAT_ROUTE)
                        BahasaInggrisGame.LISTENING -> navController.navigate(LISTENING_ROUTE)
                    }
                }
            )
        }
        composable(KOSAKATA_ROUTE) {
            KosakataScreen(onBack = { navController.popBackStack() })
        }
        composable(TATA_BAHASA_ROUTE) {
            TataBahasaScreen(onBack = { navController.popBackStack() })
        }
        composable(SUSUN_KALIMAT_ROUTE) {
            SusunKalimatScreen(onBack = { navController.popBackStack() })
        }
        composable(LISTENING_ROUTE) {
            ListeningScreen(onBack = { navController.popBackStack() })
        }
        composable(FISIKA_ROUTE) {
            FisikaScreen(
                onBack = { navController.popBackStack() },
                onOpenGame = { game ->
                    when (game) {
                        FisikaGame.BESARAN -> navController.navigate(FISIKA_BESARAN_ROUTE)
                        FisikaGame.GERAK -> navController.navigate(FISIKA_GERAK_ROUTE)
                        FisikaGame.ENERGI -> navController.navigate(FISIKA_ENERGI_ROUTE)
                        FisikaGame.LISTRIK -> navController.navigate(FISIKA_LISTRIK_ROUTE)
                    }
                }
            )
        }
        composable(FISIKA_BESARAN_ROUTE) {
            BesaranScreen(onBack = { navController.popBackStack() })
        }
        composable(FISIKA_GERAK_ROUTE) {
            GerakScreen(onBack = { navController.popBackStack() })
        }
        composable(FISIKA_ENERGI_ROUTE) {
            EnergiScreen(onBack = { navController.popBackStack() })
        }
        composable(FISIKA_LISTRIK_ROUTE) {
            ListrikMagnetScreen(onBack = { navController.popBackStack() })
        }
        composable(KIMIA_ROUTE) {
            KimiaScreen(
                onBack = { navController.popBackStack() },
                onOpenGame = { game ->
                    when (game) {
                        KimiaGame.ATOM -> navController.navigate(KIMIA_ATOM_ROUTE)
                        KimiaGame.IKATAN -> navController.navigate(KIMIA_IKATAN_ROUTE)
                        KimiaGame.ASAM_BASA -> navController.navigate(KIMIA_ASAM_BASA_ROUTE)
                        KimiaGame.WUJUD -> navController.navigate(KIMIA_WUJUD_ROUTE)
                    }
                }
            )
        }
        composable(KIMIA_ATOM_ROUTE) {
            AtomScreen(onBack = { navController.popBackStack() })
        }
        composable(KIMIA_IKATAN_ROUTE) {
            IkatanScreen(onBack = { navController.popBackStack() })
        }
        composable(KIMIA_ASAM_BASA_ROUTE) {
            AsamBasaScreen(onBack = { navController.popBackStack() })
        }
        composable(KIMIA_WUJUD_ROUTE) {
            WujudScreen(onBack = { navController.popBackStack() })
        }
        composable(MUSIK_ROUTE) {
            MusikScreen(
                onBack = { navController.popBackStack() },
                onOpenGame = { game ->
                    when (game) {
                        MusikGame.NOT_NADA -> navController.navigate(MUSIK_NOT_NADA_ROUTE)
                        MusikGame.RITME_TEMPO -> navController.navigate(MUSIK_RITME_ROUTE)
                        MusikGame.ALAT_MUSIK -> navController.navigate(MUSIK_ALAT_ROUTE)
                        MusikGame.TEORI_MUSIK -> navController.navigate(MUSIK_TEORI_ROUTE)
                    }
                }
            )
        }
        composable(MUSIK_NOT_NADA_ROUTE) {
            NotNadaScreen(onBack = { navController.popBackStack() })
        }
        composable(MUSIK_RITME_ROUTE) {
            RitmeTempoScreen(onBack = { navController.popBackStack() })
        }
        composable(MUSIK_ALAT_ROUTE) {
            AlatMusikScreen(onBack = { navController.popBackStack() })
        }
        composable(MUSIK_TEORI_ROUTE) {
            TeoriMusikScreen(onBack = { navController.popBackStack() })
        }
        composable(SENI_ROUTE) {
            SeniScreen(
                onBack = { navController.popBackStack() },
                onOpenGame = { game ->
                    when (game) {
                        SeniGame.WARNA -> navController.navigate(SENI_WARNA_ROUTE)
                        SeniGame.UNSUR -> navController.navigate(SENI_UNSUR_ROUTE)
                        SeniGame.TEKNIK -> navController.navigate(SENI_TEKNIK_ROUTE)
                        SeniGame.ALIRAN -> navController.navigate(SENI_ALIRAN_ROUTE)
                    }
                }
            )
        }
        composable(SENI_WARNA_ROUTE) {
            WarnaScreen(onBack = { navController.popBackStack() })
        }
        composable(SENI_UNSUR_ROUTE) {
            UnsurSeniScreen(onBack = { navController.popBackStack() })
        }
        composable(SENI_TEKNIK_ROUTE) {
            TeknikMediaScreen(onBack = { navController.popBackStack() })
        }
        composable(SENI_ALIRAN_ROUTE) {
            AliranTokohScreen(onBack = { navController.popBackStack() })
        }
        composable(JADWAL_ROUTE) {
            JadwalScreen(onBack = { navController.popBackStack() })
        }
        composable(TARGET_HIDUP_ROUTE) {
            TargetHidupScreen(onBack = { navController.popBackStack() })
        }
        composable(STATISTIK_ROUTE) {
            StatistikScreen(onBack = { navController.popBackStack() })
        }
        composable(SETTINGS_ROUTE) {
            SettingsScreen(onBack = { navController.popBackStack() })
        }
        composable(MONEY_MANAGER_ROUTE) {
            MoneyManagerScreen(
                onBack = { navController.popBackStack() },
                onAddTransaction = { navController.navigate(ADD_TRANSACTION_ROUTE) },
                onAddSavingsGoal = { navController.navigate(ADD_SAVINGS_GOAL_ROUTE) },
                onOpenSavingsGoal = { id -> navController.navigate("$SAVINGS_GOAL_DETAIL_ROUTE/$id") }
            )
        }
        composable(ADD_TRANSACTION_ROUTE) {
            AddTransactionScreen(onBack = { navController.popBackStack() })
        }
        composable(ADD_SAVINGS_GOAL_ROUTE) {
            AddSavingsGoalScreen(onBack = { navController.popBackStack() })
        }
        composable(
            route = "$SAVINGS_GOAL_DETAIL_ROUTE/{$SAVINGS_GOAL_ID_ARG}",
            arguments = listOf(navArgument(SAVINGS_GOAL_ID_ARG) { type = NavType.LongType })
        ) {
            // goalId flows into SavingsGoalDetailViewModel via Hilt's SavedStateHandle
            // injection, populated automatically from this route's nav argument.
            SavingsGoalDetailScreen(onBack = { navController.popBackStack() })
        }
    }
}
