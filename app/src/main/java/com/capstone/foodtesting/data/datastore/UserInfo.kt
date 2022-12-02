package com.capstone.foodtesting.data.datastore

data class UserInfo (val name:String,
                     val email:String,
                     val pw:String?,
                     val gender:String?,
                     val age:Int?,
                     val birthYear:String?,
                     val birthDay:String?,
                     val photoURL:String?
                     )