package com.app.pixstory.ui.dashboard.you.activity.datasource;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PageKeyedDataSource;

import com.app.pixstory.data.DataManager;
import com.app.pixstory.data.DataManagerService;
import com.app.pixstory.data.model.StoryData;
import com.app.pixstory.data.remote.APIService;
import com.app.pixstory.ui.dashboard.upload.api.ApiClient;
import com.app.pixstory.ui.dashboard.you.activity.PageStoryResponse;
import com.app.pixstory.utils.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("CheckResult")
public class SeeMoreStoriesDataSource extends PageKeyedDataSource<Integer, StoryData> {

    private DataManager mDataManager;
    public static final int PAGE_SIZE = 10;
    private static final int FIRST_PAGE = 1;
    private MutableLiveData<String> progressLiveStatus;
    private int pageId;

    public SeeMoreStoriesDataSource(int pageId) {
        mDataManager = DataManagerService.getInstance();
        progressLiveStatus = new MutableLiveData<>();
        this.pageId = pageId;
    }

    public MutableLiveData<String> getProgressLiveStatus() {
        return progressLiveStatus;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull final LoadInitialCallback<Integer, StoryData> callback) {
        progressLiveStatus.postValue(Constants.LOADING_FIRST);
        ApiClient.getClient().create(APIService.class)
                .getPageStories("Bearer" + mDataManager.getAccessToken(), pageId, FIRST_PAGE, PAGE_SIZE)
                .enqueue(new Callback<PageStoryResponse>() {
                    @Override
                    public void onResponse(Call<PageStoryResponse> call, Response<PageStoryResponse> response) {
                        progressLiveStatus.postValue(Constants.LOADED);
                        if (response.body() != null) {
                            callback.onResult(response.body().getData(), null, FIRST_PAGE + 1);
                        }
                    }

                    @Override
                    public void onFailure(Call<PageStoryResponse> call, Throwable t) {
                        t.printStackTrace();
                        progressLiveStatus.postValue(Constants.LOADED);
                    }
                });

    }

    @Override
    public void loadBefore(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, StoryData> callback) {

        ApiClient.getClient().create(APIService.class)
                .getPageStories("Bearer" + mDataManager.getAccessToken(), pageId, params.key, PAGE_SIZE)
                .enqueue(new Callback<PageStoryResponse>() {
                    @Override
                    public void onResponse(Call<PageStoryResponse> call, Response<PageStoryResponse> response) {
                        Integer adjacentKey = (params.key > 1) ? params.key - 1 : null;
                        if (response.body() != null) {
                            callback.onResult(response.body().getData(), adjacentKey);
                        }
                    }

                    @Override
                    public void onFailure(Call<PageStoryResponse> call, Throwable t) {
                        t.printStackTrace();
                        progressLiveStatus.postValue(Constants.LOADED);
                    }
                });
    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, StoryData> callback) {
        progressLiveStatus.postValue(Constants.LOADING_SECOND);
        ApiClient.getClient().create(APIService.class)
                .getPageStories("Bearer" + mDataManager.getAccessToken(), pageId, params.key, PAGE_SIZE)
                .enqueue(new Callback<PageStoryResponse>() {
                    @Override
                    public void onResponse(Call<PageStoryResponse> call, Response<PageStoryResponse> response) {
                        progressLiveStatus.postValue(Constants.LOADED);
                        if (response.body() != null) {
                            callback.onResult(response.body().getData(), params.key + 1);
                        }
                    }

                    @Override
                    public void onFailure(Call<PageStoryResponse> call, Throwable t) {
                        t.printStackTrace();
                        progressLiveStatus.postValue(Constants.LOADED);
                    }
                });
    }
}
