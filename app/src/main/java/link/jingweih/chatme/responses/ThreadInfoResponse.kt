package link.jingweih.chatme.responses

import com.google.firebase.Timestamp
import link.jingweih.chatme.domain.ChatThread
import link.jingweih.chatme.domain.Profile
import java.util.*

data class ThreadInfoResponse(
    var threadId: String,
    val createAt: Timestamp,
    val members: List<Profile>,
    val lastMessage: MessageResponse?
)