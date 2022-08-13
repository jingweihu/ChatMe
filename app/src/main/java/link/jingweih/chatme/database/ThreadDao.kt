package link.jingweih.chatme.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import link.jingweih.chatme.domain.ChatThread
import link.jingweih.chatme.domain.ChatThreadParticipant
import link.jingweih.chatme.domain.ChatThreadWithMembers

@Dao
interface ThreadDao {

    @Transaction
    @Query("SELECT * FROM chatthread")
    fun getThreadWithMembers(): Flow<List<ChatThreadWithMembers>>

//    @Query("SELECT EXISTS(SELECT * FROM chatthreadparticipant WHERE thread_id = :threadId)")
//    fun isThreadReady(threadId: String): Boolean

    @Query("SELECT EXISTS(SELECT * FROM chatthread WHERE update_time < :updateTime AND thread_id = :threadId)")
    fun isThreadReady(updateTime: Long, threadId: String): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertThreadParticipants(threadParticipants: List<ChatThreadParticipant>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertChatThread(chatThreads: ChatThread)
}