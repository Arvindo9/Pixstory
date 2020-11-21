package com.app.pixstory.ui.dashboard.story_detail;

import android.content.Intent;
import android.view.View;

import com.app.pixstory.R;
import com.app.pixstory.base.BaseActivity;
import com.app.pixstory.ui.dashboard.story_detail.adapter.StoryFullPhotoViewPagerAdapter;
import com.app.pixstory.data.model.story.PhotoStory;
import com.app.pixstory.databinding.ActivityPhotoScreenBinding;
import com.app.pixstory.ui.dashboard.story_detail.model.PhotoScreenViewModel;

import java.util.ArrayList;

public class StoryPhotoFullViewActivity extends BaseActivity<ActivityPhotoScreenBinding, PhotoScreenViewModel> {


    private ActivityPhotoScreenBinding binding;
    private PhotoScreenViewModel viewModel;
    ArrayList<PhotoStory> listData;
    int position = 0;
    private String story_title = "";

    @Override
    public int getLayout() {
        return R.layout.activity_photo_screen;
    }

    @Override
    protected void getBinding(ActivityPhotoScreenBinding binding) {

        this.binding = binding;
    }

    @Override
    protected void getViewModel(PhotoScreenViewModel viewModel) {

        this.viewModel = viewModel;
    }

    @Override
    protected Class<PhotoScreenViewModel> setViewModel() {
        return PhotoScreenViewModel.class;
    }

    @Override
    protected void init() {
        itemLists();
        setUP();
    }

    private void setUP(){
        binding.close.setOnClickListener(this::onClick);
    }

    private void onClick(View view) {
        if (view.getId() == R.id.close){
            finish();
        }
    }

    public void itemLists() {
        Intent intent = getIntent();
        listData = new ArrayList<>();
        listData = (ArrayList<PhotoStory>) intent.getSerializableExtra("info");
        position = intent.getIntExtra("position", position);
        story_title = intent.getStringExtra("story_title");

        binding.title.setText(story_title);

        binding.pager.setAdapter(new StoryFullPhotoViewPagerAdapter(getApplicationContext(), listData, position, this));
        binding.pager.setCurrentItem(position);

    }
}
