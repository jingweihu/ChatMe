package link.jingweih.chatme.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.ktx.Firebase
import link.jingweih.android.identity.ui.FirebaseLoginActivityContract
import link.jingweih.chatme.MainActivity
import link.jingweih.chatme.R

class SplashActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth

    private val createLoginActivity =
        registerForActivityResult(FirebaseLoginActivityContract()) { result ->
            // parseResult will return this as string?
            if (result) {
                startActivity(Intent(this, MainActivity::class.java))
            }
            finish()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        auth = FirebaseAuth.getInstance()
        if (auth.currentUser == null) {
            createLoginActivity.launch(getString(R.string.app_name))
        } else {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}