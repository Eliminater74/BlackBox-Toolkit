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

package com.kunalkene1797.blackboxkit.utils.tools;

import com.kunalkene1797.blackboxkit.utils.Constants;
import com.kunalkene1797.blackboxkit.utils.Utils;
import com.kunalkene1797.blackboxkit.utils.root.RootUtils;

import java.util.LinkedHashMap;

/**
 * Created by willi on 01.01.15.
 */
public class Buildprop implements Constants {

    public static void overwrite(String oldKey, String oldValue, String newKey, String newValue) {
        RootUtils.mount(true, "/system");
        RootUtils.runCommand("sed 's|" + oldKey + "=" + oldValue + "|" + newKey + "=" + newValue
                + "|g' -i /system/build.prop");
    }

    public static void addKey(String key, String value) {
        RootUtils.mount(true, "/system");
        RootUtils.runCommand("echo " + key + "=" + value + " >> " + BUILD_PROP);
    }

    public static LinkedHashMap<String, String> getProps() {
        String[] values = Utils.readFile(BUILD_PROP).split("\\r?\\n");
        LinkedHashMap<String, String> list = new LinkedHashMap<>();
        for (String prop : values)
            if (!prop.isEmpty() && !prop.startsWith("#")) {
                String[] line = prop.split("=");
                list.put(line.length > 0 ? line[0] : "", line.length > 1 ? line[1] : "");
            }
        return list;
    }

}
