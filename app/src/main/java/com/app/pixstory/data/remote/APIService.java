package com.app.pixstory.data.remote;

import com.app.pixstory.data.model.PageDetailResponse;
import com.app.pixstory.data.model.PageMemberResponse;
import com.app.pixstory.data.model.PageSearchResponse;
import com.app.pixstory.data.model.api.AddInterestResponse;
import com.app.pixstory.data.model.api.UserHomeListResponse;
import com.app.pixstory.data.model.add_support.SupportResponse;
import com.app.pixstory.data.model.api.AccountDetailResponse;
import com.app.pixstory.data.model.api.BioResponse;
import com.app.pixstory.data.model.api.CitationResponse;
import com.app.pixstory.data.model.api.CountryResponse;
import com.app.pixstory.data.model.api.EducationResponse;
import com.app.pixstory.data.model.api.EmailLoginResponse;
import com.app.pixstory.data.model.api.EmailPasswordLoginResponse;
import com.app.pixstory.data.model.api.FAQResponse;
import com.app.pixstory.data.model.api.ForgetPasswordResponse;
import com.app.pixstory.data.model.api.ImageUploadResponse;
import com.app.pixstory.data.model.api.LoginResponse;
import com.app.pixstory.data.model.add_user_interest.AddUserInterestResponse;
import com.app.pixstory.data.model.api.UserPrefrenceResponse;
import com.app.pixstory.data.model.api.UserProfileResponse;
import com.app.pixstory.data.model.api.WorkExperienceResponse;
import com.app.pixstory.data.model.api.messages.MessageResponse;
import com.app.pixstory.data.model.create_page.PageResponse;
import com.app.pixstory.data.model.get_story_user.StoryUserResponse;
import com.app.pixstory.data.model.global_search.GlobalInterestResponse;
import com.app.pixstory.data.model.home.HomeResponse;
import com.app.pixstory.data.model.link_user_interest.LinkInterestResponse;
import com.app.pixstory.data.model.page_list.PageListResponse;
import com.app.pixstory.data.model.story.StoryResponse;
import com.app.pixstory.data.model.upload.MyPhotosModel;
import com.app.pixstory.data.model.api.SendOtpEmailResponse;
import com.app.pixstory.data.model.api.StaticPageResponse;
import com.app.pixstory.data.model.access.AccessRequest;
import com.app.pixstory.data.model.interest.InterestRequest;
import com.app.pixstory.data.model.upload_details.DetailResponse;
import com.app.pixstory.data.model.upload_image.UploadImageRequest;
import com.app.pixstory.data.model.user_count.UserCountResponse;
import com.app.pixstory.data.model.version_check.AppVersionResponse;
import com.app.pixstory.data.model.you_user.UserFollowersResponse;
import com.app.pixstory.data.model.you_user.UserFollowingResponse;
import com.app.pixstory.data.model.you_user.UserFriendResponse;
import com.app.pixstory.ui.dashboard.you.activity.PageStoryResponse;

import io.reactivex.Flowable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Author : Arvindo Mondal
 * Created on 20-02-2020
 */
public interface APIService {

    @GET("register/mobile/send-otp-number")
    Flowable<AppVersionResponse> getAppVersion();

    @POST("register/mobile/send-otp-number")
    @FormUrlEncoded
    Flowable<AccessRequest> verificationRequestMobile(
            @Field("country_code") String countryCode,
            @Field("phone") String phone,
            @Field("type") String type
    );

    @POST("register/mobile/verify-otp")
    @FormUrlEncoded
    Flowable<AccessRequest> verificationRequestMobileOtp(
            @Field("country_code") String countryCode,
            @Field("phone") String phone,
            @Field("otp") String otp
    );

    @POST("register/check-username")
    @FormUrlEncoded
    Flowable<AccessRequest> generateUserName(
            @Field("username") String name
    );

    @POST("register/sign-up")
    @FormUrlEncoded
    Flowable<AccessRequest> signup(
            @Field("sign_type") String signType,
            @Field("first_name") String firstName,
            @Field("last_name") String lastName,
            @Field("username") String userName,
            @Field("gender") String gender,
            @Field("dob") String dob,
            @Field("country") String country,
            @Field("mobile") String mobile,
            @Field("email") String email,
            @Field("password") String password,
            @Field("confirm_password") String confirmPassword,
            @Field("is_email_veirfied") String emailVerified,
            @Field("device_id") String deviceId,
            @Field("platform") String platform,
            @Field("country_code") String countryCode,

            @Field("fb_id") String fbId,
            @Field("google_id") String googleId,
            @Field("linkedin_id") String linkedInId
    );

    @POST("list/interest-list")
    Flowable<InterestRequest> getInterestList(@Header("Authorization") String token);

    // get master category list (interest)
    @POST("list/interest-list")
    Flowable<InterestRequest> getMaterDetail(@Header("Authorization") String token);

    // add new interest
    @POST("interest/add-user-interest")
    @FormUrlEncoded
    Flowable<AddUserInterestResponse> addUserInterest(
            @Header("Authorization") String token,
            @Field("title") String title
    );

    // link interest
    @POST("interest/link-interest-category")
    @FormUrlEncoded
    Flowable<LinkInterestResponse> linkUserInterest(
            @Header("Authorization") String token,
            @Field("interest_id") int interest_id,
            @Field("category_id[]") int[] category_id
    );

    // interest global search
    @POST("interest/fetch-global-interest")
    @FormUrlEncoded
    Flowable<GlobalInterestResponse> getInterestGlobalSearch(
            @Header("Authorization") String token,
            @Field("search") String search
    );

    // home page
    // get story detail
    @POST("list/get-home-feeds")
    @FormUrlEncoded
    Flowable<HomeResponse> getStoryHome(
            @Header("Authorization") String token,
            @Field("page") int page,
            @Field("limit") int limit,
            @Field("limit_story") int limit_story,
            @Field("filter") String  filter
    );

    // You page

    // get story list
    @POST("list/get-stories-user")
    @FormUrlEncoded
    Flowable<StoryUserResponse> getStoryUser(
            @Header("Authorization") String token,
            @Field("user_type") String user_type,
            @Field("type") String type,
            @Field("user_id") int user_id,
            @Field("page") int page,
            @Field("limit") int limit
    );

    // get user page list
    @POST("page/page-list-home")
    @FormUrlEncoded
    Flowable<PageListResponse> pageListHome(
            @Header("Authorization") String token,
            @Field("user_type") String user_type,
            @Field("type") String type,
            @Field("user_id") int user_id,
            @Field("page") int page,
            @Field("limit") int limit
    );

    // get user photo list
    @POST("photo/get-users-photo")
    @FormUrlEncoded
    Flowable<MyPhotosModel> getUserPhoto(
            @Header("Authorization") String token,
            @Field("user_type") String user_type,
            @Field("type") String type,
            @Field("user_id") int user_id,
            @Field("page") int  page,
            @Field("limit") int  limit
    );


    // get story detail
    @POST("story/get-stories-detail")
    @FormUrlEncoded
    Flowable<StoryResponse> storyDetail(
            @Header("Authorization") String token,
            @Field("story_id") int story_id
    );

    // add support
    @POST("story/get-support-stories")
    @FormUrlEncoded
    Flowable<SupportResponse> addSupport(
            @Header("Authorization") String token,
            @Field("story_id") int story_id
    );
    // add challenge
    @POST("story/get-challenge-stories")
    @FormUrlEncoded
    Flowable<SupportResponse> addChallenge(
            @Header("Authorization") String token,
            @Field("story_id") int story_id
    );

    // add notes
    @POST("story/get-notes-stories")
    @FormUrlEncoded
    Flowable<SupportResponse> addNotes(
            @Header("Authorization") String token,
            @Field("story_id") int story_id
    );

    // add notes
    @POST("story/add-note-to-story")
    @FormUrlEncoded
    Flowable<AccessRequest> addNotesStory(
            @Header("Authorization") String token,
            @Field("msg") String msg,
            @Field("story_id") int story_id
    );

    // ADD FAVOURITES TO STORY
    // add notes
    @POST("story/add-to-fav-story")
    @FormUrlEncoded
    Flowable<AccessRequest> addFavouritesStory(
            @Header("Authorization") String token,
            @Field("is_fav") int is_fav,
            @Field("story_id") int story_id
    );


    // upload gallery images
    @POST("photo/upload-photo")
    @FormUrlEncoded
    Flowable<UploadImageRequest> uploadGalleryImages(
            @Header("Authorization") String token,
            @Field("image[]") String[] image,
            @Field("unique_key") String uniqueKey
    );

    // refresh token
    @POST("token/refresh")
    @FormUrlEncoded
    Flowable<AccessRequest> refreshToken(
            @Field("token") String token
    );

    /*@POST("email-login/socail-login")
    @FormUrlEncoded
    Flowable<AccessRequest> socialLogin(
            @Field("login_type") String loginType,
            @Field("fb_id") String fbId,
            @Field("google_id") String googleId,
            @Field("linkedin_id") String linkedInId
    );*/

    @GET("token/logout")
    Flowable<AccessRequest> logout(@Header("Authorization") String token);

    @POST("email-login/login")
    @FormUrlEncoded
    Flowable<EmailPasswordLoginResponse> loginEmailPassword(
            @Field("email") String email,
            @Field("password") String password);

    @POST("register/send-otp-email")
    @FormUrlEncoded
    Flowable<SendOtpEmailResponse> sendOtpEmail(@Field("email") String email, @Field("type") String type);

    @POST("register/verify-otp-email")
    @FormUrlEncoded
    Flowable<EmailLoginResponse> verifyOtpEmail(
            @Field("otp") String otp,
            @Field("email") String email,
            @Field("username") String username,
            @Field("token") String token,
            @Field("type") String type);

    @GET("list/country-list")
    Flowable<CountryResponse> countryList();

    @POST("story/upload-story")
    @FormUrlEncoded
    Flowable<StoryResponse> createStory(
            @Header("Authorization") String token,
            @Field("image[]") int[] image,
            @Field("story_type") String  story_type,
            @Field("title") String  title,
            @Field("narrative") String  narrative,
            @Field("publish_type") String  publish_type,
            @Field("interest_id[]") int[] interest_id,
            @Field("is_type") String  is_type,
            @Field("parent_story_id") int  parent_story_id
    );

    @POST("page/upload-page")
    @FormUrlEncoded
    Flowable<PageResponse> createPage(
            @Header("Authorization") String token,
            @Field("type") String  type, // create,update
            @Field("page_id") String  pageId,
            @Field("cover_image") int cover_image,
            @Field("page_type") int  page_type,
            @Field("title") String  title,
            @Field("about") String  about,
            @Field("visibility") String  visibility,
            @Field("page_status") String  page_status,
            @Field("interest_id[]") int[] interest_id
    );




    @POST("register/check-mobile")
    @FormUrlEncoded
    Flowable<AccessRequest> checkMobile(@Field("mobile") String mobile);

    @POST("static-page/fetch-page-content")
    @FormUrlEncoded
    Flowable<StaticPageResponse> fetchStaticPages(@Field("page_type") String pageType);

    @POST("email-login/login")
    @FormUrlEncoded
    Flowable<LoginResponse> login(
            @Field("login_type") String login_type,
            @Field("username") String username,
            @Field("email") String email,
            @Field("password") String password,
            @Field("mobile") String mobile,
            @Field("country_code") String country_code,
            @Field("social_id") String social_id
    );

    @POST("user/add-user-preference")
    @FormUrlEncoded
    Flowable<AccessRequest> setPreferences(
            @Header("Authorization") String token,
            @Field("profile_activity_others") int yourProfileActivity,
            @Field("profile_activity_type") int yourProfileActivityType,
            @Field("profile_activity_interest") int myProfileActivity,
            @Field("allow_message") int allowMessage,
            @Field("message_type") int allowMessageType,
            @Field("help_query") int helpOther,
            @Field("interest_id[]") int[] interestIsd
    );


    @POST("register/sign-up")
    @FormUrlEncoded
    Flowable<AccessRequest> signup(
            @Field("sign_type") String signType,
            @Field("first_name") String firstName,
            @Field("last_name") String lastName,
            @Field("username") String userName,
            @Field("gender") String gender,
            @Field("dob") String dob,
            @Field("country") String country,
            @Field("mobile") String mobile,
            @Field("email") String email,
            @Field("password") String password,
            @Field("confirm_password") String confirmPassword,
            @Field("is_email_veirfied") String emailVerified,
            @Field("is_mobile_veirfied") String mobileVerified,
            @Field("device_id") String deviceId,
            @Field("platform") String platform,
            @Field("country_code") String countryCode,
            @Field("social_id") String socialId
    );

    @POST("photo/add-photo-detail")
    @FormUrlEncoded
    Flowable<DetailResponse> addPhotoDetails(
            @Header("Authorization") String token,
            @Field("photo_id") int  photo_id,
            @Field("caption") String  caption,
            @Field("credit") String  credit,
            @Field("interest_id[]") int[] interestIsd
    );


    @POST("register/forgot-password")
    @FormUrlEncoded
    Flowable<ForgetPasswordResponse> forgetPassword(
            @Field("forgot_type") String forgotType,
            @Field("email") String email,
            @Field("username") String username,
            @Field("country_code") String countryCode,
            @Field("phone") String phone
    );

    @POST("register/verify-otp-common")
    @FormUrlEncoded
    Flowable<AccessRequest> verifyForgetPhoneOtp(
            @Field("type") String type,
            @Field("otp") String otp,
            @Field("country_code") String countryCode,
            @Field("phone") String phone
    );

    @POST("register/update-password")
    @FormUrlEncoded
    Flowable<AccessRequest> updatePassword(
            @Field("password") String password,
            @Field("confirm_password") String confirmPassword,
            @Field("token") String token,
            @Field("forgot_type") String forgotType,
            @Field("country_code") String countryCode,
            @Field("phone") String phone,
            @Field("username") String username,
            @Field("email") String email
    );

    @POST("user/add-work")
    @FormUrlEncoded
    Flowable<WorkExperienceResponse> addWorkExperience(
            @Header("Authorization") String token,
            @Field("job_title") String jobTitle,
            @Field("organisation") String organisation,
            @Field("is_current") String isCurrent
    );

    @GET("user/get-user-detail")
    Flowable<UserProfileResponse> getUserProfileData(@Header("Authorization") String token);

    @POST("user/add-qualification")
    @FormUrlEncoded
    Flowable<EducationResponse> addEducation(
            @Header("Authorization") String token,
            @Field("degree") String degree,
            @Field("university") String university,
            @Field("institute") String institute
    );

    @POST("user/add-citation")
    @FormUrlEncoded
    Flowable<CitationResponse> addCitation(
            @Header("Authorization") String token,
            @Field("description") String description,
            @Field("link") String link
    );

    @POST("user/add-bio")
    @FormUrlEncoded
    Flowable<BioResponse> addBio(
            @Header("Authorization") String token,
            @Field("bio") String bio
    );

    @POST("user/update-user-photo")
    @Multipart
    Flowable<ImageUploadResponse> uploadProfileImage(
            @Part MultipartBody.Part file,
            @Header("Authorization") String token);


    @POST("user/update-remove-citation")
    @FormUrlEncoded
    Flowable<CitationResponse> updateDeleteCitation(
            @Header("Authorization") String token,
            @Field("id") String id,
            @Field("type") String type,
            @Field("description") String description,
            @Field("link") String link
    );


    @POST("user/update-remove-work")
    @FormUrlEncoded
    Flowable<WorkExperienceResponse> updateDeleteWorkExperience(
            @Header("Authorization") String token,
            @Field("id") String id,
            @Field("type") String type,
            @Field("job_title") String jobTitle,
            @Field("organisation") String organisation,
            @Field("is_current") String isCurrent
    );

    @POST("user/update-remove-qualification")
    @FormUrlEncoded
    Flowable<EducationResponse> updateDeleteEducation(
            @Header("Authorization") String token,
            @Field("id") String id,
            @Field("type") String type,
            @Field("degree") String degree,
            @Field("university") String university,
            @Field("institute") String institute
    );

    @POST("static-page/faq-list")
    @FormUrlEncoded
    Flowable<FAQResponse> getFAQList(@Header("Authorization") String token, @Field("question") String question);


    // upload gallery images
    @Multipart
    @POST("photo/upload-single-photo")
    Flowable<UploadImageRequest> uploadSingleImage(
            @Header("Authorization") String token,
            @Part("unique_key") RequestBody uniqueKey,
            @Part MultipartBody.Part file
    );

    @POST("user/update-user-detail")
    @FormUrlEncoded
    Flowable<AccessRequest> updateAccountDetail(
            @Header("Authorization") String token,
            @Field("first_name") String firstName,
            @Field("last_name") String lastName,
            @Field("email") String email,
            @Field("username") String username,
            @Field("gender") String gender,
            @Field("dob") String dob,
            @Field("country_id") String countryId,
            @Field("mobile") String mobile,
            @Field("country_code") String countryCode
    );

    @POST("user/change-password")
    @FormUrlEncoded
    Flowable<AccessRequest> changePassword(
            @Header("Authorization") String token,
            @Field("old_password") String oldPassword,
            @Field("new_password") String newPassword,
            @Field("confirm_password") String confirmPassword
    );

    @POST("user/update-verify-email-mobile")
    @FormUrlEncoded
    Flowable<AccessRequest> verifyUpdateEmailMobile(
            @Header("Authorization") String authToken,
            @Field("type") String type,  // email,mobile
            @Field("verify_type") String verifyType,// skip,verify
            @Field("email") String email,
            @Field("token") String token,
            @Field("mobile") String mobile,
            @Field("country_code") String countryCode,
            @Field("otp") String otp
    );

    @GET("user/get-user-preference")
    Flowable<UserPrefrenceResponse> getUserPrefrence(@Header("Authorization") String token);

    @POST("user/update-user-preference")
    @FormUrlEncoded
    Flowable<AccessRequest> updateUserPrefrence(
            @Header("Authorization") String authToken,
            @Field("profile_activity_others") int profileOthers,
            @Field("profile_activity_interest") int profileInterest,
            @Field("allow_message") int allowMessage,
            @Field("help_query") int helpQuery,
            @Field("profile_activity_type") int profileType,
            @Field("message_type") int messageType
    );


    // delete multiple images
    @POST("photo/remove-users-multiple-photo")
    @FormUrlEncoded
    Flowable<AccessRequest> deleteImages(
            @Header("Authorization") String header,
            @Field("photo_id[]") int[] photo_id
    );

    // delete single images
    @POST("photo/remove-users-photo")
    @FormUrlEncoded
    Flowable<AccessRequest> deleteSingleImages(
            @Header("Authorization") String header,
            @Field("photo_id") int photo_id
    );

    // delete single images
    @POST("photo/add-to-fav-photo")
    @FormUrlEncoded
    Flowable<AccessRequest> setImageFavourite(
            @Header("Authorization") String header,
            @Field("is_fav") int is_fav,
            @Field("photo_id") int photo_id
    );

    @POST("user/user-list-home")
    @FormUrlEncoded
    Call<UserHomeListResponse> getUserHomeList(
            @Header("Authorization") String token,
            @Field("type") String type,
            @Field("page") int page,
            @Field("limit") int limit
    );

    @POST("chat/send-follow-req")
    @FormUrlEncoded
    Flowable<AccessRequest> followRequest(
            @Header("Authorization") String token,
            @Field("receiver") int receiver,
            @Field("is_follow") int isFollow
    );

    @POST("user/get-user-interest")
    @FormUrlEncoded
    Call<AddInterestResponse> addInterest(
            @Header("Authorization") String token,
            @Field("user_id") int userId,
            @Field("page") int page,
            @Field("limit") int limit
    );



    //Message-----------------

    @POST("user/get-users-list")
    @FormUrlEncoded
    Flowable<MessageResponse> getMessageNewUserList(
            @Header("Authorization") String token,
            @Field("search") String search
    );

    @POST("chat/get-chat-list")
    @FormUrlEncoded
    Flowable<MessageResponse> getMessageUsers(
            @Header("Authorization") String token,
            @Field("page") int page,
            @Field("limit") int limit
    );

    @POST("chat/get-chats-single")
    @FormUrlEncoded
    Flowable<MessageResponse> getMessages(
            @Header("Authorization") String token,
            @Field("receiver") String userId,
            @Field("page") int page,
            @Field("limit") int limit
    );

    @POST("chat/sent-msg")
    @FormUrlEncoded
    Flowable<MessageResponse> sendMessage(
            @Header("Authorization") String token,
            @Field("receiver") String userId,
            @Field("msg_text") String message
    );

    @Multipart
    @POST("chat/sent-msg")
    Flowable<MessageResponse> sendMessageFile(
            @Header("Authorization") String token,
            @Part("receiver") RequestBody userId,
            @Part MultipartBody.Part file
    );

    @POST("chat/get-bulletin-list")
    @FormUrlEncoded
    Flowable<MessageResponse> getMessageUsersBulletin(
            @Header("Authorization") String token,
            @Field("type") String type,
            @Field("country_id") int country_id,
            @Field("page") int page,
            @Field("limit") int limit
    );

    @POST("chat/chat-board-bulletin")
    @FormUrlEncoded
    Flowable<MessageResponse> getMessagesBulletin(
            @Header("Authorization") String token,
            @Field("bulletin_id") String bulletinId,
            @Field("page") int page,
            @Field("limit") int limit
    );

    @POST("chat/sent-bulletin-msg")
    @FormUrlEncoded
    Flowable<MessageResponse> sendMessageBulletin(
            @Header("Authorization") String token,
            @Field("bulletin_id") String userId,
            @Field("message") String message
    );

    @Multipart
    @POST("chat/sent-bulletin-msg")
    Flowable<MessageResponse> sendMessageFileBulletin(
            @Header("Authorization") String token,
            @Part("bulletin_id") RequestBody bulletinId,
            @Part MultipartBody.Part file
    );

    @POST("chat/create-bulletin")
    @FormUrlEncoded
    Flowable<MessageResponse> sendMessageBulletin(
            @Header("Authorization") String token,
            @Field("subject") String subject,
            @Field("message") String message,
            @Field("interest[]") int[] interests,
            @Field("country_id") String country_id
    );

    @Multipart
    @POST("chat/create-bulletin")
    Flowable<MessageResponse> sendMessageFileBulletin(
            @Header("Authorization") String token,
            @Part("subject") RequestBody subject,
            @Part("country_id") RequestBody countryId,
            @Part("interest[]") int[] interest,
            @Part MultipartBody.Part file
    );

    @POST("chat/send-follow-req")
    @FormUrlEncoded
    Flowable<MessageResponse> sendFollow(
            @Header("Authorization") String token,
            @Field("receiver") String subject,
            @Field("is_follow") int fallow
    );

    @POST("page-member/accept-page-req")
    @FormUrlEncoded
    Flowable<MessageResponse> sendApprove(
            @Header("Authorization") String token,
            @Field("msg_id") int messageId,
            @Field("accept") int accept
    );

    @POST("chat/clear-inbox")
    @FormUrlEncoded
    Flowable<MessageResponse> deleteMessageUser(
            @Header("Authorization") String token,
            @Field("receiver_id") int userId
    );

    //-------------------------------------------

    @POST("page/get-page-detail")
    @FormUrlEncoded
    Flowable<PageDetailResponse> getPageDetail(
            @Header("Authorization") String token,
            @Field("page_id") int pageId
    );


    // you friend
    @POST("user/friend-list")
    @FormUrlEncoded
    Flowable<UserFriendResponse> getFriendList(
            @Header("Authorization") String token,
            @Field("page") int  page,
            @Field("limit") int  limit
    );

    // you friend
    @POST("user/follower-list")
    @FormUrlEncoded
    Flowable<UserFollowersResponse> getFollowersList(
            @Header("Authorization") String token,
            @Field("page") int  page,
            @Field("limit") int  limit
    );

    // you friend
    @POST("user/following-list")
    @FormUrlEncoded
    Flowable<UserFollowingResponse> getFollowingList(
            @Header("Authorization") String token,
            @Field("page") int  page,
            @Field("limit") int  limit
    );

    // FRIEND, FOLLOWING AND FOLLOWERS
    @GET("user/get-count-friend-follow")
    Flowable<UserCountResponse> getFriendFollowFollowingList(@Header("Authorization") String token);


    @POST("story/search-by-filter")
    @FormUrlEncoded
    Call<PageSearchResponse> pageSearch(
            @Header("Authorization") String token,
            @Field("filter") String filter,
            @Field("search") String search,
            @Field("find_in_page") String findInPage,
            @Field("page_id") int pageId,
            @Field("page") int page,
            @Field("limit") int limit
    );

    @POST("page/share-story-page")
    @FormUrlEncoded
    Flowable<AccessRequest> shareStory(
            @Header("Authorization") String token,
            @Field("story_id") int storyId,
            @Field("page_id") int pageId
    );

    @POST("page/page-stories-by-id")
    @FormUrlEncoded
    Call<PageStoryResponse> getPageStories(
            @Header("Authorization") String token,
            @Field("page_id") int pageId,
            @Field("page") int page,
            @Field("limit") int limit

    );

    @POST("page/page-member-by-id")
    @FormUrlEncoded
    Call<PageMemberResponse> getPageMembers(
            @Header("Authorization") String token,
            @Field("page_id") int pageId,
            @Field("page") int page,
            @Field("limit") int limit
    );

    @POST("page/follow-page")
    @FormUrlEncoded
    Flowable<AccessRequest> followPage(
            @Header("Authorization") String token,
            @Field("page_id") int pageId,
            @Field("is_follow") int isFollow
    );

    // GET USER DETAIL BY ID
    @POST("user/get-user-detail-by-id")
    @FormUrlEncoded
    Flowable<AccountDetailResponse> getUserDetailById(
            @Header("Authorization") String token,
            @Field("user_id") int user_id
    );

    @POST("user/refresh-user-detail")
    @FormUrlEncoded
    Flowable<AccountDetailResponse> getAccountDetailData(
            @Header("Authorization") String token,
            @Field("user_id") int user_id
    );


}
