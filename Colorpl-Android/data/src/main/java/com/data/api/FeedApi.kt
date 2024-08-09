package com.data.api

import com.data.model.paging.ResponsePagedComment
import com.data.model.paging.ResponsePagedFeed
import com.data.model.request.RequestCreateComment
import com.data.model.request.RequestReviewEdit
import com.data.model.response.ResponseCommentEdit
import com.data.model.response.ResponseReviewCreate
import com.data.model.response.ResponseReviewDetail
import com.data.model.response.ResponseReviewEdit
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface FeedApi {

    // 모든 리뷰 조회
    @GET("reviews/all")
    suspend fun getAllFeedData(
        @Query("page") page: Int,
        @Query("size") size: Int
    ): ResponsePagedFeed

    // 리뷰 생성
    @Multipart
    @POST("reviews/create")
    suspend fun createUserFeedData(
        @Part("request") request: RequestBody,
        @Part file: MultipartBody.Part?,
    ): ResponseReviewCreate

    // 특정멤버의 모든 리뷰 조회
    @GET("reviews/members/{memberId}")
    suspend fun getUserFeedData(
        @Path("memberId") memberId: Int,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): ResponsePagedFeed

    // 특정멤버의 모든 리뷰 조회
    @GET("reviews/myreviews")
    suspend fun getMyFeedData(
        @Query("page") page: Int,
        @Query("size") size: Int
    ): ResponsePagedFeed

    // 특정 리뷰 상세 조회
    @GET("reviews/details/{reviewId}")
    suspend fun getDetailFeedData(
        @Path("reviewId") reviewId: Int,
    ): ResponseReviewDetail

    // 리뷰 수정
    @PUT("reviews/update/{reviewId}")
    suspend fun editUserFeedData(
        @Path("reviewId") reviewId: Int,
        @Body requestReviewEdit: RequestReviewEdit
    ): ResponseReviewEdit

    // 리뷰 삭제
    @DELETE("reviews/delete/{reviewId}")
    suspend fun deleteFeedData(
        @Path("reviewId") reviewId: Int,
    ): ResponseReviewEdit

    // 공감 추가
    @POST("reviews/empathize/{reviewId}")
    suspend fun createEmpathizeData(
        @Path("reviewId") reviewId: Int,
    ): ResponseReviewEdit

    // 공감 취소
    @DELETE("reviews/empathize/{reviewId}")
    suspend fun deleteEmpathizeData(
        @Path("reviewId") reviewId: Int,
    ): ResponseReviewEdit

    // 특정 리뷰 댓글 달기
    @POST("comments/reviews/{reviewId}")
    suspend fun createCommentData(
        @Path("reviewId") reviewId: Int,
        @Body requestCreateComment: RequestCreateComment
    ): ResponseCommentEdit

    // 특정 리뷰 댓글 가져오기
    @GET("comments/reviews/{reviewId}")
    suspend fun getCommentData(
        @Path("reviewId") reviewId: Int,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): ResponsePagedComment

    // 특정 댓글 수정
    @PUT("comments/reviews/{reviewId}/{commentId}")
    suspend fun editCommentData(
        @Path("reviewId") reviewId: Int,
        @Path("commentId") commentId: Int,
        @Body requestEditComment: RequestCreateComment
    ): ResponseCommentEdit

    // 특정 댓글 삭제
    @DELETE("comments/{commentId}")
    suspend fun deleteCommentData(
        @Path("commentId") commentId: Int,
    ): ResponseCommentEdit
}