package com.mehul.demoapplication.model

import androidx.databinding.BaseObservable


class Item : BaseObservable() {
    var title: String? = null
    var description: String? = null
    var imageHref: String? = null


}