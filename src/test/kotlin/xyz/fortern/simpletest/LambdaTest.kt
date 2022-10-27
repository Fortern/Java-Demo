package xyz.fortern.simpletest

import java.util.function.Consumer

fun runTask(task: Runnable, i: Int) {
	println(task)
	println(i)
}

fun runTask(task: Consumer<Int>, i: Int) {
	task.accept(i)
}

fun main() {
	/*
	kotlin中的lambda，如果参数有0个或1个，都可以省略，因此写无参lambda，编译器不知道你要调用哪一个函数
	 */
	//上面两个runTask函数，注释掉任何一个，下面这行都可以执行。否则，编译器不知道下面这行代码第一个参数究竟是Runnable还是Consumer
	//runTask({ println() } , 5)
	runTask({ -> println() }, 5)//如果传入无参的lambda，箭头左侧不写参数即可
	runTask({ it -> println() }, 5)//如果传入1个参数的lambda，箭头左侧需要写出形参列表
	//也可以给Lambda指定具体的接口类型
	runTask(Runnable { println() }, 5)
	runTask(Consumer { println(it) }, 5)
}
