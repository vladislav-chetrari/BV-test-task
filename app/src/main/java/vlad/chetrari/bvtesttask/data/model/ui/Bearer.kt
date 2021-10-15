package vlad.chetrari.bvtesttask.data.model.ui

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Bearer(val token: String) : Parcelable
