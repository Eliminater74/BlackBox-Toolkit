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

package com.kunalkene1797.blackboxkit.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Toast;

import com.kunalkene1797.blackboxkit.R;
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
import com.kunalkene1797.blackboxkit.utils.kernel.CPU;
import com.kunalkene1797.blackboxkit.utils.root.RootFile;
import com.kunalkene1797.blackboxkit.utils.root.RootUtils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Created by willi on 30.11.14.
 */
public class Utils implements Constants {

    public static boolean DARKTHEME = false;

    public static void errorDialog(Context context, Exception e) {
        new AlertDialog.Builder(context).setMessage(e.getMessage()).setNeutralButton(context.getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
    }

    public static void circleAnimate(final View view, int cx, int cy) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            view.setVisibility(View.INVISIBLE);

            int finalRadius = Math.max(view.getWidth(), view.getHeight());
            Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, finalRadius);
            anim.setDuration(500);
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    view.setVisibility(View.VISIBLE);
                }
            });
            anim.start();
        }
    }

    public static String getExternalStorage() {
        String path = RootUtils.runCommand("echo ${SECONDARY_STORAGE%%:*}");
        return path.contains("/") ? path : null;
    }

    public static String getInternalStorage() {
        String dataPath = existFile("/data/media/0") ? "/data/media/0" : "/data/media";
        if (!new RootFile(dataPath).isEmpty()) return dataPath;
        if (existFile("/sdcard")) return "/sdcard";
        return Environment.getExternalStorageDirectory().getPath();
    }

    public static void confirmDialog(String title, String message, DialogInterface.OnClickListener onClickListener,
                                     Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (title != null) builder.setTitle(title);
        if (message != null) builder.setMessage(message);
        builder.setNegativeButton(context.getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        }).setPositiveButton(context.getString(R.string.ok), onClickListener).show();
    }

    public static String readAssetFile(Context context, String file) {
        BufferedReader in = null;
        try {
            StringBuilder buf = new StringBuilder();
            InputStream is = context.getAssets().open(file);
            in = new BufferedReader(new InputStreamReader(is));

            String str;
            boolean isFirst = true;
            while ((str = in.readLine()) != null) {
                if (isFirst) isFirst = false;
                else buf.append('\n');
                buf.append(str);
            }
            return buf.toString();
        } catch (IOException e) {
            Log.e(TAG, "Unable to read " + file);
        } finally {
            if (in != null) try {
                in.close();
            } catch (IOException e) {
                Log.e(TAG, "Unable to close Reader " + file);
            }
        }
        return null;
    }

    public static void setLocale(String lang, Context context) {
        Resources res = context.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.locale = new Locale(lang);
        res.updateConfiguration(conf, dm);
    }

    public static void vibrate(int duration) {
        RootUtils.runCommand("echo " + duration + " > /sys/class/timed_output/vibrator/enable");
    }

    public static List<String> getApplys(Class mClass) {
        List<String> applys = new ArrayList<>();

        if (mClass == BatteryFragment.class)
            applys.addAll(new ArrayList<>(Arrays.asList(BATTERY_ARRAY)));
        else if (mClass == CPUFragment.class) {
            for (String cpu : CPU_ARRAY)
                if (cpu.startsWith("/sys/devices/system/cpu/cpu%d/cpufreq"))
                    for (int i = 0; i < CPU.getCoreCount(); i++)
                        applys.add(String.format(cpu, i));
                else applys.add(cpu);
        } else if (mClass == CPUHotplugFragment.class) for (String[] array : CPU_HOTPLUG_ARRAY)
            applys.addAll(new ArrayList<>(Arrays.asList(array)));
        else if (mClass == CPUVoltageFragment.class)
            applys.addAll(new ArrayList<>(Arrays.asList(CPU_VOLTAGE_ARRAY)));
        else if (mClass == GPUFragment.class) for (String[] arrays : GPU_ARRAY)
            applys.addAll(new ArrayList<>(Arrays.asList(arrays)));
        else if (mClass == IOFragment.class)
            applys.addAll(new ArrayList<>(Arrays.asList(IO_ARRAY)));
        else if (mClass == KSMFragment.class)
            applys.addAll(new ArrayList<>(Arrays.asList(KSM_ARRAY)));
        else if (mClass == LMKFragment.class) applys.add(LMK_MINFREE);
        else if (mClass == MiscFragment.class) for (String[] arrays : MISC_ARRAY)
            applys.addAll(new ArrayList<>(Arrays.asList(arrays)));
        else if (mClass == ScreenFragment.class) for (String[] arrays : SCREEN_ARRAY)
            applys.addAll(new ArrayList<>(Arrays.asList(arrays)));
        else if (mClass == SoundFragment.class) for (String[] arrays : SOUND_ARRAY)
            applys.addAll(new ArrayList<>(Arrays.asList(arrays)));
        else if (mClass == ThermalFragment.class) for (String[] arrays : THERMAL_ARRAYS)
            applys.addAll(new ArrayList<>(Arrays.asList(arrays)));
        else if (mClass == VMFragment.class)
            applys.addAll(new ArrayList<>(Arrays.asList(VM_ARRAY)));
        else if (mClass == WakeFragment.class) for (String[] arrays : WAKE_ARRAY)
            applys.addAll(new ArrayList<>(Arrays.asList(arrays)));

        return applys;
    }

    public static double celsiusToFahrenheit(double celsius) {
        double temp = celsius * 9 / 5 + 32;
        return (double) Math.round(temp * 100.0) / 100.0;
    }

    public static long stringToLong(String string) {
        try {
            return Long.parseLong(string);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static int stringToInt(String string) {
        try {
            return Math.round(Float.parseFloat(string));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static void launchUrl(Context context, String link) {
        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(link)));
    }

    public static int getActionBarHeight(Context context) {
        TypedArray ta = context.obtainStyledAttributes(new int[]{R.attr.actionBarSize});
        int actionBarSize = ta.getDimensionPixelSize(0, 112);
        ta.recycle();
        return actionBarSize;
    }

    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public static int getScreenOrientation(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels <
                context.getResources().getDisplayMetrics().heightPixels ?
                Configuration.ORIENTATION_PORTRAIT : Configuration.ORIENTATION_LANDSCAPE;
    }

    public static void toast(String message, Context context) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static int getInt(String name, int defaults, Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).getInt(name, defaults);
    }

    public static void saveInt(String name, int value, Context context) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit().putInt(name, value).apply();
    }

    public static boolean getBoolean(String name, boolean defaults, Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).getBoolean(name, defaults);
    }

    public static void saveBoolean(String name, boolean value, Context context) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit().putBoolean(name, value).apply();
    }

    public static String getString(String name, String defaults, Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).getString(name, defaults);
    }

    public static void saveString(String name, String value, Context context) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit().putString(name, value).apply();
    }

    public static String getProp(String key) {
        return RootUtils.runCommand("getprop " + key);
    }

    public static boolean isPropActive(String key) {
        try {
            return RootUtils.runCommand("getprop | grep " + key).split("]:")[1].contains("running");
        } catch (Exception ignored) {
            return false;
        }
    }

    public static boolean hasProp(String key) {
        try {
            return RootUtils.runCommand("getprop | grep " + key).split("]:").length > 1;
        } catch (Exception ignored) {
            return false;
        }
    }

    public static void writeFile(String path, String text, boolean append) {
        try {
            FileWriter fWriter = new FileWriter(path, append);
            fWriter.write(text);
            fWriter.flush();
            fWriter.close();
        } catch (Exception e) {
            Log.e(TAG, "Failed to write " + path);
        }
    }

    public static String readFile(String file, boolean root) {
        if (root) return new RootFile(file).readFile();
        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null)
                stringBuilder.append(line).append("\n");
            bufferedReader.close();
            fileReader.close();
            return stringBuilder.toString().trim();
        } catch (FileNotFoundException ignored) {
            Log.e(TAG, "File does not exist " + file);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "Failed to read " + file);
        }
        return null;
    }

    public static boolean existFile(String file) {
        return new RootFile(file).exists();
    }

    public static String readFile(String file) {
        return readFile(file, true);
    }

}
