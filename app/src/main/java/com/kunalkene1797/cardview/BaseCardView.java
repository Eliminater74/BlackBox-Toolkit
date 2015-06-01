/*
 * Copyright (C) 2015 Willi Ye
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kunalkene1797.cardview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.kunalkene1797.blackboxkit.R;
import com.kunalkene1797.blackboxkit.utils.Utils;

/**
 * Created by willi on 23.12.14.
 */
public abstract class BaseCardView extends CardView {

    /**
     * Default layout
     */
    private static final int DEFAULT_LAYOUT = R.layout.inner_cardview;

    /**
     * Views
     */
    protected View layoutView;

    private HeaderCardView headerCardView;
    private LinearLayout headerLayout;

    private AppCompatTextView innerView;
    private String mTitle;

    protected LinearLayout customLayout;
    private View customView;

    public BaseCardView(Context context) {
        this(context, DEFAULT_LAYOUT);
    }

    public BaseCardView(Context context, int layout) {
        this(context, null, layout);
    }

    public BaseCardView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, DEFAULT_LAYOUT);
    }

    public BaseCardView(Context context, AttributeSet attributeSet, int layout) {
        super(context, attributeSet);

        // Add a padding
        int padding = getResources().getDimensionPixelSize(R.dimen.basecard_padding);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        layoutParams.setMargins(padding, padding, padding, padding);
        setLayoutParams(layoutParams);

        // Make a rounded card
        setRadius(getResources().getDimensionPixelSize(R.dimen.basecard_radius));

        // Set background color depending on the current theme
        setCardBackgroundColor(getResources().getColor(Utils.DARKTHEME ?
                R.color.card_background_dark : R.color.card_background_light));

        // This will enable the touch feedback of the card
        TypedArray ta = getContext().obtainStyledAttributes(new int[]{R.attr.selectableItemBackground});
        Drawable d = ta.getDrawable(0);
        ta.recycle();
        setForeground(d);

        // Add the base view
        View view = LayoutInflater.from(getContext()).inflate(R.layout.base_cardview, null, false);
        addView(view);

        headerLayout = (LinearLayout) view.findViewById(R.id.header_layout);

        setUpHeader();

        LinearLayout innerLayout = (LinearLayout) view.findViewById(R.id.inner_layout);
        customLayout = (LinearLayout) view.findViewById(R.id.custom_layout);

        // Inflate the innerlayout
        layoutView = LayoutInflater.from(getContext()).inflate(layout, null, false);

        // If sub class overwrites the default layout then don't try to get the TextView
        if (layout == DEFAULT_LAYOUT) {
            innerView = (AppCompatTextView) layoutView.findViewById(R.id.inner_view);
            if (mTitle != null) innerView.setText(mTitle);
        } else setUpInnerLayout(layoutView);

        // Add innerlayout to base view
        innerLayout.addView(layoutView);
    }

    /**
     * Will get executed if sub class overwrites the default layout
     * to use a custom one
     *
     * @param view is the parent innerlayout of the custom layout
     */
    public abstract void setUpInnerLayout(View view);

    /**
     * Sets the string value of TextView in innerlayout
     *
     * @param mTitle new Text of the card
     */
    public void setText(String mTitle) {
        this.mTitle = mTitle;
        if (innerView != null) innerView.setText(mTitle);
    }

    /**
     * Replaces the innerlayout with a custom view
     *
     * @param view new View of the card
     */
    public void setView(View view) {
        customView = view;
        setUpCustomLayout();
    }

    /**
     * Add a header to the card
     *
     * @param headerCardView new Header of the card
     */
    public void addHeader(HeaderCardView headerCardView) {
        this.headerCardView = headerCardView;
        setUpHeader();
    }

    private void setUpHeader() {
        if (headerCardView != null && headerLayout != null) {
            headerLayout.removeAllViews();
            headerLayout.addView(headerCardView.getView());
            headerLayout.setVisibility(VISIBLE);
        }
    }

    public void removeHeader() {
        headerCardView = null;
        if (headerLayout != null) {
            headerLayout.removeAllViews();
            headerLayout.setVisibility(GONE);
        }
    }

    private void setUpCustomLayout() {
        if (customLayout != null && customView != null) {
            innerView.setVisibility(GONE);
            customLayout.setVisibility(VISIBLE);
            customLayout.removeAllViews();
            try {
                ((ViewGroup) customView.getParent()).removeView(customView);
            } catch (NullPointerException ignored) {
            }
            customLayout.addView(customView);
        }
    }

}