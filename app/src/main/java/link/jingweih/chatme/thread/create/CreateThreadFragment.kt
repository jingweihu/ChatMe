package link.jingweih.chatme.thread.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import link.jingweih.chatme.R
import link.jingweih.chatme.databinding.FragmentCreateThreadBinding
import link.jingweih.core.ui.toast.toast
import link.jingweih.jingwei.core.framework.exts.applyText
import link.jingweih.jingwei.core.framework.ui.BaseBottomSheetFragment

@AndroidEntryPoint
class CreateThreadFragment : BaseBottomSheetFragment<FragmentCreateThreadBinding>() {
    private val viewModel: CreateThreadViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
    }

    override fun initView() {
        binding.searchEmail.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.createThread(query.orEmpty())
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                binding.errorMessage.isVisible = false
                return false
            }

        })
    }

    override fun initObserver() {
        viewModel.createThreadUiState.observe(this) { uiState ->
            when (uiState) {
                is CreateThreadUiState.Success -> {
                    dismiss()
                }
                is CreateThreadUiState.Failure -> {
                    binding.errorMessage.text = getString(
                        R.string.create_thread_error,
                        uiState.error ?: "Unknown error"
                    )
                    binding.errorMessage.isVisible = true
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