package org.example.Controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.Model.ACModel;
import org.example.Utils.ConfigLoader;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ACModelController {
    private String pathToFile;
    private static ACModelController instance;
    private List<ACModel> acModells;

    private ACModelController(String pathToFile){
        this.pathToFile=pathToFile;
        this.acModells=new ArrayList<>();
        loadACModels();
    }

    public static ACModelController getInstance(){
        if (instance==null){
            instance=new ACModelController(ConfigLoader.getProperty("ACModelsFile"));
        }

        return instance;
    }

    private void loadACModels(){
        try (FileReader file=new FileReader(pathToFile)) {
            Gson gson=new Gson();
            ACModel[] list=gson.fromJson(file,ACModel[].class);
            acModells= Arrays.asList(list);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public ACModel getRandom(){
        Random random=new Random();
        return acModells.get(random.nextInt(acModells.size()));
    }

    public ACModel getACModel(String name){
        for (ACModel model: acModells){
            if (model.name.equals(name)){
                return model;
            }
        }
        return null;
    }



}
