package com.thegeekchief.demo

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth

class FirebaseUIActivity : ComponentActivity() {

    // [START auth_fui_create_launcher]
    // See: https://developer.android.com/training/basics/intents/result
    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract(),
    ) { res ->
        this.onSignInResult(res)
    }
    // [END auth_fui_create_launcher]

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_firebase_ui)
        delete()
        createSignInIntent()
    }

    private fun createSignInIntent() {
        // [START auth_fui_create_intent]
        // Choose authentication providers
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build(),
            AuthUI.IdpConfig.MicrosoftBuilder().build(),
            //AuthUI.IdpConfig.PhoneBuilder().build(),
            AuthUI.IdpConfig.FacebookBuilder().build(),
            //AuthUI.IdpConfig.GitHubBuilder().build()
            //AuthUI.IdpConfig.TwitterBuilder().build(),
        )

        // Create and launch sign-in intent
        val signInIntent =
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setLogo(R.drawable.ic_launcher_background) // Set logo drawable
                .setTheme(R.style.Theme_Demo) // Set theme
                .setTosAndPrivacyPolicyUrls(
                    "https://example.com/terms.html",
                    "https://example.com/privacy.html",
                ).build()
        signInLauncher.launch(signInIntent)
        // [END auth_fui_create_intent]
    }

    // [START auth_fui_result]
    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        val response = result.idpResponse
        if (result.resultCode == RESULT_OK) {
            // Successfully signed in
            val mUser = FirebaseAuth.getInstance().currentUser
            mUser?.getIdToken(true)?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val idToken: String? = task.result?.token
                    println(idToken)
                    // ...
                } else {
                    // Handle error -> task.getException();
                }
            }

            // ...
        } else {
            // Sign in failed. If response is null the user canceled the
            // sign-in flow using the back button. Otherwise check
            // response.getError().getErrorCode() and handle the error.
            // ...
        }
    }
    // [END auth_fui_result]

    private fun signOut() {
        // [START auth_fui_signout]
        AuthUI.getInstance().signOut(this).addOnCompleteListener {
            // ...
        }
        // [END auth_fui_signout]
    }

    private fun delete() {
        // [START auth_fui_delete]
        AuthUI.getInstance().delete(this).addOnCompleteListener {
            // ...
        }
        // [END auth_fui_delete]
    }

}