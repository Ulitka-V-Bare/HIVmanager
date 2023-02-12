package com.example.hivmanager.ui.screens.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.hivmanager.R

@Preview
@Composable
fun MyTopAppBar(header:String = "Header", modifier: Modifier = Modifier) {
    TopAppBar(
        backgroundColor = MaterialTheme.colors.background,
        modifier = modifier
    ) {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (image, text) = createRefs()
            Image(
                painter = painterResource(id = if(isSystemInDarkTheme())R.drawable.logo_light else R.drawable.logo_no_background),
                contentDescription = "logo",
                modifier = Modifier
                    .size(50.dp)
                    .constrainAs(image) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end)
                    }
            )
            Text(
                text = header,
                fontSize = 20.sp,
                modifier = Modifier.constrainAs(text) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                }
            )
        }

    }
}