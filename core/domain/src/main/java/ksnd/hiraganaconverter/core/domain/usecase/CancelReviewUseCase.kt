package ksnd.hiraganaconverter.core.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ksnd.hiraganaconverter.core.domain.repository.ReviewInfoRepository
import ksnd.hiraganaconverter.core.resource.di.IODispatcher
import javax.inject.Inject

class CancelReviewUseCase @Inject constructor(
    private val reviewInfoRepository: ReviewInfoRepository,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
) {
    suspend operator fun invoke(): Unit = withContext(ioDispatcher) {
        reviewInfoRepository.updateLastRequestReviewLocalDate()
    }
}
