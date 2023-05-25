package com.example.profilelab

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MotionEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.profilelab.ui.theme.ProfileLabTheme
import com.example.profilelab.view_models.FireCourt
import com.example.profilelab.view_models.FireCourtsViewModel
import kotlin.math.roundToInt


class Courts : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val courtVM = ViewModelProvider(this)[FireCourtsViewModel::class.java]
            var courtsList = remember { mutableStateOf(listOf<FireCourt>()) }
            courtVM.courtList.observe(this) {
                courtsList.value = it
            }

            ProfileLabTheme {
                Column() {
                    TopAppBar(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = Color.Red),
                        title = {
                            Text(text = "Courts")
                        },
                        navigationIcon = {
                            IconButton(onClick = {
                                finish()
                            }) {
                                Icon(Icons.Filled.ArrowBack, "backIcon")
                            }
                        },
                    )
                    LazyColumn(
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        modifier = Modifier
                            .background(color = Color(0xFFFAFAFA))
                            .fillMaxWidth()
                            .fillMaxHeight()
                    ) {
                        items(
                            items = courtsList.value,
                            key = { it.id }
                        ) {
                            EmployeeCard(emp = it)
                        }
                    }
                }

            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun EmployeeCard(emp: FireCourt) {

    val mContext = LocalContext.current
    val moreInfo = remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .fillMaxWidth()
            .background(color = Color(0xFFFAFAFA)),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        shape = RoundedCornerShape(corner = CornerSize(16.dp)),
        onClick = {
//            val intent = Intent(
//                Intent.ACTION_VIEW,
//                Uri.parse("http://maps.google.com/maps?saddr=45.062354,7.662426&daddr=${emp.location["lat"]},${emp.location["lon"]}")
//            )
            val intent = Intent(mContext, CourtDetail::class.java)
            intent.putExtra("header", emp.name)
            intent.putExtra("id", emp.id)
            mContext.startActivity(intent)
        },
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ),

    ) {

        Row(modifier = Modifier.padding(20.dp)) {
            Column(modifier = Modifier.weight(1f),
                Arrangement.Center) {
                Text(
                    text = emp.name,
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                    )
                )

                Spacer(modifier = Modifier.padding(5.dp))
                Text(
                    text = "Address: ",
                    style = TextStyle(
                        color = Color.Gray,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                    )
                )
                Text(
                    modifier = Modifier.padding(start = 4.dp),
                    text = emp.address,
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 14.sp,
                        letterSpacing = 0.15.sp,
                    )
                )
                Spacer(modifier = Modifier.padding(5.dp))
                CustomRatingBar(
                    modifier = Modifier,
                    rating = emp.rate.toInt()
                )
            }
            GlideImage(
                model = "https://picsum.photos/200?blur?random=${emp.id}",
                contentDescription = "Image",
                modifier = Modifier
                    .padding(8.dp)
                    .size(90.dp)
                    .clip((RoundedCornerShape(16.dp)))
            )
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CustomRatingBar(
    modifier: Modifier = Modifier,
    rating: Int,
) {
    var ratingState by remember { mutableStateOf(rating) }
    var selected by remember { mutableStateOf(false) }
    val size by animateDpAsState(
        targetValue = if (selected) 24.dp else 16.dp,
        spring(Spring.DampingRatioLowBouncy)
    )

    Row (
        modifier = modifier,//.padding(8.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
            ){
        for (i in 1..5) {
            Icon(
                painter = painterResource(id = R.drawable.round_star_24),
                contentDescription = "rating",
                modifier = modifier
                    .width(size)
                    .height(size),
//                    .clickable(onClick = {
//                        ratingState = i
//                    })
//                    .pointerInteropFilter {
//                        when(it.action) {
//                            MotionEvent.ACTION_DOWN -> {
//                                selected = true
//                                ratingState = i
//                            }
//                            MotionEvent.ACTION_UP -> {
//                                selected = false
//                            }
//                        }
//                        true
//                    },
                tint = if (i <= ratingState) Color(0xFFE0B84F) else Color(0xFFE0E0E0)
            )
        }

    }
}