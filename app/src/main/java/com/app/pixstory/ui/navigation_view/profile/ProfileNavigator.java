package com.app.pixstory.ui.navigation_view.profile;

import com.app.pixstory.base.BaseNavigator;
import com.app.pixstory.data.model.api.BioResponse;
import com.app.pixstory.data.model.api.Citation;
import com.app.pixstory.data.model.api.Education;
import com.app.pixstory.data.model.api.UserProfileResponse;
import com.app.pixstory.data.model.api.Work;

import java.util.List;

public interface ProfileNavigator extends BaseNavigator {

    void userDataFetched(UserProfileResponse.Data data);

    void workUpdated(List<Work> data);

    void educationUpdated(List<Education> data);

    void citationUpdated(List<Citation> data);

    void bioUpdated(BioResponse.Data data);

    void imageUploaded(String path);

    void workChanged(List<Work> data, String type);

    void educationChanged(List<Education> data, String type);

    void citationChanged(List<Citation> data, String type);

}
