package ksnd.hiraganaconverter.core.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ksnd.hiraganaconverter.core.domain.repository.ConvertHistoryRepository
import ksnd.hiraganaconverter.core.domain.repository.ConvertRepository
import ksnd.hiraganaconverter.core.domain.repository.DataStoreRepository
import ksnd.hiraganaconverter.core.domain.repository.ReviewInfoRepository
import ksnd.hiraganaconverter.core.model.ui.HiraKanaType
import ksnd.hiraganaconverter.core.resource.AppConfig
import ksnd.hiraganaconverter.core.resource.di.IODispatcher
import java.util.Locale
import javax.inject.Inject

class ConvertTextUseCase @Inject constructor(
    private val convertRepository: ConvertRepository,
    private val dataStoreRepository: DataStoreRepository,
    private val convertHistoryRepository: ConvertHistoryRepository,
    private val reviewInfoRepository: ReviewInfoRepository,
    private val appConfig: AppConfig,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
) {
    suspend operator fun invoke(inputText: String, selectedTextType: HiraKanaType): String = withContext(ioDispatcher) {
        val isReachedConvertMaxLimit = dataStoreRepository.checkIsExceedingMaxLimit()
        if (isReachedConvertMaxLimit) throw IsReachedConvertMaxLimitException

        val response = convertRepository.requestConvert(
            sentence = inputText,
            type = selectedTextType.name.lowercase(Locale.ENGLISH),
            appId = appConfig.apiKey,
        )

        when {
            response == null -> throw ConversionFailedException
            response.isSuccessful.not() -> throw InterceptorError
            else -> {
                val outputText = response.body()?.converted ?: ""
                convertHistoryRepository.insertConvertHistory(
                    beforeText = inputText,
                    afterText = outputText,
                )
                reviewInfoRepository.countUpTotalConvertCount()
                return@withContext outputText
            }
        }
    }
}
