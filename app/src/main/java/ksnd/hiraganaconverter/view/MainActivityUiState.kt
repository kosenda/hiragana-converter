package ksnd.hiraganaconverter.view

import ksnd.hiraganaconverter.core.data.inappupdate.InAppUpdateState
import ksnd.hiraganaconverter.core.model.ui.FontType
import ksnd.hiraganaconverter.core.model.ui.Theme

data class MainActivityUiState(
    val theme: Theme = Theme.AUTO,
    val fontType: FontType = FontType.YUSEI_MAGIC,
    val inAppUpdateState: InAppUpdateState = InAppUpdateState.Requesting,
    val isRequestingReview: Boolean = false,
    val isConnectNetwork: Boolean? = null,
)
