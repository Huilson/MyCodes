package com.example.minhabomba2.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.minhabomba2.ui.bomba.details.BombaDetailsScreen
import com.example.minhabomba2.ui.bomba.form.BombaFormScreen
import com.example.minhabomba2.ui.bomba.list.BombaListScreen

@Composable
fun NavGraphic(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = "listBomba",
        modifier = modifier
    ){
        composable(route = "listBomba"){
            BombaListScreen(
                onAddPressed = {
                    val id = 0
                    navController.navigate("bombaForm?id=$id")
                },
                onItemPressed = { abastecer ->
                    navController.navigate("bombaDetails/${abastecer.id}")
                }
            )
        }

        composable(route = "bombaDetails/{id}",
            arguments = listOf(navArgument(name = "id"){type = NavType.IntType})
        ){
            BombaDetailsScreen(
                onBackPressed = { navController.popBackStack() },
                onItemDeleted = {navController.navigate("listBomba"){
                    popUpTo(navController.graph.findStartDestination().id){
                        inclusive = true
                    } } },
                onEditPressed = {
                    val id = it.arguments?.getInt("id") ?: 0
                    navController.navigate("bombaForm?id=$id")
                }
            )
        }

        composable(
            route = "bombaForm?id={id}",
            arguments = listOf(
                navArgument(name = "id"){type = NavType.StringType; nullable = false}
            )
        ){
            BombaFormScreen(
                onBackPressed = { navController.popBackStack() },
                onItemSaved = {navController.navigate("listBomba"){
                    popUpTo(navController.graph.findStartDestination().id){
                        inclusive = true
                    } } }
            )
        }
    }
}