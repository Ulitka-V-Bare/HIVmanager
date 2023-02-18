package com.example.hivmanager.ui.screens.components

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Download
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout

/** контейнер для изображения, используется в чате,
 * две версии для приема ImageBitmap, если изображение скачано, и painter, если локальное
 * */
@Composable
fun ImageContainer(
    imageBitmap: ImageBitmap,
    imageName: String = "",
    onCloseClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    onSaveClick: (Bitmap) -> Unit = {}
) {
    Surface(
        modifier = modifier,
        elevation = 4.dp
    ) {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (image, closeButton) = createRefs()
            Image(
                modifier = Modifier
                    .constrainAs(image) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .heightIn(350.dp)
                    .widthIn(270.dp),
                bitmap = imageBitmap,
                contentDescription = "image",
                contentScale = ContentScale.Crop,
            )
            if (onCloseClick != null) {
                IconButton(onClick = onCloseClick, modifier = Modifier
                    .size(18.dp)
                    .constrainAs(closeButton) {
                        top.linkTo(parent.top, 8.dp)
                        end.linkTo(parent.end, 8.dp)
                    }) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "delete image button"
                    )
                }
            } else {
                Surface(elevation = 6.dp, modifier = Modifier.constrainAs(closeButton) {
                    top.linkTo(parent.top, 8.dp)
                    end.linkTo(parent.end, 8.dp)
                }.clickable { onSaveClick(imageBitmap.asAndroidBitmap()) }
                ) {
                        Icon(
                            modifier = Modifier
                                .size(23.dp),
                            imageVector = Icons.Filled.Download,
                            contentDescription = "delete image button"
                        )

                }

            }
        }
    }
}

@Composable
fun ImageContainer(
    painterResource: Int,
    onCloseClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        elevation = 4.dp
    ) {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (image, closeButton) = createRefs()
            Image(
                modifier = Modifier
                    .constrainAs(image) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .heightIn(350.dp)
                    .widthIn(270.dp),
                painter = androidx.compose.ui.res.painterResource(painterResource),
                contentDescription = "image",
                contentScale = ContentScale.Crop,
            )
            if (onCloseClick != null) {
                IconButton(onClick = onCloseClick, modifier = Modifier
                    .size(18.dp)
                    .constrainAs(closeButton) {
                        top.linkTo(parent.top, 8.dp)
                        end.linkTo(parent.end, 8.dp)
                    }) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "delete image button"
                    )
                }
            }
        }
    }
}