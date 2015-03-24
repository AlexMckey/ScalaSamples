
object Main {
  def main(args: Array[String]): Unit = {
    import rx.lang.scala

    val lib = new rx.lang.scala.examples.MovieLib(
      rx.lang.scala.Observable(
        new rx.lang.scala.examples.Movie(3000),
        new rx.lang.scala.examples.Movie(1000),
        new rx.lang.scala.examples.Movie(2000)))

    lib.longMovies.subscribe(m => println("A movie of length " + m.lengthInSeconds + "s"));
  }

}
