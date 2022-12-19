package cn.lwl.spark.api

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable.ListBuffer

object Partition {
  var conf = new SparkConf()
  conf.setAppName("aggregator")
  conf.setMaster("local")
  var context = new SparkContext(conf)
  context.setLogLevel("ERROR")

  def main(args: Array[String]): Unit = {
    testRepartition()
  }


  def testMapPartition(): Unit = {
    var list = context.parallelize(1 to 10, 2)

    // 模拟从数据库中读取数据

    val mapRDD: RDD[String] = list.map(s => {
      println("---建立连接---")
      var value = s"selected_$s"
      println("关闭连接")
      value
    })
    mapRDD.foreach(println)
    println("==========进化版本==============")

    // 上述处理有问题,每条数据都会建立连接,关闭连接，极大浪费资源
    // 能不能建立一次连接然后去处理数据，数据处理完之后 在关闭连接
    val res = list.mapPartitionsWithIndex(
      (index, iterator) => {
        var result = new ListBuffer[String];
        println("建立连接")
        while (iterator.hasNext) {
          var value = iterator.next()
          result.+=(s"index=$index,selected_$value")
        }
        println("关闭连接")
        result.iterator
      })
    res.foreach(println)

    println("==========再进化版本==============")
    // 但是上面方法有内存oom的隐患,会有listBuffer这个内存中的集合,
    // 如果数据量太大 可能会造成oom,能不能不在内存中有这个buffer，采取pip line形式，来一条计算一条
    // 然后将每条计算结果进行返回
    val result: RDD[String] = list.mapPartitionsWithIndex(
      (index, ite) => {
        println("建立连接")
        new Iterator[String] {
          override def hasNext: Boolean = if (ite.hasNext) true else {
            println("关闭连接");
            false
          }

          override def next(): String = {
            var value = ite.next();
            s"index=$index,selected_$value"
          }
        }
      }
    )
    result.foreach(println)
  }

  def testRepartition(): Unit = {
    var list = context.parallelize(1 to 100, 5)

    // 抽样测试
    val r1 = list.sample(false, 0.1)
    val r2 = list.sample(false, 0.1)
    val r3 = list.sample(false, 0.1, 20)
    val r4 = list.sample(true, 0.1)
    r1.foreach(println)
    println("=======")
    r2.foreach(println)
    println("=======")
    r3.foreach(println)
    println("======")
    r4.foreach(println)

    println("==========repartition==============")

    var data = context.parallelize(1 to 10, 5)

    println(s"data:${data.getNumPartitions}")
    // 一定发生了shuffle操作,可看源码
    val value = data.repartition(2)
    value.foreach(println)
    // 可以尝试关闭分区
    data.coalesce(3, false)
  }
}
