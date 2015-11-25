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

package com.mobium.reference.utils.executing;

/**
 *
 * Date: 19.09.12
 * Time: 16:12
 */
public class ExecutingException extends Exception {
    private String userMessage;
    private String errorType;
    private boolean canRepeat = true;

    public ExecutingException(String userMessage) {
        this.userMessage = userMessage;
    }

    public ExecutingException(String userMessage, String detailMessage) {
        super(detailMessage);
        this.userMessage = userMessage;
    }

    public ExecutingException(String userMessage, String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
        this.userMessage = userMessage;
    }

    public ExecutingException(String userMessage, Throwable throwable) {
        super(throwable);
        this.userMessage = userMessage;
    }

    public ExecutingException(String userMessage, boolean canRepeat) {
        this.userMessage = userMessage;
        this.canRepeat = canRepeat;
    }

    public ExecutingException(String userMessage, String detailMessage, boolean canRepeat) {
        super(detailMessage);
        this.userMessage = userMessage;
        this.canRepeat = canRepeat;
    }

    public ExecutingException(String userMessage, String detailMessage, boolean canRepeat, Throwable throwable) {
        super(detailMessage, throwable);
        this.userMessage = userMessage;
        this.canRepeat = canRepeat;
    }

    public ExecutingException(String userMessage, boolean canRepeat, Throwable throwable) {
        super(throwable);
        this.userMessage = userMessage;
        this.canRepeat = canRepeat;
    }

    public boolean isCanRepeat() {
        return canRepeat;
    }

    public String getUserMessage() {
        return userMessage;
    }

    public String getErrorType() {
        return errorType;
    }
}
