package ksnd.hiraganaconverter.model.usecase

import ksnd.hiraganaconverter.BuildConfig
import ksnd.hiraganaconverter.model.HiraKanaType
import ksnd.hiraganaconverter.model.TimeFormat
import ksnd.hiraganaconverter.model.getNowTime
import ksnd.hiraganaconverter.model.repository.ConvertHistoryRepository
import ksnd.hiraganaconverter.model.repository.ConvertRepository
import ksnd.hiraganaconverter.model.repository.DataStoreRepository
import java.util.Locale
import javax.inject.Inject

class ConvertTextUseCase @Inject constructor(
    private val convertRepository: ConvertRepository,
    private val dataStoreRepository: DataStoreRepository,
    private val convertHistoryRepository: ConvertHistoryRepository,
) {
    suspend operator fun invoke(inputText: String, timeZone: String, selectedTextType: HiraKanaType): String {
        val isReachedConvertMaxLimit = dataStoreRepository.checkReachedConvertMaxLimit(
            today = getNowTime(timeZone = timeZone, format = TimeFormat.YEAR_MONTH_DATE),
        )
        if (isReachedConvertMaxLimit) throw IsReachedConvertMaxLimitException

        val response = convertRepository.requestConvert(
            sentence = inputText,
            type = selectedTextType.name.lowercase(Locale.ENGLISH),
            appId = BuildConfig.apiKey,
        )

        when {
            response == null -> throw ConversionFailedException
            response.isSuccessful.not() -> throw InterceptorError
            else -> {
                val outputText = response.body()?.converted ?: ""
                convertHistoryRepository.insertConvertHistory(
                    beforeText = inputText,
                    afterText = outputText,
                    time = getNowTime(timeZone = timeZone, format = TimeFormat.YEAR_MONTH_DATE_HOUR_MINUTE),
                )
                return outputText
            }
        }
    }
}
