package hashmap;

import java.util.*;

public class MyHashMap<K, V> implements Map61B<K, V> {

    /* Instance Variables */
    private Collection<Node>[] buckets;
    // You should probably define some more!
    private double loadFactor = 0.75;
    private int size;

    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    /** Constructors */
    public MyHashMap() {
        buckets = createTable(16);
    }

    public MyHashMap(int initialSize) {
        buckets = createTable(initialSize);
    }

    public MyHashMap(int initialSize, double maxLoad) {
        buckets = createTable(initialSize);
        loadFactor = maxLoad;
    }

    private Node createNode(K key, V value) {
        return new Node(key, value);
    }

    protected Collection<Node> createBucket() {
        return new LinkedList<>();
    }

    private Collection<Node>[] createTable(int tableSize) {
        Collection<Node>[] ls = new Collection[tableSize];
        for (int i = 0; i < tableSize; i++) {
            ls[i] = createBucket();
        }
        return ls;
    }

    public void clear() {
        size = 0;
        for (int i = 0; i < buckets.length; i++) {
            buckets[i].clear();
        }
    }

    private int hash(K key, int length) {
    //    System.out.println((key.hashCode() & 0x7fffffff) % length);
        return (key.hashCode() & 0x7fffffff) % length;
    }

    /** Returns true if this map contains a mapping for the specified key. */
    public boolean containsKey(K key) {
        if (size == 0) {
            return false;
        }
        else {
            int hash = hash(key, buckets.length);
            for (Node node : buckets[hash]) {
                if (node.key.equals(key)) {
                    return true;
                }
            }
            return false;
        }
    }

    public V get(K key) {
        if (size == 0) {
            return null;
        }
        else {
            int hash = hash(key, buckets.length);
            if (buckets[hash] == null) {
                return null;
            }
            else {
                for (Node node : buckets[hash]) {
                    if (node.key.equals(key)) {
                        return node.value;
                    }
                }
            }
        }
        return null;
    }

    /** Returns the number of key-value mappings in this map. */
    public int size() {
        return size;
    }

    public void put(K key, V value) {
        if (size / (double) buckets.length > loadFactor) {
            resize();
        }
        if (containsKey(key)) {
            for (Node node : buckets[hash(key, buckets.length)]) {
                if (node.key.equals(key)) {
                    node.value = value;
                }
            }
        }
        else {
            int idx = hash(key, buckets.length);
            buckets[idx].add(new Node(key, value));
            size ++;
        }
    }

    private void resize() {
        Collection<Node>[] ls = createTable(buckets.length * 2);

        for (int i = 0; i < buckets.length; i++) {
            if (buckets[i] != null) {
                for (Node node : buckets[i]) {
                    int ind = hash(node.key, buckets.length * 2);
                    ls[ind].add(node);
                }
            }
        }
        buckets = ls;
    }

    @Override
    public Iterator<K> iterator() {
        return new HASHITERATOR();
    }

    private class HASHITERATOR implements Iterator<K> {

        int cnt = 0;
        @Override
        public boolean hasNext() {
            return cnt < size;
        }

        public K next () {
            if (hasNext()) {
                return find(cnt ++);
            }
            return null;
        }

    }

    private K find(int idx) {
        int lsnum = 0;
        for (int i = 0; i < buckets.length; i++) {
            if (buckets[i] != null) {
                for (Node node : buckets[i]) {
                    if (lsnum == idx) {
                        return node.key;
                    }
                    lsnum++;
                }
            }
        }
        throw new NoSuchElementException("Index out of bounds: " + idx);
    }

    /**
     * Returns a Set view of the keys contained in this map.
     */
    public Set<K> keySet() {
    //    System.out.println("size: " + size);
        Set<K> set = new HashSet<>();
        for (int i = 0; i < buckets.length; i++) {
            if (buckets[i] != null) {
                for (Node node : buckets[i]) {
                    set.add(node.key);
                }
            }
        }
    //    System.out.println("set: " + set.size());
        return set;
    }

    public V remove(K key) {
        V oldValue = null;
        if (containsKey(key)) {
            for (Node node : buckets[hash(key, buckets.length)]) {
                if (node.key.equals(key)) {
                    oldValue = node.value;
                    buckets[hash(key, buckets.length)].remove(node);
                    return oldValue;
                }
            }
        }
        return null;
    }

    public V remove(K key, V value) {
        if (containsKey(key)) {
            for (Node node : buckets[hash(key, buckets.length)]) {
                if (node.key.equals(key)) {
                    if (node.value == value) {
                        V oldValue = node.value;
                        buckets[hash(key, buckets.length)].remove(node);
                        return oldValue;
                    }
                }
            }
        }
        return null;
    }
}
