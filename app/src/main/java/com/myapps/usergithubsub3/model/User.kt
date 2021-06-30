package com.myapps.usergithubsub3.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    var idUser: Int = 0,
    var userName: String? = null,
    var imageUser: String? = null,
    var NameUser: String? = null,
    var following: String? = null,
    var follower: String? = null,
    var company: String? = null,
    var location: String? = null
) : Parcelable