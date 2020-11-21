package com.app.pixstory.ui.universal_search;

import android.view.MenuItem;

import com.app.pixstory.R;
import com.app.pixstory.base.BaseActivity;
import com.app.pixstory.databinding.ActivityUniversalSearchBinding;

public class UniversalSearchActivity extends BaseActivity<ActivityUniversalSearchBinding, UniversalSearchViewModel> {

    private ActivityUniversalSearchBinding binding;
    private UniversalSearchViewModel viewModel;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void init() {
        toolbar(binding.layoutToolbar.toolbar, binding.layoutToolbar.toolbarTitle, R.string.results);
    }

    @Override
    protected Class<UniversalSearchViewModel> setViewModel() {
        return UniversalSearchViewModel.class;
    }

    @Override
    public int getLayout() {
        return R.layout.activity_universal_search;
    }

    @Override
    protected void getBinding(ActivityUniversalSearchBinding binding) {

        this.binding = binding;
    }

    @Override
    protected void getViewModel(UniversalSearchViewModel viewModel) {

        this.viewModel = viewModel;
    }
}
