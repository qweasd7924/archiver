import jdk.nashorn.internal.AssertsEnabled;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by Павел on 28.11.2015.
 */
public class TestArch {
    @Test
    public void testZip(){
        ArchiverImpl arcImpl = new ArchiverImpl();
        Comparator comp = new Comparator();


        List<String> list = new ArrayList<String>();


        list.add("E:\\\\t1.txt");
        list.add("E:\\\\t2.txt");
        list.add("E:\\\\t3.txt");
//        list.add("E:\\\\1.mp4");
//        list.add("E:\\\\1.mp3");
//        list.add("E:\\\\p1.jpg");

        arcImpl.zip("E:\\\\zipNew.zip", list);

        int i = comp.compareZip("E:\\\\zipNew.zip" , "E:\\\\zipToComp.zip", list);

    assertEquals(1,i);
    }
}
