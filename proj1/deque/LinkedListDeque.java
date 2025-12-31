package deque;

import java.util.Iterator;

public class LinkedListDeque<T> {

    public class Node {
        T data;
        Node next;
        Node prev;
        public Node(T data, Node next, Node prev) {
            this.data = data;
            this.next = next;
            this.prev = prev;
        }
    }

    Node head;
    Node tail;
    private int size;
    public LinkedListDeque(){
        head = new Node(null,null,null);
        tail = new Node(null,null,null);
        head.next = tail;
        tail.prev = head;
        size = 0;
    }

    public void addFirst(T item){
        head.next.prev = new Node(item, head.next, head);
        head.next = head.next.prev;
        size++;
    }

    public void addLast(T item){
        tail.prev.next = new Node(item, tail, tail.prev);
        tail.prev = tail.prev.next;
        size++;
    }

    public int size(){
        return size;
    }

    public boolean isEmpty(){
        return size == 0;
    }

    public void printDeque(){
        Node current = head.next;
        while(current != null){
            System.out.print(current.data + " ");
            current = current.next;
        }
        System.out.println();
    }

    public T removeFirst(){
        Node current = head.next;
        if(tail.prev == head){
            return null;
        }
        else{
            head.next = current.next;
            size--;
            current.next.prev = head;
            return current.data;
        }
    }

    public T get(int index){
        int cnt = 0;
        Node current = head;
        while(cnt != index + 1 && current != null){
            current = current.next;
            cnt++;
        }
        if(current == null){
            return null;
        }
        return current.data;
    }

    public T getRecursive(int index){
        if(index < 0 || index >= size){
            return null;
        }
        else {
            return getRecursiveHelper( head.next,index);
        }
    }

    public T getRecursiveHelper(Node node, int index){
        if(node == null){
            return null;
        }
        else{
            return getRecursiveHelper(node.next,index - 1);
        }
    }

    public T removeLast() {
        Node current = tail.prev;
        if(tail.prev == head){
            return null;
        }
        else{
            tail.prev = current.prev;
            size--;
            current.prev.next = tail;
            return current.data;
        }
    }

    public Iterator<T> iterator(){
        return new LinkedListIterator();
    }

    private  class LinkedListIterator implements Iterator<T>{
        Node current = head.next;
        @Override
        public boolean hasNext(){
            return current != tail;
        }
        @Override
        public T next(){
            if(hasNext()){
                Node next = current;
                current = current.next;
                return next.data;
            }
            return null;
        }
    }

    @Override
    public boolean equals(Object o){
        if (!(o instanceof Deque)) { // 关键：必须用 instanceof
            return false;
        }

        Deque<T> other = (Deque<T>) o;
        if(size != other.size()){
            return false;
        }
        Node current = head.next;
        int cnt = 0;
        while(cnt != other.size()){
            T left = (T) ((Deque<?>) o).get(cnt);
            T right = get(cnt);
            if (left == null && right == null) {
                continue;
            }
            if (left == null || right == null) {
                return false;
            }
            if (!(left.equals(right))) {
                return false;
            }
            cnt++;
        }
        return true;
    }

}
