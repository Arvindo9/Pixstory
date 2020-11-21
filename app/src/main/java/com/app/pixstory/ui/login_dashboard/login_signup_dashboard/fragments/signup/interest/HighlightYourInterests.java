package com.app.pixstory.ui.login_dashboard.login_signup_dashboard.fragments.signup.interest;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;

import com.app.pixstory.R;
import com.app.pixstory.base.BaseActivity;
import com.app.pixstory.base.views.BaseModelView;
import com.app.pixstory.databinding.ActivityHighlightYourInterestsBinding;
import com.app.pixstory.ui.dashboard.DashboardActivity;
import com.app.pixstory.utils.util.Bundles;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static com.app.pixstory.utils.Constants.DEFAULT_LOADER;

public class HighlightYourInterests extends BaseActivity<ActivityHighlightYourInterestsBinding, YourInterestViewModel>
        implements View.OnClickListener, InterestNavigator {

    private HashMap<Integer, Integer> interests;
    private ActivityHighlightYourInterestsBinding binding;
    private YourInterestViewModel viewModel;
    public static BottomSheetBehavior controlYourPrivacyBehaviour;

    // Initializing a String Array
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

    private ArrayList<String> modifiersList = new ArrayList<>(Arrays.asList(modifiers));
    private String loginType;

    @Override
    protected void init() {
        interests = new HashMap<>();
        viewModel.setNavigator(this);
        viewModel.getInterest();
//        selectedStrings = new ArrayList<>();
        toolbar(binding.layoutToolbar.toolbar, binding.layoutToolbar.toolbarTitle, R.string.highlight_your_interest);
        setup();
        subscribeToLiveData();
        clickListener();
        controlYourPrivacy();

        viewModel.getLoading().observe(this, new Observer<Boolean>() {
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

    private void subscribeToLiveData() {
        viewModel.getLiveData().observe(this, data ->{
//            viewModel.addDataToList(data);
            binding.linearBaseLayout.setListener(this::onBaseCalendarIconClick);
            binding.linearBaseLayout.addItems(data);
        });
    }

    private void onBaseCalendarIconClick(View view, View lastView, BaseModelView model){
        if(interests.containsKey(model.getId())){
            view.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_white_black_25));
            interests.remove(model.getId());
        }
        else{
            view.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_neon_black_25));
            interests.put(model.getId(), model.getId());
        }
    }

    @Override
    protected Class<YourInterestViewModel> setViewModel() {
        return YourInterestViewModel.class;
    }

    @Override
    public int getLayout() {
        return R.layout.activity_highlight_your_interests;
    }

    @Override
    protected void getBinding(ActivityHighlightYourInterestsBinding binding) {
        this.binding = binding;
    }

    @Override
    protected void getViewModel(YourInterestViewModel viewModel) {
        this.viewModel = viewModel;
    }

    private void setup(){
        binding.bottomSheetControlYourPrivacy.showYourProfile.setChecked(true);
        binding.bottomSheetControlYourPrivacy.helpOther.setChecked(true);
        binding.bottomSheetControlYourPrivacy.allowOthers.setChecked(true);
        binding.bottomSheetControlYourPrivacy.showMyProfile.setChecked(true);

        Bundle bundle = getIntent().getExtras();
        loginType = Bundles.getInstance().getSignUpDataType(bundle);
        /*
        final YourInterestAdapter adapter = new YourInterestAdapter(numbers, this);
        binding.grid.setAdapter(adapter);
        binding.grid.setOnItemClickListener((parent, v, position, id) -> {
            int selectedIndex = adapter.selectedPositions.indexOf(position);
            if (selectedIndex > -1) {
                adapter.selectedPositions.remove(selectedIndex);
                ((GridItemView) v).display(false);
                selectedStrings.remove(parent.getItemAtPosition(position));
            } else {
                adapter.selectedPositions.add(position);
                ((GridItemView) v).display(true);
                selectedStrings.add((String) parent.getItemAtPosition(position));
            }
        });
        */
    }

    private void clickListener() {
        binding.bottomSheetControlYourPrivacy.expandControlYourPrivacy.setOnClickListener(this);
        binding.getIn.setOnClickListener(this);
        binding.bottomSheetControlYourPrivacy.ok.setOnClickListener(this);
    }

    private void controlYourPrivacy() {
        controlYourPrivacyBehaviour = BottomSheetBehavior.from(binding.bottomSheetControlYourPrivacy.bottomSheetLl);
        // set callback for changes
        controlYourPrivacyBehaviour.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    binding.bottomSheetControlYourPrivacy.views.setVisibility(View.GONE);
                    binding.getIn.setVisibility(View.VISIBLE);
                } else if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    binding.bottomSheetControlYourPrivacy.views.setVisibility(View.VISIBLE);
                    binding.bottomSheetControlYourPrivacy.views.setBackgroundColor(Color.TRANSPARENT);
                    binding.getIn.setVisibility(View.GONE);
                } else {
                    binding.bottomSheetControlYourPrivacy.views.setVisibility(View.VISIBLE);
                    binding.getIn.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                binding.blur.setVisibility(View.VISIBLE);
                binding.blur.setAlpha(slideOffset);
                binding.bottomSheetControlYourPrivacy.views.setAlpha(slideOffset);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.expand_control_your_privacy:
                expandControl();
                break;
            case R.id.get_in:
                getIn();
                break;

            case R.id.ok:
                onProceed();
                break;
        }
    }

    private void onProceed() {
        boolean yourProfileActivity = binding.bottomSheetControlYourPrivacy.showYourProfile.isChecked();
        int yourProfileActivityType = modifier[binding.bottomSheetControlYourPrivacy.showYourProfileSpinner.getSelectedItemPosition()];
        boolean myProfileActivity = binding.bottomSheetControlYourPrivacy.showMyProfile.isChecked();
        boolean allowMessage = binding.bottomSheetControlYourPrivacy.allowOthers.isChecked();
       // String allowMessageType = "";/*modifier[binding.bottomSheetControlYourPrivacy.allowOthersSpinner.getSelectedItemPosition()]*/;
        int allowMessageType = modifier[binding.bottomSheetControlYourPrivacy.helpOtherSpinner.getSelectedItemPosition()];
        boolean helpOther = binding.bottomSheetControlYourPrivacy.helpOther.isChecked();

        viewModel.setPreferences(loginType, yourProfileActivity, yourProfileActivityType, myProfileActivity,
                allowMessage, allowMessageType, helpOther, interests);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void expandControl() {
        controlYourPrivacyBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
        yourPreference();
    }

    private void getIn(){
        binding.getIn.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.image_click));
        if (interests.size() < 1){
            Toast.makeText(this, getResources().getString(R.string.pick_items), Toast.LENGTH_SHORT).show();
        }
        else {
            expandControl();
        }
    }

    private void yourPreference(){
        final List<String> plantsList = new ArrayList<>(Arrays.asList(modifiers));
        // Initializing an ArrayAdapter
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(
                this,R.layout.spinner_item,plantsList);

        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        binding.bottomSheetControlYourPrivacy.showYourProfileSpinner.setAdapter(spinnerArrayAdapter);
     //   binding.bottomSheetControlYourPrivacy.showMyProfileSpinner.setAdapter(spinnerArrayAdapter);
     //   binding.bottomSheetControlYourPrivacy.allowOthersSpinner.setAdapter(spinnerArrayAdapter);
        binding.bottomSheetControlYourPrivacy.helpOtherSpinner.setAdapter(spinnerArrayAdapter);
    }

    @Override
    public void message(int message) {
        showToast(message);
    }

    @Override
    public void message(String message) {
        showToast(message);
    }

    @Override
    public void onSuccess() {
        startActivity(DashboardActivity.class);
    }
}
