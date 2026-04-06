package com.umcspot.spot.study.detail.screen.camera

import androidx.activity.compose.BackHandler
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import com.umcspot.spot.designsystem.theme.B400
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.designsystem.theme.Y400
import com.umcspot.spot.study.detail.model.QrScanState

@Composable
fun QrScannerScreen(
    onQrScanned: (String) -> Unit,
    onClose: () -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }

    var scanState by remember { mutableStateOf(QrScanState.DEFAULT) }
    val isProcessedRef = remember { mutableStateOf(false) }

    val scannerBoxSize = 280.dp

    val strokeColor = when (scanState) {
        QrScanState.DEFAULT -> SpotTheme.colors.white
        QrScanState.SCANNING -> SpotTheme.colors.B400
        QrScanState.ERROR -> SpotTheme.colors.Y400
    }

    val guideText = when (scanState) {
        QrScanState.DEFAULT -> "QR코드를 사각형 안에 맞춰주세요."
        QrScanState.SCANNING -> "QR코드를 읽고 있습니다."
        QrScanState.ERROR -> "다시 시도해주세요."
    }

    BackHandler { onClose() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SpotTheme.colors.black)
    ) {
        CameraPreview(
            cameraProviderFuture = cameraProviderFuture,
            lifecycleOwner = lifecycleOwner,
            isProcessedRef = isProcessedRef,
            onQrDetected = { scanState = QrScanState.SCANNING },
            onQrScanned = { token ->
                isProcessedRef.value = true
                onQrScanned(token)
            },
            onQrError = {
                scanState = QrScanState.ERROR
                isProcessedRef.value = false
            }
        )

        ScannerGuidelineOverlay(
            boxSize = scannerBoxSize,
            strokeColor = strokeColor
        )

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = guideText,
                color = SpotTheme.colors.white,
                style = SpotTheme.typography.regular_500,
                modifier = Modifier
                    .offset(y = scannerBoxSize / 2 + 16.dp)
                    .background(
                        color = SpotTheme.colors.black.copy(alpha = 0.8f),
                        shape = RoundedCornerShape(6.dp)
                    )
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
    }
}

@Composable
private fun CameraPreview(
    cameraProviderFuture: com.google.common.util.concurrent.ListenableFuture<ProcessCameraProvider>,
    lifecycleOwner: androidx.lifecycle.LifecycleOwner,
    isProcessedRef: androidx.compose.runtime.MutableState<Boolean>,
    onQrDetected: () -> Unit,
    onQrScanned: (String) -> Unit,
    onQrError: () -> Unit
) {
    AndroidView(
        factory = { ctx ->
            val previewView = PreviewView(ctx).apply {
                implementationMode = PreviewView.ImplementationMode.COMPATIBLE
            }
            val executor = ContextCompat.getMainExecutor(ctx)

            cameraProviderFuture.addListener({
                val cameraProvider = cameraProviderFuture.get()
                val preview = Preview.Builder().build().also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }
                val scanner = BarcodeScanning.getClient()

                val imageAnalyzer = ImageAnalysis.Builder()
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()
                    .also { analysis ->
                        analysis.setAnalyzer(executor) { imageProxy ->
                            val mediaImage = imageProxy.image
                            if (mediaImage != null && !isProcessedRef.value) {
                                val image = InputImage.fromMediaImage(
                                    mediaImage,
                                    imageProxy.imageInfo.rotationDegrees
                                )
                                scanner.process(image)
                                    .addOnSuccessListener { barcodes ->
                                        if (barcodes.isNotEmpty()) {
                                            onQrDetected()
                                            barcodes.first().rawValue?.let { token ->
                                                onQrScanned(token)
                                            }
                                        }
                                    }
                                    .addOnFailureListener { onQrError() }
                                    .addOnCompleteListener { imageProxy.close() }
                            } else {
                                imageProxy.close()
                            }
                        }
                    }

                runCatching {
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        CameraSelector.DEFAULT_BACK_CAMERA,
                        preview,
                        imageAnalyzer
                    )
                }
            }, executor)
            previewView
        },
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
private fun ScannerGuidelineOverlay(
    boxSize: Dp,
    strokeColor: Color,
    overlayColor: Color = Color.Black.copy(alpha = 0.6f),
    strokeWidth: Dp = 3.dp,
    cornerRadius: Dp = 16.dp
) {
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer(alpha = 0.99f)
    ) {
        val boxSizePx = boxSize.toPx()
        val cornerRadiusPx = cornerRadius.toPx()
        val left = (size.width - boxSizePx) / 2
        val top = (size.height - boxSizePx) / 2

        drawRect(color = overlayColor)

        drawRoundRect(
            color = Color.Transparent,
            topLeft = Offset(left, top),
            size = Size(boxSizePx, boxSizePx),
            cornerRadius = CornerRadius(cornerRadiusPx, cornerRadiusPx),
            blendMode = BlendMode.Clear
        )

        drawRoundRect(
            color = strokeColor,
            topLeft = Offset(left, top),
            size = Size(boxSizePx, boxSizePx),
            cornerRadius = CornerRadius(cornerRadiusPx, cornerRadiusPx),
            style = Stroke(width = strokeWidth.toPx())
        )
    }
}