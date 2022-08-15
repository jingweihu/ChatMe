package link.jingweih.chatme.domain

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.firebase.firestore.Exclude
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class Profile (
    var email: String = "",
    var displayName: String? = null,
    var photoURL: String? = null,
    @PrimaryKey
    var uid: String = "",
): Parcelable {

    @Ignore
    @get:Exclude
    var isYourself: Boolean = false
}