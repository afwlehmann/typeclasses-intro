package typeclasses

trait Numeric[T] {
  def times(x: T, y: T): T
}

object Numeric {

  implicit object IntIsIntegral extends Numeric[Int] {
    def times(x: Int, y: Int): Int = x * y
  }

  implicit object CharIsIntegral extends Numeric[Char] {
    def times(x: Char, y: Char): Char = (x * y).toChar
  }

  // ... something similar for Double, Float and all other
  // classes for which you want to provide the corresponding
  // behaviour.

}

object numericFoo {

  //  def scale[T](xs: Traversable[T], factor: T) =
  //    xs map (x => x * factor)

  def scale[T](xs: Traversable[T], factor: T)(implicit num: Numeric[T]): Traversable[T] =
    xs map (x => num.times(x, factor))

  val x = scale(List(1,2,3), 4)
  require(x == List(4, 8, 12))

  val y = scale(List('A', 'B'), 1.toChar)
  require(y == List('A', 'B'))

  //scale(List("This", "line", "will", "not"), "compile.")

}
