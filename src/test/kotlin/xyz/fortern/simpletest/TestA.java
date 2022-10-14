package xyz.fortern.simpletest;

import org.junit.jupiter.api.Test;

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
}
