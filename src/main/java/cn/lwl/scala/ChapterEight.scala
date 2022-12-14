package cn.lwl.scala
/**
 * 偏函数:PartialFunction
 */
object ChapterEight {
  def main(args: Array[String]): Unit = {
    // 输入是任意类型,返回是String类型
    def function: PartialFunction[Any, String] = {
      case 1 => "one and only"
      case "abc" => "i like you"
      case 99 => "i will live to 99"
      case _=>"no thing"
    }

    println(function(1))
    println(function(99))
    println(function(20))
  }
}
