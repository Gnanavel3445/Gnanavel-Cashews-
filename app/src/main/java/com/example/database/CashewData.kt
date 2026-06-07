package com.example.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "inquiries")
data class Inquiry(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val email: String,
    val phone: String,
    val productGrade: String,
    val message: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "favorites")
data class Favorite(
    @PrimaryKey val gradeCode: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Dao
interface CashewDao {
    @Query("SELECT * FROM inquiries ORDER BY timestamp DESC")
    fun getAllInquiries(): Flow<List<Inquiry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInquiry(inquiry: Inquiry)

    @Query("SELECT * FROM favorites")
    fun getAllFavorites(): Flow<List<Favorite>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(favorite: Favorite)

    @Delete
    suspend fun removeFavorite(favorite: Favorite)

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE gradeCode = :gradeCode)")
    fun isFavorite(gradeCode: String): Flow<Boolean>
}

@Database(entities = [Inquiry::class, Favorite::class], version = 1, exportSchema = false)
abstract class CashewDatabase : RoomDatabase() {
    abstract fun cashewDao(): CashewDao
}

class CashewRepository(private val cashewDao: CashewDao) {
    val inquiries: Flow<List<Inquiry>> = cashewDao.getAllInquiries()
    val favorites: Flow<List<Favorite>> = cashewDao.getAllFavorites()

    suspend fun insertInquiry(inquiry: Inquiry) = cashewDao.insertInquiry(inquiry)
    suspend fun addFavorite(gradeCode: String) = cashewDao.addFavorite(Favorite(gradeCode))
    suspend fun removeFavorite(gradeCode: String) = cashewDao.removeFavorite(Favorite(gradeCode))
    fun isFavorite(gradeCode: String): Flow<Boolean> = cashewDao.isFavorite(gradeCode)
}

object DatabaseClient {
    private var instance: CashewDatabase? = null

    fun getDatabase(context: android.content.Context): CashewDatabase {
        return instance ?: synchronized(this) {
            val db = androidx.room.Room.databaseBuilder(
                context.applicationContext,
                CashewDatabase::class.java,
                "cashew_db"
            ).fallbackToDestructiveMigration().build()
            instance = db
            db
        }
    }
}

