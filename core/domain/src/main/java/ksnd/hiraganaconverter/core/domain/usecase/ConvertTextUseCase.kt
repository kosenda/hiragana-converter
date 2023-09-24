package ksnd.hiraganaconverter.core.domain.usecase

import ksnd.hiraganaconverter.core.domain.repository.ConvertHistoryRepository
import ksnd.hiraganaconverter.core.domain.repository.ConvertRepository
import ksnd.hiraganaconverter.core.domain.repository.DataStoreRepository
import ksnd.hiraganaconverter.core.model.ui.HiraKanaType
import ksnd.hiraganaconverter.core.resource.AppConfig
import java.util.Locale
import javax.inject.Inject

class ConvertTextUseCase @Inject constructor(
    private val convertRepository: ConvertRepository,
    private val dataStoreRepository: DataStoreRepository,
    private val convertHistoryRepository: ConvertHistoryRepository,
    private val appConfig: AppConfig,
) {
    suspend operator fun invoke(inputText: String, selectedTextType: HiraKanaType): String {
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
                return outputText
            }
        }
    }
}