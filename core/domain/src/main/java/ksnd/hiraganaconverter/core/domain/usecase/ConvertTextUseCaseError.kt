package ksnd.hiraganaconverter.core.domain.usecase

import java.lang.RuntimeException

sealed class ConvertTextUseCaseError : RuntimeException()
// TODO object と data object の違いを調べて対処する
object IsReachedConvertMaxLimitException : ConvertTextUseCaseError()
object ConversionFailedException : ConvertTextUseCaseError()
object InterceptorError : ConvertTextUseCaseError()
