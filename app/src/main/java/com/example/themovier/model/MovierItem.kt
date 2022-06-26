package com.example.themovier.model

data class MovierItem(val title: String)

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

