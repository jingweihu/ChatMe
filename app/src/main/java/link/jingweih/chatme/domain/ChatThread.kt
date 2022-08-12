package link.jingweih.chatme.domain;

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.Timestamp
import kotlinx.parcelize.Parcelize
import java.util.*

@Entity
@Parcelize
data class ChatThread(
    @PrimaryKey
    @ColumnInfo(name = "thread_id")
    val threadId: String,
    @ColumnInfo(name = "create_at")
    val createAt: Timestamp,
    @Embedded(prefix = "last_message_")
    val lastMessage: ChatMessage?,
    @ColumnInfo(name = "update_time")
    val updateTime: Long = Date().time
): Parcelable
