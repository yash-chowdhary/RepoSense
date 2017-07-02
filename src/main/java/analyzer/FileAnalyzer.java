package analyzer;


import dataObject.Author;
import dataObject.FileInfo;
import dataObject.Line;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by matanghao1 on 28/5/17.
 */
public class FileAnalyzer {

    private static final String[] ignoredList = new String[] {"org/",".git",".log", ".class",".classpath","bin/",".gitignore",".DS_Store",".project"};

    public static void analyzeAllFiles(String repoRoot, File directory,ArrayList<FileInfo> result){

        for (File file:directory.listFiles()){
            String relativePath = file.getAbsolutePath().replaceFirst(repoRoot,"");
            if (shouldIgnore(relativePath)) continue;
            if (file.isDirectory()){
                analyzeAllFiles(repoRoot, file,result);
            }else{
                if (!relativePath.endsWith(".java")) continue;
                System.out.println(relativePath);
                FileInfo fileInfo = BlameParser.blameSingleFile(repoRoot,relativePath);
                CheckStyleParser.aggregateStyleIssue(fileInfo,repoRoot);
                MethodAnalyzer.aggregateMethodInfo(fileInfo,repoRoot);
                result.add(fileInfo);
            }
        }

    }

    private static boolean shouldIgnore(String name){
        for (String element: ignoredList){
            if (name.contains(element)) return true;
        }
        return false;
    }

}