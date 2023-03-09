package xyz.fortern.util

import java.util.concurrent.TimeUnit

fun MyLock.aWait(time: Long, util: TimeUnit) {
	this.lock()
	this.condition.await(time, util)
	this.unlock()
}

fun MyLock.aSignal() {
	this.lock()
	this.condition.signal()
	this.unlock()
}
