/**
 * Created by Павел on 04.12.2015.
 */
public class UsefulMeth1 {
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
}
