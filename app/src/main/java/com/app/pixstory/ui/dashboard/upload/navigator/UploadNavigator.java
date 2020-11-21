package com.app.pixstory.ui.dashboard.upload.navigator;

import com.app.pixstory.base.BaseNavigator;
import com.app.pixstory.data.model.interest.Interest;
import com.app.pixstory.data.model.story.StoryData;
import com.app.pixstory.data.model.upload.Data;
import com.app.pixstory.data.model.upload_details.DetailData;
import com.app.pixstory.data.model.upload_image.UploadModel;
import com.app.pixstory.ui.dashboard.upload.model.MediaModel;

import java.util.List;

public interface UploadNavigator extends BaseNavigator {

    void onMyPhotoResponse(boolean b, List<Data> data, String type);

    void onDetailResponse(boolean b, DetailData detailData);

    void onStoryResponse(boolean b, StoryData storyData);


    void onFileUploadSuccess(MediaModel model, Data uploadModel);

    void onMasterInterest(Boolean success, List<Interest> interest);
}
