package link.jingweih.chatme.domain

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class Profile @JvmOverloads constructor (
    val email: String,
    val displayName: String?,
    val photoURL: String?,
    @PrimaryKey
    val uid: String,
    @Ignore
    val isYourself: Boolean = false
): Parcelable