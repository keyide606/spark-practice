package cn.lwl.scala

import java.util.Date

/**
 * 学习scala中的函数
 */
object ChapterThree {
  def main(args: Array[String]): Unit = {
    // 无参数函数
    def getAddress(): Unit = {
      println("shanghai")
    }

    var x = getAddress()
    println(x)

    println("------有参函数-------")

    // 有参数函数
    def add(i: Int, j: Int): Int = {
      return i + j
    }

    var sum = add(3, 2);
    println(sum)

    println("----递归函数---")

    def fib(n: Int): Int = {
      if (n == 1 || n == 2) {
        return 1;
      }
      fib(n - 1) + fib(n - 2)
    }

    var f = fib(6)
    println(f)

    println("----默认值函数-----")

    def getName(a: String = "zs", b: String = "lj"): String = {
      return s"$a-$b"
    }

    var z = getName("li", "jy")
    println(z)

    println("---匿名函数---")
    // 签名(Int, Int) => Int (参数列表类型)=>返回值类型
    // 匿名函数(a: Int, b: Int) => {a + b} (参数列表实现)=>{函数体}
    var y: (Int, Int) => Int = (a: Int, b: Int) => {
       a + b
    }
    println(y(3, 4))

    var compare: (Int, Int) => Int = (a: Int, b: Int) => {
      if (a > b) {
          1
      } else if (a < b) {
         -1
      } else {
         0
      }
    }
    println(compare(3,4))

    println("----嵌套函数---")

    def findMembers(): Int = {
      def member(): Int = {
        3
      }

      member()
    }

    var r = findMembers()
    println(r)

    println("----偏应用函数---")

    def log(date: Date, tp: String, message: String): Unit = {
      println(s"$date $tp $message")
    }

    def errorLog = log(_: Date, "error", _: String)

    def infoLog = log(_: Date, "info", _: String)

    errorLog(new Date(), "ok")
    infoLog(new Date(), "exception")

    println("----可变参数---")

    def getParams(a: Int*): Unit = {
      a.foreach(i => println(i))
    }

    getParams(1, 2, 3)

    println("----高阶参数---")

    // 函数作为变量,参数是函数的签名,给出类型
    def computer(a: Int, b: Int, f: (Int, Int) => Int): Int = {
      f(a, b)
    }

    var result = computer(3, 8, (a: Int, b: Int) => {
      a + b
    }
    )
    println(result)

    def getComputer(tp: String): (Int, Int) => Int = {
      if (tp.equals("+")) {
        return (a: Int, b: Int) => {
          a + b
        }
      } else {
        return (a: Int, b: Int) => {
          a * b
        }
      }
    }

    var g: (Int, Int) => Int = getComputer("*")
    println(g(2, 3))

    println("---柯里化-----")

    def fun(a: Int*)(b: String*): Unit = {
      a.foreach(println)
      b.foreach(println)
    }

    fun(1, 3, 4)("123", "456")
  }
}
