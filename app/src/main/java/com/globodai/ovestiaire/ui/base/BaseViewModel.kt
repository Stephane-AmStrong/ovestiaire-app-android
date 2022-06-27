package com.globodai.ovestiaire.ui.base

import androidx.lifecycle.ViewModel
import com.globodai.ovestiaire.data.repository.BaseRepository

abstract class BaseViewModel(
    private val repository: BaseRepository
) : ViewModel() {

}