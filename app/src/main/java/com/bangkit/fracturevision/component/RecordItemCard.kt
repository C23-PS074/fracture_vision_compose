package com.bangkit.fracturevision.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.bangkit.fracturevision.R
import com.bangkit.fracturevision.model.RecordItem

@Composable
fun RecordItemCard(
    data: RecordItem,
    modifier: Modifier = Modifier
) {
    Card (
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(8.dp)
    ){
        Row(verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                modifier = modifier
                    .width(120.dp)
                    .height(120.dp)
                ,
                model = ImageRequest.Builder(LocalContext.current)
                    .data(data.image)
                    .build(),
                contentScale = ContentScale.Crop,
                contentDescription = "Rontgen Image",
                placeholder = painterResource(id = R.drawable.ic_image),
                error = painterResource(id = R.drawable.ic_image),
            )
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = "#${data.id}",
                )
                Text(
                    text = data.result,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${data.date} at ${data.time}",
                    fontSize = 11.sp
                )
            }
        }

    }
}