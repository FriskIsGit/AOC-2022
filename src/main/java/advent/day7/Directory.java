package advent.day7;

import java.util.ArrayList;
import java.util.List;

public class Directory{
    public final String name;
    private int entireSize = -1;

    public List<Directory> getDirs(){
        return dirs;
    }

    private final List<Directory> dirs = new ArrayList<>();
    private final List<Integer> files = new ArrayList<>();

    public Directory getParentDir(){
        if(parentDir == null)
            System.err.println("Does not contain an upper directory");
        return parentDir;
    }

    private Directory parentDir;

    public Directory(String name){
        this.name = name;
    }

    //if given does not exist, creates new subdirectory and returns it
    public Directory stepInto(String name){
        for(Directory dir : dirs){
            if(dir.name.equals(name)){
                return dir;
            }
        }
        return createSubdir(name);
    }
    private Directory createSubdir(String name){
        Directory subDir = new Directory(name);
        this.dirs.add(subDir);
        subDir.parentDir = this;
        return subDir;
    }
    public void createIfNotExists(String name){
        for(Directory dir : dirs){
            if(dir.name.equals(name)){
                return;
            }
        }
        createSubdir(name);
    }
    public boolean containsDirectory(String name){
        for(Directory dir : dirs){
            if(dir.name.equals(name)){
                return true;
            }
        }
        return false;
    }
    public void appendFile(int size){
        files.add(size);
    }
    public int sizeOfFiles(){
       return files.stream().mapToInt(Integer::intValue).sum();
    }
    public int entireSize(){
        return recurseForSize(this);
    }


    private int recurseForSize(Directory dir){
        //if it was ever set don't recurse once again (assuming no changes were made in the file system)
        if(dir.entireSize != -1){
            return dir.entireSize;
        }
        int dirTotal = dir.sizeOfFiles();
        for(Directory entry : dir.dirs){
            dirTotal += recurseForSize(entry);
        }
        return dirTotal;

    }

    public String toString(){
        return name;
    }
}
