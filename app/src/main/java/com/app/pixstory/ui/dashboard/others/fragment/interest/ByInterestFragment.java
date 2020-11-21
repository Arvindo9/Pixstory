package com.app.pixstory.ui.dashboard.others.fragment.interest;


import android.os.Bundle;
import android.view.View;

import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.pixstory.R;
import com.app.pixstory.base.BaseFragment;
import com.app.pixstory.core.dialog.follow.Follow;
import com.app.pixstory.data.model.api.UserHomeListResponse;
import com.app.pixstory.databinding.FragmentByInterestBinding;
import com.app.pixstory.ui.dashboard.others.adapter.ByInterestAdapter;

import com.app.pixstory.ui.dashboard.you.HomeYouActivity;
import com.app.pixstory.ui.messages.inbox.InboxActivity;
import com.app.pixstory.ui.messages.message.MessageActivity;
import com.app.pixstory.utils.Constants;
import com.app.pixstory.utils.GlobalMethods;
import com.app.pixstory.utils.util.Bundles;
import com.bumptech.glide.Glide;

import java.util.Objects;

import static com.app.pixstory.utils.Constants.DEFAULT_LOADER;

public class ByInterestFragment extends BaseFragment<FragmentByInterestBinding, ByInterestViewModel> implements InterestNavigator {
    private FragmentByInterestBinding binding;
    private ByInterestViewModel viewModel;
    private ByInterestAdapter byInterestAdapter;
    private String type = Constants.MESSAGE_TYPE_INBOX;

    @Override
    protected void init() {
        viewModel.setNavigator(this);
        Glide.with(getBaseActivity()).asGif().load(R.drawable.loading).into(binding.loading);
        byInterestAdapter = new ByInterestAdapter();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getBaseActivity()));
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setAdapter(byInterestAdapter);
        byInterestAdapter.setListener(new ByInterestAdapter.ItemClick() {
            @Override
            public void follow(UserHomeListResponse.Data data, int position) {
                Follow.unFollow(getBaseActivity(), data.getIsFollow(), data.getName() + " " + data.getLname(), new Follow.FollowCallback() {
                    @Override
                    public void task() {

                        viewModel.doFollow(data.getUserId(), data.getIsFollow() == 0 ? 1 : 0, position);
                    }
                });
            }

            @Override
            public void chat(UserHomeListResponse.Data data, int position) {

                getBaseActivity().startActivity(MessageActivity.class, Bundles.getInstance().setMessage(type, data.getUserId(), data.getUsername()));

            }

            @Override
            public void message(String message) {
                getBaseActivity().showToast(message);
            }

            @Override
            public void message(int message) {
                getBaseActivity().showToast(message);
            }

            @Override
            public void itemClicked(Bundle bundle) {
                startUploadActivity(HomeYouActivity.class, bundle);
            }
        });

        viewModel.getItemPagedList().observe(getViewLifecycleOwner(), data -> byInterestAdapter.submitList(data));

        viewModel.getProgressLoadStatus().observe(this, status -> {
            if (Objects.requireNonNull(status).equalsIgnoreCase(Constants.LOADING_FIRST)) {
                firstLoadStart();
            } else if (status.equalsIgnoreCase(Constants.LOADED)) {
                stopLoading();
            } else {
                secondLoadStart();
            }
        });

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

    @Override
    protected Class<ByInterestViewModel> setViewModel() {
        return ByInterestViewModel.class;
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_by_interest;
    }

    @Override
    protected void getBinding(FragmentByInterestBinding binding) {
        this.binding = binding;
    }

    @Override
    protected void getViewModel(ByInterestViewModel viewModel) {
        this.viewModel = viewModel;
    }

    public void firstLoadStart() {
        binding.recyclerView.setVisibility(View.GONE);
        binding.shimmer.setVisibility(View.VISIBLE);
        binding.progress.setVisibility(View.GONE);
    }

    public void secondLoadStart() {
        binding.recyclerView.setVisibility(View.VISIBLE);
        binding.shimmer.setVisibility(View.GONE);
        binding.progress.setVisibility(View.VISIBLE);
        binding.recyclerView.setClipToPadding(true);
        binding.recyclerView.setPadding(0, 0, 0, 108);
    }


    public void stopLoading() {
        binding.recyclerView.setVisibility(View.VISIBLE);
        binding.shimmer.setVisibility(View.GONE);
        binding.progress.setVisibility(View.GONE);
        binding.recyclerView.setClipToPadding(false);
        binding.recyclerView.setPadding(0, 0, 0, 108);
    }

    @Override
    public void followStatusChanged(String message, int position, int follow) {
        viewModel.getItemPagedList().getValue().get(position).setIsFollow(follow);
     //   viewModel.getItemPagedList().getValue().get(position).setIsPro(follow == 1 ? 1 : 0);
        byInterestAdapter.notifyItemChanged(position);
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
