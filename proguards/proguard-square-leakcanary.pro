# LeakCanary specific rules #
# https://github.com/square/leakcanary

-keep class org.eclipse.mat.** { *; }
-keep class com.squareup.leakcanary.** { *; }
-keep class com.squareup.haha.** { *; }
-keep class sun.misc.** { *; }
-keep class sun.nio.** { *; }
-dontwarn com.squareup.leakcanary.**