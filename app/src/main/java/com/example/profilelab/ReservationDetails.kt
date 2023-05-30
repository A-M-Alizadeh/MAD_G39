package com.example.profilelab

import android.os.Bundle
import android.view.MotionEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.profilelab.ui.theme.ProfileLabTheme
import com.google.firebase.auth.FirebaseAuth

class ReservationDetails : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        val reservation_id = intent.getStringExtra("reservation_id").toString()
        super.onCreate(savedInstanceState)
        setContent {
            val scrollState = rememberScrollState()

            ProfileLabTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(modifier = Modifier.scrollable(scrollState, Orientation.Vertical)) {
                        TopAppBar(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(color = Color.Red),
                            title = {
                                Text(text = "Reservation Details")
                            },
                            navigationIcon = {
                                IconButton(onClick = {
                                    finish()
                                }) {
                                    Icon(Icons.Filled.ArrowBack, "backIcon")
                                }
                            },
                        )
                        Container("Android")
                    }
                }
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Container(name: String, modifier: Modifier = Modifier) {
    Card(
        modifier = Modifier
            .padding(horizontal = 10.dp, vertical = 12.dp)
            .fillMaxWidth()
            .background(color = Color(0xFFFAFAFA)),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        shape = RoundedCornerShape(corner = CornerSize(16.dp)),
        onClick = {},
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ),

        ){
        CardContent()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardContent() {
    Column(modifier = Modifier.padding(16.dp)) {
        Spacer(modifier = Modifier.padding(5.dp))
        Text(
            buildAnnotatedString {
            withStyle(style = SpanStyle(color = Color.Gray)) {
                append("Court:  ")
            }
            append("Put Court Name Here")
        },
            style = TextStyle(
                color = Color.Black,
                fontSize = 15.sp
            )
        )

        Spacer(modifier = Modifier.padding(5.dp))
        Text(
            buildAnnotatedString {
                withStyle(style = SpanStyle(color = Color.Gray)) {
                    append("Sport:  ")
                }
                append("Put Sport Name Here")
            },
            style = TextStyle(
                color = Color.Black,
                fontSize = 15.sp
            )
        )

        Spacer(modifier = Modifier.padding(5.dp))
        Text(
            buildAnnotatedString {
                withStyle(style = SpanStyle(color = Color.Gray)) {
                    append("User:  ")
                }
                append("Put User Nickname Here")
            },
            style = TextStyle(
                color = Color.Black,
                fontSize = 15.sp
            )
        )

        Spacer(modifier = Modifier.padding(5.dp))
        Text(
            buildAnnotatedString {
                withStyle(style = SpanStyle(color = Color.Gray)) {
                    append("Data:  ")
                }
                append("Put Date Here (DD/MM/YYYY)")
            },
            style = TextStyle(
                color = Color.Black,
                fontSize = 15.sp
            )
        )

        Spacer(modifier = Modifier.padding(10.dp))
        Row(modifier = Modifier
            .fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                buildAnnotatedString {
                    withStyle(style = SpanStyle(color = Color.Gray)) {
                        append("Start Time: ")
                    }
                    append("08:00")
                },
                style = TextStyle(
                    color = Color.Black,
                    fontSize = 15.sp
                ),
                modifier = Modifier
                    .padding(end = 10.dp)
            )
            Text(
                buildAnnotatedString {
                    withStyle(style = SpanStyle(color = Color.Gray)) {
                        append("End Time: ")
                    }
                    append("10:00")
                },
                style = TextStyle(
                    color = Color.Black,
                    fontSize = 15.sp
                )
            )

        }

        Spacer(modifier = Modifier.padding(15.dp))
        Text(
            buildAnnotatedString {
                withStyle(style = SpanStyle(color = Color.Gray)) { //, fontSize = 15.sp
                    append("Order Comments:  ")
                }
                append("Put Comments Here and this can get bigger")
            },
            style = TextStyle(
                color = Color.Black,
                fontSize = 15.sp
            )
        )

        Spacer(modifier = Modifier.padding(15.dp))
        Divider(color = Color(0xFFECEFF1))
        Spacer(modifier = Modifier.padding(15.dp))
        //TODO: you can add a flag to check if user has already rated this reservation
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                buildAnnotatedString {
                    withStyle(style = SpanStyle(color = Color.Gray)) {
                        append("Rate this reservation")
                    }
                },
                style = TextStyle(
                    color = Color.Black,
                    fontSize = 15.sp
                )
            )
            Spacer(modifier = Modifier.padding(5.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = Color(0xFFbdbdbd),
                        shape = RoundedCornerShape(16.dp)
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.padding(2.dp))
                CustomRatingBar2(rating = 3)//TODO: pass the view model here to save the rating
                Spacer(modifier = Modifier.padding(2.dp))
                Divider()
                Spacer(modifier = Modifier.padding(2.dp))
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    value = "",
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        focusedLabelColor = colorResource(id = R.color.info_blue),
                        unfocusedLabelColor = Color.Gray,
                        textColor = Color.Transparent,
                        placeholderColor = Color.Transparent,
                    ),
                    textStyle = TextStyle(
                        color = Color.Black,
                        fontSize = 12.sp
                    ),
                    onValueChange = {
//                    text = it
                    },
                    label = {
                        Text("comment")
                    }
                )

                TextButton(
                    modifier = Modifier,
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = colorResource(id = R.color.info_blue),
                    ),
                    onClick = { /*TODO*/ }) {
                    Text(text = "Submit")
                }
            }


        }

    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CustomRatingBar2(
    modifier: Modifier = Modifier,
    rating: Int,
) {
    var ratingState by remember { mutableStateOf(rating) }
    var selected by remember { mutableStateOf(false) }
    val size by animateDpAsState(
        targetValue = if (selected) 32.dp else 28.dp,
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
                    .height(size)
                    .clickable(onClick = {
                        ratingState = i
                    })
                    .pointerInteropFilter {
                        when (it.action) {
                            MotionEvent.ACTION_DOWN -> {
                                selected = true
                                ratingState = i
                            }

                            MotionEvent.ACTION_UP -> {
                                selected = false
                            }
                        }
                        true
                    },
                tint = if (i <= ratingState) Color(0xFFE0B84F) else Color(0xFFE0E0E0)
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    ProfileLabTheme {
        Container("Android")
    }
}