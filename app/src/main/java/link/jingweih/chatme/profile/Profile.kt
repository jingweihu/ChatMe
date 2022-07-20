package link.jingweih.chatme.profile

import android.net.Uri
import com.google.firebase.auth.FirebaseUser

data class Profile(
    val email: String,
    val isEmailVerified: Boolean,
    val displayName: String,
    val photoUrl: Uri?,
    val uid: String
) {

    companion object {

        fun fromFirebaseUser(firebaseUser: FirebaseUser): Profile {
            return Profile(
                email = firebaseUser.email ?: "",
                isEmailVerified = firebaseUser.isEmailVerified,
                displayName = firebaseUser.displayName ?: "",
                photoUrl = firebaseUser.photoUrl,
                uid = firebaseUser.uid
            )
        }

    }
}