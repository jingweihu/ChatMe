package link.jingweih.chatme.responses

import com.google.firebase.Timestamp
import link.jingweih.chatme.domain.ChatMessage

data class MessageResponse(
    var createAt: Timestamp = Timestamp.now(),
    var messageId: String = "",
    var message: String = "",
    var senderId: String = "",
    var type: MessageType = MessageType.MESSAGE
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