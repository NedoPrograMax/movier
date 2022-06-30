package com.example.themovier.model

data class MovierItem(
    val title: String = "",
    val idDb: Int = 0,
    val id: String = "",
    val posterUrl: String = "",
    val note: String = "",
    val resource: String = "",
    val season: Int = 1,
    val episode: Int = 1,
    val rating: Int = 5,
    val userId: String = "",
    val type: String = "movie",
    val startDate: String = "",
    val finishDate: String = "",
    val favoriteEpisodes : List<Episode> = listOf(),
    val seasons : List<Episode> = listOf(),
    val description: String = ""
    )
/*
val unwatchedMovieList = listOf(
    MovierItem( "Pizza"),
    MovierItem( "French toast"),
    MovierItem( "Chocolate cake"),
)

val watchedMovieList = listOf(
    MovierItem( "Hamburger"),
    MovierItem( "Hot dog"),
    MovierItem( "Chicken soup"),
)

 */

