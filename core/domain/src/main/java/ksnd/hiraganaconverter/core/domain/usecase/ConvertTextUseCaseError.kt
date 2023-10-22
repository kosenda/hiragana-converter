package ksnd.hiraganaconverter.core.domain.usecase

import ksnd.hiraganaconverter.core.model.ui.ConvertErrorType
import timber.log.Timber

sealed class ConvertTextUseCaseError : RuntimeException()
data object IsReachedConvertMaxLimitException : ConvertTextUseCaseError()
data object ConversionFailedException : ConvertTextUseCaseError()
data object InterceptorError : ConvertTextUseCaseError()

fun Throwable.toConvertErrorType(): ConvertErrorType = when (this) {
    is IsReachedConvertMaxLimitException -> ConvertErrorType.REACHED_CONVERT_MAX_LIMIT
    is ConversionFailedException -> ConvertErrorType.CONVERSION_FAILED
    is InterceptorError -> when (this.message) {
        ConvertErrorType.TOO_MANY_CHARACTER.name -> ConvertErrorType.TOO_MANY_CHARACTER
        ConvertErrorType.RATE_LIMIT_EXCEEDED.name -> ConvertErrorType.RATE_LIMIT_EXCEEDED
        ConvertErrorType.CONVERSION_FAILED.name -> ConvertErrorType.CONVERSION_FAILED
        ConvertErrorType.INTERNAL_SERVER.name -> ConvertErrorType.INTERNAL_SERVER
        ConvertErrorType.NETWORK.name -> ConvertErrorType.NETWORK
        else -> {
            Timber.w("InterceptorError: %s".format(this.message))
            ConvertErrorType.CONVERSION_FAILED
        }
    }
    else -> {
        Timber.e("Not defined ConvertTextUseCaseException! throwable: $this")
        ConvertErrorType.CONVERSION_FAILED
    }
}
