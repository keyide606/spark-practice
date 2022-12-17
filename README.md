# spark-practice
学习spark过程中的代码

scala学习参考:https://www.runoob.com/scala/scala-tutorial.html

RDD五个主要属性:

*  - A list of partitions:一个分区(切片)列表
*  - A function for computing each split:一个函数作用于每个切片上
*  - A list of dependencies on other RDDs：和其他RDD的依赖列表
*  - Optionally, a Partitioner for key-value RDDs (e.g. to say that the RDD is hash-partitioned)：可选的，一个分区器作用于key-value的rdd
*  - Optionally, a list of preferred locations to compute each split on (e.g. block locations for
    an HDFS file)：可选的，计算每个切片时一个切片位置列表
     
