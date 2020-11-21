package com.app.pixstory.utils.util;

import android.os.Bundle;

import com.app.pixstory.data.model.PageDetailResponse;
import com.app.pixstory.data.model.upload.Data;
import com.app.pixstory.data.model.upload_image.UploadModel;
import com.app.pixstory.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Author : Arvindo Mondal
 * Created on 14-03-2020
 */
public class Bundles {

    private static Bundles bundles;

    public static Bundles getInstance(){
        if(bundles == null){
            bundles = new Bundles();
        }
        return bundles;
    }

    public String getTypeForResult(Bundle bundle) {
        return bundle != null ? bundle.getString(KEY_BUNDLE + "type") : "";
    }

    public PageDetailResponse.Data getDataForResult(Bundle bundle) {
        return bundle != null ? Utils.getGsonParser().fromJson(bundle.getString(KEY_BUNDLE + "data"), PageDetailResponse.Data.class) : null;
    }

    public Bundle setTypeForResult(String type, PageDetailResponse.Data data) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_BUNDLE + "type", type);
        bundle.putString(KEY_BUNDLE + "data", Utils.getGsonParser().toJson(data));
        return bundle;
    }

    private Bundles(){}

    public static final String KEY_BUNDLE = "Bundles";

    public Bundle setSignUpData(String type, String countryCode, String mobile){
        Bundle bundle = new Bundle();
        bundle.putString(KEY_BUNDLE + "type", type);
        bundle.putString(KEY_BUNDLE + "countryCode", countryCode);
        bundle.putString(KEY_BUNDLE + "mobile", mobile);
        return bundle;
    }

    public Bundle setSignUpType(String type){
        Bundle bundle = new Bundle();
        bundle.putString(KEY_BUNDLE + "type", type);
        return bundle;
    }

    public Bundle setSignUpDataSocial(String type, HashMap<String, String> data) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_BUNDLE + "type", type);
        bundle.putSerializable(KEY_BUNDLE + "data", data);
        return bundle;
    }

    public HashMap<String, String> getSignUpData(Bundle bundle){
        return bundle != null?(HashMap<String, String>)bundle.getSerializable(KEY_BUNDLE + "data") : null;
    }

    public String getSignUpDataType(Bundle bundle){
        return bundle != null?bundle.getString(KEY_BUNDLE + "type") : "";
    }

    public String getSignUpDataCountryCode(Bundle bundle){
        return bundle != null?bundle.getString(KEY_BUNDLE + "countryCode") : "";
    }

    public String getSignUpDataMobile(Bundle bundle){
        return bundle != null?bundle.getString(KEY_BUNDLE + "mobile") : "";
    }

    public Bundle setSignUpEmail(String type,String email){
        Bundle bundle = new Bundle();
        bundle.putString(KEY_BUNDLE + "type", type);
        bundle.putString(KEY_BUNDLE + "email", email);
        return bundle;
    }

    public String getSignUpDataEmail(Bundle bundle){
        return bundle != null?bundle.getString(KEY_BUNDLE + "email") : "";
    }



    //Messages--------------------------

    public Bundle setMessageUser(String messageType) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_BUNDLE + "messageType", messageType);
        return bundle;
    }

    public Bundle setMessage(String messageType, int userId, String name) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_BUNDLE + "messageType", messageType);
        bundle.putString(KEY_BUNDLE + "userName", name);
        bundle.putInt(KEY_BUNDLE + "userId", userId);
        return bundle;
    }

    public Bundle setMessagePage(String messageType, String userId) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_BUNDLE + "messageType", messageType);
        bundle.putString(KEY_BUNDLE + "userId", userId);
        return bundle;
    }
    public String getMessageType(Bundle bundle){
        return bundle != null?bundle.getString(KEY_BUNDLE + "messageType") : "";
    }

    public int getMessageUserId(Bundle bundle){
        return bundle != null?bundle.getInt(KEY_BUNDLE + "userId") : 0;
    }

    public String getMessageUserName(Bundle bundle){
        return bundle != null?bundle.getString(KEY_BUNDLE + "userName") : "";
    }

    //Create page-----------------

    public Bundle setCreateStoryFilter(String type, String typeUpload, Data uploadModel) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_BUNDLE + "type", type);
        bundle.putString(KEY_BUNDLE + "typeUpload", typeUpload);
        bundle.putParcelable(KEY_BUNDLE + "uploadModel", uploadModel);
        return bundle;
    }

    public Bundle setCreateStoryFilterOld(String type, String typeUpload, ArrayList<Data> uploadArray, int position) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_BUNDLE + "type", type);
        bundle.putString(KEY_BUNDLE + "typeUpload", typeUpload);
        bundle.putParcelableArrayList(KEY_BUNDLE + "uploadModelArray", uploadArray);
        bundle.putInt(KEY_BUNDLE + "typePosition", position);
        return bundle;
    }

    public String getCreateStoryFilterType(Bundle bundle){
        return bundle != null?bundle.getString(KEY_BUNDLE + "type") : "";
    }

    public String getCreateStoryFilterTypeUpload(Bundle bundle){
        return bundle != null?bundle.getString(KEY_BUNDLE + "typeUpload") : "";
    }

    public int getCreateStoryFilterPosition(Bundle bundle){
        return bundle != null?bundle.getInt(KEY_BUNDLE + "typePosition") : 0;
    }

    public Data getCreateStoryFilterUploadModel(Bundle bundle){
        return bundle != null?bundle.getParcelable(KEY_BUNDLE + "uploadModel") : null;
    }

    public ArrayList<Data> getCreateStoryFilterUploadModelArray(Bundle bundle){
        return bundle != null?bundle.getParcelableArrayList(KEY_BUNDLE + "uploadModelArray") : null;
    }

    public Bundle setDataForResult(String type, int pageId, String search_text, String search_type,String findInPage) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_BUNDLE + "type", type);
        bundle.putInt(KEY_BUNDLE + "page_id", pageId);
        bundle.putString(KEY_BUNDLE + "search_text", search_text);
        bundle.putString(KEY_BUNDLE + "search_type", search_type);
        bundle.putString(KEY_BUNDLE + "find_in_page", findInPage);
        return bundle;
    }

    public String getType(Bundle bundle) {
        return bundle != null ? bundle.getString(KEY_BUNDLE + "type") : "";
    }

    public int getPageId(Bundle bundle) {
        return bundle != null ? bundle.getInt(KEY_BUNDLE + "page_id") : 0;
    }

    public String getFindInPage(Bundle bundle) {
        return bundle != null ? bundle.getString(KEY_BUNDLE + "find_in_page") : "";
    }

    public String getSearchText(Bundle bundle) {
        return bundle != null ? bundle.getString(KEY_BUNDLE + "search_text") : "";
    }

    public String getSearchType(Bundle bundle) {
        return bundle != null ? bundle.getString(KEY_BUNDLE + "search_type") : "";
    }
}
