package com.app.pixstory.ui.dashboard.you.fragments.followers;

import com.app.pixstory.R;
import com.app.pixstory.base.BaseFragment;
import com.app.pixstory.core.dialog.follow.Follow;
import com.app.pixstory.data.model.you_user.FollowersData;
import com.app.pixstory.data.model.you_user.UserList;
import com.app.pixstory.data.model.you_user.FriendData;
import com.app.pixstory.databinding.FragmentFollowersBinding;
import com.app.pixstory.ui.dashboard.you.adapter.UsernamesAdapter;
import com.app.pixstory.ui.messages.inbox.InboxActivity;
import com.app.pixstory.utils.GlobalMethods;

import java.util.ArrayList;
import java.util.List;

import static com.app.pixstory.utils.Constants.YOU_CHAT;
import static com.app.pixstory.utils.Constants.YOU_FOLLOW;

public class Followers extends BaseFragment<FragmentFollowersBinding, FollowersViewModel> implements FollowersNavigator {
    private FragmentFollowersBinding binding;
    private FollowersViewModel viewModel;

    private UsernamesAdapter usernamesAdapter;
    private static final int PAGE_START = 1;

    // STORY
    private int TOTAL_USER = 10;
    private int TOTAL_USER_COUNT = 1;
    private int CURRENT_USER = PAGE_START;
    private boolean isLoadingUser = false;
    private boolean isLastUser = false;


    private void initAdapter(List<UserList> userData) {
        usernamesAdapter = new UsernamesAdapter(new ArrayList<>());
        usernamesAdapter.setListener(this::onAdapterItem);
        binding.recyclerView.setAdapter(usernamesAdapter);
        usernamesAdapter.addItems(userData);
    }

    private void onAdapterItem(Object data, String tag, int position) {
        if (data instanceof UserList) {
            if (tag.equals(YOU_FOLLOW)) {
                Follow.unFollow(getBaseActivity(), ((UserList) data).getIsFollow(), ((UserList) data).getName() + " " + ((UserList) data).getLname(), new Follow.FollowCallback() {
                    @Override
                    public void task() {

                        viewModel.doFollow(((UserList) data).getUserId(), ((UserList) data).getIsFollow() == 0 ? 1 : 0, position);
                    }
                });
            } else if (tag.equals(YOU_CHAT)) {
                GlobalMethods.intentMethod(getContext(), InboxActivity.class);
            }
        }
    }

    @Override
    protected void init() {
        viewModel.setNavigator(this);
        viewModel.getFollowersList(CURRENT_USER, TOTAL_USER);
    }

    @Override
    protected Class<FollowersViewModel> setViewModel() {
        return FollowersViewModel.class;
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_followers;
    }

    @Override
    protected void getBinding(FragmentFollowersBinding binding) {

        this.binding = binding;
    }

    @Override
    protected void getViewModel(FollowersViewModel viewModel) {

        this.viewModel = viewModel;
    }

    @Override
    public void followStatusChanged(String message, int position, int follow) {
        viewModel.getFollowersList(CURRENT_USER, TOTAL_USER);
    }

    @Override
    public void onFollowersResponse(Boolean success, FollowersData followersData, List<UserList> followList) {
        if (success){
            initAdapter(followList);
        }
    }

    @Override
    public void message(int message) {

    }

    @Override
    public void message(String message) {

    }
}
