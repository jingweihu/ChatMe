package link.jingweih.chatme.utils

import link.jingweih.chatme.domain.ChatMessage
import java.util.concurrent.TimeUnit
import kotlin.math.abs

object ChatUtil {

    fun shouldShowMessage(messages: List<ChatMessage>, position: Int): Boolean {
        if (position == messages.size - 1) {
            return true
        }
        val prevTime = messages[position + 1].createAt.toDate()
        val currentTime = messages[position].createAt.toDate()
        val miniSDiff = abs(currentTime.time - prevTime.time)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(miniSDiff)
        return minutes >= 5
    }
}