package ru.ncedu.bean.archiver;

import java.io.*;
import java.util.Iterator;
import java.util.List;
import java.util.zip.*;


/**
 * realisation of this programe read in file "ReadMe.txt"
 * Created by ����� on 28.08.2015.
 */
public interface Archiver {
    /**
     * write files to zip file
     * @param pathToZipFile
     * @param list
     * @throws IOException
     */
    void writeToZip(String pathToZipFile, List<String> list) throws IOException;

    /**
     * unpack zip file to folder.to unpack enter "unpack_to" end path to folder
     * (D:\\ZipFileInput.zip unpack_to E:\\aaa\\bbb\\)
     * @param pathToZipFile
     * @param pathToFolder
     * @throws IOException
     */
    void unpackZipArchiv(String pathToZipFile,String pathToFolder) throws IOException;

    /**
     * devide input stream and choose next steps
     * @param str1
     * @throws IOException
     */
    void devideStream(String str1) throws IOException;

    /**
     * convert path to file in argument for ZipEntry().
     * it is internal method
     * @param pathToFile
     * @return
     */
    String nameForZipEntry(String pathToFile);
//    String nameForZipEntry(String pathToFile , int length);

    /**
     * Adding file to zip archiv. To add file enter in console "pathToZipFile"_"add"_"pathToAddedFile"
     * example : D:\\ZipFile.zip  add   D:\\ccc.txt
     * @param pathToZipFile
     * @param pathToAddedFile
     * @throws IOException
     */
    void addFileToZip(String pathToZipFile, String pathToAddedFile) throws IOException;

    /**
     * show files in zip archiv
     * @throws IOException
     */
    void showFilesOfZipArchiv() throws IOException;


    //--- input stream ---

    /**
     * searching files in zip archive. Planed to realise in coments
     * @param pathToZipFile
     * @param nameOfFile
     * @return
     * @throws IOException
     */
    Object searchFileInZip(String pathToZipFile, String nameOfFile) throws IOException;

    /**
     * manipulate with input stream, then calls meth devideStream(inputStream).
     */
    void manipulInputString();


    //--- deprecated methods (now it is not used)---
    /**
     * checking input string for having file like "ZipArchiv.zip" in first place.
     * @param str1
     * @return
     */
    boolean checkInputStream(String str1);

    /**
     * devide input stream to paths to Zip File and Files. Using in {@link #manipulInputString()}
     * @param str1
     * @throws StringIndexOutOfBoundsException
     */
    void devideInputStringToWrite(String str1) throws StringIndexOutOfBoundsException;

    /**
     * devide input stream to paths for unpacking zip file. Using in {@link #manipulInputString()}
     * @param str1
     * @throws StringIndexOutOfBoundsException
     */
    void devideInputStringToUnpack(String str1) throws StringIndexOutOfBoundsException;


}
