package com.app.pixstory.ui.navigation_view.profile;

import com.app.pixstory.R;
import com.app.pixstory.base.BaseViewModel;

import java.net.UnknownHostException;

import okhttp3.MultipartBody;

public class ProfileViewModel extends BaseViewModel<ProfileNavigator> {

    void getUserProfileData() {
        isLoading(true);
        getCompositeDisposable().add(getDataManager()
                .getUserProfileData("Bearer" + getDataManager().getAccessToken())
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {
                            isLoading(false);
                            if (response != null) {
                                if (response.getSuccess()) {
                                    getNavigator().userDataFetched(response.getData());
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

    void addWorkExperience(String jobTitle, String organisation, String isCurrent) {

        isLoading(true);
        getCompositeDisposable().add(getDataManager()
                .addWorkExperience("Bearer" + getDataManager().getAccessToken(), jobTitle, organisation, isCurrent)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {
                            isLoading(false);
                            if (response != null) {
                                if (response.getSuccess()) {
                                    getNavigator().workUpdated(response.getData());
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

    void addEducation(String degree, String university, String institute) {

        isLoading(true);
        getCompositeDisposable().add(getDataManager()
                .addEducation("Bearer" + getDataManager().getAccessToken(), degree, university, institute)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {
                            isLoading(false);
                            if (response != null) {
                                if (response.getSuccess()) {
                                    getNavigator().educationUpdated(response.getData());
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

    void addCitation(String description, String link) {

        isLoading(true);
        getCompositeDisposable().add(getDataManager()
                .addCitation("Bearer" + getDataManager().getAccessToken(), description, link)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {
                            isLoading(false);
                            if (response != null) {
                                if (response.getSuccess()) {
                                    getNavigator().citationUpdated(response.getData());
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

    void addBio(String bio) {
        isLoading(true);
        getCompositeDisposable().add(getDataManager()
                .addBio("Bearer" + getDataManager().getAccessToken(), bio)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {
                            isLoading(false);
                            if (response != null) {
                                if (response.getSuccess()) {
                                    getNavigator().bioUpdated(response.getData());
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

    void uploadImage(MultipartBody.Part body) {
            isLoading(true);
            getCompositeDisposable().add(getDataManager()
                    .uploadProfileImage(body, "Bearer" + getDataManager().getAccessToken())
                    .subscribeOn(getSchedulerProvider().io())
                    .observeOn(getSchedulerProvider().ui())
                    .subscribe(response -> {
                                isLoading(false);
                                if (response != null) {
                                    if (response.getSuccess()) {
                                        getNavigator().imageUploaded(response.getData().getThumbnail());
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

    void updateDeleteWork(String id, String type, String jobTitle, String organisation, String isCurrent) {
        isLoading(true);
        getCompositeDisposable().add(getDataManager()
                .updateDeleteWorkExperience("Bearer" + getDataManager().getAccessToken(), id, type, jobTitle, organisation, isCurrent)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {
                            isLoading(false);
                            if (response != null) {
                                if (response.getSuccess()) {
                                    getNavigator().workChanged(response.getData(), type);
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

    void updateDeleteEducation(String id, String type, String degree, String university, String institute) {
        isLoading(true);
        getCompositeDisposable().add(getDataManager()
                .updateDeleteEducation("Bearer" + getDataManager().getAccessToken(), id, type, degree, university, institute)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {
                            isLoading(false);
                            if (response != null) {
                                if (response.getSuccess()) {
                                    getNavigator().educationChanged(response.getData(), type);
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

    void updateDeleteCitation(String id, String type, String description, String link) {
        isLoading(true);
        getCompositeDisposable().add(getDataManager()
                .updateDeleteCitation("Bearer" + getDataManager().getAccessToken(), id, type, description, link)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {
                            isLoading(false);
                            if (response != null) {
                                if (response.getSuccess()) {
                                    getNavigator().citationChanged(response.getData(), type);
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