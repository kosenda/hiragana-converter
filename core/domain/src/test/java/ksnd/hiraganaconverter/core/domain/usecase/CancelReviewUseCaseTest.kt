package ksnd.hiraganaconverter.core.domain.usecase

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.test.runTest
import ksnd.hiraganaconverter.core.domain.repository.ReviewInfoRepository
import ksnd.hiraganaconverter.core.testing.MainDispatcherRule
import org.junit.Rule
import org.junit.Test

class CancelReviewUseCaseTest {
    @get: Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val reviewInfoRepository = mockk<ReviewInfoRepository>(relaxUnitFun = true)
    private val useCase = CancelReviewUseCase(
        reviewInfoRepository = reviewInfoRepository,
        ioDispatcher = mainDispatcherRule.testDispatcher,
    )

    @Test
    fun useCase_callUpdateLastRequestReviewLocalDate() = runTest {
        coEvery { reviewInfoRepository.updateLastRequestReviewLocalDate() } just runs
        useCase()
        coVerify(exactly = 1) { reviewInfoRepository.updateLastRequestReviewLocalDate() }
    }
}
