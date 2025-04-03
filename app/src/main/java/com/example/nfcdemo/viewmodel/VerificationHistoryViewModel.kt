package com.example.nfcdemo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.nfcdemo.network.page.VerificationHistoryPagingSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest

class VerificationHistoryViewModel : ViewModel() {
    private val refreshTrigger = MutableStateFlow(0)
    val pager = refreshTrigger.flatMapLatest {
        Pager(PagingConfig(pageSize = 10)) {
            VerificationHistoryPagingSource()
        }.flow
    }.cachedIn(viewModelScope)

    fun refresh() {
        refreshTrigger.value++
    }
}