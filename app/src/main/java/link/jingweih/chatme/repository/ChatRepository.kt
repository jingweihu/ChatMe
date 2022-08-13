package link.jingweih.chatme.repository

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.suspendCancellableCoroutine
import link.jingweih.chatme.database.ChatAppDatabase
import link.jingweih.chatme.domain.ChatMessage
import link.jingweih.chatme.domain.ChatThreadWithMembers
import link.jingweih.chatme.responses.MessageType
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class ChatRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val chatAppDatabase: ChatAppDatabase
) {

    suspend fun sendMessage(
        threadInfo: ChatThreadWithMembers,
        message: String,
        type: MessageType
    ): Boolean {
        val collectionReference =
            firestore.collection("threads").document(threadInfo.chatThread.threadId)
                .collection("messages")
        val messageId = collectionReference.document().id
        val chatMessage = ChatMessage(
            threadId = threadInfo.chatThread.threadId,
            messageId = messageId,
            createAt = Timestamp.now(),
            message = message,
            senderId = threadInfo.senderProfile!!.uid,
            type = type
        )
        return suspendCancellableCoroutine { cont ->
            collectionReference.add(chatMessage).addOnSuccessListener {
                cont.resume(true)
            }.addOnFailureListener {
                cont.resumeWithException(it)
            }
        }
    }
}