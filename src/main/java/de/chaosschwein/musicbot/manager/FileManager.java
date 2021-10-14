package de.chaosschwein.musicbot.manager;

import de.chaosschwein.musicbot.main.botMain;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;

public class FileManager {

    private String filename;

    public FileManager(String filename){
        createDir("configs");
        this.filename="configs/"+filename+".json";
    }
    public FileManager(){}

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public boolean isFile(){
        return (new File(this.filename).exists()&&new File(this.filename).isFile());
    }

    public static void createDir(String dir){
        File directory= new File(dir);
        if(!directory.exists()) {
            directory.mkdir();
        }
    }

    public boolean create(){
        try {return new File(this.filename).createNewFile(); } catch (IOException e) {}return false;
    }

    public void writedefault(HashMap<String,Object> dset){
        try {
            if(!isFile()) {
                create();
                JSONObject ob = new JSONObject();
                for(String key : dset.keySet()){
                    ob.put(key, dset.get(key));
                }
                FileWriter myWriter = new FileWriter(this.filename);
                myWriter.write(ob.toJSONString());
                myWriter.close();
            }
        } catch (IOException ignored) {
        }

    }

    public HashMap<String, Object> read(){
        HashMap<String, Object> a = new HashMap<>();

        try {

            JSONObject obj = (JSONObject) new JSONParser().parse(new FileReader(this.filename));
            for (Object ob: obj.keySet()) {
                a.put(ob.toString(),obj.get(ob));
            }
        } catch (ParseException | IOException ignored) {
        }
        return a;
    }

    public void write(String key,Object value){
        try {

            JSONObject obj = (JSONObject) new JSONParser().parse(new FileReader(this.filename));
            if(obj.containsKey(key)) {
                obj.replace(key, value);
            }else{
                obj.put(key,value);
            }
            FileWriter myWriter = new FileWriter(this.filename);
            myWriter.write(obj.toJSONString());
            myWriter.close();

        } catch (ParseException | IOException ignored) {
        }
    }
}
