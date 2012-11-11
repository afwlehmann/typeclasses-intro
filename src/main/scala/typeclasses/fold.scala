package typeclasses

trait CanFold[-Elem, Out] {
  def sum(acc: Out, elem: Elem): Out
  def zero: Out
}

object CanFold {

  implicit object CanFoldInts extends CanFold[Int, Long] {
    def sum(acc: Long, elem: Int): Long = acc + elem
    def zero = 0
  }

//  // Step 0 ("original"):
//  implicit def canFoldTraversables[T] =
//    new CanFold[Traversable[T], Traversable[T]] {
//      def sum(acc: Traversable[T], elem: Traversable[T]): Traversable[T] = acc ++ elem
//      def zero = Traversable.empty
//    }

  import scala.collection.generic.CanBuildFrom
  import scala.collection._

//  // Step 1: Use CanBuildFrom.
//  // Fails because That could be really anything.
//  implicit def canFoldTraversables[Repr[T] <: Traversable[T], T, That]
//    (implicit cbf: CanBuildFrom[Repr[T], T, That]): CanFold[Repr[T], That] =
//    new CanFold[Repr[T], That] {
//      def sum(acc: That, elem: Repr[T]) = acc ++ elem
//      def zero = cbf().result
//    }

//  // Step 2: Restrict That to subtypes of Traversable.
//  // Fails because the result of Traversable.++ is Traversable[T] yet we need That.
//  // Note that restricting That to exactly Traversable[T] by means of
//  // That >: Traversable[T] <: Traversable[T] would yield an equivalent result to step 1.
//  implicit def canFoldTraversables[Repr[T] <: Traversable[T], T, That <: Traversable[T]]
//    (implicit cbf: CanBuildFrom[Repr[T], T, That]): CanFold[Repr[T], That] =
//    new CanFold[Repr[T], That] {
//      def sum(acc: That, elem: Repr[T]) = acc ++ elem
//      def zero = cbf().result
//    }

//  // Step 3: Use CanBuildFrom explicitly.
//  // Works, but having to use the builder explicitly is kind of ugly. Also note that
//  // That has to be That[T] here.
//  implicit def canFoldTraversables[Repr[T] <: Traversable[T], T, That[T] <: Traversable[T]]
//    (implicit cbf: CanBuildFrom[Repr[T], T, That[T]]): CanFold[Repr[T], That[T]] =
//    new CanFold[Repr[T], That[T]] {
//      def sum(acc: That[T], elem: Repr[T]) = {
//        val builder = cbf()
//        builder ++= acc
//        builder ++= elem
//        builder.result
//      }
//      def zero = cbf().result
//    }

  // Step 4: Use TraversableLike and get rid of explicit builder application.
  // Note that That[T] has been replaced by Repr[T].
  implicit def canFoldTraversables[Repr[T] <: TraversableLike[T, Repr[T]], T]
    (implicit cbf: CanBuildFrom[Repr[T], T, Repr[T]]): CanFold[Repr[T], Repr[T]] =
    new CanFold[Repr[T], Repr[T]] {
      def sum(acc: Repr[T], elem: Repr[T]) = acc ++ elem
      def zero = cbf().result
    }

}

object foldFoo {

//  def sum[T](lst: Traversable[T]): T =
//    (lst foldLeft 0)(_ + _)

  def sum[Elem, Out](lst: Traversable[Elem])(implicit cf: CanFold[Elem, Out]): Out =
    (lst foldLeft cf.zero)(cf.sum)

  val w = sum(List(1, 2, 3, 4, 5, 6))

  val x = sum(List(List(1,2,3), List(4,5,6)))
  implicitly[x.type <:< List[Int]]

  val y = sum(List(Set(1, 2, 3), Set(3, 4, 5)))
  implicitly[y.type <:< Set[Int]]

  val z = sum(Vector(List(1, 2, 3), Set(4, 5, 6)))
  implicitly[z.type <:< Iterable[Int]]

}
