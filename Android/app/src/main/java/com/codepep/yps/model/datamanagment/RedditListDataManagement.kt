package com.codepep.yps.model.datamanagment

import com.codepep.yps.dto.RedditSubBottomLevelData
import com.codepep.yps.dto.RedditTopLevelData
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject

@Module
@InstallIn(SingletonComponent::class)
class RedditListDataManagement@Inject constructor(
    private val subBottomLevelDataContInter: SubBottomLevelDataContInter
) : RedditListDataManInter {
    private var nextPageId: String = ""
    private var pageNumber: Int = 0
    private var pageOffset: Int = 0

    override fun addToItems(item: RedditTopLevelData) {
        if (item.data.children.isEmpty()) {
            return
        }
        subBottomLevelDataContInter.add(item.data.children)
        nextPageId = item.data.after
        pageNumber++
        pageOffset = if (pageNumber == 1) {
            0
        } else {
            (pageNumber * ItemsPerPage) - PageOffset
        }
    }

    override fun getItems(): MutableList<RedditSubBottomLevelData> = subBottomLevelDataContInter.getItems()
    override fun getAfterPageId(): String = nextPageId
    override fun getPageNumber(): Int = pageNumber

    override fun getPageOffset(): Int = pageOffset
    override fun onRefresh() {
        subBottomLevelDataContInter.clear()
        pageNumber = 0
    }

    companion object {
        const val ItemsPerPage = 25
        const val PageOffset = 20
    }
}