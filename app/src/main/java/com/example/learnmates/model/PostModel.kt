package com.example.learnmates.model

import android.os.Parcel
import android.os.Parcelable


data class PostModel (
    var postId: String = "",
    var postText: String = "",
    var postImage: String = "",
    var postMember: String = "",
    var userName: String = "",
    var likeCount: Int,
    var savePost: Boolean
): Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readByte() != 0.toByte()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(postId)
        parcel.writeString(postText)
        parcel.writeString(postImage)
        parcel.writeString(postMember)
        parcel.writeString(userName)
        parcel.writeInt(likeCount)
        parcel.writeByte(if (savePost) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PostModel> {
        override fun createFromParcel(parcel: Parcel): PostModel {
            return PostModel(parcel)
        }

        override fun newArray(size: Int): Array<PostModel?> {
            return arrayOfNulls(size)
        }
    }
}