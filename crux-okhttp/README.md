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
    <version>0.3.2</version>
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

## [crux-vagrant](crux-vagrant)

 * Fetch vagrant ssh config file
 * Fetch and parse vagrant status
 * Verify if all or any machines running
 * Values are cached for reuse (since querying vagrant is expensive)
 * Super useful for using in your unit tests (e.g. skip class if vagrant not running)

### Usage

```xml
<dependency>
    <groupId>com.fizzed</groupId>
    <artifactId>crux-vagrant</artifactId>
    <version>0.3.2</version>
</dependency>
```

### Example in a unit test

```java
import com.fizzed.crux.vagrant.VagrantClient;
import com.fizzed.crux.vagrant.VagrantClients;

public class MyTest {
    
    static public final VagrantClient VAGRANT_CLIENT
        = VagrantClients.cachingOrEmptyClient();
    
    @Before
    public void onlyIfAllVagrantMachinesRunning() {
        assumeTrue("Are vagrant machines running?", VAGRANT_CLIENT.areAllMachinesRunning());
    }

    // rest of class
}
```
