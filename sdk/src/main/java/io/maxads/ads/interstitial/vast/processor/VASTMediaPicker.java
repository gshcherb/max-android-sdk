//
//  MediaPicker.java
//
//  Created by Harsha Herur on 12/4/13.
//  Copyright (c) 2014 Nexage. All rights reserved.
//

package io.maxads.ads.interstitial.vast.processor;

import java.util.List;

import io.maxads.ads.interstitial.vast.model.VASTMediaFile;


public interface VASTMediaPicker {

	VASTMediaFile pickVideo(List<VASTMediaFile> list);

}
