package com.smallraw.library.core.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public final class AppUtils {
  @SuppressLint("StaticFieldLeak")
  private static Application sApplication;
  private static final ActivityLifecycleImpl ACTIVITY_LIFECYCLE = new ActivityLifecycleImpl();

  private AppUtils() {
    throw new UnsupportedOperationException("u can't instantiate me...");
  }

  /**
   * Init utils.
   * <p>Init it in the class of Application.</p>
   *
   * @param app Application
   */
  public static void init(final Application app) {
    if (sApplication == null) {
      if (app == null) {
        sApplication = getApplicationByReflect();
      } else {
        sApplication = app;
      }
    }
    sApplication.registerActivityLifecycleCallbacks(ACTIVITY_LIFECYCLE);
  }

  /**
   * Init utils.
   * <p>Init it in the class of Application.</p>
   *
   * @param context Context
   */
  public static void init(final Context context) {
    if (context == null) {
      init(getApplicationByReflect());
      return;
    }
    init((Application) context.getApplicationContext());
  }

  public static Application getApp() {
    if (sApplication != null) return sApplication;
    Application app = getApplicationByReflect();
    init(app);
    return app;
  }

  public static Context getApplicationContext() {
    return getApp().getApplicationContext();
  }

  public static void destroyAllActivities() {
    ACTIVITY_LIFECYCLE.destroyAllActivities();
  }

  public static void destroyActivity(Class<Activity> aClass) {
    ACTIVITY_LIFECYCLE.destroyActivity(aClass);
  }

  private static Application getApplicationByReflect() {
    try {
      @SuppressLint("PrivateApi")
      Class<?> activityThread = Class.forName("android.app.ActivityThread");
      Object thread = activityThread.getMethod("currentActivityThread").invoke(null);
      Object app = activityThread.getMethod("getApplication").invoke(thread);
      if (app == null) {
        throw new NullPointerException("u should init first");
      }
      return (Application) app;
    } catch (NoSuchMethodException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
    throw new NullPointerException("u should init first");
  }

  static class ActivityLifecycleImpl implements Application.ActivityLifecycleCallbacks {

    final LinkedList<Activity> mActivityList = new LinkedList<>();
    final HashMap<Object, OnAppStatusChangedListener> mStatusListenerMap = new HashMap<>();

    private int mForegroundCount = 0;
    private int mConfigCount = 0;

    void addListener(final Object object, final OnAppStatusChangedListener listener) {
      mStatusListenerMap.put(object, listener);
    }

    void removeListener(final Object object) {
      mStatusListenerMap.remove(object);
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
      setTopActivity(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {
      setTopActivity(activity);
      if (mForegroundCount <= 0) {
        postStatus(true);
      }
      if (mConfigCount < 0) {
        ++mConfigCount;
      } else {
        ++mForegroundCount;
      }
    }

    @Override
    public void onActivityResumed(Activity activity) {
      setTopActivity(activity);
    }

    @Override
    public void onActivityPaused(Activity activity) {/**/}

    @Override
    public void onActivityStopped(Activity activity) {
      if (activity.isChangingConfigurations()) {
        --mConfigCount;
      } else {
        --mForegroundCount;
        if (mForegroundCount <= 0) {
          postStatus(false);
        }
      }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {/**/}

    @Override
    public void onActivityDestroyed(Activity activity) {
      mActivityList.remove(activity);
    }

    private void postStatus(final boolean isForeground) {
      if (mStatusListenerMap.isEmpty()) return;
      for (OnAppStatusChangedListener onAppStatusChangedListener : mStatusListenerMap.values()) {
        if (onAppStatusChangedListener == null) return;
        if (isForeground) {
          onAppStatusChangedListener.onForeground();
        } else {
          onAppStatusChangedListener.onBackground();
        }
      }
    }

    private void setTopActivity(final Activity activity) {
//      if (activity.getClass() == PermissionUtils.PermissionActivity.class) return;
      if (mActivityList.contains(activity)) {
        if (!mActivityList.getLast().equals(activity)) {
          mActivityList.remove(activity);
          mActivityList.addLast(activity);
        }
      } else {
        mActivityList.addLast(activity);
      }
    }

    Activity getTopActivity() {
      if (!mActivityList.isEmpty()) {
        final Activity topActivity = mActivityList.getLast();
        if (topActivity != null) {
          return topActivity;
        }
      }
      Activity topActivityByReflect = getTopActivityByReflect();
      if (topActivityByReflect != null) {
        setTopActivity(topActivityByReflect);
      }
      return topActivityByReflect;
    }

    private Activity getTopActivityByReflect() {
      try {
        @SuppressLint("PrivateApi")
        Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
        Object activityThread = activityThreadClass.getMethod("currentActivityThread").invoke(null);
        Field activitiesField = activityThreadClass.getDeclaredField("mActivityList");
        activitiesField.setAccessible(true);
        Map activities = (Map) activitiesField.get(activityThread);
        if (activities == null) return null;
        for (Object activityRecord : activities.values()) {
          Class activityRecordClass = activityRecord.getClass();
          Field pausedField = activityRecordClass.getDeclaredField("paused");
          pausedField.setAccessible(true);
          if (!pausedField.getBoolean(activityRecord)) {
            Field activityField = activityRecordClass.getDeclaredField("activity");
            activityField.setAccessible(true);
            return (Activity) activityField.get(activityRecord);
          }
        }
      } catch (ClassNotFoundException e) {
        e.printStackTrace();
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      } catch (InvocationTargetException e) {
        e.printStackTrace();
      } catch (NoSuchMethodException e) {
        e.printStackTrace();
      } catch (NoSuchFieldException e) {
        e.printStackTrace();
      }
      return null;
    }

    public void destroyAllActivities() {
      for (Activity activity : mActivityList) {
        activity.finish();
      }
    }

    public void destroyActivity(Class<Activity> aClass) {
      for (Activity activity : mActivityList) {
        if (activity.getClass() == aClass) {
          activity.finish();
        }
      }
    }
  }

  public interface OnAppStatusChangedListener {
    void onForeground();

    void onBackground();
  }
}
