package cn.lwl.scala

/**
 * scala中的集合操作
 */
object ChapterFour {
  def main(args: Array[String]): Unit = {
    testFunction()
  }

  def testArray(): Unit = {
    var arr = Array(1, 2, 3, 4, 5)
    println(arr(0))
    // 循环
    for (element <- arr) {
      println(element)
    }
    var it = arr.iterator
    while (it.hasNext) {
      var j = it.next()
      println(j)
    }
  }


  def testList(): Unit = {
    // 不可变列表
    var list = List(1, 2, 3, 4, 5)
    // 查询索引0位置的元素
    println(list(0))
    // 遍历
    list.foreach(println)
    // map操作
    val ints = list.map(i => i + 1)
    ints.foreach(println)
    // 可变列表
    import scala.collection.mutable.ListBuffer
    val listBuffers = ListBuffer(1, 2, 3, 4, 5, 6)
    listBuffers.+=(3)
    val iterator = listBuffers.iterator
    while (iterator.hasNext) {
      println(iterator.next())
    }
  }

  def testSet(): Unit = {
    // 不可变set
    val set = Set(1, 2, 3, 4, 3)
    println(set(5))
    set.foreach(println)
    // 可变set
    import scala.collection.mutable.Set // 可以在任何地方引入 可变集合

    val mutableSet = Set(1, 2, 3)
    mutableSet.add(4)
    println(mutableSet)
    mutableSet.remove(1)
    mutableSet += 5
    var that = mutableSet.+(10)
    println(that)
    println(mutableSet)
    mutableSet -= 2
    println(mutableSet)
  }

  def testTuple(): Unit = {
    // tuple
    println("------tuple------")
    val tuple = Tuple2("aa", 123)
    println(tuple._2)
    println(tuple._1)
  }

  def testMap(): Unit = {
    println("-----map------")
    val map = Map(("a", 1), ("b", 2), ("c", 3))
    println(map.get("a").getOrElse("hello,world!"))
    println(map.get("d").getOrElse("hello,world!"))
  }

  def testFunction(): Unit = {
    val strings = List("hello world", "hello scala", "hello spark")
    val flatMaps = strings.flatMap(s => s.split(" "))
    println(flatMaps)
    val tuples = flatMaps.map((_, 1))
    tuples.foreach(println)

    val ite = strings.iterator
    val thenMap = ite.flatMap(s => s.split(" "))
    val functions = thenMap.map((_, 1))
    functions.foreach(println)
    // 迭代器 迭代到末尾了，无法再使用
    val thatMap = ite.map(s => s"$s,!")
    thatMap.foreach(println)
    // todo 理清iterator源码

  }
}
