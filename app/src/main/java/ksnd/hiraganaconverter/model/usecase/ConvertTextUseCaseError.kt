package ksnd.hiraganaconverter.model.usecase

import java.lang.RuntimeException

sealed class ConvertTextUseCaseError : RuntimeException()
object IsReachedConvertMaxLimitException : ConvertTextUseCaseError()
object ConversionFailedException : ConvertTextUseCaseError()
object InterceptorError : ConvertTextUseCaseError()
