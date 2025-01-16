Crux by Fizzed
==============

[![Maven Central](https://img.shields.io/maven-central/v/com.fizzed/crux?color=blue&style=flat-square)](https://mvnrepository.com/artifact/com.fizzed/crux)

## Automated Testing

The following Java versions and platforms are tested using GitHub workflows:

[![Java 8](https://img.shields.io/github/actions/workflow/status/fizzed/crux/java8.yaml?branch=master&label=Java%208&style=flat-square)](https://github.com/fizzed/crux/actions/workflows/java8.yaml)
[![Java 11](https://img.shields.io/github/actions/workflow/status/fizzed/crux/java11.yaml?branch=master&label=Java%2011&style=flat-square)](https://github.com/fizzed/crux/actions/workflows/java11.yaml)
[![Java 17](https://img.shields.io/github/actions/workflow/status/fizzed/crux/java17.yaml?branch=master&label=Java%2017&style=flat-square)](https://github.com/fizzed/crux/actions/workflows/java17.yaml)
[![Java 21](https://img.shields.io/github/actions/workflow/status/fizzed/crux/java21.yaml?branch=master&label=Java%2021&style=flat-square)](https://github.com/fizzed/crux/actions/workflows/java21.yaml)

[![Linux x64](https://img.shields.io/github/actions/workflow/status/fizzed/crux/java11.yaml?branch=master&label=Linux%20x64&style=flat-square)](https://github.com/fizzed/crux/actions/workflows/java11.yaml)
[![MacOS arm64](https://img.shields.io/github/actions/workflow/status/fizzed/crux/macos-arm64.yaml?branch=master&label=MacOS%20arm64&style=flat-square)](https://github.com/fizzed/crux/actions/workflows/macos-x64.yaml)
[![Windows x64](https://img.shields.io/github/actions/workflow/status/fizzed/crux/windows-x64.yaml?branch=master&label=Windows%20x64&style=flat-square)](https://github.com/fizzed/crux/actions/workflows/windows-x64.yaml)

The following platforms are tested using the [Fizzed, Inc.](http://fizzed.com) build system:

[![Linux arm64](https://img.shields.io/badge/Linux%20arm64-passing-green)](buildx-results.txt)
[![Linux riscv64](https://img.shields.io/badge/Linux%20riscv64-passing-green)](buildx-results.txt)
[![Linux MUSL x64](https://img.shields.io/badge/Linux%20MUSL%20x64-passing-green)](buildx-results.txt)
[![MacOS x64](https://img.shields.io/badge/MacOS%20x64-passing-green)](buildx-results.txt)
[![Windows arm64](https://img.shields.io/badge/Windows%20arm64-passing-green)](buildx-results.txt)
[![FreeBSD x64](https://img.shields.io/badge/FreeBSD%20x64-passing-green)](buildx-results.txt)
[![OpenBSD x64](https://img.shields.io/badge/OpenBSD%20x64-passing-green)](buildx-results.txt)

## Overview

Various foundational utility libraries for Java 8+.  Everything is published
to maven central.  All documentation is included within each module.

 - [crux-util](crux-util)
 - [crux-uri](crux-uri)
 - [crux-okhttp](crux-okhttp)
 - [crux-vagrant](crux-vagrant)

## Usage

In your pom file add:

```xml
<dependencies>
  <dependency>
    <groupId>com.fizzed</groupId>
    <artifactId>crux-util</artifactId>
    <version>1.0.48</version>
  </dependency>
</dependencies>
```

## Sponsorship & Support

![](https://cdn.fizzed.com/github/fizzed-logo-100.png)

Project by [Fizzed, Inc.](http://fizzed.com) (Follow on Twitter: [@fizzed_inc](http://twitter.com/fizzed_inc))

**Developing and maintaining opensource projects requires significant time.** If you find this project useful or need
commercial support, we'd love to chat. Drop us an email at [ping@fizzed.com](mailto:ping@fizzed.com)

Project sponsors may include the following benefits:

 - Priority support (outside of Github)
 - Feature development & roadmap
 - Priority bug fixes
 - Privately hosted continuous integration tests for their unique edge or use cases
 
## License

Copyright (C) 2016 Fizzed, Inc.

This work is licensed under the Apache License, Version 2.0. See LICENSE for details.
