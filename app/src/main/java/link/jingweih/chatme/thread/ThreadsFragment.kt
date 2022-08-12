package link.jingweih.chatme.thread

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout.HORIZONTAL
import android.widget.LinearLayout.VERTICAL
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import link.jingweih.chatme.R
import link.jingweih.chatme.databinding.FragmentThreadsBinding
import link.jingweih.core.ui.toast.toast
import link.jingweih.jingwei.core.framework.ui.BaseFragment

@AndroidEntryPoint
class ThreadsFragment : BaseFragment<FragmentThreadsBinding>() {
    private val viewModel: ThreadsViewModel by viewModels()
    private val threadAdapter: ThreadAdapter by lazy {
        ThreadAdapter(onTapThread = { chatThreadWithMembers ->
            val direction = ThreadsFragmentDirections.actionThreadsFragmentToChatFragment(chatThreadWithMembers)
            findNavController().navigate(direction)
        })
    }

    override fun createFragmentViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentThreadsBinding {
        return FragmentThreadsBinding.inflate(inflater, container, false)
    }

    override fun initObserver() {
        viewModel.threadsUiState.observe(this) {
            when(it) {
                is ThreadsUiState.Success -> {
                    threadAdapter.submitList(it.threads)
                }
                is ThreadsUiState.Failure -> {
                    toast(it.error ?: "Unknown Error")
                }

            }
        }
    }

    override fun initView() {
        binding.topAppBar.inflateMenu(R.menu.chat_action_bar_menu)
        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.create_chat -> {
                    findNavController().navigate(R.id.action_threadsFragment_to_createThreadFragment)
                    true
                }
                else -> false
            }
        }
        binding.recyclerView.apply {
            adapter = threadAdapter
            layoutManager = LinearLayoutManager(context)
        }
        binding.recyclerView.addItemDecoration(DividerItemDecoration(context, VERTICAL))
    }

}