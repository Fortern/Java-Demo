package xyz.fortern.util

import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.Condition
import java.util.concurrent.locks.Lock

fun Lock.aWait(condition: Condition, time: Long, util: TimeUnit) {
	this.lock()
	condition.await(time, util)
	this.unlock()
}

fun Lock.aSignal(condition: Condition) {
	this.lock()
	condition.signal()
	this.unlock()
}
