package link.jingweih.chatme.domain

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(primaryKeys = ["uid", "thread_id"])
data class ChatThreadParticipant(
    val uid: String,
    @ColumnInfo(name= "thread_id")
    val threadId: String
)