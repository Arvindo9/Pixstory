package com.app.pixstory.utils;

import com.app.pixstory.data.model.api.ListModel;
import com.app.pixstory.data.model.db.messages.MessageUsers;
import com.app.pixstory.ui.dashboard.you.model.YouFilterModel;

import java.util.ArrayList;

import static com.app.pixstory.utils.Constants.CREATED;
import static com.app.pixstory.utils.Constants.FAV;
import static com.app.pixstory.utils.Constants.FAVOURITES;
import static com.app.pixstory.utils.Constants.FOLLOWING;
import static com.app.pixstory.utils.Constants.HOME_FILTER_FEATURE;
import static com.app.pixstory.utils.Constants.HOME_FILTER_FEATURE_DATA;
import static com.app.pixstory.utils.Constants.HOME_FILTER_FRIEND;
import static com.app.pixstory.utils.Constants.HOME_FILTER_FRIEND_DATA;
import static com.app.pixstory.utils.Constants.HOME_FILTER_LATEST;
import static com.app.pixstory.utils.Constants.HOME_FILTER_LATEST_DATA;
import static com.app.pixstory.utils.Constants.HOME_FILTER_PAGE;
import static com.app.pixstory.utils.Constants.HOME_FILTER_PAGE_DATA;
import static com.app.pixstory.utils.Constants.HOME_FILTER_TRENDING;
import static com.app.pixstory.utils.Constants.HOME_FILTER_TRENDING_DATA;
import static com.app.pixstory.utils.Constants.MEMBERSHIP;
import static com.app.pixstory.utils.Constants.PAGE_CREATED;
import static com.app.pixstory.utils.Constants.PAGE_FOLLOWING;
import static com.app.pixstory.utils.Constants.PAGE_MEMBERSHIP;
import static com.app.pixstory.utils.Constants.PAGE_REQUEST_CREATED;
import static com.app.pixstory.utils.Constants.PAGE_REQUEST_FOLLOW;
import static com.app.pixstory.utils.Constants.PAGE_REQUEST_MEMBER;
import static com.app.pixstory.utils.Constants.PHOTO_CREATED;
import static com.app.pixstory.utils.Constants.PHOTO_FAVOURITES;
import static com.app.pixstory.utils.Constants.STORY_CREATED;
import static com.app.pixstory.utils.Constants.STORY_FAVOURITES;
import static com.app.pixstory.utils.Constants.UN_FAV;

public class ConstantsData {

    // home
    private static String[] interest = {
            HOME_FILTER_TRENDING, HOME_FILTER_FEATURE, HOME_FILTER_LATEST, HOME_FILTER_FRIEND, HOME_FILTER_PAGE
    };

    private static String[] filterData = {
            HOME_FILTER_TRENDING_DATA, HOME_FILTER_FEATURE_DATA, HOME_FILTER_LATEST_DATA, HOME_FILTER_FRIEND_DATA, HOME_FILTER_PAGE_DATA
    };

    public static ArrayList<ListModel> getList() {
        ArrayList<ListModel> listModels = new ArrayList<>();
        for (int i = 0; i < interest.length; i++) {
            ListModel listModel = new ListModel();
            listModel.setName(interest[i]);
            listModel.setNameData(filterData[i]);
            listModels.add(listModel);
        }
        return listModels;
    }

    // you filter story
    private static String[] filterStories = {
            CREATED, FAVOURITES
    };

    private static String[] filterActionStory = {
            STORY_CREATED, STORY_FAVOURITES
    };
    private static String[] filterStorySetData = {
            UN_FAV, FAV
    };

    public static ArrayList<YouFilterModel> youFilterStoriesList() {
        ArrayList<YouFilterModel> youFilterModels = new ArrayList<>();
        for (int i = 0; i < filterStories.length; i++) {
            YouFilterModel youFilterModel = new YouFilterModel();
            youFilterModel.setFilterName(filterStories[i]);
            youFilterModel.setSetFilterAction(filterActionStory[i]);
            youFilterModel.setSetDataName(filterStorySetData[i]);
            youFilterModels.add(youFilterModel);
        }
        return youFilterModels;
    }

    // you filter page
    private static String[] youFilterPages = {
            CREATED, FOLLOWING, MEMBERSHIP
    };

    private static String[] filterActionPage = {
            PAGE_CREATED, PAGE_FOLLOWING, PAGE_MEMBERSHIP
    };
    private static String[] filterPageSetData = {
            PAGE_REQUEST_CREATED, PAGE_REQUEST_FOLLOW, PAGE_REQUEST_MEMBER
    };

    public static ArrayList<YouFilterModel> youFilterPagesList() {
        ArrayList<YouFilterModel> youFilterModels = new ArrayList<>();
        for (int i = 0; i < youFilterPages.length; i++) {
            YouFilterModel youFilterModel = new YouFilterModel();
            youFilterModel.setFilterName(youFilterPages[i]);
            youFilterModel.setSetFilterAction(filterActionPage[i]);
            youFilterModel.setSetDataName(filterPageSetData[i]);
            youFilterModels.add(youFilterModel);
        }
        return youFilterModels;
    }

    // you filter photo
    private static String[] filterPhoto = {
            CREATED, FAVOURITES
    };

    private static String[] filterActionPhoto = {
            PHOTO_CREATED, PHOTO_FAVOURITES
    };
    private static String[] filterPhotoSetData = {
            UN_FAV, FAV
    };

    public static ArrayList<YouFilterModel> youFilterPhotoList() {
        ArrayList<YouFilterModel> youFilterModels = new ArrayList<>();
        for (int i = 0; i < filterPhoto.length; i++) {
            YouFilterModel youFilterModel = new YouFilterModel();
            youFilterModel.setFilterName(filterPhoto[i]);
            youFilterModel.setSetFilterAction(filterActionPhoto[i]);
            youFilterModel.setSetDataName(filterPhotoSetData[i]);
            youFilterModels.add(youFilterModel);
        }
        return youFilterModels;
    }

    //Message------
    public static ArrayList<MessageUsers> getMessageUserList() {
        ArrayList<MessageUsers> listModels = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            MessageUsers listModel = new MessageUsers();
            listModels.add(listModel);
        }
        return listModels;
    }
}
