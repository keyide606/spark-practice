package cn.lwl.scala

/**
 * trait关键字,scala类可以多继承
 */
object ChapterFive {
  class Student {
    def study(): Unit = {
      println("study....")
    }
  }

  trait Singer {
    def sing(): Unit = {
      println("sing .....")
    }
  }


  class person(name: String) extends Student with Singer {
    def introduce(): Unit = {
      println(s"my name is $name,my hobby is ")
    }
  }


  def main(args: Array[String]): Unit = {
    val p = new person("zs")
    p.introduce()
    p.study()
    p.sing()
  }
}
