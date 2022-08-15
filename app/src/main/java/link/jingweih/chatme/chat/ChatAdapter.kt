package link.jingweih.chatme.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import link.jingweih.chatme.R
import link.jingweih.chatme.domain.ChatMessage
import link.jingweih.chatme.utils.ChatUtil
import java.text.SimpleDateFormat

class ChatAdapter : ListAdapter<ChatMessage, RecyclerView.ViewHolder>(DIFF_UTIL) {

    companion object {
        val DIFF_UTIL = object : DiffUtil.ItemCallback<ChatMessage>() {
            override fun areItemsTheSame(
                oldItem: ChatMessage,
                newItem: ChatMessage
            ): Boolean {
                return oldItem.messageId == newItem.messageId
            }

            override fun areContentsTheSame(
                oldItem: ChatMessage,
                newItem: ChatMessage
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 0) {
            val view = LayoutInflater.from(parent.context).inflate(
                R.layout.chat_receiver_item_view, parent, false
            )
            ReceiverViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(
                R.layout.chat_sender_item_view, parent, false
            )
            SenderViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val data = currentList[position]
        if (holder is SenderViewHolder) {
            Glide.with(holder.image).load(data.currentProfile?.photoURL).fitCenter()
                .into(holder.image)
            holder.timeStamp.text = SimpleDateFormat("EEE, d HH:mm").format(data.createAt.toDate())
            holder.timeStamp.isVisible = ChatUtil.shouldShowMessage(currentList, position)
            holder.message.text = data.message
        } else if (holder is ReceiverViewHolder) {
            Glide.with(holder.image).load(data.currentProfile?.photoURL).fitCenter()
                .into(holder.image)
            holder.timeStamp.text = SimpleDateFormat("EEE, d HH:mm").format(data.createAt.toDate())
            holder.message.text = data.message
            holder.timeStamp.isVisible = ChatUtil.shouldShowMessage(currentList, position)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val profile = currentList[position]?.currentProfile
        return if (profile?.isYourself == false) {
            0
        } else {
            1
        }
    }

    class SenderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView
        val message: TextView
        val timeStamp: TextView

        init {
            message = view.findViewById(R.id.message)
            timeStamp = view.findViewById(R.id.timeStamp)
            image = view.findViewById(R.id.image)
        }
    }

    class ReceiverViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView
        val message: TextView
        val timeStamp: TextView

        init {
            message = view.findViewById(R.id.message)
            timeStamp = view.findViewById(R.id.timeStamp)
            image = view.findViewById(R.id.image)
        }
    }
}