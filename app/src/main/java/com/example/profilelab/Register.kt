package com.example.profilelab

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.profilelab.ui.theme.ProfileLabTheme
import com.example.profilelab.view_models.Interest
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging


val tempList = listOf(
    "soccer",
    "basketball",
    "tennis",
    "baseball",
    "golf",
    "running",
    "volleyball",
    "badminton",
    "swimming",
    "boxing",
    "table tennis",
    "skiing",
    "ice skating",
    "roller skating",
    "cricket",
    "rugby",
    "pool",
    "darts",
    "football",
    "bowling",
    "ice hockey",
    "surfing",
    "karate",
    "horse racing",
    "snowboarding",
    "skateboarding",
    "cycling",
    "archery",
    "fishing",
    "gymnastics",
    "figure skating",
    "rock climbing",
    "sumo wrestling",
    "taekwondo",
    "fencing",
    "water skiing",
    "jet skiing",
    "weight lifting",
    "scuba diving",
    "judo",
    "wind surfing",
    "kickboxing",
    "sky diving",
    "hang gliding",
    "bungee jumping",
)
class Register : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            this.window.statusBarColor = ContextCompat.getColor(this,R.color.red_700)

//            val interestVM = ViewModelProvider(this)[InterestViewModel::class.java]
//            val interestList = remember { mutableStateOf(listOf<Interest>()) }
//            interestVM.interests.observe(this) {
//                interestList.value = it
//            }

            ProfileLabTheme {

                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    register("Register")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun register(name: String) {
    val mContext = LocalContext.current
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    Box(modifier = Modifier.fillMaxSize()) {
        ClickableText(
            text = AnnotatedString("Already have an account? Sign in"),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(20.dp),
            onClick = {
                onBackPressedDispatcher?.onBackPressed()
            },
            style = TextStyle(
                fontSize = 14.sp,
                fontFamily = FontFamily.Default,
                textDecoration = TextDecoration.Underline,
                color = colorResource(id = R.color.black)
            )
        )
    }
    Column(
        modifier = Modifier.padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        val username = remember { mutableStateOf(TextFieldValue()) }
        val nickname = remember { mutableStateOf(TextFieldValue()) }
        val password = remember { mutableStateOf(TextFieldValue()) }
        val repeatPassword = remember { mutableStateOf(TextFieldValue()) }


//        val interestVM = ViewModelProvider(mContext as ComponentActivity)[InterestViewModel::class.java]
//        var interestList by remember { mutableStateOf(listOf<Interest>()) }
//        interestVM.interests.observe(mContext) {
//            interestList = it
//        }

        var interestList by remember { mutableStateOf(
            tempList.mapIndexed { index, it ->
                Interest(
                    index,
                    it,
                    false
                )
            }
        ) }


        Text(text = "Register", style = TextStyle(fontSize = 40.sp, fontFamily = FontFamily.Cursive))

        Spacer(modifier = Modifier.height(20.dp))
        TextField(
            label = { Text(text = "Email") },
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = colorResource(id =R.color.red_500),
                unfocusedIndicatorColor = Color.Transparent,
                placeholderColor = Color.LightGray,
                focusedLabelColor = colorResource(id =R.color.red_500),
                unfocusedLabelColor = Color.LightGray,
            ),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = "Email Icon",
                    tint = Color.LightGray
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .border(1.dp, colorResource(id = R.color.gray), RoundedCornerShape(10.dp))
                .background(color = Color.White),
            value = username.value,
            onValueChange = { username.value = it })

        Spacer(modifier = Modifier.height(20.dp))
        TextField(
            label = { Text(text = "NickName") },
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = colorResource(id =R.color.red_500),
                unfocusedIndicatorColor = Color.Transparent,
                placeholderColor = Color.LightGray,
                focusedLabelColor = colorResource(id =R.color.red_500),
                unfocusedLabelColor = Color.LightGray,
            ),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Face,
                    contentDescription = "User Icon",
                    tint = Color.LightGray
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .border(1.dp, colorResource(id = R.color.gray), RoundedCornerShape(10.dp)),
            value = nickname.value,
            onValueChange = { nickname.value = it })

        Spacer(modifier = Modifier.height(20.dp))
        TextField(
            label = { Text(text = "Password: 123abc") },
            value = password.value,
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = colorResource(id =R.color.red_500),
                unfocusedIndicatorColor = Color.Transparent,
                placeholderColor = Color.LightGray,
                focusedLabelColor = colorResource(id =R.color.red_500),
                unfocusedLabelColor = Color.LightGray,
            ),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Password Icon",
                    tint = Color.LightGray
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .border(1.dp, colorResource(id = R.color.gray), RoundedCornerShape(10.dp)),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            onValueChange = { password.value = it })

        Spacer(modifier = Modifier.height(20.dp))
        TextField(
            label = { Text(text = "Repeat Password") },
            value = repeatPassword.value,
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = colorResource(id =R.color.red_500),
                unfocusedIndicatorColor = Color.Transparent,
                placeholderColor = Color.LightGray,
                focusedLabelColor = colorResource(id =R.color.red_500),
                unfocusedLabelColor = Color.LightGray,
            ),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Password Icon",
                    tint = Color.LightGray
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .border(1.dp, colorResource(id = R.color.gray), RoundedCornerShape(10.dp)),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            onValueChange = { repeatPassword.value = it })


        //TODO: Updaing the state of the button
        Spacer(modifier = Modifier.height(20.dp))
        Text(text = "Interests", style = TextStyle(fontSize = 20.sp, fontFamily = FontFamily.Cursive))
        Spacer(modifier = Modifier.height(10.dp))
        LazyRow(modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)) {
            items(interestList.size){i->
                var myModif = Modifier
                    .padding(all = 4.dp)
                    .background(Color.White, RoundedCornerShape(8.dp))
                    .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                    .padding(all = 8.dp)
                var fontColor = Color.Black
                if (interestList[i].selected){
                        myModif = Modifier
                            .padding(all = 4.dp)
                            .background(
                                colorResource(id = R.color.red_500),
                                RoundedCornerShape(8.dp)
                            )
                            .border(1.dp, Color.White, RoundedCornerShape(8.dp))
                            .padding(all = 8.dp)
                        fontColor = Color.White
                    }
                Text(
                    color = fontColor,
                    text = interestList[i].title,
                    modifier = myModif.then(Modifier
                    .clickable {
                        interestList = interestList.mapIndexed{j, item ->
                            if (i == j){
                                item.copy(selected = !item.selected)
                            }else{
                                item
                            }
                        }
                }))
            }
        }


        Spacer(modifier = Modifier.height(20.dp))
        Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
            Button(
                onClick = {
                    if (username.value.text.isNotEmpty()
                        && password.value.text.isNotEmpty()
                        && repeatPassword.value.text.isNotEmpty()
                        && nickname.value.text.isNotEmpty()) {
                            if (password.value.text != repeatPassword.value.text){
                                Toast.makeText(mContext, "Passwords don't match", Toast.LENGTH_SHORT).show()
                                return@Button
                            }
                            signUp(username.value.text, nickname.value.text, password.value.text, mContext, interestList)
                        return@Button
                    }
                    Toast.makeText(mContext, "Please fill all fields", Toast.LENGTH_SHORT).show()
                    return@Button
                },
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.red_500)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(text = "ًRegister")
            }
        }
    }
}

fun signUp(username: String,nickname:String, passwrod: String, mContext: Context, interests: List<Interest>) {
    FirebaseMessaging.getInstance().token
        .addOnCompleteListener(OnCompleteListener<String?> { task ->
            if (!task.isSuccessful) {
                Toast.makeText(mContext, "Failed to register", Toast.LENGTH_SHORT).show()
                Log.w(TAG, "------> Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result
            //old part register
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(username, passwrod)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = FirebaseAuth.getInstance().currentUser
                        val db = FirebaseFirestore.getInstance()
                        val interestz = arrayListOf<String>()
                        interests.map { interest ->
                            if (interest.selected){
                                interestz.add(interest.title)
                            }}
                        val userMap = hashMapOf(
                            "username" to username,
                            "nickname" to nickname,
                            "password" to passwrod,
                            "interests" to interestz,
                            "fcmToken" to token
                        )
                        db.collection("users").document(user?.uid.toString()).set(userMap)
                            .addOnSuccessListener {
                                Toast.makeText(mContext, "Registered Successfully", Toast.LENGTH_SHORT).show()
                                mContext.startActivity(Intent(mContext, Login::class.java))
                            }
                            .addOnFailureListener {
                                Log.d("TAG", "======> Failed to register${it.message}")
                                Toast.makeText(mContext, "Failed to register", Toast.LENGTH_SHORT).show()
                            }
                    } else {
                        Log.d("TAG", "======> Failed to register${task.exception?.message}")
                        Toast.makeText(mContext, "Failed to register", Toast.LENGTH_SHORT).show()
                    }
                }
            //old part register

        })
}

@Preview(showBackground = true)
@Composable
fun registerPreview() {
    ProfileLabTheme {
        register("Register")
    }
}