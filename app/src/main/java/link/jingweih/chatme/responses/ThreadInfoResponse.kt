package link.jingweih.chatme.responses

import com.google.firebase.Timestamp
import link.jingweih.chatme.domain.ChatThread
import link.jingweih.chatme.domain.Profile

data class ThreadInfoResponse(
    var threadId: String,
    val createAt: Timestamp,
    val members: List<Profile>,
    val lastMessage: MessageResponse?
) {

    fun toDomainO(): ChatThread {
        return ChatThread(
            threadId = threadId,
            createAt = createAt,
            lastMessage = lastMessage?.toDomainO(threadId)
        )
    }
}