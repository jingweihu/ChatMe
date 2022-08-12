package link.jingweih.chatme.utils

import com.google.gson.Gson

object GsonUtil {

    inline fun <reified T>  toClass(mapObject: Any?): T {
        val gson = Gson()
        return try {
            gson.fromJson(gson.toJsonTree(mapObject), T::class.java)
        } catch (e: Exception) {
            throw e
        }
    }

    inline fun <reified T> stringToClass(string: String): T {
        return try {
            Gson().fromJson(string, T::class.java)
        } catch (e: Exception) {
            throw e
        }
    }
}