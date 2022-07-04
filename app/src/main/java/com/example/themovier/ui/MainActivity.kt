package com.example.themovier.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.themovier.ui.navigation.MovierNavigation
import com.example.themovier.ui.theme.TheMovierTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val selectImageLauncher:ActivityResultLauncher<Intent> = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result->
        if (result.resultCode == RESULT_OK && result.data != null){
          imageState.value = result.data?.data
        }
    }

    private val imageState = mutableStateOf<Uri?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MovierApp(selectImageLauncher, imageState)
        }
    }
}

@Composable
fun MovierApp(selectImageLauncher: ActivityResultLauncher<Intent>, imageState: MutableState<Uri?>) {
    TheMovierTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
           val navController = rememberNavController()
            MovierNavigation(navController = navController, selectImageLauncher, imageState)
    /*        val backStackEntry = navController.currentBackStackEntryAsState()
            val route = backStackEntry.value?.destination?.route
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
                MovierNavigation(navController = navController, selectImageLauncher, imageState)
            }

     */
        }
    }



}
/*
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

 */

