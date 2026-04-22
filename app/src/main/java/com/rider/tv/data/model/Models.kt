package com.rider.tv.data.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Category(
    @SerializedName("category_id") @SerialName("category_id") val id: String,
    @SerializedName("category_name") @SerialName("category_name") val name: String,
    @SerializedName("parent_id") @SerialName("parent_id") val parentId: Int = 0
)

@Serializable
data class Stream(
    @SerializedName("num") @SerialName("num") val num: Int,
    @SerializedName("name") @SerialName("name") val name: String,
    @SerializedName("stream_type") @SerialName("stream_type") val streamType: String,
    @SerializedName("stream_id") @SerialName("stream_id") val streamId: Int,
    @SerializedName("stream_icon") @SerialName("stream_icon") val streamIcon: String?,
    @SerializedName("category_id") @SerialName("category_id") val categoryId: String,
    @SerializedName("container_extension") @SerialName("container_extension") val containerExtension: String? = "mp4"
)

@Serializable
data class ExternalAccount(
    @SerializedName("username") @SerialName("username") val username: String,
    @SerializedName("password") @SerialName("password") val password: String,
    @SerializedName("portal_url") @SerialName("portal_url") val portalUrl: String
)
