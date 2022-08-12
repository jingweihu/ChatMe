package link.jingweih.chatme.domain

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Ignore
import androidx.room.Junction
import androidx.room.Relation
import com.google.firebase.Timestamp
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class ChatThreadWithMembers(
    @Embedded val chatThread: ChatThread,
    @Relation(
        parentColumn = "thread_id",
        entityColumn = "uid",
        associateBy = Junction(ChatThreadParticipant::class)
    )
    val members: List<Profile>
): Parcelable {

    @IgnoredOnParcel
    @Ignore
    var timestamp: Timestamp = chatThread.lastMessage?.createAt ?: chatThread.createAt

    @IgnoredOnParcel
    @Ignore
    val receiverProfile = members.find { !it.isYourself }

    @IgnoredOnParcel
    @Ignore
    val senderProfile = members.find { it.isYourself }
}