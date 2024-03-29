package com.example.profilelab

import android.app.StatusBarManager
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.ContactsContract.CommonDataKinds.Email
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
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
import androidx.core.content.ContextCompat.startActivity
import com.example.profilelab.ui.theme.ProfileLabTheme
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import javax.security.auth.callback.Callback


class Login : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ProfileLabTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    login("Android")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun login(name: String, modifier: Modifier = Modifier) {
    val mContext = LocalContext.current

    Box(modifier = Modifier.fillMaxSize()) {
        ClickableText(
            text = AnnotatedString("Sign up here"),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(20.dp),
            onClick = {
                val nav = Intent(mContext, Register::class.java)
                mContext.startActivity(nav)
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

        var enabledd  by rememberSaveable { mutableStateOf(true) }
        val username = remember { mutableStateOf(TextFieldValue()) }
        val password = remember { mutableStateOf(TextFieldValue()) }

        Text(text = "Login", style = TextStyle(fontSize = 40.sp, fontFamily = FontFamily.Serif))

        Spacer(modifier = Modifier.height(20.dp))
        OutlinedTextField(
            label = { Text(text = "Email") },
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent,
                placeholderColor = Color.LightGray,

                unfocusedIndicatorColor = Color.LightGray,
                unfocusedLabelColor = Color.LightGray,
                unfocusedLeadingIconColor = Color.LightGray,

                focusedLabelColor = colorResource(id =R.color.red_500),
                focusedIndicatorColor = colorResource(id =R.color.red_500),
                focusedLeadingIconColor = colorResource(id =R.color.red_500),
            ),
            modifier = Modifier
                .fillMaxWidth(),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = "Email Icon",
                    tint = Color.LightGray,
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            value = username.value,
            onValueChange = { username.value = it })


        Spacer(modifier = Modifier.height(20.dp))
        OutlinedTextField(
            label = { Text(text = "Password") },
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent,
                placeholderColor = Color.LightGray,

                unfocusedIndicatorColor = Color.LightGray,
                unfocusedLabelColor = Color.LightGray,
                unfocusedLeadingIconColor = Color.LightGray,

                focusedLabelColor = colorResource(id =R.color.red_500),
                focusedIndicatorColor = colorResource(id =R.color.red_500),
                focusedLeadingIconColor = colorResource(id =R.color.red_500),
            ),
            modifier = Modifier
                .fillMaxWidth(),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Password Icon",
                    tint = Color.LightGray
                )
            },
            value = password.value,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            onValueChange = { password.value = it })


        Spacer(modifier = Modifier.height(20.dp))
        Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
            Button(
                onClick = {
                    if (username.value.text.isNotEmpty() && password.value.text.isNotEmpty()){
                        signIn(username.value.text, password.value.text, mContext) {
                            enabledd = true
                        }
                        return@Button
                    }else{
                        Toast.makeText(mContext, "Please fill all the fields", Toast.LENGTH_SHORT).show()
                    }

                },
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.red_500)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .clickable(
                        enabled = enabledd,
                        onClick = { enabledd = false }
                    )
            ) {
                Text(text = "Login")
            }
        }


        Spacer(modifier = Modifier.height(20.dp))
        ClickableText(
            text = AnnotatedString("Forgot password?"),
            onClick = {
//                val nav = Intent(mContext, MainActivity::class.java)
//                mContext.startActivity(nav)
            },
            style = TextStyle(
                fontSize = 14.sp,
                fontFamily = FontFamily.Default
            )
        )
    }
}

fun signIn(username: String, password: String, mContext: Context, listerner: ()->Unit) {
    FirebaseAuth.getInstance().signInWithEmailAndPassword(username, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val usr = task.result?.user

                FirebaseMessaging.getInstance().token
                    .addOnCompleteListener(OnCompleteListener<String?> { task ->
                        if (!task.isSuccessful) {
                            Toast.makeText(mContext, "FCM Token Error", Toast.LENGTH_SHORT).show()
                            Log.w(TAG, "------> Fetching FCM registration token failed", task.exception)
                            return@OnCompleteListener

                        }else{
                            val token = task.result

                            Log.e(TAG, "------> FCM token: $token | FIREAUTH: ${usr?.uid.toString()}")

                            //TODO update the user fcm token
                            FirebaseFirestore.getInstance()
                                .collection("users")
                                .document(usr?.uid.toString())
                                .update("fcmToken", token)
                                .addOnSuccessListener {
                                    Log.d(TAG, "DocumentSnapshot successfully updated!")

                                    //TODO get the current user data
                                    Toast.makeText(mContext, "Login successful", Toast.LENGTH_SHORT).show()
                                    val nav = Intent(mContext, MainActivity::class.java)
                                    mContext.startActivity(nav)

                                }.addOnFailureListener(OnFailureListener { e ->
                                    Toast.makeText(mContext, "Failed to Login", Toast.LENGTH_SHORT).show()
                                })
                        }
                    })

            } else {
                Toast.makeText(mContext, "Unsuccessful Authentication", Toast.LENGTH_SHORT).show()
            }
            listerner()
        }
}

@RequiresApi(Build.VERSION_CODES.Q)
@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    ProfileLabTheme{
        login("Android")
    }
}