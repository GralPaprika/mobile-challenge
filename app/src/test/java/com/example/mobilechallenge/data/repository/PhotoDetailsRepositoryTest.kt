package com.example.mobilechallenge.data.repository

import com.example.mobilechallenge.data.remote.LoremApiService
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever

class PhotoDetailsRepositoryTest {
    
    @Mock
    private lateinit var loremApiService: LoremApiService
    
    private lateinit var photoDetailsRepository: PhotoDetailsRepository
    
    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        photoDetailsRepository = PhotoDetailsRepositoryImpl(loremApiService)
    }
    
    @Test
    fun `getPhotoDescription returns expected text`() = runTest {
        // Arrange
        val expectedDescription = "Lorem ipsum dolor sit amet"
        whenever(loremApiService.getLoremText()).thenReturn(expectedDescription)
        
        // Act
        val result = photoDetailsRepository.getPhotoDescription()
        
        // Assert
        assertEquals(expectedDescription, result)
    }
    
    @Test(expected = RuntimeException::class)
    fun `getPhotoDescription throws exception on API error`() = runTest {
        // Arrange
        whenever(loremApiService.getLoremText()).thenThrow(RuntimeException("API Error"))
        
        // Act
        photoDetailsRepository.getPhotoDescription()
    }
    
    @Test
    fun `getPhotoDescription returns correct texts on multiple calls`() = runTest {
        // Arrange
        val firstDescription = "First description"
        val secondDescription = "Second description"
        whenever(loremApiService.getLoremText()).thenReturn(firstDescription).thenReturn(secondDescription)
        
        // Act
        val result1 = photoDetailsRepository.getPhotoDescription()
        val result2 = photoDetailsRepository.getPhotoDescription()
        
        // Assert
        assertEquals(firstDescription, result1)
        assertEquals(secondDescription, result2)
    }
}
