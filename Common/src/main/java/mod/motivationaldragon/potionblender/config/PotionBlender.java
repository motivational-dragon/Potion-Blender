package mod.motivationaldragon.potionblender.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import mod.motivationaldragon.potionblender.Constants;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class PotionBlender {

    private PotionBlender(){}


    private static final String CONFIG_FILE_NAME = "potion_blender_config.json";
    private static final Path CONFIG_PATH = Path.of(Constants.MOD_ID, CONFIG_FILE_NAME);
    private static final Gson JSON_PARSER = new GsonBuilder().setPrettyPrinting().create();

    private static ConfigInstance config;

    private static boolean isReady = false;


    static {
        readConfig();
    }

    public static ConfigInstance getConfig(){
        return config;
    }

    private static void readConfig(){

        if(!isReady){init();}

        try {
            String jsonString = Files.readString(CONFIG_PATH);
            PotionBlender.config = deserializeConfig(jsonString);
            Constants.LOG.info("Loaded config");
            return;
        } catch (IOException e) {
            Constants.LOG.error("Could not read config file");
            e.printStackTrace();
        }

        Constants.LOG.warn("Unable to read config, using a default one as fallback");
        PotionBlender.config = new ConfigInstance();
    }

    public static void init() {

        if(isReady){return;}
        isReady = true;

        Path configPath = Path.of(Constants.MOD_ID, CONFIG_FILE_NAME);
        if(!Files.exists(configPath)){
            try {
                Path path = Path.of(Constants.MOD_ID);
                Constants.LOG.info("No config file found, creating a new one at: %s...".formatted(path));
                Files.createDirectories(path);

                ConfigInstance configInstance = new ConfigInstance();

                String jsonString = serializeConfig(configInstance);
                Files.createFile(configPath);
                Files.writeString(configPath, jsonString);

            }catch (IOException e){
                Constants.LOG.error("Could not access config file");
                e.printStackTrace();
            }
        }
    }


    public static ConfigInstance deserializeConfig(String configAsJson){
        try {
            return JSON_PARSER.fromJson(configAsJson, ConfigInstance.class);
        }catch (JsonSyntaxException e){
            Constants.LOG.error("Could not parse config JSON. Make sure syntax is correct");
            e.printStackTrace();
        }
        Constants.LOG.warn("Unable to deserialize config, using a default one as fallback");
        return new ConfigInstance();
    }

    public static String serializeConfig(ConfigInstance config){
        return JSON_PARSER.toJson(config);
    }

}
