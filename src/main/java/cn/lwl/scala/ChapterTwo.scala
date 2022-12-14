package cn.lwl.scala

/**
 * scala中的控制语句
 */
object ChapterTwo {
  def main(args: Array[String]): Unit = {
    testFor()
  }

  def testFor(): Unit = {
    // 简单版
    var seq = 2 until (10)
    for (i <- seq) {
      println(i)
    }
    //只要偶数
    for (i <- seq if i % 2 == 0) {
      println(i)
    }
    // 打印99乘法表
    for (i <- 1 to 9) {
      for (j <- 1 to i) {
        print(s"$i*$j=${1 * j}\t")
      }
      println()
    }

    //
    val ints = for (i <- 1 to 10) yield {
      i * 2
    }
    println(ints)
  }

  def testWhile(a: Int): Unit = {
    var b = a
    while (b > 0) {
      println(b)
      b = b - 1;
    }
  }

  def testIf(): Unit = {
    // if语句
    var a = 10
    if (a > 0) {
      println("正数")
    } else {
      println("负数")
    }
  }
}
