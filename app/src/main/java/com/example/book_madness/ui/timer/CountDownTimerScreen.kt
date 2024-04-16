@file:OptIn(ExperimentalPermissionsApi::class, ExperimentalPermissionsApi::class)

package com.example.book_madness.ui.timer

import android.Manifest
import android.widget.Toast
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.book_madness.R
import com.example.book_madness.model.Reminder
import com.example.book_madness.ui.AppViewModelFactoryProvider
import com.example.book_madness.ui.navigation.BookMadnessTitlesResId
import com.example.book_madness.ui.BookMadnessTopAppBar
import com.example.book_madness.util.FIVE_SECONDS
import com.example.book_madness.util.ONE_DAY
import com.example.book_madness.util.SEVEN_DAYS
import com.example.book_madness.util.THIRTY_DAYS
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import java.util.concurrent.TimeUnit

@Composable
fun CountDownTimerScreen(
    bottomNavigationBar: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CountTimerViewModel = viewModel(factory = AppViewModelFactoryProvider.Factory),
) {
    val time by viewModel.time
    val progress by viewModel.progress
    val isPlaying by viewModel.isPlaying
    val celebrate by viewModel.celebrate

    Scaffold(
        topBar = {
            BookMadnessTopAppBar(
                title = stringResource(BookMadnessTitlesResId.TIMER_SCREEN),
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
            modifier = modifier.padding(innerPadding),
            onScheduleReminder = { viewModel.scheduleReminder(it) },
            optionSelected = { viewModel.handleCountDownTimer() }
        )
    }
}

@Composable
fun CountDownBody(
    time: String,
    progress: Float,
    isPlaying: Boolean,
    celebrate: Boolean,
    onScheduleReminder: (Reminder) -> Unit,
    optionSelected: () -> Unit,
    modifier: Modifier = Modifier
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
                text = stringResource(id = R.string.reading_time_title),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineLarge
            )
            Text(
                text = stringResource(id = R.string.enjoy_title),
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
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.extra_large))
                    .semantics { contentDescription = "Timer progress indicator" }
            )
            CountDownButton(
                isPlaying = isPlaying,
                optionSelected = { optionSelected() }
            )

            ReminderButton(onScheduleReminder)
        }
    }
}

@Composable
fun CountDownIndicator(
    progress: Float,
    time: String,
    modifier: Modifier = Modifier
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
    modifier: Modifier = Modifier
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

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ReminderButton(
    onScheduleReminder: (Reminder) -> Unit,
    modifier: Modifier = Modifier
) {
    var showReminderDialog by rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current
    val permissionState =
        rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)


    Button(
        onClick = {
            when (permissionState.status) {
                is PermissionStatus.Denied -> {
                    if (!permissionState.status.shouldShowRationale) {
                        permissionState.launchPermissionRequest()
                    } else {
                        Toast.makeText(
                            context,
                            "Looks like you permanently denied permission. Please provide it in Settings",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                is PermissionStatus.Granted -> {
                    showReminderDialog = true
                }
            }
        },
        modifier = modifier
            .height(70.dp)
            .width(200.dp),
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.extra_large))
    ) {
        Text(
            text = stringResource(id = R.string.reminder_button),
            fontSize = 20.sp
        )
    }

    if (showReminderDialog) {
        ReminderDialogContent(
            onDialogDismiss = { showReminderDialog = false },
            onScheduleReminder = onScheduleReminder
        )
    }
}

@Composable
fun ReminderDialogContent(
    onDialogDismiss: () -> Unit,
    onScheduleReminder: (Reminder) -> Unit,
    modifier: Modifier = Modifier
) {
    val reminders = listOf(
        Reminder(R.string.five_seconds_button, FIVE_SECONDS, TimeUnit.SECONDS),
        Reminder(R.string.one_day_button, ONE_DAY, TimeUnit.DAYS),
        Reminder(R.string.one_week_button, SEVEN_DAYS, TimeUnit.DAYS),
        Reminder(R.string.one_month_button, THIRTY_DAYS, TimeUnit.DAYS)
    )

    AlertDialog(
        onDismissRequest = { onDialogDismiss() },
        confirmButton = {},
        title = { Text(stringResource(R.string.remind_me_title)) },
        text = {
            Column {
                reminders.forEach {
                    Text(
                        text = stringResource(it.durationRes),
                        modifier = Modifier
                            .clickable {
                                onScheduleReminder(it)
                                onDialogDismiss()
                            }
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    )
                }
            }
        },
        modifier = modifier
    )
}

@Composable
fun CountDownButton(
    isPlaying: Boolean,
    optionSelected: () -> Unit,
    modifier: Modifier = Modifier
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
        optionSelected = { },
        onScheduleReminder = { }
    )
}
