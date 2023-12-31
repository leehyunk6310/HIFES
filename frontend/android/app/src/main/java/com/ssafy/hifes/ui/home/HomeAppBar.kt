package com.ssafy.hifes.ui.home

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ssafy.hifes.R
import com.ssafy.hifes.ui.HifesDestinations
import com.ssafy.hifes.ui.main.MainViewModel
import com.ssafy.hifes.ui.theme.pretendardFamily


private const val TAG = "HomeAppBar_하이페스"

@Preview
@Composable
fun HomePrev() {
//    HomeAppBar(rememberNavController(),"")
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun HomeAppBar(
    navController: NavController,
    viewModel: MainViewModel,
    submit: (keyword: String) -> Unit = {}
) {
    val image: Painter = painterResource(id = R.drawable.icon_search)
    val otherImage: Painter = painterResource(id = R.drawable.icon_mypage)
    val keyboardController = LocalSoftwareKeyboardController.current
    val keyword = viewModel.searchKeyword.observeAsState()

    CenterAlignedTopAppBar(
        title = { Text(text = "My App") },
        modifier = Modifier.height(60.dp).background(color = Color.White),
        actions = {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .size(60.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                var text by remember { mutableStateOf(keyword.value ?: "") }
                Spacer(modifier = Modifier.size(18.dp))
                TextField(
                    value = text,
                    onValueChange = { text = it },
                    textStyle = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = pretendardFamily,
                        fontWeight = FontWeight.Normal
                    ),
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = androidx.compose.ui.text.input.ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = {
                        submit(text)
                        keyboardController?.hide()
                    }),
                    leadingIcon = {
                        IconButton(onClick = { submit(text) }) {
                            Icon(
                                painter = image,
                                contentDescription = "Trailing icon",
                                modifier = Modifier.fillMaxSize(0.7f)
                            )
                        }
                    },
                    placeholder = {
                        Text(
                            stringResource(id = R.string.home_app_bar),
                            fontFamily = pretendardFamily,
                            fontWeight = FontWeight.Light,
                            fontSize = 14.sp
                        )
                    },
                    singleLine = true,
                    modifier = Modifier
                        .clip(RoundedCornerShape(30.dp))
                        .border(1.dp, Color.Gray, RoundedCornerShape(30.dp))
                        .background(color = Color.White)
                        .height(50.dp)
                        .align(Alignment.CenterVertically)
                        .weight(1f), // TextField를 Row의 남은 공간에 채우도록 함
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        disabledContainerColor = Color.White,
                        focusedIndicatorColor = Color.White
                    )
                )

                IconButton(onClick = {
                    navController.navigate(HifesDestinations.MY_PAGE_ROUTE)
                }) {
                    Icon(
                        painter = otherImage,
                        contentDescription = "Other icon",
                        modifier = Modifier.fillMaxSize(1f)
                    )
                }
            }
        }
    )
}



