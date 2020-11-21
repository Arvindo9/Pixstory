package com.app.pixstory.ui.dashboard.you.navigator;

import com.app.pixstory.base.BaseNavigator;

/**
 * Created by Kamlesh Yadav on 18-04-2020.
 * Eighteen Pixels India Private Limited Lucknow U.P
 * kamlesh@18pixels.com
 */
public interface DeleteImageNavigator extends BaseNavigator {
    void onImageDeletedResponse(Boolean success, String message);

    void onImageFavouriteResponse(Boolean success, String message);
}
