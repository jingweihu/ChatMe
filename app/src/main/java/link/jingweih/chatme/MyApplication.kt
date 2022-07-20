package link.jingweih.chatme

import android.app.Application
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp
import link.jingweih.android.flipper.Flipper

@HiltAndroidApp
class MyApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        initFlipper()
    }

    private fun initFlipper() {
        val sharePreferences = mapOf(
            packageName + "_preferences" to MODE_PRIVATE
        )
        Flipper.init(this) {
            addBasePlugin()
            addDatabasesPlugin()
            addNetworkPlugin(true)
            addSharePreferencesPlugin(sharePreferences)
        }
    }
}