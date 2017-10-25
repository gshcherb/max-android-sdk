# max-android-sdk
MAX Advertising Systems Android SDK

[![Build Status](https://travis-ci.com/MAXAds/max-android-sdk.svg?token=TpV16HrJqGHqqWgsCvCc&branch=master)](https://travis-ci.com/MAXAds/max-android-sdk)

## Overview

MAX is a simple parallel bidding mechanism, which works by running an auction before you call your primary ad waterfall. This "pre-bid" phase allows more than one exchange or programmatic buyer to establish a price for your inventory and then compete at various points in your waterfall.

It works as follows:

1. Your app calls MAX to prepare and send a pre-bid request to the MAX backend.
2. MAX contacts all of your configured ad sources and collects bids from each.
3. The bids are compared and the highest bid that is compliant with your targeting and other rules is selected.
4. MAX returns the pre-bid results and a set of keywords.
5. Your app calls your ad server/SSP normally, attaching the keywords that were returned. The keywords are designed to match specific line items in your waterfall, which can be set up to trigger only if a certain price is met.
6. If a line item matches the keywords, the ad server/SSP hands off to the MAX custom event. The custom event then shows the ad returned in the pre-bid from the partner exchange or buyer.
7. If nothing matches a MAX line item, then your waterfall proceeds normally.

In this way, you can increase competition and take control of the exchange layer in your ad waterfall without disrupting
the way your ads are served currently.

## Banner Integration

It is simple to wrap your existing banner ad view with MAX using the MAX RequestManager. MAX will handle all auto-refresh and error retry logic, so you should disable these features on your SSP. Example integration with MoPub:

``` mMoPubView.setAutorefreshEnabled(false);
    mRequestManager.setAdUnitId(MAX_BANNER_ADUNIT_ID);
    mRequestManager.setRequestListener(new RequestManager.RequestListener() {
      @Override
      public void onRequestSuccess(@NonNull Ad ad) {
        mAd = ad;
        mMoPubView.setAdUnitId(MOPUB_BANNER_ADUNIT_ID);
        mMoPubView.setKeywords(ad.getPrebidKeywords());
        mMoPubView.loadAd();
      }

      @Override
      public void onRequestFail(@NonNull Throwable throwable) {
        mRequestManager.startRefreshTimer(RequestManager.DEFAULT_REFRESH_TIME_SECONDS);
      }
    });

    mMoPubView.setBannerAdListener(new MoPubView.BannerAdListener() {
      @Override
      public void onBannerLoaded(MoPubView banner) {
        mRequestManager.startRefreshTimer(mAd.getRefreshTimeSeconds());
      }

      @Override
      public void onBannerFailed(MoPubView banner, MoPubErrorCode errorCode) {
        mRequestManager.startRefreshTimer(mAd.getRefreshTimeSeconds());
      }

      @Override
      public void onBannerClicked(MoPubView banner) {
      }

      @Override
      public void onBannerExpanded(MoPubView banner) {
      }

      @Override
      public void onBannerCollapsed(MoPubView banner) {
      }
    });
    
    mRequestManager.requestAd();
```

## Interstitial Integration

Interstitials work similarly to the above. None of your other interstitial display logic needs to change. Example integration with MoPub:

``` mMoPubInterstitial = new MoPubInterstitial(this, MOPUB_INTERSTITIAL_ADUNIT_ID);
    mRequestManager.setAdUnitId(MAX_INTERSTITIAL_ADUNIT_ID);
    mRequestManager.setRequestListener(new RequestManager.RequestListener() {
      @Override
      public void onRequestSuccess(@NonNull Ad ad) {
        mAd = ad;
        mMoPubInterstitial.setKeywords(ad.getPrebidKeywords());
        mMoPubInterstitial.load();
      }

      @Override
      public void onRequestFail(@NonNull Throwable throwable) {
      }
    });

    mMoPubInterstitial.setInterstitialAdListener(new MoPubInterstitial.InterstitialAdListener() {
      @Override
      public void onInterstitialLoaded(MoPubInterstitial interstitial) {
        mMoPubInterstitial.show();
      }

      @Override
      public void onInterstitialFailed(MoPubInterstitial interstitial, MoPubErrorCode errorCode) {
      }

      @Override
      public void onInterstitialShown(MoPubInterstitial interstitial) {
      }

      @Override
      public void onInterstitialClicked(MoPubInterstitial interstitial) {
      }

      @Override
      public void onInterstitialDismissed(MoPubInterstitial interstitial) {
      }
    });
  
    mRequestManager.requestAd();
```

## Ad Server/SSP Setup

### Pre-bid Keywords 

MAX works by triggering line items in your SSP/ad server waterfall using keyword targeting. The keywords returned from
the MAX pre-bid system for a $0.26 bid look like this:

```
	m_max:true,max_bidX:000,max_bidXX:020,max_bidXXX:026
```

This structure allows you to set price increments in your SSP at the dollar, ten cent or single cent level. 

### Line Item Custom Events

To create a MAX line item:

1. Create a new line item of type Custom Native 
2. Set the Custom Event Class to the appropriate value depending on their type: `MaxMoPubBannerCustomEvent` or `MAXMoPubInterstitialCustomEvent`.
3. Set the Custom Event Info to `{"adunit_id": "<MAX_ADUNIT_ID>"}` where the value of `MAX_ADUNIT_ID` corresponds to the ID of the MAX ad unit for this request.

## Debugging
Use Chrome Developer Tools (chrome://inspect) to view network requests, debug view heirarchies, debug web views
* [Stetho](http://facebook.github.io/stetho/) - Sophisticated debug bridge for Android applications accessed using Chrome Developer Tools

Configuration:

In your app's build.gradle
``` 
compile 'com.facebook.stetho:stetho:1.5.0'
compile 'com.facebook.stetho:stetho-okhttp3:1.5.0'
```

In your app's Application file
```
MaxAds.initialize(this,
  Collections.<Interceptor>emptyList(),
  Collections.<Interceptor>singletonList(new StethoInterceptor()));
```

* [OkHttp3 Logging Interceptor](https://github.com/square/okhttp/tree/master/okhttp-logging-interceptor) - logs HTTP request and response data to Android Studio Logcat

Configuration:

In your app's build.gradle
``` 
compile 'com.squareup.okhttp3:logging-interceptor:3.8.1'
```

In your app's Application file
```
MaxAds.initialize(this,
  Collections.<Interceptor>singletonList(new HttpLoggingInterceptor().setLevel(level)),
  Collections.<Interceptor>emptyList());
```

* MRAID Log Level

```
MRAIDLog.setLoggingLevel(logLevel);
```

* VAST Log Level

```
VASTLog.setLoggingLevel(logLevel);
```

* Enable strict mode. SDK will force crash when supplied with invalid arguments to certain methods.

```
Checks.NoThrow.setStrictMode(true);
```

## Developers
### Patterns
* [Model View Presenter](https://en.wikipedia.org/wiki/Model%E2%80%93view%E2%80%93presenter)

### Tools
* [Genymotion](https://www.genymotion.com/) - Android emulation platform

### Libraries
Android

* [Android Support V4](https://developer.android.com/topic/libraries/support-library/index.html) - Backwards compatibility library for new Android features
* [Android Support Annotations](https://developer.android.com/studio/write/annotations.html) - Provide hints to code inspection tools to catch issues before run time

RxJava

* [RxJava2](https://github.com/ReactiveX/RxJava) - Asynchronous and event-based programming
* [RxAndroid](https://github.com/ReactiveX/RxAndroid) - RxJava bindings for Android

Network

* [Retrofit](http://square.github.io/retrofit/) - A type-safe HTTP client for Android and Java
* [OkHttp](http://square.github.io/okhttp/) - An HTTP & HTTP/2 client for Android and Java applications
* [GSON](https://github.com/google/gson) - A Java serialization/deserialization library to convert Java Objects into JSON and back

Testing

* [Robolectric](http://robolectric.org/) - Run unit tests quickly in the JVM instead of an emulator / device
* [Mockito](http://site.mockito.org/) - Mocking framework

## Author

MAX, hello@maxads.co

## License

MAX is available under the MIT license. See the LICENSE file for more info.
