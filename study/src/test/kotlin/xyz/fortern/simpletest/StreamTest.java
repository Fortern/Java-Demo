package xyz.fortern.simpletest;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class StreamTest {
	
	@Test
	public void test1() {
		var stringList = Arrays.asList(1, 2, 3, 4);
		var map = stringList.stream()
				.collect(HashMap::new,
						(BiConsumer<Map<Integer, Integer>, Integer>) (hashMap, integer) -> hashMap.put(integer, 2 * integer),
						Map::putAll);
		map.forEach((k, v) -> System.out.println(k + "=" + v));
	}
	
}
