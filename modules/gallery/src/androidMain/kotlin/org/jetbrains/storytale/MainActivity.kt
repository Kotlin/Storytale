package org.jetbrains.storytale

import App
import Story
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      App()
      val story = Story("Android Story") {
        App()
      }
    }
  }
}

@Preview
@Composable
fun AppAndroidPreview() {
  App()
}
