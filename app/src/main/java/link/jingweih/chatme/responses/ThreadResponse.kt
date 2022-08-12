package link.jingweih.chatme.responses

import com.google.firebase.Timestamp
import com.google.gson.annotations.SerializedName
import link.jingweih.chatme.domain.ChatThread

data class ThreadResponse(
    var threadId: String = "",
    var createAt: Timestamp = Timestamp.now(),
    var lastMessage: MessageResponse? = null
) {

    fun toDomainO(): ChatThread {
        return ChatThread(
            threadId = threadId,
            createAt = createAt,
            lastMessage = lastMessage?.toDomainO(threadId)
        )
    }
}