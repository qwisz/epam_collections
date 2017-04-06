package ru.epam.training;

import javax.annotation.processing.SupportedSourceVersion;
import java.util.*;

public class CustomTreeMap<K extends Comparable<K>, V> implements Map<K, V> {

    private Node<K, V> root;
    private int size = 0;
    private final Comparator<K> comparator;

    public CustomTreeMap() {
        this.comparator = null;
    }

    public CustomTreeMap(Comparator<K> comparator) {
        this.comparator = comparator;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return (size == 0 && root == null);
    }

    @Override
    public boolean containsKey(Object key) {
        Objects.requireNonNull(key);

        if (root == null) return false;
        root.key.compareTo((K) key);
        return find(root, (K) key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        if (root == null) return false;
        if (root.value == null) {
            return value == null;
        } else {
            return root.value.equals(value);
        }
    }

    @Override
    public V get(Object key) {
        Objects.requireNonNull(key);
        return findNode(root, (K) key).getValue();
    }

    private Node<K, V> findNode(Node<K, V> node, K key) {
        if (node == null) {
            return null;
        } else if (node.getKey().compareTo(key) < 0) {
            return findNode(node.right, key);
        } else if (node.getKey().compareTo(key) > 0) {
            return findNode(node.left, key);
        } else {
            return node;
        }
    }

    @Override
    public V put(K key, V value) {
        Objects.requireNonNull(key);
        root = put(root, key, value);
        return value;
    }

    private Node<K, V> put(Node<K, V> node, K key, V value) {
        if (node == null) {
            size++;
            return new Node<>(key, value);
        }
        if (node.key.equals(key)) {
            node.value = value;
        } else if (node.key.compareTo(key) > 0) {
            node.left = put(node.left, key, value);
        } else {
            node.right = put(node.right, key, value);
        }
        size++;
        return node;
    }

    private Node<K, V> find(Node<K, V> node, K key) {
        if (node == null) {
            return null;
        }
        if (node.key.equals(key)) {
            return node;
        } else if (node.key.compareTo(key) > 0) {
            return find(node.left, key);
        } else {
            return find(node.right, key);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public V remove(Object key) {
        if (key == null) {
            throw new NullPointerException("value is null");
        }
        if (root == null) {
            return null;
        }
        Node parent = root;
        Node curr = root;
        int cmp;
        while ((cmp = compare((K)  curr.value, (K) key)) != 0) {
            parent = curr;
            if (cmp > 0) {
                curr = curr.left;
            } else {
                curr = curr.right;
            }
            if (curr == null) {
                return null; // ничего не нашли
            }
        }
        if (curr.left != null && curr.right != null) {
            Node next = curr.right;
            Node pNext = curr;
            while (next.left != null) {
                pNext = next;
                next = next.left;
            } //next = наименьший из больших
            curr.value = next.value;
            next.value = null;
            //у правого поддерева нет левых потомков
            if (pNext == curr) {
                curr.right = next.right;
            } else {
                pNext.left = next.right;
            }
            next.right = null;
        } else {
            if (curr.left != null) {
                reLink(parent, curr, curr.left);
            } else if (curr.right != null) {
                reLink(parent, curr, curr.right);
            } else {
                reLink(parent, curr, null);
            }
        }
        size--;
        return (V) curr.value;
    }

    @SuppressWarnings("unchecked")
    private void reLink(Node parent, Node curr, Node child) {
        if (parent == curr) {
            root = child;
        } else if (parent.left == curr) {
            parent.left = child;
        } else {
            parent.right = child;
        }
        curr.value = null;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        Objects.requireNonNull(m);
        for (Map.Entry<? extends K, ? extends V> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void clear() {
        size = 0;
        root = null;
    }

    @Override
    public Set<K> keySet() {

        Set<K> keySet = new HashSet<>();
        for (Entry<K, V> node : getNodes()) {
            keySet.add(node.getKey());
        }
        return keySet;
    }

    @Override
    public Collection<V> values() {
        List<V> values = new ArrayList<>();
        for (Entry<K, V> node : getNodes()) {
            values.add(node.getValue());
        }
        return values;
    }

    private List<Map.Entry<K, V>> getNodes() {
        List<Map.Entry<K, V>> nodes = new ArrayList<>();
        orderedNodes(root, nodes);
        return nodes;
    }

    private void orderedNodes(Node<K, V> node, List<Map.Entry<K, V>> nodes) {
        if (node != null) {
            orderedNodes(node.left, nodes);
            nodes.add(node);
            orderedNodes(node.right, nodes);
        }
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return new HashSet<>(getNodes());
    }


    private int compare(K v1, K v2) {
        return comparator == null ? v1.compareTo(v2) : comparator.compare(v1, v2);
    }

    private class Node<K extends Comparable<K>, V> implements Map.Entry<K, V> {
        private final K key;
        private V value;
        private Node<K, V> left;
        private Node<K, V> right;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            V prev = this.value;
            this.value = value;
            return prev;
        }

        public K getKey() {
            return key;
        }
    }

}