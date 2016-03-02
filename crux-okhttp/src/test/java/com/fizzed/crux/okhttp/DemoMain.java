/*
 * Copyright 2016 Fizzed, Inc.
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
package com.fizzed.crux.okhttp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import okhttp3.Response;

public class DemoMain {
    
    static public void main(String[] args) throws IOException {
        
        OkEdgeState state = new OkEdgeState()
            .cookies(true)
            .insecure(true)
            .loggingLevel(OkEdge.LoggingLevel.HEADERS);
        
        Response response;
        
        /**
        response = new OkEdge(state)
            //.insecure(true)
            //.logging(OkEdge.LoggingLevel.BODY)
            .postJson("http://jsonplaceholder.typicode.com/posts").body(
                "{\n" +
                "\"userId\": 1,\n" +
                "\"id\": 1,\n" +
                "\"title\": \"this is a title\",\n" +
                "\"body\": \"a body\"\n" +
                "}")
            .execute();
        */
        
        /**
        response = new OkEdge(state)
            .getJson("http://jsonplaceholder.typicode.com/posts/1")
            .execute();
        */
        
        response = new OkEdge(state)
            .get("http://dev.mysql.com/get/Downloads/MySQL-5.7/mysql-5.7.11-win32.zip")
            .execute();
        
        System.out.println("Done executing, has response...");
        
        // 1-shot body...
        InputStream input = response.body().byteStream();
        File targetFile = new File("mysql-5.7.11-win32.zip");
        OutputStream output = new FileOutputStream(targetFile);
        byte[] buffer = new byte[8 * 1024];
        int bytesRead;
        System.err.println("About to start I/O from input to output");
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
            System.err.println(".");
        }
        
        /**
        response = new OkEdge()
        .insecure(true)
        .logging(OkEdge.LoggingLevel.BODY)
        .cookies(true)
        .get("https://example.com/")
        .execute();
        System.out.println("response: " + response.code());
        response = new OkEdge()
        .insecure(true)
        .logging(OkEdge.LoggingLevel.BODY)
        .cookies(true)
        .get("https://example.com")
        .execute();
        System.out.println("response: " + response.code());
        response = new OkEdge()
        .insecure(true)
        .logging(OkEdge.LoggingLevel.BODY)
        .cookies(true)
        .postForm("https://example.com")
        .add("username", "jjlauer")
        .add("password", "test")
        .commit()
        .execute();
        System.out.println("response: " + response.code());
         */

    }
    
}
