package io.maxads.ads.interstitial.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import io.maxads.ads.interstitial.InterstitialBroadcastReceiver;

public class InterstitialActivity extends Activity {
  @NonNull public static final String HTML_KEY = "html_key";
  @NonNull public static final String BROADCAST_ID_KEY = "broadcast_id_key";

  @Nullable private String mHtml;
  private long mBroadcastId;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    final Intent intent = getIntent();
    mHtml = intent.getStringExtra(HTML_KEY);
    mBroadcastId = intent.getLongExtra(BROADCAST_ID_KEY, -1);
  }

  public void broadcastAction(long broadcastId, @NonNull String action) {
    Intent intent = new Intent(action);
    intent.putExtra(InterstitialBroadcastReceiver.BROADCAST_ID, broadcastId);
    LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
  }
}
