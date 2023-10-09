package ksnd.hiraganaconverter.view.navigation

sealed class NavRoute(val route: String) {
    data object Converter: NavRoute(route = "converter")
    data object History: NavRoute(route = "history")
    data object Setting: NavRoute(route = "setting")
}
