package com.ftg2021.effo2021V1.Apps.News.Scroll;

import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.ftg2021.effo2021V1.Apps.News.NewsMainActivity;

public class SnapHelperOneByOne extends LinearSnapHelper {

    @Override
    public int findTargetSnapPosition(RecyclerView.LayoutManager layoutManager, int velocityX, int velocityY) {

        if (!(layoutManager instanceof RecyclerView.SmoothScroller.ScrollVectorProvider)) {
            return RecyclerView.NO_POSITION;
        }

        final View currentView = findSnapView(layoutManager);

        if (currentView == null) {
            return RecyclerView.NO_POSITION;
        }

        LinearLayoutManager myLayoutManager = (LinearLayoutManager) layoutManager;

        int position1 = myLayoutManager.findFirstVisibleItemPosition();
        int position2 = myLayoutManager.findLastVisibleItemPosition();

        int currentPosition = layoutManager.getPosition(currentView);

        if (velocityY > 100) {
            currentPosition = position2;
        } else if (velocityY < 100) {
            currentPosition = position1;
        }

        if (currentPosition == RecyclerView.NO_POSITION) {
            return RecyclerView.NO_POSITION;
        }

        if (NewsMainActivity.appBar.getVisibility() == View.VISIBLE) {


            NewsMainActivity.appBar.setVisibility(View.GONE);
            NewsMainActivity.bottomNavigationView.setVisibility(View.GONE);
            NewsMainActivity.blackBoxBehindBottomNav.setVisibility(View.GONE);

            NewsMainActivity.visibility = false;
        }



        return currentPosition;
    }
}