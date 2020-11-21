package com.app.pixstory.ui.navigation_view.account.account_settings;

import com.app.pixstory.base.BaseNavigator;
import com.app.pixstory.data.model.api.AccountDetailResponse;
import com.app.pixstory.data.model.api.CountryResponse;
import java.util.List;
import java.util.Map;

public interface AccountSettingsNavigator extends BaseNavigator {

    void accountDetailsFetched(AccountDetailResponse.User userData, List<CountryResponse.Data> countryList);

    void accountUpdated(String message, String type);

    void usernameValid();

    void usernameInvalid(Map<String, List<String>> error);

    void passwordChanged(String message);

    void onMobileVerifies(boolean status, String message);
}
