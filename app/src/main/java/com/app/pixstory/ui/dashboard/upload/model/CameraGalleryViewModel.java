package com.app.pixstory.ui.dashboard.upload.model;

import com.app.pixstory.base.BaseViewModel;

/**
 * Created by Kamlesh Yadav on 16-04-2020.
 * Eighteen Pixels India Private Limited Lucknow U.P
 * kamlesh@18pixels.com
 */
public class CameraGalleryViewModel extends BaseViewModel {
    public String getToken() {
        return getDataManager().getAccessToken();
    }
}
