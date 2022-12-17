package cn.lwl.spark.example

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object WordCountScala {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
    // 本地运行
    conf.setMaster("local")
    conf.setAppName("WordCountScala")
    val context = new SparkContext(conf)
//    val fileDataSet: RDD[String] = context.textFile("data/word.txt")
//    val middleResult: RDD[String] = fileDataSet.flatMap(x => x.split(","))
//    val tupleResult: RDD[(String, Int)] = middleResult.map(x => {
//      (x, 1)
//    })
//    val res: RDD[(String, Int)] = tupleResult.reduceByKey((x: Int, y: Int) => {
//      x + y
//    })
//    res.foreach(println)

    // 简化spark WordCount
    val result: RDD[(String, Int)] = context.textFile("data/word.txt")
      .flatMap(_.split(","))
      .map((_, 1))
      .reduceByKey(_ + _)

    result.foreach(println)
    // 在word count 进一步,求出现n次的字符的种类
    // 观察spark-ui
    // spark ui 术语
    // 1. stage：阶段,代表可以在一台机器上完成的计算,故如果需要聚合结果,会产生新的stage
    // 2. job :  一个application有多个job,每个job代表一次执行流程,如 foreach方法的调用
    // job之间可以复用中间结果
    val res = result.map(x => (x._2, 1)).reduceByKey(_ + _)
    res.foreach(println)

    Thread.sleep(Long.MaxValue)
  }


}
