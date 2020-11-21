package com.app.pixstory.ui.dashboard.you.fragments.friends;

import android.view.View;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.pixstory.R;
import com.app.pixstory.base.BaseFragment;
import com.app.pixstory.core.dialog.follow.Follow;
import com.app.pixstory.data.model.you_user.UserList;
import com.app.pixstory.data.model.you_user.FriendData;
import com.app.pixstory.databinding.FragmentFriendsBinding;
import com.app.pixstory.ui.dashboard.you.adapter.UsernamesAdapter;
import com.app.pixstory.ui.messages.inbox.InboxActivity;
import com.app.pixstory.utils.GlobalMethods;
import com.app.pixstory.utils.PaginationScrollListener;

import java.util.ArrayList;
import java.util.List;

import static com.app.pixstory.base.BaseApplication.getContext;
import static com.app.pixstory.utils.Constants.STORY_FILTER_TYPE;
import static com.app.pixstory.utils.Constants.USER_ID;
import static com.app.pixstory.utils.Constants.USER_TYPE;
import static com.app.pixstory.utils.Constants.YOU_CHAT;
import static com.app.pixstory.utils.Constants.YOU_FOLLOW;

public class Friends extends BaseFragment<FragmentFriendsBinding, FriendsViewModel> implements FriendsNavigator {
    private FragmentFriendsBinding binding;
    private FriendsViewModel viewModel;
    private UsernamesAdapter usernamesAdapter;
    private static final int PAGE_START = 1;

    // STORY
    private int TOTAL_USER = 10;
    private int TOTAL_USER_COUNT = 1;
    private int CURRENT_USER = PAGE_START;
    private boolean isLoadingUser = false;
    private boolean isLastUser = false;

    @Override
    protected void init() {
        viewModel.setNavigator(this);
        initAdapter();
        viewModel.getFriendList(CURRENT_USER, TOTAL_USER);

    }

    private void initAdapter() {
        usernamesAdapter = new UsernamesAdapter(new ArrayList<>());
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        usernamesAdapter.setListener(this::onAdapterItem);
        binding.recyclerView.setAdapter(usernamesAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        binding.recyclerView.setLayoutManager(linearLayoutManager);
        binding.recyclerView.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoadingUser = true;
                CURRENT_USER += 1;
                binding.mainProgress.setVisibility(View.VISIBLE);
                loadNextPageStory();
            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_USER_COUNT;
            }

            @Override
            public boolean isLastPage() {
                return isLastUser;
            }

            @Override
            public boolean isLoading() {
                return isLoadingUser;
            }
        });
    }

    private void loadNextPageStory() {
        viewModel.getFriendList(CURRENT_USER, TOTAL_USER);
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
    protected Class<FriendsViewModel> setViewModel() {
        return FriendsViewModel.class;
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_friends;
    }

    @Override
    protected void getBinding(FragmentFriendsBinding binding) {

        this.binding = binding;
    }

    @Override
    protected void getViewModel(FriendsViewModel viewModel) {

        this.viewModel = viewModel;
    }

    @Override
    public void message(int message) {

    }

    @Override
    public void message(String message) {

    }

    @Override
    public void onFriendResponse(Boolean success, FriendData data, List<UserList> friendList) {
        if (success){
            binding.mainProgress.setVisibility(View.GONE);
            if (friendList.size() > 0){
                if (CURRENT_USER == 1) {
                    usernamesAdapter.addItems(friendList);
                    if (CURRENT_USER <= TOTAL_USER_COUNT) usernamesAdapter.addLoadingFooter();
                    else isLastUser = true;
                } else {
                    usernamesAdapter.removeLoadingFooter();
                    isLoadingUser = false;
                    usernamesAdapter.addItems(friendList);
                    TOTAL_USER_COUNT = CURRENT_USER + 1;
                    if (CURRENT_USER != TOTAL_USER_COUNT) usernamesAdapter.addLoadingFooter();
                    else isLastUser = true;
                }
            }
        } else {
            binding.mainProgress.setVisibility(View.GONE);
        }
    }

    @Override
    public void followStatusChanged(String message, int position, int follow) {
        viewModel.getFriendList(CURRENT_USER, TOTAL_USER);
     //   viewModel.getItemPagedList().getValue().get(position).setIsFollow(follow);
      //  viewModel.getItemPagedList().getValue().get(position).setIsPro(follow == 1 ? 1 : 0);
     //   byInterestAdapter.notifyItemChanged(position);
    }
}
