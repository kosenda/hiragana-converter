package ksnd.hiraganaconverter.feature.info

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ksnd.hiraganaconverter.core.resource.AppConfig
import javax.inject.Inject

@HiltViewModel
class InfoViewModel @Inject constructor(
    appConfig: AppConfig,
) : ViewModel() {
    var versionName = appConfig.versionName
        private set
}
