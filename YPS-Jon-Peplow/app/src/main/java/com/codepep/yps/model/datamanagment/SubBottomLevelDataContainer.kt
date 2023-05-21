package com.codepep.yps.model.datamanagment

import com.codepep.yps.dto.RedditSubBottomLevelData

class SubBottomLevelDataContainer : SubBottomLevelDataContInter {
    private var listItems: MutableList<RedditSubBottomLevelData> = ArrayList()

    override fun add(items: List<RedditSubBottomLevelData>) {
        listItems.addAll(items)
    }

    override fun clear() {
        listItems.clear()
    }

    override fun getItems(): MutableList<RedditSubBottomLevelData> = listItems
}