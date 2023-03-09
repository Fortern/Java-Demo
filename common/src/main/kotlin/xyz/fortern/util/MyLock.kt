package xyz.fortern.util

import java.util.concurrent.locks.Condition
import java.util.concurrent.locks.ReentrantLock

class MyLock : ReentrantLock() {
	val condition: Condition = super.newCondition()
}