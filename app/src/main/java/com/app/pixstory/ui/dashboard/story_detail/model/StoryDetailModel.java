package com.app.pixstory.ui.dashboard.story_detail.model;

import com.app.pixstory.R;
import com.app.pixstory.base.BaseViewModel;
import com.app.pixstory.ui.dashboard.story_detail.navigator.StoryDetailNavigator;

import java.net.UnknownHostException;

import static com.app.pixstory.utils.Constants.BEARER;
import static com.app.pixstory.utils.Constants.IS_STORY_FAV;

public class StoryDetailModel extends BaseViewModel<StoryDetailNavigator> {
    private String token = BEARER + getDataManager().getAccessToken();
    public String getToken() {
        return getDataManager().getAccessToken();
    }

    public void storyDetail(int story_id) {
            isLoading(true);
            isSwipeRefresh(true);
            getCompositeDisposable().add(getDataManager()
                    .storyDetail(token, story_id)
                    .subscribeOn(getSchedulerProvider().io())
                    .observeOn(getSchedulerProvider().ui())
                    .subscribe(response -> {
                                isLoading(false);
                                isSwipeRefresh(false);
                                if (response != null) {
                                    if (response.getSuccess()) {
                                        if (response.getData() != null) {
                                            ((StoryDetailNavigator) getNavigator()).onStoryDetailResponse(
                                                    response.getSuccess(),
                                                    response.getData().getPhotoStory(),
                                                    response.getData().getStory(),
                                                    response.getData().getStoryInterest(),
                                                    response.getData());
                                        }
                                    } else {
                                        ((StoryDetailNavigator) getNavigator()).message(response.getMessage());
                                    }
                                } else {
                                    ((StoryDetailNavigator) getNavigator()).message(R.string.default_error);
                                }
                            },
                            throwable -> {
                                isLoading(false);
                                throwable.printStackTrace();
                            })
            );

        }

    public void getSupport(int story_id) {
        isLoading(true);
        isSwipeRefresh(true);
        getCompositeDisposable().add(getDataManager()
                .addSupport(token, story_id)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {
                            isLoading(false);
                            isSwipeRefresh(false);
                            if (response != null) {
                                if (response.getSuccess()) {
                                    if (response.getData() != null) {
                                        ((StoryDetailNavigator) getNavigator()).onAddSupportResponse(
                                                response.getSuccess(),
                                                response.getData(),
                                                response.getMessage());
                                    } else {
                                        ((StoryDetailNavigator) getNavigator()).message(response.getMessage());
                                    }
                                } else {
                                    ((StoryDetailNavigator) getNavigator()).message(response.getMessage());
                                }
                            } else {
                                ((StoryDetailNavigator) getNavigator()).message(R.string.default_error);
                            }
                        },
                        throwable -> {
                            isLoading(false);
                            throwable.printStackTrace();
                        })
        );
    }

    public void getChallenge(int story_id) {
        isLoading(true);
        isSwipeRefresh(true);
        getCompositeDisposable().add(getDataManager()
                .addChallenge(token, story_id)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {
                            isLoading(false);
                            isSwipeRefresh(false);
                            if (response != null) {
                                if (response.getSuccess()) {
                                    if (response.getData() != null) {
                                        ((StoryDetailNavigator) getNavigator()).onAddSupportResponse(
                                                response.getSuccess(),
                                                response.getData(), response.getMessage());
                                    }
                                } else {
                                    ((StoryDetailNavigator) getNavigator()).message(response.getMessage());
                                }
                            } else {
                                ((StoryDetailNavigator) getNavigator()).message(R.string.default_error);
                            }
                        },
                        throwable -> {
                            isLoading(false);
                            throwable.printStackTrace();
                        })
        );
    }

    public void getNotes(int story_id) {
        isLoading(true);
        isSwipeRefresh(true);
        getCompositeDisposable().add(getDataManager()
                .addNotes(token, story_id)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {
                            isLoading(false);
                            isSwipeRefresh(false);
                            if (response != null) {
                                if (response.getSuccess()) {
                                    if (response.getData() != null) {
                                        ((StoryDetailNavigator) getNavigator()).onAddSupportResponse(
                                                response.getSuccess(),
                                                response.getData(), response.getMessage());
                                    }
                                } else {
                                    ((StoryDetailNavigator) getNavigator()).message(response.getMessage());
                                }
                            } else {
                                ((StoryDetailNavigator) getNavigator()).message(R.string.default_error);
                            }
                        },
                        throwable -> {
                            isLoading(false);
                            throwable.printStackTrace();
                        })
        );
    }

    public void addNotesStory(String message, int story_id) {
        isLoading(true);
        getCompositeDisposable().add(getDataManager()
                .addNotesStory(token, message, story_id)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {
                            isLoading(false);
                            if (response != null) {
                                if (response.getSuccess()) {
                                        ((StoryDetailNavigator) getNavigator()).onAddNotesStoryResponse(
                                                response.getSuccess());
                                } else {
                                    ((StoryDetailNavigator) getNavigator()).message(response.getMessage());
                                }
                            } else {
                                ((StoryDetailNavigator) getNavigator()).message(R.string.default_error);
                            }
                        },
                        throwable -> {
                            isLoading(false);
                            throwable.printStackTrace();
                        })
        );
    }

    public void addFavouriteStory(int is_fav, int story_id) {
        getCompositeDisposable().add(getDataManager()
                .addFavouritesStory(token, is_fav, story_id)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {
                            isLoading(false);
                            if (response != null) {
                                if (response.getSuccess()) {
                                    ((StoryDetailNavigator) getNavigator()).onStoryFavouriteResponse(response.getSuccess());
                                } else {
                                    ((StoryDetailNavigator) getNavigator()).message(response.getMessage());
                                }
                            } else {
                                ((StoryDetailNavigator) getNavigator()).message(R.string.default_error);
                            }
                        },
                        throwable -> {
                            throwable.printStackTrace();
                        })
        );
    }

    public void doFollow(int receiver, int follow) {
        isLoading(true);
        getCompositeDisposable().add(getDataManager()
                .followRequest(token, receiver, follow)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {
                            isLoading(false);
                            if (response != null) {
                                if (response.getSuccess()) {
                                    getNavigator().followStatusChanged(response.getMessage(), follow);

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
