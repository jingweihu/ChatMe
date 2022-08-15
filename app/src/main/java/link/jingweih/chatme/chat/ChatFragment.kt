package link.jingweih.chatme.chat

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.KeyEvent
import android.view.KeyEvent.ACTION_DOWN
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import link.jingweih.chatme.databinding.FragmentChatBinding
import link.jingweih.core.ui.toast.toast
import link.jingweih.jingwei.core.framework.ui.BaseFragment
import javax.inject.Inject

@AndroidEntryPoint
class ChatFragment : BaseFragment<FragmentChatBinding>() {
    private val navArgs by navArgs<ChatFragmentArgs>()

    @Inject
    lateinit var chatViewModelFactory: ChatViewModel.ChatViewModelFactory
    private val viewModel: ChatViewModel by viewModels {
        ChatViewModel.providesFactory(
            assistedFactory = chatViewModelFactory,
            navArgs = navArgs
        )
    }

    private val chatAdapter: ChatAdapter by lazy {
        ChatAdapter()
    }

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
                }
                is ChatUiState.Success -> {
                    chatAdapter.submitList(uiState.messages)
                    Handler(Looper.getMainLooper()).postDelayed(
                        Runnable { binding.recyclerView.smoothScrollToPosition(0) },
                        50
                    )
                }
            }
        }
    }

    override fun initView() {
        binding.topAppBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        binding.topAppBar.title = navArgs.chatThreadWithMembers.receiverProfile?.email
        binding.message.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                val message = v?.text
                if ((actionId == EditorInfo.IME_ACTION_DONE
                            || event?.action == ACTION_DOWN) && !message.isNullOrBlank()
                ) {
                    viewModel.sendMessage(message.toString())
                    hideSoftKeyBoard(v)
                    binding.message.setText("")
                    return true
                }
                return false
            }
        })

        val linearLayoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, true)
        linearLayoutManager.stackFromEnd = true
        binding.recyclerView.apply {
            adapter = chatAdapter
            layoutManager = linearLayoutManager
        }
    }

    private fun hideSoftKeyBoard(v: View) {
        val imm: InputMethodManager =
            v.context.getSystemService(Context.INPUT_METHOD_SERVICE)
                    as InputMethodManager
        imm.hideSoftInputFromWindow(v.windowToken, 0)
    }
}