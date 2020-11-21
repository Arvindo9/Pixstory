package com.app.pixstory.ui.dashboard.you.activity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.app.pixstory.R;
import com.app.pixstory.base.BaseViewModel;
import com.app.pixstory.data.model.MemberData;
import com.app.pixstory.data.model.StoryData;
import com.app.pixstory.ui.dashboard.you.activity.datasource.SearchMemberDataSource;
import com.app.pixstory.ui.dashboard.you.activity.datasource.SearchStoriesDataSource;
import com.app.pixstory.ui.dashboard.you.activity.datasource.SeeMoreMemberDataSource;
import com.app.pixstory.ui.dashboard.you.activity.datasource.SeeMoreStoriesDataSource;
import com.app.pixstory.ui.dashboard.you.activity.factory.SearchMemberFactory;
import com.app.pixstory.ui.dashboard.you.activity.factory.SearchStoriesFactory;
import com.app.pixstory.ui.dashboard.you.activity.factory.SeeMoreMembersFactory;
import com.app.pixstory.ui.dashboard.you.activity.factory.SeeMoreStoriesFactory;

import java.net.UnknownHostException;

public class ResultViewModel extends BaseViewModel<ResultNavigator> {

    private LiveData<PagedList<StoryData>> storyList = new MutableLiveData<>();
    private LiveData<PagedList<MemberData>> memberList = new MutableLiveData<>();
    private LiveData<String> progressLoadStatus = new MutableLiveData<>();

    public LiveData<PagedList<StoryData>> getStoriesPagedList() {
        return storyList;
    }

    public LiveData<PagedList<MemberData>> getMembersPagedList() {
        return memberList;
    }

    public LiveData<String> getProgressLoadStatus() {
        return progressLoadStatus;
    }

    public void getPageStories(int pageId) {
        SeeMoreStoriesFactory seeMoreStoriesFactory = new SeeMoreStoriesFactory(pageId);

        PagedList.Config pagedListConfig =
                (new PagedList.Config.Builder())
                        .setEnablePlaceholders(false)
                        .setPageSize(SeeMoreStoriesDataSource.PAGE_SIZE).build();

        storyList = new LivePagedListBuilder<>(seeMoreStoriesFactory, pagedListConfig).build();
        progressLoadStatus = Transformations.switchMap(seeMoreStoriesFactory.getMutableLiveData(), SeeMoreStoriesDataSource::getProgressLiveStatus);
    }

    public void getSearchedPageStories(int pageId, String searchText, String searchType, String findInPage) {
        SearchStoriesFactory searchStoriesFactory = new SearchStoriesFactory(pageId, searchText, searchType, findInPage);

        PagedList.Config pagedListConfig =
                (new PagedList.Config.Builder())
                        .setEnablePlaceholders(false)
                        .setPageSize(SearchStoriesDataSource.PAGE_SIZE).build();

        storyList = new LivePagedListBuilder<>(searchStoriesFactory, pagedListConfig).build();
        progressLoadStatus = Transformations.switchMap(searchStoriesFactory.getMutableLiveData(), SearchStoriesDataSource::getProgressLiveStatus);
    }

    public void getPageMembers(int pageId) {
        SeeMoreMembersFactory seeMoreMembersFactory = new SeeMoreMembersFactory(pageId);

        PagedList.Config pagedListConfig =
                (new PagedList.Config.Builder())
                        .setEnablePlaceholders(false)
                        .setPageSize(SeeMoreStoriesDataSource.PAGE_SIZE).build();

        memberList = new LivePagedListBuilder<>(seeMoreMembersFactory, pagedListConfig).build();
        progressLoadStatus = Transformations.switchMap(seeMoreMembersFactory.getMutableLiveData(), SeeMoreMemberDataSource::getProgressLiveStatus);
    }

    public void getSearchedPageMembers(int pageId, String searchText, String searchType, String findInPage) {
        SearchMemberFactory searchMemberFactory = new SearchMemberFactory(pageId, searchText, searchType, findInPage);

        PagedList.Config pagedListConfig =
                (new PagedList.Config.Builder())
                        .setEnablePlaceholders(false)
                        .setPageSize(SearchMemberDataSource.PAGE_SIZE).build();

        memberList = new LivePagedListBuilder<>(searchMemberFactory, pagedListConfig).build();
        progressLoadStatus = Transformations.switchMap(searchMemberFactory.getMutableLiveData(), SearchMemberDataSource::getProgressLiveStatus);
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

    public void shareStory(int storyId, int pageId) {
        isLoading(true);
        getCompositeDisposable().add(getDataManager()
                .shareStory("Bearer" + getDataManager().getAccessToken(), storyId, pageId)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {
                            isLoading(false);
                            if (response != null) {
                                if (response.getSuccess()) {
                                    getNavigator().storyShared(response.getMessage());

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
