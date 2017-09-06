package io.maxads.ads.base.api;

import android.support.annotation.NonNull;

import java.util.Random;

public interface Jitter {
  Jitter DEFAULT  = new Jitter() {
    @NonNull private final Random mRandom = new Random();
    /**
     * @return a random value inside [0.85, 1.15] every time it's called
     */
    @Override
    public double get() {
      return 0.85 + mRandom.nextDouble() % 0.3f;
    }
  };

  Jitter NO_OP = new Jitter() {
    @Override
    public double get() {
      return 1;
    }
  };

  double get();
}
