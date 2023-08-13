package ksnd.hiraganaconverter.model

import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import com.google.android.play.core.install.InstallStateUpdatedListener

interface InAppUpdateManager {
    suspend fun requestUpdate(
        activityResultLauncher: ActivityResultLauncher<IntentSenderRequest>,
        alreadyDownloaded: () -> Unit,
        notAvailable: () -> Unit,
        onFailed: () -> Unit,
    )
    fun startInstall()
    fun registerListener(listener: InstallStateUpdatedListener)
    fun unregisterListener(listener: InstallStateUpdatedListener)
}
