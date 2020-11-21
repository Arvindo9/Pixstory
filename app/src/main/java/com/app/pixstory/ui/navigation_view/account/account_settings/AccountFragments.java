package com.app.pixstory.ui.navigation_view.account.account_settings;

import android.view.View;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.navigation.Navigation;

import com.app.pixstory.R;
import com.app.pixstory.base.BaseFragment;
import com.app.pixstory.data.model.api.UserPrefrenceResponse;
import com.app.pixstory.databinding.FragmentAccountBinding;
import com.app.pixstory.ui.navigation_view.account.AccountNavigator;
import com.app.pixstory.ui.navigation_view.account.AccountViewModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.app.pixstory.utils.Constants.DEFAULT_LOADER;

public class AccountFragments extends BaseFragment<FragmentAccountBinding, AccountViewModel> implements AccountNavigator {
    private FragmentAccountBinding binding;
    private AccountViewModel viewModel;
    private UserPrefrenceResponse.Data data;
    private String[] modifiers = new String[]{
            "public",
            "private",
            "self"
    };
    private int[] modifier = new int[]{
            1,
            2,
            3
    };
    private BottomSheetBehavior preferenceBehaviour;

    @Override
    protected void init() {
        setUp();
        viewModel.setNavigator(this);
        preferenceSheet();
        setSpinners();

        viewModel.getLoading().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    showSimmerLoader(DEFAULT_LOADER);
                } else {
                    hideSimmerLoader();
                }
            }
        });
    }

    private void preferenceSheet() {
        preferenceBehaviour = BottomSheetBehavior.from(binding.bottomSheetPrivacy.privacy);

        preferenceBehaviour.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    preferenceBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                binding.blur.setVisibility(View.VISIBLE);
                binding.blur.setAlpha(slideOffset);
                binding.bottomSheetPrivacy.closePreference.animate().scaleX(0.05f + slideOffset).scaleY(0.05f + slideOffset).setDuration(0).start();
            }
        });

    }

    private void onProceed() {
        boolean yourProfileActivity = binding.bottomSheetPrivacy.showYourProfile.isChecked();
        int yourProfileActivityType = modifier[binding.bottomSheetPrivacy.showYourProfileSpinner.getSelectedItemPosition()];
        boolean myProfileActivity = binding.bottomSheetPrivacy.showMyProfile.isChecked();
        boolean allowMessage = binding.bottomSheetPrivacy.allowOthers.isChecked();
        int allowMessageType = modifier[binding.bottomSheetPrivacy.helpOtherSpinner.getSelectedItemPosition()];
        boolean helpOther = binding.bottomSheetPrivacy.helpOther.isChecked();

        viewModel.updateUserPreference(yourProfileActivity, yourProfileActivityType, myProfileActivity,
                allowMessage, allowMessageType, helpOther);
    }

    private void setUp(){
        binding.accountSettings.setOnClickListener(this::onClick);
        binding.privacySettings.setOnClickListener(this::onClick);
        binding.bottomSheetPrivacy.save.setOnClickListener(this::onClick);
        binding.bottomSheetPrivacy.closePreference.setOnClickListener(this::onClick);
    }

    private void onClick(View view) {
        if (view.getId() == R.id.account_settings){
            Navigation.findNavController(view).navigate(R.id.action_navigation_account_to_navigation_account_settings);
        }
        else if(view.getId() == R.id.privacy_settings)
        {
            viewModel.getUserPrefrence();
        }
        else if(view.getId() == R.id.save)
        {
            onProceed();
        }
        else if(view.getId() == R.id.close_preference)
        {
            preferenceBehaviour.setHideable(true);
            preferenceBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }

    @Override
    protected Class<AccountViewModel> setViewModel() {
        return AccountViewModel.class;
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_account;
    }

    @Override
    protected void getBinding(FragmentAccountBinding binding) {

        this.binding = binding;
    }

    @Override
    protected void getViewModel(AccountViewModel viewModel) {

        this.viewModel = viewModel;
    }

    @Override
    public void getPreference(UserPrefrenceResponse.Data data) {

        this.data = data;

        if (data.getProfileActivityOthers() == 0)
            binding.bottomSheetPrivacy.showYourProfile.setChecked(false);
        else
            binding.bottomSheetPrivacy.showYourProfile.setChecked(true);

        if(data.getProfileActivityType() == 1)
            binding.bottomSheetPrivacy.showYourProfileSpinner.setSelection(0,true);
        else if(data.getProfileActivityType() == 2)
            binding.bottomSheetPrivacy.showYourProfileSpinner.setSelection(1,true);
        else
            binding.bottomSheetPrivacy.showYourProfileSpinner.setSelection(2,true);

        if (data.getProfileActivityInterest() == 0)
            binding.bottomSheetPrivacy.showMyProfile.setChecked(false);
        else
            binding.bottomSheetPrivacy.showMyProfile.setChecked(true);

        if (data.getAllowMessage() == 0)
            binding.bottomSheetPrivacy.allowOthers.setChecked(false);
        else
            binding.bottomSheetPrivacy.allowOthers.setChecked(true);

        if(data.getMessageType() == 1)
            binding.bottomSheetPrivacy.allowOthersSpinner.setSelection(0,true);
        else if(data.getProfileActivityType() == 2)
            binding.bottomSheetPrivacy.allowOthersSpinner.setSelection(1,true);
        else
            binding.bottomSheetPrivacy.allowOthersSpinner.setSelection(2,true);

        if (data.getHelpQuery() == 0)
            binding.bottomSheetPrivacy.helpOther.setChecked(false);
        else
            binding.bottomSheetPrivacy.helpOther.setChecked(true);

        preferenceBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    private void setSpinners(){
        final List<String> plantsList = new ArrayList<>(Arrays.asList(modifiers));
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(getBaseActivity(),R.layout.spinner_item,plantsList);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        binding.bottomSheetPrivacy.showYourProfileSpinner.setAdapter(spinnerArrayAdapter);
        binding.bottomSheetPrivacy.helpOtherSpinner.setAdapter(spinnerArrayAdapter);
    }


    @Override
    public void prefrenceUpdated() {
        getBaseActivity().showToast("Preference Updated");
        preferenceBehaviour.setHideable(true);
        preferenceBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    @Override
    public void message(int message) {
        getBaseActivity().showToast(message);
    }

    @Override
    public void message(String message) {
        getBaseActivity().showToast(message);
    }
}
