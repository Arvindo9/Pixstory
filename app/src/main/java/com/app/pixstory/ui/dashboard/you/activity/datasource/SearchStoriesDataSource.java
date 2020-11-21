package com.app.pixstory.ui.dashboard.you.activity.datasource;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PageKeyedDataSource;

import com.app.pixstory.data.DataManager;
import com.app.pixstory.data.DataManagerService;
import com.app.pixstory.data.model.PageSearchResponse;
import com.app.pixstory.data.model.StoryData;
import com.app.pixstory.data.remote.APIService;
import com.app.pixstory.ui.dashboard.upload.api.ApiClient;
import com.app.pixstory.utils.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("CheckResult")
public class SearchStoriesDataSource extends PageKeyedDataSource<Integer, StoryData> {

    private DataManager mDataManager;
    public static final int PAGE_SIZE = 10;
    private static final int FIRST_PAGE = 1;
    private MutableLiveData<String> progressLiveStatus;
    private int pageId;
    private String searchText, searchType,findInPage;

    public SearchStoriesDataSource(int pageId, String searchText, String searchType, String findInPage) {
        mDataManager = DataManagerService.getInstance();
        progressLiveStatus = new MutableLiveData<>();
        this.pageId = pageId;
        this.searchText = searchText;
        this.searchType = searchType;
        this.findInPage = findInPage;
    }

    public MutableLiveData<String> getProgressLiveStatus() {
        return progressLiveStatus;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull final LoadInitialCallback<Integer, StoryData> callback) {
        progressLiveStatus.postValue(Constants.LOADING_FIRST);
        ApiClient.getClient().create(APIService.class)
                .pageSearch("Bearer" + mDataManager.getAccessToken(), searchType, searchText, findInPage, pageId, FIRST_PAGE, PAGE_SIZE)
                .enqueue(new Callback<PageSearchResponse>() {
                    @Override
                    public void onResponse(Call<PageSearchResponse> call, Response<PageSearchResponse> response) {
                        progressLiveStatus.postValue(Constants.LOADED);
                        if (response.body() != null && response.body().getData() != null) {
                            callback.onResult(response.body().getData().getStoryData(), null, FIRST_PAGE + 1);
                        }
                    }

                    @Override
                    public void onFailure(Call<PageSearchResponse> call, Throwable t) {
                        t.printStackTrace();
                        progressLiveStatus.postValue(Constants.LOADED);
                    }
                });

    }

    @Override
    public void loadBefore(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, StoryData> callback) {

        ApiClient.getClient().create(APIService.class)
                .pageSearch("Bearer" + mDataManager.getAccessToken(), searchType, searchText, findInPage, pageId, params.key, PAGE_SIZE)
                .enqueue(new Callback<PageSearchResponse>() {
                    @Override
                    public void onResponse(Call<PageSearchResponse> call, Response<PageSearchResponse> response) {
                        Integer adjacentKey = (params.key > 1) ? params.key - 1 : null;
                        if (response.body() != null && response.body().getData() != null) {
                            callback.onResult(response.body().getData().getStoryData(), adjacentKey);
                        }
                    }

                    @Override
                    public void onFailure(Call<PageSearchResponse> call, Throwable t) {
                        t.printStackTrace();
                        progressLiveStatus.postValue(Constants.LOADED);
                    }
                });
    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, StoryData> callback) {
        progressLiveStatus.postValue(Constants.LOADING_SECOND);
        ApiClient.getClient().create(APIService.class)
                .pageSearch("Bearer" + mDataManager.getAccessToken(), searchType, searchText, findInPage, pageId, params.key, PAGE_SIZE)
                .enqueue(new Callback<PageSearchResponse>() {
                    @Override
                    public void onResponse(Call<PageSearchResponse> call, Response<PageSearchResponse> response) {
                        progressLiveStatus.postValue(Constants.LOADED);
                        if (response.body() != null && response.body().getData() != null) {
                            callback.onResult(response.body().getData().getStoryData(), params.key + 1);
                        }
                    }

                    @Override
                    public void onFailure(Call<PageSearchResponse> call, Throwable t) {
                        t.printStackTrace();
                        progressLiveStatus.postValue(Constants.LOADED);
                    }
                });
    }
}
