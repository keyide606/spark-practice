package cn.lwl.scala

import java.util

/**
 * 隐式转换
 */
object ChapterNine {
  def main(args: Array[String]): Unit = {
    val list = new util.LinkedList[String]()
    list.add("a")
    list.add("b")
    list.add("c")
    list.add("d")
    // 定义方法隐式转换
    //    implicit def transfer[T](list: util.LinkedList[T]): Unit = {
    //      new one[T](list)
    //    }
    //    list.forEach(println)
    // 定义隐式转换类
    //    implicit class one[T](list: util.LinkedList[T]) {
    //      def foreach(f: (T) => Unit): Unit = {
    //        val iterator = list.iterator()
    //        while (iterator.hasNext) {
    //          f(iterator.next())
    //        }
    //      }
    //    }
    //    list.foreach(println)

    val list1 = new util.LinkedList[String]()
    list1.add("abc")
    list1.add("java")
    list1.add("scala")
    val strings = new util.ArrayList[String]()
    strings.add("hive")
    strings.add("hadoop")
    strings.add("hbase")

    implicit def transferTwo[T](list: util.LinkedList[T]): Unit = {
      new Two(list.iterator())
    }

    implicit def transferThree[T](list: util.ArrayList[T]): Unit = {
      new Two(list.iterator())
    }

    list1.forEach(println)
    strings.forEach(println)


    implicit var name: String = "zs"

    // 隐式装换参数
    def getName(implicit name: String): String = {
      return s"$name--age"
    }

    println(getName)

  }

  class Two[T](list: util.Iterator[T]) {
    def foreach(f: (T) => Unit): Unit = {
      while (list.hasNext) {
        f(list.next())
      }
    }
  }
}
