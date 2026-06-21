package com.example.simpleanimations.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.simpleanimations.ui.theme.OceanBlue
import com.example.simpleanimations.ui.theme.OnPrimary
import com.example.simpleanimations.ui.theme.SkyBlue


@Composable
fun ListItemSquare(
    text:String = "123",
    modifier:Modifier = Modifier
){
    val grad1 = OceanBlue   // #2563EB
    val grad2 = SkyBlue     // #38BDF8

    Box(
        modifier = modifier
            .padding(vertical = OUTER_PADDING)
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        grad1.copy(alpha = 0.10f),
                        grad2.copy(alpha = 0.10f)
                    )
                )
            )
            .padding(INNER_PADDING) // inner padding
    ) {
        Box(
            modifier = Modifier
                .size(BOX_SIZE)
                .clip(RoundedCornerShape(16.dp))
                .background(Brush.linearGradient(listOf(grad1, grad2))),
            contentAlignment = Alignment.Center
        ){
            Text(
                text = text,
                color = OnPrimary,
            )
        }
    }

}

@Composable
fun ListItemWide(
    title: String,
    subtitle: String = "lorem ipsum porro",
    modifier: Modifier = Modifier
) {
    val grad1 = OceanBlue   // #2563EB
    val grad2 = SkyBlue     // #38BDF8

    // readable “on light” tones
    val titleColor = Color(0xFF0F172A)     // deep navy
    val subtitleColor = Color(0xFF475569)  // cool gray

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 2.dp, vertical = OUTER_PADDING)
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        grad1.copy(alpha = 0.10f),
                        grad2.copy(alpha = 0.10f)
                    )
                )
            )
            .padding(INNER_PADDING) // inner padding
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Brush.linearGradient(listOf(grad1, grad2)))
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    color = titleColor,
                    fontSize = 16.sp,
                    lineHeight = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1
                )
                Text(
                    text = subtitle,
                    color = subtitleColor,
                    fontSize = 14.sp,
                    lineHeight = 16.sp,
                    fontWeight = FontWeight.Normal,
                    maxLines = 2
                )
            }
        }
    }
}


