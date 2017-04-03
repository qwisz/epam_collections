package ru.epam.training;

import java.lang.reflect.Array;
import java.util.*;

public class CustomLinkedList<T> implements List<T> {

    private Node<T> head = new Node<>(null);
    private int size = 0;

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return !head.hasNext();
    }

    @Override
    public boolean contains(Object o) {
        Node<T> node = head;
        while (node.hasNext()) {
            node = node.next;
            if (node.value == null) {
                if (o == null) {
                    return true;
                }
            } else if (node.value.equals(o)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Iterator<T> iterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        Object[] array = new Object[size];
        Node<T> e = head;
        for (int i = 0; i < size; i++) {
            array[i] = e.value;
            e = e.next;
        }
        return array;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T1> T1[] toArray(T1[] a) {
        if (a.length < size)
            a = (T1[]) Array.newInstance(a.getClass().getComponentType(), size);
        else if (a.length > size)
            a[size] = null;
        Node<T> e = head;
        for (int i = 0; i < size; i++) {
            a[i] = (T1) e.value;
            e = e.next;
        }
        return a;
    }

    @Override
    public boolean add(T t) {
        Node<T> iterator = head;
        while (iterator.hasNext()) {
            iterator = iterator.next;
        }
        iterator.next = new Node<>(t);
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        Node<T> current = head.next;
        Node<T> prev = head;
        while (current != null) {
            if (o.equals(current.value)) {
                prev.next = current.next;
                size--;
                return true;
            }
            prev = current;
            current = current.next;
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object e : c)
            if (!contains(e))
                return false;
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        c.forEach(this::add);
        return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {
        head = new Node<>(null);
        size = 0;
    }

    @Override
    public T get(int index) {
        return getNodeByIndex(index).value;
    }

    @Override
    public T set(int index, T element) {
        Node<T> node = getNodeByIndex(index);
        T old = node.value;
        node.value = element;
        return old;
    }

    @Override
    public void add(int index, T element) {
        Node<T> iterator = head;
        for (int i = 0; i < index - 1; i++) {
            iterator = iterator.next;
        }
        Node<T> temp = iterator.next;
        iterator.next = new Node<>(element);
        iterator.next.next = temp;
        size++;
    }

    @Override
    public T remove(int index) {
        Node<T> current = getNodeByIndex(index - 1);
        size--;
        T value = current.next.value;
        current.next = current.next.next;
        return value;
    }

    @Override
    public int indexOf(Object o) {
        int index = 0;
        if (o == null) {
            for (Node<T> x = head.next; x != null; x = x.next) {
                if (x.value == null)
                    return index;
            }
        } else {
            for (Node<T> x = head.next; x != null; x = x.next) {
                if (o.equals(x.value))
                    return index;
            }
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        return 0;
    }

    @Override
    public ListIterator<T> listIterator() {
        return listIterator(0);
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return new ListIterator<T>() {
            int current = index;

            @Override
            public boolean hasNext() {
                return current != size;
            }

            @Override
            public T next() {
                return getNodeByIndex(current++).value;
            }

            @Override
            public boolean hasPrevious() {
                return current != 0;
            }

            @Override
            public T previous() {
                return getNodeByIndex(current - 1).value;
            }

            @Override
            public int nextIndex() {
                return current + 1;
            }

            @Override
            public int previousIndex() {
                return current - 1;
            }

            @Override
            public void remove() {
                CustomLinkedList.this.remove(getNodeByIndex(current--));
            }

            @Override
            public void set(T element) {
                getNodeByIndex(index).value = element;
            }

            @Override
            public void add(T element) {
                CustomLinkedList.this.add(current++, element);
            }
        };
    }


    @Override
    @SuppressWarnings("unchecked")
    public List<T> subList(int fromIndex, int toIndex) {
        checkBounds(fromIndex);
        checkBounds(toIndex);
        checkBounds(toIndex - fromIndex);
        final LinkedList<T> list = new LinkedList<>();
        int elementsRemained = toIndex - fromIndex;
        for (Node node = getNodeByIndex(fromIndex); elementsRemained > 0; elementsRemained--) {
            list.add((T)node.value);
        }
        return list;
    }

    private void checkBounds(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException();
    }

    private Node<T> getNodeByIndex(int index) {

        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }

        Node<T> current = head.next;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current;
    }

    private class Node<T> {

        private Node<T> next;
        private T value;

        public Node(T value) {
            this.value = value;
        }

        public boolean hasNext() {
            return next != null;
        }

    }
}
