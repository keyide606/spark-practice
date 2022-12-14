package cn.lwl.scala

/**
 * match关键字
 */
object ChapterSeven {
  def main(args: Array[String]): Unit = {
    val tuple = new Tuple6(1, 3, 99, "abc", false, 22)
    val iterator = tuple.productIterator

    val units = iterator.map(x => {
      x match {
        case 1 => println(s"$x is 1")
        case "abc" => println(s"$x is abc")
        case false => println(s"$x is false")
        case w: Int if w > 80 => println(s"$w > 80")
        case _ => println(s"dont no what type")
      }
    })
    while(units.hasNext){
      println(units.next())
    }

  }
}
