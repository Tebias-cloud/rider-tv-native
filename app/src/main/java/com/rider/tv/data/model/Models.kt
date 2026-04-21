package com.rider.tv.data.model

import com.google.gson.annotations.SerializedName

data class Category(
    @SerializedName("category_id") val id: String,
    @SerializedName("category_name") val name: String,
    @SerializedName("parent_id") val parentId: Int = 0
)

data class Stream(
    @SerializedName("num") val num: Int,
    @SerializedName("name") val name: String,
    @SerializedName("stream_type") val streamType: String,
    @SerializedName("stream_id") val streamId: Int,
    @SerializedName("stream_icon") val streamIcon: String?,
    @SerializedName("category_id") val categoryId: String
)

data class ExternalAccount(
    @SerializedName("username") val username: String,
    @SerializedName("password") val password: String,
    @SerializedName("portal_url") val portalUrl: String
)
