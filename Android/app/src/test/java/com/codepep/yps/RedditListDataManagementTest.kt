package com.codepep.yps

import com.codepep.yps.model.datamanagment.RedditListDataManagement
import com.codepep.yps.model.datamanagment.SubBottomLevelDataContInter
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class RedditListDataManagementTest {
    @Mock
    private lateinit var subBottomLevelDataContInter: SubBottomLevelDataContInter

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun `no items added if argument items are empty`() {
        val mockData = MockRedditItemsDataProvider.getMockData(0)
        val redditListDataManagement = RedditListDataManagement(subBottomLevelDataContInter)
        redditListDataManagement.addToItems(mockData)
        verify(subBottomLevelDataContInter, times(0)).add(any())
    }

    @Test
    fun `items added if argument items are not empty`() {
        val mockData = MockRedditItemsDataProvider.getMockData(5)
        val redditListDataManagement = RedditListDataManagement(subBottomLevelDataContInter)
        redditListDataManagement.addToItems(mockData)
        verify(subBottomLevelDataContInter, times(1)).add(mockData.data.children)
    }

    @Test
    fun `page number increased on items added`() {
        val mockData = MockRedditItemsDataProvider.getMockData(5)
        val redditListDataManagement = RedditListDataManagement(subBottomLevelDataContInter)
        var pageNumber = redditListDataManagement.getPageNumber()
        Assert.assertEquals("Page number should be 0!", 0, pageNumber)
        redditListDataManagement.addToItems(mockData)
        pageNumber = redditListDataManagement.getPageNumber()
        Assert.assertEquals("Page number should be 1!", 1, pageNumber)
    }

    @Test
    fun `page offset is calculated when page number is greater than 1`() {
        val mockData = MockRedditItemsDataProvider.getMockData(5)
        val redditListDataManagement = RedditListDataManagement(subBottomLevelDataContInter)
        var pageNumber = redditListDataManagement.getPageNumber()
        Assert.assertEquals("Page number should be 0!", 0, pageNumber)
        redditListDataManagement.addToItems(mockData)
        redditListDataManagement.addToItems(mockData)
        pageNumber = redditListDataManagement.getPageNumber()
        val expectedPageOffset = pageNumber * RedditListDataManagement.ItemsPerPage - RedditListDataManagement.PageOffset
        Assert.assertEquals("Page offset not correct!", expectedPageOffset, redditListDataManagement.getPageOffset())
    }

    @Test
    fun `on refresh clears data members as expected`() {
        val mockData = MockRedditItemsDataProvider.getMockData(5)
        val redditListDataManagement = RedditListDataManagement(subBottomLevelDataContInter)
        redditListDataManagement.addToItems(mockData)
        var pageNumber = redditListDataManagement.getPageNumber()
        Assert.assertEquals("Page number should be 1!", 1, pageNumber)

        redditListDataManagement.onRefresh()

        verify(subBottomLevelDataContInter, times(1)).clear()
        pageNumber = redditListDataManagement.getPageNumber()
        Assert.assertEquals("Page number should be 0!", 0, pageNumber)
    }
}