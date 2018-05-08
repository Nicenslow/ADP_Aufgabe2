package tuerme_von_Hanoi;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Test;

public class HanoiLoesungTest {

    
	@Test
    public void hanoi() {
        HanoiLoesung hanoi = new HanoiLoesung();
        int n;

        n = 1;
        ArrayList<Paar<Integer>> list = hanoi.hanoi(n);

        assertEquals((int)Math.pow(2, n) - 1, list.size());
        assertEquals("0->2", list.get(0).toString());
       

        n = 3;
        list = hanoi.hanoi(n);
        assertEquals((int)Math.pow(2, n) - 1, list.size());

        n = 8;
        list = hanoi.hanoi(n);
        assertEquals((int)Math.pow(2, n) - 1, list.size());

        n = 12;
        list = hanoi.hanoi(n);
        assertEquals((int)Math.pow(2, n) - 1, list.size());

        n = 14;
        list = hanoi.hanoi(n);
        assertEquals((int)Math.pow(2, n) - 1, list.size());

        n = 14;
        list = hanoi.hanoi(n);
        assertEquals((int)Math.pow(2, n) - 1, list.size());
        
    }
}
