package xyz.fortern.simpletest;

import org.junit.jupiter.api.Test;

import java.util.function.Consumer;
import java.util.function.Function;

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
		Consumer<String> consumer = new Consumer<>() {
			@Override
			public void accept(String s) {
				System.out.println(s);
			}
		};
		Class<?> clazz1 = consumer.getClass();
		System.out.println("匿名内部类测试");
		//toString
		System.out.println("toString(): " + clazz1);//class xyz.fortern.simpletest.TestA$1
		//全类名
		System.out.println("getName()" + clazz1.getName());//xyz.fortern.simpletest.TestA$1
		//类的名称，如果是匿名内部类则为空字符串
		System.out.println("getSimpleName()" + clazz1.getSimpleName());//
		//标准名称，如果是匿名内部类则为null
		System.out.println("getCanonicalName()" + clazz1.getCanonicalName());//null
		
		System.out.println("=================");
		System.out.println("Lambda测试");
		Function<String, String> function = a -> a;
		Class<?> clazz2 = function.getClass();
		//toString
		System.out.println("toString(): " + clazz2);//class xyz.fortern.simpletest.TestA$$Lambda$343/0x0000000800ca27b8
		//全类名
		System.out.println("getName(): " + clazz2.getName());//xyz.fortern.simpletest.TestA$$Lambda$343/0x0000000800ca27b8
		//类的名称，如果是Lambda则为空字符串
		System.out.println("getSimpleName(): " + clazz2.getSimpleName());//TestA$$Lambda$343/0x0000000800ca27b8
		//标准名称，如果是Lambda则为null
		System.out.println("getCanonicalName(): " + clazz2.getCanonicalName());//null
		
		System.out.println("=================");
		System.out.println("普通类测试");
		Class<TestA> clazz3 = TestA.class;
		//toString
		System.out.println("toString(): " + clazz3);//class xyz.fortern.simpletest.TestA
		//全类名
		System.out.println("getName(): " + clazz3.getName());//xyz.fortern.simpletest.TestA
		//类的简单名称
		System.out.println("getSimpleName(): " + clazz3.getSimpleName());//TestA
		//标准名称
		System.out.println("getCanonicalName(): " + clazz3.getCanonicalName());//xyz.fortern.simpletest.TestA
	}
}
