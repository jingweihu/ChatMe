package link.jingweih.chatme.thread.create

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import link.jingweih.chatme.databinding.FragmentCreateThreadBinding
import link.jingweih.core.ui.toast.toast
import link.jingweih.jingwei.core.framework.ui.BaseBottomSheetFragment

@AndroidEntryPoint
class CreateThreadFragment : BaseBottomSheetFragment<FragmentCreateThreadBinding>() {
    private val viewModel: CreateThreadViewModel by viewModels()

    override fun initView() {
        binding.submit.setOnClickListener {
            val text = binding.friendEmail.text.toString()
            if (text.isNotEmpty()) {
                viewModel.createThread(text)
            }
        }
    }

    override fun initObserver() {
        viewModel.createThreadUiState.observe(this) { uiState ->
            when (uiState) {
                is CreateThreadUiState.Success -> {
                    dismiss()
                }
                is CreateThreadUiState.Failure -> {
                    toast(message = uiState.error ?: "Unknown error")
                }
            }

        }
    }

    override fun createFragmentViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentCreateThreadBinding {
        return FragmentCreateThreadBinding.inflate(inflater, container, false)
    }
}