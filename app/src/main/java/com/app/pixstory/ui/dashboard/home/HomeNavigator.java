package com.app.pixstory.ui.dashboard.home;

import com.app.pixstory.base.BaseNavigator;
import com.app.pixstory.data.model.get_story_user.Story;
import com.app.pixstory.data.model.get_story_user.StoryUserData;
import com.app.pixstory.data.model.home.HomeData;

import java.util.List;

/**
 * Created by Kamlesh Yadav on 06-04-2020.
 * Eighteen Pixels India Private Limited Lucknow U.P
 * kamlesh@18pixels.com
 */
public interface HomeNavigator extends BaseNavigator {

    void onStoryHomeResponse(Boolean success, List<HomeData> data);
}
