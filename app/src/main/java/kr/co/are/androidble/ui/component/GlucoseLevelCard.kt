import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun GlucoseLevelCard(
    glucoseLevel: Int,
    unit: String,
    time: String,
    createdTime: String? = null,
    modifiedTime: String? = null
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer), // 테마의 primaryContainer 색상 사용
        shape = RoundedCornerShape(16.dp), // 카드의 모서리를 둥글게 처리합니다.
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp) // 카드에 약간의 그림자 효과를 줍니다.
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                // 현재 시간을 표시
                Text(
                    text = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss")),
                    color = MaterialTheme.colorScheme.onPrimaryContainer, // 텍스트에 primaryContainer 대비 색상 사용
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // 원형 프로그레스바 및 총합 점수
            CircularScoreIndicator(
                score = glucoseLevel,
                unit = unit,
                maxScore = 200
            )
        }
    }
}

@Composable
fun CircularScoreIndicator(score: Int, unit: String, maxScore: Int) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(100.dp)
    ) {
        CircularProgressIndicator(
            progress = { score / maxScore.toFloat() },
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.primary, // ProgressBar 색상도 테마에 맞게 설정
            strokeWidth = 8.dp,
            trackColor = ProgressIndicatorDefaults.circularIndeterminateTrackColor,
        )
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "$score",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary // 점수 텍스트 색상 설정
            )
            Text(
                text = unit,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.primary
            )
        }

    }
}

@Preview
@Composable
fun PreviewGlucoseLevelCard() {
    GlucoseLevelCard(
        glucoseLevel = 56,
        unit = "mg/dL",
        time = "2020.03.21"
    )
}
