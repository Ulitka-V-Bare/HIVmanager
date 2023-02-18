package com.example.hivmanager.ui.screens.components

import android.os.Build.VERSION.SDK_INT
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import coil.size.Size
import com.example.hivmanager.R

/***
 * гифка загрузки в чате
 */

@Composable
fun LoadingGif(
    modifier: Modifier = Modifier,
    ) {
        val context = LocalContext.current
    val imageLoader = ImageLoader.Builder(context)
        .components {
            if (SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .build()
    Image(
        painter = rememberAsyncImagePainter(
            ImageRequest.Builder(context).data(data = if(isSystemInDarkTheme()) R.drawable.spinner_dark_mode else R.drawable.spinner_light_mode).apply(block = {
                size(Size.ORIGINAL)
            }).build(), imageLoader = imageLoader
        ),
        contentDescription = null,
        modifier = modifier.fillMaxWidth(),
    )
//    var atEnd by remember {
//        mutableStateOf(false)
//    }
//    val image = AnimatedImageVector.animatedVectorResource(R.drawable.spinner_1s_204px)
//    Icon(
//        painter = rememberAnimatedVectorPainter(image,false),
//      //  painter = painterResource(id = R.drawable.spinner),
//        contentDescription = null
//    )
}