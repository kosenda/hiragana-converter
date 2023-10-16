package ksnd.hiraganaconverter.core.data.inappupdate

import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.ktx.requestAppUpdateInfo
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import ksnd.hiraganaconverter.core.testing.MainDispatcherRule
import org.junit.Rule
import org.junit.Test

class InAppUpdateManagerImplTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val appUpdateManager = mockk<AppUpdateManager>(relaxUnitFun = true)
    private val inAppUpdateManager = InAppUpdateManagerImpl(appUpdateManager = appUpdateManager)

    @Test
    fun requestAppUpdateInfo_downloaded_callAlreadyDownloaded() = runTest {
        val appUpdateInfo = mockk<AppUpdateInfo>(relaxUnitFun = true)
        coEvery { appUpdateManager.requestAppUpdateInfo() } returns appUpdateInfo
        coEvery { appUpdateInfo.installStatus() } returns InstallStatus.DOWNLOADED
        var callType: CallType? = null
        inAppUpdateManager.requestUpdate(
            activityResultLauncher = mockk(),
            alreadyDownloaded = { callType = CallType.ALREADY_DOWNLOADED },
            notAvailable = { callType = CallType.NOT_AVAILABLE },
            onFailed = { callType = CallType.ON_FAILED },
        )
        assertThat(callType).isEqualTo(CallType.ALREADY_DOWNLOADED)
        coVerify(exactly = 1) { appUpdateManager.requestAppUpdateInfo() }
    }

    @Test
    fun startInstall_callOnce_callCompleteUpdate() {
        inAppUpdateManager.startInstall()
        verify(exactly = 1) { appUpdateManager.completeUpdate() }
    }

    @Test
    fun registerListener_callOnce_callRegisterListener() {
        inAppUpdateManager.registerListener(mockk())
        verify(exactly = 1) { appUpdateManager.registerListener(any()) }
    }

    @Test
    fun unregisterListener_callOnce_callUnregisterListener() {
        inAppUpdateManager.unregisterListener(mockk())
        verify(exactly = 1) { appUpdateManager.unregisterListener(any()) }
    }

    companion object {
        enum class CallType {
            ALREADY_DOWNLOADED,
            NOT_AVAILABLE,
            ON_FAILED,
        }
    }
}
