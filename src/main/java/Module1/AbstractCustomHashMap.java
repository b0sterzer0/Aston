package Module1;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public abstract class AbstractCustomHashMap<K, V> implements Map<K, V> {

    public abstract V get(Object key);

    public abstract V put(K key, V value);

    public abstract V remove(Object key);

    @Override
    public V getOrDefault(Object key, Object defaultValue) {
        return null;
    }

    @Override
    public void forEach(BiConsumer action) {
    }

    @Override
    public void replaceAll(BiFunction function) {
    }

    @Override
    public V putIfAbsent(Object key, Object value) {
        return null;
    }

    @Override
    public boolean replace(Object key, Object oldValue, Object newValue) {
        return false;
    }

    @Override
    public V replace(Object key, Object value) {
        return null;
    }

    @Override
    public V computeIfAbsent(Object key, Function mappingFunction) {
        return null;
    }

    @Override
    public V computeIfPresent(Object key, BiFunction remappingFunction) {
        return null;
    }

    @Override
    public V compute(Object key, BiFunction remappingFunction) {
        return null;
    }

    @Override
    public V merge(Object key, Object value, BiFunction remappingFunction) {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean containsKey(Object key) {
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        return false;
    }

    @Override
    public void putAll(Map m) {

    }

    @Override
    public void clear() {

    }

    @Override
    public Set keySet() {
        return Set.of();
    }

    @Override
    public Collection values() {
        return List.of();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return Set.of();
    }

    @Override
    public boolean remove(Object key, Object value) {
        return false;
    }
}
