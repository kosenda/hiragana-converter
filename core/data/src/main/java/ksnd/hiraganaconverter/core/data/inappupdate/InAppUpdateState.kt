package ksnd.hiraganaconverter.core.data.inappupdate

sealed class InAppUpdateState {
    data object Requesting : InAppUpdateState()
    data object NotAvailable : InAppUpdateState()
    data class Downloading(val percentage: Int) : InAppUpdateState()
    data object Downloaded : InAppUpdateState()
    data object Installing : InAppUpdateState()
    data object Canceled : InAppUpdateState()
    data object Failed : InAppUpdateState()
}
