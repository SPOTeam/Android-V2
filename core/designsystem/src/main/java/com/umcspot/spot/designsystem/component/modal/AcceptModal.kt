package com.umcspot.spot.designsystem.component.modal

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.umcspot.spot.designsystem.R
import com.umcspot.spot.designsystem.component.button.SpotActivationButton
import com.umcspot.spot.designsystem.component.button.SpotCancelButton
import com.umcspot.spot.designsystem.shapes.SpotShapes
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.ui.extension.noRippleClickable
import com.umcspot.spot.ui.extension.screenHeightDp
import com.umcspot.spot.ui.extension.screenWidthDp

@Composable
fun AcceptModal(
    painter: Painter,
    painterTint: Color,
    modalTitle: String,
    modalDes: String?,
    okButtonText: String,
    noButtonText: String?,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    onDismiss: () -> Unit = {}
) {
    val isSingleButton = noButtonText == null
    val horizontalPadding = if (isSingleButton) screenWidthDp(68.dp) else 0.dp

    Card(
        modifier = modifier
            .width(screenWidthDp(326.dp))
            .wrapContentHeight(),
        shape = SpotShapes.Round,
        colors = CardDefaults.elevatedCardColors(
            containerColor = SpotTheme.colors.white
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(17.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Image(
                    painter = painterResource(R.drawable.dismiss),
                    contentDescription = null,
                    modifier = Modifier.noRippleClickable { onDismiss() }
                )
            }

            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier.size(screenWidthDp(24.dp)),
                colorFilter = if (painterTint != Color.Unspecified) ColorFilter.tint(painterTint) else null
            )

            Spacer(Modifier.height(screenHeightDp(7.dp)))

            Text(
                modifier = Modifier.height(screenHeightDp(30.dp)),
                text = modalTitle,
                style = SpotTheme.typography.h2,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(screenHeightDp(20.dp)))

            if (modalDes != null) {
                Text(
                    text = modalDes,
                    style = SpotTheme.typography.regular_500,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(screenHeightDp(20.dp)))
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = horizontalPadding),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                SpotActivationButton(
                    modifier = Modifier
                        .weight(1f)
                        .height(screenHeightDp(39.dp)),
                    buttonText = okButtonText,
                    isEnabled = true,
                    onClick = onClick,
                    style = SpotTheme.typography.h5
                )


                if (noButtonText != null) {
                    SpotCancelButton(
                        modifier = Modifier
                            .weight(1f)
                            .height(screenHeightDp(39.dp)),
                        buttonText = noButtonText,
                        onClick = onDismiss
                    )
                }
            }
        }
    }
}

@Composable
fun AcceptDialog(
    visible: Boolean,
    painter: Painter = painterResource(R.drawable.ic_check),
    painterTint: Color = Color.Unspecified,
    modalTitle: String,
    modalDes: String?,
    okButtonText: String,
    noButtonText: String?,
    onClick: () -> Unit,
    onDismiss: () -> Unit,
) {
    if (!visible) return
    Dialog(onDismissRequest = onDismiss) {
        AcceptModal(
            painter = painter,
            painterTint = painterTint,
            modalTitle = modalTitle,
            modalDes = modalDes,
            okButtonText = okButtonText,
            noButtonText = noButtonText,
            onClick = onClick,
            onDismiss = onDismiss,
        )
    }
}