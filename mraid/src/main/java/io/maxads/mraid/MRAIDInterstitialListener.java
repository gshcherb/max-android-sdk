package io.maxads.mraid;

public interface MRAIDInterstitialListener {

	/******************************************************************************
	 * A listener for basic MRAIDInterstitial ad functionality.
	 ******************************************************************************/

	public void mraidInterstitialLoaded(MRAIDInterstitial mraidInterstitial);

	public void mraidInterstitialShow(MRAIDInterstitial mraidInterstitial);

	public void mraidInterstitialHide(MRAIDInterstitial mraidInterstitial);

}
