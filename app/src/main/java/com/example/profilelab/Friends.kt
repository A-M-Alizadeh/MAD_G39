package com.example.profilelab

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.profilelab.models.FireUser
import com.example.profilelab.sealed.PeopleDataState
import com.example.profilelab.ui.theme.ProfileLabTheme
import com.example.profilelab.view_models.liveVM.PeopleLiveViewModel


class Friends : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val mContext = LocalContext.current
//            val usersVM = ViewModelProvider(this)[FireUserViewModel::class.java]
//            var usersList = remember { mutableStateOf(listOf<FireUser>()) }
//            usersVM.usersList.observe(this) {
//                usersList.value = it
//            }
            val peopleVM: PeopleLiveViewModel by viewModels()

            ProfileLabTheme {
                Column() {
                    TopAppBar(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = Color.Red),
                        title = {
                            Text(text = "People")
                        },
                        navigationIcon = {
                            IconButton(onClick = {
                                finish()
                            }) {
                                Icon(Icons.Filled.ArrowBack, "backIcon")
                            }
                        },
                        actions = {
                            IconButton(onClick = {
                                val intent = Intent(mContext, FriendsContainer::class.java)
                                startActivity(intent)
                            }) {
                                Icon(Icons.Filled.Person, "settingsIcon")
                            }
                        }
                    )

                    SetData(peopleVM)

                }

            }
        }
    }

    @Composable
    fun SetData(peopleVM: PeopleLiveViewModel) {
        when (val result = peopleVM.response.value) {
            is PeopleDataState.Loading -> {
//                return "Loading"
            }
            is PeopleDataState.Success -> {
                LazyPeople(result.data, peopleVM)
            }
            is PeopleDataState.Failure -> {
//                return "Error"
            }

            else -> {
//                return "Error"
            }
        }
    }

    @Composable
    fun LazyPeople(data: MutableList<FireUser>, peopleVM: PeopleLiveViewModel) {
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            modifier = Modifier
                .background(color = Color(0xFFFAFAFA))
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            items(
                items = data,
                key = { item -> item.id }
            ) {
                FriendCard(emp = it, peopleVM = peopleVM)
            }
        }
    }


    @OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
    @Composable
    fun FriendCard(emp: FireUser, peopleVM: PeopleLiveViewModel){

        val mContext = LocalContext.current
        Card(
            modifier = Modifier
                .padding(
                    start = 8.dp,
                    top = 8.dp,
                    end = 8.dp,
                    bottom = 0.dp
                )
                .fillMaxWidth()
                .background(color = Color(0xFFFAFAFA)), //Color(0xFFFAFAFA)
            elevation = CardDefaults.cardElevation(
                defaultElevation = 2.dp
            ),
            shape = RoundedCornerShape(corner = CornerSize(16.dp)),
            onClick = {
            },
            colors = CardDefaults.cardColors(
                containerColor = Color.White, //Color.White
            ),

            ) {

            Row(modifier = Modifier
                .padding(
                    start = 20.dp,
                    top = 20.dp,
                    end = 20.dp,
                    bottom = 0.dp
                )
                .background(color = Color.White)) {
                Column(
                    modifier = Modifier.weight(1f),
                    Arrangement.Center
                ) {
                    Text(
                        text = emp.nickname,
                        style = TextStyle(
                            color = Color.Black,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                        )
                    )

                    Spacer(modifier = Modifier.padding(5.dp))
                    Text(buildAnnotatedString {
                        withStyle(style = SpanStyle(color = Color.Gray)) {
                            append("Email: ")
                        }
                        append(emp.username)
                    },
                        style = TextStyle(
                            color = Color.Black,
                            fontSize = 15.sp
                        )
                    )

                    Spacer(modifier = Modifier.padding(5.dp))
                    Row() {
                        emp.interests?.map { interest ->
                            Text(
                                modifier = Modifier
                                    .padding(2.dp)
                                    .border(
                                        1.dp,
                                        colorResource(id = R.color.white),
                                        RoundedCornerShape(50.dp)
                                    )
                                    .background(Color(0xFFF5F5F5), RoundedCornerShape(50.dp))
                                    .padding(5.dp),
                                text = interest,
                                style = TextStyle(
                                    color = Color.Black,
                                    fontSize = 8.sp
                                )
                            )
                        }

                    }
                }

                GlideImage(
                    model = "https://xsgames.co/randomusers/assets/avatars/male/${(0..50).random()}.jpg",
                    contentDescription = "Image",
                    modifier = Modifier
                        .padding(8.dp)
                        .size(80.dp)
                        .clip((CircleShape))
                )

            }
            TextButton(
                modifier = Modifier
                    .padding(vertical = 0.dp, horizontal = 8.dp),
                colors = ButtonDefaults.textButtonColors(
                    contentColor = Color(0xFF388E3C),
                ),
                onClick = {
                    peopleVM.sendLiveFriendRequest(emp)
                    val notice = FireNotification(
                        mContext,
                        title = "Friend Request",
                        message = "Your Friend Request sent to : ${emp.nickname}",
                    )
                    notice.fireNotification()
                }) {
                Text("Connect +")
            }
        }
    }

}






