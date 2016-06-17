package com.example.neo.scorebardemo.view;

import android.animation.AnimatorSet;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.util.Pair;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.neo.scorebardemo.R;

import java.util.List;

/**
 * Created by neo on 16-3-4.
 * 分数条形统计图
 */
public class ScoreBarChart extends LinearLayout {

    private int mLineSpace = dp2px(16);

    // 动画集合
    private AnimatorSet animatorSet;

    public ScoreBarChart(Context context) {
        super(context);
        initView();
    }

    public ScoreBarChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        getAttrs(context, attrs, 0);
        initView();
    }

    public ScoreBarChart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getAttrs(context, attrs, defStyleAttr);
        initView();
    }

    private void getAttrs(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray typeArray = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.ScoreBarChart, defStyleAttr, 0);
        try {
            mLineSpace = typeArray.getDimensionPixelSize(R.styleable.ScoreBarChart_lineSpace, dp2px(16));
        } finally {
            typeArray.recycle();
        }
    }

    private void initView() {
        this.setOrientation(LinearLayout.VERTICAL);
    }

    public void setChartData(List<Pair<String, Integer>> data, int colorStyle, int scoreStyle) {
        if (this.getChildCount() > 0) updateChartData(data, colorStyle, scoreStyle);
        else addChartData(data, colorStyle, scoreStyle);
    }

    /**
     * 为图片添加数据
     *
     * @param data       图标数据，注意包名为 android.support.v4.util.Pair;
     * @param colorStyle
     * @param scoreStyle
     */
    public void addChartData(List<Pair<String, Integer>> data, int colorStyle, int scoreStyle) {
        if (data == null || data.size() <= 0) return;
        ScoreBarView[] barViews = new ScoreBarView[data.size()];
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 0, 0, mLineSpace);
        animatorSet = new AnimatorSet();
        AnimatorSet.Builder builder = null;
        for (int i = 0; i < data.size(); i++) {
            barViews[i] = new ScoreBarView(getContext());
            Pair<String, Integer> pair = data.get(i);
            barViews[i].setName(i + 1 + " ." + pair.first);
            barViews[i].setColorStyle(colorStyle);
            barViews[i].setScoreStyle(scoreStyle);
            this.addView(barViews[i], layoutParams);
            if (i == 0) {
                builder = animatorSet.play(barViews[i].createValueChangeAnimation(pair.second));
            } else if (builder != null) {
                builder.with(barViews[i].createValueChangeAnimation(pair.second));
            }
        }
    }

    /**
     * 不删除view，更新数据
     *
     * @param data
     * @param colorStyle
     * @param scoreStyle
     */
    public void updateChartData(List<Pair<String, Integer>> data, int colorStyle, int scoreStyle) {
        if (data == null || this.getChildCount() != data.size()) return;
        ScoreBarView[] barViews = new ScoreBarView[data.size()];
        animatorSet = new AnimatorSet();
        AnimatorSet.Builder builder = null;
        for (int i = 0; i < data.size(); i++) {
            barViews[i] = (ScoreBarView) this.getChildAt(i);
            Pair<String, Integer> pair = data.get(i);
            barViews[i].setName(i + 1 + " ." + pair.first);
            barViews[i].setColorStyle(colorStyle);
            barViews[i].setScoreStyle(scoreStyle);
            if (i == 0) {
                builder = animatorSet.play(barViews[i].createValueChangeAnimation(pair.second));
            } else if (builder != null) {
                builder.with(barViews[i].createValueChangeAnimation(pair.second));
            }
        }
    }

    public void startAnimator() {
        if (animatorSet != null) {
            animatorSet.cancel();
            animatorSet.start();
        }
    }

    public boolean IsAnimatorRunning() {
        if (animatorSet != null) {
            return animatorSet.isRunning();
        }
        return false;
    }

    private int dp2px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
