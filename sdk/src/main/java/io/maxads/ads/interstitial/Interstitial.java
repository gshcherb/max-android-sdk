package io.maxads.ads.interstitial;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;

import io.maxads.ads.base.api.RequestManager;
import io.maxads.ads.base.model.Ad;
import io.maxads.ads.base.util.Checks;
import io.maxads.ads.base.util.InitializationHelper;
import io.maxads.ads.interstitial.presenter.InterstitialPresenter;
import io.maxads.ads.interstitial.presenter.InterstitialPresenterFactory;

public class Interstitial implements RequestManager.RequestListener, InterstitialPresenter.Listener {

  public interface Listener {
    void onInterstitialLoaded(@NonNull Interstitial interstitial);
    void onInterstitialShown(@NonNull Interstitial interstitial);
    void onInterstitialClicked(@NonNull Interstitial interstitial);
    void onInterstitialDismissed(@NonNull Interstitial interstitial);
    void onInterstitialError(@NonNull Interstitial interstitial);
  }

  @NonNull private final InterstitialPresenterFactory mInterstitialPresenterFactory;
  @NonNull private final RequestManager mRequestManager;
  @NonNull private InitializationHelper mInitializationHelper;
  @Nullable private InterstitialPresenter mInterstitialPresenter;
  @Nullable private Listener mListener;
  private boolean mIsDestroyed;

  public Interstitial(@NonNull Activity activity) {
    this(new InterstitialPresenterFactory(activity), new RequestManager(), new InitializationHelper());
  }

  @VisibleForTesting
  Interstitial(@NonNull InterstitialPresenterFactory interstitialPresenterFactory,
               @NonNull RequestManager requestManager,
               @NonNull InitializationHelper initializationHelper) {
    mInterstitialPresenterFactory = interstitialPresenterFactory;
    mRequestManager = requestManager;
    mRequestManager.setRequestListener(this);
    mInitializationHelper = initializationHelper;
  }

  public void setListener(@Nullable Listener listener) {
    mListener = listener;
  }

  public void load(@NonNull String adUnitId) {
    if (!Checks.NoThrow.checkArgument(mInitializationHelper.isInitialized(), "MaxAds SDK has not been initialized. " +
      "Please call MaxAds#initialize in your application's onCreate method.")) {
      return;
    }

    if (!Checks.NoThrow.checkNotNull(adUnitId, "adUnitId cannot be null")) {
      return;
    }

    if (!Checks.NoThrow.checkArgument(!mIsDestroyed, "Interstitial has been destroyed")) {
      return;
    }

    mRequestManager.setAdUnitId(adUnitId);
    mRequestManager.requestAd();
  }

  @VisibleForTesting
  void loadInterstitial(@NonNull Ad ad) {
    if (mIsDestroyed) {
      return;
    }

    mInterstitialPresenter = mInterstitialPresenterFactory.createInterstitialPresenter(ad, this);
    if (mInterstitialPresenter == null) {
      if (mListener != null) {
        mListener.onInterstitialError(this);
      }
      return;
    }

    mInterstitialPresenter.load();
  }

  public void show() {
    if (mInterstitialPresenter == null) {
      return;
    }

    mInterstitialPresenter.show();
  }

  public void destroy() {
    mRequestManager.destroy();
    if (mInterstitialPresenter != null) {
      mInterstitialPresenter.destroy();
    }
    mInterstitialPresenter = null;
    mListener = null;
    mIsDestroyed = true;
  }

  // RequestManager.RequestListener
  @Override
  public void onRequestSuccess(@NonNull Ad ad) {
    if (mIsDestroyed) {
      return;
    }

    loadInterstitial(ad);
  }

  @Override
  public void onRequestFail(@NonNull Throwable throwable) {
    if (mIsDestroyed) {
      return;
    }

    if (mListener != null) {
      mListener.onInterstitialError(this);
    }
  }

  // Interstitial.Listener
  @Override
  public void onInterstitialLoaded(@NonNull InterstitialPresenter interstitialPresenter) {
    if (mIsDestroyed) {
      return;
    }

    if (mListener != null) {
      mListener.onInterstitialLoaded(this);
    }
  }

  @Override
  public void onInterstitialShown(@NonNull InterstitialPresenter interstitialPresenter) {
    if (mIsDestroyed) {
      return;
    }

    if (mListener != null) {
      mListener.onInterstitialShown(this);
    }
  }

  @Override
  public void onInterstitialClicked(@NonNull InterstitialPresenter interstitialPresenter) {
    if (mIsDestroyed) {
      return;
    }

    if (mListener != null) {
      mListener.onInterstitialClicked(this);
    }
  }

  @Override
  public void onInterstitialDismissed(@NonNull InterstitialPresenter interstitialPresenter) {
    if (mIsDestroyed) {
      return;
    }

    if (mInterstitialPresenter != null) {
      mInterstitialPresenter.destroy();
      mInterstitialPresenter = null;
    }

    if (mListener != null) {
      mListener.onInterstitialDismissed(this);
    }
  }

  @Override
  public void onInterstitialError(@NonNull InterstitialPresenter interstitialPresenter) {
    if (mIsDestroyed) {
      return;
    }

    if (mInterstitialPresenter != null) {
      mInterstitialPresenter.destroy();
      mInterstitialPresenter = null;
    }

    if (mListener != null) {
      mListener.onInterstitialError(this);
    }
  }
}
