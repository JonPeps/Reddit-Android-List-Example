package com.codepep.yps.model.datamanagment

import com.codepep.yps.dto.RedditSubBottomLevelData

interface SubBottomLevelDataContInter {
    fun add(items: List<RedditSubBottomLevelData>)
    fun clear()
    fun getItems(): MutableList<RedditSubBottomLevelData>
}