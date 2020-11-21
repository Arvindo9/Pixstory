package com.app.pixstory.ui.dashboard.you.navigator;

import com.app.pixstory.base.BaseNavigator;
import com.app.pixstory.data.model.get_story_user.Story;
import com.app.pixstory.data.model.get_story_user.StoryUserData;
import com.app.pixstory.data.model.page_list.PageListData;
import com.app.pixstory.data.model.upload.Data;

import java.util.List;

/**
 * Created by Kamlesh Yadav on 06-04-2020.
 * Eighteen Pixels India Private Limited Lucknow U.P
 * kamlesh@18pixels.com
 */
public interface HomeYouNavigator extends BaseNavigator {

    void onStoryUserResponse(Boolean success, StoryUserData data, List<Story> story);

    void onPageResponse(Boolean success, List<PageListData> data);

    void onMyPhotoResponse(boolean b, List<Data> data, String type);
}
