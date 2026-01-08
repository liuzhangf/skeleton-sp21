package tester;

import static org.junit.Assert.*;
import org.junit.Test;
import student.StudentArrayDeque;
import java.util.Random;

import java.util.Random;

public class TestArrayDequeEC {
    Random random = new Random(0);
    @Test
    public void test() {
        StudentArrayDeque<Integer> sd = new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> ad = new ArrayDequeSolution<>();
        String message = "";
        int cnt = 0;
        while (true) {
            if (cnt >= 10000){
                break;
            }
            int op = random.nextInt(4);
            String lsmessage = "";
            switch (op) {
                case 0:
                    int a = random.nextInt(10000);
                    sd.addFirst(a);
                    ad.addFirst(a);
                    lsmessage += "addFirst(" + a + ")\n";
                    message += lsmessage;
                    break;
                case 1:
                    int b = random.nextInt(10000);
                    ad.addLast(b);
                    sd.addLast(b);
                    lsmessage += "addLast(" + b + ")\n";
                    message += lsmessage;
                    break;
                case 2:
                    if (!sd.isEmpty() && !ad.isEmpty()) {
                        Integer aa = (Integer) sd.removeFirst();
                        Integer bb = (Integer) ad.removeFirst();
                        lsmessage += "removeFirst()\n";
                        message += lsmessage;
                        assertEquals(message, aa, bb);
                    }
                    break;
                case 3:
                    if (!sd.isEmpty() && !ad.isEmpty()) {
                        Integer aa = (Integer) sd.removeLast();
                        Integer bb = (Integer) ad.removeLast();
                        lsmessage += "removeLast()\n";
                        message += lsmessage;
                        assertEquals(message, aa, bb);
                    }
                    break;
                }
            cnt ++;
        }
    }
}
