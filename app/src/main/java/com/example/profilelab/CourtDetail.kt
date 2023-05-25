package com.example.profilelab

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.profilelab.ui.theme.ProfileLabTheme
import com.example.profilelab.view_models.FireCourt
import com.example.profilelab.view_models.FireCourtsViewModel
import kotlin.math.min

class CourtDetail : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val header = intent.getStringExtra("header")
        val id = intent.getStringExtra("id")

        setContent {
            val mContext = LocalContext.current
            val courtVM = ViewModelProvider(this)[FireCourtsViewModel::class.java]
            val court = remember { mutableStateOf(listOf<FireCourt>()) }
            val scrollState = rememberScrollState()
            courtVM.courtList.observe(this) {
                court.value = it
                Log.e("CourtDetail", "------> Court: ${court.value} $id")
            }

            LaunchedEffect(
                key1 = id,
                block = {
                    if (id != null) {
                        courtVM.getCourtById(id)
                    }
                }
            )

            ProfileLabTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column (modifier = Modifier.scrollable(scrollState, Orientation.Vertical)) {
                        TopAppBar(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(color = Color.Red),
                            title = {
                                if (header != null) {
                                    Text(text = header)
                                }
                            },
                            navigationIcon = {
                                IconButton(onClick = {
                                    finish()
                                }) {
                                    Icon(Icons.Filled.ArrowBack, "backIcon")
                                }
                            },
                        )

                        GlideImage(
                        model = "https://picsum.photos/500/200",
                        contentDescription = "Image",
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth()
                            .fillMaxHeight(0.2f)
                            .background(color = Color.White)
                            .clip((RoundedCornerShape(8.dp))), contentScale = ContentScale.FillBounds,)

                        //detail content
                        //detail content
                        Row(
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.Bottom,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .padding(bottom = 8.dp)
                            .background(color = Color(0xFFFAFAFA))
                        ){
                            Icon(
                                Icons.Filled.LocationOn,
                                "LocationIcon",
                                tint = colorResource(id = R.color.info_blue),
                            )
                            Text(
                                modifier = Modifier
                                    .clickable(onClick = {
                                        val mapIntent = Intent(
                                            Intent.ACTION_VIEW,
                                            Uri.parse("http://maps.google.com/maps?saddr=45.062354,7.662426&daddr=${court.value[0].location["lat"]},${court.value[0].location["lon"]}")
                                        )
                                        mContext.startActivity(mapIntent)
                                    }),
                                text = "See Location on Map",
                                color = colorResource(id = R.color.info_blue),
                            )
                        }

                        //detail content
                        if (court.value.isNotEmpty()) {
                            LazyColumn(
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                                modifier = Modifier
                                    .background(color = Color(0xFFFAFAFA))
                                    .fillMaxWidth()
                                    .fillMaxHeight()
                            ) {
                                items(
                                    items = court.value[0].sports.values.toList(),
                                    key = {it}
                                ) {
                                    Card(
                                        modifier = Modifier
                                            .padding(horizontal = 8.dp, vertical = 8.dp)
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
                                        CardContent(it)
                                    }
                                }
                            }
                        }


                    }
                }
            }
        }
    }
}

@Composable
fun CardContent(sport: String){
    Row(modifier = Modifier.fillMaxWidth()) {
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .padding(16.dp)
        ){
            Text(
                modifier = Modifier
                    .alpha(0.8f),
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                text = sport)
            Text(
                modifier = Modifier
                    .alpha(0.5f)
                    .padding(top = 4.dp),
                fontSize = 12.sp,

                text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.")
        }
    }
}









