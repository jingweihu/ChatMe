package link.jingweih.chatme.domain;

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import link.jingweih.chatme.responses.MessageType

@Entity(primaryKeys = ["thread_id", "message_id"])
@Parcelize
data class ChatMessage(
    @ColumnInfo(name = "thread_id")
    val threadId: String,
    @ColumnInfo(name = "message_id")
    var messageId: String = "",
    @ColumnInfo(name = "create_at")
    val createAt: Timestamp? = null,
    val message: String,
    @ColumnInfo(name = "sender_id")
    val senderId: String,
    val type: MessageType
): Parcelable {

    @IgnoredOnParcel
    @Ignore
    @get:Exclude
    var currentProfile: Profile? = null
}
