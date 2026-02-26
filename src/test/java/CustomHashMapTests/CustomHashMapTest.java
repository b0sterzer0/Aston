package CustomHashMapTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import Module1.CustomHashMap;

class CustomHashMapTest {

    private CustomHashMap<String, Integer> map;

    @BeforeEach
    void setUp() {
        map = new CustomHashMap<>();
    }

    @Test
    void testPutAndGet() {
        map.put("one", 1);
        map.put("two", 2);
        map.put("three", 3);

        assertEquals(1, map.get("one"));
        assertEquals(2, map.get("two"));
        assertEquals(3, map.get("three"));
        assertEquals(3, map.size());
    }

    @Test
    void testUpdateExistingKey() {
        map.put("key", 100);
        map.put("key", 200);

        assertEquals(200, map.get("key"));
        assertEquals(1, map.size());
    }

    @Test
    void testGetNonExistentKey() {
        assertNull(map.get("nonexistent"));
    }

    @Test
    void testGetOrDefault() {
        assertEquals(999, map.getOrDefault("nonexistent", 999));
        map.put("exists", 42);
        assertEquals(42, map.getOrDefault("exists", 999));
    }

    @Test
    void testRemove() {
        map.put("key", 100);
        assertEquals(100, map.remove("key"));
        assertNull(map.get("key"));
        assertEquals(0, map.size());
    }

    @Test
    void testRemoveNonExistentKey() {
        assertNull(map.remove("nonexistent"));
    }

    @Test
    void testClear() {
        map.put("one", 1);
        map.put("two", 2);
        map.clear();

        assertTrue(map.isEmpty());
        assertEquals(0, map.size());
        assertNull(map.get("one"));
        assertNull(map.get("two"));
    }

    @Test
    void testIsEmpty() {
        assertTrue(map.isEmpty());
        map.put("key", 100);
        assertFalse(map.isEmpty());
    }

    @Test
    void testSize() {
        assertEquals(0, map.size());
        map.put("one", 1);
        assertEquals(1, map.size());
        map.put("two", 2);
        assertEquals(2, map.size());
        map.remove("one");
        assertEquals(1, map.size());
    }

    @Test
    void testNullKey() {
        map.put(null, 100);
        assertEquals(100, map.get(null));

        map.put(null, 200);
        assertEquals(200, map.get(null));
        assertEquals(1, map.size());

        assertEquals(200, map.remove(null));
        assertNull(map.get(null));
    }

    @Test
    void testNullValue() {
        map.put("key", null);
        assertNull(map.get("key"));
    }

    @Test
    void testKeySet() {
        map.put("one", 1);
        map.put("two", 2);

        var keys = map.keySet();
        assertEquals(2, keys.size());
        assertTrue(keys.contains("one"));
        assertTrue(keys.contains("two"));
    }

    @Test
    void testValues() {
        map.put("one", 1);
        map.put("two", 2);

        var values = map.values();
        assertEquals(2, values.size());
        assertTrue(values.contains(1));
        assertTrue(values.contains(2));
    }

    @Test
    void testResize() {
        // Добавляем много элементов для принудительного расширения
        for (int i = 0; i < 100; i++) {
            map.put("key" + i, i);
        }

        assertEquals(100, map.size());
        for (int i = 0; i < 100; i++) {
            assertEquals(i, map.get("key" + i));
        }
    }

    @Test
    void testHashCollision() {
        // Тест на коллизию - используем ключи с одинаковым хэшем
        CustomHashMap<SameHashKey, String> collisionMap = new CustomHashMap<>();

        SameHashKey key1 = new SameHashKey(1);
        SameHashKey key2 = new SameHashKey(2);

        collisionMap.put(key1, "first");
        collisionMap.put(key2, "second");

        assertEquals("first", collisionMap.get(key1));
        assertEquals("second", collisionMap.get(key2));
    }

    // Вспомогательный класс для тестирования коллизий
    private static class SameHashKey {
        private final int id;

        SameHashKey(int id) {
            this.id = id;
        }

        @Override
        public int hashCode() {
            return 42; // Все ключи имеют одинаковый хэш
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            SameHashKey that = (SameHashKey) obj;
            return id == that.id;
        }
    }
}