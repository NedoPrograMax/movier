package com.example.themovier.model

data class MovieFromApi(
    val page: Int,
    val results: List<Movie>,
    val total_pages: Int,
    val total_results: Int
)