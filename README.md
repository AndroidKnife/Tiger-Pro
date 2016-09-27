# Tiger-Pro For Android

Sample Android app that i use at Tiger Application as a reference for new Android projects. It demonstrates the architecture, tools that i use when developing for the Android platform.

(Nowadays, i have used this pattern in at least 3 projects, it turns out to speed up the android development and make a cool application.)

Libraries and tools included:

- Support libraries
- RecyclerViews
- [RxJava](https://github.com/ReactiveX/RxJava) and [RxAndroid](https://github.com/ReactiveX/RxAndroid) 
- [RxBus](https://github.com/AndroidKnife/RxBus)
- [Retrofit 2](http://square.github.io/retrofit/)
- [OKHttp](https://github.com/square/okhttp)
- [Dagger 2](http://google.github.io/dagger/)
- [Butterknife](https://github.com/JakeWharton/butterknife)
- [Timber](https://github.com/JakeWharton/timber)
- [Glide](https://github.com/bumptech/glide)
- Functional tests with [Espresso](https://google.github.io/android-testing-support-library/docs/espresso/index.html)
- [Robolectric](http://robolectric.org/)
- [Mockito](http://mockito.org/)
- [Checkstyle](http://checkstyle.sourceforge.net/), [PMD](https://pmd.github.io/) and [Findbugs](http://findbugs.sourceforge.net/) for code analysis

## Requirements

- JDK 1.8
- [Android SDK](http://developer.android.com/sdk/index.html).
- Android N [(API 24) ](http://developer.android.com/tools/revisions/platforms.html).
- Latest Android SDK Tools and build tools.

## Architecture

This project follows Android architecture guidelines that are based on MVP. For more details, you can checkout [android-architecture](https://github.com/googlesamples/android-architecture)

### How to implement a new screen following MVP

Imagine you have to implement a sign in screen (Include an activity only). 

1. Create a new package under `ui` called `signin`
2. Create an new class called `SignInActivityView` that extends `ActivityView<SignInActivityViewPresenter>`.
3. Create a `SignInActivityViewPresenter` class that extends `ActivityPresenter<SignInActivityView>`
4. Implement the methods in `SignInActivityView` and `SignInActivityViewPresenter`, add functions that your Activity requires to perform the necessary actions, e.g. `signIn(String email)`. Once the sign in action finishes you should call `view.showSignInSuccessful()` in presenter.
5. Create a `SignInActivityViewPresenterTest`and write unit tests for `signIn(email)`.
6. Fragments also provide the same workflow.

**From above, you should have `SignInActivityView` and `SignInActivityViewPresenter` in your `ui.signin` package to define your MVP view.**

## Code Quality

This project include code analysis tools and unit test tools, **but still working on unit test and functional test for this project.** 

### Code Analysis tools 

The following code analysis tools are set up on this project:

* [PMD](https://pmd.github.io/): It finds common programming flaws like unused variables, empty catch blocks, unnecessary object creation, and so forth. See [this project's PMD ruleset](config/quality/pmd/ruleset.xml).

``` 
./gradlew pmd
```

* [Findbugs](http://findbugs.sourceforge.net/): This tool uses static analysis to find bugs in Java code. Unlike PMD, it uses compiled Java bytecode instead of source code.

```
./gradlew findbugs
```

* [Checkstyle](http://checkstyle.sourceforge.net/): It ensures that the code style follows your team development guidelines. See [checkstyle config file](config/quality/checkstyle/checkstyle.xml).

```
./gradlew checkstyle
```

### The check task

To ensure that your code is valid and stable use check: 

```
./gradlew check
```

This will run checkstyle -> findbugs -> PMD -> Android Lint -> Unit Test. All Done!

## New project setup 

To quickly start a new project, you can:

1. Keep the `mvp` package and rename the `tiger` package to your application.
2. Modify package in `build.gradle` and `AndroidManifest.xml`.
3. Add sign config to project.
4. Add proguard files to project, if it's the library proguard file, you should put it in `proguards` folder.
5. If you want to enable jni support, checkout `build.gradle` for more details.
6. All Done! Enjoy your development.

## TODO List

1. Unit Test For Project
2. Project Template for Android Studio IDE
3. Usage Examples For New Project

## FAQ
**Q:** How to do pull requests?<br/>
**A:** Ensure good code quality and consistent formatting.

## License

```
    Copyright 2016 HwangJR, Inc.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
```