package link.jingweih.chatme.responses

import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseUser
import link.jingweih.chatme.domain.ChatMessage
import link.jingweih.chatme.domain.Profile

data class MessageResponse(
    val createAt: Timestamp,
    val messageId: String,
    val message: String,
    val senderId: String,
    val type: MessageType
) {

    fun toDomainO(threadId: String): ChatMessage {
        return ChatMessage(
            threadId = threadId,
            messageId = messageId,
            createAt = createAt,
            message = message,
            senderId = senderId,
            type = type
        )
    }
}


fun FirebaseUser.toDomainO(isYourself: Boolean): Profile {
    return Profile(
        email = email ?: "",
        displayName = displayName ?: "",
        photoURL = photoUrl?.toString(),
        uid = uid,
        isYourself = isYourself
    )
}