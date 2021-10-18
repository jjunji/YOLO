package com.yolo.yolo_android.data.datasource.remote

import com.yolo.yolo_android.data.ResultData
import com.yolo.yolo_android.model.*
import io.reactivex.Single
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface YoloDataSource {
    fun signup(queryMap: HashMap<String, String>): Single<ResultData<SignupResponse>>
    fun login(queryMap: HashMap<String, String>): Single<ResultData<LoginResponse>>
    fun getHomeInfo(): Single<ResultData<HomeResponse>>
    fun deleteAccount(): Single<ResultData<BaseResponse>>

    suspend fun uploadPost(images: List<MultipartBody.Part>,
                           params: HashMap<String, RequestBody>,
                           onStart: () -> Unit,
                           onComplete: () -> Unit
    ): Flow<ResultData<CommonResponse>>

    suspend fun deletePost(postId: Int,
                           onStart: () -> Unit,
                           onComplete: () -> Unit
    ): Flow<ResultData<CommonResponse>>

    suspend fun likePost(postId: Int,
                         isLike: Boolean,
                         onStart: () -> Unit,
                         onComplete: () -> Unit
    ): Flow<ResultData<CommonResponse>>

    suspend fun getCommentList(postId: Int,
                               onStart: () -> Unit,
                               onComplete: () -> Unit
    ): Flow<ResultData<CommentListResponse>>

    suspend fun postComment(postId: Int,
                            param: HashMap<String, RequestBody>,
                            image: MultipartBody.Part?,
                            onStart: () -> Unit,
                            onComplete: () -> Unit
    ): Flow<ResultData<PostCommentResponse>>

    suspend fun deleteComment(commentId: Int,
                              onStart: () -> Unit,
                              onComplete: () -> Unit
    ): Flow<ResultData<CommonResponse>>

    suspend fun getMyProfile(onStart: () -> Unit,
                             onComplete: () -> Unit
    ): Flow<ResultData<ProfileResponse>>

    suspend fun updateProfile(param: HashMap<String, RequestBody>,
                              image: MultipartBody.Part?,
                              onStart: () -> Unit,
                              onComplete: () -> Unit

    ): Flow<ResultData<CommonResponse>>

    suspend fun deleteProfileImage(imageUrl: String,
                                   onStart: () -> Unit,
                                   onComplete: () -> Unit
    ): Flow<ResultData<CommonResponse>>

    suspend fun getTripDetail(contentId: Int,
                              contentTypeId: Int,
                              onStart: () -> Unit,
                              onComplete: () -> Unit
    ): Flow<ResultData<HomeDetailResponse>>

    suspend fun getNotice(onStart: () -> Unit,
                          onComplete: () -> Unit
    ): Flow<ResultData<NoticeResponse>>
}