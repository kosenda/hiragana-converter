package ksnd.hiraganaconverter.feature.info

import ksnd.hiraganaconverter.core.resource.AppConfig
import org.junit.Test

class InfoViewModelTest {
    private val viewModel = InfoViewModel(appConfig = APP_CONFIG)

    @Test
    fun viewModel_initial_setVersionName() {
        assert(viewModel.versionName == APP_CONFIG.versionName)
    }

    companion object {
        private val APP_CONFIG = AppConfig(
            apiKey = "apiKey",
            applicationId = "applicationId",
            buildType = "buildType",
            isDebug = true,
            versionCode = 1,
            versionName = "versionName",
        )
    }
}
