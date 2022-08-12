package link.jingweih.chatme.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.functions.FirebaseFunctionsException
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import link.jingweih.chatme.database.ChatAppDatabase
import link.jingweih.chatme.domain.ChatThreadParticipant
import link.jingweih.chatme.domain.ChatThreadWithMembers
import link.jingweih.chatme.responses.ThreadInfoResponse
import link.jingweih.chatme.responses.ThreadResponse
import link.jingweih.chatme.utils.GsonUtil
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class ThreadRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val chatAppDatabase: ChatAppDatabase
) {

    suspend fun createThread(friendEmail: String): Boolean {
        return suspendCancellableCoroutine { cont ->
            Firebase.functions.getHttpsCallable("createThread")
                .call(hashMapOf("email" to friendEmail))
                .continueWith { task ->
                    val result = task.result?.data!!
//                    GsonUtil.stringToClass<CreateThreadResponse>(result)
                    cont.resume(true)
                }.addOnCompleteListener { task ->
                    val e = task.exception
                    if (e is FirebaseFunctionsException) {
                        cont.resumeWithException(e)
                    }
                }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getThreads(): Flow<List<ThreadResponse>> = callbackFlow {
        val ref = firestore.collection("threads")
            .whereEqualTo("members.${auth.currentUser!!.uid}", true)

        val subscription = ref.addSnapshotListener { snapshot, e ->
            if (e != null) {
                throw e
            }
            val threads = ArrayList<ThreadResponse>()
            for (doc in snapshot!!) {
                threads.add(doc.toObject(ThreadResponse::class.java))
            }
            trySend(threads)
        }

        awaitClose { subscription.remove() }
    }

    suspend fun getThreadInfo(thread: ThreadResponse): ThreadInfoResponse {
        return suspendCancellableCoroutine { cont ->
            Firebase.functions.getHttpsCallable("getThreadInfo")
                .call(hashMapOf("threadId" to thread.threadId))
                .continueWith { task ->
                    val result = task.result?.data!!
                    val threadInfo = GsonUtil.toClass<ThreadInfoResponse>(
                        result
                    ).copy(createAt = thread.createAt)
                    cont.resume(threadInfo)
                }.addOnCompleteListener { task ->
                    val e = task.exception
                    if (e is FirebaseFunctionsException) {
                        cont.resumeWithException(e)
                    }
                }
        }
    }

    fun isThreadReady(threadId: String): Boolean {
        val expiredTime = Date().time - TimeUnit.SECONDS.toMillis(10)
        return chatAppDatabase.threadDao().isThreadReady(expiredTime, threadId)
    }

    fun saveThread(threadResponse: ThreadResponse) {
        chatAppDatabase.threadDao().insertChatThread(threadResponse.toDomainO())
    }

    fun saveThreadInfo(threadInfoResponse: ThreadInfoResponse) {
        val members = threadInfoResponse.members
        val profileDao = chatAppDatabase.profileDao()
        val threadDao = chatAppDatabase.threadDao()
        chatAppDatabase.runInTransaction {
            threadDao.insertChatThread(threadInfoResponse.toDomainO())
            if (members.isNotEmpty()) {
                profileDao.insertProfile(members)
                threadDao.insertThreadParticipants(
                    members.map {
                        ChatThreadParticipant(
                            uid = it.uid,
                            threadId = threadInfoResponse.threadId
                        )
                    }
                )
            }
        }
    }

    fun getChatThreadWithMembers(): List<ChatThreadWithMembers> {
        val uid = auth.currentUser!!.uid
        return chatAppDatabase.threadDao().getThreadWithMembers().map {
            val newMembers = it.members.map { profile ->
                if (profile.uid == uid) {
                    profile.copy(isYourself = true)
                } else {
                    profile
                }
            }
            it.copy(members = newMembers)
        }
    }
}