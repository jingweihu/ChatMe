package link.jingweih.chatme.thread

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import link.jingweih.chatme.R
import link.jingweih.chatme.databinding.ThreadItemViewBinding
import link.jingweih.chatme.domain.ChatThreadWithMembers
import java.text.SimpleDateFormat

class ThreadAdapter(private val onTapThread: (ChatThreadWithMembers) -> Unit) :
    ListAdapter<ChatThreadWithMembers, ThreadAdapter.ThreadViewHolder>(DIFF_UTIL) {

    companion object {
        val DIFF_UTIL = object : DiffUtil.ItemCallback<ChatThreadWithMembers>() {
            override fun areItemsTheSame(
                oldItem: ChatThreadWithMembers,
                newItem: ChatThreadWithMembers
            ): Boolean {
                return oldItem.chatThread.threadId == newItem.chatThread.threadId
            }

            override fun areContentsTheSame(
                oldItem: ChatThreadWithMembers,
                newItem: ChatThreadWithMembers
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    class ThreadViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val userName: TextView
        val message: TextView
        val timeStamp: TextView
        val image: ImageView

        init {
            // Define click listener for the ViewHolder's View.
            val binding = ThreadItemViewBinding.bind(view)
            userName = binding.username
            message = binding.message
            timeStamp = binding.timeStamp
            image = binding.image
        }
    }

    override fun onBindViewHolder(holder: ThreadViewHolder, position: Int) {
        val data = currentList[position]
        val context = holder.itemView.context
        val profileUrl = data.receiverProfile?.photoURL
        holder.message.text =
            data.chatThread.lastMessage?.message ?: context.getString(R.string.empty_text)
        holder.timeStamp.text = SimpleDateFormat("EEE, d HH:mm").format(data.timestamp.toDate())
        holder.userName.text = data.receiverProfile?.email

        if (!profileUrl.isNullOrBlank()) {
            Glide.with(holder.image).load(profileUrl).circleCrop().into(holder.image)
        } else {
            holder.image.setImageResource(R.drawable.ic_account_circle_24)
        }
        holder.itemView.setOnClickListener{
            onTapThread(data)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ThreadViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.thread_item_view, parent, false)
        return ThreadViewHolder(view)
    }
}
