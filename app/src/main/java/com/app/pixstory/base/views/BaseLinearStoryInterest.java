package com.app.pixstory.base.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.app.pixstory.R;
import com.app.pixstory.data.model.story.StoryInterest;
import com.app.pixstory.utils.util.Dimensions;

import java.util.ArrayList;
import java.util.List;

public class BaseLinearStoryInterest extends LinearLayoutCompat {
    @LayoutRes
    private int layout;
    @LayoutRes
    private int layoutText;
    private float rightMargin = 5f;
    private List<StoryInterest> list;
    private BaseLinearStoryInterest.CallBack callback;
    private int viewSize = 70;

    private float topMargin = 10f;
    private float sideMargin = 10f;
    private View lastView;
    private View lastViewTmp;


    public interface CallBack{
        //        void onBaseCalendarIconClick(View view, BaseModelView model);
        void onBaseCalendarIconClick(View currentView, View lastView, StoryInterest model);
    }

    public void setListener(BaseLinearStoryInterest.CallBack callback){
        this.callback = callback;
    }

    public BaseLinearStoryInterest(Context context) {
        super(context);
        init(context, null);
        list = new ArrayList<>();
    }

    public BaseLinearStoryInterest(Context context, AttributeSet attrs) {
        super(context, attrs);
        getAttributes(attrs);
        init(context, attrs);
        list = new ArrayList<>();
    }

    public BaseLinearStoryInterest(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getAttributes(attrs);
        init(context, attrs);
        list = new ArrayList<>();
    }

    public void addItems(List<StoryInterest> model) {
        list.clear();
        list.addAll(model);
        onItemUpdates();
    }

    public void clearItems() {
        list.clear();
    }

    private void getAttributes(AttributeSet attrs) {
        TypedArray type = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.BaseLinearTextHorizontal,
                0, 0);

        try {
            int textBackground = type.getColor(R.styleable.BaseLinearTextHorizontal_TextBackground,
                    Color.parseColor("#FFFFFF"));
            int textColor = type.getColor(R.styleable.BaseLinearTextHorizontal_TextColor,
                    Color.parseColor("#FFFFFF"));
            layout = type.getResourceId(R.styleable.BaseLinearTextHorizontal_Layout, 0);
            layoutText = type.getResourceId(R.styleable.BaseLinearTextHorizontal_LayoutText,0);
            viewSize = (int) type.getDimension(R.styleable.BaseLinearTextHorizontal_ViewSize,0);
            rightMargin = type.getDimension(R.styleable.BaseLinearTextHorizontal_RightMargin,0);
        } finally {
            type.recycle();
        }
    }

    private void init(Context context, AttributeSet attrs) {

    }


    private void onItemUpdates() {
        LinearLayoutCompat mainView;
        LinearLayoutCompat.LayoutParams params;
        TextView view;
        for (int i = 0; i < list.size(); i++) {
            mainView = (LinearLayoutCompat)
                    LayoutInflater.from(getContext()).inflate(layout, null);

            params = new LayoutParams(LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);
            params.setMargins(10, 10, 10, 10);
            mainView.setLayoutParams(params);

            boolean ok = true;
            View lastView = null;
            float viewWidth = 0;
            while (ok && i < list.size()) {
                view = (TextView) LayoutInflater.from(getContext()).inflate(layoutText, null);
                view.setText(list.get(i).getTitle());
                int finalI = i;

                if(i == 0 && callback != null){
                    this.lastView = view;
                    lastViewTmp = view;
//                    callback.onBaseCalendarIconClick(view, this.lastView, list.get(finalI));
                }
                view.setOnClickListener(v -> {
                    if (callback != null) {
                        if(this.lastView == null){
                            this.lastView = v;
                        }
                        else{
                            this.lastView = lastViewTmp;
                        }
                        lastViewTmp = v;
                        callback.onBaseCalendarIconClick(v, this.lastView, list.get(finalI));
                    }
                });

                params = new LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.WRAP_CONTENT,
                        LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
                params.setMargins(0, 0, (int) rightMargin, 0);
                view.setLayoutParams(params);

                if (lastView == null) {
                    lastView = view;
                    mainView.addView(view);
                    viewWidth = Dimensions.getTextViewWeight(view);
                    i++;
                } else {
                    float textSize = Dimensions.getTextViewWeight(view);
                    viewWidth = viewWidth + textSize + sideMargin + sideMargin;
                    int screenWidth = Dimensions.getScreenWidth(getContext());

                    if ((screenWidth - viewWidth) > 0) {
                        mainView.addView(view);
                        lastView = view;
                        i++;
                    }else if((screenWidth - (viewWidth - rightMargin)) > 0){
                        params = new LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.WRAP_CONTENT,
                                LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
                        params.setMargins(0, 0, 0, 0);
                        view.setLayoutParams(params);
                        mainView.addView(view);

                        ok = false;
//                        i++;
                    }
                    else {
                        ok = false;
                        i--;
                    }
                }
            }
            this.addView(mainView);
        }
    }

    private void onItemUpdates2() {
//        final int textSize = 70;
        int screenWidth = Dimensions.getScreenWidth(getContext());
        for(int i=0; i < list.size(); i++){
            LinearLayoutCompat mainView = (LinearLayoutCompat)
                    LayoutInflater.from(getContext()).inflate(layout,null);

            LayoutParams params =
                    new LayoutParams(LayoutParams.WRAP_CONTENT,
                            LayoutParams.WRAP_CONTENT);
            params.setMargins(0,20, 0,0);
            mainView.setLayoutParams(params);

            boolean ok = true;
            View lastView = null;
            float viewWidth = 0;
            while(ok && i < list.size()) {
                TextView view = (TextView) LayoutInflater.from(getContext()).inflate(layoutText, null);
                view.setText(list.get(i).getTitle());

//                params = new LayoutParams(viewSize, viewSize);
                params.setMargins(0,0, (int) rightMargin,0);
                view.setLayoutParams(params);

                int finalI = i;
                view.setOnClickListener(v -> {
                    if(callback != null) {
//                        callback.onBaseCalendarIconClick(view, list.get(finalI));
                    }
                });

                if(lastView == null){
                    lastView = view;
                    mainView.addView(view);
                    viewWidth = Dimensions.getTextViewWeight(view);// + rightMargin;
                    i++;
                }
                else {
                    float textSize = Dimensions.getTextViewWeight(view);
                    viewWidth = viewWidth + textSize + rightMargin;
//                    int screenWidth = Dimensions.getScreenWidth(getContext());
//                    viewWidth = viewWidth + viewSize;

//                    Logger.e(" i =" + i);
//                    Logger.e(" viewWidth =" + viewWidth);
//                    Logger.e(" screenWidth =" + screenWidth);
//                    Logger.e(" rightMargin =" + rightMargin);
//                    Logger.e(" (screenWidth - viewWidth) =" + (screenWidth - viewWidth));

                    if (viewWidth < screenWidth && ((screenWidth - viewWidth) > rightMargin)) {
                        mainView.addView(view);
                        lastView = view;
                        i++;
                    } else {
                        ok = false;
                        i--;
                    }
                }
            }
            this.addView(mainView);
        }
    }
}

