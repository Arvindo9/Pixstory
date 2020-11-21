package com.app.pixstory.ui.dashboard.upload.activity.page;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;

import com.app.pixstory.R;
import com.app.pixstory.base.BaseActivity;
import com.app.pixstory.core.binding.BindingUtils;
import com.app.pixstory.core.dialog.story.StoryPublished;
import com.app.pixstory.data.model.PageDetailResponse;
import com.app.pixstory.data.model.create_page.PageData;
import com.app.pixstory.data.model.upload.Data;
import com.app.pixstory.databinding.ActivityCreatePageBinding;
import com.app.pixstory.ui.dashboard.upload.model.CreatePageViewModel;
import com.app.pixstory.ui.dashboard.upload.navigator.CreatePageNavigator;
import com.app.pixstory.ui.interests.Interests;
import com.app.pixstory.utils.Constants;
import com.app.pixstory.utils.Utils;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.ArrayList;

import static com.app.pixstory.utils.Constants.CREATION_TYPE;
import static com.app.pixstory.utils.Constants.IS_TYPE;
import static com.app.pixstory.utils.Constants.PAGE_STATUS;
import static com.app.pixstory.utils.Constants.PAGE_TYPE;
import static com.app.pixstory.utils.Constants.PUBLISH_OPTION;

/**
 * Author       : Arvindo Mondal
 * Created date : 13-08-2019
 * Designation  : Programmer
 * Strength     : Never give up
 * Motto        : To be known as great Mathematician
 * Skills       : Algorithms and logic
 */
public class CreatePageActivity extends BaseActivity<ActivityCreatePageBinding, CreatePageViewModel>
        implements CreatePageNavigator {
    private ActivityCreatePageBinding binding;
    private CreatePageViewModel viewModel;
    public BottomSheetBehavior shareOptionBehaviour;
    private ArrayList<Data> dataArrayList;
    private int position = 0;
    String addTitleStr = "", pageAboutStr = "",  type ="";
    private PageDetailResponse.Data data;
    private int[] interest_id;

    @Override
    public int getLayout() {
        return R.layout.activity_create_page;
    }

    @Override
    protected void getBinding(ActivityCreatePageBinding binding) {
        this.binding = binding;
    }

    @Override
    protected void getViewModel(CreatePageViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    protected Class<CreatePageViewModel> setViewModel() {
        return CreatePageViewModel.class;
    }

    @Override
    protected void init() {
        viewModel.setNavigator(this);
        initialization();
        setShareOptionBehaviour();
        clickListener();
        choosePublishOption();
    }

   /* private void initialization() {
        dataArrayList = new ArrayList<>();
        Bundle bundle = getIntent().getBundleExtra(Constants.KEY_DEFAULT_ACTIVITY_BUNDLE);
        if (bundle != null) {
            dataArrayList = bundle.getParcelableArrayList("key");
            addTitleStr = bundle.getString("page_title");
            pageAboutStr = bundle.getString("page_about");
            position = bundle.getInt("position");

            // SET PREVIEW DATA
            binding.pageTitle.setText(addTitleStr);
            binding.pageAbout.setText(pageAboutStr);
            BindingUtils.setStringPhotos(binding.banner, dataArrayList.get(position).getPath(), binding.progressBar);
        }
    }*/

    private void initialization() {
        dataArrayList = new ArrayList<>();
        Bundle bundle = getIntent().getBundleExtra(Constants.KEY_DEFAULT_ACTIVITY_BUNDLE);
        if (bundle != null) {
            type = bundle.getString("type");
            if(type != null && type.equalsIgnoreCase("create")) {
                dataArrayList = bundle.getParcelableArrayList("key");
                addTitleStr = bundle.getString("page_title");
                pageAboutStr = bundle.getString("page_about");
                position = bundle.getInt("position");

                binding.pageTitle.setText(addTitleStr);
                binding.pageAbout.setText(pageAboutStr);
                BindingUtils.setStringPhotos(binding.banner, dataArrayList.get(position).getPath(), binding.progressBar);

            }
            else
            {
                data = Utils.getGsonParser().fromJson(String.valueOf(bundle.get("data")), PageDetailResponse.Data.class);

                if(data != null) {
                    binding.pageTitle.setText(data.getPage().getTitle());
                    binding.pageAbout.setText(data.getPage().getAbout());
                    BindingUtils.setStringPhotos(binding.banner, data.getPage().getCoverImgPath(), binding.progressBar);
                }
            }
        }
    }


    private void clickListener() {
        binding.pageTitle.setOnClickListener(this::onClick);
        binding.createNew.setOnClickListener(this::onClick);
        binding.ok.setOnClickListener(this::onClick);
        binding.bottomSheetPublishOption.publishStory.setOnClickListener(this::onClick);
    }

    //----------

    private void setShareOptionBehaviour() {
        shareOptionBehaviour = BottomSheetBehavior.from(binding.bottomSheetPublishOption.publishOption);
        // set callback for changes
        shareOptionBehaviour.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
               /* if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    shareOptionBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
                }*/
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                binding.blur.setVisibility(View.VISIBLE);
                binding.blur.setAlpha(slideOffset);
            }
        });
    }
    //---------


    private void onClick(View view) {
        if (view.getId() == R.id.ok) {
            shareOptionBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else if (view.getId() == R.id.publish_story) {
            String token = "Bearer" + viewModel.getToken();
            shareOptionBehaviour.setHideable(true);
            shareOptionBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);

            if(type.equalsIgnoreCase("create")) {
                if (Interests.interestsId != null) {
                    interest_id = new int[Interests.interestsId.size()];
                    int j = 0;
                    for (int key : Interests.interestsId.keySet()) {
                        interest_id[j++] = key;
                    }
                }

                viewModel.publishPage(token,type,"", dataArrayList.get(position).getId(), PAGE_TYPE, addTitleStr, pageAboutStr, PUBLISH_OPTION, PAGE_STATUS, interest_id);
            }
            else
            {
                interest_id = new int[data.getPage().getPageInterest().size()];
                for (int i = 0; i < data.getPage().getPageInterest().size(); i++) {
                    interest_id[i] = data.getPage().getPageInterest().get(i).getId();
                }
                viewModel.publishPage(token,type, String.valueOf(data.getPage().getId()),data.getPage().getPhotoId(), PAGE_TYPE, data.getPage().getTitle(), data.getPage().getAbout(), PUBLISH_OPTION, PAGE_STATUS, interest_id);
            }


        }
        /*else if (view.getId() == R.id.publish_story) {
            String token = "Bearer" + viewModel.getToken();
            shareOptionBehaviour.setHideable(true);
            shareOptionBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);

            //-----------------------interest id------------------------
            int[] interest_id = new int[0];
            if (Interests.interestsId != null) {
                interest_id = new int[Interests.interestsId.size()];
                int j = 0;
                for (int key : Interests.interestsId.keySet()) {
                    interest_id[j++] = key;
                }
            }

            viewModel.publishPage(token, dataArrayList.get(position).getId(), PAGE_TYPE, addTitleStr, pageAboutStr, PUBLISH_OPTION, PAGE_STATUS, interest_id);

        }*/
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void choosePublishOption() {
        binding.bottomSheetPublishOption.publishOptionRg.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.for_public) {
                PUBLISH_OPTION = "public";
            } else if (checkedId == R.id.friend) {
                PUBLISH_OPTION = "friends";
            } else if (checkedId == R.id.only_for_me) {
                PUBLISH_OPTION = "only_me";
            }
        });
    }

    @Override
    public void onPageResponse(Boolean success, PageData data) {
        if (success) {
            showToast("Page created successfully");
            CREATION_TYPE = "page";
            StoryPublished.storyPublishedSuccessfully(this, this, getIntent(), IS_TYPE);
        }
    }

    @Override
    public void message(int message) {
        showToast(message);
    }

    @Override
    public void message(String message) {
        showToast(message);
    }
}
