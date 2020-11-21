package com.app.pixstory.data.model.api;

/**
 * Created by Kamlesh Yadav on 15-04-2020.
 * Eighteen Pixels India Private Limited Lucknow U.P
 * kamlesh@18pixels.com
 */
public class AccountCombinedResponse {
    private AccountDetailResponse accountDetailResponse;
    private CountryResponse countryResponse;

    public AccountCombinedResponse(AccountDetailResponse accountDetailResponse, CountryResponse countryResponse) {
        this.accountDetailResponse = accountDetailResponse;
        this.countryResponse = countryResponse;
    }

    public AccountDetailResponse getAccountDetailResponse() {
        return accountDetailResponse;
    }

    public void setAccountDetailResponse(AccountDetailResponse accountDetailResponse) {
        this.accountDetailResponse = accountDetailResponse;
    }

    public CountryResponse getCountryResponse() {
        return countryResponse;
    }

    public void setCountryResponse(CountryResponse countryResponse) {
        this.countryResponse = countryResponse;
    }
}
