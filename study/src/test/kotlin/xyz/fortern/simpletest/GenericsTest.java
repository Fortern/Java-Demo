package xyz.fortern.simpletest;

import java.util.ArrayList;
import java.util.List;

/**
 * 泛型通配符的理解
 */
public class GenericsTest {
	public static void main(String[] args) {
		List<MiniCat> miniCats = new ArrayList<>();//ArrayList是List的子（实现）类，因此可以赋值
		//List<Object> objs = strs; //List<String> 并不是 List<Object> 的子类型
		//objs.add(1);//通过List<Object>的变量存入数字
		//String s = strs.get(0);//通过List<String>取出String，产生类型转换错误
		
		//泛型String被擦除，存入Object类型
		miniCats.add(new MiniCat());
		
		//取出元素时强转为String
		MiniCat miniCat = miniCats.get(0);
		System.out.println(miniCat);
		
		List<? extends Cat> cats = miniCats;//这样赋值是允许的，看上去List<MiniCat>是List<? extends Cat>的子类型
		//这样赋值，只能从中读取数据，而不能写入数据（null除外？）
		
		cats.add(null);//因为类型擦除，可以存入Object(null)。但不能存入其他数据？因为没有(? extends Cat)类型
		//cats.add(new Cat());
		
		//字节码中get到的数据强转为Cat，因为cats的泛型是<? extends Cats>
		Cat cats1 = cats.get(0);//从Cat的集合中读取MiniCat没问题
		System.out.println(cats1);
		
		/*
		对于List<? extends CharSequence> charSequences只能从中读数据，不能向里面写数据(除了null)
		 */
		
		List<? super Cat> superAnimalList;
		
		List<Animal> objectList = new ArrayList<>();
		objectList.add(new Animal());
		
		superAnimalList = objectList;//这样赋值是允许的，看上去List<CharSequence>是List<? super String>的子类型
		
		//向Cat的集合中放Cat的子类是没有问题的
		superAnimalList.add(new Cat());
		superAnimalList.add(new MiniCat());
		//superAnimalList.add(new Animal());
		//只能写入不能“读取”
		
		//stringList的泛型是<? super String>，实际类型无法确定，因此取数据只能是Object类型
		Object object = superAnimalList.get(0);
	}
}

class Animal {
}

class Cat extends Animal {
}

class MiniCat extends Cat {
}