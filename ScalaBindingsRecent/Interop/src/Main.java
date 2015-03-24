import rx.Observable;
import rx.lang.scala.examples.Movie;
import rx.lang.scala.examples.MovieLib;

import static rx.lang.scala.ImplicitFunctionConversions.toScalaObservable;

public class Main {

    public static void main(String[] args) {

        Observable<Movie> movies = Observable.from(
                new Movie(3000),
                new Movie(1000),
                new Movie(2000)
        );

        MovieLib lib = new MovieLib(toScalaObservable(movies));

        lib.longMovies().asJavaObservable().subscribe(m ->
                System.out.println("A movie of length " + m.lengthInSeconds() + "s")
        );
    }

}
