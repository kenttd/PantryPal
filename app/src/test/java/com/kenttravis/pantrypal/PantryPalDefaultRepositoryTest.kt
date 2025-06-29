package com.kenttravis.pantrypal

import com.kenttravis.pantrypal.repository.PantryPalDefaultRepository
import com.kenttravis.pantrypal.sources.local.LocalDataSource
import com.kenttravis.pantrypal.sources.remote.AuthenticateRequest
import com.kenttravis.pantrypal.sources.remote.ErrorAndMessageResponse
import com.kenttravis.pantrypal.sources.remote.RemoteDataSource
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.Assert.*

class PantryPalDefaultRepositoryTest {

    private val localDataSource = mockk<LocalDataSource>()
    private val remoteDataSource = mockk<RemoteDataSource>()
    private val repository = PantryPalDefaultRepository(localDataSource, remoteDataSource)

    @Test
    fun `authenticate should call remote data source`() = runTest {
        // Given
        val request = AuthenticateRequest("user", "pass")
        val expected = ErrorAndMessageResponse(false, "success", null)
        coEvery { remoteDataSource.authenticate(request) } returns expected

        // When
        val result = repository.authenticate(request)

        // Then
        assertEquals(expected, result)
        coVerify { remoteDataSource.authenticate(request) }
    }
}