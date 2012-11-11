package typeclasses

trait PDF[T] extends (T => Double)
trait PDFDiscrete[T] extends PDF[T]
trait PDFGaussian[T] extends PDF[T]

trait CanBuildPDF[Elem, That] {
  def apply(xs: Traversable[Elem]): That
}

object CanBuildPDF {

  implicit object DiscreteFromStrings extends CanBuildPDF[String, PDFDiscrete[String]] {
    def apply(xs: Traversable[String]) =
      new PDFDiscrete[String] {
        // Determine the number of occurences of each distinct String ...
        def apply(x: String) = throw new Error("Not yet implemented.")
      }
  }

  implicit object GaussianFromDoubles extends CanBuildPDF[Double, PDFGaussian[Double]] {
    def apply(xs: Traversable[Double]) =
      new PDFGaussian[Double] {
        // Determine the mean and standard deviation from the given values ...
        def apply(x: Double) = throw new Error("Not yet implemented.")
      }
  }

}

object pdfFoo {

  //  def createPDF[T](lst: Traversable[T]): PDF = ???

  def createPDF[Elem, That](lst: Traversable[Elem])
    (implicit bldr: CanBuildPDF[Elem, That]): That =
    bldr(lst)

  val x = createPDF(List("Hello", "world"))
  implicitly[x.type <:< PDFDiscrete[String]]

  val y = createPDF(Array.fill(50000)(util.Random.nextDouble))
  implicitly[y.type <:< PDFGaussian[Double]]

}
