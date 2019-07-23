import org.junit.Test;
import rx.Observable;

public class Operators {

    @Test
    public void operators(){

        Observable.just("1", "2", "3", "4", "5", "6", "7")
                .map(Integer::valueOf)
                .map(i -> getDouble(i))
                .filter(i -> i > 6)
                .subscribe(System.out::println);

    }

    private static Integer getDouble(int number){
        return number * 2;
    }

    private static Observable<Integer> getDoubleObservable(int number){
        return Observable.just(number * 2);
    }


    // { {1,2}, {3,4}, {5,6} } -> map -> { {1,2}, {3,4}, {5,6} }

    // { {1,2}, {3,4}, {5,6} } -> flatMap -> {1,2,3,4,5,6}

    // { {'a','b'}, {'c','d'}, {'e','f'} } -> flatMap -> {'a','b','c','d','e','f'}


    @Test
    public void flatMap(){
        String[][] data = new String[][]{{"a", "b"}, {"c", "d"}, {"e", "f"}};
        Observable<String[]> obsList = Observable.from(data);

        //Observable<String[]> obsFilter = obsList.filter(item -> "a".equals(item.toString()));
        //obsFilter.subscribe(System.out::println);


        obsList.flatMap(Observable::from)
                .filter(item -> "a".equals(item))
                .subscribe(System.out::println);


    }
}
