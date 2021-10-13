package vlad.chetrari.bvtesttask.app.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*

abstract class BaseActivity(
    @LayoutRes contentLayoutId: Int
) : AppCompatActivity(contentLayoutId) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeLiveData()
        lifecycleScope.launchWhenResumed { observeStateFlows() }
    }

    protected open fun observeLiveData() = Unit

    protected open suspend fun observeStateFlows() = Unit

    protected fun <T> LiveData<T>.observe(consumer: (T) -> Unit) =
        observe(this@BaseActivity, Observer { consumer(it) })

    protected fun <T> LiveData<T?>.safeObserve(consumer: (T) -> Unit) =
        observe(this@BaseActivity, Observer { it?.let(consumer) })
}