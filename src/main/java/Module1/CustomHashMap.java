package Module1;

import java.util.Map;
import java.util.Objects;

public class CustomHashMap<K, V> extends AbstractCustomHashMap<K, V> {
    static final int DEFAULT_INITIAL_CAPACITY = 16;
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    static final int MAX_CAPACITY = 1 << 30;

    Node<K, V>[] buckets;
    int size = 0;
    float loadFactor;
    int capacity;

    public CustomHashMap() {
        this.capacity = DEFAULT_INITIAL_CAPACITY;
        this.loadFactor = DEFAULT_LOAD_FACTOR;
        initBuckets();
    }

    public CustomHashMap(int initialCapacity) {
        if (initialCapacity <= 0) throw new IllegalArgumentException("Начальная вместимость должна быть больше 0");
        if (initialCapacity > MAX_CAPACITY) initialCapacity = MAX_CAPACITY;

        this.capacity = initialCapacity;
        initBuckets();
    }

    public CustomHashMap(int initialCapacity, float loadFactor) {
        if (initialCapacity <= 0) throw new IllegalArgumentException("Начальная вместимость должна быть больше 0");
        if (initialCapacity > MAX_CAPACITY) initialCapacity = MAX_CAPACITY;
        if (loadFactor <= 0) throw new IllegalArgumentException("Параметр расширения массива должен быть больше 0");

        this.capacity = initialCapacity;
        this.loadFactor = loadFactor;
        initBuckets();
    }

    static final int hash(Object key) {
        if (key == null) return 0;
        int h = key.hashCode();
        return h ^ (h >>> 16);
    }

    private void initBuckets() {
        this.buckets = new Node[this.capacity];
    }

    private int getBucketIndex(Object key) {
        if (key == null) return 0;
        int hash = hash(key);
        return (this.capacity - 1) & hash;
    }

    private void resize() {
        this.capacity *= 2;
        Node<K,V>[] newBuckets = new Node[this.capacity];

        for (Node<K, V> node : this.buckets) {
            while(node != null) {
                Node<K, V> next = node.next;
                int newIndex = getBucketIndex(node.key);
                node.next = null;
                if (newBuckets[newIndex] == null) newBuckets[newIndex] = node;
                else {
                    node.next = newBuckets[newIndex];
                    newBuckets[newIndex] = node;
                }
                node = next;
            }
        }
        this.buckets = newBuckets;
    }

    @Override
    public V get(Object key) {
        V result = null;
        int keyHash = hash(key);
        int bucketIndex = getBucketIndex(key);
        Node<K, V> currentNode = this.buckets[bucketIndex];
        while (currentNode != null) {
            if (currentNode.hash == keyHash && (currentNode.key == key || currentNode.key.equals(key))) {
                result = currentNode.value;
                break;
            }
            currentNode = currentNode.next;
        }
        return result;
    }

    @Override
    public V put(K key, V value) {
        int bucketIndex = getBucketIndex(key);
        int keyHash = hash(key);

        Node<K, V> currentNode = this.buckets[bucketIndex];
        Node<K, V> prevNode = null;

        while (currentNode != null) {
            if (currentNode.hash == keyHash && (currentNode.key == key || key.equals(currentNode.key))) {
                currentNode.setValue(value);
                return value;
            }
            prevNode = currentNode;
            currentNode = currentNode.next;
        }

        if ((this.size + 1) >= (this.buckets.length * this.loadFactor)) {
            resize();
            bucketIndex = getBucketIndex(key);
        }

        if (this.buckets[bucketIndex] == null) {
            this.buckets[bucketIndex] = new Node<>(keyHash, key, value, null);
        } else prevNode.next = new Node<>(keyHash, key, value, null);

        this.size++;
        return value;
    }

    @Override
    public V remove(Object key) {
        return null;
    }

    static class Node<K, V> implements Map.Entry<K, V> {
        final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        @Override
        public K getKey() {
            return this.key;
        }

        @Override
        public V getValue() {
            return this.value;
        }

        @Override
        public V setValue(V value) {
            this.value = value;
            return value;
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(key) ^ Objects.hashCode(value);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;

            return obj instanceof Map.Entry<?, ?> e
                    && Objects.equals(key, e.getKey())
                    && Objects.equals(value, e.getValue());
        }

        @Override
        public String toString() {
            return this.key + " = " + this.value;
        }
    }
}
