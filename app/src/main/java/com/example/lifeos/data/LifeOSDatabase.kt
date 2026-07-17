package com.example.lifeos.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.lifeos.data.habit.HabitCompletionDao
import com.example.lifeos.data.habit.HabitCompletionEntity
import com.example.lifeos.data.chat.ChatMessageDao
import com.example.lifeos.data.chat.ChatMessageEntity
import com.example.lifeos.data.habit.HabitDao
import com.example.lifeos.data.habit.HabitEntity
import com.example.lifeos.data.jadwal.JadwalDao
import com.example.lifeos.data.jadwal.JadwalEntity
import com.example.lifeos.data.money.BudgetDao
import com.example.lifeos.data.money.BudgetEntity
import com.example.lifeos.data.money.TransactionDao
import com.example.lifeos.data.money.TransactionEntity
import com.example.lifeos.data.notes.ChecklistItemDao
import com.example.lifeos.data.notes.ChecklistItemEntity
import com.example.lifeos.data.notes.NoteDao
import com.example.lifeos.data.notes.NoteEntity
import com.example.lifeos.data.pomodoro.PomodoroSessionDao
import com.example.lifeos.data.pomodoro.PomodoroSessionEntity
import com.example.lifeos.data.puzzle.PuzzleResultDao
import com.example.lifeos.data.puzzle.PuzzleResultEntity
import com.example.lifeos.data.savings.SavingsDepositDao
import com.example.lifeos.data.savings.SavingsDepositEntity
import com.example.lifeos.data.savings.SavingsGoalDao
import com.example.lifeos.data.savings.SavingsGoalEntity
import com.example.lifeos.data.speedmath.SpeedMathResultDao
import com.example.lifeos.data.speedmath.SpeedMathResultEntity
import com.example.lifeos.data.targethidup.TargetHidupDao
import com.example.lifeos.data.targethidup.TargetHidupEntity

@Database(
    entities = [
        NoteEntity::class,
        ChecklistItemEntity::class,
        HabitEntity::class,
        HabitCompletionEntity::class,
        PomodoroSessionEntity::class,
        TransactionEntity::class,
        SavingsGoalEntity::class,
        SavingsDepositEntity::class,
        BudgetEntity::class,
        SpeedMathResultEntity::class,
        PuzzleResultEntity::class,
        JadwalEntity::class,
        TargetHidupEntity::class,
        ChatMessageEntity::class,
    ],
    version = 15,
    exportSchema = false
)
abstract class LifeOSDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
    abstract fun checklistItemDao(): ChecklistItemDao
    abstract fun habitDao(): HabitDao
    abstract fun habitCompletionDao(): HabitCompletionDao
    abstract fun jadwalDao(): JadwalDao
    abstract fun targetHidupDao(): TargetHidupDao
    abstract fun chatMessageDao(): ChatMessageDao
    abstract fun pomodoroSessionDao(): PomodoroSessionDao
    abstract fun transactionDao(): TransactionDao
    abstract fun savingsGoalDao(): SavingsGoalDao
    abstract fun savingsDepositDao(): SavingsDepositDao
    abstract fun budgetDao(): BudgetDao
    abstract fun speedMathResultDao(): SpeedMathResultDao
    abstract fun puzzleResultDao(): PuzzleResultDao

    companion object {
        @Volatile
        private var instance: LifeOSDatabase? = null

        fun getInstance(context: Context): LifeOSDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    LifeOSDatabase::class.java,
                    "lifeos.db"
                ).fallbackToDestructiveMigration().build().also { instance = it }
            }
        }
    }
}
