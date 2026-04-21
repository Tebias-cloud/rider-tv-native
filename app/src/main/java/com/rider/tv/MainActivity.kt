package com.rider.tv

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import com.rider.tv.data.MovieApiService
import com.rider.tv.ui.DashboardScreen
import com.rider.tv.viewmodel.MovieViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Build API client
        val api = MovieApiService.create("http://api.movieplatform.pro")
        val viewModel = MovieViewModel(api)

        setContent {
            DashboardScreen(viewModel = viewModel)
        }
        
        // Trigger data load for the current logged-in user (Test ID: e6388e35-2791-4da7-947b-053fe761aed5)
        viewModel.loadData("e6388e35-2791-4da7-947b-053fe761aed5")
    }
}
