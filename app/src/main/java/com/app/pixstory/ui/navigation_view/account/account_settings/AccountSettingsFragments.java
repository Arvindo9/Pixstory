package com.app.pixstory.ui.navigation_view.account.account_settings;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import androidx.annotation.NonNull;
import com.app.pixstory.R;
import com.app.pixstory.base.BaseFragment;
import com.app.pixstory.core.dialog.date.DateDialog;
import com.app.pixstory.core.dialog.otp.Verification;
import com.app.pixstory.data.model.api.AccountDetailResponse;
import com.app.pixstory.data.model.api.CountryResponse;
import com.app.pixstory.databinding.FragmentAccountSettingsBinding;
import com.app.pixstory.utils.General;
import com.app.pixstory.utils.Utils;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.app.pixstory.utils.Constants.DEFAULT_LOADER;
import static com.app.pixstory.utils.Constants.FROM_ACCOUNT;
import static com.app.pixstory.utils.Constants.LOGIN_TYPE_MOBILE_NUMBER;
import static com.app.pixstory.utils.Constants.SKIP;
import static com.app.pixstory.utils.Constants.VERIFY;

public class AccountSettingsFragments extends BaseFragment<FragmentAccountSettingsBinding, AccountSettingsViewModel> implements AccountSettingsNavigator, View.OnClickListener, Verification.VerificationCallback {
    private FragmentAccountSettingsBinding binding;
    private AccountSettingsViewModel viewModel;
    private String firstName, lastName, email, username, gender, dob, countryId, mobile, countryCode;
    private String gen, dob_1, country_id;
    private List<String> country = new ArrayList<>();
    private List<CountryResponse.Data> countryListData = new ArrayList<>();
    private BottomSheetBehavior changePasswordBehaviour;
    private AccountDetailResponse.User userData;

    @Override
    protected void init() {

        viewModel.setNavigator(this);
        viewModel.getAccountDetailData();
        setClickListeners();
        setTextWatchers();
        binding.spinner.setEnabled(false);
        binding.dateOfBirth.setEnabled(false);
        binding.ccp.setEnabled(false);
        binding.setLifecycleOwner(this);
        changePassword();

        viewModel.getLoading().observe(getViewLifecycleOwner(), aBoolean -> {
            if (aBoolean) {
                showSimmerLoader(DEFAULT_LOADER);
            } else {
                hideSimmerLoader();
            }
        });

        binding.genderRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {

            if (checkedId == R.id.male) {
                gender = "male";
            } else if (checkedId == R.id.female) {
                gender = "female";
            } else if (checkedId == R.id.others) {
                gender = "others";
            }

            if (gen.equalsIgnoreCase(gender)) {
                binding.genderOk.setVisibility(View.GONE);
                binding.genderEdit.setVisibility(View.VISIBLE);

            } else {
                binding.genderOk.setVisibility(View.VISIBLE);
                binding.genderEdit.setVisibility(View.GONE);
            }

        });

        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                countryId = String.valueOf(countryListData.get(position).getId());
                if (countryId.equalsIgnoreCase(country_id)) {
                    binding.spinnerOk.setVisibility(View.GONE);
                    binding.spinnerEdit.setVisibility(View.VISIBLE);
                } else {
                    binding.spinnerOk.setVisibility(View.VISIBLE);
                    binding.spinnerEdit.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void changePassword() {
        changePasswordBehaviour = BottomSheetBehavior.from(binding.bottomSheetChangePassword.changePassword);

        changePasswordBehaviour.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    changePasswordBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                binding.blur.setVisibility(View.VISIBLE);
                binding.blur.setAlpha(slideOffset);
                binding.bottomSheetChangePassword.closeChangePassword.animate().scaleX(0.05f + slideOffset).scaleY(0.05f + slideOffset).setDuration(0).start();
            }
        });

    }

    private void setTextWatchers() {

        binding.firstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0 || s.toString().equalsIgnoreCase(firstName)) {
                    binding.inputTextName.setError(null);
                    binding.firstNameOk.setVisibility(View.GONE);
                    binding.firstNameEdit.setVisibility(View.VISIBLE);
                } else {
                    binding.inputTextName.setError(null);
                    binding.firstNameOk.setVisibility(View.VISIBLE);
                    binding.firstNameEdit.setVisibility(View.GONE);
                }
            }
        });

        binding.lastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0 || s.toString().equalsIgnoreCase(lastName)) {
                    binding.inputTextLastName.setError(null);
                    binding.lastNameOk.setVisibility(View.GONE);
                    binding.lastNameEdit.setVisibility(View.VISIBLE);
                } else {
                    binding.inputTextLastName.setError(null);
                    binding.lastNameOk.setVisibility(View.VISIBLE);
                    binding.lastNameEdit.setVisibility(View.GONE);
                }
            }
        });

        binding.username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0 || s.toString().equalsIgnoreCase(username)) {
                    binding.inputTextUsername.setError(null);
                    binding.usernameOk.setVisibility(View.GONE);
                    binding.usernameEdit.setVisibility(View.VISIBLE);
                } else {
                    binding.inputTextUsername.setError(null);
                    binding.usernameOk.setVisibility(View.VISIBLE);
                    binding.usernameEdit.setVisibility(View.GONE);
                }
            }
        });

        binding.email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0 || s.toString().equalsIgnoreCase(email)) {
                    binding.inputTextEmail.setError(null);
                    binding.emailOk.setVisibility(View.GONE);
                    binding.emailEdit.setVisibility(View.VISIBLE);
                } else {
                    binding.inputTextEmail.setError(null);
                    binding.emailOk.setVisibility(View.VISIBLE);
                    binding.emailEdit.setVisibility(View.GONE);
                }
            }
        });

        binding.phoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0 || s.toString().equalsIgnoreCase(mobile)) {
                    binding.inputTextPhoneNumber.setError(null);
                    binding.mobileOk.setVisibility(View.GONE);
                    binding.mobileEdit.setVisibility(View.VISIBLE);
                } else {
                    binding.inputTextPhoneNumber.setError(null);
                    binding.mobileOk.setVisibility(View.VISIBLE);
                    binding.mobileEdit.setVisibility(View.GONE);
                }
            }
        });

    }

    private void setClickListeners() {

        binding.firstNameEdit.setOnClickListener(this);
        binding.firstNameOk.setOnClickListener(this);
        binding.lastNameEdit.setOnClickListener(this);
        binding.lastNameOk.setOnClickListener(this);
        binding.usernameEdit.setOnClickListener(this);
        binding.usernameOk.setOnClickListener(this);
        binding.genderEdit.setOnClickListener(this);
        binding.genderOk.setOnClickListener(this);
        binding.dateOfBirth.setOnClickListener(this);
        binding.dobEdit.setOnClickListener(this);
        binding.dobOk.setOnClickListener(this);
        binding.spinnerEdit.setOnClickListener(this);
        binding.spinnerOk.setOnClickListener(this);
        binding.emailEdit.setOnClickListener(this);
        binding.emailOk.setOnClickListener(this);
        binding.mobileEdit.setOnClickListener(this);
        binding.mobileOk.setOnClickListener(this);
        binding.changePassword.setOnClickListener(this);
        binding.bottomSheetChangePassword.closeChangePassword.setOnClickListener(this);
        binding.bottomSheetChangePassword.savePassword.setOnClickListener(this);

    }

    @Override
    protected Class<AccountSettingsViewModel> setViewModel() {
        return AccountSettingsViewModel.class;
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_account_settings;
    }

    @Override
    protected void getBinding(FragmentAccountSettingsBinding binding) {

        this.binding = binding;
    }

    @Override
    protected void getViewModel(AccountSettingsViewModel viewModel) {

        this.viewModel = viewModel;
    }

    @Override
    public void accountDetailsFetched(AccountDetailResponse.User userData, List<CountryResponse.Data> countryList) {

        if (userData != null && countryList != null) {

            this.userData = userData;
            firstName = userData.getName();
            lastName = userData.getLname();
            username = userData.getUsername();
            email = userData.getEmail();
            gender = userData.getGender();
            dob = General.getDateReadable1(userData.getDob());
            countryId = String.valueOf(userData.getCountryId());
            mobile = userData.getMobile();
            countryCode = userData.getCountryCode();
            gen = gender;
            dob_1 = dob;
            countryListData = countryList;
            country_id = countryId;

            binding.setUser(userData);

            binding.firstName.setText(firstName);
            binding.lastName.setText(lastName);
            binding.username.setText(username);
            binding.dateOfBirth.setText(dob);
            binding.email.setText(email);
            binding.phoneNumber.setText(mobile);

            if (gender.equalsIgnoreCase("male"))
                binding.male.setChecked(true);
            else if (gender.equalsIgnoreCase("female"))
                binding.female.setChecked(true);
            else
                binding.others.setChecked(true);


            setCountry(countryList);
            countryListData = countryList;
            binding.spinner.setSelection(Utils.getSpinnerIndex(binding.spinner, userData.getCountryName()));


        } else {
            getBaseActivity().showToast("Account data not found");
        }
    }

    private void setCountry(List<CountryResponse.Data> countryList) {
        for (int i = 0; i < countryList.size(); i++) {
            country.add(countryList.get(i).getCountryName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getBaseActivity(), R.layout.simple_spinner_item, country);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinner.setAdapter(adapter);
    }

    @Override
    public void accountUpdated(String message, String type) {

        if (type.equalsIgnoreCase("firstname")) {
            binding.inputTextLastName.setError(null);
            binding.lastNameOk.setVisibility(View.GONE);
            binding.lastNameEdit.setVisibility(View.VISIBLE);
            userData.setName(Objects.requireNonNull(binding.firstName.getText()).toString().trim());
            getBaseActivity().showToast("First name updated");
        } else if (type.equalsIgnoreCase("lastname")) {
            binding.inputTextLastName.setError(null);
            binding.lastNameOk.setVisibility(View.GONE);
            binding.lastNameEdit.setVisibility(View.VISIBLE);
            userData.setLname(Objects.requireNonNull(binding.lastName.getText()).toString().trim());
            getBaseActivity().showToast("Last name updated");
        } else if (type.equalsIgnoreCase("username")) {
            binding.inputTextUsername.setError(null);
            binding.usernameOk.setVisibility(View.GONE);
            binding.usernameEdit.setVisibility(View.VISIBLE);
            userData.setUsername(Objects.requireNonNull(binding.username.getText()).toString().trim());
            getBaseActivity().showToast("Username updated");
        } else if (type.equalsIgnoreCase("gender")) {
            binding.male.setClickable(false);
            binding.female.setClickable(false);
            binding.others.setClickable(false);
            binding.genderOk.setVisibility(View.GONE);
            binding.genderEdit.setVisibility(View.VISIBLE);
            userData.setGender(gender);
            getBaseActivity().showToast("Gender updated");
        } else if (type.equalsIgnoreCase("dob")) {
            binding.dobOk.setVisibility(View.GONE);
            binding.dobEdit.setVisibility(View.VISIBLE);
            userData.setDob(dob);
            getBaseActivity().showToast("Date of birth updated");
        } else if (type.equalsIgnoreCase("country")) {
            binding.spinnerOk.setVisibility(View.GONE);
            binding.spinnerEdit.setVisibility(View.VISIBLE);
            userData.setCountryId(Integer.valueOf(countryId));
            getBaseActivity().showToast("Country updated");
        } else if (type.equalsIgnoreCase("email")) {
            binding.inputTextEmail.setError(null);
            binding.emailOk.setVisibility(View.GONE);
            binding.emailEdit.setVisibility(View.VISIBLE);
            userData.setEmail(Objects.requireNonNull(binding.email.getText()).toString().trim());
            getBaseActivity().showToast("Country updated");
        } else if (type.equalsIgnoreCase("mobile")) {
            String countryCode = binding.ccp.getDefaultCountryCode();
            if (!countryCode.contains("+")) {
                countryCode = "+" + countryCode;
            }
            userData.setMobile(Objects.requireNonNull(binding.phoneNumber.getText()).toString().trim());
            userData.setCountryCode(countryCode);
            Verification.verify(getBaseActivity(), this, countryCode, Objects.requireNonNull(binding.phoneNumber.getText()).toString().trim(), "", LOGIN_TYPE_MOBILE_NUMBER, false, "", FROM_ACCOUNT);
        }
    }

    @Override
    public void usernameValid() {
        viewModel.updateAccountDetail("username", firstName, lastName, email, Objects.requireNonNull(binding.username.getText()).toString().trim(), gender, dob, countryId, mobile, countryCode);
    }

    @Override
    public void usernameInvalid(Map<String, List<String>> error) {
        if (error != null) {
            if (error.containsKey("username")) {
                binding.inputTextUsername.setError(Objects.requireNonNull(error.get("username")).get(0));
            }
        }
    }

    @Override
    public void passwordChanged(String message) {
        getBaseActivity().showToast(message);
        changePasswordBehaviour.setHideable(true);
        changePasswordBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    @Override
    public void onMobileVerifies(boolean status, String message) {
        if (status) {
            binding.inputTextPhoneNumber.setError(null);
            binding.mobileOk.setVisibility(View.GONE);
            binding.mobileEdit.setVisibility(View.VISIBLE);
            getBaseActivity().showToast("Mobile number updated");
        } else {
            binding.inputTextPhoneNumber.setError(null);
            binding.mobileOk.setVisibility(View.VISIBLE);
            binding.mobileEdit.setVisibility(View.GONE);
        }
    }

    @Override
    public void message(int message) {
        getBaseActivity().showToast(message);
    }

    @Override
    public void message(String message) {
        getBaseActivity().showToast(message);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.first_name_edit:
                Utils.setFocus(binding.firstName);
                Utils.openKeypad(getBaseActivity(), binding.firstName);
                break;
            case R.id.first_name_ok:
                viewModel.updateAccountDetail("firstname", Objects.requireNonNull(binding.firstName.getText()).toString().trim(), lastName, email, username, gender, dob, countryId, mobile, countryCode);
                break;
            case R.id.last_name_edit:
                Utils.setFocus(binding.lastName);
                Utils.openKeypad(getBaseActivity(), binding.lastName);
                break;
            case R.id.last_name_ok:
                viewModel.updateAccountDetail("lastname", firstName, Objects.requireNonNull(binding.lastName.getText()).toString().trim(), email, username, gender, dob, countryId, mobile, countryCode);
                break;
            case R.id.username_edit:
                Utils.setFocus(binding.username);
                Utils.openKeypad(getBaseActivity(), binding.username);
                break;
            case R.id.username_ok:
                viewModel.checkUsername(Objects.requireNonNull(binding.username.getText()).toString().trim());
                break;
            case R.id.gender_edit:
                binding.male.setClickable(true);
                binding.female.setClickable(true);
                binding.others.setClickable(true);
                break;
            case R.id.gender_ok:
                viewModel.updateAccountDetail("gender", firstName, lastName, email, username, gender, dob, countryId, mobile, countryCode);
                break;
            case R.id.dob_edit:
                binding.dateOfBirth.setEnabled(true);
                break;
            case R.id.dob_ok:
                viewModel.updateAccountDetail("dob", firstName, lastName, email, username, gender, dob, countryId, mobile, countryCode);
                break;
            case R.id.date_of_birth:
                DateDialog dateFragment = new DateDialog();
                dateFragment.setup(this::date, DateDialog.TYPE_DATE_BIRTH);
                getBaseActivity().startDialog(dateFragment, DateDialog.TAG);
                break;

            case R.id.spinner_edit:
                binding.spinner.setEnabled(true);
                break;

            case R.id.spinner_ok:
                viewModel.updateAccountDetail("country", firstName, lastName, email, username, gender, dob, countryId, mobile, countryCode);
                break;

            case R.id.email_edit:
                Utils.setFocus(binding.email);
                Utils.openKeypad(getBaseActivity(), binding.email);
                break;

            case R.id.email_ok:
                viewModel.updateAccountDetail("email", firstName, lastName, Objects.requireNonNull(binding.email.getText()).toString().trim(), username, gender, dob, countryId, mobile, countryCode);
                break;

            case R.id.mobile_edit:
                Utils.setFocus(binding.phoneNumber);
                binding.ccp.setEnabled(true);
                Utils.openKeypad(getBaseActivity(), binding.phoneNumber);
                break;

            case R.id.mobile_ok:
                String countryCode = binding.ccp.getDefaultCountryCode();
              /*  if (!countryCode.contains("+")) {
                    countryCode = "+" + countryCode;
                }*/
                viewModel.updateAccountDetail("mobile", firstName, lastName, email, username, gender, dob, countryId, Objects.requireNonNull(binding.phoneNumber.getText()).toString().trim(), countryCode);
                break;

            case R.id.change_password:
                changePasswordBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
                break;

            case R.id.close_change_password:
                changePasswordBehaviour.setHideable(true);
                changePasswordBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
                break;

            case R.id.save_password:

                if (Objects.requireNonNull(binding.bottomSheetChangePassword.currentPassword.getText()).toString().trim().isEmpty()) {
                    getBaseActivity().showToast("Enter current password");
                    return;
                }

                if (Objects.requireNonNull(binding.bottomSheetChangePassword.newPassword.getText()).toString().trim().isEmpty()) {
                    getBaseActivity().showToast("Enter new password");
                    return;
                }

                if (Objects.requireNonNull(binding.bottomSheetChangePassword.confirmPassword.getText()).toString().trim().isEmpty()) {
                    getBaseActivity().showToast("Enter confirm password");
                    return;
                }

                viewModel.updatePassword(
                        Objects.requireNonNull(binding.bottomSheetChangePassword.currentPassword.getText()).toString().trim(),
                        Objects.requireNonNull(binding.bottomSheetChangePassword.newPassword.getText()).toString().trim(),
                        Objects.requireNonNull(binding.bottomSheetChangePassword.confirmPassword.getText()).toString().trim()
                );

                break;
        }
    }

    public void date(String readableData, String sendDate, String... params) {
        binding.dateOfBirth.setText(readableData);
        if (dob_1.equalsIgnoreCase(readableData)) {
            binding.dobOk.setVisibility(View.GONE);
            binding.dobEdit.setVisibility(View.VISIBLE);
        } else {
            binding.dobOk.setVisibility(View.VISIBLE);
            binding.dobEdit.setVisibility(View.GONE);
            dob = readableData;
        }
    }

    @Override
    public void onVerifyRequestMobile(String countryCodePicker, String mobile, String loginType, String otp) {
        viewModel.onVerifyOtpMobile(loginType, VERIFY, "", "", mobile, countryCodePicker, otp);
    }

    @Override
    public void onResendOtpMobile(String loginType, String countryCode, String mobile, String type) {
        viewModel.updateAccountDetail("mobile", firstName, lastName, email, username, gender, dob, countryId, mobile, countryCode);
    }

    @Override
    public void skipSignUp() {
        // No code
    }

    @Override
    public void skipVerification(String mobile, String countryCode, String otp) {
        viewModel.onVerifyOtpMobile(LOGIN_TYPE_MOBILE_NUMBER, SKIP, "", "", mobile, countryCode, "1234");
    }

    @Override
    public void onVerifyRequestEmail(String email, String loginType, String otp, String token) {
        // No code
    }

    @Override
    public void onResendOtpEmail(String loginType, String email) {
        // No code
    }

}
