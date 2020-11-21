package com.app.pixstory.ui.navigation_view.account.contactus;

import com.app.pixstory.R;
import com.app.pixstory.base.BaseFragment;
import com.app.pixstory.databinding.ContactusBinding;

public class ContactUs extends BaseFragment<ContactusBinding, ContactUsViewModel> {
    private ContactusBinding binding;
    private ContactUsViewModel viewModel;

    @Override
    protected Class<ContactUsViewModel> setViewModel() {
        return ContactUsViewModel.class;

    }

    @Override
    public int getLayout() {
        return R.layout.contactus;
    }

    @Override
    protected void getBinding(ContactusBinding binding) {

        this.binding = binding;
    }

    @Override
    protected void getViewModel(ContactUsViewModel viewModel) {

        this.viewModel = viewModel;
    }

    @Override
    protected void init() {

    }
}
