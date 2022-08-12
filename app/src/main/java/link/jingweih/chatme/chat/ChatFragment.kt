package link.jingweih.chatme.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import link.jingweih.chatme.databinding.FragmentChatBinding
import link.jingweih.jingwei.core.framework.ui.BaseFragment

@AndroidEntryPoint
class ChatFragment : BaseFragment<FragmentChatBinding>() {
    val navArgs by navArgs<ChatFragmentArgs>()
    override fun createFragmentViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentChatBinding {
        return FragmentChatBinding.inflate(inflater, container, false)
    }

    override fun initObserver() {
    }

    override fun initView() {
        binding.topAppBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        binding.topAppBar.setTitle(navArgs.chatThreadWithMembers.receiverProfile?.email)
    }
}