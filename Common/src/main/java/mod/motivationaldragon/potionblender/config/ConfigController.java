package mod.motivationaldragon.potionblender.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import mod.motivationaldragon.potionblender.Constants;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public class ConfigController {

    private ConfigController(){}


    private static final String CONFIG_FILE_NAME = "potion_blender_config.json";

    public static final int CONFIG_VERSION = 1;

    private static final Path CONFIG_PATH = Path.of(Constants.MOD_ID, CONFIG_FILE_NAME);
    private static final Gson JSON_PARSER = new GsonBuilder().setPrettyPrinting()
            .registerTypeHierarchyAdapter(Item.class, new ItemSerializer())
            .create();



    private static PotionBlenderConfig config;

    private static boolean isInitialized = false;

    public static PotionBlenderConfig getConfig(){
        if(config == null || !isInitialized){
            readConfig();
        }
        return config;
    }

    private static void readConfig(){

        if(!isInitialized){init();}

        try {
            String jsonString = Files.readString(CONFIG_PATH);
            config = deserializeConfig(jsonString);
            Constants.LOG.info("Loaded config");

            if(config.getConfigVersion() < CONFIG_VERSION){
                Constants.LOG.warn("Old config detected. Default value for missing entries will be generated");
                config.setConfigVersion(CONFIG_VERSION);
                saveConfig(config);
            }

            return;
        } catch (IOException e) {
            Constants.LOG.error("Could not read config file");
            e.printStackTrace();
        }

        Constants.LOG.warn("Unable to read config, using default values as fallback");
        ConfigController.config = new PotionBlenderConfig();
    }

    public static void init() {

        if(isInitialized){return;}
        isInitialized = true;

        Path configPath = Path.of(Constants.MOD_ID, CONFIG_FILE_NAME);
        Path path = Path.of(Constants.MOD_ID);
        if(Files.exists(configPath)){return;}

        Constants.LOG.info("No config file found, creating a new one at: %s...".formatted(path));
        saveConfig( new PotionBlenderConfig());

    }

    private static void saveConfig(PotionBlenderConfig config) {
        Path configPath = Path.of(Constants.MOD_ID, CONFIG_FILE_NAME);
            try {
                Path dirPath = Path.of(Constants.MOD_ID);
                Files.createDirectories(dirPath);

                try (var fileWriter = new FileWriter(configPath.toAbsolutePath().toString(),false)){
                    String jsonString = serializeConfig(config);
                    fileWriter.write(jsonString);
                    Constants.LOG.info("Successfully wrote config to disk");
                }

            }catch (IOException e){
                Constants.LOG.error("Could not access config file");
                e.printStackTrace();
            }
    }


    public static PotionBlenderConfig deserializeConfig(String configAsJson){
        try {
            PotionBlenderConfig potionBlenderConfig = JSON_PARSER.fromJson(configAsJson, PotionBlenderConfig.class);
            return Objects.requireNonNullElseGet(potionBlenderConfig, ConfigController::getFallback);
        }catch (JsonSyntaxException e){
            Constants.LOG.error("Could not parse config JSON. Make sure syntax is correct");
            e.printStackTrace();
        }
        return getFallback();
    }

    @NotNull
    private static PotionBlenderConfig getFallback() {
        Constants.LOG.warn("Unable to deserialize config, using a default one as fallback");
        return new PotionBlenderConfig();
    }

    public static String serializeConfig(PotionBlenderConfig config){
        return JSON_PARSER.toJson(config);
    }

}
