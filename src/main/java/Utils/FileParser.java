package Utils;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class FileParser {

    public static void main(String[] args)
    {
        formatRawData();
    }


    /**
     * Util that is used to format the raw data from the raw file
     */
    private static void formatRawData() {
        try {
            File file = new File("rawData.txt");
            BufferedReader br = new BufferedReader(new FileReader(file));
            String str;
            StringBuilder sb = new StringBuilder();
            while ((str = br.readLine()) != null){
                System.out.println(str.split(",")[0]);
                sb.append(str.split(",")[0]).append("\n");
            }
            File fileOut = new File("domains.txt");
            Charset charset = StandardCharsets.US_ASCII;
            OutputStream os = new FileOutputStream(fileOut);
            os.write(charset.encode(sb.toString()).array());
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets all the domains from domains.txt
     * @return Returns a list of domains
     */
    public static ArrayList<String> getDomains() {
        ArrayList<String> res = new ArrayList<>();
        try {
            File file = new File("domains.txt");
            BufferedReader br = new BufferedReader(new FileReader(file));
            String str;
            while ((str = br.readLine()) != null){
                res.add(str);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }
}
