package vlad.chetrari.bvtesttask.base

import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity(
    @LayoutRes contentLayoutId: Int
) : AppCompatActivity(contentLayoutId) {
}