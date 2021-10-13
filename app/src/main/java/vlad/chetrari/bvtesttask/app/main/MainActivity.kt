package vlad.chetrari.bvtesttask.app.main

import android.content.res.Resources
import android.os.Bundle
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import vlad.chetrari.bvtesttask.R
import vlad.chetrari.bvtesttask.app.base.BaseActivity
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity(R.layout.activity_main) {

    @Inject
    //checking di works
    lateinit var res: Resources

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //checking di works
        Timber.d("resources = $res")
    }
}