package cn.lwl.spark.example;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.VoidFunction;
import scala.Tuple2;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class WordCountJava {
    public static void main(String[] args) throws IOException {
        SparkConf conf = new SparkConf();
        conf.setMaster("local");
        conf.setAppName("WordCountJava");
        JavaSparkContext context = new JavaSparkContext(conf);
        JavaRDD<String> fileRDD = context.textFile("data/word.txt");
        JavaRDD<String> words = fileRDD.flatMap(x -> Arrays.stream(x.split(",")).iterator());
        JavaPairRDD<String, Integer> pairRDD = words.mapToPair(x -> new Tuple2<>(x, 1));
        JavaPairRDD<String, Integer> result = pairRDD.reduceByKey((x, y) -> x + y);
        result.foreach(new VoidFunction<Tuple2<String, Integer>>() {
            @Override
            public void call(Tuple2<String, Integer> stringIntegerTuple2) throws Exception {
                System.out.println(stringIntegerTuple2._1 + "  " + stringIntegerTuple2._2);
            }
        });
    }


    public static void generateFile() throws IOException {
        File file = new File("data/word.txt");
        FileWriter fileWriter = new FileWriter(file);
        int i = 0;
        while (i < 10000) {
            fileWriter.write("hello,spark," + i + "\n");
            i++;
        }
        fileWriter.close();
    }
}
