crux-vagrant
============

## Overview

Utility library for working with Vagrant

 * Fetch vagrant ssh config file
 * Fetch and parse vagrant status
 * Verify if all or any machines running
 * Values are cached for reuse (since querying vagrant is expensive)
 * Super useful for using in your unit tests (e.g. skip class if vagrant not running)

## Usage

```xml
<dependency>
    <groupId>com.fizzed</groupId>
    <artifactId>crux-vagrant</artifactId>
    <version>0.3.2</version>
</dependency>
```

## Examples

In a junit test

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
