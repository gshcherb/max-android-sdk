package io.maxads.ads.base.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.annotation.NonNull;

import java.util.List;

public class IntentHandlerDelegate {
  @NonNull private final Context mContext;

  public IntentHandlerDelegate(@NonNull Context context) {
    mContext = context;
  }

  public boolean canHandleIntent(@NonNull Intent intent) {
    final PackageManager packageManager = mContext.getPackageManager();
    final List<ResolveInfo> resolveInfos = packageManager.queryIntentActivities(intent, 0);
    return !resolveInfos.isEmpty();
  }

  public boolean handleDeepLink(@NonNull Uri uri) {
    final Intent intent = new Intent(Intent.ACTION_VIEW);
    intent.setData(uri);
    final boolean canHandleIntent = canHandleIntent(intent);
    if (canHandleIntent) {
      mContext.startActivity(intent);
    }
    return canHandleIntent;
  }
}
