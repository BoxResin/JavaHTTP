# JavaHTTP

[![Download](https://api.bintray.com/packages/boxresin/maven/JavaHTTP/images/download.svg) ](https://bintray.com/boxresin/maven/JavaHTTP/_latestVersion)
[![GitHub license](https://img.shields.io/badge/license-MIT-blue.svg)](https://raw.githubusercontent.com/BoxResin/JavaHTTP/master/LICENSE)

![java-logo](https://cloud.githubusercontent.com/assets/13031505/25526914/d0f8f148-2c50-11e7-930e-14767ec12829.jpeg)
![_http_logo_dpwnload 2](https://cloud.githubusercontent.com/assets/13031505/25526913/d0f735ba-2c50-11e7-83c6-d2b3411a031e.png)


## About

A light-weight HTTP client library for Java. It's based on `HttpURLConnection` in java network package.

[![light-weight library](https://cloud.githubusercontent.com/assets/13031505/25526854/92c769e0-2c50-11e7-9457-5fed461497f9.png)](https://bintray.com/boxresin/maven/JavaHTTP#files/boxresin/library/JavaHTTP/1.0.0)

## Getting Started

Add the following to `app`'s `build.gradle` file:

```gradle
dependencies { 
    compile 'boxresin.library:JavaHTTP:1.1.0'
}
```

## Usage

To send an HTTP request to `google.com` with `GET` method, do like below.

```Java
HttpResponse response = new HttpRequester()
    .setUrl("https://www.google.com/")
    .setMethod("GET")
    .request();
```

You can get information of the HTTP response from the `HttpResponse` object.

```java
int statusCode = response.getStatusCode();
String statusMessage = response.getStatusMessage();
String body = response.getBody();
String headerValue = response.getHeader("header-key");
```

**NOTE**: To use this library in Android project, you have to add `INTERNET` permission to `AndroidManifest.xml` first. Furthermore, **DO NOT** call `HttpRequester`'s `request` method on the UI thread.

- **Sending a request with POST method**

```java
HttpResponse response = new HttpRequester()
    .setUrl("https://www.google.com/")
    .setMethod("POST")
    .addParameter("key", "value")
    .addParameter("key", "value")
    .addParameter("key", "value")
    .addParameter("key", "value")
    .request();
```

- **Sending a request with some headers**

```java
HttpResponse response = new HttpRequester()
    .setUrl("https://www.google.com/")
    .setMethod("GET")
    .addHeader("key", "value")
    .addHeader("key", "value")
    .addHeader("key", "value")
    .addHeader("key", "value")
    .request();
```

- **URL query string with percent-encoding**

```java
// These keys and values will be percent encoded.
Map<String, String> params = new HashMap<>();
params.put("q", "java http");
params.put("from", "https://github.com/BoxResin/JavaHTTP");

HttpResponse response = new HttpRequester()
        .setUrl("https://www.google.com/", params)
        .setMethod("GET")
        .request();
```

## Document
You can see all documents of the library at [here](https://boxresin.github.io/JavaHTTP/document/v1.1.0/).

## License

```
MIT License

Copyright (c) 2017 Minsuk Eom

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
