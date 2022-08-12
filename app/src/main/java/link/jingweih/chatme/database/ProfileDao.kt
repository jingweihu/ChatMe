package link.jingweih.chatme.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import link.jingweih.chatme.domain.Profile

@Dao
interface ProfileDao {

    @Insert(onConflict = REPLACE)
    fun insertProfile(profiles: List<Profile>)
}