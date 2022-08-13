package link.jingweih.chatme.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import link.jingweih.chatme.database.ChatAppDatabase
import link.jingweih.chatme.domain.Profile
import javax.inject.Inject


class AccountRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val database: ChatAppDatabase
) {

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getProfile(): Flow<Profile> = callbackFlow {
        val uid = auth.currentUser!!.uid
        val ref = firestore.collection("users").document(uid)
        val subscription = ref.addSnapshotListener { snapshot, e ->
            if (e != null) {
                throw e
            }
            trySend(snapshot?.toObject(Profile::class.java)!!)
        }
        awaitClose { subscription.remove() }
    }

    fun logout() {
        database.clearAllTables()
        auth.signOut()
    }
}