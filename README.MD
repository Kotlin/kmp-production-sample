# KMM RSS Reader

<img src="/media/ios+android.png"/>


This is an open-sourced mobile crossplatform application built with [Koltin Multiplatform Mobile](https://kotlinlang.org/lp/mobile/). It's a simple RSS reader and you can download it on AppStore and Google Play. It's dedicated to demonstrating how KMM can be used in real production projects.


## Project structure

This repository contains a common Kotlin Multiplatform Module, Android project, and iOS project. The common module is connected with the Android project via Gradle multi-project mechanism. For use in iOS applications, the shared module compiles into a framework, which is exposed to the Xcode project via the `PackForXcode` Gradle task. This framework connects to the Xcode project that builds into an iOS application. 

You can achieve the same structure by creating a project with the [KMM Plugin project wizard](https://plugins.jetbrains.com/plugin/14936-kotlin-multiplatform-mobile) or cloning the [basic sample project](https://github.com/Kotlin/kmm-sample/).

<img src="/media/basic-project-structure.png"/>


## Architecture

Kotlin Multiplatform is a flexible technology that allows you to share only what you want to share, starting from the core layer ending with sharing UI layer.

This sample demonstrates sharing not only the data & domain layers of the app, but also the application state:

<img src="/media/top-level-arch.jpeg"/>


### Shared data & domain layers

There are two types of data sources: network service for getting RSS feed updates and local storage for caching feed to make it able to use the application offline. For making API requests [Ktor HTTP Client](https://ktor.io/docs/client.html) is used. [Kotlinx.serialization](https://github.com/Kotlin/kotlinx.serialization) is used to serialize feeds data and store it locally via [MultiplaformSettings](https://github.com/russhwolf/multiplatform-settings). This logic organized in the `com.github.jetbrains.rssreader.core` package in the shared module. 


### Shared application state

Redux pattern is used for managing application state. The simplified Redux architecture is implemented in the shared module. The `Store` class dispatches the **actions**, that can be produced by user actions or by some async work and generates the new state. It stores the actual **state** and allows to subscribe to the state updates via Kotlin **StateFlow**. To provide an additional information about the state updates, the `Store` class also produces **effects**, that can be used to display this information via alerts, for example. This logic organized in the `com.github.jetbrains.rssreader.app` package in the shared KMM module. 

<img src="/media/arch-details.jpeg"/>


### Native UI

The UI layer is fully native and implemented using SwiftUI for iOS and Jetpack Compose for Android. 

**On the iOS side,** the Store from the KMM library is wrapped into the `ObservableObject` and implements the state as a `@Published` wrapped property, so changes are published whenever a dispatched action produces a new state after being reduced in the shared module. The store is injected as an `Environment Object` in the root view of the application and is easily accessible anywhere in the application. SwiftUI does all aspects of diffing on the render pass when your state changes. 

For subscribing to state updates [the simple wrapper](https://github.com/terrakok/KmmRssReader/blob/master/shared/src/iosMain/kotlin/com/github/jetbrains/rssreader/CFlow.kt) is used, which allows you to provide a callback that will be called on each new value (the state in our case) emitted.

## Multiplatform features used

**✅ Platform-specific API usage.** RSS feeds usually support only XML format. kotlinx.serialization library currently doesn’t support parsing XML data, but there is no need to implement your own parser. Instead, platform libraries (`XmlPullParser` for Android and `NSXMLParser` for iOS) are used. The common interface `FeedParser` is declared in the `commonMain` source-set. Platform implementations are placed in `iOSMain` and `AndroidMain` source sets accordingly. They are injected into the RSSReader class (the KMM module entry point) via the `create` factory method, which is declared in the [RSSReader class companion object](https://github.com/terrakok/KmmRssReader/blob/master/shared/src/androidMain/kotlin/com/github/jetbrains/rssreader/RssReader.kt). 





