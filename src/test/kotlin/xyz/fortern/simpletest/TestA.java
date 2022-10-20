package xyz.fortern.simpletest;

import org.junit.jupiter.api.Test;

import java.util.function.Consumer;

public class TestA {
	@Test
	void testBinary() {
		int a = 0b11000000000000000000000000000000;//所有权限
		int b = 0b10000000000000000000000000000000;//第一位权限
		System.out.println(Integer.MIN_VALUE + " " + b);
		System.out.printf("%d\n", a);//十进制
		System.out.printf("%x\n", a);//十六进制
		System.out.printf("%b\n", a);//十六进制
		int c = a & b;
		System.out.println(Integer.toBinaryString(c));
		System.out.println(c);
	}
	
	@Test
	void stringWithNull() {
		System.out.println("abc" + null + "def");
	}
	
	@Test
	void printException() {
		try {
			throw new RuntimeException("手动抛出异常");
		} catch (RuntimeException e) {
			System.out.println(e);//异常堆栈的第一行信息，包含异常类型与描述信息
			System.out.println("======");
			System.out.println(e.getMessage());//异常的描述信息
			System.out.println("======");
			e.printStackTrace();//异常的完整信息
		}
	}
	
	@Test
	void printClassName() {
		Consumer<String> consumer = new Consumer<String>() {
			@Override
			public void accept(String s) {
				System.out.println(s);
			}
		};
		Class<? extends Consumer> clazz = consumer.getClass();
		System.out.println(clazz);//class xyz.fortern.simpletest.TestA
		System.out.println("======");
		//全类名
		System.out.println(clazz.getName());//xyz.fortern.simpletest.TestA
		System.out.println("======");
		//不包含包路径的类名如果是匿名内部类则为空字符串
		System.out.println(clazz.getSimpleName());//TestA
		System.out.println("======");
		//标准名称，如果是匿名内部类则为null
		System.out.println(clazz.getCanonicalName());//xyz.fortern.simpletest.TestA
	}
}
