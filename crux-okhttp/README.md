crux-okhttp
===========

## Overview

Extensions and utilities to Square's [okhttp library](https://github.com/square/okhttp).

 * OkEdge: the client + request combined for much simpler usage w/ better readability
 * Shared state, with per-request overrides for things like followRedirects, etc.
 * Helpers to trust any cert (insecure mode)
 * In-memory cookie jar
 * Simplified form & json post and put
 * Handling of empty request body
 * Form values accept objects rather than just strings
 * Simplified authentication
 * Hamcrest matchers for your unit tests!

## Usage

```xml
<dependency>
    <groupId>com.fizzed</groupId>
    <artifactId>crux-okhttp</artifactId>
    <version>0.4.0</version>
</dependency>
```

## Examples

```java
public class MyClass {
    
    static public void main(String[] args) throws Exception {
        Response response = new OkEdge()
            .getJson("http://jsonplaceholder.typicode.com/posts/1")
            .execute();
    }

}
```
