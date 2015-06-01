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

import com.kunalkene1797.blackboxkit.R;
import com.kunalkene1797.blackboxkit.elements.SeekBarCardView;
import com.kunalkene1797.blackboxkit.elements.SwitchCardView;
import com.kunalkene1797.blackboxkit.fragments.RecyclerViewFragment;
import com.kunalkene1797.blackboxkit.utils.kernel.Sound;

/**
 * Created by willi on 06.01.15.
 */
public class SoundFragment extends RecyclerViewFragment implements
        SwitchCardView.DSwitchCard.OnDSwitchCardListener,
        SeekBarCardView.DSeekBarCardView.OnDSeekBarCardListener {

    private SwitchCardView.DSwitchCard mSoundControlEnableCard;
    private SeekBarCardView.DSeekBarCardView mHeadphoneGainCard;
    private SeekBarCardView.DSeekBarCardView mHandsetMicrophoneGainCard;
    private SeekBarCardView.DSeekBarCardView mCamMicrophoneGainCard;
    private SeekBarCardView.DSeekBarCardView mSpeakerGainCard;
    private SeekBarCardView.DSeekBarCardView mHeadphonePowerAmpGainCard;
    private SeekBarCardView.DSeekBarCardView mMicrophoneGainCard;
    private SeekBarCardView.DSeekBarCardView mVolumeGainCard;

    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);

        if (Sound.hasSoundControlEnable()) soundControlEnableInit();
        if (Sound.hasHeadphoneGain()) headphoneGainInit();
        if (Sound.hasHandsetMicrophoneGain()) handsetMicrophoneGainInit();
        if (Sound.hasCamMicrophoneGain()) camMicrophoneGainInit();
        if (Sound.hasSpeakerGain()) speakerGainInit();
        if (Sound.hasHeadphonePowerAmpGain()) headphonePowerAmpGainInit();
        if (Sound.hasMicrophoneGain()) microphoneGainInit();
        if (Sound.hasVolumeGain()) volumeGainInit();
    }

    private void soundControlEnableInit() {
        mSoundControlEnableCard = new SwitchCardView.DSwitchCard();
        mSoundControlEnableCard.setDescription(getString(R.string.sound_control));
        mSoundControlEnableCard.setChecked(Sound.isSoundControlActive());
        mSoundControlEnableCard.setOnDSwitchCardListener(this);

        addView(mSoundControlEnableCard);
    }

    private void headphoneGainInit() {
        mHeadphoneGainCard = new SeekBarCardView.DSeekBarCardView(Sound.getHeadphoneGainLimits());
        mHeadphoneGainCard.setTitle(getString(R.string.headphone_gain));
        mHeadphoneGainCard.setProgress(Sound.getHeadphoneGainLimits().indexOf(Sound.getCurHeadphoneGain()));
        mHeadphoneGainCard.setOnDSeekBarCardListener(this);

        addView(mHeadphoneGainCard);
    }

    private void handsetMicrophoneGainInit() {
        mHandsetMicrophoneGainCard = new SeekBarCardView.DSeekBarCardView(Sound.getHandsetMicrophoneGainLimits());
        mHandsetMicrophoneGainCard.setTitle(getString(R.string.handset_microphone_gain));
        mHandsetMicrophoneGainCard.setProgress(Sound.getHandsetMicrophoneGainLimits().indexOf(
                Sound.getCurHandsetMicrophoneGain()));
        mHandsetMicrophoneGainCard.setOnDSeekBarCardListener(this);

        addView(mHandsetMicrophoneGainCard);
    }

    private void camMicrophoneGainInit() {
        mCamMicrophoneGainCard = new SeekBarCardView.DSeekBarCardView(Sound.getCamMicrophoneGainLimits());
        mCamMicrophoneGainCard.setTitle(getString(R.string.cam_microphone_gain));
        mCamMicrophoneGainCard.setProgress(Sound.getCamMicrophoneGainLimits().indexOf(Sound.getCurCamMicrophoneGain()));
        mCamMicrophoneGainCard.setOnDSeekBarCardListener(this);

        addView(mCamMicrophoneGainCard);
    }

    private void speakerGainInit() {
        mSpeakerGainCard = new SeekBarCardView.DSeekBarCardView(Sound.getSpeakerGainLimits());
        mSpeakerGainCard.setTitle(getString(R.string.speaker_gain));
        mSpeakerGainCard.setProgress(Sound.getSpeakerGainLimits().indexOf(Sound.getCurSpeakerGain()));
        mSpeakerGainCard.setOnDSeekBarCardListener(this);

        addView(mSpeakerGainCard);
    }

    private void headphonePowerAmpGainInit() {
        mHeadphonePowerAmpGainCard = new SeekBarCardView.DSeekBarCardView(Sound.getHeadphonePowerAmpGainLimits());
        mHeadphonePowerAmpGainCard.setTitle(getString(R.string.headphone_poweramp_gain));
        mHeadphonePowerAmpGainCard.setProgress(Sound.getHeadphonePowerAmpGainLimits().indexOf(
                Sound.getCurHeadphonePowerAmpGain()));
        mHeadphonePowerAmpGainCard.setOnDSeekBarCardListener(this);

        addView(mHeadphonePowerAmpGainCard);
    }

    private void microphoneGainInit() {
        mMicrophoneGainCard = new SeekBarCardView.DSeekBarCardView(Sound.getMicrophoneGainLimits());
        mMicrophoneGainCard.setTitle(getString(R.string.microphone_gain));
        mMicrophoneGainCard.setProgress(Sound.getMicrophoneGainLimits().indexOf(Sound.getMicrophoneGain()));
        mMicrophoneGainCard.setOnDSeekBarCardListener(this);

        addView(mMicrophoneGainCard);
    }

    private void volumeGainInit() {
        mVolumeGainCard = new SeekBarCardView.DSeekBarCardView(Sound.getVolumeGainLimits());
        mVolumeGainCard.setTitle(getString(R.string.volume_gain));
        mVolumeGainCard.setProgress(Sound.getVolumeGainLimits().indexOf(Sound.getVolumeGain()));
        mVolumeGainCard.setOnDSeekBarCardListener(this);

        addView(mVolumeGainCard);
    }

    @Override
    public void onChecked(SwitchCardView.DSwitchCard dSwitchCard, boolean checked) {
        if (dSwitchCard == mSoundControlEnableCard)
            Sound.activateSoundControl(checked, getActivity());
    }

    @Override
    public void onChanged(SeekBarCardView.DSeekBarCardView dSeekBarCardView, int position) {
    }

    @Override
    public void onStop(SeekBarCardView.DSeekBarCardView dSeekBarCardView, int position) {
        if (dSeekBarCardView == mHeadphoneGainCard)
            Sound.setHeadphoneGain(Sound.getHeadphoneGainLimits().get(position), getActivity());
        else if (dSeekBarCardView == mHandsetMicrophoneGainCard)
            Sound.setHandsetMicrophoneGain(Sound.getHandsetMicrophoneGainLimits().get(position), getActivity());
        else if (dSeekBarCardView == mCamMicrophoneGainCard)
            Sound.setCamMicrophoneGain(Sound.getCamMicrophoneGainLimits().get(position), getActivity());
        else if (dSeekBarCardView == mSpeakerGainCard)
            Sound.setSpeakerGain(Sound.getSpeakerGainLimits().get(position), getActivity());
        else if (dSeekBarCardView == mHeadphonePowerAmpGainCard)
            Sound.setHeadphonePowerAmpGain(Sound.getHeadphonePowerAmpGainLimits().get(position), getActivity());
        else if (dSeekBarCardView == mMicrophoneGainCard)
            Sound.setMicrophoneGain(Sound.getMicrophoneGainLimits().get(position), getActivity());
        else if (dSeekBarCardView == mVolumeGainCard)
            Sound.setVolumeGain(Sound.getVolumeGainLimits().get(position), getActivity());
    }
}
