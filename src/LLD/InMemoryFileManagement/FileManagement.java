package LLD.InMemoryFileManagement;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class FileManagement {
    public static void main(String [] args){
        FileSystem fileSystem = new FileSystem();
        fileSystem.mkdir("/folder1");
        fileSystem.mkdir("/folder2");
        fileSystem.addContentToFile("/folder1/file1", "Content of file1");
        fileSystem.addContentToFile("/folder2/file2", "Content of file2");
        System.out.println(fileSystem.ls("/"));
        System.out.println(fileSystem.ls("/folder1"));
        System.out.println(fileSystem.ls("/folder2"));
        System.out.println(fileSystem.readContentFromFile("/folder1/file1"));
        fileSystem.move("/folder1/file1", "/folder2");
        System.out.println(fileSystem.ls("/folder1"));
        System.out.println(fileSystem.ls("/folder2"));
        fileSystem.delete("/folder1");
        System.out.println(fileSystem.ls("/"));
    }
}


class FileNode{

    // We could have used Collections.sort(result) in the
    // ls function as Treemap will make each insertion O(logn)
    // also what if we need support of multiple types of sorting, that's why!!
    private String name;
    private StringBuilder file;
    private TreeMap<String, FileNode> subDirectories;

    public FileNode(String name){
        this.name = name;
        this.file = new StringBuilder();
        this.subDirectories = new TreeMap<>();
    }

    public String getName(){
        return this.name;
    }

    public TreeMap<String, FileNode> getSubDirectories(){
        return this.subDirectories;
    }

    public void createSubDirectories(String pathName){
        this.subDirectories.put(pathName, new FileNode(pathName));
    }

    public void addContent(String content){
        this.file.append(content);
    }

    public String readFile(){
        return this.file.toString();
    }

    public List<String> listSubDirectories(){
        List<String> list = new ArrayList<>();

        if(this.file.length()>0){
            list.add(this.name);
        }
        else{
            list.addAll(this.subDirectories.keySet());
        }
        return list;
    }

}
class FileSystem {
    FileNode root;
    public FileSystem() {
        root = new FileNode("/");
    }

    public List<String> ls(String path) {
        FileNode node = findNode(path, false);
        return node==null? null: node.listSubDirectories();
    }

    public void mkdir(String path) {
        findNode(path, true);
    }

    public void addContentToFile(String filePath, String content) {
        FileNode node = findNode(filePath, true);
        node.addContent(content);
    }

    public String readContentFromFile(String filePath) {
        FileNode node = findNode(filePath, true);
        return node.readFile();
    }

    public void move(String sourcePath, String destinationPath) {
        FileNode sourceNode = findNode(sourcePath, false);
        FileNode destinationNode = findNode(destinationPath, true);
        if (sourceNode != null && destinationNode != null) {
            destinationNode.createSubDirectories(sourceNode.getName());
            destinationNode.getSubDirectories().putAll(sourceNode.getSubDirectories());
            sourceNode.getSubDirectories().clear();
        }
    }

    public void delete(String path) {
        FileNode node = findNode(path, false);
        if (node != null && !path.equals("/")) {
            String parentPath = path.substring(0, path.lastIndexOf('/'));
            FileNode parentNode = findNode(parentPath, false);
            if (parentNode != null) {
                parentNode.getSubDirectories().remove(node.getName());
            }
        }
    }

    private FileNode findNode(String path, boolean make){
        String[] nodes = path.split("/");
        FileNode fnode = root;

        for(String node: nodes){
            if(node.isEmpty()) continue;

            if(!fnode.getSubDirectories().containsKey(node)){
                if(make){
                    fnode.createSubDirectories(node);
                }else{
                    return null;
                }
            }

            fnode = fnode.getSubDirectories().get(node);
        }

        return fnode;
    }
}

/**
 * Your FileSystem object will be instantiated and called as such:
 * FileSystem obj = new FileSystem();
 * List<String> param_1 = obj.ls(path);
 * obj.mkdir(path);
 * obj.addContentToFile(filePath,content);
 * String param_4 = obj.readContentFromFile(filePath);
 */