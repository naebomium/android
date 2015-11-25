/*
 * Copyright (c) 2013 Extradea LLC.
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

package com.mobium.reference.utils;

import android.os.Build;

import java.io.IOException;
import java.io.InputStream;

/**
 *
 *
 * Date: 13.12.12
 * Time: 12:06
 */
public class PhoneHardwarePerformance {
    private boolean isARMv7;

    private float bogoMips;

    public PhoneHardwarePerformance() {
        isARMv7 = Build.CPU_ABI.contains("v7");

        try {
            String[] cpuInfo = readCpuInfo().split("\n");
            for (String s : cpuInfo) {
                s = s.toLowerCase();
                if (!isARMv7) {
                    if (s.contains("processor")) {
                        isARMv7 = s.contains("armv7");
                    }
                }

                if (s.contains("bogomips")) {
                    bogoMips = Math.max(Float.parseFloat(s.substring(s.indexOf(':') + 1).trim()), bogoMips);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String readCpuInfo() {
        ProcessBuilder cmd;
        String result = "";
        try {
            String[] args = {"/system/bin/cat", "/proc/cpuinfo"};
            cmd = new ProcessBuilder(args);

            Process process = cmd.start();
            InputStream in = process.getInputStream();
            byte[] re = new byte[1024];
            while (in.read(re) != -1) {
                System.out.println(new String(re));
                result = result + new String(re);
            }
            in.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public boolean isARMv7() {
        return isARMv7;
    }

    public float getBogoMips() {
        return bogoMips;
    }

    public boolean isPhoneSlow() {
        return bogoMips < 1000;
    }
}
