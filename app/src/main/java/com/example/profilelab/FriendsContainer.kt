package com.example.profilelab

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.example.profilelab.ui.theme.ProfileLabTheme
import com.example.profilelab.view_models.FireUserViewModel
import com.example.profilelab.view_models.FriendRequest
import kotlinx.coroutines.launch

class FriendsContainer : ComponentActivity() {



    @OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
//            val userVM = ViewModelProvider(this)[FireUserViewModel::class.java]
//            val requests = remember { mutableStateOf(ArrayList<FriendRequest>()) }
//            userVM.currentUser.observe(this) {
//                var requestsList = ArrayList<FriendRequest>()
//                Log.w("FriendsContainer", "---------->  requestsList: ${it.friendRequests}")
//                Log.w("FriendsContainer", "---------->  requestsList: ${it.friendRequests::class.simpleName}")
//                for (i in 0 until it.friendRequests.size) {
////                    requestsList.add()
//                    Log.w("FriendsContainer", "---------->  requestsList: ${it.friendRequests[i]}")
////                    Log.w("FriendsContainer", "---------->  Type: ${it.friendRequests[i]::class.simpleName}")
//                    Log.w("FriendsContainer", "---------->  Type: ${it.friendRequests[i]}")
//                    Log.w("What ?????","${it.friendRequests[i]}")
//
//                }
//                requests.value = requestsList
//                Log.d("FriendsContainer", "---------->  requests: ${requests.value}")
//            }

            val tabs = listOf(
                TabItem(
                    title = "Friends",
                    icon = Icons.Filled.ThumbUp,
                    screen = { FriendsTab(content = "Friends")},
                ),
                TabItem(
                    title = "Requests",
                    icon = Icons.Filled.Send,
                    screen = { RequestTab(content = "Requests")},
                ),
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
}

@Composable
private fun RequestTab(
    content: String,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = content )
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            modifier = Modifier
                .background(color = Color(0xFFFAFAFA))
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            items(
                items = listOf('a', 'b', 'c'),
                key = { it }
            ) {
                Text(text = it.toString())
            }
        }
    }
}

@Composable
private fun FriendsTab(
    content: String
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = content )
    }
}
