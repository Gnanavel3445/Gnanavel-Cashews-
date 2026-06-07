package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.example.ui.CashewAppMain
import com.example.viewmodel.CashewViewModel

class MainActivity : ComponentActivity() {
  private val viewModel: CashewViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      CashewAppMain(viewModel = viewModel, modifier = Modifier.fillMaxSize())
    }
  }
}

