package cn.lwl.spark.api

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * 根据数据计算出各个网站的PV UV,并输出各top5
 * PV：pv的全称是page view，译为页面浏览量或点击量，
 * 通常是衡量一个网站甚至一条网络新闻的指标。用户每次对网站中的一个页面的请求或访问均被记录1个PV，
 * 用户对同一页面的多次访问，pv累计。例如，用户访问了4个页面，pv就+4
 * UV: uv的全称是unique view，译为通过互联网访问、浏览这个网页的自然人，
 * 访问网站的一台电脑客户端被视为一个访客，在同一天内相同的客户端只被计算一次
 */
object UserBehaviour {
  var conf = new SparkConf();
  conf.setAppName("user-behaviour-analyse")
  conf.setMaster("local")
  var context = new SparkContext(conf)

  def main(args: Array[String]): Unit = {
    getUserView()
  }


  def getPageView(): Unit = {

    var fileData = context.textFile("data/userView.txt", 5);
    // 43.169.217.152	河北	2018-11-12	1542011088714	3292380437528494072	www.taobao.com	Login
    val pair: RDD[(String, Int)] = fileData.map(s => (s.split("\t")(5), 1))
    val wordCountRDD: RDD[(String, Int)] = pair.reduceByKey(_ + _)
    val pageViewResult = wordCountRDD.map(s => (s._2, s._1)).sortByKey(false).map(_.swap)
    val tuples: Array[(String, Int)] = pageViewResult.take(5)
    tuples.foreach(println)
  }


  def getUserView(): Unit = {
    var fileData = context.textFile("data/userView.txt", 5);
    // 43.169.217.152	河北	2018-11-12	1542011088714	3292380437528494072	www.taobao.com	Login
    val value: RDD[(String, String)] = fileData.map(line => {
      val strings = line.split("\t")
      (strings(5), strings(0))
    })
    val distinctData = value.distinct()

    val userViewResult = distinctData.map(s => (s._1, 1)).reduceByKey(_ + _).sortByKey(false)
    val tuples = userViewResult.take(5)
    tuples.foreach(println)
  }
}
