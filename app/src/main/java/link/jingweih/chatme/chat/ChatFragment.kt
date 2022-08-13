package link.jingweih.chatme.chat

import android.view.KeyEvent
import android.view.KeyEvent.ACTION_DOWN
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import link.jingweih.chatme.databinding.FragmentChatBinding
import link.jingweih.core.ui.toast.toast
import link.jingweih.jingwei.core.framework.ui.BaseFragment

@AndroidEntryPoint
class ChatFragment : BaseFragment<FragmentChatBinding>() {

    private val viewModel: ChatViewModel by viewModels()

    val navArgs by navArgs<ChatFragmentArgs>()

    override fun createFragmentViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentChatBinding {
        return FragmentChatBinding.inflate(inflater, container, false)
    }

    override fun initObserver() {
        viewModel.chatUiState.observe(this) { uiState ->
            when (uiState) {
                is ChatUiState.Failure -> {
                    toast(message = uiState.error ?: "Unknown error")
                }
                is ChatUiState.Send -> {
                    binding.text.apply {
                        setText("")
                        clearFocus()
                    }
                }
            }
        }
    }

    override fun initView() {
        binding.topAppBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        binding.topAppBar.title = navArgs.chatThreadWithMembers.receiverProfile?.email
        binding.text.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                val message = v?.text
                if ((actionId == EditorInfo.IME_ACTION_SEND
                            || event?.action == ACTION_DOWN) && !message.isNullOrBlank()
                ) {
                    viewModel.sendMessage(message.toString(), navArgs.chatThreadWithMembers)
                    return true
                }
                return false
            }
        })
    }
}