import java.io.IOException;
import java.util.List;
/**
 * realisation of this programe read in file "ReadMe.txt"
 * Created by Павел on 28.08.2015.
 */
public interface Archiver {
    /**
     * write files to zip file
     * @param pathToZipFile
     * @param list
     * @throws IOException
     */
    void zip(String pathToZipFile, List<String> list);

    /**
     * unpack zip file to folder.to unpack enter "unpack_to" end path to folder
     * (D:\\ZipFileInput.zip unpack_to E:\\aaa\\bbb\\)
     * @param pathToZipFile
     * @param pathToFolder
     * @throws IOException
     */
    void unzip(String pathToZipFile,String pathToFolder);

    /**
     * Adding file to zip archiv. To add file enter in console "pathToZipFile"_"add"_"pathToAddedFile"
     * example : D:\\ZipFile.zip  add   D:\\ccc.txt
     * @param pathToZipFile
     * @param pathToAddedFile
     * @throws IOException
     */
    void addFileToZip(String pathToZipFile, String pathToAddedFile);

    /**
     * show files in zip archiv
     * @throws IOException
     */
    void showFilesOfZipArchiv(String path);
}
