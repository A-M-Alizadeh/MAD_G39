package com.example.profilelab

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.core.graphics.toColorInt
import coil.compose.rememberAsyncImagePainter
import com.example.profilelab.ui.theme.ProfileLabTheme
import com.example.profilelab.view_models.CourtCompleteModel


object samples {
    val courtsList = listOf(
        CourtCompleteModel(
            id = 1,
            name = "The Court",
            location = mapOf(
                "lat" to "19.34376",
                "lng" to "104.12743",
            ),
            sports = mapOf(
                1 to "Basketball",
                2 to "Volleyball",
                3 to "Badminton",
            ),
        ),
        CourtCompleteModel(
            id = 2,
            name = "The Court 2",
            location = mapOf(
                "lat" to "49.97451",
                "lng" to "11.99917",
            ),
            sports = mapOf(
                1 to "Basketball",
                2 to "Volleyball",
                3 to "Badminton",
            ),
        ),
        CourtCompleteModel(
            id = 3,
            name = "The Court 3",
            location = mapOf(
                "lat" to "-34.06841",
                "lng" to "140.63944",
            ),
            sports = mapOf(
                1 to "Basketball",
                2 to "Volleyball",
                3 to "Badminton",
            ),
        ),
        CourtCompleteModel(
            id = 4,
            name = "The Court 4",
            location = mapOf(
                "lat" to "19.34376",
                "lng" to "104.12743",
            ),
            sports = mapOf(
                1 to "Basketball",
                2 to "Volleyball",
                3 to "Badminton",
            ),
        ),
        CourtCompleteModel(
            id = 5,
            name = "The Court 5",
            location = mapOf(
                "lat" to "19.34376",
                "lng" to "104.12743",
            ),
            sports = mapOf(
                1 to "Basketball",
                2 to "Volleyball",
                3 to "Badminton",
            ),
        ),
        CourtCompleteModel(
            id = 6,
            name = "The Court 6",
            location = mapOf(
                "lat" to "19.34376",
                "lng" to "104.12743",
            ),
            sports = mapOf(
                1 to "Basketball",
                2 to "Volleyball",
                3 to "Badminton",
            ),
        ),
        CourtCompleteModel(
            id = 7,
            name = "The Court 7",
            location = mapOf(
                "lat" to "19.34376",
                "lng" to "104.12743",
            ),
            sports = mapOf(
                1 to "Basketball",
                2 to "Volleyball",
                3 to "Badminton",
            ),
        ),
        CourtCompleteModel(
            id = 8,
            name = "The Court 8",
            location = mapOf(
                "lat" to "19.34376",
                "lng" to "104.12743",
            ),
            sports = mapOf(
                1 to "Basketball",
                2 to "Volleyball",
                3 to "Badminton",
            ),
        ),

    )
}

class Courts : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ProfileLabTheme {
                val crts = remember { samples.courtsList }
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    modifier = Modifier.background(color = Color(0xFFFAFAFA))
                        .fillMaxWidth().fillMaxHeight()
                ) {
                    items(crts) {
                        EmployeeCard(emp = it)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployeeCard(emp: CourtCompleteModel) {

    val mContext = LocalContext.current
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
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps?saddr=20.344,34.34&daddr=20.5666,45.345")
            )
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
                    text = "Address : " + "This is the Address of the Court if you tap on it it will take you to the map",
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 15.sp
                    )
                )
            }
            Image(
                painter = rememberAsyncImagePainter("https://picsum.photos/200"),
                contentDescription = "Profile Image",
                contentScale = ContentScale.FillHeight,
                modifier = Modifier
                    .padding(8.dp)
                    .size(110.dp)
                    .clip((CircleShape))
            )
        }
    }
}

//@Composable
//fun DetailsContent() {
//    val crts = remember { samples.courtsList }
//    LazyColumn(
//        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
//    ) {
//        items(
//            crts
//        ) {
//            EmployeeCard(emp = it)
//        }
//    }
//}