package io.maxads.test_app;

import android.content.res.AssetManager;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.maxads.test_app.model.TestAdItem;

public class TestAdReader {
  @NonNull private static final String TAG = TestAdReader.class.getSimpleName();
  @NonNull private final AssetManager mAssetManager;
  @NonNull private final String mTestAdsPath;

  public TestAdReader(@NonNull AssetManager assetManager, @NonNull String testAdsPath) {
    mAssetManager = assetManager;
    mTestAdsPath = testAdsPath;
  }

  @NonNull
  public List<TestAdItem> getTestAdItems() {
    String[] filenames;
    try {
      filenames = mAssetManager.list(mTestAdsPath);
    } catch (IOException e) {
      Log.e(TAG, e.getMessage(), e);
      return Collections.emptyList();
    }

    final List<TestAdItem> testAdItems = new ArrayList<>(filenames.length);
    for (String filename : filenames) {
      try {
        final InputStream is = mAssetManager.open(mTestAdsPath + File.separator + filename);
        final BufferedReader bufferReader = new BufferedReader(new InputStreamReader(is));
        String line;
        final StringBuilder stringBuilder = new StringBuilder();
        while ((line = bufferReader.readLine()) != null) {
          stringBuilder.append(line).append(System.getProperty("line.separator"));
        }
        bufferReader.close();
        testAdItems.add(new TestAdItem(filename, stringBuilder.toString()));
      } catch (IOException e) {
        Log.e(TAG, e.getMessage(), e);
      }
    }

    return testAdItems;
  }
}
