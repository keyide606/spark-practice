package cn.lwl.spark.api

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Aggregator {

  var conf = new SparkConf()
  conf.setAppName("aggregator")
  conf.setMaster("local")
  var context = new SparkContext(conf)

  def main(args: Array[String]): Unit = {
    testGroupByKey()

  }


  def testGroupByKey(): Unit = {
    var list: RDD[(String, Int)] = context.parallelize(List(
      ("zs", 12),
      ("ww", 21),
      ("zl", 33),
      ("ls", 44),
      ("zs", 12),
      ("ww", 21),
      ("zl", 33),
      ("ls", 44)
    ))
    var groupData: RDD[(String, Iterable[Int])] = list.groupByKey()
    groupData.foreach(println)
    // 转换回来

    val originalData: RDD[(String, Int)] = groupData.flatMap(e => (e._2.map(s => (e._1, s))).iterator)

    originalData.foreach(println)
    // 简单写法
    val value: RDD[(String, Int)] = groupData.flatMapValues(e => e.iterator)

    // 取每组高分的两个元素
    val top: RDD[(String, List[Int])] = groupData.mapValues(e => e.toList.sorted.take(2))
    top.foreach(println)
  }


  def testMath(): Unit = {
    println("====sum,min,max,avg======")
    var list: RDD[(String, Int)] = context.parallelize(List(
      ("zs", 12),
      ("ww", 21),
      ("zl", 33),
      ("ls", 14),
      ("zs", 12),
      ("ww", 31),
      ("zl", 33),
      ("ls", 44)
    ))

    // sum
    val sumRDD: RDD[(String, Int)] = list.reduceByKey(_ + _)
    // min
    val minRDD: RDD[(String, Int)] = list.reduceByKey((oldValue, newValue) => if (oldValue > newValue) newValue else oldValue)
    // max
    val maxRDD: RDD[(String, Int)] = list.reduceByKey((oldValue, newValue) => if (oldValue > newValue) oldValue else newValue)
    // count
    val countRDD: RDD[(String, Int)] = list.groupByKey().mapValues(e => e.toList.size)
    // avg
    var avgCount: RDD[(String, Int)] = sumRDD.join(countRDD).mapValues(e => e._1 / e._2)

    // 自定义combineKey
    val combineByKeyRDD: RDD[(String, (Int, Int))] = list.combineByKey(
      // createCombiner: V => C,
      (value: Int) => (value, 1),
      //  mergeValue: (C, V) => C,
      (oldValue: (Int, Int), newValue: Int) => (oldValue._1 + newValue, oldValue._2 + 1),
      // mergeCombiners: (C, C) => C
      (oldValue: (Int, Int), newValue: (Int, Int)) => (oldValue._1 + newValue._1, oldValue._2 + newValue._2)
    )
    var avg = combineByKeyRDD.mapValues(e => e._1 / e._2)
    avg.foreach(println)
  }

}
