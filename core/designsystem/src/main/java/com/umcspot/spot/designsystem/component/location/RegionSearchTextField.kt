package com.umcspot.spot.designsystem.component.location

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.umcspot.spot.designsystem.R
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.ui.extension.screenWidthDp

@Composable
fun RegionSearchTextField(
    query: String,
    onQueryChange: (String) -> Unit,
    focusRequester: FocusRequester,
    isFocused: Boolean,
    onFocusChanged: (Boolean) -> Unit,
    onSearchIconClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        textStyle = SpotTheme.typography.h5,
        placeholder = {
            Text(
                text = "OO시, OO구, OO동",
                style = SpotTheme.typography.h5,
                color = SpotTheme.colors.gray300
            )
        },
        trailingIcon = {
            IconButton(
                onClick = onSearchIconClick,
                modifier = Modifier.size(screenWidthDp(48.dp))
            ) {
                Icon(
                    painter = painterResource(R.drawable.search),
                    contentDescription = "검색",
                    modifier = Modifier.size(screenWidthDp(18.dp)),
                    tint = SpotTheme.colors.gray400
                )
            }
        },
        singleLine = true,
        shape = RoundedCornerShape(10.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = SpotTheme.colors.primary,
            unfocusedBorderColor = SpotTheme.colors.gray300,
            cursorColor = SpotTheme.colors.primary,
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent
        ),
        modifier = modifier
            .fillMaxWidth()
            .focusRequester(focusRequester)
            .onFocusChanged { fs -> onFocusChanged(fs.isFocused) }
            .padding(all = screenWidthDp(10.dp))
    )
}