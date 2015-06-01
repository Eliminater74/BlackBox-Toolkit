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

package com.kunalkene1797.blackboxkit.fragments.other;

import android.os.Bundle;
import android.view.View;

import com.kunalkene1797.blackboxkit.R;
import com.kunalkene1797.blackboxkit.elements.CardViewItem;
import com.kunalkene1797.blackboxkit.fragments.RecyclerViewFragment;
import com.kunalkene1797.blackboxkit.utils.Utils;

/**
 * Created by willi on 27.12.14.
 */
public class AboutusFragment extends RecyclerViewFragment {

    private final String APP_SOURCE = "https://github.com/KunalKene1797/BlackBox-Toolkit";
    private final String ISSUE_LINK = "http://kunalkene1797.in/";

    @Override
    public boolean showApplyOnBoot() {
        return false;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);

        dev();
        core_app();
        licenseInit();
        appSourceInit();
        featureRequestInit();

    }
    private void dev() {
        CardViewItem.DCardView mAppSourceCard = new CardViewItem.DCardView();
        mAppSourceCard.setTitle(getString(R.string.dev));
        mAppSourceCard.setDescription(getString(R.string.dev_summary));
        mAppSourceCard.setOnDCardListener(new CardViewItem.DCardView.OnDCardListener() {
            @Override
            public void onClick(CardViewItem.DCardView dCardView) {
                Utils.launchUrl(getActivity(), ISSUE_LINK);
            }
        });

        addView(mAppSourceCard);

    }
    private void core_app() {
        CardViewItem.DCardView mAppSourceCard = new CardViewItem.DCardView();
        mAppSourceCard.setTitle(getString(R.string.core_app));
        mAppSourceCard.setDescription(getString(R.string.core_app_summary));
        addView(mAppSourceCard);
    }

    private void licenseInit() {
        CardViewItem.DCardView mLicenseCard = new CardViewItem.DCardView();
        mLicenseCard.setTitle(getString(R.string.license));

        View view = inflater.inflate(R.layout.app_license_view, container, false);

        mLicenseCard.setView(view);
        addView(mLicenseCard);
    }

    private void appSourceInit() {
        CardViewItem.DCardView mAppSourceCard = new CardViewItem.DCardView();
        mAppSourceCard.setTitle(getString(R.string.open_source));
        mAppSourceCard.setDescription(getString(R.string.open_source_summary));
        mAppSourceCard.setOnDCardListener(new CardViewItem.DCardView.OnDCardListener() {
            @Override
            public void onClick(CardViewItem.DCardView dCardView) {
                Utils.launchUrl(getActivity(), APP_SOURCE);
            }
        });

        addView(mAppSourceCard);
    }

    private void featureRequestInit() {
        CardViewItem.DCardView mFeatureRequestCard = new CardViewItem.DCardView();
        mFeatureRequestCard.setTitle(getString(R.string.feature_request));
        mFeatureRequestCard.setDescription(getString(R.string.feature_request_summary));
        mFeatureRequestCard.setOnDCardListener(new CardViewItem.DCardView.OnDCardListener() {
            @Override
            public void onClick(CardViewItem.DCardView dCardView) {
                Utils.launchUrl(getActivity(), ISSUE_LINK);
            }
        });

        addView(mFeatureRequestCard);
    }



}
