package pjwstk.project;

import jdk.nashorn.internal.runtime.regexp.joni.Regex;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ContentParser {
    private List<String> words = new ArrayList<>();
    private String[] specialWords;
    private boolean changed = false;
    private boolean script = false;
    private boolean style = false;
    private boolean texthtml = false;

    public ContentParser()  {
        try {
            File configFile = new File("src\\pjwstk\\project\\CONFIG");
            BufferedReader fileReader = new BufferedReader(new FileReader(configFile));
            String textLine = "";
            while((textLine = fileReader.readLine()) != null)   {
                if(textLine.contains("WORDS=")) {
                    textLine = textLine.replace("WORDS=", "");
                    specialWords = textLine.split(";");
                    changed = true;
                }
            }
        }   catch (Exception e) {
            e.printStackTrace();
        }
    }
    public String modify(String textToModify)    {
        String outputWord = textToModify;
        for(int i = 0; i<specialWords.length; i++)  {
            if(textToModify.contains(specialWords[i]))  {
                   {
                       if(textToModify.contains("<script"))   {
                           script = true;
                       }
                       if(textToModify.contains("</script"))    {
                           script = false;
                       }
                       if(textToModify.contains("<style"))   {
                           style = true;
                       }
                       if(textToModify.contains("</style"))    {
                           style = false;
                       }
                       if(!style && !script)  {
                           outputWord = outputWord.replace(specialWords[i],"<b><font color=\"red\">"+specialWords[i]+"</font></b>");
                       }
                }
            }
        }
        return outputWord;
    }
}
