package com.app.pixstory.ui.navigation_view.account;

import android.view.MenuItem;

import com.app.pixstory.R;
import com.app.pixstory.base.BaseActivity;
import com.app.pixstory.core.dialog.universal_search.UniversalSearch;
import com.app.pixstory.databinding.ActivityAccountBinding;


public class AccountActivity extends BaseActivity<ActivityAccountBinding, AccountViewModel> {

    private ActivityAccountBinding binding;
    private AccountViewModel viewModel;


    @Override
    public int getLayout() {
        return R.layout.activity_account;
    }

    @Override
    protected void getBinding(ActivityAccountBinding binding) {

        this.binding = binding;
    }

    @Override
    protected void getViewModel(AccountViewModel viewModel) {

        this.viewModel = viewModel;
    }

    @Override
    protected Class<AccountViewModel> setViewModel() {
        return AccountViewModel.class;
    }

    @Override
    protected void init() {

        toolbar(binding.layoutToolbar.toolbar, binding.layoutToolbar.toolbarTitle, R.string.account);
        binding.layoutToolbar.search.setOnClickListener(v -> UniversalSearch.universalSearch(this));

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}