package deque;

import java.util.Comparator;
import java.util.Iterator;

public class MaxArrayDeque<T> extends ArrayDeque<T>implements Deque<T>, Iterable<T> {
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

        Iterator<T> itr = this.iterator();
        while (itr.hasNext()) {
            T val = itr.next();
            if (c.compare(val, max_val) > 0) {
                max_val = val;
            }
        }
        return max_val;
    }
}