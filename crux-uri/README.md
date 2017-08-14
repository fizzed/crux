crux-uri
=========

## Overview

A lightweight, zero-dependency library for building and using URIs.  Supports
builder-style syntax. Accepts objects in various methods to simplify building
valid REST uris. Designed in its own project so you can avoid pulling in larger
libraries (e.g. okhttp or apache http) just for their URI class.

## Usage

```xml
<dependency>
    <groupId>com.fizzed</groupId>
    <artifactId>crux-util</artifactId>
    <version><!-- latest version --></version>
</dependency>
```
## Example

```java

import com.fizzed.crux.uri.MutableUri;
import com.fizzed.crux.uri.Uri;

...

Uri uri = new MutableUri()
    .scheme("https")
    .host("fizzed.com")
    .path("/contact")
    .query("a", 1)
    .immutable();

```
