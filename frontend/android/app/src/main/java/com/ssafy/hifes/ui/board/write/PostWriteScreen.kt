package com.ssafy.hifes.ui.board.write

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Divider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.gowtham.ratingbar.RatingBar
import com.gowtham.ratingbar.StepSize
import com.ssafy.hifes.R
import com.ssafy.hifes.ui.board.BoardViewModel
import com.ssafy.hifes.ui.board.boardcommon.PostType
import com.ssafy.hifes.ui.common.top.TopWithBack
import com.ssafy.hifes.ui.detail.Image
import com.ssafy.hifes.ui.iconpack.MyIconPack
import com.ssafy.hifes.ui.iconpack.myiconpack.Emptystar
import com.ssafy.hifes.ui.iconpack.myiconpack.Filledstar
import com.ssafy.hifes.ui.iconpack.myiconpack.Imageadd
import com.ssafy.hifes.ui.theme.PrimaryPink
import com.ssafy.hifes.ui.theme.pretendardFamily


private const val TAG = "PostWriteScreen"

@Composable
fun PostWriteScreen(
    navController: NavController,
    viewModel: BoardViewModel
) {
    val boardType = viewModel.boardType.observeAsState()

    Scaffold(
        topBar = {
            TopWithBack(
                navController,
                title = stringResource(id = R.string.board_write_appbar_title),
                btn = true,
                btnText = stringResource(id = R.string.board_write_finish),
                onClick = {})
        },
        content = { it ->
            Column(
                modifier = Modifier.padding(it),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (boardType.value == PostType.ASK) {
                    Column(modifier = Modifier.fillMaxWidth().padding(10.dp, 5.dp), horizontalAlignment = Alignment.End) {
                        setHiddenPostWrite()
                    }
                    Spacer(modifier = Modifier.size(20.dp))
                }
                if (boardType.value == PostType.REVIEW) {
                    Spacer(modifier = Modifier.size(30.dp))
                    RatingPostWrite()
                    Spacer(modifier = Modifier.size(30.dp))
                }
                ImagePostWrite()
                Spacer(modifier = Modifier.size(20.dp))
                Divider()
                Spacer(modifier = Modifier.size(4.dp))
                TextFieldPostTitle()
                Spacer(modifier = Modifier.size(4.dp))
                Divider()
                TextFieldPostContent()
            }
        }
    )
}

@Composable
fun RatingPostWrite() {
    var rating: Float by remember { mutableStateOf(5f) }
    RatingBar(
        value = rating,
        onValueChange = {
            rating = it
        },
        onRatingChanged = {
            Log.d(TAG, "RatingPostWrite: ${it}")
        },
        stepSize = StepSize.HALF,
        size = 40.dp,
        painterEmpty = rememberVectorPainter(MyIconPack.Emptystar),
        painterFilled = rememberVectorPainter(MyIconPack.Filledstar),
        spaceBetween = 0.dp
    )
}

@Composable
fun setHiddenPostWrite() {
    var isHidden by remember { mutableStateOf(false) }
    val stateText = if(isHidden) "비공개" else "공개"

    Row(verticalAlignment = Alignment.CenterVertically) {
        Spacer(modifier = Modifier.size(10.dp))
        Text(text = stateText, modifier = Modifier.weight(1f), fontFamily = pretendardFamily, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.size(10.dp))
        Switch(
            checked = isHidden,
            onCheckedChange = {
                isHidden = !isHidden
            },
            colors = SwitchDefaults.colors(
                checkedBorderColor = PrimaryPink,
                checkedThumbColor = Color.White,
                checkedTrackColor = PrimaryPink,
                uncheckedThumbColor = PrimaryPink,
                uncheckedBorderColor = PrimaryPink,
                uncheckedTrackColor = Color.White
            )
        ) 
    }

}

@Composable
fun ImagePostWrite() {
    Image(image = rememberVectorPainter(MyIconPack.Imageadd))
}

@Composable
fun TextFieldPostTitle() {
    var text by remember { mutableStateOf("") }
    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = text,
        onValueChange = {
            text = it
        },
        textStyle = TextStyle(fontFamily = pretendardFamily, fontWeight = FontWeight.Normal),
        placeholder = { Text(text = stringResource(id = R.string.board_write_text_field_title_hint)) },
        colors = TextFieldDefaults.colors(
            disabledTextColor = Color.Transparent,
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            disabledContainerColor = Color.White,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
        )
    )
}

@Composable
fun TextFieldPostContent() {
    var text by remember { mutableStateOf("") }
    TextField(
        modifier = Modifier.fillMaxWidth(),
        textStyle = TextStyle(fontFamily = pretendardFamily, fontWeight = FontWeight.Normal),
        value = text,
        onValueChange = {
            text = it
        },
        placeholder = { Text(text = stringResource(id = R.string.board_write_text_field_content_hint), fontFamily = pretendardFamily, fontWeight = FontWeight.Normal) },
        colors = TextFieldDefaults.colors(
            disabledTextColor = Color.Transparent,
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            disabledContainerColor = Color.White,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
        )
    )
}

@Composable
@Preview
fun PreviewPostWriteScreen() {
    val viewModel = BoardViewModel()
    viewModel.getReviewPostList()
    PostWriteScreen(
        navController = rememberNavController(),
        viewModel = viewModel
    )
}