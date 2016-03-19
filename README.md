Crux by Fizzed
==============

[Fizzed, Inc.](http://fizzed.com) (Follow on Twitter: [@fizzed_inc](http://twitter.com/fizzed_inc))

## Overview

Foundational libraries for Fizzed.  Attempts to keep dependencies to a bare
minimum.

## [crux-util](crux-util)

 * Utilities for working with security (e.g. TLS)

## [crux-okhttp](crux-okhttp)

 * OkEdge: client+request combined for much simpler usage w/ better readability
 * Shared state, with per-request overrides for things like followRedirects, etc.
 * Helpers to trust any cert (insecure mode)
 * In-memory cookie jar
 * Simplified form & json post and put
 * Handling of empty request body
 * Form values accept objects rather than just strings
 * Simplified authentication
 * Hamcrest matchers for your unit tests!

## [crux-vagrant](crux-vagrant)

 * Fetch vagrant ssh config file
 * Fetch and parse vagrant status
 * Verify if all or any machines running
 * Values are cached for reuse (since querying vagrant is expensive)
 * Super useful for using in your unit tests (e.g. skip class if vagrant not running)

```java
public class MyTest {
    
    static public final VagrantClient VAGRANT
        = new VagrantClient.Builder()
            .workingDirectory(Paths.get("."))
            .safeLoad();
    
    @Before
    public void onlyIfAllVagrantMachinesRunning() {
        assumeTrue("Is vagrant running?", VAGRANT.areAllMachinesRunning());
    }

    // rest of class
}
```
 

## License

Copyright (C) 2016 Fizzed, Inc.

This work is licensed under the Apache License, Version 2.0. See LICENSE for details.
