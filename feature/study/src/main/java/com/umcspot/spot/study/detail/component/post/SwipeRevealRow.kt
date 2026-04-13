//package com.umcspot.spot.study.detail.component.post
//
//import androidx.compose.foundation.gestures.Orientation
//import androidx.compose.foundation.gestures.anchoredDraggable
//import androidx.compose.foundation.gestures.rememberAnchoredDraggableState
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.BoxWithConstraints
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.offset
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.rememberCoroutineScope
//import androidx.compose.runtime.snapshotFlow
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.platform.LocalDensity
//import androidx.compose.ui.unit.Dp
//import androidx.compose.ui.unit.IntOffset
//import androidx.compose.ui.unit.dp
//import kotlinx.coroutines.launch
//import kotlin.math.roundToInt
//
//private enum class RevealState { Closed, Revealed }
//
//@Composable
//fun SwipeRevealRow(
//    revealWidth: Dp,                 // 👈 최대 스와이프 거리(캡)
//    thresholdFraction: Float = 0.5f, // 👈 revealWidth 대비 몇 % 넘으면 액션 실행할지
//    backgroundModifier: Modifier = Modifier,
//    background: @Composable () -> Unit,
//    content: @Composable () -> Unit,
//    onAction: () -> Unit
//) {
//    val density = LocalDensity.current
//    val scope = rememberCoroutineScope()
//
//    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
//        val revealPx = with(density) { revealWidth.toPx() }
//
//        val state = rememberAnchoredDraggableState(
//            initialValue = RevealState.Closed,
//            // revealWidth(=앵커 거리) 대비 임계치
//            positionalThreshold = { totalDistance -> totalDistance * thresholdFraction },
//            velocityThreshold = { with(density) { 600.dp.toPx() } }
//        )
//
//        LaunchedEffect(revealPx) {
//            state.updateAnchors(
//                androidx.compose.foundation.gestures.DraggableAnchors {
//                    RevealState.Closed at 0f
//                    RevealState.Revealed at -revealPx // 👈 EndToStart만(음수)
//                }
//            )
//        }
//
//        // Revealed로 "정착"하면 액션 실행 후 바로 닫기
//        LaunchedEffect(state) {
//            snapshotFlow { state.currentValue }.collect { v ->
//                if (v == RevealState.Revealed) {
//                    onAction()
//                    scope.launch { state.animateTo(RevealState.Closed) }
//                }
//            }
//        }
//
//        // 1) 배경(고정)
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .then(backgroundModifier),
//            contentAlignment = Alignment.CenterEnd
//        ) {
//            background()
//        }
//
//        // 2) 전경(실제로 움직이는 카드/아이템)
//        Box(
//            modifier = Modifier
//                .offset { IntOffset(state.requireOffset().roundToInt(), 0) }
//                .anchoredDraggable(
//                    state = state,
//                    orientation = Orientation.Horizontal,
//                    reverseDirection = false // LTR 기준 우→좌가 음수로 이동
//                )
//                .fillMaxWidth()
//        ) {
//            content()
//        }
//    }
//}
