import org.junit.Test;
import rx.Observable;
import rx.Subscriber;
import rx.exceptions.Exceptions;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Creation {

    //TODO create
    @Test
    public void create01() {
        Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> observer) {
                try {
                    if (!observer.isUnsubscribed()) {
                        for (int i = 1; i < 5; i++) {
                            observer.onNext(i);
                        }
                        observer.onCompleted();
                    }
                } catch (Exception e) {
                    observer.onError(e);
                }
            }
        }).subscribe(new Subscriber<Integer>() {
            @Override
            public void onNext(Integer item) {
                System.out.println("Next: " + item);
            }

            @Override
            public void onError(Throwable error) {
                System.err.println("Error: " + error.getMessage());
            }

            @Override
            public void onCompleted() {
                System.out.println("Sequence complete.");
            }
        });
    }

    //TODO delay !!!
    @Test
    public void create02() throws InterruptedException {
        Observable.create((Observable.OnSubscribe<Integer>) subscriber -> {

            subscriber.onNext(1);
            subscriber.onNext(2);
            subscriber.onCompleted();
        })
                .filter(i -> i % 2 == 0)
                .map(i -> "Value " + i + " processed on " + Thread.currentThread())
                //.delay(0, TimeUnit.SECONDS)
                .subscribe(s -> {
                    for (int i = 0 ; i < 100; i++){
                        System.out.println("SOME VALUE =>" + i);
                    }
                });
        System.out.println("Will print BEFORE values are emitted");

        Thread.sleep(2000);
    }


    //TODO interval !!!
    @Test
    public void interval() throws InterruptedException{
        Observable<Long> observable1 = Observable.interval(1, 2, TimeUnit.SECONDS);
        observable1.subscribe((l)-> System.out.println(Thread.currentThread().getName() + ":" + l));

        Thread.sleep(10000);
    }



    //TODO just
    @Test
    public void just() {
        Observable<String> map = Observable.just(getName())
                .map(item -> {
                    System.out.println("hamza : " + item);
                    return item.toUpperCase();
                });

        map.subscribe(System.out::println);

        map.subscribe(item -> System.out.println(item.length()));
    }

    private String getName(){
        String name = "vural";
        System.out.println(name);
        return name;
    }

    //TODO from
    @Test
    public void from() {
        List<Integer> integers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        //Observable.from(integers).subscribe(System.out::println, err -> System.err.println(err));
        //Observable.just(integers).subscribe(System.out::println);

        Observable.fromCallable(() -> "ReactiveX").map(String::toUpperCase).subscribe(System.out::println);

    }

    //TODO defer
    @Test
    public void defer() {

        Car car = new Car();
        Observable<String> brandObservable = car.getBrandObservable();
        Observable<String> brandDeferObservable = car.getBrandDeferObservable();

        car.brand = "BMW";

        brandObservable.subscribe(brand -> System.out.println("brandObservable: brand ->  " + brand));
        brandDeferObservable.subscribe(brand -> System.out.println("brandDeferObservable: brand ->  " + brand));
    }


    //TODO range
    @Test
    public void range(){
        Observable.range(3,5).subscribe(System.out::println);
    }

//======================================================================
    //TODO ERROR HANDLING

    //TODO Unchecked Exceptions
    @Test
    public void uncheckedException() {
        Observable.just("Hello!")
                //.map(input -> getRuntimeException())
                .subscribe(
                        System.out::println,
                        error -> System.err.println(error + "Error!")
                );
    }

    //TODO Checked Exceptions
    @Test
    public void checkedException01() {
        Observable.just("Hello!")
                .map(input -> {
                    try {
                        return getException(input);
                    } catch (Throwable t) {
                        throw Exceptions.propagate(t);
                    }
                })
                .subscribe(
                        item -> System.out.println("onNext :: " + item),
                        err -> System.err.println("onError :: " + err),
                        () -> System.out.println("onCompleted :: Completed"));
    }


    @Test
    public void checkedException02() {
        Observable.just("Hello!")
                .flatMap(input -> {
                    try {
                        return Observable.just(getException(input));
                    } catch (Throwable t) {
                        return Observable.error(t);
                    }
                })
                .subscribe(
                        item -> System.out.println("onNext :: " + item),
                        err -> System.err.println("onError :: " + err),
                        () -> System.out.println("onCompleted :: Completed"));
    }


    //TODO onErrorReturn
    @Test
    public void onErrorReturn() throws InterruptedException {
        Observable.just("Hello!")
                .map(input -> getRuntimeException())
                .onErrorReturn(err -> "value returned in case of existing error")
                .subscribe(
                        System.out::println,
                        error -> System.out.println("Error!"),
                        () -> System.out.println("onCompleted")
                );

    }


//======================================================================


    private static Object getRuntimeException() {
        throw new RuntimeException();

    }


    private static String getException(String input) throws IOException {
        throw new IOException("io error");
    }
}


class Car {

    String brand = "DEFAULT";

    Observable<String> getBrandObservable() {
        return rx.Observable.just(brand);
    }

    Observable<String> getBrandDeferObservable() {
        return Observable.defer(() -> Observable.just(brand));
    }
}
