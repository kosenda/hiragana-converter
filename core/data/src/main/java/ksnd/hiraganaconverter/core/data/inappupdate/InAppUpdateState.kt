package ksnd.hiraganaconverter.core.data.inappupdate

sealed class InAppUpdateState {
    object Requesting : InAppUpdateState()
    object NotAvailable : InAppUpdateState()
    data class Downloading(val percentage: Int) : InAppUpdateState()
    object Downloaded : InAppUpdateState()
    object Installing : InAppUpdateState()
    object Canceled : InAppUpdateState()
    object Failed : InAppUpdateState()
}
