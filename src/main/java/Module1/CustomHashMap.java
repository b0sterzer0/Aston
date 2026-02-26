package Module1;

import java.util.*;

/**
 * Реализация хэш-таблицы (HashMap) с разрешением коллизий методом цепочек.
 * Поддерживает null-ключи (всегда помещаются в бакет с индексом 0).
 * Не является потокобезопасной.
 *
 * @param <K> тип ключей
 * @param <V> тип значений
 */
public class CustomHashMap<K, V> extends AbstractCustomHashMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int MAX_CAPACITY = 1 << 30;

    private Node<K, V>[] buckets;
    private int size = 0;
    private float loadFactor;
    private int capacity;
    private Set<K> keySet;

    public CustomHashMap() {
        this.capacity = DEFAULT_INITIAL_CAPACITY;
        this.loadFactor = DEFAULT_LOAD_FACTOR;
        initBuckets();
    }

    public CustomHashMap(int initialCapacity) {
        if (initialCapacity <= 0) throw new IllegalArgumentException("Начальная вместимость должна быть больше 0");
        if (initialCapacity > MAX_CAPACITY) initialCapacity = MAX_CAPACITY;

        this.capacity = initialCapacity;
        this.loadFactor = DEFAULT_LOAD_FACTOR;
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

    /**
     * Вычисляет хэш-код ключа с дополнительным перемешиванием битов.
     * Для null-ключа возвращает 0.
     */
    static final int hash(Object key) {
        if (key == null) return 0;
        int h = key.hashCode();
        return h ^ (h >>> 16);
    }

    /** Инициализирует массив бакетов текущей вместимостью. */
    private void initBuckets() {
        this.buckets = new Node[this.capacity];
    }

    /**
     * Вычисляет индекс бакета для ключа.
     * Для null-ключа всегда возвращает 0.
     */
    private int getBucketIndex(Object key) {
        if (key == null) return 0;
        int hash = hash(key);
        return (this.capacity - 1) & hash;
    }

    /**
     * Увеличивает вместимость массива бакетов вдвое и перераспределяет все элементы.
     * Вызывается автоматически при достижении порога (size > buckets.length * loadFactor).
     */
    private void resize() {
        if (this.capacity * 2 > MAX_CAPACITY) {
            this.capacity = MAX_CAPACITY;
        }
        else this.capacity *= 2;

        Node<K,V>[] newBuckets = new Node[this.capacity];

        for (Node<K, V> node : this.buckets) {
            while(node != null) {
                Node<K, V> next = node.next;
                int newIndex = getBucketIndex(node.getKey());
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
    public int size() {
        return this.size;
    }

    @Override
    public void clear() {
        for (int i = 0; i < this.buckets.length; i++) {
            this.buckets[i] = null;
        }
        this.size = 0;
        this.keySet = null;
    }

    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }

    /**
     * Возвращает неизменяемое множество всех ключей хэш-таблицы
     */
    @Override
    public Set<K> keySet() {
        if (this.keySet == null) {
            this.keySet = new HashSet<>();
            for (Node<K, V> node : this.buckets) {
                while (node != null) {
                    keySet.add(node.key);
                    node = node.next;
                }
            }
        }
        return Set.copyOf(this.keySet);
    }

    /**
     * Возвращает неизменяемый список всех значений ключей хэш-таблицы
     */
    @Override
    public List<V> values() {
        List<V> values = new ArrayList<>();
        for (Node<K, V> node : this.buckets) {
            while (node != null) {
                values.add(node.value);
                node = node.next;
            }
        }
        return List.copyOf(values);
    }

    /**
     * Возвращает значение, связанное с указанным ключом. Если нет такого ключа - null
     */
    @Override
    public V get(Object key) {
        V result = null;
        int keyHash = hash(key);
        int bucketIndex = getBucketIndex(key);

        Node<K, V> currentNode = this.buckets[bucketIndex];

        while (currentNode != null) {
            if (currentNode.hash == keyHash && (currentNode.getKey() == key || currentNode.getKey().equals(key))) {
                result = currentNode.getValue();
                break;
            }
            currentNode = currentNode.next;
        }

        return result;
    }

    /**
     * Возвращает значение по ключу или значение по умолчанию, если ключ не найден.
     */
    @Override
    public V getOrDefault(Object key, V defaultValue) {
        V result = get(key);
        return result == null ? defaultValue : result;
    }

    /**
     * Добавляет пару "ключ-значение" в хэш-таблицу.
     * Если ключ уже существует, заменяет соответствующее значение.
     */
    @Override
    public V put(K key, V value) {
        int bucketIndex = getBucketIndex(key);
        int keyHash = hash(key);

        Node<K, V> currentNode = this.buckets[bucketIndex];
        Node<K, V> prevNode = null;

        while (currentNode != null) {
            if (currentNode.hash == keyHash && (currentNode.getKey() == key || key.equals(currentNode.getKey()))) {
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
        } else {
            prevNode.next = new Node<>(keyHash, key, value, null);
        }

        this.size++;
        this.keySet = null;
        return value;
    }

    /**
     * Удаляет элемент по ключу и возвращает его значение. Если ключ не найден возвращает null
     */
    @Override
    public V remove(Object key) {
        V value = null;
        int bucketIndex = getBucketIndex(key);
        int keyHash = hash(key);

        Node<K, V> currentNode = this.buckets[bucketIndex];
        Node<K, V> prevNode = null;

        while (currentNode != null) {
            if (currentNode.hash == keyHash && (currentNode.key == key || currentNode.key.equals(key))) {
                value = currentNode.getValue();
                if (prevNode == null) this.buckets[bucketIndex] = currentNode.next;
                else prevNode.next = currentNode.next;
                this.size--;
                this.keySet = null;
                break;
            }
            prevNode = currentNode;
            currentNode = currentNode.next;
        }
        return value;
    }

    /**
     * Внутренний класс, представляющий узел связного списка.
     * Хранит пару "ключ-значение", хэш ключа и ссылку на следующий узел.
     */
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
