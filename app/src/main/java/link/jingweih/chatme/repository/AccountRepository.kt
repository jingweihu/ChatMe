package link.jingweih.chatme.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.suspendCancellableCoroutine
import link.jingweih.chatme.database.ChatAppDatabase
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException


class AccountRepository @Inject constructor(private val auth: FirebaseAuth,
private val database: ChatAppDatabase) {

    suspend fun updateProfile(userProfileChangeRequest: UserProfileChangeRequest): FirebaseUser {
        return suspendCancellableCoroutine { cont ->
            auth.currentUser!!.updateProfile(userProfileChangeRequest)
                .addOnSuccessListener {
                    cont.resume(auth.currentUser!!)
                }.addOnFailureListener {
                    cont.resumeWithException(it)
                }
        }
    }

    fun logout() {
        database.clearAllTables()
        auth.signOut()
    }

}