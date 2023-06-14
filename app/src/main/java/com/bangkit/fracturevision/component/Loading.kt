package com.bangkit.fracturevision.component

import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun LoadingCircular(
    modifier: Modifier = Modifier
) {
    CircularProgressIndicator(
        modifier = modifier.size(32.dp),
        color = Color.LightGray,
        strokeWidth = 5.dp
    )
}