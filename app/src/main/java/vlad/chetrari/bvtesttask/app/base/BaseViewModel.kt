package vlad.chetrari.bvtesttask.app.base

import androidx.annotation.CallSuper
import androidx.annotation.MainThread
import androidx.lifecycle.*
import timber.log.Timber

abstract class BaseViewModel : ViewModel() {

    val error = actionLiveData<Throwable>()

    protected val <T> LiveData<T>.mutable: MutableLiveData<T>
        get() = this as MutableLiveData<T>

    protected fun <T> mutableLiveData(): LiveData<T> = MutableLiveData()

    protected fun <T> mutableLiveData(initialValue: T): LiveData<T> = MutableLiveData(initialValue)

    @CallSuper
    protected open fun onError(error: Throwable) {
        Timber.e(error)
        this.error.mutable.postValue(error)
    }

    protected inline fun <T> Result<T>.fold(onSuccess: (value: T) -> Unit) = fold(
        onSuccess = onSuccess,
        onFailure = ::onError
    )

    protected fun <T> actionLiveData(): LiveData<T> = object : MutableLiveData<T>() {

        @MainThread
        override fun observe(owner: LifecycleOwner, observer: Observer<in T>) = super.observe(owner, Observer {
            if (it == null) return@Observer
            observer.onChanged(it)
            value = null
        })
    }
}