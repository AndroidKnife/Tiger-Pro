# RxBus 1.0.3

-keepattributes *Annotation*
-keepclassmembers class ** {
    @com.hwangjr.rxbus.annotation.Subscribe public *;
    @com.hwangjr.rxbus.annotation.Produce public *;
}