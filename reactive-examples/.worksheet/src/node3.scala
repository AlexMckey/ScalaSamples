import math.random
/* This worksheet demonstrates some of the code snippets from
* Week3, Lecture 1, "Monads and Effects", particularly slides 12 and 15 (Quiz).
* "flatMap is the plumber for the happy path."
*/

/* We define our own Try, Success and Failure in order to experiment
* with the alternate definitions of flatMap proposed in the Quiz.
*/
//import scala.util.{Try, Success, Failure}

object node3 {;import org.scalaide.worksheet.runtime.library.WorksheetSupport._; def main(args: Array[String])=$execute{;$skip(453); 
  println("Welcome to the Scala worksheet")
  
  abstract class Try[+T] {
      def flatMap[S](f: T=>Try[S]): Try[S] = this match {
		    case Success(value)   => f(value)  //(a)
		    //case Success(value)   => Try(f(value)) //(b)
		    //case Success(value)   => try {f(value) } catch { case t:Throwable => Failure(t)} //(c)
		    case failure @ Failure(t)        => Failure(t) //fixed typo
		  }
  }
  
  case class Success[+T](elem: T) extends Try[T]
  
  case class Failure(t: Throwable) extends Try[Nothing]
  
  object Try {
    def apply[T](r: =>T): Try[T] = {

		  
      try { Success(r) }
      catch { case t:Throwable => Failure(t) }
  
    }
  }
  
  
  abstract class Coin {
     val denomination: Int
  }
    case class Silver() extends Coin {
      val denomination = 1
    }
    case class Gold() extends Coin {
      val denomination = 10
    }
    
  abstract class Treasure{
    val value: Int
    }
  
  trait Adventure {
    def collectCoins(): Try[List[Coin]]
    def buyTreasure(coins: List[Coin]): Try[Treasure]
  };$skip(1051); 
 
  def eatenByMonster(a:Adventure) = (random < 0.3)
  class GameOverException(msg: String) extends Error{
    override def toString = msg
  };System.out.println("""eatenByMonster: (a: node3.Adventure)Boolean""");$skip(114); 
  val treasureCost = 50
  
  object Diamond extends Treasure {
    val value = treasureCost
    override def toString = "Diamond"
  };System.out.println("""treasureCost  : Int = """ + $show(treasureCost ));$skip(293); 
   
  def coinSource(rand: Double, prob: Double ): Coin =
    if (rand < prob) {
      Thread.sleep(100)
      new Gold
    }
    else {
      Thread.sleep(10)
      new Silver
    }
  
  object AdventureFactory {
    /* The anonymous class syntax is used for this factory object,
    * allowing us to instantiate an object
    * that extends a trait having undefined members.
    * The anonymous class must provide definitions
    * for all undefined members of the trait.
    * Note that this object has an apply method:
    * AdventureFactory() is desugared to
    * AdventureFactory.apply()
    */
    def apply() = new Adventure {
       def collectCoins(): Try[List[Coin]] = Try {
         if (eatenByMonster(this))
           throw(new GameOverException("Oooops"))
         else for { i <- 1 to 10 toList } yield coinSource(random, 0.5)
       }
       def totalCoins(coins: List[Coin]) =
         coins.foldLeft(0)( (sum, coin) => sum + coin.denomination  )
       
       def buyTreasure(coins: List[Coin]): Try[Treasure] = Try
       {
         if (totalCoins(coins) < treasureCost)
           throw(new GameOverException("Nice try!"))
         else
           Diamond
       }
    }
  };System.out.println("""coinSource: (rand: Double, prob: Double)node3.Coin""");$skip(1538); 
  /* Exception handling with flatMap.
  */
  def block(i: Int) = {
    println("Iteration: " + i.toString)
	  val adventure: Adventure = AdventureFactory()
	  val tryCoins: Try[List[Coin]] = adventure.collectCoins()
	  val tryTreasure: Try[Treasure] = tryCoins.flatMap(coins=>{adventure.buyTreasure(coins)})
	  tryTreasure match {
	    case Success(treasure)     => println("Treasure: " + treasure.toString + " " + i.toString)
	    case Failure(t)      => println("Error Message: " + t.toString + " " + i.toString)
	  }
	};System.out.println("""block: (i: Int)Unit""");$skip(355); 
  /* Multiple executions of a block of commands where
   * each block contains one collectCoins and
   * one buyTreasure. If either call fails, the whole iteration does not fail,
   * because we are catching exceptions (with flatMap) in this implementation.
   * Note that these blocks execute synchrounsly.
   */
  (1 to 10 toList).foreach(i =>block(i))}


   
}