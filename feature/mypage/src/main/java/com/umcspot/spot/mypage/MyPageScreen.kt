package com.umcspot.spot.mypage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel

@Composable
fun MyPageScreen(
    contentPadding : PaddingValues,
    onParticipatingClick : () -> Unit,
    onMyRecruitingClick : () -> Unit,
    onMyAppliedClick : () -> Unit,
    onEditInterestClick : () -> Unit,
    onEditInterestLocationClick : () -> Unit,
    viewmodel : ViewModel = hiltViewModel()
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "mypage Screen",
            color = Color.Black
        )
    }
}