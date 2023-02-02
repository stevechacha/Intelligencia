# Intelligencia

### CREDIT SCORING FOR NON CUSTOMER

The goal of this solution is to assess customers credit worthiness utilising Android data. The output shall include a score and a limit


#### 1. EXTRACTING ANDROID DATA USING APP
-SMS/MMS

-Call logs

-Installed android application

-Contacts

-System logs and informatio( on-progress)

##### 2. DATA PREPROCESSING
-Data cleaning

-Feature engineering

-Missing Data treatment

-Feature selection

-Outlier treatment

### 3. MODELING

##### Some features used in scoring

Total Credit - Total amount of money received for the last 6 months

Total Debit -  Total amount of money spent for the last 6 months

Total debt - Total amount of money borrowed for the last 6 months

Financial applications - The count of financial apps in the android phone 

99th quantile credit amount - 99th percentile of all credit transactions 

### 4. Backened
Used Python ( Flask  for Api)


Here are some of the Screenshot of the App


![image](https://user-images.githubusercontent.com/48447675/199010482-961857fa-5ced-4e25-a84b-ec7b3924a93d.png)


![image](https://user-images.githubusercontent.com/48447675/199010626-4033e891-aebc-42ab-a2f9-cea41b957864.png)

## Tech Stack.
- [Kotlin](https://developer.android.com/kotlin) - Kotlin is a programming language that can run on JVM. Google has announced Kotlin as one of its officially supported programming languages in Android Studio; and the Android community is migrating at a pace from Java to Kotlin.
- Jetpack components:
  - [Android KTX](https://developer.android.com/kotlin/ktx.html) - Android KTX is a set of Kotlin extensions that are included with Android Jetpack and other Android libraries. KTX extensions provide concise, idiomatic Kotlin to Jetpack, Android platform, and other APIs.
  - [AndroidX](https://developer.android.com/jetpack/androidx) - Major improvement to the original Android [Support Library](https://developer.android.com/topic/libraries/support-library/index), which is no longer maintained.
  - [Lifecycle](https://developer.android.com/topic/libraries/architecture/lifecycle) - Lifecycle-aware components perform actions in response to a change in the lifecycle status of another component, such as activities and fragments. These components help you produce better-organized, and often lighter-weight code, that is easier to maintain.
  - [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) -The ViewModel class is designed to store and manage UI-related data in a lifecycle conscious way.
  - [LiveData](https://developer.android.com/topic/libraries/architecture/livedata) - LiveData is an observable data holder class. Unlike a regular observable, LiveData is lifecycle-aware, meaning it respects the lifecycle of other app components, such as activities, fragments, or services. This awareness ensures LiveData only updates app component observers that are in an active lifecycle state.
  - [Room database](https://developer.android.com/training/data-storage/room) - The Room persistence library provides an abstraction layer over SQLite to allow fluent database access while harnessing the full power of SQLite.

- [Kotlin Coroutines](https://developer.android.com/kotlin/coroutines) - A concurrency design pattern that you can use on Android to simplify code that executes asynchronously.
- [Retrofit](https://square.github.io/retrofit) -  Retrofit is a REST client for Java/ Kotlin and Android by Square inc under Apache 2.0 license. Its a simple network library that is used for network transactions. By using this library we can seamlessly capture JSON response from web service/web API.
- [GSON](https://github.com/square/gson) - JSON Parser,used to parse requests on the data layer for Entities and understands Kotlin non-nullable and default parameters.
- [Logging Interceptor](https://github.com/square/okhttp/blob/master/okhttp-logging-interceptor/README.md) -  logs HTTP request and response data.
- [Coil](https://coil-kt.github.io/coil/compose/)- An image loading library for Android backed by Kotlin Coroutines.
- [Timber](https://github.com/JakeWharton/timber)- A logger with a small, extensible API which provides utility on top of Android's normal Log class.

