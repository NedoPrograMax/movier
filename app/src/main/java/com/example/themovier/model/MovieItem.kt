package com.example.themovier.model

import androidx.annotation.DrawableRes

data class MovieItem(val title: String)

val unwatchedMovieList = listOf(
    MovieItem( "Pizza"),
    MovieItem( "French toast"),
    MovieItem( "Chocolate cake"),
)

val watchedMovieList = listOf(
    MovieItem( "Hamburger"),
    MovieItem( "Hot dog"),
    MovieItem( "Chicken soup"),
)

