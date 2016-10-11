import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info("----------------   start   ---------------");
        ArchiverImpl a = new ArchiverImpl();
        Console console = new Console();

        console.manipulInputString();
        logger.info("----------------    end    ---------------\n");

//        E:\\zipF.zip E:\\1.mp4 E:\\2.mp4 E:\\3.mp4
//        E:\\zipF.zip      E:\\folderToUnzip\\
//        E:\\zipF.zip      E:\\4.wmv
//        E:\\zipF.zip E:\\1.mp4 E:\\1.mp3 E:\\p1.jpg
    }
}
