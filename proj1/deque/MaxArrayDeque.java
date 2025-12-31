package deque;

import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T> {
    private final Comparator<T> comparator;
    public MaxArrayDeque(Comparator<T> c) {
        comparator = c;
    }

    public T max() {
        return MaxHelper(comparator);
    }

    public T max(Comparator<T> c) {
        return MaxHelper(c);
    }

    private T MaxHelper( Comparator<T> c ) {
        T max_val = get(0);

        if (isEmpty()) {return null;}
        else {
            for (int i = 1; i < size(); i++) {
                if (c.compare(get(i), max_val) > 0) {
                    max_val = get(i);
                }
            }
        }
        return max_val;
    }
}