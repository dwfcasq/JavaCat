package org.jcat;
import java.io.*;
import java.util.*;

public class javacat {

    //在指定的目录下搜索所有文件
    public static List<String> searchFiles(String directory) {
        //ArrayList来存储文件路径
        List<String> files = new ArrayList<>();
        File dir = new File(directory);
        //确保传入的目录路径是有效的目录。
        if (dir.isDirectory()) {
            searchFilesRecursive(dir,files);
        }
        //返回包含所有文件路径的列表
        return files;
    }

    //递归地搜索指定目录及其子目录中的所有文件，并将这些文件的绝对路径添加到files中。
    private static void searchFilesRecursive(File directory,List<String> files) {
        File[] fileList = directory.listFiles();//用于获取目录中的文件和子目录的数组。
        if (fileList != null) {
            for (File file : fileList) {
                if (file.isDirectory()) {
                    searchFilesRecursive(file,files);// 递归调用
                } else {
                    files.add(file.getAbsolutePath());// 添加文件的绝对路径
                }
            }
        }
    }

    //在指定文件中查找包含特定内容的行，并返回这些行及其行号的列表。
    public static List<String> searchContent(String filePath, String content) {
        List<String> matchingLines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int lineNum = 1;
            while ((line = reader.readLine()) != null) {
                if (line.contains(content)) {
                    matchingLines.add("Line " + lineNum + ": " + line);
                }
                lineNum++;
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + filePath);
            e.printStackTrace();
        }
        return matchingLines;
    }

    public static void writeToFile(String outputFilePath, String filePath, List<String> matchingLines,String key,String content) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath, true))) {
            writer.write("[+] 文件路径: " + filePath + "\n");
            writer.write("[-] 总行数: " + matchingLines.size() + "\n");
            writer.write("[*] 漏洞类型: " + key+ "\n");
            writer.write("[/] 匹配的关键字: " + content+ "\n");
            for (String line : matchingLines) {
                writer.write("[=] " + line + "\n");
            }
            writer.write("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //解析每行关键词
    public static HashMap<String, String[]> readkwFile(String kwPath) throws IOException {
        HashMap<String, String[]> hashmap = new HashMap<>();
        BufferedReader br = new BufferedReader(new FileReader(kwPath));
        String line;
        while ((line = br.readLine()) != null) {
            int colonIndex = line.indexOf(':');
            // 提取冒号前面的字符
            String beforeColon = line.substring(0, colonIndex).trim();
            // 处理每一行
            String oneLine = line.substring(colonIndex+1);
            String[] values = oneLine.split(","); // 按逗号分隔
            hashmap.put(beforeColon,values);
        }
        return hashmap;
    }

    public static void clearFile(String filePath) {
        File file = new File(filePath);
        try (FileOutputStream fos = new FileOutputStream(file, false)) {
            //每次运行会直接清空结果文件，然后写入新的结果
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println("\u001B[32midentify danger function running...\u001B[0m");
        String kwPath = "src/main/java/org/jcat/keywords.txt"; // 关键词路径
        String outfile="src/main/java/org/jcat/re.txt";// 提前创建该文件，这是结果输出的路径
        String directory = "F:\\JavaPro\\ManageBooks\\src\\main"; //指定要搜索的目录
        // 可以继续添加其他排除的后缀
        List<String> excludedExtensions = Arrays.asList(".exe",".css",".html",".png",".tmp",".log",".md",".sql",".class",".xml",".js",".jpg",".gitignore");

        clearFile(outfile);//每次运行会清空上次运行结果
        List<String> foundFiles = searchFiles(directory);
        HashMap<String, String[]> hashMap=readkwFile(kwPath);
        if (foundFiles.size()==0){
            System.out.println("\u001B[36m请输入有效目录\u001B[36m");
            System.exit(0); // 正常退出
        }
        for (String filePath : foundFiles) {  //每一个绝对路径
            // 判断文件路径是否以排除的后缀结尾
            boolean isExcluded = excludedExtensions.stream().anyMatch(filePath::endsWith);
            if (isExcluded) {
                System.out.println("排除后缀不处理:" + filePath);
            }else {
                for (Map.Entry<String, String[]> entry : hashMap.entrySet()) {
                    String Key=entry.getKey();
                    String[] contents=entry.getValue();
                    for (String content:contents){ //每一个关键字数组
                        List<String> foundLines = searchContent(filePath, content);
                        if (foundLines.size()>0){
                            writeToFile(outfile,filePath,foundLines,Key,content);
                        }
                    }
                }
            }
        }
        System.out.println("\u001B[32midentify danger function Finished!查看结果在:"+outfile+"\u001B[0m");
}
}