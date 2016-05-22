package ru.ncedu.bean.archiver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class main{
    public static void main(String[] args) throws IOException {
        String pathToZip = "D:\\forAr\\zip.zip";
        String pathToUnzip = "D:\\forAr\\for unzip\\";
        ArchiverImpl a = new ArchiverImpl();

        List<String> list = new ArrayList<String>();
        list.add("D:\\forAr\\folder\\123.txt");
        list.add("D:\\forAr\\folder\\234.txt");
        list.add("D:\\forAr\\folder\\f1");
        list.add("D:\\forAr\\folder\\1.wmv");
        list.add("D:\\forAr\\folder\\3.wmv");

//        a.writeToZip(pathToZip, list);

        a.unpackZipArchiv(pathToZip,pathToUnzip);


//        a.manipulInputString();

//        input stream
//        D:\\ZipFileInput.zip   D:\\aaa.txt   D:\\pict.jpg  D:\\music.mp3
//        unpack
//        D:\\ZipFileInput.zip unpack_to E:\\aaa\\bbb\\
//        add
//        D:\\ToCopy.zip  add  D:\\ccc.txt

    }
}
