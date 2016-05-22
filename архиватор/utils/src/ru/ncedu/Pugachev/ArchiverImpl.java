package ru.ncedu.bean.archiver;

import com.sun.deploy.association.Action;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;


/**
 * Created by ����� on 28.08.2015.
 */
public class ArchiverImpl implements Archiver{
    private String pathToFile;                 // path to file for archiving
    private String pathToZipFile;              // path to zip file
    private String pathToFolder;               // path to folder

    private List<String> list= new ArrayList<String>();         //list for input files

    //--- main methods ---

    @Override
    public void writeToZip(String pathToZipFile, List<String> list) throws IOException {

        int k = pathToZipFile.length();
        while (pathToZipFile.charAt(k-1)!='\\'){
            k--;
        }

        File folder = new File(pathToZipFile.substring(0,k));

        if(!folder.isDirectory()){                //if folder not found - create it
            File newFolder =new File(pathToZipFile.substring(0,k));
            newFolder.mkdirs();
        }

        ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(pathToZipFile));
        for(int j=0; j<list.size(); j++) {
            int i = 0;

            String pathToFile = list.get(j);
            if (new File(pathToFile).isDirectory()) {
                addFolderToZip(zout ,pathToFile,pathToFile);
            } else {
//                System.out.println("link to paking file: " + pathToFile);
                addFileToZip(zout,pathToFile);
//                System.out.println("-------");
            }
        }
        zout.close();
//        System.out.println("");
//        System.out.println("Archiving ended ");
    }

    public void addFolderToZip(ZipOutputStream zout,String pathToFile,String pathToRootFold){
        File[] listFile = new File(pathToFile).listFiles();

        for (int j = 0; j <listFile.length ; j++) {
            int i = 0;
            byte [] buffer = null;


            try {
                ZipEntry zentry;

                if (listFile[j].isDirectory()){
                    addFolderToZip(zout,listFile[j].getPath(),pathToRootFold);
                }
                FileInputStream zin = new FileInputStream(listFile[j]);

                    String a = listFile[j].getPath();                   //velosiped)
                    String b = nameForZipEntry(pathToRootFold);
                    String c = a.substring(pathToRootFold.length()-b.length(),a.length());
                    zentry = new ZipEntry(c);
                zout.putNextEntry(zentry);
                //write
                buffer = new byte[1024];

                while (zin.available() > 0) {
                    int length = zin.read(buffer);
                    zout.write(buffer,0,length);
                }

                zin.close();
                //end write
//                System.out.println(nameForZipEntry(pathToFile) + " packed");
            }catch (FileNotFoundException fnfe) {
//                System.out.println(nameForZipEntry(pathToFile) + "- file not found");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void addFileToZip(ZipOutputStream zout,String pathToFile){
        int i = 0;
        byte[] buffer = new byte[1024];

        try {
            FileInputStream zin = new FileInputStream(pathToFile);

            ZipEntry zentry = new ZipEntry(nameForZipEntry(pathToFile));;
            zout.putNextEntry(zentry);
            //write
            while (zin.available() > 0) {
                int length = zin.read(buffer);
                zout.write(buffer,0,length);
            }
//            while (i != -1) {
//                i = zin.read();
//                if (i == -1) break;
//                zout.write(i);
//            }
            zin.close();
            //end write
//            System.out.println(nameForZipEntry(pathToFile) + " packed");
        } catch (FileNotFoundException fnfe) {
//            System.out.println(nameForZipEntry(pathToFile) + "- file not found");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void unpackZipArchiv(String pathToZipFile, String pathToFolder) throws IOException {
        try {
            File folder =new File(pathToFolder);
            if(folder.isDirectory()==false){
                System.out.println("created new directory");
            File ff = new File(pathToFolder+"\\");
            ff.mkdirs();}

            ZipFile zipFile =new ZipFile(pathToZipFile);
                Iterator<Object> iter = (Iterator<Object>) zipFile.entries();
                Object obj;

            while(iter.hasNext()) {
                obj = iter.next();

                File a = new File (pathToFolder + obj.toString());
                String toCheck = obj.toString();
                    if (toCheck.contains("\\")){
                        System.out.println("is dir");
                        int k = 0;
                            for (int i = 0; i <toCheck.length() ; i++) {
                                if (toCheck.charAt(i) == '\\'){
                                  k = i;
                                }
                            }
                        String toDir = toCheck.substring(0,k);
                        File isDirEx = new File(pathToFolder + toDir);
                            if (!isDirEx.exists()){
                                isDirEx.mkdirs();
                            }
                    }
                FileOutputStream fout = new FileOutputStream(pathToFolder + obj.toString());

                    ZipEntry zipEntry = new ZipEntry(obj.toString());
                    System.out.println("file "+zipEntry+" is unpacking");

                InputStream is = zipFile.getInputStream(zipEntry);
                ZipInputStream zin = new ZipInputStream(zipFile.getInputStream(zipEntry));

                byte[] buffer = new byte[1024];
                    while (is.available() > 0) {
                        int length = is.read(buffer);
                        fout.write(buffer,0,length);
                    }
                zin.close();
                fout.close();
                System.out.println(zipEntry + " is unpacked");
                System.out.println("---");
            }
            zipFile.close();
            System.out.println();
            System.out.println(zipFile.getName()+" unpacked to "+pathToFolder);
        }
        catch (FileNotFoundException ignore){
        }
    }

    @Override
    public void addFileToZip(String pathToZipFile, String pathToAddedFile) throws IOException {
        String pathToZipFile1 = pathToZipFile.replaceAll(".zip","1.zip");                                                      // create path clone of zip file
        String fileInArchiv = this.searchFileInZip(this.getPathToZipFile(),this.nameForZipEntry(pathToAddedFile)).toString();  // search file in archiv with same name to adding file
        try {
            File fileToAdd = new File (pathToAddedFile);
            if (fileToAdd.exists()==false){
                System.out.println("File to adding not found");
                new FileNotFoundException();
            }
            else if (fileInArchiv.equals(fileToAdd.getName())){                         //checking for exsist adding file with same name  in archiv
                System.out.println("File with same name is exists in zip");
            }

            else{
                File zipFileToWrite = new File(pathToZipFile1);                         // create clone of zip file
                ZipFile zipFileToRead = new ZipFile(pathToZipFile);                     // old zip file
                Object obj = null ;

                ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(zipFileToWrite));
                Iterator<Object> iter = (Iterator<Object>) zipFileToRead.entries();                 //cloning old zip file

                while (iter.hasNext()){
                    int i = 0;

                    obj = iter.next();
                    ZipEntry zipEntryOld = new ZipEntry(obj.toString());
                    zout.putNextEntry(zipEntryOld);

                    InputStream is = zipFileToRead.getInputStream(zipEntryOld);

                    while (i!=-1){
                        i = is.read();
                        if(i==-1){break;}
                        zout.write(i);
                    }
                    is.close();
                }

                int i =0;
                FileInputStream fin = new FileInputStream(fileToAdd);
                ZipEntry zipEntry = new ZipEntry(this.nameForZipEntry(pathToAddedFile));
                zout.putNextEntry(zipEntry);

                while (i!=-1){
                    i = fin.read();
                    if(i==-1){
                        break;}
                    zout.write(i);
                }
                fin.close();
                zout.close();
                                                                    //new zip file  created
                zipFileToRead.close();                              //delete old file and rename new zip to name of old
                String name = zipFileToRead.getName();

                File f1 = new File(pathToZipFile);
                File f2 = new File(name);

//                f1.setWritable(true);
                f1.delete();                                        //delete clone of zip file

                zipFileToWrite.renameTo(f2);
                System.out.println("adding file " + this.nameForZipEntry(pathToAddedFile)+" complited");
            }
        }
        catch ( FileNotFoundException fnfe ){
            System.out.println("File to adding not found");
        }

    }

    @Override
    public Object searchFileInZip(String pathToZipFile, String nameOfFile) throws IOException {
        ZipFile zipFile = new ZipFile(pathToZipFile);
        Iterator<Object> iter = (Iterator<Object>) zipFile.entries();
        Object obj = null;

        while(iter.hasNext()){
            obj = iter.next();
            if(obj.toString().equals(nameOfFile)){
                zipFile.close();
                return obj;
            }

        }
        zipFile.close();
        return "-1";
    }

    //---working with input stream ---
    @Override
    public void manipulInputString()  {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.println("to pack files enter path to zip file and list paths to packed files through the gap");
            System.out.println("example:   D:\\\\ZipFileInput.zip   D:\\\\aaa.txt   D:\\\\pict.jpg");
            System.out.println("");
            System.out.println("to unpack zip file enter path to zip file + unpack_to + path to folder through the gap");
            System.out.println("example:   D:\\\\ZipFileInput.zip unpack_to E:\\\\aaa\\\\bbb\\\\");

            System.out.println("\nPlease enter input stream");
            String inputString=br.readLine();
            br.close();
            this.devideStream(inputString);
        } catch (IOException e) {
            System.out.println("Error");
        }

    }

    @Override
    public void devideStream(String str1) throws IOException {
        Pattern linkPattern = Pattern.compile("([:\\.\\-A-Za-z\\d\\\\]*\\.zip)");

        String str = str1.replaceAll("[ ]{2,}"," ").trim();     //initialize string to work

        String[]strMass= str.split(" ");
        Matcher linkMatcher = linkPattern.matcher(strMass[0]);

        if (linkMatcher.matches()==false){
            System.out.println("Error in link to zip file");
        }
        else{
            this.setPathToZipFile(strMass[0]);
        }

        if (strMass[1].equals("unpack_to") && strMass.length<4){
            System.out.println("complete unpack");
            this.unpackZipArchiv(this.getPathToZipFile(),strMass[2]);
        }
        else if(strMass[1].equals("add")){
            System.out.println("comliting to add file ");
            this.addFileToZip(this.getPathToZipFile(),strMass[2]);
        }
        else {
            for (int i = 1; i <strMass.length ; i++) {
                this.addToList(strMass[i]);
            }
            System.out.println(list);
            this.writeToZip(this.getPathToZipFile(),this.getList());
        }
//        for (int j = 0; j <strMass.length ; j++) {
//
//            System.out.println(strMass[j]);
//        }
    }

    //--- intermediate methods ---
    @Override
    public String nameForZipEntry(String pathToFile) {
        String nameForZipEntry;
        int k = 0;

        for (int i = 0; i < pathToFile.length(); i++) {
            if (pathToFile.charAt(i) == '\\') {
                k = i;
            }
        }
        nameForZipEntry = pathToFile.substring(k + 1);
        return nameForZipEntry;
    }

    @Override
    public void showFilesOfZipArchiv() throws IOException{
        {
            ZipFile file=new ZipFile(this.pathToZipFile);

            Iterator<Object> iter= (Iterator<Object>) file.entries();
            Object obj;
            while(iter.hasNext()){
                obj=iter.next();
                System.out.println(obj.toString());
                ZipEntry a=file.getEntry(obj.toString());
                ZipInputStream zin = new ZipInputStream(file.getInputStream(a));
                while(zin.read()!=-1){
                    System.out.println((char)zin.read());
                }
            }
            file.close();
        }
    }

    //--- deprecated methods (now it is not used)---
    @Override
    public boolean checkInputStream(String str1) {
        int j;
        String str = str1.trim()+" aaa";                                       //string to work
        String zip = ".zip";
        setPathToZipFile(str.substring(0, j = str.indexOf(' ')));

        if(str.substring(j - 4, j).equals(zip)){
            return  true;
        }
        else
            return false;

    }

    @Override
    public void devideInputStringToWrite(String str1) throws StringIndexOutOfBoundsException {
        try {
            int j, interval1, interval2;
            String str = str1.trim();                                       //string to work

            setPathToZipFile(str.substring(0, j = str.indexOf(' ')));           //set path to zip file
            interval1 = j;
            while (str.charAt(interval1) == ' ') {
                interval1++;
            }
            interval2 = str.indexOf(' ', interval1);

            while (str.substring(interval1, interval2).length() != 0) {         //sets links to files for pucking
                list.add(str.substring(interval1, interval2));

                interval1 = interval2;
                while (str.charAt(interval1) == ' ') {
                    interval1++;
                }
                if (str.substring(interval1, str.length()).contains(" "))
                    interval2 = str.indexOf(' ', interval1);
                else {
                    interval2 = str.length();
                }
            }
        }
        catch(StringIndexOutOfBoundsException sioofbe){
        }
    }

    @Override
    public void devideInputStringToUnpack(String str1) throws StringIndexOutOfBoundsException {
        try {
            int j, interval1, interval2;
            String str = str1.trim();                                       //string to work

            setPathToZipFile(str.substring(0, j = str.indexOf(' ')));           //set path to zip file
            interval1 = j;
            while (str.charAt(interval1) == ' ') {
                interval1++;
            }
            interval2 = str.indexOf(' ', interval1);

            int k=str.length()-1;
            while(str.substring(k,str.length()) .startsWith(" ")!=true){
                k--;
            }
            this.setPathToFolder(str.substring(k+1,str.length()));
        }
        catch(StringIndexOutOfBoundsException sioofbe){
        }
    }

    //--- getters/setters ---

    public String getPathToZipFile() {
        return pathToZipFile;
    }

    public void setPathToZipFile(String pathToZipFile) {
        this.pathToZipFile = pathToZipFile;
    }

    public String getPathToFile() {
        return pathToFile;
    }

    public void setPathToFile(String pathToFile) {
        this.pathToFile = pathToFile;
    }

    public void setPathToFolder(String pathToFolder) {
        this.pathToFolder = pathToFolder;
    }

    public String getPathToFolder() {
        return pathToFolder;
    }

    //--- list ---

    public List<String> getList(){
        return list;
    }

    public void addToList(String... param){
        for (String par:param){
            list.add(par);
        }
    }

    public int getNumberOfListElem(){
        int i=0;
        for (String obj :list) {
            i++;
        }

        return i;
    }

}
