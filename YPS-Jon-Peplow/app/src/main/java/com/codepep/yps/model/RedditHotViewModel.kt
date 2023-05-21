package com.codepep.yps.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codepep.yps.dao.RedditHotRepository
import com.codepep.yps.model.datamanagment.RedditListDataManagement
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class RedditHotViewModel@Inject constructor(
private val repository: RedditHotRepository,
private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
private val dataManagement: RedditListDataManagement
): ViewModel() {
    val state = MutableStateFlow<ViewModelState>(ViewModelState.LOADING)
    private var hasLoaded = false

    init {
        loadHotTopics()
    }

    fun hasLoaded(): Boolean = hasLoaded

    fun pageOffset(): Int = dataManagement.getPageOffset()

    fun refresh() {
        hasLoaded = false
        dataManagement.onRefresh()
        loadHotTopics()
    }

    fun loadHotTopics() = viewModelScope.launch {
        try {
            state.value = ViewModelState.LOADING
            val item = withContext(ioDispatcher) {
                val nextAfterPage = dataManagement.getAfterPageId()
                repository.fetchHotTopics(nextAfterPage)
            }
            val data = item.body()
            data?.let {
                dataManagement.addToItems(it)
                state.value = ViewModelState.SUCCESS(dataManagement.getItems())
                hasLoaded = true
            }?: run {
                state.value = ViewModelState.FAILURE("Data is null!")
            }
        } catch (e: Exception) {
            state.value = ViewModelState.FAILURE(e.message)
        }
    }
}