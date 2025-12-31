package deque;

public interface Deque <T>{
    void addLast(T item);
    void addFirst(T item);
    boolean isEmpty();
    int size();
    T removeFirst();
    T removeLast();
    T getFirst();
    T getLast();
    void printDeque();
    T get(int index);
}
