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

import com.fizzed.crux.okhttp.OkEdge.LoggingLevel;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import okhttp3.HttpUrl;
import okhttp3.Response;

public class DemoMain {
    
    static public void main(String[] args) throws IOException {
        
        OkEdgeState state = new OkEdgeState()
            .cookies(true)
            .insecure(true)
            .logging(LoggingLevel.BODY);
        
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
        
        HttpUrl url = HttpUrl.parse("http://dev.mysql.com/get/Downloads/MySQL-5.7/mysql-5.7.11-win32.zip");
        
        response = new OkEdge(state)
            .get(url.toString())
            .execute();
        
        System.out.println("Done executing, has response...");
        
        try (InputStream input = response.body().byteStream()) {
        
            List<String> paths = url.pathSegments();
            String lastPath = paths.get(paths.size()-1);
            
            File targetFile = new File(lastPath);
            
            if (targetFile.exists()) {
                System.err.println("Target file " + targetFile + " already exists. Delete it.");
                System.exit(1);
            }
            
            try (OutputStream output = new FileOutputStream(targetFile)) {
                byte[] buffer = new byte[16 * 1024];
                int bytesRead;
                System.out.println("About to start I/O from input to output");

                int count = 0;
                while ((bytesRead = input.read(buffer)) != -1) {
                    output.write(buffer, 0, bytesRead);

                    if (count > 72) {
                        count = 0;
                        System.out.println();
                    }

                    System.out.print(".");
                    System.out.flush();

                    count++;
                }
            }
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
