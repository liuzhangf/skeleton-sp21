package deque;

public interface Deque <T>{
    void addLast(T item);

    void addFirst(T item);
    int size();
    T removeFirst();
    T removeLast();
    void printDeque();
    T get(int index);
    default boolean isEmpty() {
        return size() == 0;
    }

}
