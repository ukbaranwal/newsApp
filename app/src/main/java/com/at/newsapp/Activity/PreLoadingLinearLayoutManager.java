package com.at.newsapp.Activity;

import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

public class PreLoadingLinearLayoutManager extends LinearLayoutManager {
    private int mPages = 5;
    private OrientationHelper mOrientationHelper;

    public PreLoadingLinearLayoutManager(final Context context) {
        super(context);
    }

    public PreLoadingLinearLayoutManager(final Context context, final int pages) {
        super(context);
        this.mPages = pages;
    }

    public PreLoadingLinearLayoutManager(final Context context, final int orientation, final boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    @Override
    public void setOrientation(final int orientation) {
        super.setOrientation(orientation);
        mOrientationHelper = null;
    }
    public void setPages(final int pages) {
        this.mPages = pages;
    }

    @Override
    protected int getExtraLayoutSpace(final RecyclerView.State state) {
        if (mOrientationHelper == null) {
            mOrientationHelper = OrientationHelper.createOrientationHelper(this, getOrientation());
        }
        return mOrientationHelper.getTotalSpace() * mPages;
    }
}