package com.example.hivmanager.ui.screens.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout

@Composable
fun ImageContainer(
    imageBitmap: ImageBitmap,
    onCloseClick:()->Unit = {},
    modifier: Modifier = Modifier
){
    Surface(
        modifier = modifier,
        elevation = 4.dp
    ) {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val(image,closeButton) = createRefs()
            Image(
                modifier = Modifier.constrainAs(image){
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
                bitmap = imageBitmap,
                contentDescription = "image",
                contentScale = ContentScale.Crop,
            )
            IconButton(onClick = onCloseClick, modifier = Modifier.size(18.dp).constrainAs(closeButton){
                top.linkTo(parent.top,8.dp)
                end.linkTo(parent.end,8.dp)
            }) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "delete image button"
                )
            }
        }
    }
}