package link.jingweih.chatme.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import link.jingweih.chatme.domain.*

@Database(
    entities = [
        Profile::class,
        ChatMessage::class,
        ChatThread::class, ChatThreadParticipant::class,
    ], version = 1, exportSchema = false
)
@TypeConverters(value= [TimeStampConverter::class])
abstract class ChatAppDatabase : RoomDatabase() {

    abstract fun threadDao(): ThreadDao

    abstract fun profileDao(): ProfileDao
}