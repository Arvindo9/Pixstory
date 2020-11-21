package com.app.pixstory.ui.dashboard.you.activity.datasource;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PageKeyedDataSource;

import com.app.pixstory.data.DataManager;
import com.app.pixstory.data.DataManagerService;
import com.app.pixstory.data.model.MemberData;
import com.app.pixstory.data.model.PageMemberResponse;
import com.app.pixstory.data.remote.APIService;
import com.app.pixstory.ui.dashboard.upload.api.ApiClient;
import com.app.pixstory.utils.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("CheckResult")
public class SeeMoreMemberDataSource extends PageKeyedDataSource<Integer, MemberData> {

    private DataManager mDataManager;
    public static final int PAGE_SIZE = 10;
    private static final int FIRST_PAGE = 1;
    private MutableLiveData<String> progressLiveStatus;
    private int pageId;

    public SeeMoreMemberDataSource(int pageId) {
        mDataManager = DataManagerService.getInstance();
        progressLiveStatus = new MutableLiveData<>();
        this.pageId = pageId;
    }

    public MutableLiveData<String> getProgressLiveStatus() {
        return progressLiveStatus;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull final LoadInitialCallback<Integer, MemberData> callback) {
        progressLiveStatus.postValue(Constants.LOADING_FIRST);
        ApiClient.getClient().create(APIService.class)
                .getPageMembers("Bearer" + mDataManager.getAccessToken(), pageId, FIRST_PAGE, PAGE_SIZE)
                .enqueue(new Callback<PageMemberResponse>() {
                    @Override
                    public void onResponse(Call<PageMemberResponse> call, Response<PageMemberResponse> response) {
                        progressLiveStatus.postValue(Constants.LOADED);
                        if (response.body() != null) {
                            callback.onResult(response.body().getData(), null, FIRST_PAGE + 1);
                        }
                    }

                    @Override
                    public void onFailure(Call<PageMemberResponse> call, Throwable t) {
                        t.printStackTrace();
                        progressLiveStatus.postValue(Constants.LOADED);
                    }
                });

    }

    @Override
    public void loadBefore(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, MemberData> callback) {

        ApiClient.getClient().create(APIService.class)
                .getPageMembers("Bearer" + mDataManager.getAccessToken(), pageId, params.key, PAGE_SIZE)
                .enqueue(new Callback<PageMemberResponse>() {
                    @Override
                    public void onResponse(Call<PageMemberResponse> call, Response<PageMemberResponse> response) {
                        Integer adjacentKey = (params.key > 1) ? params.key - 1 : null;
                        if (response.body() != null) {
                            callback.onResult(response.body().getData(), adjacentKey);
                        }
                    }

                    @Override
                    public void onFailure(Call<PageMemberResponse> call, Throwable t) {
                        t.printStackTrace();
                        progressLiveStatus.postValue(Constants.LOADED);
                    }
                });
    }

    @Override
    public void loadAfter(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, MemberData> callback) {
        progressLiveStatus.postValue(Constants.LOADING_SECOND);

        ApiClient.getClient().create(APIService.class)
                .getPageMembers("Bearer" + mDataManager.getAccessToken(), pageId, params.key, PAGE_SIZE)
                .enqueue(new Callback<PageMemberResponse>() {
                    @Override
                    public void onResponse(Call<PageMemberResponse> call, Response<PageMemberResponse> response) {
                        progressLiveStatus.postValue(Constants.LOADED);
                        if (response.body() != null) {
                            callback.onResult(response.body().getData(), params.key + 1);
                        }
                    }

                    @Override
                    public void onFailure(Call<PageMemberResponse> call, Throwable t) {
                        t.printStackTrace();
                        progressLiveStatus.postValue(Constants.LOADED);
                    }
                });
    }
}
