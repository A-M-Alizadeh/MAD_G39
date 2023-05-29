package com.example.profilelab

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
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
import com.example.profilelab.models.Data
import com.example.profilelab.models.NotificationModel
import com.example.profilelab.sealed.FriendsDataState
import com.example.profilelab.sealed.RequestDataState
import com.example.profilelab.ui.theme.ProfileLabTheme
import com.example.profilelab.view_models.FriendRequest
import com.example.profilelab.view_models.NotificationViewModel
import com.example.profilelab.view_models.liveVM.FriendsLiveViewModel
import com.example.profilelab.view_models.liveVM.RequestsLiveViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FriendsContainer : ComponentActivity() {
    private  val TAG = "ContainerActivity"
    private val notificationViewModel: NotificationViewModel by viewModels()


    @OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mcontext: Context = this

        notificationViewModel.connectionError.observe(this){
            when(it){
                "sending"-> {
                    Toast.makeText(this, "sending notification", Toast.LENGTH_SHORT).show()
                }
                "sent"-> {
                    Toast.makeText(this, "notification sent", Toast.LENGTH_SHORT).show()
                }
                "error while sending"-> {
                    Toast.makeText(this, "error while sending", Toast.LENGTH_SHORT).show()
                }
            }
        }

        notificationViewModel.response.observe(this){
            if (it.isNotEmpty())
                Log.d(TAG, "Notification in Kotlin: $it ")
        }

        fun push(username: String, receiverToken: String, title: String, message: String){
//            FirebaseMessaging.getInstance().token.addOnSuccessListener { token ->
//                Log.d(TAG, "push: $token")
            Log.d(TAG, "=======> push: $receiverToken")

                notificationViewModel
                    .sendNotification(
                        NotificationModel(
                            receiverToken,
                            Data(title,"$username $message")
                        )
                    )

                Toast.makeText(mcontext, "Notification sent", Toast.LENGTH_SHORT).show()

//            }
        }

        setContent {
            val mContext = LocalContext.current
//            val friendRequestVM = ViewModelProvider(this)[FriendsViewModel::class.java]
//            val requests = remember { mutableStateOf(listOf<FriendRequest>()) }
//            val friends = remember { mutableStateOf(listOf<FriendRequest>()) }
//            friendRequestVM.requestList.observe(this) {
//                requests.value = it
//            }
//            friendRequestVM.friendList.observe(this) {
//                friends.value = it
//            }
            val friendsVM: FriendsLiveViewModel by viewModels()
            val requestsVM: RequestsLiveViewModel by viewModels()

            val tabs = listOf(
                TabItem(
                    title = "Friends",
                    icon = Icons.Filled.ThumbUp,
                    screen = {
                        setFriendsData(friendsVM){ username, receiverToken, title, message ->
                            push(username, receiverToken, title, message)
                        }
                             },
                ),
                TabItem(
                    title = "Requests",
                    icon = Icons.Filled.Send,
                ) {
                    setRequestData(requestsVM){ username, receiverToken, title, message ->
                        push(username, receiverToken, title, message)
                    }
                },
            )
            val pagerState = rememberPagerState(
                initialPage = 0,
                initialPageOffsetFraction = 0f
            ) {
                tabs.size
            }
            val coroutineScope = rememberCoroutineScope()


            ProfileLabTheme {
                Column (
                    modifier = Modifier
                ) {
                    TopAppBar(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = Color.Red),
                        title = {
                            Text(text = "Friends")
                        },
                        navigationIcon = {
                            IconButton(onClick = {
                                finish()
                            }) {
                                Icon(Icons.Filled.ArrowBack, "backIcon")
                            }
                        }
                    )
                    TabRow(
                        selectedTabIndex = pagerState.currentPage,
                        contentColor = colorResource(id = R.color.info_blue),
                        indicator = { tabPositions ->
                            TabRowDefaults.Indicator(
                                Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                                color = colorResource(id = R.color.info_blue)
                            )
                        },
                    ) {
                        tabs.forEachIndexed { index, item ->
                            Tab(
                                selected = index == pagerState.currentPage,
                                text = { Text(text = item.title) },
                                icon = { Icon(item.icon,  "")},
                                onClick = { coroutineScope.launch { pagerState.animateScrollToPage(index) } },
                                selectedContentColor = colorResource(id = R.color.info_blue),
                                unselectedContentColor = colorResource(id = R.color.gray),
                            )
                        }
                    }
                    HorizontalPager(
                        modifier = Modifier,
                        state = pagerState,
                        userScrollEnabled = false,
                    ) {
                        tabs[pagerState.currentPage].screen()
                    }
                }
            }
        }
    }

    @Composable
    fun setRequestData(requestsVM: RequestsLiveViewModel, push: (String, String, String, String) -> Unit) {
        when (val result = requestsVM.response.value) {
            is RequestDataState.Loading -> {
//                return "Loading"
            }
            is RequestDataState.Success -> {
                RequestTab(
                    content = "Requests",
                    data = result.data,
                    requestsVM
                ) { username, receiverToken, title, message ->
                    push(username, receiverToken, title, message)
                }
            }
            is RequestDataState.Failure -> {
//                return "Error"
            }

            else -> {
//                return "Error"
            }
        }
    }

    @Composable
    fun setFriendsData(friendsVM: FriendsLiveViewModel, push: (String, String, String, String) -> Unit) {
        when (val result = friendsVM.response.value) {
            is FriendsDataState.Loading -> {
//                return "Loading"
            }
            is FriendsDataState.Success -> {
                FriendsTab(
                    content = "Friends",
                    data= result.data,
                    friendsVM,
                ) { username, receiverToken, title, message ->
                        push(username, receiverToken, title, message)
                }
            }
            is FriendsDataState.Failure -> {
//                return "Error"
            }

            else -> {
//                return "Error"
            }
        }
    }

}



//-------------------------------------------- Requests Tab --------------------------------------------
@Composable
private fun RequestTab(
    content: String,
    data: MutableList<FriendRequest>,
    requestsVM: RequestsLiveViewModel,
    notifier : (String, String, String, String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            modifier = Modifier
                .background(color = Color(0xFFFAFAFA))
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            items(
                items = data,//listOf('a', 'b', 'c'),
                key = { it.id },
            ) {
                RequestCard(request = it, requestsVM = requestsVM, notifier= notifier)
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class, ExperimentalMaterial3Api::class)
@Composable
fun RequestCard(
    request: FriendRequest,
    requestsVM: RequestsLiveViewModel,
    notifier : (String, String, String, String) -> Unit){
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
                    text = request.senderNickname.takeIf { request.incoming} ?: request.receiverNickname,
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                    )
                )

                Spacer(modifier = Modifier.padding(5.dp))
                Text(buildAnnotatedString {
                    withStyle(style = SpanStyle(color = Color.Gray)) {
                        append("Email: ")
                    }
                    append(request.senderUsername.takeIf { request.incoming} ?: request.receiverUsername)
                },
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 15.sp
                    )
                )
            }

            GlideImage(
                model = "https://xsgames.co/randomusers/assets/avatars/male/${(0..50).random()}.jpg",
                contentDescription = "Image",
                modifier = Modifier
                    .padding(8.dp)
                    .size(40.dp)
                    .clip((CircleShape))
            )

        }
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
                if (request.incoming){
                    TextButton(
                        modifier = Modifier
                            .padding(vertical = 0.dp, horizontal = 8.dp),
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = colorResource(id = R.color.red_500),
                        ),
                        onClick = {
                            requestsVM.changeFriendshipStatus(request.id, 2)
                        }) {
                        Text("Reject")
                    }
                    TextButton(
                        modifier = Modifier
                            .padding(vertical = 0.dp, horizontal = 8.dp),
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = Color(0xFF388E3C) ,
                        ),
                        onClick = {
                            Log.d("TAG", "====> FriendCard:${request.id} ${request.senderFcmToken}")
                            requestsVM.changeFriendshipStatus(request.id, 1)
                            notifier(
                                "Friend Request",
                                request.senderFcmToken,
                                "Friend Request",
                                "${request.senderNickname} friend request accepted!")
                        }) {
                        Text("Accept")
                    }
                }else{
                    TextButton(
                        modifier = Modifier
                            .padding(vertical = 0.dp, horizontal = 8.dp),
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = colorResource(id = R.color.info_blue),
                        ),
                        onClick = {
                            requestsVM.changeFriendshipStatus(request.id, 3)
                        }) {
                        Text("Cancel")
                    }
                }



        }

    }
}



//-------------------------------------------- Friends Tab --------------------------------------------

@Composable
private fun FriendsTab(
    content: String,
    data: MutableList<FriendRequest>,
    friendsVM: FriendsLiveViewModel,
    notifier : (String, String, String, String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            modifier = Modifier
                .background(color = Color(0xFFFAFAFA))
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            items(
                items = data,//listOf('a', 'b', 'c'),
                key = { it.id }
            ) {
                FriendCard(request = it, friendsVM = friendsVM, notifier = notifier)
            }
        }
    }
}


@OptIn(ExperimentalGlideComposeApi::class, ExperimentalMaterial3Api::class)
@Composable
fun FriendCard(
    request: FriendRequest,
    friendsVM: FriendsLiveViewModel,
    notifier : (String, String, String, String) -> Unit){
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
                    text = request.receiverNickname.takeIf { FirebaseAuth.getInstance().currentUser?.uid.toString() == request.senderId} ?: request.senderNickname,
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                    )
                )

                Spacer(modifier = Modifier.padding(5.dp))
                Text(buildAnnotatedString {
                    withStyle(style = SpanStyle(color = Color.Gray)) {
                        append("Email: ")
                    }
                    append(request.receiverUsername.takeIf {FirebaseAuth.getInstance().currentUser?.uid.toString() == request.senderId} ?: request.senderUsername)
                },
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 15.sp
                    )
                )
            }

            GlideImage(
                model = "https://xsgames.co/randomusers/assets/avatars/male/${(0..50).random()}.jpg",
                contentDescription = "Image",
                modifier = Modifier
                    .padding(8.dp)
                    .size(40.dp)
                    .clip((CircleShape))
            )

        }
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
                TextButton(
                    modifier = Modifier
                        .padding(vertical = 0.dp, horizontal = 8.dp),
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = Color(0xFFFBC02D),
                    ),
                    onClick = {
                        Log.d("TAG", "====> FriendCard:${request.id} ${request.senderFcmToken}")
                        var fcm : String = request.senderFcmToken
                        if (FirebaseAuth.getInstance().currentUser?.uid.toString() == request.senderId){
                            fcm = request.receiverFcmToken
                        }
                        notifier( //here
                            request.receiverNickname,
                            fcm,
                            "Heads Up",
                            "${request.senderNickname} are you up for a game?")
                    }) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Bell Icon",
                        tint = Color(0xFFFBC02D)
                    )
                    Text("Notify")
                }


        }

    }
}
