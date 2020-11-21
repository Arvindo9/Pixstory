package com.app.pixstory.ui.dashboard.others.datasource;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PageKeyedDataSource;

import com.app.pixstory.data.DataManager;
import com.app.pixstory.data.DataManagerService;
import com.app.pixstory.data.model.api.UserHomeListResponse;
import com.app.pixstory.data.remote.APIService;
import com.app.pixstory.ui.dashboard.upload.api.ApiClient;
import com.app.pixstory.utils.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("CheckResult")
public class InterestDataSource extends PageKeyedDataSource<Integer, UserHomeListResponse.Data> {

    private DataManager mDataManager;
    public static final int PAGE_SIZE = 10;
    private static final int FIRST_PAGE = 1;
    private MutableLiveData<String> progressLiveStatus;

    public InterestDataSource() {
        mDataManager = DataManagerService.getInstance();
        progressLiveStatus = new MutableLiveData<>();
    }

    public MutableLiveData<String> getProgressLiveStatus() {
        return progressLiveStatus;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull final LoadInitialCallback<Integer, UserHomeListResponse.Data> callback) {
        progressLiveStatus.postValue(Constants.LOADING_FIRST);
        ApiClient.getClient().create(APIService.class)
                .getUserHomeList("Bearer" + mDataManager.getAccessToken(), "interest", FIRST_PAGE, PAGE_SIZE)
                .enqueue(new Callback<UserHomeListResponse>() {
                    @Override
                    public void onResponse(Call<UserHomeListResponse> call, Response<UserHomeListResponse> response) {
                        progressLiveStatus.postValue(Constants.LOADED);
                        if (response.body() != null) {
                            callback.onResult(response.body().getData(), null, FIRST_PAGE + 1);
                        }
                    }

                    @Override
                    public void onFailure(Call<UserHomeListResponse> call, Throwable t) {
                        t.printStackTrace();
                        progressLiveStatus.postValue(Constants.LOADED);
                    }
                });
    }

    @Override
    public void loadBefore(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, UserHomeListResponse.Data> callback) {

        ApiClient.getClient().create(APIService.class)
                .getUserHomeList("Bearer" + mDataManager.getAccessToken(), "interest", params.key, PAGE_SIZE)
                .enqueue(new Callback<UserHomeListResponse>() {
                    @Override
                    public void onResponse(Call<UserHomeListResponse> call, Response<UserHomeListResponse> response) {
                        Integer adjacentKey = (params.key > 1) ? params.key - 1 : null;
                        if (response.body() != null) {

                            callback.onResult(response.body().getData(), adjacentKey);
                        }
                    }

                    @Override
                    public void onFailure(Call<UserHomeListResponse> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
    }

    @Override
    public void loadAfter(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, UserHomeListResponse.Data> callback) {
        progressLiveStatus.postValue(Constants.LOADING_SECOND);
        ApiClient.getClient().create(APIService.class)
                .getUserHomeList("Bearer" + mDataManager.getAccessToken(), "interest", params.key, PAGE_SIZE)
                .enqueue(new Callback<UserHomeListResponse>() {
                    @Override
                    public void onResponse(Call<UserHomeListResponse> call, Response<UserHomeListResponse> response) {
                        progressLiveStatus.postValue(Constants.LOADED);
                        if (response.body() != null) {
                            callback.onResult(response.body().getData(), params.key + 1);
                        }
                    }

                    @Override
                    public void onFailure(Call<UserHomeListResponse> call, Throwable t) {
                        t.printStackTrace();
                        progressLiveStatus.postValue(Constants.LOADED);
                    }
                });
    }
}
