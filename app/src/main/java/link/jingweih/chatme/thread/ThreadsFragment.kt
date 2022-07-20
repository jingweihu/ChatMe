package link.jingweih.chatme.thread

import android.view.LayoutInflater
import android.view.ViewGroup
import link.jingweih.chatme.databinding.FragmentThreadsBinding
import link.jingweih.jingwei.core.framework.ui.BaseFragment

class ThreadsFragment : BaseFragment<FragmentThreadsBinding>() {
    override fun createFragmentViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentThreadsBinding {
        return FragmentThreadsBinding.inflate(inflater, container, false)
    }

    override fun initObserver() {
    }

    override fun initView() {
    }
}