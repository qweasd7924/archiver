import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipFile;

/**
 * Created by Павел on 22.11.2015.
 */
public class UsefulMeth {
    private String pathToFile;                 // path to file for archiving
    private String pathToZipFile;              // path to zip file
    private String pathToFolder;               // path to folder

    private List<String> list= new ArrayList<String>();         //list for input files


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

    public Object searchFileInZip(String pathToZipFile, String nameOfFile) throws IOException {
        System.out.println(pathToZipFile);
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
