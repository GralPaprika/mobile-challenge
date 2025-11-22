package com.example.mobilechallenge.domain.usecase

import app.cash.turbine.test
import com.example.mobilechallenge.data.repository.PhotoDetailsRepository
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GetPhotoDescriptionUseCaseTest {
    
    @Mock
    private lateinit var photoDetailsRepository: PhotoDetailsRepository
    
    private lateinit var getPhotoDescriptionUseCase: GetPhotoDescriptionUseCase
    
    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        getPhotoDescriptionUseCase = GetPhotoDescriptionUseCaseImpl(photoDetailsRepository)
    }
    
    @Test
    fun `invoke emits success result with description`() = runTest {
        // Arrange
        val expectedDescription = "Lorem ipsum dolor sit amet, consectetur adipiscing elit"
        whenever(photoDetailsRepository.getPhotoDescription()).thenReturn(expectedDescription)
        
        // Act & Assert
        getPhotoDescriptionUseCase.invoke().test {
            val result = awaitItem()
            assertTrue(result.isSuccess)
            assertEquals(expectedDescription, result.getOrNull())
            awaitComplete()
        }
    }
    
    @Test
    fun `invoke emits failure result on exception`() = runTest {
        // Arrange
        val exception = RuntimeException("Network error")
        whenever(photoDetailsRepository.getPhotoDescription()).thenThrow(exception)
        
        // Act & Assert
        getPhotoDescriptionUseCase.invoke().test {
            val result = awaitItem()
            assertTrue(result.isFailure)
            assertEquals(exception, result.exceptionOrNull())
            awaitComplete()
        }
    }
    
    @Test
    fun `invoke emits only one value`() = runTest {
        // Arrange
        val expectedDescription = "Test description"
        whenever(photoDetailsRepository.getPhotoDescription()).thenReturn(expectedDescription)
        
        // Act & Assert
        getPhotoDescriptionUseCase.invoke().test {
            awaitItem() // Consume the success item
            awaitComplete() // Expect completion without more items
        }
    }
    
    @Test
    fun `multiple invocations emit correct results`() = runTest {
        // Arrange
        val firstDescription = "First"
        val secondDescription = "Second"
        whenever(photoDetailsRepository.getPhotoDescription())
            .thenReturn(firstDescription)
            .thenReturn(secondDescription)
        
        // Act & Assert - First invocation
        getPhotoDescriptionUseCase.invoke().test {
            val result1 = awaitItem()
            assertTrue(result1.isSuccess)
            assertEquals(firstDescription, result1.getOrNull())
            awaitComplete()
        }
        
        // Act & Assert - Second invocation
        getPhotoDescriptionUseCase.invoke().test {
            val result2 = awaitItem()
            assertTrue(result2.isSuccess)
            assertEquals(secondDescription, result2.getOrNull())
            awaitComplete()
        }
    }
}
