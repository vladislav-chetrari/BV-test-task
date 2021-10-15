package vlad.chetrari.bvtesttask.data.model.ui

sealed class Result<out T> {
    data class Success<out R>(val value: R) : Result<R>()
    data class Error(val throwable: Throwable) : Result<Nothing>()
    object Progress : Result<Nothing>()

    val isComplete: Boolean
        get() = this !is Progress
}