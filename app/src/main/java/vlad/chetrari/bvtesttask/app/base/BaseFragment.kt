package vlad.chetrari.bvtesttask.app.base

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope

abstract class BaseFragment(@LayoutRes contentLayoutId: Int) : Fragment(contentLayoutId) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeLiveData()
        lifecycleScope.launchWhenCreated {
            observeStateFlows()
        }
    }

    protected open fun observeLiveData() = Unit

    protected open suspend fun observeStateFlows() = Unit

    protected fun <T> LiveData<T>.observe(consumer: (T) -> Unit) =
        observe(viewLifecycleOwner, Observer { consumer(it) })

    protected fun <T> LiveData<T?>.safeObserve(consumer: (T) -> Unit) =
        observe(viewLifecycleOwner, Observer { it?.let(consumer) })
}