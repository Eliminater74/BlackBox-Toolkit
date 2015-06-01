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

package com.kunalkene1797.blackboxkit.fragments.information;

import android.os.Bundle;

import com.kunalkene1797.blackboxkit.R;
import com.kunalkene1797.blackboxkit.elements.CardViewItem;
import com.kunalkene1797.blackboxkit.elements.UsageCardView;
import com.kunalkene1797.blackboxkit.fragments.RecyclerViewFragment;
import com.kunalkene1797.blackboxkit.fragments.kernel.CPUFragment;
import com.kunalkene1797.blackboxkit.utils.Utils;
import com.kunalkene1797.blackboxkit.utils.kernel.CPU;
import com.kunalkene1797.blackboxkit.utils.kernel.Info;

/**
 * Created by willi on 20.12.14.
 */
public class KernelInformationFragment extends RecyclerViewFragment {
    private CardViewItem.DCardView mTempCard;
    private static CPUFragment cpuFragment;
    private static CPUFragment.GovernorPart governorPart;

    private final String UPDATE_SOURCE = "http://kunalkene1797.in/downloads/";

    @Override
    public boolean showApplyOnBoot() {
        return false;
    }

    @Override

    public void init(Bundle savedInstanceState) {


        super.init(savedInstanceState);
        if (CPU.hasTemp()) tempInit();

        CardViewItem.DCardView kernelVersionCard = new CardViewItem.DCardView();
        kernelVersionCard.setTitle(getString(R.string.kernel_version));
        kernelVersionCard.setDescription(Info.getKernelVersion());

        CardViewItem.DCardView deviceNameCard = new CardViewItem.DCardView();
        deviceNameCard.setTitle(getString(R.string.device_name));
        deviceNameCard.setDescription(Info.getDeviceName());

        CardViewItem.DCardView kernelUpdateCard = new CardViewItem.DCardView();
        kernelUpdateCard.setTitle(getString(R.string.update_version));
        kernelUpdateCard.setDescription(getString(R.string.update_version_information));
        kernelUpdateCard.setOnDCardListener(new CardViewItem.DCardView.OnDCardListener() {
            @Override
            public void onClick(CardViewItem.DCardView dCardView) {
                Utils.launchUrl(getActivity(), UPDATE_SOURCE);
            }
        });

        CardViewItem.DCardView cpuCard = new CardViewItem.DCardView();
        cpuCard.setTitle(getString(R.string.cpu_information));
        cpuCard.setDescription(Info.getCpuInfo());

        CardViewItem.DCardView memCard = new CardViewItem.DCardView();
        memCard.setTitle(getString(R.string.memory_information));
        memCard.setDescription(Info.getMemInfo());

        addView(kernelVersionCard);
        addView(deviceNameCard);
        addView(mTempCard);
        addView(kernelUpdateCard);
        addView(cpuCard);
        addView(memCard);
    }

    private void tempInit() {
        mTempCard = new CardViewItem.DCardView();
        mTempCard.setTitle(getString(R.string.cpu_temp));
        mTempCard.setDescription(CPU.getTemp());

    }

}
