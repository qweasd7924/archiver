import java.io.*;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Created by Павел on 04.12.2015.
 */
public class Comparator {
    UsefulMeth1 meth = new UsefulMeth1();

    private int i = 1;
    public int compareZip(String pathToZip1,String pathToZip2, List<String> list){
        Iterator<String> it = list.iterator();
            while(it.hasNext()){
                try{
                    String pathToFile = it.next();
                    ZipFile zipFile1 = new ZipFile(pathToZip1);
                    ZipFile zipFile2 = new ZipFile(pathToZip2);
                    ZipEntry zentry1 = new ZipEntry(meth.nameForZipEntry(pathToFile));
                    ZipEntry zentry2 = new ZipEntry(meth.nameForZipEntry(pathToFile));

                    int j=1;
                    try(InputStream is1 = zipFile1.getInputStream(zentry1)) {
                        try(InputStream is2 = zipFile2.getInputStream(zentry2)) {
                            byte[] buffer1 = new byte[1024];
                            byte[] buffer2 = new byte[1024];
                            while (is1.available() > 0 && is2.available()>0) {
                                is1.read(buffer1);
                                is2.read(buffer2);

                                System.out.println(Arrays.toString(buffer1));
                                System.out.println(Arrays.toString(buffer2));


                                if (!Arrays.equals(buffer1,buffer2)){
                                    i = 0;
                                    System.out.println(j);
                                }
                                j++;
                            }
                        }
                    }
                }catch (IOException e){
                    System.out.println("err");
                }
            }
        return i;
    }
//
//public int compareFiles(String pathToFile1, String pathToFile2){
//    try(FileInputStream fis1 = new FileInputStream(new File(pathToFile1))){
//        try(FileInputStream fis2 = new FileInputStream(new File(pathToFile1))) {
//            byte[] bufferFile1 = new byte[1024];
//            byte[] bufferFile2 = new byte[1024];
//            while (fis1.available() > 0) {
//                fis1.read(bufferFile1);
//                fis2.read(bufferFile2);
//
//                System.out.println(bufferFile1);
//                System.out.println(bufferFile2);
//
//                if (!Arrays.equals(bufferFile1,bufferFile2)){
//                    i = 0;
//                    System.out.println("err");
//                }
//            }
//        }
//
//    } catch (FileNotFoundException e) {
//        e.printStackTrace();
//    } catch (IOException e) {
//        e.printStackTrace();
//    }
//    return i;
//}
}
