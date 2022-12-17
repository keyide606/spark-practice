package cn.lwl.spark.api

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * 练习spark的算子,api
 * 通过观察spark-ui 看看各算子使用过程中的依赖情况,分区数量变化情况
 */
object Main {

  var conf = new SparkConf()
  conf.setAppName("api")
  conf.setMaster("local")
  var context = new SparkContext(conf)
  // 设置只输出error日志
  context.setLogLevel("ERROR")

  def main(args: Array[String]): Unit = {
    testCoGroup()
  }


  def testDistinct(): Unit = {
    // 过滤和收集
    val value: RDD[Int] = context.parallelize(List(1, 2, 3, 4, 5, 4, 3, 2, 1))
    val filterData: RDD[Int] = value.filter(x => (x > 3))
    var distinctData: RDD[Int] = filterData.distinct()
    var ints: Array[Int] = filterData.collect()
    ints.foreach(println)

    // 自己实现去重
    var data = value.map((_, 1)).reduceByKey(_ + _).map(_._1)
    data.foreach(println)
  }

  /**
   * 合并RDD
   */
  def testUnion(): Unit = {
    var one = context.parallelize(List(1, 2, 3, 4))
    println(one.partitions.size)
    var two = context.parallelize(List(5, 6, 7, 8))
    val value: RDD[Int] = one.union(two)
    println(value.partitions.size)
    value.foreach(println)
    Thread.sleep(Long.MaxValue)
  }

  /**
   * 求两个RDD的笛卡尔积
   */
  def testCartesian(): Unit = {
    var one = context.parallelize(List(2, 3, 4, 5))
    println(one.partitions.size)
    var two = context.parallelize(List(9, 6, 7, 8))
    // 1. 如果数据不需要区分记录来自于哪个partition,进行全量运算,那么过程不需要分区器,也不需要shuffle
    //2 .shuffle =》 洗牌,会对每条记录根据key计算分区号
    // 3. 拉取全量数据进行计算,比 先计算分区 然后落盘 最后通过IO读取 快
    val value = one.cartesian(two)
    println(value.partitions.size)
    value.foreach(println)
  }

  /**
   * 求两个RDD的交集
   */
  def testIntersection(): Unit = {
    var one = context.parallelize(List(0, 3, 4, 5))
    println(one.partitions.size)
    var two = context.parallelize(List(1, 2, 3, 4))
    val value = one.intersection(two)
    value.foreach(println)
  }

  def testSubtract(): Unit = {
    var one = context.parallelize(List(1, 3, 4, 5))
    println(one.partitions.size)
    var two = context.parallelize(List(6, 2, 3, 4))
    val value = one.subtract(two)
    value.foreach(println)
  }

  def testJoin(): Unit = {
    var one: RDD[(String, Int)] = context.parallelize(List(
      ("zs", 1),
      ("ww", 2),
      ("zl", 3),
      ("ls", 4)
    ))
    println(one.partitions.size)
    var two: RDD[(String, Int)] = context.parallelize(List(
      ("zs", 10),
      ("ww", 20),
      ("pl", 30),
      ("cb", 40)))
    val value: RDD[(String, (Int, Int))] = one.join(two)
    println(two.partitions.size)
    value.foreach(println)
  }

  def testLeftOuterJoin(): Unit = {
    var one: RDD[(String, Int)] = context.parallelize(List(
      ("zs", 12),
      ("ww", 21),
      ("zl", 33),
      ("ls", 44)
    ))
    println(one.partitions.size)
    var two: RDD[(String, Int)] = context.parallelize(List(
      ("zs", 10),
      ("ww", 20),
      ("zs", 30),
      ("cb", 40)))
    val value: RDD[(String, (Int, Option[Int]))] = one.leftOuterJoin(two)
    println(two.partitions.size)
    value.foreach(println)
  }

  def testFullOuterJoin(): Unit = {
    var one: RDD[(String, Int)] = context.parallelize(List(
      ("zs", 122),
      ("ww", 211),
      ("zl", 333),
      ("ls", 444)
    ))
    println(one.partitions.size)
    var two: RDD[(String, Int)] = context.parallelize(List(
      ("zs", 10),
      ("ww", 20),
      ("zs", 30),
      ("cb", 40)))
    val value: RDD[(String, (Option[Int], Option[Int]))] = one.fullOuterJoin(two)
    println(two.partitions.size)
    value.foreach(println)
  }

  def testCoGroup(): Unit = {
    var one: RDD[(String, Int)] = context.parallelize(List(
      ("zs", 122),
      ("ww", 211),
      ("zl", 333),
      ("ls", 444)
    ))
    println(one.partitions.size)
    var two: RDD[(String, Int)] = context.parallelize(List(
      ("zs", 10),
      ("ww", 20),
      ("zs", 30),
      ("cb", 40)))
    val value = one.cogroup(two)
    println(value.partitions.size)
    value.foreach(println)
  }

}
