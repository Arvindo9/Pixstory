package com.app.pixstory.ui.dashboard.story_detail;

import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;

import com.app.pixstory.R;
import com.app.pixstory.base.BaseActivity;
import com.app.pixstory.core.binding.BindingUtils;
import com.app.pixstory.core.dialog.follow.Follow;
import com.app.pixstory.data.model.add_support.SupportData;
import com.app.pixstory.data.model.story.PhotoStory;
import com.app.pixstory.data.model.story.Story;
import com.app.pixstory.data.model.story.StoryData;
import com.app.pixstory.data.model.story.StoryInterest;
import com.app.pixstory.databinding.StoryDetailPageBinding;
import com.app.pixstory.ui.dashboard.home.adapter.BadgeAdapter;
import com.app.pixstory.ui.dashboard.story_detail.adapter.FlexboxInterestAdapter;
import com.app.pixstory.ui.dashboard.story_detail.model.StoryDetailModel;
import com.app.pixstory.ui.dashboard.story_detail.navigator.StoryDetailNavigator;
import com.app.pixstory.ui.dashboard.upload.activity.story.UploadActivity;
import com.app.pixstory.ui.dashboard.story_detail.adapter.AddSupportAdapter;
import com.app.pixstory.ui.dashboard.story_detail.adapter.StoryDetailAdapter;
import com.app.pixstory.ui.messages.inbox.InboxActivity;
import com.app.pixstory.utils.Constants;
import com.app.pixstory.utils.GlobalMethods;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.app.pixstory.base.BaseApplication.getContext;
import static com.app.pixstory.utils.Constants.DEFAULT_LOADER;
import static com.app.pixstory.utils.Constants.IS_STORY_FAV;
import static com.app.pixstory.utils.Constants.IS_TYPE;
import static com.app.pixstory.utils.Constants.STORY_ID;

public class StoryDetailAction  extends BaseActivity<StoryDetailPageBinding, StoryDetailModel> implements StoryDetailNavigator {
    private com.app.pixstory.databinding.StoryDetailPageBinding binding;
    private StoryDetailModel viewModel;
    float density = 0.0f;
    private static int NUM_PAGES = 0;
    private static int currentPage = 0;
    private HashMap<Integer, String> addStoryInterest;
    int story_id = 0, id = 0;
    private String story_title = "", fname = "", lname = "";;
    private BottomSheetBehavior notesSheetBehaviour;

    @Override
    public int getLayout() {
        return R.layout.story_detail_page;
    }

    @Override
    protected void getBinding(StoryDetailPageBinding binding) {

        this.binding = binding;
    }

    @Override
    protected void getViewModel(StoryDetailModel viewModel) {

        this.viewModel = viewModel;
    }

    @Override
    protected Class<StoryDetailModel> setViewModel() {
        return StoryDetailModel.class;
    }

    @Override
    protected void init() {
        toolbar(binding.layoutToolbar.toolbar, binding.layoutToolbar.toolbarTitle, R.string.posted_two);
        viewModel.setNavigator(this);

        addStoryInterest = new HashMap<>();
        viewModel.getLoading().observe(this, o -> {
            if ((Boolean) o) {
                showSimmerLoader(DEFAULT_LOADER);
            } else {
                hideSimmerLoader();
            }
        });

        initialization();
        initPager();
        clickListener();
        setNotes();

    }


    private void initialization() {
        Bundle bundle = getIntent().getBundleExtra(Constants.KEY_DEFAULT_ACTIVITY_BUNDLE);
        if (bundle != null) {
            int story_id = bundle.getInt("story_id");
            viewModel.storyDetail(story_id);

        }
    }

    private void initPager() {
        binding.cardStorySlide.pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == (binding.cardStorySlide.pager.getAdapter().getCount() - 1)) {
                    binding.cardStorySlide.next.setVisibility(View.GONE);
                } else {
                    binding.cardStorySlide.next.setVisibility(View.VISIBLE);
                }

                if (position == 0) {
                    binding.cardStorySlide.previous.setVisibility(View.GONE);
                } else {
                    binding.cardStorySlide.previous.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void clickListener() {
        binding.layoutToolbar.cancel.setOnClickListener(this::onClick);
        binding.cardStorySlide.next.setOnClickListener(this::onClick);
        binding.cardStorySlide.previous.setOnClickListener(this::onClick);
        binding.support.setOnClickListener(this::onClick);
        binding.challenge.setOnClickListener(this::onClick);
        binding.notes.setOnClickListener(this::onClick);
        binding.cardStorySlide.previous.setOnClickListener(this::onClick);
        binding.cardStoryNarrative.readNarrative.setOnClickListener(this::onClick);
        binding.cardStoryNarrative.action.setOnClickListener(this::onClick);
        binding.bottomSheetAddNotes.closeNotes.setOnClickListener(this::onClick);
        binding.bottomSheetAddNotes.send.setOnClickListener(this::onClick);
        binding.cardUserProfile.follow.setOnClickListener(this::onClick);
        binding.cardUserProfile.chat.setOnClickListener(this::onClick);


    }

    private void onClick(View view) {
        if (view.getId() == R.id.next) {
            binding.cardStorySlide.pager.setCurrentItem(getItem(+1), true);
        } else if (view.getId() == R.id.previous) {
            binding.cardStorySlide.pager.setCurrentItem(getItem(-1), true);
        } else if (view.getId() == R.id.support) {
            IS_TYPE = "support";
            binding.support.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_white_black_25));
            binding.challenge.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_neon_black_25));
            binding.notes.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_neon_black_25));
            viewModel.getSupport(story_id);
            binding.cardStoryNarrative.narrativeLl.setVisibility(View.GONE);
            binding.cardStoryNarrative.action.setVisibility(View.VISIBLE);
            binding.cardStoryNarrative.addActionType.setText(getResources().getString(R.string.add_support));
        } else if (view.getId() == R.id.challenge) {
            IS_TYPE = "challenge";
            binding.support.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_neon_black_25));
            binding.challenge.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_white_black_25));
            binding.notes.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_neon_black_25));
            viewModel.getChallenge(story_id);
            binding.cardStoryNarrative.narrativeLl.setVisibility(View.GONE);
            binding.cardStoryNarrative.action.setVisibility(View.VISIBLE);
            binding.cardStoryNarrative.addActionType.setText(getResources().getString(R.string.add_challenge));
        } else if (view.getId() == R.id.notes) {
            IS_TYPE = "notes";
            binding.support.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_neon_black_25));
            binding.challenge.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_neon_black_25));
            binding.notes.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_white_black_25));
            viewModel.getNotes(story_id);
            binding.cardStoryNarrative.narrativeLl.setVisibility(View.GONE);
            binding.cardStoryNarrative.action.setVisibility(View.VISIBLE);
            binding.cardStoryNarrative.addActionType.setText(getResources().getString(R.string.add_notes));
        } else if (view.getId() == R.id.readNarrative) {
            binding.support.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_neon_black_25));
            binding.challenge.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_neon_black_25));
            binding.notes.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_neon_black_25));
            binding.cardStoryNarrative.narrativeLl.setVisibility(View.VISIBLE);
            binding.cardStoryNarrative.action.setVisibility(View.GONE);
        } else if (view.getId() == R.id.favourites){
            setStoryFavourites();
        } else if (view.getId() == R.id.cancel) {
            finish();
        } else if (view.getId() == R.id.action) {
            STORY_ID = story_id;
            if (IS_TYPE.equals("support") || IS_TYPE.equals("challenge")) {
                startActivity(UploadActivity.class);
            } else if (IS_TYPE.equals("notes")) {
                notesSheetBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        } else if (view.getId() == R.id.close_notes) {
            notesSheetBehaviour.setHideable(true);
            notesSheetBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else if (view.getId() == R.id.send) {
            String messageStr = binding.bottomSheetAddNotes.message.getText().toString().trim();
            if (messageStr.equals("")) {
                showToast("Please add notes");
            } else {
                viewModel.addNotesStory(messageStr, story_id);
            }
        }else if (view.getId() == R.id.follow){
            followStory();
        } else if (view.getId() == R.id.chat){
            GlobalMethods.intentMethod(getContext(), InboxActivity.class);
        }
    }

    private void followStory() {
        Follow.unFollow(this, 0, fname + " " + lname, new Follow.FollowCallback() {
            @Override
            public void task() {

                viewModel.doFollow(id, 0 == 0 ? 1 : 0);
            }
        });
    }

    private void setStoryFavourites() {
        if (IS_STORY_FAV == 0){
            viewModel.addFavouriteStory(IS_STORY_FAV, story_id);
        } else if (IS_STORY_FAV == 1){
            viewModel.addFavouriteStory(IS_STORY_FAV, story_id);
        }

    }

    private void setNotes() {
        notesSheetBehaviour = BottomSheetBehavior.from(binding.bottomSheetAddNotes.notes);
        notesSheetBehaviour.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    notesSheetBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                binding.blur.setVisibility(View.VISIBLE);
                binding.blur.setAlpha(slideOffset);
            }
        });

    }

    private int getItem(int i) {
        return binding.cardStorySlide.pager.getCurrentItem() + i;
    }

    public void itemLists(List<PhotoStory> photoStories) {
        binding.cardStorySlide.pager.setAdapter(new StoryDetailAdapter(getApplicationContext(), photoStories, 0, this, story_title));
        binding.cardStorySlide.pager.setCurrentItem(0);
    }

    public void itemAnimationLists(List<PhotoStory> photoStories) {
        binding.cardStorySlide.pager.setAdapter(new StoryDetailAdapter(getApplicationContext(), photoStories, 0, this, story_title));
        try {
            density = getResources().getDisplayMetrics().density;
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Set circle indicator radius
        NUM_PAGES = photoStories.size();
        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                binding.cardStorySlide.pager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 5000, 5000);

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
    public void onStoryDetailResponse(Boolean success,
                                      List<PhotoStory> photoStory,
                                      Story story,
                                      List<StoryInterest> storyInterest,
                                      StoryData storyData
    ) {

        if (success) {
            initializeData(story, storyInterest, storyData);
            if (story.getStoryType().equals("quick")) {
                itemLists(photoStory);
            } else {
                itemAnimationLists(photoStory);

            }

        } else {
            showToast("Error!");
            finish();
        }
    }

    @Override
    public void onAddSupportResponse(Boolean success, List<SupportData> data, String message) {
        if (success) {
            addSupportAdapter(data);
        } else {
            binding.cardStoryNarrative.addSupportRecyclerView.setVisibility(View.GONE);
            showToast(message);
        }
    }

    @Override
    public void onAddNotesStoryResponse(Boolean success) {
        if (success){
            notesSheetBehaviour.setHideable(true);
            notesSheetBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
            viewModel.getNotes(story_id);
        }
    }

    @Override
    public void onStoryFavouriteResponse(Boolean success) {
        if (success){
            if (IS_STORY_FAV == 0){
                binding.cardStoryNarrative.favImg.setColorFilter(ContextCompat.getColor(this, R.color.blue), android.graphics.PorterDuff.Mode.SRC_IN);
                IS_STORY_FAV = 1;
            } else if (IS_STORY_FAV == 1){
                binding.cardStoryNarrative.favImg.setColorFilter(ContextCompat.getColor(this, R.color.black), android.graphics.PorterDuff.Mode.SRC_IN);
                IS_STORY_FAV = 0;
            }
        }
    }

    @Override
    public void followStatusChanged(String message, int follow) {

    }

    private void initializeData(Story story, List<StoryInterest> storyInterest, StoryData storyData) {
        // story
        story_id = story.getId();
        story_title = story.getTitle();
        fname = story.getUserFirstName();
        lname = story.getUserLastName();
        id = story.getId();
        binding.cardUserProfile.title.setText(story.getTitle());
        binding.cardStoryNarrative.narrative.setText(story.getNarrative());
        binding.cardUserProfile.username.setText(story.getUsername());
        binding.cardUserProfile.integrityScore.setText(getResources().getString(R.string.integrity_score) + story.getIntegrityScore());
        BindingUtils.setStringPhotos(binding.cardUserProfile.profileImage, story.getCoverImgPath(), binding.cardUserProfile.progressBar);
        // story data
        binding.layoutToolbar.toolbarTitle.setText(storyData.getPublishedOn());

        if (storyData.getSupportCount() == 0) {
            binding.supportCount.setVisibility(View.GONE);
        } else {
            binding.supportCount.setVisibility(View.VISIBLE);
            binding.supportCount.setText(String.valueOf(storyData.getSupportCount()));
        }

        if (storyData.getChallengeCount() == 0) {
            binding.challengeCount.setVisibility(View.GONE);
        } else {
            binding.challengeCount.setVisibility(View.VISIBLE);
            binding.challengeCount.setText(String.valueOf(storyData.getChallengeCount()));
        }

        if (storyData.getNoteCount() == 0) {
            binding.noteCount.setVisibility(View.GONE);
        } else {
            binding.noteCount.setVisibility(View.VISIBLE);
            binding.noteCount.setText(String.valueOf(storyData.getNoteCount()));
        }
        binding.layoutToolbar.viewCount.setText(storyData.getViewCount() + "" + "Views");
        if (storyData.getIsTrending() == 0) {
            binding.cardStorySlide.isTrending.setVisibility(View.GONE);
        } else {
            binding.cardStorySlide.isTrending.setVisibility(View.VISIBLE);
        }
        // story interest
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(this);
        binding.cardStoryNarrative.masterCategory.setLayoutManager(layoutManager);
        FlexboxInterestAdapter adapter = new FlexboxInterestAdapter(storyInterest);
        binding.cardStoryNarrative.masterCategory.setAdapter(adapter);
        adapter.setListener(this::onStoryInterestClick);

        // SHOW BADGE
        BadgeAdapter badgeAdapter = new BadgeAdapter(new ArrayList<>());
        binding.cardUserProfile.badgeRecyclerView.setAdapter(badgeAdapter);
        binding.cardUserProfile.badgeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, true));
        badgeAdapter.addItems(storyData.getBadges());

        // IS STORY FAV
        if (storyData.getIsFav() == 0){
            binding.cardStoryNarrative.favImg.setColorFilter(ContextCompat.getColor(this, R.color.black), android.graphics.PorterDuff.Mode.SRC_IN);
            IS_STORY_FAV = 0;
        } else if (storyData.getIsFav() == 1){
            binding.cardStoryNarrative.favImg.setColorFilter(ContextCompat.getColor(this, R.color.blue), android.graphics.PorterDuff.Mode.SRC_IN);
            IS_STORY_FAV = 1;
        }
    }

    private void onStoryInterestClick(List<StoryInterest> storyInterests, int position, TextView view) {
        if (addStoryInterest.containsKey(storyInterests.get(position).getId())) {
            view.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_white_black_25));
            addStoryInterest.remove(storyInterests.get(position).getId());
        } else {
            view.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_neon_black_25));
            addStoryInterest.put(storyInterests.get(position).getId(), storyInterests.get(position).getTitle());

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addSupportAdapter(List<SupportData> data) {
        AddSupportAdapter addSupportAdapter = new AddSupportAdapter(new ArrayList<>(), IS_TYPE);
        binding.cardStoryNarrative.addSupportRecyclerView.setHasFixedSize(true);
        binding.cardStoryNarrative.addSupportRecyclerView.setAdapter(addSupportAdapter);

        addSupportAdapter.setListener(this::onAddSupportClick);
        addSupportAdapter.addItems(data);

    }

    private void onAddSupportClick(Object object, String tag, int position) {
        if (object instanceof SupportData) {
            if (tag.equals("support") || tag.equals("challenge")){
                int storyId = ((SupportData) object).getStoryId();
                viewModel.storyDetail(storyId);
              /*  Bundle bundle = new Bundle();
                bundle.putInt("story_id", storyId);
                bundle.putInt("position", position);
                startUploadActivity(StoryDetailAction.class, bundle);*/
            } else {
                showToast("On Progress");
            }

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //  finishAffinity();
    }
}
