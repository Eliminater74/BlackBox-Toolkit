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

package com.kunalkene1797.blackboxkit;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.kunalkene1797.blackboxkit.elements.DAdapter;
import com.kunalkene1797.blackboxkit.elements.ScrimInsetsFrameLayout;
import com.kunalkene1797.blackboxkit.elements.SplashView;
import com.kunalkene1797.blackboxkit.fragments.information.FrequencyTableFragment;
import com.kunalkene1797.blackboxkit.fragments.information.KernelInformationFragment;
import com.kunalkene1797.blackboxkit.fragments.kernel.BatteryFragment;
import com.kunalkene1797.blackboxkit.fragments.kernel.CPUFragment;
import com.kunalkene1797.blackboxkit.fragments.kernel.CPUHotplugFragment;
import com.kunalkene1797.blackboxkit.fragments.kernel.CPUVoltageFragment;
import com.kunalkene1797.blackboxkit.fragments.kernel.GPUFragment;
import com.kunalkene1797.blackboxkit.fragments.kernel.IOFragment;
import com.kunalkene1797.blackboxkit.fragments.kernel.KSMFragment;
import com.kunalkene1797.blackboxkit.fragments.kernel.LMKFragment;
import com.kunalkene1797.blackboxkit.fragments.kernel.MiscFragment;
import com.kunalkene1797.blackboxkit.fragments.kernel.ScreenFragment;
import com.kunalkene1797.blackboxkit.fragments.kernel.SoundFragment;
import com.kunalkene1797.blackboxkit.fragments.kernel.ThermalFragment;
import com.kunalkene1797.blackboxkit.fragments.kernel.VMFragment;
import com.kunalkene1797.blackboxkit.fragments.kernel.WakeFragment;
import com.kunalkene1797.blackboxkit.fragments.other.AboutusFragment;
import com.kunalkene1797.blackboxkit.fragments.other.SettingsFragment;
import com.kunalkene1797.blackboxkit.fragments.tools.BackupFragment;
import com.kunalkene1797.blackboxkit.fragments.tools.BuildpropFragment;
import com.kunalkene1797.blackboxkit.fragments.tools.InitdFragment;
import com.kunalkene1797.blackboxkit.fragments.tools.ProfileFragment;
import com.kunalkene1797.blackboxkit.fragments.tools.RecoveryFragment;
import com.kunalkene1797.blackboxkit.utils.Constants;
import com.kunalkene1797.blackboxkit.utils.Utils;
import com.kunalkene1797.blackboxkit.utils.kernel.CPUHotplug;
import com.kunalkene1797.blackboxkit.utils.kernel.CPUVoltage;
import com.kunalkene1797.blackboxkit.utils.kernel.GPU;
import com.kunalkene1797.blackboxkit.utils.kernel.KSM;
import com.kunalkene1797.blackboxkit.utils.kernel.LMK;
import com.kunalkene1797.blackboxkit.utils.kernel.Screen;
import com.kunalkene1797.blackboxkit.utils.kernel.Sound;
import com.kunalkene1797.blackboxkit.utils.kernel.Thermal;
import com.kunalkene1797.blackboxkit.utils.kernel.Wake;
import com.kunalkene1797.blackboxkit.utils.root.RootUtils;
import com.kunalkene1797.blackboxkit.utils.tools.Backup;

/**
 * Created by willi on 01.12.14.
 */

public class MainActivity extends AppCompatActivity implements Constants {

    /**
     * Cache the context of this activity
     */
    private static Context context;

    /**
     * The argument string of LAUNCH_NAME
     */
    public static String LAUNCH_ARG = "launch_section";

    private String LAUNCH_NAME;

    /**
     * Views
     */
    private Toolbar toolbar;

    private ActionBarDrawerToggle mDrawerToggle;

    private DrawerLayout mDrawerLayout;
    private ScrimInsetsFrameLayout mScrimInsetsFrameLayout;
    private RecyclerView mDrawerList;
    private SplashView mSplashView;

    private DAdapter.Adapter mAdapter;

    /**
     * Current Fragment position
     */
    private int cur_position;

    private AlertDialog betaDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // If there is a previous activity running, kill it
        if (context != null) {
            RootUtils.closeSU();
            ((Activity) context).finish();
        }
        context = this;

        // Check if darktheme is in use and cache it as boolean
        Utils.DARKTHEME = Utils.getBoolean("darktheme", true, this);
        if (Utils.DARKTHEME) super.setTheme(R.style.AppThemeDark);

        // Show a dialog if user is running a beta version
        if (Utils.getBoolean("forceenglish", false, this)) Utils.setLocale("en", this);
        try {
            LAUNCH_NAME = getIntent().getStringExtra(LAUNCH_ARG);
            if (LAUNCH_NAME == null && VERSION_NAME.contains("beta") && Utils.getBoolean("betainfo", true, this))
                betaDialog = new AlertDialog.Builder(MainActivity.this)
                        .setMessage(getString(R.string.beta_message, VERSION_NAME))
                        .setNeutralButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

        setContentView(R.layout.activity_main);
        mDrawerList = (RecyclerView) findViewById(R.id.drawer_list);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mLayoutManager.setSmoothScrollbarEnabled(true);
        mDrawerList.setLayoutManager(mLayoutManager);
        mDrawerList.setHasFixedSize(true);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (Utils.DARKTHEME) toolbar.setPopupTheme(R.style.ThemeOverlay_AppCompat_Dark);
        setSupportActionBar(toolbar);

        if (mDrawerLayout != null && mScrimInsetsFrameLayout != null)
            mDrawerLayout.closeDrawer(mScrimInsetsFrameLayout);

        // Use an AsyncTask to initialize everything
        new Task().execute();
    }

    /**
     * Gets called when there is an input on the navigation drawer
     *
     * @param position position of the fragment
     */
    private void selectItem(int position) {
        Fragment fragment = ITEMS.get(position).getFragment();

        mDrawerLayout.closeDrawer(mScrimInsetsFrameLayout);
        if (fragment == null || cur_position == position) return;
        cur_position = position;

        try {
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commitAllowingStateLoss();
        } catch (Exception e) {
            e.printStackTrace();
        }

        ActionBar actionBar;
        if ((actionBar = getSupportActionBar()) != null)
            actionBar.setTitle(ITEMS.get(position).getTitle());
        mAdapter.setItemChecked(position, true);
    }

    /**
     * Add all fragments in a list
     */
    private void setList() {
        ITEMS.clear();
        ITEMS.add(new DAdapter.Header(getString(R.string.information)));
        ITEMS.add(new DAdapter.Item(getString(R.string.kernel_information), new KernelInformationFragment()));
        ITEMS.add(new DAdapter.Item(getString(R.string.frequency_table), new FrequencyTableFragment()));
        ITEMS.add(new DAdapter.Header(getString(R.string.kernel)));
        ITEMS.add(new DAdapter.Item(getString(R.string.cpu), new CPUFragment()));
        if (CPUVoltage.hasCpuVoltage())
            ITEMS.add(new DAdapter.Item(getString(R.string.cpu_voltage), new CPUVoltageFragment()));
        if (CPUHotplug.hasCpuHotplug())
            ITEMS.add(new DAdapter.Item(getString(R.string.cpu_hotplug), new CPUHotplugFragment()));
        if (Thermal.hasThermal())
            ITEMS.add(new DAdapter.Item(getString(R.string.thermal), new ThermalFragment()));
        if (GPU.hasGpuControl())
            ITEMS.add(new DAdapter.Item(getString(R.string.gpu), new GPUFragment()));
        if (Screen.hasScreen())
            ITEMS.add(new DAdapter.Item(getString(R.string.screen), new ScreenFragment()));
        if (Wake.hasWake())
            ITEMS.add(new DAdapter.Item(getString(R.string.wake_controls), new WakeFragment()));
        if (Sound.hasSound())
            ITEMS.add(new DAdapter.Item(getString(R.string.sound), new SoundFragment()));
        ITEMS.add(new DAdapter.Item(getString(R.string.battery), new BatteryFragment()));
        ITEMS.add(new DAdapter.Item(getString(R.string.io_scheduler), new IOFragment()));
        if (KSM.hasKsm())
            ITEMS.add(new DAdapter.Item(getString(R.string.ksm), new KSMFragment()));
        if (LMK.getMinFrees() != null)
            ITEMS.add(new DAdapter.Item(getString(R.string.low_memory_killer), new LMKFragment()));
        ITEMS.add(new DAdapter.Item(getString(R.string.virtual_memory), new VMFragment()));
        ITEMS.add(new DAdapter.Item(getString(R.string.misc_controls), new MiscFragment()));
        ITEMS.add(new DAdapter.Header(getString(R.string.tools)));
        if (Backup.hasBackup())
            ITEMS.add(new DAdapter.Item(getString(R.string.backup), new BackupFragment()));
        ITEMS.add(new DAdapter.Item(getString(R.string.build_prop_editor), new BuildpropFragment()));
        ITEMS.add(new DAdapter.Item(getString(R.string.profile), new ProfileFragment()));
        ITEMS.add(new DAdapter.Item(getString(R.string.recovery), new RecoveryFragment()));
        ITEMS.add(new DAdapter.Item(getString(R.string.initd), new InitdFragment()));
        ITEMS.add(new DAdapter.Header(getString(R.string.other)));
        ITEMS.add(new DAdapter.Item(getString(R.string.settings), new SettingsFragment()));
        ITEMS.add(new DAdapter.Item(getString(R.string.about_us), new AboutusFragment()));
    }

    /**
     * Define all views
     */
    private void setView() {
        mScrimInsetsFrameLayout = (ScrimInsetsFrameLayout) findViewById(R.id.scrimInsetsFrameLayout);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setStatusBarBackgroundColor(getResources().getColor(R.color.statusbar_color));
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        mSplashView = (SplashView) findViewById(R.id.splash_view);
    }

    /**
     * Setup the views
     */
    private void setInterface() {
        mScrimInsetsFrameLayout.setLayoutParams(getDrawerParams());
        if (Utils.DARKTHEME)
            mScrimInsetsFrameLayout.setBackgroundColor(getResources().getColor(R.color.navigationdrawer_background_dark));

        mAdapter = new DAdapter.Adapter(ITEMS);
        mDrawerList.setAdapter(mAdapter);

        mAdapter.setItemOnly(true);
        mAdapter.setOnItemClickListener(new DAdapter.Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                selectItem(position);
            }
        });

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, 0, 0);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        if (Utils.DARKTHEME)
            mDrawerLayout.setBackgroundColor(getResources().getColor(R.color.black));

        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                if (mDrawerToggle != null) mDrawerToggle.syncState();
            }
        });
    }

    private class Task extends AsyncTask<Void, Void, Void> {

        private boolean hasRoot;
        private boolean hasBusybox;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            setView();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // Check root access and busybox installation
            if (RootUtils.rooted()) hasRoot = RootUtils.rootAccess();
            if (hasRoot) hasBusybox = RootUtils.busyboxInstalled();

            if (hasRoot && hasBusybox) {
                RootUtils.su = new RootUtils.SU();

                // Set permissions to specific files which are not readable by default
                String[] writePermission = {LMK_MINFREE};
                for (String file : writePermission)
                    RootUtils.runCommand("chmod 644 " + file);

                setList();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (!hasRoot || !hasBusybox) {
                Intent i = new Intent(MainActivity.this, TextActivity.class);
                Bundle args = new Bundle();
                args.putString(TextActivity.ARG_TEXT, !hasRoot ? getString(R.string.no_root)
                        : getString(R.string.no_busybox));
                Log.d(TAG, !hasRoot ? "no root" : "no busybox");
                i.putExtras(args);
                startActivity(i);

                if (hasRoot)
                    // Root is there but busybox is missing
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=stericson.busybox")));
                    } catch (ActivityNotFoundException ignored) {
                    }
                if (betaDialog != null) betaDialog.dismiss();
                cancel(true);
                finish();
                return;
            }

            mSplashView.finish();
            setInterface();

            // If LAUNCH_NAME is not null then open the fragment which matches with the string
            if (LAUNCH_NAME == null) LAUNCH_NAME = KernelInformationFragment.class.getSimpleName();
            for (int i = 0; i < ITEMS.size(); i++) {
                if (ITEMS.get(i).getFragment() != null)
                    if (LAUNCH_NAME.equals(ITEMS.get(i).getFragment().getClass().getSimpleName()))
                        selectItem(i);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mDrawerToggle.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (mScrimInsetsFrameLayout != null)
            mScrimInsetsFrameLayout.setLayoutParams(getDrawerParams());
        if (mDrawerToggle != null) mDrawerToggle.onConfigurationChanged(newConfig);
    }

    /**
     * This makes onBackPressed function work in Fragments
     */
    @Override
    public void onBackPressed() {
        try {
            if (!ITEMS.get(cur_position).getFragment().onBackPressed())
                if (!mDrawerLayout.isDrawerOpen(mScrimInsetsFrameLayout)) super.onBackPressed();
                else mDrawerLayout.closeDrawer(mScrimInsetsFrameLayout);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Exit SU
     */
    @Override
    protected void onDestroy() {
        RootUtils.closeSU();
        super.onDestroy();
    }

    /**
     * Let other Classes kill this activity
     */
    public static void destroy() {
        if (context != null) ((Activity) context).finish();
    }

    /**
     * A function to calculate the width of the Navigation Drawer
     * Phones and Tablets have different sizes
     *
     * @return the LayoutParams for the Drawer
     */
    private DrawerLayout.LayoutParams getDrawerParams() {
        DrawerLayout.LayoutParams params = (DrawerLayout.LayoutParams) mScrimInsetsFrameLayout.getLayoutParams();
        int width = getResources().getDisplayMetrics().widthPixels;

        boolean tablet = Utils.isTablet(this);
        int actionBarSize = Utils.getActionBarHeight(this);
        if (Utils.getScreenOrientation(this) == Configuration.ORIENTATION_LANDSCAPE) {
            params.width = width / 2;
            if (tablet)
                params.width -= actionBarSize + (35 * getResources().getDisplayMetrics().density);
        } else params.width = tablet ? width / 2 : width - actionBarSize;

        return params;
    }

    /**
     * Interface to make onBackPressed function work in Fragments
     */
    public interface OnBackButtonListener {
        boolean onBackPressed();
    }

}
