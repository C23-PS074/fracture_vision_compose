package com.bangkit.fracturevision

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.bangkit.fracturevision.model.User
import com.bangkit.fracturevision.preference.UserPreference
import kotlinx.coroutines.launch

class AppViewModel(private val pref: UserPreference) : ViewModel() {
    fun getUser() : LiveData<User> {
        return pref.getUser().asLiveData()
    }
    fun login(user: User) {
        viewModelScope.launch {
            pref.login(user)
        }
    }
    fun logout() {
        viewModelScope.launch {
            pref.logout()
        }
    }
}

class AppViewModelFactory(private val pref: UserPreference) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AppViewModel::class.java)) {
            return AppViewModel(pref) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}