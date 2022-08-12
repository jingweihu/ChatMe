package link.jingweih.chatme.domain

import androidx.room.TypeConverter
import com.google.firebase.Timestamp
import java.util.*

class TimeStampConverter {
    @TypeConverter
    fun fromTimestamp(value: Long?): Timestamp? {
        return value?.let { Timestamp(Date(it)) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Timestamp?): Long? {
        return date?.toDate()?.time
    }
}