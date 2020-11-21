package com.app.pixstory.ui.dashboard.story_detail.navigator;

import com.app.pixstory.base.BaseNavigator;
import com.app.pixstory.data.model.add_support.SupportData;
import com.app.pixstory.data.model.story.PhotoStory;
import com.app.pixstory.data.model.story.Story;
import com.app.pixstory.data.model.story.StoryData;
import com.app.pixstory.data.model.story.StoryInterest;

import java.util.List;

public interface StoryDetailNavigator extends BaseNavigator {

    void onStoryDetailResponse(Boolean success, List<PhotoStory> photoStory, Story story, List<StoryInterest> storyInterest, StoryData data);

    void onAddSupportResponse(Boolean success, List<SupportData> data, String message);

    void onAddNotesStoryResponse(Boolean success);

    void onStoryFavouriteResponse(Boolean success);

    void followStatusChanged(String message, int follow);
}
