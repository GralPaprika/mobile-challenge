package com.example.mobilechallenge.presentation.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.testTag
import com.example.mobilechallenge.R
import com.example.mobilechallenge.ui.theme.Accent
import com.example.mobilechallenge.ui.theme.MobileChallengeTheme
import com.example.mobilechallenge.ui.theme.Primary
import com.example.mobilechallenge.ui.theme.Tertiary

@Composable
fun NoConnectionScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Primary),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.wifi_off),
                contentDescription = "No connection",
                tint = Tertiary,
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .size(64.dp)
                    .testTag("wifi_off_icon")
            )
            
            Text(
                text = "No Internet Connection",
                color = Accent,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
            )
            
            Text(
                text = "Please check your internet connection and try again",
                color = Tertiary,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 12.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NoConnectionScreenPreview() {
    MobileChallengeTheme {
        NoConnectionScreen()
    }
}
