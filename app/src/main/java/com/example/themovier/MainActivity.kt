package com.example.themovier

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.themovier.constants.Constants
import com.example.themovier.model.BottomNavItem
import com.example.themovier.navigation.MovierNavigation
import com.example.themovier.ui.theme.TheMovierTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MovierApp()
        }
    }
}

@Composable
fun MovierApp(){
    TheMovierTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
           val navController = rememberNavController()
            Scaffold(
                bottomBar = {
                    BottomNavigationBar(
                        items = Constants.navBottomItemsList,
                        navController = navController){
                        navController.navigate(it.route)
                    }
                }
            ) {
                it
                MovierNavigation(navController = navController)
            }
        }
    }
}

@Composable
fun BottomNavigationBar(
    items: List<BottomNavItem>,
    navController: NavController,
    modifier: Modifier = Modifier,
    onItemClick: (BottomNavItem) -> Unit
){
    val backStackEntry = navController.currentBackStackEntryAsState()
    val route = backStackEntry.value?.destination?.route
    if (
        Constants.navBottomItemsList.any {item->
            item.route == route
        }
    ){
    BottomNavigation(
        modifier = modifier,
        backgroundColor = Color.DarkGray,
        elevation = 5.dp
    ) {
        items.forEach {item->
            val selected = (item.route == route)
            BottomNavigationItem(
                selected = selected,
                onClick = { onItemClick(item) },
            icon = {
                   Column(horizontalAlignment = Alignment.CenterHorizontally) {
                       if (item.badgeCount > 0){
                           BadgedBox(badge = {
                               Text(text = item.badgeCount.toString())
                           } ) {
                               Icon(imageVector = item.icon, contentDescription = item.name + " Icon")
                           }
                       }
                       else{
                           Icon(imageVector = item.icon, contentDescription = item.name + " Icon")
                       }
                       if (selected){
                           Text(
                               text = item.name,
                               textAlign = TextAlign.Center,
                           fontSize = 10.sp)
                       }
                   }
            },
            selectedContentColor = Color.Red,
                unselectedContentColor = Color.Gray
                )
        }
    }
    }
    else{
        Box{}
    }
}

