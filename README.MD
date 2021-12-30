# KMM RSS Reader

<img src="/media/ios+android.png"/>  

This is an open-source, mobile, cross-platform application built
with [Kotlin Multiplatform Mobile](https://kotlinlang.org/lp/mobile/). It's a simple RSS reader, and
you can download it from the [App Store](https://apps.apple.com/ru/app/kmm-rss-reader/id1563922264)
and [Google Play](https://play.google.com/store/apps/details?id=com.github.jetbrains.rssreader.androidApp).
It's been designed to demonstrate how KMM can be used in real production projects.

## Desktop and Web experiment

<img src="/media/desktop+web.png"/>

## Project structure

This repository contains a common Kotlin Multiplatform module, a Compose (Android+Desktop) project,
an iOS project and a Web project. The common module is connected with the Compose project via the
Gradle multi-project mechanism. For use in iOS applications, the shared module compiles into a
framework that is exposed to the Xcode project via the internal integration Gradle task. This
framework connects to the Xcode project that builds an iOS application. For web the shared module
transpiles into a commonjs2 module and connect the a React.js application using NPM and webpack.

You can achieve the same structure by creating a project with
the [KMM Plugin project wizard](https://plugins.jetbrains.com/plugin/14936-kotlin-multiplatform-mobile)
or cloning the [basic sample project](https://github.com/Kotlin/kmm-sample/).

<img src="/media/basic-structure.jpg"/>

## Architecture

Kotlin Multiplatform Mobile is a flexible technology that allows you to share only what you want to
share, from the core layer to UI layers.

This sample demonstrates sharing not only the data and domain layers of the app but also the
application state:

<img src="/media/top-level-arch.jpeg"/>

### Shared data and domain layers

There are two types of data sources. The network service is for getting RSS feed updates, while
local storage is for caching the feed, which makes it possible to use the application
offline. [Ktor HTTP Client](https://ktor.io/docs/client.html) is used for making API
requests. [Kotlinx.serialization](https://github.com/Kotlin/kotlinx.serialization) is used to
serialize feed data and store it locally
with [MultiplaformSettings](https://github.com/russhwolf/multiplatform-settings). This logic is
organized in the shared module of the `com.github.jetbrains.rssreader.core` package.

### Shared application state

The Redux pattern is used for managing the application state. The simplified Redux architecture is
implemented in the shared module. The `Store` class dispatches the **actions** that can be produced
either by a user or by some async work, and generates the new state. It stores the actual **state**
and facilitates subscription to state updates via Kotlin's `StateFlow`. To provide additional
information about state updates, the `Store` class also produces **effects** that, for example, can
be used to display this information via alerts. This logic is organized in the shared KMM module of
the `com.github.jetbrains.rssreader.app` package.

<img src="/media/arch-details.jpg"/>

### Native UI

The UI layer is fully native and implemented using SwiftUI for iOS, Jetpack Compose for Android,
Compose Multiplatform for Desktop and React.js for web browser.

**On the iOS side,** the `Store` from the KMM library is wrapped into the `ObservableObject` and
implements the state as a `@Published` wrapped property. This publishes changes whenever a
dispatched action produces a new state after being reduced in the shared module. The store is
injected as an `Environment Object` into the root view of the application, and is easily accessible
from anywhere in the application. SwiftUI performs all aspects of diffing on the render pass when
your state changes.

For subscribing to state
updates, [the simple wrapper](https://github.com/Kotlin/kmm-production-sample/blob/master/shared/src/iosMain/kotlin/com/github/jetbrains/rssreader/core/CFlow.kt)
is used. This wrapper allows you to provide a callback that will be called when each new value (the
state in our case) is emitted.

## React Web App

[Getting started with the react web app](/reactApp/README.md)

**On the Web side,** the `Store` from the KMM library is exposed through a top level object
declaration acting as view model. \
This is necessary because sealed classes used to represent dispatchable actions are not exportable
to JavaScript. \
In this view model we use `MainScope` to collect `StateFlow` updates and make them avalaible to
JavaScript by way of callback. Finally the state is consumed through the React Context API.

## Multiplatform features used

**✅ Platform-specific API usage.** RSS feeds usually only support the XML format.
The `kotlinx.serialization` library currently doesn't support parsing XML data, but there is no need
to implement your own parser. Instead, platform libraries (`XmlPullParser` for
Android, `NSXMLParser` for iOS and `DOMParser` for web) are used. The common `FeedParser` interface
is declared in the `commonMain` source set. Platform implementations are placed in the
corresponding `iOSMain`, `AndroidMain` and `jsMain` source sets. They are injected into the
RSSReader class (the KMM module entry point) via the `create` factory method, which is declared in
the [RSSReader class companion object](https://github.com/Kotlin/kmm-production-sample/blob/master/shared/src/androidMain/kotlin/com/github/jetbrains/rssreader/core/RssReader.kt).


