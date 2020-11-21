package com.app.pixstory.ui.dashboard.others.fragment.nearby;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.app.pixstory.R;
import com.app.pixstory.base.BaseViewModel;
import com.app.pixstory.data.model.api.UserHomeListResponse;
import com.app.pixstory.ui.dashboard.others.datasource.InterestDataSource;
import com.app.pixstory.ui.dashboard.others.datasource.NearbyDataSource;
import com.app.pixstory.ui.dashboard.others.factory.NearbyDataSourceFactory;

import java.net.UnknownHostException;

public class NearbyViewModel extends BaseViewModel<NearbyNavigator> {

    private LiveData<PagedList<UserHomeListResponse.Data>> userPagedList;
    private LiveData<String> progressLoadStatus;

    public LiveData<String> getProgressLoadStatus() {
        return progressLoadStatus;
    }

    public LiveData<PagedList<UserHomeListResponse.Data>> getNearbyPagedList() {
        return userPagedList;
    }

    public NearbyViewModel() {
        NearbyDataSourceFactory nearbyDataSourceFactory = new NearbyDataSourceFactory();

        PagedList.Config pagedListConfig =
                (new PagedList.Config.Builder())
                        .setEnablePlaceholders(false)
                        .setPageSize(InterestDataSource.PAGE_SIZE).build();

        userPagedList = new LivePagedListBuilder<>(nearbyDataSourceFactory, pagedListConfig).build();
        progressLoadStatus = Transformations.switchMap(nearbyDataSourceFactory.getMutableLiveData(), NearbyDataSource::getProgressLiveStatus);
    }

    public void doFollow(int receiver, int follow, int position) {
        isLoading(true);
        getCompositeDisposable().add(getDataManager()
                .followRequest("Bearer" + getDataManager().getAccessToken(), receiver, follow)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {
                            isLoading(false);
                            if (response != null) {
                                if (response.getSuccess()) {
                                    getNavigator().followStatusChanged(response.getMessage(), position, follow);

                                } else {
                                    getNavigator().message(response.getMessage());
                                }
                            } else {
                                getNavigator().message(R.string.default_error);
                            }
                        },
                        throwable -> {
                            isLoading(false);
                            if (throwable instanceof UnknownHostException) {
                                getNavigator().message(R.string.default_network_error);
                            } else {
                                getNavigator().message(R.string.default_error);
                            }
                            throwable.printStackTrace();
                        })
        );
    }

}
