package com.ihsanarslan.mynotes.model

data class NoteFirestore(
    var title: String,
    var content: String,
    var color:Long,
    var liked:Boolean,
    var user:String
)