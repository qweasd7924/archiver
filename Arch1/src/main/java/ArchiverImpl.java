import com.sun.nio.zipfs.ZipFileStore;
import com.sun.org.apache.xpath.internal.SourceTree;
import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.IOUtils;
import sun.nio.ch.IOUtil;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.*;


/**
 * Created by Павел on 28.08.2015.
 */
public class ArchiverImpl implements Archiver{
    UsefulMeth meth = new UsefulMeth();
    private static  final Logger logger = LoggerFactory.getLogger(ArchiverImpl.class);

//    @Override
//    public void zip(String pathToZipFile, List<String> list) {
//            try(ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(pathToZipFile));){
//                File folder = new File(pathToZipFile);
//
//                if(folder.isDirectory() == false){                //if folder not found - create it
//                    int j = pathToZipFile.length();
//                    while (pathToZipFile.charAt(j-1) != '\\'){
//                        j--;
//                    }
//                    File newFolder =new File(pathToZipFile.substring(0,j));
//                    newFolder.mkdirs();
//                }
//                logger.info(pathToZipFile+" -link to new zip");
//                byte [] buffer = null;
//                Iterator<String> it = list.iterator();
//
//                while(it.hasNext()){
//                    buffer = new byte[1024];
//                    String path = it.next();
//                    logger.info("link to packing file:  " + path);
//                    try(FileInputStream fin = new FileInputStream(path);) {
//                        ZipEntry zentry = new ZipEntry(meth.nameForZipEntry(path));
//                        zout.putNextEntry(zentry);
//
////                        while ((fin.read(buffer))>=0){
//                        while (fin.available()>0){
//
//                            fin.read(buffer);
////                            for (int i = 0; i <buffer.length ; i++) {
////                                System.out.println((char)buffer[i]);
////
////                            }
////                                zout.write(fin.read(buffer));
//                            zout.write(buffer);
//                        }
//                        logger.info(meth.nameForZipEntry(path) + " packed");
//                        logger.trace("--------");
//                    }
//                    catch (FileNotFoundException fnfe){
//                        logger.error(meth.nameForZipEntry(path) +"- file not found");
//                    }
//                }
//                logger.info("Archiving ended ");
//        } catch (IOException e) {
//            logger.error("Error in zip-m : IOex");
//        }
//    }


    //BufferOut
//    @Override
//    public void zip(String pathToZipFile, List<String> list) {
//        try(ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(pathToZipFile));){
//            File folder = new File(pathToZipFile);
//
//            if(folder.isDirectory() == false){                //if folder not found - create it
//                int j = pathToZipFile.length();
//                while (pathToZipFile.charAt(j-1) != '\\'){
//                    j--;
//                }
//                File newFolder =new File(pathToZipFile.substring(0,j));
//                newFolder.mkdirs();
//            }
//            logger.info(pathToZipFile+" -link to new zip");
//            Iterator<String> it = list.iterator();
//
//            while(it.hasNext()){
//                String path = it.next();
//                logger.info("link to packing file:  " + path);
//                ZipEntry zentry = new ZipEntry(meth.nameForZipEntry(path));
//                zout.putNextEntry(zentry);
//                BufferedOutputStream bout = new BufferedOutputStream(zout,1024);
//
//                try(BufferedInputStream fin = new BufferedInputStream((new FileInputStream(path)),1024)) {
//                        while (fin.available()>0){
//                        bout.write(fin.read());
//                        }
//                    logger.info(meth.nameForZipEntry(path) + " packed");
//                    logger.trace("--------");
//
//                }
//                catch (FileNotFoundException fnfe){
//                    logger.error(meth.nameForZipEntry(path) +"- file not found");
//                }
//            }
//            logger.info("Archiving ended ");
//        } catch (IOException e) {
//            logger.error("Error in zip-m : IOex");
//        }
//    }

    @Override
    public void zip(String pathToZipFile, List<String> list) {
        try(ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(pathToZipFile));){
            File folder = new File(pathToZipFile);

            if(folder.isDirectory() == false){                //if folder not found - create it
                int j = pathToZipFile.length();
                while (pathToZipFile.charAt(j-1) != '\\'){
                    j--;
                }
                File newFolder =new File(pathToZipFile.substring(0,j));
                newFolder.mkdirs();
            }
            logger.info(pathToZipFile+" -link to new zip");
            byte [] buffer = null;
            Iterator<String> it = list.iterator();

            while(it.hasNext()){
                String path = it.next();
                logger.info("link to packing file:  " + path);
                try(FileInputStream fin = new FileInputStream(path);) {
                    buffer = new byte[fin.available()];

                    ZipEntry zentry = new ZipEntry(meth.nameForZipEntry(path));
                    zout.putNextEntry(zentry);

//                        while ((fin.read(buffer))>=0){
                    while (fin.available() > 0) {

                        fin.read(buffer);
//                                zout.write(fin.read(buffer));
                        zout.write(buffer);
                    }
                    logger.info(meth.nameForZipEntry(path) + " packed");
                    logger.trace("--------");
                }
                catch (FileNotFoundException fnfe){
                    logger.error(meth.nameForZipEntry(path) +"- file not found");
                }
            }
            logger.info("Archiving ended ");
        } catch (IOException e) {
            logger.error("Error in zip-m : IOex");
        }
    }

    @Override
    public void unzip(String pathToZipFile, String pathToFolder){
        try {
            File folder =new File(pathToFolder);
            if(folder.isDirectory()==false){
                logger.info("created new directory");
                File newFolder = new File(pathToFolder+"\\");
                newFolder.mkdirs();
            }

            ZipFile zipFile =new ZipFile(pathToZipFile);
            Iterator<Object> iter = (Iterator<Object>) zipFile.entries();
            Object obj;
            while(iter.hasNext()) {
                obj = iter.next();

                ZipEntry zipEntry = new ZipEntry(obj.toString());
                logger.info("file "+zipEntry+" is being unziped");

                try(BufferedOutputStream bout =
                            new BufferedOutputStream(new FileOutputStream(pathToFolder + obj.toString()),1024);
                    BufferedInputStream bis =
                            new BufferedInputStream(zipFile.getInputStream(zipEntry),1024)) {
                    while (bis.available()>0) {
                        bout.write(bis.read());
                    }
                }

                logger.info(zipEntry + " is unziped\n---");
            }
            zipFile.close();
            logger.info(zipFile.getName()+" unpacked to "+pathToFolder);
        }
        catch (FileNotFoundException f){
            logger.error("Error in unzip-m : FileNotFoundEx");
        }
        catch (IOException e){
            logger.error("Error in unzip-m : IOEx");
        }
    }

//    @Override
//    public void addFileToZip(String pathToZipFile, String pathToAddedFile){
//        try {
//            String pathToZipFile1 = pathToZipFile.replace(".zip", "1.zip");                                                      // create path clone of zip file
//            String fileInArchiv = meth.searchFileInZip(pathToZipFile,meth.nameForZipEntry(pathToAddedFile)).toString();  // search file in archiv with same name to adding file
//
//            File fileToAdd = new File (pathToAddedFile);
//            if (fileToAdd.exists()==false){
//                throw new  FileNotFoundException();
//            }
//            else if (fileInArchiv.equals(fileToAdd.getName())){                         //checking for exsist adding file with same name  in archiv
//                logger.warn("File with same name exists in zip");
//            }
//            else{
//                File zipFileToWrite = new File(pathToZipFile1);                         // create clone of zip file
//                ZipFile zipFileToRead = new ZipFile(pathToZipFile);                     // old zip file
//                Object obj = null ;
//
//                ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(zipFileToWrite));
//                Iterator<Object> iter = (Iterator<Object>) zipFileToRead.entries();                 //cloning old zip file
//
//                while (iter.hasNext()){
//                    obj = iter.next();
//                    ZipEntry zipEntryOld = new ZipEntry(obj.toString());
//                    zout.putNextEntry(zipEntryOld);
//                    BufferedOutputStream bout = new BufferedOutputStream(zout,1024);
//
//                    InputStream is = zipFileToRead.getInputStream(zipEntryOld);
//                    BufferedInputStream bis = new BufferedInputStream(is,1024);
//
//                    while (is.available()>0){
//                        bout.write(bis.read());
//                    }
//                    is.close();
//                    bis.close();
//                }
//
//                int i =0;
//                FileInputStream fin = new FileInputStream(fileToAdd);
//                BufferedInputStream bin = new BufferedInputStream(fin,1024);
//                ZipEntry zipEntry = new ZipEntry(meth.nameForZipEntry(pathToAddedFile));
//                zout.putNextEntry(zipEntry);
//                BufferedOutputStream bout = new BufferedOutputStream(zout,1024);
//
//                while (bin.available()>0){
//                    bout.write(bin.read());
//                }
//                fin.close();
//                bin.close();
////                bout.close();
//                zout.close();
//                //new zip file  created
//                zipFileToRead.close();                              //delete old file and rename new zip to name of old
//                String name = zipFileToRead.getName();
//
//                File f1 = new File(pathToZipFile);
//                File f2 = new File(name);
//
//                f1.delete();                                        //delete clone of zip file
//
//                zipFileToWrite.renameTo(f2);
//                logger.info("adding file " + meth.nameForZipEntry(pathToAddedFile)+" complited");
//            }
//        }
//        catch ( FileNotFoundException fnfe ){
//            logger.error("File to add not found");
//        }
//        catch (IOException e ){
//            logger.error("Error in addFileToZip-m : IOEx");
//        }
//    }

    @Override
    public void addFileToZip(String pathToZipFile, String pathToAddedFile){
        try {
            String pathToZipFile1 = pathToZipFile.replace(".zip", "1.zip");                                                      // create path clone of zip file
            String fileInArchiv = meth.searchFileInZip(pathToZipFile,meth.nameForZipEntry(pathToAddedFile)).toString();  // search file in archiv with same name to adding file

            File fileToAdd = new File (pathToAddedFile);
            if (fileToAdd.exists()==false){
                throw new  FileNotFoundException();
            }
            else if (fileInArchiv.equals(fileToAdd.getName())){                         //checking for exsist adding file with same name  in archiv
                logger.warn("File with same name exists in zip");
            }
            else {
                File zipFileToWrite = new File(pathToZipFile1);                         // create clone of zip file
                ZipFile zipFileToRead = new ZipFile(pathToZipFile);                     // old zip file
                Object obj = null ;

                try(ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(zipFileToWrite));){

                    Iterator<Object> iter = (Iterator<Object>) zipFileToRead.entries();                 //cloning old zip file

                    while (iter.hasNext()){
                        obj = iter.next();
                        ZipEntry zipEntryOld = new ZipEntry(obj.toString());
                        zout.putNextEntry(zipEntryOld);
                        BufferedOutputStream bout = new BufferedOutputStream(zout,1024);

                        InputStream is = zipFileToRead.getInputStream(zipEntryOld);
                        BufferedInputStream bis = new BufferedInputStream(is,1024);

                        while (is.available()>0){
                            bout.write(bis.read());
                        }
                        is.close();
                        bis.close();
                    }

                    int i =0;
                    FileInputStream fin = new FileInputStream(fileToAdd);
                    BufferedInputStream bin = new BufferedInputStream(fin,1024);
                    ZipEntry zipEntry = new ZipEntry(meth.nameForZipEntry(pathToAddedFile));
                    zout.putNextEntry(zipEntry);
                    BufferedOutputStream bout = new BufferedOutputStream(zout,1024);

                    while (bin.available()>0){
                        bout.write(bin.read());
                    }
                    fin.close();
                    bin.close();
//                bout.close();
                    zout.close();
                    //new zip file  created
                    zipFileToRead.close();                              //delete old file and rename new zip to name of old
                    String name = zipFileToRead.getName();

                    File f1 = new File(pathToZipFile);
                    File f2 = new File(name);

                    f1.delete();                                        //delete clone of zip file

                    zipFileToWrite.renameTo(f2);
                    logger.info("adding file " + meth.nameForZipEntry(pathToAddedFile)+" complited");

                }catch (IOException e){

                }
            }
        }
        catch ( FileNotFoundException fnfe ){
            logger.error("File to add not found");
        }
        catch (IOException e ){
            logger.error("Error in addFileToZip-m : IOEx");
        }
    }

    @Override
    public void showFilesOfZipArchiv(String path) {
        try{
            System.out.println(path +" - link to zip");
            ZipFile file=new ZipFile(path);

            Iterator<Object> iter= (Iterator<Object>) file.entries();
            Object obj;
            while(iter.hasNext()){
                obj=iter.next();
                logger.info(obj.toString());
//                System.out.println(obj.toString());
                ZipEntry a=file.getEntry(obj.toString());
                ZipInputStream zin = new ZipInputStream(file.getInputStream(a));
                while(zin.read()!=-1){
                    System.out.println((char)zin.read());
                }
            }
            file.close();
        }catch (IOException ex){
            logger.error("Error in showFilesOfZip-m : IOEx");
        }
    }
}
