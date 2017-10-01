package io.maxads.ads.banner.presenter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import io.maxads.ads.base.MaxAds;
import io.maxads.ads.base.model.Ad;
import io.maxads.ads.base.view.HtmlWebView;
import io.maxads.ads.base.view.HtmlWebViewClient;

public class HtmlBannerPresenter implements BannerPresenter, View.OnClickListener {

  @NonNull private final HtmlWebView mHtmlWebView;
  @NonNull private final Ad mAd;
  @Nullable private BannerPresenter.Listener mListener;

  public HtmlBannerPresenter(@NonNull Context context, @NonNull Ad ad) {
    mAd = ad;
    mHtmlWebView = new HtmlWebView(context);
    mHtmlWebView.setWebViewClient(new HtmlWebViewClient(context));
    mHtmlWebView.setOnClickListener(this);
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
//    final String sampleBanner = "<!DOCTYPE html> <html> <head>  <!-- Adgroup is 365cd2475e074026b93da14103a36b97 -->  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, user-scalable=no\">  <style type='text/css'> .mp_center { position: fixed; top: 50%; left: 50%; margin-left: -160px !important; margin-top: -25px !important; } </style>  <script type=\"text/javascript\"> function mopubFinishLoad(){ setTimeout( function() { window.location = 'mopub://finishLoad' }, 0 ); } </script> <script type=\"text/javascript\"> if (trackImpressionHelper == null || typeof(trackImpressionHelper) != \"function\") { function trackImpressionHelper() { var urls = new Array(); var i = 0;  urls[i++]=\"http://ads.mopub.com/m/imp?appid=&cid=d06f9bde98134f76931cdf04951b60dd&city=Squamish&ckv=2&country_code=CA&cppck=C42E9&dev=Nexus%205X&exclude_adgroups=365cd2475e074026b93da14103a36b97&id=b195f8dd8ded45fe847ad89ed1d016da&is_mraid=0&os=Android&osv=7.1.2&req=12a62f2a5c1144b79df49d2102a11fd9&reqt=1506891984.0&rev=0.000050&udid=ifa%3A318aa2db-a4b8-4317-9ad9-22c7ab52c413&video_type=\";  var hiddenSpan = document.createElement('span'); hiddenSpan.style.display = 'none'; var i = 0; for (var i=0;i<urls.length;i++) { var img = document.createElement('img'); img.src = urls[i]; hiddenSpan.appendChild(img); } var body = document.getElementsByTagName('body')[0]; body.appendChild(hiddenSpan); } } </script>  <script type=\"text/javascript\"> function webviewDidClose(){ if (typeof webviewDidCloseHelper == 'function') { webviewDidCloseHelper(); } } function webviewDidAppear(){  if(typeof webviewDidAppearHelper == 'function') { webviewDidAppearHelper(); } } window.addEventListener(\"load\", function() { var links = document.getElementsByTagName('a'); for(var i=0; i < links.length; i++) { links[i].setAttribute('target','_blank'); } }, false);  </script>  </head> <body style=\"margin:0;padding:0;\">   <style> @-webkit-keyframes animatedBackground { from { background-position: 0 0; } to { background-position: 100% 0; } } @-webkit-keyframes slideIn { 0% { left: 0; } 45% { left: 0; } 50% { left: -256px; } 100% {left: -256px;} } body { padding: 0; margin: 0; font-family: \"HelveticaNeue-Light\", \"Helvetica Neue Light\", \"Helvetica Neue\", Helvetica, Arial, \"Lucida Grande\", sans-serif; } a { color: black; text-decoration: none; } footer, header, section { display: block; } #workspace { background-image: url(https://d30x8mtr3hjnzo.cloudfront.net/creatives/db3b0d3b952e4ad48a072406470cd4ba); background-position: 0 0; width: 320px; height: 50px; -webkit-animation: animatedBackground 10s linear infinite; position: relative; font-size: 20px; font-weight: 100; font-style: italic; overflow: hidden; } header { position: absolute; top: 0; left: 0; bottom: 0; width: 64px; background-color: #20ace5; background-image: url(https://d30x8mtr3hjnzo.cloudfront.net/creatives/cf054e77de8f4ddda768c8e0f814182b); } #scroller { width: 512px; overflow: hidden; line-height: 50px; height: 50px; position: absolute; right: 0; top: 0; left: 64px; } #scroller span { width: 256px; display: table-cell; text-align: center; position: relative; left: -256px; -webkit-animation: slideIn 4s linear; } #stars { position: relative; top: 3px; } </style> <body> <a href =\"https://www.mopub.com/click-test/\"> <div id=\"workspace\"> <header></header> <section id=\"scroller\"> <span id=\"frame-1\"> <img id=\"stars\" src=\"https://d30x8mtr3hjnzo.cloudfront.net/creatives/b41ccf68854e40d3915236a145d746e1\"/> Success!  </span> <span id=\"frame-3\"> Tap to test this ad.  </span> </section> </div> </a> </body> <script type=\"text/javascript\"> if (typeof htmlWillCallFinishLoad == \"undefined\" || !htmlWillCallFinishLoad) { if(typeof mopubFinishLoad == 'function') { window.onload = mopubFinishLoad; } }  if(typeof trackImpressionHelper == 'function') { trackImpressionHelper(); }  </script>  <script src=\"https://pixel.adsafeprotected.com/jload?anId=8260&pubId=108e64891ab140adbe7d2325510533e9&campId=Banner&chanId=b195f8dd8ded45fe847ad89ed1d016da&placementId=html\"></script>  </body> </html>\n";
    mHtmlWebView.loadDataWithBaseURL("http://" + MaxAds.HOST + "/", mAd.getCreative(), "text/html", "utf-8", null);
    if (mListener != null) {
      mListener.onBannerLoaded(this, mHtmlWebView);
    }
  }

  @Override
  public void destroy() {
    mHtmlWebView.destroy();
    mListener = null;
  }

  @Override
  public void onClick(View view) {
    if (mListener != null) {
      mListener.onBannerClicked(this);
    }
  }
}
