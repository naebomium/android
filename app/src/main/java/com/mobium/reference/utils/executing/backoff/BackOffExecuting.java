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

/**
 *
 *
 * Date: 13.12.12
 * Time: 1:43
 */
public class BackOffExecuting {
    private BackOffStrategy strategy;

    public BackOffExecuting(BackOffStrategy strategy) {
        this.strategy = strategy;
    }

    public boolean execute(BackOffRunnable runnable) {
        int attempt = 0;
        while (true) {
            try {
                runnable.run();
                return true;
            } catch (BackOffException e) {
                e.printStackTrace();
                long delay = strategy.getNextAttemptDelay(++attempt);
                if (delay < 0)
                    return false;
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                    return false;
                }
            }
        }
    }
}
