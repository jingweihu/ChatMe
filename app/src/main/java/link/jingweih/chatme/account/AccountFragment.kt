package link.jingweih.chatme.account

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import link.jingweih.chatme.databinding.FragmentAccountBinding
import link.jingweih.chatme.splash.SplashActivity
import link.jingweih.core.ui.toast.toast
import link.jingweih.jingwei.core.framework.ui.BaseFragment

@AndroidEntryPoint
class AccountFragment : BaseFragment<FragmentAccountBinding>() {
    private val viewModel: AccountViewModel by viewModels()

    override fun createFragmentViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAccountBinding {
        return FragmentAccountBinding.inflate(inflater, container, false)
    }

    override fun initObserver() {
        viewModel.accountUiState.observe(this) { uiState ->
            when (uiState) {
                is AccountUiState.Success -> {
                    binding.displayName.setText(uiState.profile.displayName)
                    binding.email.text = uiState.profile.email
                    Glide.with(this).load(uiState.profile.photoURL).fitCenter()
                        .into(binding.photoImage)
                }
                is AccountUiState.Logout -> {
                    activity?.apply {
                        startActivity(Intent(this, SplashActivity::class.java))
                        finish()
                    }
                }
                is AccountUiState.Failure -> {
                    toast(message = uiState.error ?: "Unknown error")
                }
            }
        }
    }

    override fun initView() {

        binding.logout.setOnClickListener {
            viewModel.logout()
        }
    }
}