package com.example.book_madness.ui.timer

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.book_madness.R
import com.example.book_madness.ui.AppViewModelFactoryProvider
import com.example.book_madness.ui.navigation.BookMadnessTitlesResId
import com.example.book_madness.util.BookMadnessTopAppBar

@Composable
fun CountDownTimerScreen(
    modifier: Modifier = Modifier,
    bottomNavigationBar: @Composable () -> Unit,
    viewModel: CountTimerViewModel = viewModel(factory = AppViewModelFactoryProvider.Factory),
) {
    val time by viewModel.time
    val progress by viewModel.progress
    val isPlaying by viewModel.isPlaying
    val celebrate by viewModel.celebrate

    Scaffold(
        topBar = {
            BookMadnessTopAppBar(
                title = stringResource(BookMadnessTitlesResId.COUNT_DOWN_TIMER_SCREEN),
                canNavigateBack = false
            )
        },
        bottomBar = { bottomNavigationBar() }
    )
    { innerPadding ->
        CountDownBody(
            time = time,
            progress = progress,
            isPlaying = isPlaying,
            celebrate = celebrate,
            modifier = modifier.padding(innerPadding)
        ) {
            viewModel.handleCountDownTimer()
        }
    }
}

@Composable
fun CountDownBody(
    time: String,
    progress: Float,
    isPlaying: Boolean,
    celebrate: Boolean,
    modifier: Modifier = Modifier,
    optionSelected: () -> Unit,
) {
    Box(modifier = Modifier) {
        if (celebrate)
            ShowCelebrationAnimation()
        Column(
            modifier = modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Text(
                text = stringResource(id = R.string.timer_title),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineLarge
            )
            Text(
                text = stringResource(id = R.string.enjoy),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = stringResource(id = R.string.timer_description),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium
            )
            CountDownIndicator(
                progress = progress,
                time = time,
                modifier = Modifier.padding(dimensionResource(id = R.dimen.extra_large))
            )
            CountDownButton(
                isPlaying = isPlaying
            ) {
                optionSelected()
            }
        }
    }
}

@Composable
fun CountDownIndicator(
    progress: Float,
    time: String,
    modifier: Modifier = Modifier,
) {
    val size = 250
    val stroke = 18
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec,
        label = ""
    )

    Column(modifier = modifier) {
        Box {
            CircularProgressIndicatorBackGround(
                color = Color.Gray,
                stroke = stroke,
                modifier = Modifier
                    .height(size.dp)
                    .width(size.dp),
            )
            CircularProgressIndicator(
                progress = animatedProgress,
                modifier = Modifier
                    .height(size.dp)
                    .width(size.dp),
                strokeWidth = stroke.dp,
            )

            Column(modifier = Modifier.align(Alignment.Center)) {
                Text(
                    text = time,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}

@Composable
fun CircularProgressIndicatorBackGround(
    color: Color,
    stroke: Int,
    modifier: Modifier = Modifier,
) {
    val style = with(LocalDensity.current) { Stroke(stroke.dp.toPx()) }
    Canvas(
        modifier = modifier,
        onDraw = {
            val innerRadius = (size.minDimension - style.width) / 2

            drawArc(
                color = color,
                startAngle = 1f,
                sweepAngle = 360f,
                topLeft = Offset(
                    (size / 2.0f).width - innerRadius,
                    (size / 2.0f).height - innerRadius
                ),
                size = Size(innerRadius * 2, innerRadius * 2),
                useCenter = true,
                style = style
            )
        }
    )
}

@Composable
fun CountDownButton(
    modifier: Modifier = Modifier,
    isPlaying: Boolean,
    optionSelected: () -> Unit,
) {
    Column(
        modifier = Modifier.padding(top = dimensionResource(id = R.dimen.extra_large))
    ) {
        Button(
            onClick = { optionSelected() },
            modifier = modifier
                .height(70.dp)
                .width(200.dp),
            shape = RoundedCornerShape(dimensionResource(id = R.dimen.extra_large))
        )
        {
            val buttonText = if (!isPlaying) {
                stringResource(id = R.string.start_button)
            } else {
                stringResource(id = R.string.stop_button)
            }
            Text(
                text = buttonText,
                fontSize = 20.sp
            )
        }
    }
}

@Composable
fun ShowCelebrationAnimation(modifier: Modifier = Modifier) {
    val composition by rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(R.raw.timer_complete_animation)
    )

    LottieAnimation(
        composition = composition,
        modifier = modifier.fillMaxSize()
    )
}

@Preview(showBackground = true)
@Composable
fun CountDownViewPreview() {
    CountDownBody(
        time = "01:00",
        progress = 0.5f,
        isPlaying = false,
        celebrate = true,
        optionSelected = {}
    )
}
