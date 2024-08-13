package com.example.musiclly.model



data class CategoryModel(
    val coverurl : String,
    val name : String,
    var songs : List<String>
) {
    constructor() : this("","", listOf())
}