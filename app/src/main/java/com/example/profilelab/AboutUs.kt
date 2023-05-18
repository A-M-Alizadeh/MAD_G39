package com.example.profilelab

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.profilelab.ui.theme.ProfileLabTheme
import com.google.firebase.firestore.FirebaseFirestore


class AboutUs : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProfileLabTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
//                    val db = FirebaseFirestore.getInstance()
//                    db.collection("reservations")
//                        .get()
//                        .addOnCompleteListener { task ->
//                            if (task.isSuccessful) {
//                                for (document in task.result) {
//                                    Log.d(TAG, document.id + " => " + document.data)
//                                }
//                            } else {
//                                Log.w(TAG, "Error getting documents.", task.exception)
//                            }
//                        }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = colorResource(R.color.black)),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "MAD G39 2023",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize(align = Alignment.Center)
                .padding(horizontal = 16.dp)
                .padding(vertical = 20.dp)
        )
        Text(
            text = "To Be Implemented",
            style = MaterialTheme.typography.bodySmall,
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize(align = Alignment.Center)
                .padding(horizontal = 16.dp)
                .padding(vertical = 20.dp)
        )

    }
//    Scaffold() { innerPadding ->
//        LazyColumn(
//            modifier = Modifier
//                .padding(innerPadding)
//                .clip(shape = RoundedCornerShape(0.dp, 0.dp, 100.dp, 100.dp))
//                .background(color = colorResource(R.color.red_500)),
//            //contentPadding = innerPadding,
//            verticalArrangement = Arrangement.spacedBy(8.dp)
//        ) {
//            item{
//                Text(
//                    text = "MAD G39 2023",
//                    style = MaterialTheme.typography.headlineMedium,
//                    color = Color.White,
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .wrapContentSize(align = androidx.compose.ui.Alignment.Center)
//                        .padding(horizontal = 16.dp)
//                        .padding(vertical = 20.dp)
//                )
//            }
//        }
//    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ProfileLabTheme {
        Greeting("Android")
    }
}