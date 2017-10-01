package io.maxads.ads.interstitial.presenter;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Random;

import io.maxads.ads.base.model.Ad;
import io.maxads.ads.interstitial.InterstitialBroadcastReceiver;
import io.maxads.ads.interstitial.activity.InterstitialActivity;

public class HtmlInterstitialPresenter implements InterstitialPresenter {
  @NonNull private final Context mContext;
  @NonNull private final Ad mAd;
  private final long mBroadcastId;

  @Nullable private InterstitialBroadcastReceiver mInterstitialBroadcastReceiver;
  @Nullable private InterstitialPresenter.Listener mListener;

  public HtmlInterstitialPresenter(@NonNull Context context, @NonNull Ad ad) {
    mContext = context;
    mAd = ad;
    mBroadcastId = new Random().nextLong();
  }

  @Override
  public void setListener(@Nullable Listener listener) {
    mListener = listener;
  }

  @NonNull
  @Override
  public Ad getAd() {
    return mAd;
  }

  @Override
  public void load() {
    mInterstitialBroadcastReceiver = new InterstitialBroadcastReceiver(mContext, this, mBroadcastId);
    mInterstitialBroadcastReceiver.setListener(mListener);

    final IntentFilter intentFilter = new IntentFilter();
    intentFilter.addAction(InterstitialBroadcastReceiver.INTERSTITIAL_SHOW);
    intentFilter.addAction(InterstitialBroadcastReceiver.INTERSTITIAL_CLICK);
    intentFilter.addAction(InterstitialBroadcastReceiver.INTERSTITIAL_DISMISS);
    intentFilter.addAction(InterstitialBroadcastReceiver.INTERSTITIAL_ERROR);

    mInterstitialBroadcastReceiver.register(intentFilter);

    if (mListener != null) {
      mListener.onInterstitialLoaded(this);
    }
  }

  @Override
  public void show() {
    final Intent intent = new Intent(mContext, InterstitialActivity.class);
//    final String sampleInterstitial = "<!DOCTYPE html> <html> <head>  <!-- Adgroup is 365cd2475e074026b93da14103a36b97 -->  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, user-scalable=no\">  <style type='text/css'> .mp_center { position: fixed; top: 50%; left: 50%; margin-left: -0px !important; margin-top: -0px !important; } </style>  <script type=\"text/javascript\"> function mopubFinishLoad(){ setTimeout( function() { window.location = 'mopub://finishLoad' }, 0 ); } </script> <script type=\"text/javascript\"> if (trackImpressionHelper == null || typeof(trackImpressionHelper) != \"function\") { function trackImpressionHelper() { var urls = new Array(); var i = 0;  var hiddenSpan = document.createElement('span'); hiddenSpan.style.display = 'none'; var i = 0; for (var i=0;i<urls.length;i++) { var img = document.createElement('img'); img.src = urls[i]; hiddenSpan.appendChild(img); } var body = document.getElementsByTagName('body')[0]; body.appendChild(hiddenSpan); } } </script>  <script type=\"text/javascript\"> function webviewDidClose(){ if (typeof webviewDidCloseHelper == 'function') { webviewDidCloseHelper(); } } function webviewDidAppear(){  if(typeof trackImpressionHelper == 'function') { trackImpressionHelper(); }  if(typeof webviewDidAppearHelper == 'function') { webviewDidAppearHelper(); } } window.addEventListener(\"load\", function() { var links = document.getElementsByTagName('a'); for(var i=0; i < links.length; i++) { links[i].setAttribute('target','_blank'); } }, false);  </script>  </head> <body style=\"margin:0;padding:0;\"> <!DOCTYPE html> <html> <head></head> <style> @-webkit-keyframes animatedBackground { from { background-position: 0 0; } to { background-position: 100% 0; } } @-webkit-keyframes fadeIn { from { opacity: 0; max-width: 50%; } to { opacity: 1; max-width: 100%; } } body, html { padding: 0; margin: 0; font-family: \"HelveticaNeue-Light\", \"Helvetica Neue Light\", \"Helvetica Neue\", Helvetica, Arial, \"Lucida Grande\", sans-serif; height: 100%; } a { color: black; text-decoration: none; } footer, header, section { display: block; } #workspace { background-image: url(https://d30x8mtr3hjnzo.cloudfront.net/creatives/c45945f4063a4789a79aec907bcb04cc); background-position: 0 0; position: absolute; top: 0; bottom: 0; left: 0; right: 0; width: 100%; height: 100%; -webkit-animation: animatedBackground 20s linear infinite; } header { position: absolute; top: 0; left: 0; right: 0; height: 100px; line-height: 100px; background-color: #20ace5; text-transform: capitalize; color: white; text-align: center; font-size: 40px; } footer { position: absolute; bottom: 0; left: 0; right: 0; text-align: center; padding-bottom: 18px; } footer p { font-size: 24px; margin-bottom: 12px; font-style: italic; font-weight: 100; } .centered { max-width: 100%; height: auto; overflow: auto; margin: auto; position: absolute; top: 0; left: 0; bottom: 0; right: 0; } #star-circle { } #nice { -webkit-animation: fadeIn 1s ease-in; } </style> <body> <a href=\"https://www.mopub.com/click-test/\"> <div id=\"workspace\"> <header> Success!  </header> <img id=\"star-circle\" class=\"centered\" src=\"https://d30x8mtr3hjnzo.cloudfront.net/creatives/e92584303dbf4f7e908f663060f3b07b\" /> <img id=\"nice\" class=\"centered\" src=\"https://d30x8mtr3hjnzo.cloudfront.net/creatives/6d4b7ded21fd41428ff2f0670ca3ba3f\"/> <footer> <p>Tap to test this ad.</p> <img id=\"mopub\" src=\"https://d30x8mtr3hjnzo.cloudfront.net/creatives/2a7466458cfd4c70adb556da05587270\"> </footer> </div> </a></body> </html> <script type=\"text/javascript\"> if (typeof htmlWillCallFinishLoad == \"undefined\" || !htmlWillCallFinishLoad) { if(typeof mopubFinishLoad == 'function') { window.onload = mopubFinishLoad; } }  </script>  </body> </html>\n";
    intent.putExtra(InterstitialActivity.HTML_KEY, mAd.getCreative());
    intent.putExtra(InterstitialActivity.BROADCAST_ID_KEY, mBroadcastId);
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    mContext.startActivity(intent);
  }

  @Override
  public void destroy() {
    if (mInterstitialBroadcastReceiver != null) {
      mInterstitialBroadcastReceiver.unregister();
    }
  }
}
