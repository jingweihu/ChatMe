package link.jingweih.chatme.contact

import android.view.LayoutInflater
import android.view.ViewGroup
import dagger.hilt.android.AndroidEntryPoint
import link.jingweih.chatme.databinding.FragmentContactsBinding
import link.jingweih.jingwei.core.framework.ui.BaseFragment

@AndroidEntryPoint
class ContactsFragment : BaseFragment<FragmentContactsBinding>() {
    override fun createFragmentViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentContactsBinding {
        return FragmentContactsBinding.inflate(inflater, container, false)
    }

    override fun initObserver() {
    }

    override fun initView() {

    }
}