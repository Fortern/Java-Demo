package xyz.fortern.simpletest;

import org.junit.jupiter.api.Test;

public class ThreadTest {
	
	@Test
	void test1() {
		Thread thread1 = new Thread(() -> {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});
		thread1.start();
		//在sleep期间，线程被调用interrupt()方法，会抛出
		thread1.interrupt();
	}
	
	@Test
	void test2() throws InterruptedException {
		Thread thread = new Thread() {
			@Override
			public void run() {
				System.out.println("线程开始");
				System.out.println("线程中断");
				this.interrupt();//线程中断0
				System.out.println("线程睡眠");
				try {
					sleep(1000);
				} catch (InterruptedException e) {
					//先中断再sleep也会抛出InterruptedException
					e.printStackTrace();
				}
			}
		};
		
		thread.start();
	}
}
