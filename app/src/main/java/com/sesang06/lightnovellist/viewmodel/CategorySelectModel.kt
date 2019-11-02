package com.sesang06.lightnovellist.viewmodel

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CategorySelectModel(
    val category: String,
    var isSelected: Boolean = false
): Parcelable