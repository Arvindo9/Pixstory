package com.app.pixstory.ui.dashboard.others.fragment.interest;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.app.pixstory.R;
import com.app.pixstory.base.BaseViewModel;
import com.app.pixstory.data.model.api.UserHomeListResponse;
import com.app.pixstory.ui.dashboard.others.datasource.InterestDataSource;
import com.app.pixstory.ui.dashboard.others.factory.InterestDataSourceFactory;

import java.net.UnknownHostException;

public class ByInterestViewModel extends BaseViewModel<InterestNavigator> {

    private LiveData<PagedList<UserHomeListResponse.Data>> userPagedList;
    private LiveData<String> progressLoadStatus;

    public LiveData<PagedList<UserHomeListResponse.Data>> getItemPagedList() {
        return userPagedList;
    }

    public LiveData<String> getProgressLoadStatus() {
        return progressLoadStatus;
    }

    public ByInterestViewModel() {
        InterestDataSourceFactory interestDataSourceFactory = new InterestDataSourceFactory();

        PagedList.Config pagedListConfig =
                (new PagedList.Config.Builder())
                        .setEnablePlaceholders(false)
                        .setPageSize(InterestDataSource.PAGE_SIZE).build();

        userPagedList =  new LivePagedListBuilder<>(interestDataSourceFactory, pagedListConfig).build();
        progressLoadStatus = Transformations.switchMap(interestDataSourceFactory.getMutableLiveData(), InterestDataSource::getProgressLiveStatus);
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
