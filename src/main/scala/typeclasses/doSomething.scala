package typeclasses

class MySuperClass

trait CanDoSomething[T]

object CanDoSomething {
  implicit object MySuperClassCanDoSomething extends CanDoSomething[MySuperClass]
  implicit object DoubleCanDoSomething extends CanDoSomething[Double]
  implicit object VectorOfDoubleCanDoSomething extends CanDoSomething[Vector[Double]]
}

object doSomethingFoo {

//  def doSomething[T](x: T) = ???
//
//
//  def doSomething[T <: MySuperClass](x: T) = ???
//
//
//  object MySuperClassCanDoSomething extends CanDoSomething[MySuperClass]
//  object DoubleCanDoSomething extends CanDoSomething[Double]
//  object VectorOfDoubleCanDoSomething extends CanDoSomething[Vector[Double]]
//
//  def doSomething[T](x: T)(ev: CanDoSomething[T]) = ???
//
//
//  def doSomething[T](x: T)(implicit ev: CanDoSomething[T]) = ???

  def doSomething[T: CanDoSomething](x: T) =
    throw new Error("Not implemented.")

  doSomething(1.0)
  doSomething(Vector(1.0, 2.0, 3.0))
  doSomething(new MySuperClass)
  //doSomething("This line will not compile!")

}
