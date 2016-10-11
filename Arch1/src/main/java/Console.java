import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Console {
    private static  final Logger logger = LoggerFactory.getLogger(Console.class);

    UsefulMeth meth = new UsefulMeth();
    ArchiverImpl aimpl = new ArchiverImpl();
    int caseSt = 0;

    public void manipulInputString()  {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))){
            System.out.println("1 - zip\n2 - unzip\n3 - add to zip\n4 - show files");
            String intString = br.readLine();
                try{
                    caseSt = new Integer(intString);
                }catch (NumberFormatException e){
                    logger.error("Not a number");
                }
            showInCons();
        } catch (IOException e) {
            logger.error("Error in manipulInputString-m : IOEx");
        }
    }

    public void showInCons(){
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            System.out.println("-------------");
            switch (caseSt){
                case 1:
                    logger.info("zip");
                    String inputString=br.readLine();
                    this.devideStream(inputString);
                    break;
                case 2:
                    logger.info("unzip");
                    inputString=br.readLine();
                    this.devideStream(inputString);
                    break;
                case 3:
                    logger.info("add");
                    inputString=br.readLine();
                    this.devideStream(inputString);
                    break;
                case 4:
                    logger.info("show");
                    inputString=br.readLine();
                    this.devideStream(inputString);
                    break;
                default:
                    logger.error("error in case");
                    break;
            }
        } catch (IOException e) {
            logger.error("Error in showInCons-m : IOex");
        }
    }

    public void devideStream(String str1){
        Pattern linkPattern = Pattern.compile("([:\\.\\-A-Za-z\\d\\\\]*\\.zip)");
        String str = str1.replaceAll("[ ]{2,}"," ").trim();     //initialize string to work

        String[]strMass= str.split(" ");
        Matcher linkMatcher = linkPattern.matcher(strMass[0]);

        if (linkMatcher.matches()==false){
            logger.error("Error in link to zip file");
        }
        else{
            meth.setPathToZipFile(strMass[0]);
        }

        if(caseSt == 1){
            for (int i = 1; i <strMass.length ; i++) {
                meth.addToList(strMass[i]);
            }
            logger.info("Objects to add: " + meth.getList());
            aimpl.zip(meth.getPathToZipFile(),meth.getList());
        }
        else if(caseSt == 2){
            logger.info("unziping");
            aimpl.unzip(meth.getPathToZipFile(),strMass[1]);
        }
        else if(caseSt == 3){
            logger.info("completing to add file ");
            aimpl.addFileToZip(meth.getPathToZipFile(),strMass[1]);
        }else if(caseSt == 4){
            logger.info("Show files in zip");
            aimpl.showFilesOfZipArchiv(strMass[0]);
        }
    }
}
