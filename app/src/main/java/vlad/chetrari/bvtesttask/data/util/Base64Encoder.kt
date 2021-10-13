package vlad.chetrari.bvtesttask.data.util

import android.util.Base64
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Base64Encoder @Inject constructor() {

    fun encode(value: String): String = Base64.encodeToString(value.toByteArray(), Base64.NO_WRAP)
}