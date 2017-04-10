package ru.epam.training;

import java.util.*;

public class CustomHashMap<K, V> implements Map<K, V> {

    private static final int DEFAULT_CAPACITY = 16;

    private CustomEntry<K, V>[] buckets = new CustomEntry[DEFAULT_CAPACITY];
    private int size = 0;

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        CustomEntry<K, V> bucket = buckets[0];
        if(bucket != null){
            return bucket.key.equals(key);
        }
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        CustomEntry<K, V> bucket = buckets[0];
        if (bucket != null) {
            return bucket.value.equals(value);
        }
        return false;
    }

    @Override
    @SuppressWarnings("unchecked")
    public V get(Object key) {
        CustomEntry<K, V> keyEntry = getEntryWithKey((K) key);
        return (keyEntry == null) ? null : keyEntry.getValue();
    }

    private CustomEntry<K, V> getEntryWithKey(K key) {
        CustomEntry<K, V> bucket = buckets[hash(key)];
        return getEntryInBucketWithKey(bucket, key);
    }

    private CustomEntry<K, V> getEntryInBucketWithKey(CustomEntry<K, V> bucket, K key) {
        CustomEntry<K, V> currEntry = bucket;
        while (currEntry != null) {
            if (currEntry.key.equals(key)) {
                return currEntry;
            }
            currEntry = currEntry.next();
        }
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public V put(K key, V value) {
        Objects.requireNonNull(key);
        int index = hash(key);

        if (buckets[index] == null) {
            buckets[index] = new CustomEntry<>(key, value);
            size++;
        } else {

            CustomEntry currEntry = getEntryWithKey(key);

            if (currEntry == null) {
                CustomEntry<K, V> newEntry = new CustomEntry<>(key, value);
                newEntry.next = buckets[index];
                buckets[index] = newEntry;
                size++;
            } else {
                V prevValue = (V) currEntry.value;
                currEntry.value = value;
                return prevValue;
            }
        }
        return null;
    }

    @Override
    public V remove(Object key) {
        int index = hash(key);
        CustomEntry<K, V> entry = buckets[hash(key)];
        CustomEntry<K, V> last = null;
        V temp = null;

        while (entry != null) {
            if (key.equals(entry.getKey())) {
                temp = entry.getValue();
                if (last == null)
                    buckets[index] = entry.next;
                else
                    last.next = entry.next;
                size--;
            }
            last = entry;
            entry = entry.next;
        }
        return temp;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {

    }

    @Override
    public void clear() {
        buckets = new CustomEntry[DEFAULT_CAPACITY];
        size = 0;
    }

    @Override
    public Set<K> keySet() {
        Set<K> keys = new HashSet<>();
        entrySet().forEach(x -> keys.add(x.getKey()));
        return keys;

    }

    @Override
    public Collection<V> values() {
        Set<Entry<K, V>> entries = entrySet();
        List<V> values = new ArrayList<>(entries.size());
        entries.forEach(x -> values.add(x.getValue()));
        return values;
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        Set<Entry<K, V>> entries = new HashSet<>();
        for (CustomEntry<K, V> bucket: buckets) {
            CustomEntry<K, V> entry = bucket;
            while (entry.hasNext()) {
                entries.add(entry);
                entry.next();
            }
        }
        return entries;
    }

    private int hash(Object key) {
        return Math.abs(key.hashCode() % buckets.length);
    }

    private class CustomEntry<K, V> implements Iterator<CustomEntry<K, V>>, Map.Entry<K, V> {

        private final K key;
        private V value;
        private CustomEntry<K, V> next = null;

        CustomEntry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public boolean hasNext() {
            return this.next != null ;
        }

        public CustomEntry<K, V> next() {
            return this.next;
        }

        void setNext(CustomEntry<K, V> next) {
            this.next = next;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            V old = this.value;
            this.value = value;
            return old;
        }
    }
}
