package com.codepep.yps

import com.codepep.yps.dao.RedditHotRepository
import com.codepep.yps.dto.RedditTopLevelData
import com.codepep.yps.model.RedditHotViewModel
import com.codepep.yps.model.ViewModelState
import com.codepep.yps.model.datamanagment.RedditListDataManagement
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
class RedditHotViewModelTest {
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()
    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var mockData: RedditTopLevelData

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        mockData = MockRedditItemsDataProvider.getMockData(RedditListDataManagement.ItemsPerPage)
    }

    @Test
    fun `loads data when first run`() = runTest {
        val mockResponse = mock<Response<RedditTopLevelData>>()
        val mockDataManagement = Mockito.mock(RedditListDataManagement::class.java)
        whenever(mockResponse.body()).thenReturn(mockData)
        val mockRepository = Mockito.mock(RedditHotRepository::class.java)

        val dataResult = mockData.data.children.toMutableList()

        whenever(mockRepository.fetchHotTopics("id")).thenReturn(mockResponse)
        whenever(mockDataManagement.getItems()).thenReturn(dataResult)
        whenever(mockDataManagement.getAfterPageId()).thenReturn("id")

        val viewModel = RedditHotViewModel(mockRepository, testDispatcher, mockDataManagement)

        verify(mockRepository, times(1)).fetchHotTopics("id")
        verify(mockDataManagement, times(1)).addToItems(mockData)
        verify(mockDataManagement, times(1)).getItems()
        Assert.assertEquals("Success state not set!", ViewModelState.SUCCESS(dataResult), viewModel.state.value)
    }

    @Test
    fun `on refresh clears data and fetches topics`() = runTest {
        val mockDataManagement = Mockito.mock(RedditListDataManagement::class.java)
        val mockRepository = Mockito.mock(RedditHotRepository::class.java)

        mockDataManagement.addToItems(mockData)

        val viewModel = RedditHotViewModel(mockRepository, testDispatcher, mockDataManagement)
        viewModel.refresh()

        verify(mockDataManagement, times(1)).onRefresh()
        Assert.assertEquals("Loaded items should be false!!", false, viewModel.hasLoaded())
    }

    @Test
    fun `error state hit when exception thrown`()  = runTest {
        val mockDataManagement = Mockito.mock(RedditListDataManagement::class.java)
        val mockRepository = Mockito.mock(RedditHotRepository::class.java)
        whenever(mockDataManagement.getAfterPageId()).thenReturn("")
        whenever(mockRepository.fetchHotTopics("")).thenThrow(RuntimeException("Error"))
        val viewModel = RedditHotViewModel(mockRepository, testDispatcher, mockDataManagement)
        Assert.assertEquals("Error state not set!", ViewModelState.FAILURE("Error"), viewModel.state.value)
    }

    @Test
    fun `error state hit when data returned is empty`()  = runTest {
        val mockDataManagement = Mockito.mock(RedditListDataManagement::class.java)
        val mockRepository = Mockito.mock(RedditHotRepository::class.java)
        whenever(mockDataManagement.getAfterPageId()).thenReturn("")
        val response = mock<Response<RedditTopLevelData>>()
        whenever(mockRepository.fetchHotTopics("")).thenReturn(response)
        val viewModel = RedditHotViewModel(mockRepository, testDispatcher, mockDataManagement)
        Assert.assertEquals("Error state not set!", ViewModelState.FAILURE("Data is null!"), viewModel.state.value)
    }
}