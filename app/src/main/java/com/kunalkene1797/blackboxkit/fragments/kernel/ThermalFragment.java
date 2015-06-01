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

package com.kunalkene1797.blackboxkit.fragments.kernel;

import android.os.Bundle;
import android.view.View;

import com.kunalkene1797.blackboxkit.R;
import com.kunalkene1797.blackboxkit.elements.DAdapter;
import com.kunalkene1797.blackboxkit.elements.DividerCardView;
import com.kunalkene1797.blackboxkit.elements.InformationCardView;
import com.kunalkene1797.blackboxkit.elements.PopupCardItem;
import com.kunalkene1797.blackboxkit.elements.SeekBarCardView;
import com.kunalkene1797.blackboxkit.elements.SwitchCardView;
import com.kunalkene1797.blackboxkit.fragments.RecyclerViewFragment;
import com.kunalkene1797.blackboxkit.utils.Utils;
import com.kunalkene1797.blackboxkit.utils.kernel.CPU;
import com.kunalkene1797.blackboxkit.utils.kernel.Thermal;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by willi on 03.05.15.
 */
public class ThermalFragment extends RecyclerViewFragment implements SwitchCardView.DSwitchCard.OnDSwitchCardListener,
        SeekBarCardView.DSeekBarCardView.OnDSeekBarCardListener, PopupCardItem.DPopupCard.OnDPopupCardListener {

    private SwitchCardView.DSwitchCard mThermaldCard;

    private SwitchCardView.DSwitchCard mIntelliThermalEnableCard;
    private SwitchCardView.DSwitchCard mIntelliThermalOptimizedEnableCard;
    private SwitchCardView.DSwitchCard mThermalDebugModeCard;
    private SwitchCardView.DSwitchCard mCoreControlEnableCard;
    private SwitchCardView.DSwitchCard mVddRestrictionEnableCard;
    private SeekBarCardView.DSeekBarCardView mLimitTempDegCCard;
    private SeekBarCardView.DSeekBarCardView mCoreLimitTempDegCCard;
    private SeekBarCardView.DSeekBarCardView mCoreTempHysteresisDegCCard;
    private SeekBarCardView.DSeekBarCardView mFreqStepCard;
    private SwitchCardView.DSwitchCard mImmediatelyLimitStopCard;
    private SeekBarCardView.DSeekBarCardView mPollMsCard;
    private SeekBarCardView.DSeekBarCardView mTempHysteresisDegCCard;
    private SeekBarCardView.DSeekBarCardView mThermalLimitLowCard;
    private SeekBarCardView.DSeekBarCardView mThermalLimitHighCard;
    private SwitchCardView.DSwitchCard mTempSafetyCard;
    private SwitchCardView.DSwitchCard mTempThrottleEnableCard;
    private SeekBarCardView.DSeekBarCardView mTempLimitCard;
    private SwitchCardView.DSwitchCard mFreqLimitDebugCard;
    private PopupCardItem.DPopupCard mMinFreqIndexCard;

    private SeekBarCardView.DSeekBarCardView mAllowedLowLowCard;
    private SeekBarCardView.DSeekBarCardView mAllowedLowHighCard;
    private PopupCardItem.DPopupCard mAllowedLowFreqCard;
    private SeekBarCardView.DSeekBarCardView mAllowedMidLowCard;
    private SeekBarCardView.DSeekBarCardView mAllowedMidHighCard;
    private PopupCardItem.DPopupCard mAllowedMidFreqCard;
    private SeekBarCardView.DSeekBarCardView mAllowedMaxLowCard;
    private SeekBarCardView.DSeekBarCardView mAllowedMaxHighCard;
    private PopupCardItem.DPopupCard mAllowedMaxFreqCard;
    private SeekBarCardView.DSeekBarCardView mCheckIntervalMsCard;
    private SeekBarCardView.DSeekBarCardView mShutdownFreqCard;

    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);

        if (!Utils.getBoolean("hideinfocardthermal", false, getActivity())) {
            final InformationCardView.DInformationCard mInformationCard = new InformationCardView.DInformationCard();
            mInformationCard.setText(getString(R.string.thermal_info));
            mInformationCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO: Recyclerview is bugged
                    //removeView(mInformationCard);
                    Utils.saveBoolean("hideinfocardthermal", true, getActivity());
                }
            });

            addView(mInformationCard);
        }

        if (Thermal.hasThermald()) thermaldInit();
        if (Thermal.hasThermalSettings()) thermalInit();
        if (Thermal.hasMsmThermal()) msmThermalInit();
    }

    private void thermaldInit() {
        mThermaldCard = new SwitchCardView.DSwitchCard();
        mThermaldCard.setTitle(getString(R.string.thermald));
        mThermaldCard.setDescription(getString(R.string.thermald_summary));
        mThermaldCard.setChecked(Thermal.isThermaldActive());
        mThermaldCard.setOnDSwitchCardListener(this);

        addView(mThermaldCard);
    }

    private void thermalInit() {
        if (Thermal.hasIntelliThermalEnable()) {
            mIntelliThermalEnableCard = new SwitchCardView.DSwitchCard();
            mIntelliThermalEnableCard.setTitle(getString(R.string.intellithermal));
            mIntelliThermalEnableCard.setDescription(getString(R.string.intellithermal_summary));
            mIntelliThermalEnableCard.setChecked(Thermal.isIntelliThermalActive());
            mIntelliThermalEnableCard.setOnDSwitchCardListener(this);

            addView(mIntelliThermalEnableCard);
        }

        if (Thermal.hasIntelliThermalOptimizedEnable()) {
            mIntelliThermalOptimizedEnableCard = new SwitchCardView.DSwitchCard();
            mIntelliThermalOptimizedEnableCard.setTitle(getString(R.string.intellithermal_optimized));
            mIntelliThermalOptimizedEnableCard.setDescription(getString(R.string.intellithermal_optimized_summary));
            mIntelliThermalOptimizedEnableCard.setChecked(Thermal.isIntelliThermalOptimizedActive());
            mIntelliThermalOptimizedEnableCard.setOnDSwitchCardListener(this);

            addView(mIntelliThermalOptimizedEnableCard);
        }

        if (Thermal.hasThermalDebugMode()) {
            mThermalDebugModeCard = new SwitchCardView.DSwitchCard();
            mThermalDebugModeCard.setTitle(getString(R.string.debug_mask));
            mThermalDebugModeCard.setDescription(getString(R.string.debug_mask_thermal));
            mThermalDebugModeCard.setChecked(Thermal.isThermalDebugModeActive());
            mThermalDebugModeCard.setOnDSwitchCardListener(this);

            addView(mThermalDebugModeCard);
        }

        if (Thermal.hasCoreControlEnable()) {
            mCoreControlEnableCard = new SwitchCardView.DSwitchCard();
            mCoreControlEnableCard.setDescription(getString(R.string.core_control));
            mCoreControlEnableCard.setChecked(Thermal.isCoreControlActive());
            mCoreControlEnableCard.setOnDSwitchCardListener(this);

            addView(mCoreControlEnableCard);
        }

        if (Thermal.hasVddRestrictionEnable()) {
            mVddRestrictionEnableCard = new SwitchCardView.DSwitchCard();
            mVddRestrictionEnableCard.setDescription(getString(R.string.vdd_restriction));
            mVddRestrictionEnableCard.setChecked(Thermal.isVddRestrictionActive());
            mVddRestrictionEnableCard.setOnDSwitchCardListener(this);

            addView(mVddRestrictionEnableCard);
        }

        if (Thermal.hasLimitTempDegC()) {
            List<String> list = new ArrayList<>();
            for (double i = 50; i < 101; i++)
                list.add(i + "°C " + Utils.celsiusToFahrenheit(i) + "°F");

            mLimitTempDegCCard = new SeekBarCardView.DSeekBarCardView(list);
            mLimitTempDegCCard.setTitle(getString(R.string.freq_throttle_temp));
            mLimitTempDegCCard.setDescription(getString(R.string.freq_throttle_temp_summary));
            mLimitTempDegCCard.setProgress(Thermal.getLimitTempDegC() - 50);
            mLimitTempDegCCard.setOnDSeekBarCardListener(this);

            addView(mLimitTempDegCCard);
        }

        if (Thermal.hasCoreLimitTempDegC()) {
            List<String> list = new ArrayList<>();
            for (double i = 50; i < 101; i++)
                list.add(i + "°C " + Utils.celsiusToFahrenheit(i) + "°F");

            mCoreLimitTempDegCCard = new SeekBarCardView.DSeekBarCardView(list);
            mCoreLimitTempDegCCard.setTitle(getString(R.string.core_throttle_temp));
            mCoreLimitTempDegCCard.setDescription(getString(R.string.core_throttle_temp_summary));
            mCoreLimitTempDegCCard.setProgress(Thermal.getCoreLimitTempDegC() - 50);
            mCoreLimitTempDegCCard.setOnDSeekBarCardListener(this);

            addView(mCoreLimitTempDegCCard);
        }

        if (Thermal.hasCoreTempHysteresisDegC()) {
            List<String> list = new ArrayList<>();
            for (double i = 0; i < 21; i++)
                list.add(i + "°C " + Utils.celsiusToFahrenheit(i) + "°F");

            mCoreTempHysteresisDegCCard = new SeekBarCardView.DSeekBarCardView(list);
            mCoreTempHysteresisDegCCard.setTitle(getString(R.string.core_temp_hysteresis));
            mCoreTempHysteresisDegCCard.setProgress(Thermal.getCoreTempHysteresisDegC());
            mCoreTempHysteresisDegCCard.setOnDSeekBarCardListener(this);

            addView(mCoreTempHysteresisDegCCard);
        }

        if (Thermal.hasFreqStep()) {
            List<String> list = new ArrayList<>();
            for (int i = 1; i < 11; i++) list.add(String.valueOf(i));

            mFreqStepCard = new SeekBarCardView.DSeekBarCardView(list);
            mFreqStepCard.setTitle(getString(R.string.freq_step));
            mFreqStepCard.setProgress(Thermal.getFreqStep() - 1);
            mFreqStepCard.setOnDSeekBarCardListener(this);

            addView(mFreqStepCard);
        }

        if (Thermal.hasImmediatelyLimitStop()) {
            mImmediatelyLimitStopCard = new SwitchCardView.DSwitchCard();
            mImmediatelyLimitStopCard.setDescription(getString(R.string.immediately_limit_stop));
            mImmediatelyLimitStopCard.setChecked(Thermal.isImmediatelyLimitStopActive());
            mImmediatelyLimitStopCard.setOnDSwitchCardListener(this);

            addView(mImmediatelyLimitStopCard);
        }

        if (Thermal.hasPollMs()) {
            List<String> list = new ArrayList<>();
            for (int i = 0; i < 301; i++) list.add((i * 10) + getString(R.string.ms));

            mPollMsCard = new SeekBarCardView.DSeekBarCardView(list);
            mPollMsCard.setTitle(getString(R.string.poll));
            mPollMsCard.setProgress(Thermal.getPollMs() / 10);
            mPollMsCard.setOnDSeekBarCardListener(this);

            addView(mPollMsCard);
        }

        if (Thermal.hasTempHysteresisDegC()) {
            List<String> list = new ArrayList<>();
            for (double i = 0; i < 21; i++)
                list.add(i + "°C " + Utils.celsiusToFahrenheit(i) + "°F");

            mTempHysteresisDegCCard = new SeekBarCardView.DSeekBarCardView(list);
            mTempHysteresisDegCCard.setTitle(getString(R.string.temp_hysteresis));
            mTempHysteresisDegCCard.setProgress(Thermal.getTempHysteresisDegC());
            mTempHysteresisDegCCard.setOnDSeekBarCardListener(this);

            addView(mTempHysteresisDegCCard);
        }

        if (Thermal.hasThermalLimitLow()) {
            List<String> list = new ArrayList<>();
            for (int i = 1; i < 31; i++) list.add(String.valueOf(i));

            mThermalLimitLowCard = new SeekBarCardView.DSeekBarCardView(list);
            mThermalLimitLowCard.setTitle(getString(R.string.thermal_limit_low));
            mThermalLimitLowCard.setProgress(Thermal.getThermalLimitLow() - 1);
            mThermalLimitLowCard.setOnDSeekBarCardListener(this);

            addView(mThermalLimitLowCard);
        }

        if (Thermal.hasThermalLimitHigh()) {
            List<String> list = new ArrayList<>();
            for (int i = 1; i < 31; i++) list.add(String.valueOf(i));

            mThermalLimitHighCard = new SeekBarCardView.DSeekBarCardView(list);
            mThermalLimitHighCard.setTitle(getString(R.string.thermal_limit_high));
            mThermalLimitHighCard.setProgress(Thermal.getThermalLimitHigh() - 1);
            mThermalLimitHighCard.setOnDSeekBarCardListener(this);

            addView(mThermalLimitHighCard);
        }

        if (Thermal.hasTempSafety()) {
            mTempSafetyCard = new SwitchCardView.DSwitchCard();
            mTempSafetyCard.setDescription(getString(R.string.temp_safety));
            mTempSafetyCard.setChecked(Thermal.isTempSafetyActive());
            mTempSafetyCard.setOnDSwitchCardListener(this);

            addView(mTempSafetyCard);
        }

        if (Thermal.hasTempThrottleEnable()) {
            mTempThrottleEnableCard = new SwitchCardView.DSwitchCard();
            mTempThrottleEnableCard.setTitle(getString(R.string.temp_throttle));
            mTempThrottleEnableCard.setDescription(getString(R.string.temp_throttle_summary));
            mTempThrottleEnableCard.setChecked(Thermal.isTempThrottleActive());
            mTempThrottleEnableCard.setOnDSwitchCardListener(this);

            addView(mTempThrottleEnableCard);
        }

        if (Thermal.hasTempLimit()) {
            mTempLimitCard = new SeekBarCardView.DSeekBarCardView(Thermal.getTempLimitList());
            mTempLimitCard.setTitle(getString(R.string.temp_limit));
            mTempLimitCard.setDescription(getString(R.string.temp_limit_summary));
            mTempLimitCard.setProgress(Thermal.getCurTempLimit() - Thermal.getTempLimitMin());
            mTempLimitCard.setOnDSeekBarCardListener(this);

            addView(mTempLimitCard);
        }

        if (Thermal.hasFreqLimitDebug()) {
            mFreqLimitDebugCard = new SwitchCardView.DSwitchCard();
            mFreqLimitDebugCard.setTitle(getString(R.string.freq_limit_debug));
            mFreqLimitDebugCard.setDescription(getString(R.string.freq_limit_debug_summary));
            mFreqLimitDebugCard.setChecked(Thermal.isFreqLimitDebugActive());
            mFreqLimitDebugCard.setOnDSwitchCardListener(this);

            addView(mFreqLimitDebugCard);
        }

        if (Thermal.hasMinFreqIndex() && CPU.getFreqs() != null) {
            List<String> list = new ArrayList<>();
            for (int freq : CPU.getFreqs()) list.add((freq / 1000) + getString(R.string.mhz));

            mMinFreqIndexCard = new PopupCardItem.DPopupCard(list);
            mMinFreqIndexCard.setTitle(getString(R.string.temp_limit_min_freq));
            mMinFreqIndexCard.setDescription(getString(R.string.temp_limit_min_freq_summary));
            mMinFreqIndexCard.setItem((Thermal.getMinFreqIndex() / 1000) + getString(R.string.mhz));
            mMinFreqIndexCard.setOnDPopupCardListener(this);

            addView(mMinFreqIndexCard);
        }

    }

    private void msmThermalInit() {
        List<DAdapter.DView> views = new ArrayList<>();

        if (Thermal.hasAllowedLowLow()) {
            List<String> list = new ArrayList<>();
            for (double i = 40; i < 101; i++)
                list.add(i + "°C " + Utils.celsiusToFahrenheit(i) + "°F");

            mAllowedLowLowCard = new SeekBarCardView.DSeekBarCardView(list);
            mAllowedLowLowCard.setTitle(getString(R.string.allowed_low_low));
            mAllowedLowLowCard.setProgress(Thermal.getAllowedLowLow() - 40);
            mAllowedLowLowCard.setOnDSeekBarCardListener(this);

            views.add(mAllowedLowLowCard);
        }

        if (Thermal.hasAllowedLowHigh()) {
            List<String> list = new ArrayList<>();
            for (double i = 40; i < 101; i++)
                list.add(i + "°C " + Utils.celsiusToFahrenheit(i) + "°F");

            mAllowedLowHighCard = new SeekBarCardView.DSeekBarCardView(list);
            mAllowedLowHighCard.setTitle(getString(R.string.allowed_low_high));
            mAllowedLowHighCard.setProgress(Thermal.getAllowedLowHigh() - 40);
            mAllowedLowHighCard.setOnDSeekBarCardListener(this);

            views.add(mAllowedLowHighCard);
        }

        if (Thermal.hasAllowedLowFreq() && CPU.getFreqs() != null) {
            List<String> list = new ArrayList<>();
            for (int freq : CPU.getFreqs()) list.add((freq / 1000) + getString(R.string.mhz));

            mAllowedLowFreqCard = new PopupCardItem.DPopupCard(list);
            mAllowedLowFreqCard.setTitle(getString(R.string.allowed_low_freq));
            mAllowedLowFreqCard.setItem((Thermal.getAllowedLowFreq() / 1000) + getString(R.string.mhz));
            mAllowedLowFreqCard.setOnDPopupCardListener(this);

            views.add(mAllowedLowFreqCard);
        }

        if (Thermal.hasAllowedMidLow()) {
            List<String> list = new ArrayList<>();
            for (double i = 40; i < 101; i++)
                list.add(i + "°C " + Utils.celsiusToFahrenheit(i) + "°F");

            mAllowedMidLowCard = new SeekBarCardView.DSeekBarCardView(list);
            mAllowedMidLowCard.setTitle(getString(R.string.allowed_mid_low));
            mAllowedMidLowCard.setProgress(Thermal.getAllowedMidLow() - 40);
            mAllowedMidLowCard.setOnDSeekBarCardListener(this);

            views.add(mAllowedMidLowCard);
        }

        if (Thermal.hasAllowedMidHigh()) {
            List<String> list = new ArrayList<>();
            for (double i = 40; i < 101; i++)
                list.add(i + "°C " + Utils.celsiusToFahrenheit(i) + "°F");

            mAllowedMidHighCard = new SeekBarCardView.DSeekBarCardView(list);
            mAllowedMidHighCard.setTitle(getString(R.string.allowed_mid_high));
            mAllowedMidHighCard.setProgress(Thermal.getAllowedMidHigh() - 40);
            mAllowedMidHighCard.setOnDSeekBarCardListener(this);

            views.add(mAllowedMidHighCard);
        }

        if (Thermal.hasAllowedMidFreq() && CPU.getFreqs() != null) {
            List<String> list = new ArrayList<>();
            for (int freq : CPU.getFreqs()) list.add((freq / 1000) + getString(R.string.mhz));

            mAllowedMidFreqCard = new PopupCardItem.DPopupCard(list);
            mAllowedMidFreqCard.setTitle(getString(R.string.allowed_mid_freq));
            mAllowedMidFreqCard.setItem((Thermal.getAllowedMidFreq() / 1000) + getString(R.string.mhz));
            mAllowedMidFreqCard.setOnDPopupCardListener(this);

            views.add(mAllowedMidFreqCard);
        }

        if (Thermal.hasAllowedMaxLow()) {
            List<String> list = new ArrayList<>();
            for (double i = 40; i < 101; i++)
                list.add(i + "°C " + Utils.celsiusToFahrenheit(i) + "°F");

            mAllowedMaxLowCard = new SeekBarCardView.DSeekBarCardView(list);
            mAllowedMaxLowCard.setTitle(getString(R.string.allowed_max_low));
            mAllowedMaxLowCard.setProgress(Thermal.getAllowedMaxLow() - 40);
            mAllowedMaxLowCard.setOnDSeekBarCardListener(this);

            views.add(mAllowedMaxLowCard);
        }

        if (Thermal.hasAllowedMaxHigh()) {
            List<String> list = new ArrayList<>();
            for (double i = 40; i < 101; i++)
                list.add(i + "°C " + Utils.celsiusToFahrenheit(i) + "°F");

            mAllowedMaxHighCard = new SeekBarCardView.DSeekBarCardView(list);
            mAllowedMaxHighCard.setTitle(getString(R.string.allowed_max_high));
            mAllowedMaxHighCard.setProgress(Thermal.getAllowedMaxHigh() - 40);
            mAllowedMaxHighCard.setOnDSeekBarCardListener(this);

            views.add(mAllowedMaxHighCard);
        }

        if (Thermal.hasAllowedMaxFreq() && CPU.getFreqs() != null) {
            List<String> list = new ArrayList<>();
            for (int freq : CPU.getFreqs()) list.add((freq / 1000) + getString(R.string.mhz));

            mAllowedMaxFreqCard = new PopupCardItem.DPopupCard(list);
            mAllowedMaxFreqCard.setTitle(getString(R.string.allowed_max_freq));
            mAllowedMaxFreqCard.setItem((Thermal.getAllowedMaxFreq() / 1000) + getString(R.string.mhz));
            mAllowedMaxFreqCard.setOnDPopupCardListener(this);

            views.add(mAllowedMaxFreqCard);
        }

        if (Thermal.hasCheckIntervalMs()) {
            List<String> list = new ArrayList<>();
            for (int i = 0; i < 61; i++) list.add((i * 50) + getString(R.string.ms));

            mCheckIntervalMsCard = new SeekBarCardView.DSeekBarCardView(list);
            mCheckIntervalMsCard.setTitle(getString(R.string.check_interval));
            mCheckIntervalMsCard.setProgress(Thermal.getCheckIntervalMs() / 50);
            mCheckIntervalMsCard.setOnDSeekBarCardListener(this);

            views.add(mCheckIntervalMsCard);
        }

        if (Thermal.hasShutdownTemp()) {
            List<String> list = new ArrayList<>();
            for (double i = 40; i < 101; i++)
                list.add(i + "°C " + Utils.celsiusToFahrenheit(i) + "°F");

            mShutdownFreqCard = new SeekBarCardView.DSeekBarCardView(list);
            mShutdownFreqCard.setTitle(getString(R.string.shutdown_temp));
            mShutdownFreqCard.setProgress(Thermal.getShutdownTemp() - 40);
            mShutdownFreqCard.setOnDSeekBarCardListener(this);

            views.add(mShutdownFreqCard);
        }

        if (views.size() > 0) {
            DividerCardView.DDividerCard mMsmThermalDividerCard = new DividerCardView.DDividerCard();
            mMsmThermalDividerCard.setText(getString(R.string.msm_thermal));
            mMsmThermalDividerCard.setDescription(getString(R.string.msm_thermal_summary));
            addView(mMsmThermalDividerCard);

            addAllViews(views);
        }
    }

    @Override
    public void onChecked(SwitchCardView.DSwitchCard dSwitchCard, boolean checked) {
        if (dSwitchCard == mThermaldCard) Thermal.activateThermald(checked, getActivity());
        else if (dSwitchCard == mIntelliThermalEnableCard)
            Thermal.activateIntelliThermal(checked, getActivity());
        else if (dSwitchCard == mIntelliThermalOptimizedEnableCard)
            Thermal.activateIntelliThermalOptimized(checked, getActivity());
        else if (dSwitchCard == mThermalDebugModeCard)
            Thermal.activateThermalDebugMode(checked, getActivity());
        else if (dSwitchCard == mCoreControlEnableCard)
            Thermal.activateCoreControl(checked, getActivity());
        else if (dSwitchCard == mVddRestrictionEnableCard)
            Thermal.activateVddRestriction(checked, getActivity());
        else if (dSwitchCard == mImmediatelyLimitStopCard)
            Thermal.activateImmediatelyLimitStop(checked, getActivity());
        else if (dSwitchCard == mTempSafetyCard) Thermal.activateTempSafety(checked, getActivity());
        else if (dSwitchCard == mTempThrottleEnableCard)
            Thermal.activateTempThrottle(checked, getActivity());
        else if (dSwitchCard == mFreqLimitDebugCard)
            Thermal.activateFreqLimitDebug(checked, getActivity());
    }

    @Override
    public void onChanged(SeekBarCardView.DSeekBarCardView dSeekBarCardView, int position) {
    }

    @Override
    public void onStop(SeekBarCardView.DSeekBarCardView dSeekBarCardView, int position) {
        if (dSeekBarCardView == mLimitTempDegCCard)
            Thermal.setLimitTempDegC(position + 50, getActivity());
        else if (dSeekBarCardView == mCoreLimitTempDegCCard)
            Thermal.setCoreLimitTempDegC(position + 50, getActivity());
        else if (dSeekBarCardView == mCoreTempHysteresisDegCCard)
            Thermal.setCoreTempHysteresisDegC(position, getActivity());
        else if (dSeekBarCardView == mFreqStepCard)
            Thermal.setFreqStep(position + 1, getActivity());
        else if (dSeekBarCardView == mPollMsCard) Thermal.setPollMs(position * 10, getActivity());
        else if (dSeekBarCardView == mTempHysteresisDegCCard)
            Thermal.setTempHysteresisDegC(position, getActivity());
        else if (dSeekBarCardView == mThermalLimitLowCard)
            Thermal.setThermalLimitLow(position + 1, getActivity());
        else if (dSeekBarCardView == mThermalLimitHighCard)
            Thermal.setThermalLimitHigh(position + 1, getActivity());
        else if (dSeekBarCardView == mTempLimitCard)
            Thermal.setTempLimit(position + 50, getActivity());
        else if (dSeekBarCardView == mAllowedLowLowCard)
            Thermal.setAllowedLowLow(position + 40, getActivity());
        else if (dSeekBarCardView == mAllowedLowHighCard)
            Thermal.setAllowedLowHigh(position + 40, getActivity());
        else if (dSeekBarCardView == mAllowedMidLowCard)
            Thermal.setAllowedMidLow(position + 40, getActivity());
        else if (dSeekBarCardView == mAllowedMidHighCard)
            Thermal.setAllowedMidHigh(position + 40, getActivity());
        else if (dSeekBarCardView == mAllowedMaxLowCard)
            Thermal.setAllowedMaxLow(position + 40, getActivity());
        else if (dSeekBarCardView == mAllowedMaxHighCard)
            Thermal.setAllowedMaxHigh(position + 40, getActivity());
        else if (dSeekBarCardView == mCheckIntervalMsCard)
            Thermal.setCheckIntervalMs(position * 50, getActivity());
        else if (dSeekBarCardView == mShutdownFreqCard)
            Thermal.setShutdownTemp(position + 40, getActivity());
    }

    @Override
    public void onItemSelected(PopupCardItem.DPopupCard dPopupCard, int position) {
        if (dPopupCard == mMinFreqIndexCard)
            Thermal.setMinFreqIndex(CPU.getFreqs().get(position), getActivity());
        else if (dPopupCard == mAllowedLowFreqCard)
            Thermal.setAllowedLowFreq(CPU.getFreqs().get(position), getActivity());
        else if (dPopupCard == mAllowedMidFreqCard)
            Thermal.setAllowedMidFreq(CPU.getFreqs().get(position), getActivity());
        else if (dPopupCard == mAllowedMaxFreqCard)
            Thermal.setAllowedMaxFreq(CPU.getFreqs().get(position), getActivity());
    }
}
