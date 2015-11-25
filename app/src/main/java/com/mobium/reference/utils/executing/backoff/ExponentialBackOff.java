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

package com.mobium.reference.utils.executing.backoff;

import java.util.Random;

/**
 *
 *
 * Date: 13.12.12
 * Time: 1:29
 */
public class ExponentialBackOff extends BackOffStrategy {

    public static final long DEFAULT_TIME_SLOT = 100;

    private long timeSlot;
    private Random rnd = new Random();
    private int totalAttemptsCount;

    public ExponentialBackOff() {
        this(DEFAULT_TIME_SLOT, Integer.MAX_VALUE);
    }

    public ExponentialBackOff(long timeSlot) {
        this(timeSlot, Integer.MAX_VALUE);
    }

    public ExponentialBackOff(long timeSlot, int totalAttemptsCount) {
        this.totalAttemptsCount = totalAttemptsCount;
        this.timeSlot = timeSlot;
    }

    @Override
    public long getNextAttemptDelay(int attemptCount) {
        if (attemptCount >= totalAttemptsCount)
            return -1;
        return rnd.nextInt(attemptCount) * timeSlot;
    }
}
