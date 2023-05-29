package com.bangkit.fracturevision.navigation

sealed class ViewState {
    object IsLogin: ViewState()
    object NotLogin: ViewState()
    object Loading: ViewState()
}
