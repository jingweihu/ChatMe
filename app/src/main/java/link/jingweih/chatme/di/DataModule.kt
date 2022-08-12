package link.jingweih.chatme.di;

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import link.jingweih.chatme.database.ChatAppDatabase

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    fun providesFirestoreDb(): FirebaseFirestore {
        val firestore = Firebase.firestore
        firestore.firestoreSettings = firestoreSettings {
            cacheSizeBytes = FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED
        }
        return firestore
    }

    @Provides
    fun providesDatabase(@ApplicationContext context: Context): ChatAppDatabase {
        return Room.databaseBuilder(
            context,
            ChatAppDatabase::class.java, "chat-db"
        ).build()
    }
}
