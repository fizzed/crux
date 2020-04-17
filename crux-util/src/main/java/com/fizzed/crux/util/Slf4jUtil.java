/*
 * Copyright 2020 Fizzed, Inc.
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
package com.fizzed.crux.util;

import org.slf4j.Logger;

public class Slf4jUtil {
 
    static public void log(
            MessageLevel messageLevel,
            Logger log,
            String message,
            Object... arguments) {
        
        switch ((messageLevel != null ? messageLevel : MessageLevel.DEBUG)) {
            case ERROR:
                log.error(message, arguments);
                break;
            case WARN:
                log.warn(message, arguments);
                break;
            case INFO:
                log.info(message, arguments);
                break;
            case DEBUG:
                log.debug(message, arguments);
                break;
            case TRACE:
                log.trace(message, arguments);
                break;
        }
    }
    
}