package ksnd.hiraganaconverter.model

import android.content.Context
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.ktx.isFlexibleUpdateAllowed
import com.google.android.play.core.ktx.requestAppUpdateInfo
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import javax.inject.Inject

const val DAYS_FOR_FLEXIBLE_UPDATE = 2L

class InAppUpdateManagerImpl @Inject constructor(
    @ApplicationContext context: Context,
) : InAppUpdateManager {
    private val appUpdateManager = AppUpdateManagerFactory.create(context)

    override suspend fun requestUpdate(
        activityResultLauncher: ActivityResultLauncher<IntentSenderRequest>,
        alreadyDownloaded: () -> Unit,
        notAvailable: () -> Unit,
        onFailed: () -> Unit,
    ) {
        runCatching {
            val appUpdateInfo = appUpdateManager.requestAppUpdateInfo()

            if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                alreadyDownloaded()
                return
            }

            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE &&
                (appUpdateInfo.clientVersionStalenessDays() ?: -1) >= DAYS_FOR_FLEXIBLE_UPDATE &&
                appUpdateInfo.isFlexibleUpdateAllowed
            ) {
                appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    activityResultLauncher,
                    AppUpdateOptions.newBuilder(AppUpdateType.FLEXIBLE).build(),
                )
            } else {
                notAvailable()
            }
        }.onFailure {
            Timber.e("Failed requestUpdate: $it")
            onFailed()
        }
    }

    override fun startInstall() {
        runCatching {
            appUpdateManager.completeUpdate()
        }.onFailure {
            Timber.e("Failed startInstall: $it")
        }
    }

    override fun registerListener(listener: InstallStateUpdatedListener) {
        appUpdateManager.registerListener(listener)
    }

    override fun unregisterListener(listener: InstallStateUpdatedListener) {
        appUpdateManager.unregisterListener(listener)
    }
}
