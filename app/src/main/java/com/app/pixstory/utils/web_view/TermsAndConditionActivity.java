package com.app.pixstory.utils.web_view;

import android.os.Build;
import android.text.Html;
import android.view.MenuItem;

import com.app.pixstory.R;
import com.app.pixstory.base.BaseActivity;
import com.app.pixstory.databinding.ActivityTermsAndConditionBinding;
import com.app.pixstory.ui.navigation_view.terms_and_conditions.TermsAndConditionsViewModel;

import static com.app.pixstory.utils.Constants.DEFAULT_LOADER;

/**
 * Created by Kamlesh Yadav on 09-04-2020.
 * Eighteen Pixels India Private Limited Lucknow U.P
 * kamlesh@18pixels.com
 */
public class TermsAndConditionActivity extends BaseActivity<ActivityTermsAndConditionBinding, TermsAndConditionsViewModel> {

    private ActivityTermsAndConditionBinding binding;
    private TermsAndConditionsViewModel viewModel;

    @Override
    public int getLayout() {
        return R.layout.activity_terms_and_condition;
    }

    @Override
    protected void getBinding(ActivityTermsAndConditionBinding binding) {
        this.binding = binding;
    }

    @Override
    protected void getViewModel(TermsAndConditionsViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    protected Class<TermsAndConditionsViewModel> setViewModel() {
        return TermsAndConditionsViewModel.class;
    }

    @Override
    protected void init() {
        toolbar(binding.toolbar.toolbar, binding.toolbar.text, R.string.terms_and_conditions);
        viewModel.getPageData();

        viewModel.getData().observe(this, data -> {
            if (data != null) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    binding.content.setText(Html.fromHtml(data.getContent(), Html.FROM_HTML_MODE_LEGACY));
                } else {
                    binding.content.setText(Html.fromHtml(data.getContent()));
                }
            } else {
                showToast("Something went wrong");
            }
        });

        viewModel.getLoading().observe(this, aBoolean -> {
            if (aBoolean) {
                showSimmerLoader(DEFAULT_LOADER);
            } else {
                hideSimmerLoader();
            }
        });
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
