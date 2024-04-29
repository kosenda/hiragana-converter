package ksnd.hiraganaconverter.core.domain.usecase

import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import ksnd.hiraganaconverter.core.domain.repository.ReviewInfoRepository
import ksnd.hiraganaconverter.core.testing.MainDispatcherRule
import org.junit.Rule
import org.junit.Test

class CancelReviewUseCaseTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val reviewInfoRepository = mockk<ReviewInfoRepository>(relaxUnitFun = true)
    private val useCase = CancelReviewUseCase(
        reviewInfoRepository = reviewInfoRepository,
        ioDispatcher = mainDispatcherRule.testDispatcher,
    )

    @Test
    fun invoke_callUpdateLastRequestReviewLocalDate() = runTest {
        useCase()

        coVerify(exactly = 1) { reviewInfoRepository.updateLastRequestReviewLocalDate() }
    }
}
