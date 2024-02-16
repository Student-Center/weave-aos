package com.studentcenter.weave.domain.extension

fun emoji(unicode: Int): String{
    return String(Character.toChars(unicode))
}