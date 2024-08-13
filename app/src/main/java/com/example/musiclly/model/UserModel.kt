package com.example.musiclly.model

import java.security.Timestamp

data class UserModel(
    var phone: String = "",
    var username: String = "",
    var createdTimestamp: Timestamp? = null,
    var userId: String = "",
    var fcmToken: String = ""
) {
    constructor(phone: String, username: String, createdTimestamp: Timestamp, userId: String) : this(
        phone, username, createdTimestamp, userId, ""
    )
}
