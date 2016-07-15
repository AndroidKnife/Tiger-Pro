package com.hwangjr.mvp;

import android.app.Application;
import android.content.Context;
import android.os.StrictMode;
import android.util.Log;

import com.hwangjr.mvp.di.components.AppComponent;
import com.hwangjr.mvp.di.components.DaggerAppComponent;
import com.hwangjr.mvp.di.modules.AppModule;
import com.hwangjr.mvp.utils.LoggerUtils;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import timber.log.Timber;

public class MVPApplication extends Application {
    private static MVPApplication sInstance;
    private AppComponent appComponent;

    private RefWatcher refWatcher;

    @Override
    public void onCreate() {
        super.onCreate();
        this.sInstance = this;
        setupComponent();

        refWatcher = LeakCanary.install(this);
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());

            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build());
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .penaltyDeathOnNetwork()
                    .build());
        } else {
            Timber.plant(new CrashReportTree());
        }
    }

    private void setupComponent() {
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    @Override
    public void onLowMemory() {
        android.os.Process.killProcess(android.os.Process.myPid());
        super.onLowMemory();
    }

    public void exit() {
        System.gc();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    public static MVPApplication getInstance() {
        return sInstance;
    }

    public static AppComponent getAppComponent() {
        return sInstance.appComponent;
    }

    public static RefWatcher getRefWatcher(Context context) {
        MVPApplication application = (MVPApplication) context.getApplicationContext();
        return application.refWatcher;
    }

    /**
     * A tree which logs important information for crash reporting.
     */
    private static class CrashReportTree extends Timber.DebugTree {
        @Override
        protected void log(int priority, String tag, String message, Throwable t) {
            if (priority == Log.ERROR && t != null) {
                String crashLog = Log.getStackTraceString(t);
                StringBuilder sb = new StringBuilder(message + "\n");
                sb.append(crashLog);
                LoggerUtils.writeCrashLog(sb.toString());
                super.log(priority, tag, sb.toString(), t);
            }
        }
    }
}
