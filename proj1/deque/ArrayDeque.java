package deque;

import java.util.Iterator;


public class ArrayDeque<T> implements Deque<T>, Iterable<T> {

    private T[] item;
    private int head;
    private int tail;
    private int size;


    public ArrayDeque() {
        item = (T[]) new Object[8];
        head = 0;
        tail = 0;
    }

    private void resize(int capacity) {
        T[] a = (T[]) new Object[capacity];
        int mod = item.length;
        int newhead = 0;
        for(int i = 0; i < size; i = (i + 1)) {
            a[(newhead++) % capacity] = item[(head + i) % mod];
        }
        item = a;
        head = 0;
        tail = newhead;
    }

    /*
      head 指向的是当前队列的第一个元素
      tail 都是分别指向要填入的位置
    */

    public void addFirst(T x){
        if(size == item.length) {
            resize(item.length * 2);
        }
        item[(head - 1 + item.length)  % item.length] = x;
        head =  (head - 1 + item.length)  % item.length;
        size++;
    }

    public void addLast(T x){
        if(size == item.length){
            resize(item.length * 2);
        }
        item[tail] = x;
        tail = (tail + 1) % item.length;
        size++;
    }

    public int size(){
        return size;
    }

    public void printDeque() {
        for (int i = 0; i < size; i++) {
            int idx = (head + i) % item.length;
            System.out.print(item[idx] + " ");
        }
        System.out.println();
    }

    public T removeFirst(){
        if(isEmpty()){
            return null;
        }
        T x = item[head];
        head  = (head + 1) % item.length;
        size--;

        if (size <= item.length / 2 && size >= 16){
            resize(item.length / 2);
        }

        return x;
    }

    public T removeLast(){
        if(isEmpty()){
            return null;
        }
        tail = (tail - 1 + item.length) % item.length;
        T x = item[tail];
        size--;
        if (size <= item.length / 2 && size >= 8){
            resize(item.length / 2);
        }
        return x;
    }

    public T get(int index){
        if(index < 0 || index >= size){
            return null;
        }
        return item[(head + index) % item.length];
    }

    public Iterator<T> iterator(){
        return new ArrayListIterator();
    }

    private class ArrayListIterator implements Iterator<T>{
        int cnt = 0;
        public boolean hasNext(){
            return cnt < size;
        }

        public T next() {
            if (hasNext()) {
                return get(cnt++);
            }
            return null;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Deque)) {
            return false;
        }
        if (size() != ((Deque<?>) o).size()) {
            return false;
        }
        for (int i = 0; i < size(); i++) { // Time Complexity high
            T left = (T) ((Deque<?>) o).get(i);
            T right = get(i);
            if (left == null && right == null) {
                continue;
            }
            if (left == null || right == null) {
                return false;
            }
            if (!(left.equals(right))) {
                return false;
            }
        }
        return true;
    }
}
