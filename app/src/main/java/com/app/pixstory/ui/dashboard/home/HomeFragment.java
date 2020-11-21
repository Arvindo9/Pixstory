package com.app.pixstory.ui.dashboard.home;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.pixstory.R;
import com.app.pixstory.base.BaseFragment;
import com.app.pixstory.data.model.api.ListModel;
import com.app.pixstory.data.model.home.HomeData;
import com.app.pixstory.databinding.FragmentHomeBinding;
import com.app.pixstory.ui.dashboard.home.adapter.HomeAdapter;
import com.app.pixstory.ui.dashboard.home.adapter.HomeInterestPageAdapter;
import com.app.pixstory.ui.dashboard.others.HomeOthersActivity;
import com.app.pixstory.ui.dashboard.story_detail.StoryDetailPage;
import com.app.pixstory.ui.dashboard.you.HomeYouActivity;
import com.app.pixstory.utils.ConstantsData;
import com.app.pixstory.utils.GlobalMethods;
import com.app.pixstory.utils.PaginationScrollListener;

import java.util.ArrayList;
import java.util.List;

import static com.app.pixstory.utils.Constants.FROM_YOU;
import static com.app.pixstory.utils.Constants.HOME_FILTER;
import static com.app.pixstory.utils.Constants.USER_TYPE;

public class HomeFragment extends BaseFragment<FragmentHomeBinding, HomeViewModel>
        implements View.OnClickListener, HomeNavigator {

    FragmentHomeBinding binding;
    HomeViewModel viewModel;
    private boolean isInterest = false;
    private String token = "";

    private static final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int currentPage = PAGE_START;
    // limiting to 5 for this tutorial, since total pages in actual API is very large. Feel free to modify.
    private int TOTAL_PAGES = 1;

    private LinearLayoutManager linearLayoutManager;
    private static final String TAG = "HomeFragment";
    private HomeAdapter homeAdapter;

    @Override
    protected void init() {
        viewModel.setNavigator(this);
        token = "Bearer" + viewModel.getToken();
        interestAdapter();
        clickListener();
        initAdapter();
        loadFirstPage();
    }


    private void initAdapter() {
        homeAdapter = new HomeAdapter(new ArrayList<>(), binding.homeRecyclerView);
        binding.homeRecyclerView.setItemAnimator(new DefaultItemAnimator());
        homeAdapter.setListener(this::onAdapterItem);
        binding.homeRecyclerView.setAdapter(homeAdapter);
        linearLayoutManager = new LinearLayoutManager(getContext());
        binding.homeRecyclerView.setLayoutManager(linearLayoutManager);
        binding.homeRecyclerView.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;

                binding.mainProgress.setVisibility(View.VISIBLE);
                loadNextPage();

                // mocking network delay for API call
                /*new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        binding.mainProgress.setVisibility(View.VISIBLE);
                        loadNextPage();
                    }
                }, 1000);*/
            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGES;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });
    }

    private void loadFirstPage() {
        viewModel.getStoryHome(token, currentPage, 2, 3, HOME_FILTER);
    }

    private void loadNextPage() {
        viewModel.getStoryHome(token, currentPage, 2, 3, HOME_FILTER);
    }

    private void interestAdapter() {
        HomeInterestPageAdapter homeInterestAdapter = new HomeInterestPageAdapter(new ArrayList<>());
        homeInterestAdapter.setListener(this::onFilterItemAdapter);
        binding.homeInterestRecyclerView.setAdapter(homeInterestAdapter);
        homeInterestAdapter.addItems(ConstantsData.getList());  //call to set data
    }

    private void onFilterItemAdapter(Object dataFilter, String tag, int position) {
        if (dataFilter instanceof ListModel) {
            String home_filter = ((ListModel) dataFilter).getNameData();
            HOME_FILTER = home_filter;
            binding.mainProgress.setVisibility(View.VISIBLE);
            TOTAL_PAGES = 1;
            isLoading = false;
            isLastPage = false;
            currentPage = PAGE_START;
            initAdapter();
            viewModel.getStoryHome(token, currentPage, 0, 3, HOME_FILTER);


        }
    }

    private void onAdapterItem(Object data, String tag, int position) {
        if (data instanceof HomeData) {
            if (tag.equals("horizontal_banner") || tag.equals("square_banner")) {
                if (((HomeData) data).getArrayType().equals("story")) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("story_id", ((HomeData) data).getStoryId());
                    startUploadActivity(StoryDetailPage.class, bundle);
                } else if (((HomeData) data).getArrayType().equals("page")) {
                    Toast.makeText(getContext(), "Page not created", Toast.LENGTH_SHORT).show();
                }
            } else if (tag.equals("horizontal_profile") || tag.equals("square_profile")) {
                binding.home.setBackgroundColor(getResources().getColor(R.color.neon));
                binding.homeYou.setBackgroundColor(getResources().getColor(R.color.white));
                binding.homeOthers.setBackgroundColor(getResources().getColor(R.color.neon));
                Bundle bundle = new Bundle();
                bundle.putInt("user_id", ((HomeData) data).getUserId());
                startUploadActivity(HomeYouActivity.class, bundle);
            }
        }
    }

    private void clickListener() {
        binding.homeDropDown.setOnClickListener(this);
        binding.homeYou.setOnClickListener(this);
        binding.homeOthers.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_you:
                binding.home.setBackgroundColor(getResources().getColor(R.color.neon));
                binding.homeYou.setBackgroundColor(getResources().getColor(R.color.white));
                binding.homeOthers.setBackgroundColor(getResources().getColor(R.color.neon));
                FROM_YOU = "you";
                GlobalMethods.intentMethod(getContext(), HomeYouActivity.class);
                break;
            case R.id.home_others:
                binding.home.setBackgroundColor(getResources().getColor(R.color.neon));
                binding.homeYou.setBackgroundColor(getResources().getColor(R.color.neon));
                binding.homeOthers.setBackgroundColor(getResources().getColor(R.color.white));
                GlobalMethods.intentMethod(getContext(), HomeOthersActivity.class);
                break;
            case R.id.home_drop_down:
                binding.home.setBackgroundColor(getResources().getColor(R.color.white));
                binding.homeYou.setBackgroundColor(getResources().getColor(R.color.neon));
                binding.homeOthers.setBackgroundColor(getResources().getColor(R.color.neon));
                homeInterestDropDown();
                break;
        }

    }

    @Override
    public void onResume() {
        binding.home.setBackgroundColor(getResources().getColor(R.color.white));
        binding.homeYou.setBackgroundColor(getResources().getColor(R.color.neon));
        binding.homeOthers.setBackgroundColor(getResources().getColor(R.color.neon));
        super.onResume();
    }

    private void homeInterestDropDown() {
        if (isInterest) {
            binding.homeInterestRecyclerView.setVisibility(View.GONE);
            binding.homeDropDown.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_drop_down_black_24dp));
            isInterest = false;
        } else {
            binding.homeInterestRecyclerView.setVisibility(View.VISIBLE);
            binding.homeDropDown.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_drop_up_black_24dp));
            isInterest = true;
        }
    }


    @Override
    protected Class<HomeViewModel> setViewModel() {
        return HomeViewModel.class;
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_home;
    }

    @Override
    protected void getBinding(FragmentHomeBinding binding) {
        this.binding = binding;
    }

    @Override
    protected void getViewModel(HomeViewModel viewModel) {
        this.viewModel = viewModel;
    }


    @Override
    public void message(int message) {
        binding.mainProgress.setVisibility(View.GONE);
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void message(String message) {
        binding.mainProgress.setVisibility(View.GONE);
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStoryHomeResponse(Boolean success, List<HomeData> data) {
        if (success) {
            if (currentPage == 1) {
                binding.mainProgress.setVisibility(View.GONE);
                addItemToAdapter(data);
                if (currentPage <= TOTAL_PAGES) homeAdapter.addLoadingFooter();
                else isLastPage = true;
            } else {
                binding.mainProgress.setVisibility(View.GONE);
                homeAdapter.removeLoadingFooter();
                isLoading = false;
                addItemToAdapter(data);
//                homeAdapter.addItems(data);
                TOTAL_PAGES = currentPage + 1;
                if (currentPage != TOTAL_PAGES) homeAdapter.addLoadingFooter();
                else isLastPage = true;
            }
        } else {
        }
    }

    private void addItemToAdapter(List<HomeData> datas) {
        ArrayList<HomeData> list = new ArrayList<>();
        for (HomeData data:datas) {
            if(data.getPageType() == 1){
                list.add(data);
                list.add(null);
            }
            else{
                list.add(data);
            }
        }

        homeAdapter.addItems(list);
    }

}
