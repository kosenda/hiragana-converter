package ksnd.hiraganaconverter.feature.info.licence

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mikepenz.aboutlibraries.Libs
import com.mikepenz.aboutlibraries.util.withContext
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ksnd.hiraganaconverter.core.resource.di.IODispatcher
import javax.inject.Inject

@HiltViewModel
class LicenseViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {
    private val _libs = MutableStateFlow<Libs?>(null)
    val libs: StateFlow<Libs?> = _libs

    init {
        getLicenses()
    }

    private fun getLicenses() {
        viewModelScope.launch(ioDispatcher) {
            _libs.value = Libs.Builder().withContext(context).build()
        }
    }
}
