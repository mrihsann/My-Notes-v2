package com.ihsanarslan.mynotes.model

data class Note(
    var id: Int,
    var title: String,
    var content: String,
    var color:Int,
    var liked:Boolean
)