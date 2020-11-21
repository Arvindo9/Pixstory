package com.app.pixstory.ui.interests;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;

import com.app.pixstory.R;
import com.app.pixstory.base.BaseActivity;
import com.app.pixstory.base.flex.InterestFlexboxAdapter;
import com.app.pixstory.base.views.BaseModelView;
import com.app.pixstory.data.model.add_user_interest.AddUserInterestData;
import com.app.pixstory.data.model.global_search.GlobalInterestData;
import com.app.pixstory.data.model.link_user_interest.LinkData;
import com.app.pixstory.databinding.InterestsBinding;
import com.app.pixstory.utils.util.Logger;
import com.google.android.flexbox.FlexboxLayoutManager;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import static com.app.pixstory.utils.Constants.DEFAULT_LOADER;

public class Interests extends BaseActivity<InterestsBinding, InterestsViewModel> implements InterestNavigator {
    private com.app.pixstory.databinding.InterestsBinding binding;
    private InterestsViewModel viewModel;
    private ArrayList<AddUserInterestData> baseModelViews;
    private LinkedHashMap<Integer, Integer> masterInterest, addUserInterest;
    private ArrayList<Integer> addUser;
    int add_user_int = 0;
    public static LinkedHashMap<Integer, Integer> interestsId;
    public String LINK_INTEREST = "";

    private GlobalInterestAdapter autoSuggestAdapter;
    private int userId = -111;

    @Override
    public int getLayout() {
        return R.layout.interests;
    }

    @Override
    protected void getBinding(InterestsBinding binding) {
        this.binding = binding;
    }

    @Override
    protected void getViewModel(InterestsViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    protected Class<InterestsViewModel> setViewModel() {
        return InterestsViewModel.class;
    }

    @Override
    protected void init() {
        toolbar(binding.layoutToolbar.toolbar, binding.layoutToolbar.toolbarTitle, R.string.interests);
        addUserInterest = new LinkedHashMap<>();
        masterInterest = new LinkedHashMap<>();
        interestsId = new LinkedHashMap<>();
        baseModelViews = new ArrayList<>();
        addUser = new ArrayList<>();
        viewModel.setNavigator(this);
        initialization();
        clickListener();
        globalInterestLiveData();

        viewModel.getLoading().observe(this, (Observer) o -> {
            if ((Boolean) o) {
                showSimmerLoader(DEFAULT_LOADER);
            } else {
                hideSimmerLoader();
            }
        });

        // get master list interest
        viewModel.getInterest();
        getMasterInterest();
        setupAutoNewUsers();
    }

    // SEARCH GLOBAL INTEREST LIVE DATA
    private void globalInterestLiveData() {
        viewModel.getGlobalInterestLiveData().observe(this, data -> {
            autoSuggestAdapter.addItems(data);
        });
    }

    // AUTO COMPLETE TEXT
    private void setupAutoNewUsers() {
        //Setting up the adapter for AutoSuggest
        binding.progressBar.progressBar.setVisibility(View.GONE);
        autoSuggestAdapter = new GlobalInterestAdapter(this,
                android.R.layout.simple_dropdown_item_1line);
        binding.user.setThreshold(1);
        binding.user.setAdapter(autoSuggestAdapter);
        binding.user.setOnItemClickListener(
                (parent, view, position, id) -> {
                    //TODO onItem select
                    userId = autoSuggestAdapter.getItem(position) != null ? autoSuggestAdapter.getItem(position).getId() : -111;
                });
        binding.user.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Logger.e("onTextChanged:" + s + ", " + start + ", " + before + ", " + count);
                Log.e("onTextChanged:", s + ", " + start + ", " + before + ", " + count);
                userId = -111;
                if (before <= count) {
                    if (!TextUtils.isEmpty(binding.user.getText())) {
                        if (binding.user.getText().toString().length() > 1) {
                            viewModel.getGlobalInterestList(binding.user.getText().toString());
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }


    // ADD USER INTEREST ADAPTER
    private void addUserInterestList(AddUserInterestData data) {
        baseModelViews.add(data);
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(this);
        binding.addUserInterest.setLayoutManager(layoutManager);
        InterestFlexboxAdapter adapter = new InterestFlexboxAdapter(baseModelViews);
        binding.addUserInterest.setAdapter(adapter);
        adapter.setListener(this::onAdapterItem);
        for (int i = 0; i < baseModelViews.size(); i++) {
            interestsId.put(baseModelViews.get(i).getId(), baseModelViews.get(i).getId());
        }
    }

    private void onAdapterItem(List<AddUserInterestData> addUserInterestData, int position, TextView view) {
        if (addUserInterest.containsKey(addUserInterestData.get(position).getId())) {
            view.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_white_black_25));
            addUserInterest.remove(addUserInterestData.get(position).getId());
            addUser.remove(addUserInterestData.get(position).getId());

        } else {
            view.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_neon_black_25));
            addUserInterest.put(addUserInterestData.get(position).getId(), addUserInterestData.get(position).getId());
            addUser.add(addUserInterestData.get(position).getId());
        }
    }

    // GET MASTER INTEREST
    private void getMasterInterest() {
        viewModel.getLiveData().observe(this, data -> {
            binding.masterCategory.setListener(this::onMasterInterestClick);
            binding.masterCategory.addItems(data);
        });
    }

    // MASTER INTEREST LISTENER
    private void onMasterInterestClick(View view, View lastView, BaseModelView model) {
        if (masterInterest.containsKey(model.getId())) {
            view.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_white_black_25));
            masterInterest.remove(model.getId());
        } else {
            view.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_neon_black_25));
            masterInterest.put(model.getId(), model.getId());
        }
    }

    private void clickListener() {
        binding.add.setOnClickListener(this::onClick);
        binding.link.setOnClickListener(this::onClick);
        binding.ok.setOnClickListener(this::onClick);
    }

    private void onClick(View view) {
        if (view.getId() == R.id.add) {
            add();
        } else if (view.getId() == R.id.link) {
            LINK_INTEREST = "link";
            linkInterest();
        } else if (view.getId() == R.id.ok) {
            LINK_INTEREST = "ok";
            category();
        }
    }

    private void category() {
        // add interest
        for (int j = 0; j < addUser.size(); j++) {
            add_user_int = addUser.get(j);
        }
        if (add_user_int == 0) {
            showToast("Please select interest");
        } else {
            finish();
        }
    }

    private void linkInterest() {
        // master link interest
        int[] interest = new int[masterInterest.size()];
        int i = 0;
        for (int key : masterInterest.keySet()) {
            interest[i++] = key;
        }

        // add interest
        for (int j = 0; j < addUser.size(); j++) {
            add_user_int = addUser.get(j);
        }

        if (addUser.size() == 0){
            showToast("Please add interest");
        } else if (addUser.size() > 1) {
            showToast("Please select only one interest");
        } else if (interest.length == 0) {
            showToast("Please add category");
        } else {
            viewModel.linkUserInterest(add_user_int, interest);
        }

    }

    private void initialization() {
        binding.user.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                if (binding.user.getText().toString().isEmpty()) {
                    Toast.makeText(Interests.this, "Enter interest", Toast.LENGTH_SHORT).show();
                } else {
                    addInterest(binding.user.getText().toString().trim());
                    binding.user.setText("");
                }
                return true;
            }
            return false;
        });
    }

    public void add() {
        if (binding.user.getText().toString().isEmpty()) {
            Toast.makeText(this, "Enter interest", Toast.LENGTH_SHORT).show();
            return;
        }
        addInterest(binding.user.getText().toString().trim());
        binding.user.setText("");
    }

    private void addInterest(String interest) {
        viewModel.addUserInterest(interest);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
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
    public void onAddUserInterestResponse(boolean status, AddUserInterestData addUserInterestData) {
        if (status) {
            binding.user.setText("");
            addUserInterestList(addUserInterestData);
        } else {
            showToast("Data not found");
        }
    }

    @Override
    public void onLinkInterestResponse(boolean b, List<LinkData> linkData) {
        if (b) {
            if (LINK_INTEREST.equals("link")) {
                showToast("Interest link successfully");
            } else if (LINK_INTEREST.equals("ok")) {
                finish();
            }
        } else {
            showToast("Not linked");
        }
    }

    @Override
    public void onNewMessageSend(Boolean success, List<GlobalInterestData> messageUsers) {
        if (success) {
            //   binding.user.setText(messageUsers.get(0).getTitle());
        }
    }

    @Override
    public void showProgress(boolean status) {
        binding.progressBar.progressBar.setVisibility(status ? View.VISIBLE : View.GONE);
    }
}
