package com.example.lifeos.di

import android.content.Context
import com.example.lifeos.data.LifeOSDatabase
import com.example.lifeos.data.habit.HabitCompletionDao
import com.example.lifeos.data.habit.HabitDao
import com.example.lifeos.data.chat.ChatMessageDao
import com.example.lifeos.data.jadwal.JadwalDao
import com.example.lifeos.data.money.BudgetDao
import com.example.lifeos.data.money.TransactionDao
import com.example.lifeos.data.notes.ChecklistItemDao
import com.example.lifeos.data.notes.NoteDao
import com.example.lifeos.data.pomodoro.PomodoroSessionDao
import com.example.lifeos.data.puzzle.PuzzleResultDao
import com.example.lifeos.data.savings.SavingsDepositDao
import com.example.lifeos.data.savings.SavingsGoalDao
import com.example.lifeos.data.speedmath.SpeedMathResultDao
import com.example.lifeos.data.targethidup.TargetHidupDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Provides the Room database and its DAOs to the Hilt graph.
 *
 * The database delegates to [LifeOSDatabase.getInstance] rather than building a
 * fresh Room instance, so Hilt-injected code and any remaining manually-wired
 * code share the exact same singleton (one connection, one invalidation tracker).
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): LifeOSDatabase =
        LifeOSDatabase.getInstance(context)

    @Provides fun provideNoteDao(db: LifeOSDatabase): NoteDao = db.noteDao()
    @Provides fun provideChecklistItemDao(db: LifeOSDatabase): ChecklistItemDao = db.checklistItemDao()
    @Provides fun provideHabitDao(db: LifeOSDatabase): HabitDao = db.habitDao()
    @Provides fun provideHabitCompletionDao(db: LifeOSDatabase): HabitCompletionDao = db.habitCompletionDao()
    @Provides fun provideJadwalDao(db: LifeOSDatabase): JadwalDao = db.jadwalDao()
    @Provides fun provideTargetHidupDao(db: LifeOSDatabase): TargetHidupDao = db.targetHidupDao()
    @Provides fun provideChatMessageDao(db: LifeOSDatabase): ChatMessageDao = db.chatMessageDao()
    @Provides fun providePomodoroSessionDao(db: LifeOSDatabase): PomodoroSessionDao = db.pomodoroSessionDao()
    @Provides fun provideTransactionDao(db: LifeOSDatabase): TransactionDao = db.transactionDao()
    @Provides fun provideSavingsGoalDao(db: LifeOSDatabase): SavingsGoalDao = db.savingsGoalDao()
    @Provides fun provideSavingsDepositDao(db: LifeOSDatabase): SavingsDepositDao = db.savingsDepositDao()
    @Provides fun provideBudgetDao(db: LifeOSDatabase): BudgetDao = db.budgetDao()
    @Provides fun provideSpeedMathResultDao(db: LifeOSDatabase): SpeedMathResultDao = db.speedMathResultDao()
    @Provides fun providePuzzleResultDao(db: LifeOSDatabase): PuzzleResultDao = db.puzzleResultDao()
}
