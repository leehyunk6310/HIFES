package com.ssafy.hifes.ui.common

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ssafy.hifes.ui.theme.PrimaryPink

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChipsSelectable(chips: List<String>, onChipClick : (index : Int) -> Any) {
    var selected by rememberSaveable { mutableStateOf(0) }

    LazyRow() {
        itemsIndexed(chips) { index, item ->
            FilterChip(
                selected = selected == index,
                onClick = {
                    selected = index
                    onChipClick(index)
                          },
                label = { Text(item) },
                colors = FilterChipDefaults.filterChipColors(selectedContainerColor = PrimaryPink, selectedLabelColor = Color.White)
                )
            Spacer(modifier = Modifier.width(16.dp))
        }
    }

}
@Composable
fun Chip(
    text: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, Color.LightGray)
    ) {
        Text(
            text = text,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
        )
    }
}
@Composable
fun HashtagChips(chips: List<String>) {
   LazyRow() {
       items(chips) {item ->
           Chip(text = item)
           Spacer(modifier = Modifier.width(16.dp))
       }
   }
}

@Preview
@Composable
fun ChipPrev() {
    Column {
        ChipsSelectable(listOf("전체", "화장실", "부스", "입구"), {})
        HashtagChips(chips = listOf("#6명", "대구치맥파티", "#치킨", "맥주", "20대", "#6명", "대구치맥파티", "#치킨", "맥주", "20대"))
    }

}