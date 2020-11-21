package com.app.pixstory.data;

import com.app.pixstory.data.local.db.DatabaseService;
import com.app.pixstory.data.local.prefs.PreferenceService;
import com.app.pixstory.data.model.PageDetailResponse;
import com.app.pixstory.data.model.PageMemberResponse;
import com.app.pixstory.data.model.PageSearchResponse;
import com.app.pixstory.data.model.api.AddInterestResponse;
import com.app.pixstory.data.model.api.UserHomeListResponse;
import com.app.pixstory.data.model.access.AccessRequest;
import com.app.pixstory.data.model.add_support.SupportResponse;
import com.app.pixstory.data.model.add_user_interest.AddUserInterestResponse;
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
import com.app.pixstory.data.model.api.SendOtpEmailResponse;
import com.app.pixstory.data.model.api.StaticPageResponse;
import com.app.pixstory.data.model.api.UserPrefrenceResponse;
import com.app.pixstory.data.model.api.UserProfileResponse;
import com.app.pixstory.data.model.api.WorkExperienceResponse;
import com.app.pixstory.data.model.create_page.PageResponse;
import com.app.pixstory.data.model.get_story_user.StoryUserResponse;
import com.app.pixstory.data.model.global_search.GlobalInterestResponse;
import com.app.pixstory.data.model.home.HomeResponse;
import com.app.pixstory.data.model.interest.InterestRequest;
import com.app.pixstory.data.model.link_user_interest.LinkInterestResponse;
import com.app.pixstory.data.model.page_list.PageListResponse;
import com.app.pixstory.data.model.story.StoryResponse;
import com.app.pixstory.data.model.upload.MyPhotosModel;
import com.app.pixstory.data.model.upload_details.DetailResponse;
import com.app.pixstory.data.model.upload_image.UploadImageRequest;
import com.app.pixstory.data.model.user_count.UserCountResponse;
import com.app.pixstory.data.model.version_check.AppVersionResponse;
import com.app.pixstory.data.model.you_user.UserFollowersResponse;
import com.app.pixstory.data.model.you_user.UserFollowingResponse;
import com.app.pixstory.data.model.you_user.UserFriendResponse;
import com.app.pixstory.data.remote.APIService;

import com.app.pixstory.data.model.api.messages.MessageResponse;
import com.app.pixstory.data.model.db.messages.MessageUsers;
import com.app.pixstory.data.model.db.messages.Messages;
import com.app.pixstory.ui.dashboard.you.activity.PageStoryResponse;

import java.util.List;

import io.reactivex.Flowable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;

public final class DataManagerService implements DataManager {

    private static DataManager dataManager;

    private static PreferenceService pref;
    private static DatabaseService db;
    private static APIService apis;

    private DataManagerService() {
    }

    public static DataManager getInstance() {
        if (dataManager == null) {
            dataManager = new DataManagerService();
        }
        return dataManager;
    }

    public static void setup(PreferenceService preferencesService,
                             APIService apiService, DatabaseService dbService) {
        pref = preferencesService;
        db = dbService;
        apis = apiService;

        getInstance();
    }

    //Preferences----------------------------------

    @Override
    public void setLoginMode(int data) {
        pref.setLoginMode(data);
    }

    @Override
    public int getLoginMode() {
        return pref.getLoginMode();
    }

    @Override
    public void setFirstTimeLaunchSlider(boolean isFirstTime) {
        pref.setFirstTimeLaunchSlider(isFirstTime);
    }

    @Override
    public boolean isFirstTimeLaunchSlider() {
        return pref.isFirstTimeLaunchSlider();
    }

    @Override
    public void setAccessToken(String token) {
        pref.setAccessToken(token);
    }

    @Override
    public String getAccessToken() {
        return pref.getAccessToken();
    }

    @Override
    public void setStoryTitle(String title) {
        pref.setStoryTitle(title);
    }

    @Override
    public String getStoryTitle() {
        return pref.getStoryTitle();
    }

    @Override
    public String getStoryNarrative() {
        return pref.getStoryNarrative();
    }

    @Override
    public void setStoryNarrative(String narrative) {
        pref.setStoryNarrative(narrative);
    }

    @Override
    public void setStoryFormat(String storyFormat) {
        pref.setStoryFormat(storyFormat);
    }

    @Override
    public String getStoryFormat() {
        return pref.getStoryFormat();
    }

    //Database----------------------------------

    @Override
    public Flowable<Boolean> saveMessageUsers(List<MessageUsers> list) {
        return db.saveMessageUsers(list);
    }

    @Override
    public Flowable<List<MessageUsers>> getMessageUsers() {
        return db.getMessageUsers();
    }

    @Override
    public Flowable<Boolean> saveMessages(List<Messages> list) {
        return db.saveMessages(list);
    }

    @Override
    public Flowable<List<Messages>> getMessages(String userId) {
        return db.getMessages(userId);
    }

    //APIs----------------------------------

    @Override
    public Flowable<AppVersionResponse> getAppVersion() {
        return apis.getAppVersion();
    }

    @Override
    public Flowable<AccessRequest> verificationRequestMobile(String countryCode, String phone, String type) {
        return apis.verificationRequestMobile(countryCode, phone, type);
    }

    @Override
    public Flowable<AccessRequest> verificationRequestMobileOtp(String countryCode, String phone, String otp) {
        return apis.verificationRequestMobileOtp(countryCode, phone, otp);
    }

    @Override
    public Flowable<AccessRequest> generateUserName(String name) {
        return apis.generateUserName(name);
    }

    @Override
    public Flowable<AccessRequest> signup(String signType, String firstName, String lastName, String userName,
                                          String gender, String dob, String country, String mobile, String email, String password,
                                          String confirmPassword, String emailVerified, String deviceId, String platform, String countryCode,
                                          String fbId, String googleId, String linkedInId) {
        return apis.signup(signType, firstName, lastName, userName, gender, dob, country, mobile, email, password,
                confirmPassword, emailVerified, deviceId, platform, countryCode, fbId, googleId, linkedInId);
    }

    @Override
    public Flowable<InterestRequest> getInterestList(String token) {
        return apis.getInterestList(token);
    }

    @Override
    public Flowable<InterestRequest> getMaterDetail(String token) {
        return apis.getMaterDetail(token);
    }

    @Override
    public Flowable<AddUserInterestResponse> addUserInterest(String token, String title) {
        return apis.addUserInterest(token, title);
    }

    @Override
    public Flowable<LinkInterestResponse> linkUserInterest(String token, int interest_id, int[] category_id) {
        return apis.linkUserInterest(token, interest_id, category_id);
    }

    @Override
    public Flowable<GlobalInterestResponse> getInterestGlobalSearch(String token, String search) {
        return apis.getInterestGlobalSearch(token, search);
    }

    @Override
    public Flowable<HomeResponse> getStoryHome(String token, int page, int limit, int limit_story, String filter) {
        return apis.getStoryHome(token, page, limit, limit_story, filter);
    }

    @Override
    public Flowable<StoryUserResponse> getStoryUser(String token, String  userType, String type, int userId, int page, int limit) {
        return apis.getStoryUser(token, userType, type, userId, page, limit);
    }

    @Override
    public Flowable<StoryResponse> storyDetail(String token, int story_id) {
        return apis.storyDetail(token, story_id);
    }

    @Override
    public Flowable<SupportResponse> addSupport(String token, int story_id) {
        return apis.addSupport(token, story_id);
    }

    @Override
    public Flowable<SupportResponse> addChallenge(String token, int story_id) {
        return apis.addChallenge(token, story_id);
    }

    @Override
    public Flowable<SupportResponse> addNotes(String token, int story_id) {
        return apis.addNotes(token, story_id);
    }

    @Override
    public Flowable<AccessRequest> addNotesStory(String token, String msg, int story_id) {
        return apis.addNotesStory(token, msg, story_id);
    }

    @Override
    public Flowable<AccessRequest> addFavouritesStory(String token, int is_fav, int story_id) {
        return apis.addFavouritesStory(token, is_fav, story_id);
    }


    @Override
    public Flowable<AccessRequest> setPreferences(String token, int yourProfileActivity, int yourProfileActivityType, int myProfileActivity,
                                                  int allowMessage, int allowMessageType, int helpOther, int[] interestIsd) {
        return apis.setPreferences(token, yourProfileActivity, yourProfileActivityType, myProfileActivity, allowMessage,
                allowMessageType, helpOther, interestIsd);
    }

    @Override
    public Flowable<UploadImageRequest> uploadGalleryImages(String token, String[] image, String uniqueKey) {
        return apis.uploadGalleryImages(token, image, uniqueKey);
    }

    @Override
    public Flowable<AccessRequest> refreshToken(String token) {
        return apis.refreshToken(token);
    }

    /*@Override
    public Flowable<AccessRequest> socialLogin(String loginType, String fbId, String googleId, String linkedInId) {
        return apis.socialLogin(loginType, fbId, googleId, linkedInId);
    }*/

    @Override
    public Flowable<AccessRequest> logout(String token) {
        return apis.logout(token);
    }

    @Override
    public Flowable<EmailPasswordLoginResponse> loginEmailPassword(String email, String password) {
        return apis.loginEmailPassword(email, password);
    }

    @Override
    public Flowable<SendOtpEmailResponse> sendOtpEmail(String email, String type) {
        return apis.sendOtpEmail(email, type);
    }

    @Override
    public Flowable<EmailLoginResponse> verifyOtpEmail(String otp, String email, String username, String token, String type) {
        return apis.verifyOtpEmail(otp, email, username, token, type);
    }


    @Override
    public Flowable<CountryResponse> countryList() {
        return apis.countryList();
    }

    @Override
    public Flowable<MyPhotosModel> getUserPhoto(String token, String  userType, String type, int userId, int page, int limit) {
        return apis.getUserPhoto(token, userType, type, userId, page, limit);
    }

    @Override
    public Flowable<StoryResponse> createStory(String token, int[] image, String story_type, String title, String narrative, String publish_type, int[] interest_id, String is_type, int parent_story_id) {
        return apis.createStory(token, image, story_type, title, narrative, publish_type, interest_id, is_type, parent_story_id);
    }

    @Override
    public Flowable<PageResponse> createPage(String token, String type, String pageId, int cover_image, int page_type, String title, String about, String visibility, String page_status, int[] interest_id) {
        return apis.createPage(token,type,pageId,cover_image,page_type,title,about,visibility,page_status,interest_id);
    }

    @Override
    public Flowable<PageListResponse> pageListHome(String token, String  userType, String type, int userId, int page, int limit) {
        return apis.pageListHome(token, userType, type, userId, page, limit);
    }

    @Override
    public Flowable<AccessRequest> checkMobile(String mobile) {
        return apis.checkMobile(mobile);
    }

    @Override
    public Flowable<StaticPageResponse> fetchStaticPages(String pageType) {
        return apis.fetchStaticPages(pageType);
    }

    @Override
    public Flowable<AccessRequest> signup(String signType, String firstName, String lastName, String userName,
                                          String gender, String dob, String country, String mobile, String email,
                                          String password, String confirmPassword, String emailVerified, String mobileVerified, String deviceId, String platform, String countryCode,
                                          String socialId) {
        return apis.signup(signType, firstName, lastName, userName, gender, dob, country, mobile, email, password,
                confirmPassword, emailVerified, mobileVerified, deviceId, platform, countryCode, socialId);
    }

    @Override
    public Flowable<LoginResponse> login(String login_type, String username, String email, String password, String mobile, String country_code, String social_id) {
        return apis.login(login_type, username, email, password, mobile, country_code, social_id);
    }

    @Override
    public Flowable<DetailResponse> addPhotoDetails(String token, int photo_id, String caption, String credit, int[] interest_id) {
        return apis.addPhotoDetails(token, photo_id, caption, credit, interest_id);
    }

    @Override
    public Flowable<ForgetPasswordResponse> forgetPassword(String forgotType, String email, String username, String countryCode, String phone) {
        return apis.forgetPassword(forgotType, email, username, countryCode, phone);
    }

    @Override
    public Flowable<AccessRequest> verifyForgetPhoneOtp(String type, String otp, String countryCode, String phone) {
        return apis.verifyForgetPhoneOtp(type, otp, countryCode, phone);
    }

    @Override
    public Flowable<AccessRequest> updatePassword(String password, String confirmPassword, String token, String forgotType, String countryCode, String phone,String username, String email) {
        return apis.updatePassword(password, confirmPassword, token, forgotType, countryCode, phone,username, email);
    }

    @Override
    public Flowable<WorkExperienceResponse> addWorkExperience(String token, String jobTitle, String organisation, String isCurrent) {
        return apis.addWorkExperience(token,jobTitle,organisation,isCurrent);
    }

    @Override
    public Flowable<UserProfileResponse> getUserProfileData(String token) {
        return apis.getUserProfileData(token);
    }

    @Override
    public Flowable<EducationResponse> addEducation(String token, String degree, String university, String institute) {
        return apis.addEducation(token,degree,university,institute);
    }

    @Override
    public Flowable<CitationResponse> addCitation(String token, String description, String link) {
        return apis.addCitation(token,description,link);
    }

    @Override
    public Flowable<BioResponse> addBio(String token, String bio) {
        return apis.addBio(token,bio);
    }

    @Override
    public Flowable<ImageUploadResponse> uploadProfileImage(MultipartBody.Part body, String token) {
        return apis.uploadProfileImage(body,token);
    }

    @Override
    public Flowable<CitationResponse> updateDeleteCitation(String token, String id, String type, String description, String link) {
        return apis.updateDeleteCitation(token,id,type,description,link);
    }

    @Override
    public Flowable<WorkExperienceResponse> updateDeleteWorkExperience(String token, String id, String type, String jobTitle, String organisation, String isCurrent) {
        return apis.updateDeleteWorkExperience(token,id,type,jobTitle,organisation,isCurrent);
    }

    @Override
    public Flowable<EducationResponse> updateDeleteEducation(String token, String id, String type, String degree, String university, String institute) {
        return apis.updateDeleteEducation(token,id,type,degree,university,institute);
    }

    @Override
    public Flowable<FAQResponse> getFAQList(String token, String question) {
        return apis.getFAQList(token,question);
    }

    @Override
    public Flowable<AccountDetailResponse> getAccountDetailData(String token, int userId) {
        return apis.getAccountDetailData(token, userId);
    }

    @Override
    public Flowable<UploadImageRequest> uploadSingleImage(String token, RequestBody uniqueKey, MultipartBody.Part file) {
        return apis.uploadSingleImage(token, uniqueKey, file);
    }

    @Override
    public Flowable<AccessRequest> updateAccountDetail(String token, String firstName, String lastName, String email, String username, String gender, String dob, String countryId, String mobile, String countryCode) {
        return apis.updateAccountDetail(token,firstName,lastName,email,username,gender,dob,countryId,mobile,countryCode);
    }

    @Override
    public Flowable<AccessRequest> changePassword(String token, String oldPassword, String newPassword, String confirmPassword) {
        return apis.changePassword(token,oldPassword,newPassword,confirmPassword);
    }

    @Override
    public Flowable<AccessRequest> verifyUpdateEmailMobile(String authToken, String type, String verifyType, String email, String token, String mobile, String countryCode, String otp) {
        return apis.verifyUpdateEmailMobile(authToken,type,verifyType,email,token,mobile,countryCode,otp);
    }

    @Override
    public Flowable<UserPrefrenceResponse> getUserPrefrence(String token) {
        return apis.getUserPrefrence(token);
    }

    @Override
    public Flowable<AccessRequest> updateUserPrefrence(String authToken, int profileOthers, int profileInterest, int allowMessage, int helpQuery, int profileType, int messageType) {
        return apis.updateUserPrefrence(authToken,profileOthers,profileInterest,allowMessage,helpQuery,profileType,messageType);
    }

    @Override
    public Flowable<AccessRequest> deleteImages(String header, int[] photo_id) {
        return apis.deleteImages(header, photo_id);
    }

    @Override
    public Flowable<AccessRequest> deleteSingleImages(String header, int photo_id) {
        return apis.deleteSingleImages(header, photo_id);
    }

    @Override
    public Flowable<AccessRequest> setImageFavourite(String header, int is_fav, int photo_id) {
        return apis.setImageFavourite(header, is_fav, photo_id);
    }

    @Override
    public Call<UserHomeListResponse> getUserHomeList(String token, String type, int page, int limit) {
        return apis.getUserHomeList(token, type, page, limit);
    }

    @Override
    public Flowable<AccessRequest> followRequest(String token, int receiver, int isFollow) {
        return apis.followRequest(token,receiver,isFollow);
    }

    @Override
    public Call<AddInterestResponse> addInterest(String token, int userId, int page, int limit) {
        return apis.addInterest(token,userId,page,limit);
    }


    @Override
    public Flowable<MessageResponse> getMessageNewUserList(String token, String search) {
        return apis.getMessageNewUserList(token, search);
    }

    @Override
    public Flowable<MessageResponse> getMessageUsers(String token, int page, int limit) {
        return apis.getMessageUsers(token, page, limit);
    }

    @Override
    public Flowable<MessageResponse> getMessages(String token, String userId, int page, int limit) {
        return apis.getMessages(token, userId, page, limit);
    }

    @Override
    public Flowable<MessageResponse> sendMessage(String token, String userId, String message) {
        return apis.sendMessage(token, userId, message);
    }

    @Override
    public Flowable<MessageResponse> sendMessageFile(String token, RequestBody userId, MultipartBody.Part file) {
        return apis.sendMessageFile(token, userId, file);
    }


    @Override
    public Flowable<MessageResponse> getMessageUsersBulletin(String token, String type, int country_id, int page, int limit) {
        return apis.getMessageUsersBulletin(token, type, country_id, page, limit);
    }

    @Override
    public Flowable<MessageResponse> getMessagesBulletin(String token, String bulletinId, int page, int limit) {
        return apis.getMessagesBulletin(token, bulletinId, page, limit);
    }

    @Override
    public Flowable<MessageResponse> sendMessageBulletin(String token, String userId, String message) {
        return apis.sendMessageBulletin(token, userId, message);
    }

    @Override
    public Flowable<MessageResponse> sendMessageFileBulletin(String token, RequestBody bulletinId, MultipartBody.Part file) {
        return apis.sendMessageFileBulletin(token, bulletinId, file);
    }

    @Override
    public Flowable<MessageResponse> sendMessageBulletin(String token, String subject, String message,
                                                         int[] interests, String country_id) {
        return apis.sendMessageBulletin(token, subject, message, interests, country_id);
    }

    @Override
    public Flowable<MessageResponse> sendMessageFileBulletin(String token, RequestBody subject,
                                                             RequestBody countryId, int[] interest, MultipartBody.Part file) {
        return apis.sendMessageFileBulletin(token, subject, countryId, interest, file);
    }

    @Override
    public Flowable<MessageResponse> sendFollow(String token, String subject, int fallow) {
        return apis.sendFollow(token, subject, fallow);
    }


    @Override
    public Flowable<MessageResponse> sendApprove(String token, int messageId, int accept) {
        return apis.sendApprove(token, messageId, accept);
    }

    @Override
    public Flowable<MessageResponse> deleteMessageUser(String token, int userId) {
        return apis.deleteMessageUser(token, userId);
    }

    @Override
    public Flowable<PageDetailResponse> getPageDetail(String token, int pageId) {
        return apis.getPageDetail(token,pageId);
    }



    @Override
    public Flowable<UserFriendResponse> getFriendList(String token, int page, int limit) {
        return apis.getFriendList(token, page, limit);
    }

    @Override
    public Flowable<UserFollowersResponse> getFollowersList(String token, int page, int limit) {
        return apis.getFollowersList(token, page, limit);
    }

    @Override
    public Flowable<UserFollowingResponse> getFollowingList(String token, int page, int limit) {
        return apis.getFollowingList(token, page, limit);
    }

    @Override
    public Flowable<UserCountResponse> getFriendFollowFollowingList(String token) {
        return apis.getFriendFollowFollowingList(token);
    }

    @Override
    public Call<PageStoryResponse> getPageStories(String token, int pageId, int page, int limit) {
        return apis.getPageStories(token, pageId, page, limit);
    }

    @Override
    public Call<PageMemberResponse> getPageMembers(String token, int pageId, int page, int limit) {
        return apis.getPageMembers(token, pageId, page, limit);
    }

    @Override
    public Flowable<AccessRequest> followPage(String token, int pageId, int isFollow) {
        return apis.followPage(token, pageId, isFollow);
    }

    @Override
    public Flowable<AccountDetailResponse> getUserDetailById(String token, int user_id) {
        return apis.getUserDetailById(token, user_id);
    }

    @Override
    public Call<PageSearchResponse> pageSearch(String token, String filter, String search, String findInPage, int pageId, int page, int limit) {
        return apis.pageSearch(token, filter, search, findInPage, pageId, page, limit);
    }

    @Override
    public Flowable<AccessRequest> shareStory(String token, int storyId, int pageId) {
        return apis.shareStory(token, storyId, pageId);
    }


}
