package com.example.nfcdemo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.nfcdemo.network.page.VerificationHistoryPagingSource

class VerificationHistoryViewModel : ViewModel() {

    val pager = Pager(PagingConfig(pageSize = 10)) {
        VerificationHistoryPagingSource()
    }.flow.cachedIn(viewModelScope)


}