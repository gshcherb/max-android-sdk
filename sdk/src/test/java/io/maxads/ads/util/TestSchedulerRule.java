//package io.maxads.ads.util;
//
//import org.junit.rules.TestRule;
//import org.junit.runner.Description;
//import org.junit.runners.model.Statement;
//
//import io.reactivex.Scheduler;
//import io.reactivex.android.plugins.RxAndroidPlugins;
//import io.reactivex.functions.Function;
//import io.reactivex.internal.schedulers.ExecutorScheduler;
//import io.reactivex.plugins.RxJavaPlugins;
//
//public class TestSchedulerRule implements TestRule {
//  private final Scheduler immediate = new Scheduler() {
//    @Override
//    public Worker createWorker() {
//      return new ExecutorScheduler.ExecutorWorker(Runnable::run);
//    }
//  };
//  private final io.reactivex.schedulers.TestScheduler testScheduler = new io.reactivex.schedulers.TestScheduler();
//
//  public io.reactivex.schedulers.TestScheduler getTestScheduler() {
//    return testScheduler;
//  }
//
//  @Override
//  public Statement apply(final Statement base, Description d) {
//    return new Statement() {
//      @Override
//      public void evaluate() throws Throwable {
//        RxJavaPlugins.setIoSchedulerHandler(
//          new Function<Scheduler, Scheduler>() {
//            @Override
//            public Scheduler apply(Scheduler scheduler) throws Exception {
//              return testScheduler;
//            }
//          });
//        RxJavaPlugins.setComputationSchedulerHandler(
//          new Function<Scheduler, Scheduler>() {
//            @Override
//            public Scheduler apply(Scheduler scheduler) throws Exception {
//              return testScheduler;
//            }
//          });
//        RxJavaPlugins.setNewThreadSchedulerHandler(
//          new Function<Scheduler, Scheduler>() {
//            @Override
//            public Scheduler apply(Scheduler scheduler) throws Exception {
//              return testScheduler;
//            }
//          });
//        RxAndroidPlugins.setMainThreadSchedulerHandler(
//          new Function<Scheduler, Scheduler>() {
//            @Override
//            public Scheduler apply(Scheduler scheduler) throws Exception {
//              return immediate;
//            }
//          });
//        try {
//          base.evaluate();
//        } finally {
//          RxJavaPlugins.reset();
//          RxAndroidPlugins.reset();
//        }
//      }
//    };
//  }
//}
