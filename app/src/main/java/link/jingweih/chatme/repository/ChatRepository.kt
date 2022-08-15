package link.jingweih.chatme.repository

import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import link.jingweih.chatme.chat.ChatFragmentArgs
import link.jingweih.chatme.database.ChatAppDatabase
import link.jingweih.chatme.domain.ChatMessage
import link.jingweih.chatme.domain.ChatThread
import link.jingweih.chatme.responses.MessageResponse
import link.jingweih.chatme.responses.MessageType
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class ChatRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val chatAppDatabase: ChatAppDatabase,
    private val firebaseAuth: FirebaseAuth,
) {

    suspend fun sendMessage(
        chatThread: ChatThread,
        message: String,
        type: MessageType
    ): Boolean {
        val currentUserId = firebaseAuth.currentUser!!.uid
        val collectionReference =
            firestore.collection("threads").document(chatThread.threadId)
                .collection("messages")
        val messageId = collectionReference.document().id
        val chatMessage = MessageResponse(
            messageId = messageId,
            createAt = Timestamp.now(),
            message = message,
            senderId = currentUserId,
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

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getMessages(chatFragmentArgs: ChatFragmentArgs) = callbackFlow {
        val threadId = chatFragmentArgs.chatThreadWithMembers.chatThread.threadId
        val members = chatFragmentArgs.chatThreadWithMembers.members
        val ref = firestore.collection("threads")
            .document(threadId)
            .collection("messages")
            .orderBy("createAt", Query.Direction.DESCENDING)

        val subscription = ref.addSnapshotListener { snapshot, e ->
            if (e != null) {
                throw e
            }
            val threads = ArrayList<ChatMessage>()
            for (doc in snapshot!!) {
                val messageResponse = doc.toObject(MessageResponse::class.java)
                val chatMessage = messageResponse.toDomainO(threadId)
                chatMessage.currentProfile = members.find { chatMessage.senderId == it.uid }
                threads.add(chatMessage)
            }

            trySend(threads)
        }

        awaitClose { subscription.remove() }
    }
}