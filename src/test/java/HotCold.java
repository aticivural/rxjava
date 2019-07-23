import org.junit.Test;
import rx.Observable;
import rx.observables.ConnectableObservable;

import java.util.concurrent.TimeUnit;

public class HotCold {

    @Test
    public void coldObservable() throws InterruptedException {
        Observable<Long> coldObservable = Observable.interval(1, TimeUnit.SECONDS);
        coldObservable.subscribe(item -> System.out.println("Observer 1: " + item));
        Thread.sleep(3000);
        coldObservable.subscribe(item -> System.out.println("Observer 2: " + item));
        Thread.sleep(5000);
    }

    @Test
    public void hotObservable() throws InterruptedException {
        Observable<Long> hotObservable = Observable.interval(1, TimeUnit.SECONDS);
        ConnectableObservable<Long> connectableObservable = hotObservable.publish();
        connectableObservable.connect();

        Thread.sleep(2000);
        connectableObservable.subscribe(item -> System.out.println("Observer 1: " + item));

        Thread.sleep(3000);
        connectableObservable.subscribe(item -> System.out.println("Observer 2: " + item));
        Thread.sleep(5000);
    }
}
