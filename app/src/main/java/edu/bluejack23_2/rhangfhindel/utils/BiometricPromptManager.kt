package edu.bluejack23_2.rhangfhindel.utils

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.biometric.BiometricPrompt.PromptInfo
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

class BiometricPromptManager(private val activity: AppCompatActivity) {

    private val resultChannel = Channel<BiometricResult>()
    val promptResults = resultChannel.receiveAsFlow()

    fun showBiometricPrompt(title: String, description: String) {
        val biometricManager = BiometricManager.from(activity)
        val authenticators = if (Build.VERSION.SDK_INT >= 30) {
            BIOMETRIC_STRONG or DEVICE_CREDENTIAL
        } else BIOMETRIC_STRONG

        val promptInfo = PromptInfo.Builder()
            .setTitle(title)
            .setDescription(description)
            .setAllowedAuthenticators(authenticators)
            .setConfirmationRequired(false)

        if (Build.VERSION.SDK_INT < 30) {
            promptInfo.setNegativeButtonText("Cancel")
        }

        when (biometricManager.canAuthenticate(authenticators)) {
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                resultChannel.trySend(BiometricResult.HardwareUnavailable)
                return
            }

            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                resultChannel.trySend(BiometricResult.FeatureUnavailable)
                return
            }

            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                resultChannel.trySend(BiometricResult.AuthenticationNotSet)
                return
            }

            else -> Unit
        }

        val prompt = BiometricPrompt(activity, object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                resultChannel.trySend(BiometricResult.AuthenticationError)
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                resultChannel.trySend(BiometricResult.AuthenticationSuccess)
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                resultChannel.trySend(BiometricResult.AuthenticationFailed)
            }
        })

        prompt.authenticate(promptInfo.build())
    }

    enum class BiometricResult {
        HardwareUnavailable,
        FeatureUnavailable,
        AuthenticationFailed,
        AuthenticationSuccess,
        AuthenticationNotSet,
        AuthenticationError
    }

}