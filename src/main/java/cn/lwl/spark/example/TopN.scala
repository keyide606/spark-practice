package cn.lwl.spark.example

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable

object TopN {
  val conf = new SparkConf()

  conf.setAppName("topn")
  conf.setMaster("local")
  val context = new SparkContext(conf)

  def main(args: Array[String]): Unit = {

    val fileData = context.textFile("data/temperature.txt")

    val mapData: RDD[((Int, Int), (Int, Int))] = fileData.map(f => {
      val strings: Array[String] = f.split(" ")
      val arr = strings(0).split("-")
      ((arr(0).toInt, arr(1).toInt), (arr(2).toInt, strings(1).toInt))
    })

    // 这种实现如果某月的温度数据特别的，可能会造成数据倾斜
    //    val groupByKeyData: RDD[((Int, Int), Iterable[(Int, Int)])] = mapData.groupByKey()
    //    val value: RDD[((Int, Int), List[(Int, Int)])] = groupByKeyData.mapValues(arr => {
    //      var map = new mutable.HashMap[Int, Int]()
    //      arr.foreach(s => {
    //        if (map.getOrElse(s._1, 0) < s._2) map.put(s._1, s._2)
    //      })
    //      map.toList.sorted(new Ordering[(Int, Int)] {
    //        override def compare(x: (Int, Int), y: (Int, Int)): Int = y._2.compareTo(x._2)
    //      }).take(2)
    //    })
    //    value.foreach(println)

    // 解决某月数据过多的办法，可以先过滤同一天的数据,保留某天最大的数据即可
    //    val filterData: RDD[((Int, Int), (Int, Int))] = mapData
    //      .map(t2 => ((t2._1._1, t2._1._2, t2._2._1), t2._2._2))
    //      .reduceByKey((a, b) => if (a > b) a else b)
    //      .map(t2 => ((t2._1._1, t2._1._2), (t2._1._3, t2._2)))
    //
    //    val groupData: RDD[((Int, Int), Iterable[(Int, Int)])] = filterData.groupByKey()
    //
    //    val value: RDD[((Int, Int), List[(Int, Int)])] = groupData.mapValues(arr => arr.toList.sorted(new Ordering[(Int, Int)] {
    //      override def compare(x: (Int, Int), y: (Int, Int)): Int = y._2.compareTo(x._2)
    //    }))
    //    value.foreach(println)

    // 全排序处理,会出现问题，因为sort by按照年-月-温度，但是reduceBy按照年-月-日,shuffle处理过程中有问题
    // 后续shuffle应该按照前面shuffle的子集处理
    //    val sortedData: RDD[((Int, Int), List[(Int, Int)])] = mapData
    //      .sortBy(t2 => (t2._1._1, t2._1._2, t2._2._2),false)
    //      .map(t2 => ((t2._1._1, t2._1._2, t2._2._1), t2._2._2))
    //      .reduceByKey((a, b) => if (a > b) a else b)
    //      .map(t2 => ((t2._1._1, t2._1._2), (t2._1._3, t2._2)))
    //      .groupByKey()
    //      .mapValues(arr => arr.toList.take(2))
    //
    //    sortedData.foreach(println)

    // 以上处理或多或少都有些问题，如果每个分区都取同一个月的top2的温度数据,然后再进行合并处理
    // 那么就会大大减轻内存、shuffle的影响

    val value1: RDD[((Int, Int), List[(Int, Int)])] = mapData.combineByKey(
      (value: (Int, Int)) => Array((value._1, value._2), (0, 0), (0, 0)),
      (oldValue: Array[(Int, Int)], newValue: (Int, Int)) => {
        var flag = 0
        for (i <- 0 until oldValue.length) {
          if (oldValue(i)._1 == newValue._1) {
            if (oldValue(i)._2 < newValue._2) {
              flag = 1
              oldValue(i) = newValue
            } else {
              flag = 2
            }
          }
        }
        if (flag == 0) {
          oldValue(oldValue.length - 1) = newValue
        }
        scala.util.Sorting.quickSort(oldValue)
        oldValue
      },
      (oldValue: Array[(Int, Int)], newValue: Array[(Int, Int)]) => {
        oldValue.union(newValue).distinct
      }
    ).mapValues(arr => arr.toList)
  }


  def testShuffle(): Unit = {
    var list = context.parallelize(List(
      "hello world!",
      "hello scala!",
      "hello spark!",
      "hello hadoop!"
    ))
    // 简单word count
    val result: RDD[(String, Int)] = list.flatMap(s => s.split(" ")).map((_, 1)).reduceByKey(_ + _)
    // 执行下面代码,map会导致后面的groupByKey，再次发生shuffle
    // mapValues则不会,因为执行时知道仅仅是values发生改变，再次根据key计算分区 数据不会移动，故不会shuffle
    // 所以后面在写代码，key不变，尽量使用mapValues 和flatMapValues
    val value = result.mapValues(x => x * 10)
    // val value = result.map(x => (x._1, x._2 * 10))
    value.groupByKey().foreach(println)
  }
}
