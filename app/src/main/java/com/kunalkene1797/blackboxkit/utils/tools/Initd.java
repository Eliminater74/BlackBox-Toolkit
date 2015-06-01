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
import com.kunalkene1797.blackboxkit.utils.root.RootFile;
import com.kunalkene1797.blackboxkit.utils.root.RootUtils;

import java.util.List;

/**
 * Created by willi on 25.04.15.
 */
public class Initd implements Constants {

    public static String execute(String file) {
        return RootUtils.runCommand(INITD + "/" + file);
    }

    public static String getInitd(String file) {
        return Utils.readFile(INITD + "/" + file);
    }

    public static List<String> getInitds() {
        RootFile initd = new RootFile(INITD);
        if (!initd.exists()) {
            RootUtils.mount(true, "/system");
            initd.mkdir();
        }
        return initd.list();
    }

}
