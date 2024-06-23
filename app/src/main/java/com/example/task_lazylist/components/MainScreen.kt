package com.example.task_lazylist.components

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.task_lazylist.ui.theme.Task_LazyListTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainScreen(itemArray: Array<out String>) {
    val context = LocalContext.current
    val groupedItems = itemArray.groupBy { it.substringBefore(" ") }

    val onListItemClick = { text: String ->
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val displayButton = remember { derivedStateOf { listState.firstVisibleItemIndex > 5 } }

    Box(modifier = Modifier) {
        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(bottom = 50.dp)
        ) {
            groupedItems.forEach { (manufacturer, models) ->
                stickyHeader {
                    Text(
                        text = manufacturer,
                        color = Color.White,
                        modifier = Modifier
                            .background(Color.Gray)
                            .padding(5.dp)
                            .fillMaxWidth()
                    )
                }
                items(models) { model ->
                    MyListItem(item = model, onItemClick = onListItemClick)
                }
            }
        }

        AnimatedVisibility(
            visible = displayButton.value,
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            OutlinedButton(
                onClick = {
                    coroutineScope.launch { listState.scrollToItem(0) }
                },
                border = BorderStroke(1.dp, Color.Gray),
                shape = RoundedCornerShape(40), colors =
                ButtonDefaults.outlinedButtonColors(contentColor = Color.DarkGray),
                modifier = Modifier.padding(5.dp)
            ) {
                Text(text = "Top")
            }
        }

    }

}

@Composable
fun ImageLoader(item: String) {

    val url =
        "https://www.ebookfrenzy.com/book_examples/car_logos/" + item.substringBefore(" ") + "_logo.png"

    Image(
        painter = rememberAsyncImagePainter(model = url), contentDescription = "car image",
        contentScale = ContentScale.Fit,
        modifier = Modifier.size(75.dp)
    )
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun MainScreenPreview() {
    val itemArray: Array<String> = arrayOf("Cadillac Eldorado", "Ford Fairlane", "Plymouth Fury")

    Task_LazyListTheme {
        MainScreen(itemArray = itemArray)

    }
}