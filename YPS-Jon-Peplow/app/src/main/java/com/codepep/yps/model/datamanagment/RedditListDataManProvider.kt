package com.codepep.yps.model.datamanagment

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RedditListDataManProvider {
    @Provides
    @Singleton
    fun provideListDataManagement(subBottomLevelDataContInter: SubBottomLevelDataContInter): RedditListDataManagement = RedditListDataManagement(subBottomLevelDataContInter)
}