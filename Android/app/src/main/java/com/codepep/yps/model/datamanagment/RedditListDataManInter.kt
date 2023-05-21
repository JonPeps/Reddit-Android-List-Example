package com.codepep.yps.model.datamanagment

import com.codepep.yps.dto.RedditSubBottomLevelData
import com.codepep.yps.dto.RedditTopLevelData

interface RedditListDataManInter {
    fun addToItems(item: RedditTopLevelData)
    fun getItems(): MutableList<RedditSubBottomLevelData>
    fun getAfterPageId(): String
    fun getPageNumber(): Int
    fun getPageOffset(): Int
    fun onRefresh()
}