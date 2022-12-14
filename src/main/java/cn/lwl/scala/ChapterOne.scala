package cn.lwl.scala

/**
 * 学习scala中的object和class
 * var和val关键字
 */
object ChapterOne {
  println("静态变量!!!")
  val title = "study for spark!"

  /**
   * 1. main方法只能放在object定义的文件中
   * 2. object和class区别：
   * object：可以认为是静态的（单例对象），里面的方法可以直接使用，
   * scala中的object和class可以同名，认为是伴生关系
   * class： 类中的方法必须new一个对象，然后再使用
   * 3. java和scala区别
   * java：文件名和类型要一致
   * scala：文件只是一个载体，不要求文件名和类名一致；编译器会扫描文件中的信息，
   * 生成文件中所有类的信息，保证文件不能有相同的类
   * java类中不能写一些逻辑语句，scala中object和class可以写逻辑语句，
   * 这些逻辑语句在object main方法执行前执行,在class的构造方法执行前执行
   * 同时它们按照文件的前后顺序执行
   *
   * @param args
   */
  def main(args: Array[String]): Unit = {
    println("hello,scala!")
    // scala拼接字符串
    // val 修饰的变量不能改变，类似final
    val language = "scala"
    println(s"study,$language!")
    val student = new Student("computer")
    println(s"score=${student.score},age=${student.age}")
  }

  println(s"静态变量------,$title")
}

/**
 * scala类中默认构造方法不需要：类中裸露的代码是默认构造的
 * 个性化构造方法：def this
 * 类名构造器中的参数就是类的成员变量,默认是不可变的 val类型,private;可以进行修改成var类型
 * 只有在类名构造器上,参数可以申明类型;其他方法都不允许,其他方法参数类型都是val 不可修改
 */
class Student(department: String) {
  println("study is very happy!")
  var score = 3
  var age = 20

  def this(score: Int, age: Int) {
    // 必须首先调用默认构造
    this("computer")
    this.score = score
    this.age = age
  }

  println(s"score=$score")

  def printMessage(s: String): Unit = {
    println(ChapterOne.title)
    println("lol is so boring!")
    println(s"i like $department ")
  }

  println(s"age=${age - 1}")

  println("end---!!")
}
