package com.app.pixstory.data.model.get_story_user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Kamlesh Yadav on 06-04-2020.
 * Eighteen Pixels India Private Limited Lucknow U.P
 * kamlesh@18pixels.com
 */
public class StoryUserData {
    @SerializedName("story")
    @Expose
    private List<Story> story = null;

    public List<Story> getStory() {
        return story;
    }

    public void setStory(List<Story> story) {
        this.story = story;
    }
}
