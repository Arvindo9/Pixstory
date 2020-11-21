package com.app.pixstory.base;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import com.app.pixstory.R;
import com.app.pixstory.utils.Constants;

import org.jetbrains.annotations.NotNull;

import static com.app.pixstory.utils.Constants.DEFAULT_LOADER;
import static com.app.pixstory.utils.Constants.HOME_LOADER;
import static com.app.pixstory.utils.Constants.STORY_VIEW_LOADER;
import static com.app.pixstory.utils.Constants.UPLOAD_LOADER;
import static com.app.pixstory.utils.Constants.YOU_STORY_LOADER;

public abstract class BaseFragment<B extends ViewDataBinding, V extends ViewModel> extends Fragment {

    private BaseActivity activity;
    private B binding;
    private V viewModel;
    private final String FragmentContextExecption = "Fragment exception";
    private Dialog dialog;

    public interface Callback {
        void onFragmentAttached(Fragment fragment);

        void onFragmentDetached();
    }

    /*@Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialog = new Dialog(activity, android.R.style.Theme_Black_NoTitleBar_Fullscreen);

        onCreateFragment(savedInstanceState, getArguments());
        setHasOptionsMenu(false);
    }*/


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialog = new Dialog(activity, android.R.style.Theme_Black_NoTitleBar_Fullscreen);

        viewModel = ViewModelProviders.of(activity).get(setViewModel());
        getViewModel(viewModel);
        onCreateFragment(savedInstanceState, getArguments());
        setHasOptionsMenu(false);
    }




    @Override
    public void onAttach(@NotNull(FragmentContextExecption) Context context) {
        super.onAttach(context);
        if (context instanceof BaseActivity) {
            BaseActivity activity = (BaseActivity) context;
            this.activity = activity;
            activity.onFragmentAttached(this);
        }
    }

    /*@Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = DataBindingUtil.inflate(inflater, getLayout(), container, false);
        viewModel = ViewModelProviders.of(activity).get(setViewModel());
        getBinding(binding);
        getViewModel(viewModel);
        return binding.getRoot();
    }*/


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = DataBindingUtil.inflate(inflater, getLayout(), container, false);
        getBinding(binding);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        getBaseActivity().onFragmentDetached();
        activity = null;
        viewModel = null;

        System.gc();
    }

    public BaseActivity getBaseActivity() {
        return activity;
    }

    /**
     * @param savedInstanceState save the instance of fragment before closing
     * @param args               if any CalendarData transfer in form bundles
     *                           <p>
     *                           viewModel.setNavigator(this);
     */
    protected void onCreateFragment(Bundle savedInstanceState, Bundle args) {
    }

    protected abstract Class<V> setViewModel();

    @LayoutRes
    public abstract int getLayout();

    protected abstract void getBinding(B binding);

    protected abstract void getViewModel(V viewModel);

    protected abstract void init();

    public void showSimmerLoader(String pageLoader) {
        View view = null;
        /*if (pageLoader.equals(DEFAULT_LOADER)) {
            view = LayoutInflater.from(activity).inflate(R.layout.progress_dialog, null);
        } else if (pageLoader.equals(HOME_LOADER)) {
            view = LayoutInflater.from(activity).inflate(R.layout.simmer_loader_home_page, null);
        } else if (pageLoader.equals(YOU_STORY_LOADER)) {
            view = LayoutInflater.from(activity).inflate(R.layout.simmer_loader_you_page, null);
        } else if (pageLoader.equals(STORY_VIEW_LOADER)) {
            view = LayoutInflater.from(activity).inflate(R.layout.simmer_loader_story_view, null);
        } else if (pageLoader.equals(UPLOAD_LOADER)) {
            view = LayoutInflater.from(activity).inflate(R.layout.simmer_loader_upload, null);
        }*/

        if (pageLoader.equals(DEFAULT_LOADER)) {
            view = LayoutInflater.from(activity).inflate(R.layout.progress_dialog, null);
        } else if (pageLoader.equals(HOME_LOADER)) {
            view = LayoutInflater.from(activity).inflate(R.layout.progress_dialog, null);
        } else if (pageLoader.equals(YOU_STORY_LOADER)) {
            view = LayoutInflater.from(activity).inflate(R.layout.progress_dialog, null);
        } else if (pageLoader.equals(STORY_VIEW_LOADER)) {
            view = LayoutInflater.from(activity).inflate(R.layout.progress_dialog, null);
        } else if (pageLoader.equals(UPLOAD_LOADER)) {
            view = LayoutInflater.from(activity).inflate(R.layout.progress_dialog, null);
        }

        dialog.setContentView(view);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transparent)));
        dialog.show();
    }

    public void hideSimmerLoader() {
        if (dialog.isShowing()) {
            dialog.dismiss();
        } else {
            dialog.dismiss();
        }

    }

    public <C extends Activity> void startUploadActivity(Class<C> targetActivityClass, Bundle bundle) {
        Intent intent = new Intent(getContext(), targetActivityClass);
        intent.putExtra(Constants.KEY_DEFAULT_ACTIVITY_BUNDLE, bundle);
        intent.putExtras(bundle);
        startActivity(intent);
    }


}
