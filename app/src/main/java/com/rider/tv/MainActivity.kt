package com.rider.tv

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import com.rider.tv.data.IptvApiService
import com.rider.tv.ui.DashboardScreen
import com.rider.tv.viewmodel.IptvViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // In a real app, use DI. Here we build manually for speed as per God Mode.
        val api = IptvApiService.create("http://poraquivamosentrando.vip") // extracted from legacy
        val viewModel = IptvViewModel(api)

        setContent {
            DashboardScreen(viewModel = viewModel)
        }
        
        // Mocking login for now to trigger data load
        viewModel.loadData("dummy_user", "dummy_pass")
    }
}
