package dev.itzpipeg.pinsystem;

import com.google.common.io.ByteStreams;
import dev.itzpipeg.pinsystem.utils.CC;
import dev.itzpipeg.pinsystem.utils.pinsystem.PinManager;
import dev.itzpipeg.pinsystem.utils.pinsystem.events.PostLogin;
import dev.itzpipeg.pinsystem.utils.pinsystem.commands.PinCommand;
import dev.itzpipeg.pinsystem.utils.pinsystem.commands.PinSystemCommand;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.*;

public class Main extends Plugin {
    private Configuration config;
    private File dataFile;
    private Configuration dataConfig;
    private File languageFile;
    private Configuration languageConfig;
    public PinManager pinManager;
    private String VERSION = "1.0";

    @Override
    public void onEnable() {
        if(getProxy().getPluginManager().getPlugin("nLogin") != null){
            this.pinManager = new PinManager(this);
            registerConfig();
            registerDataFile();
            registerLanguageFile();
            registerCommands();
            registerEvents();
            getLogger().info(CC.translate("&r\n "+
                    "&9  _____ _          _____           _                 \n" +
                    "&9 |  __ (_)        / ____|         | |                \n" +
                    "&9 | |__) | _ __   | (___  _   _ ___| |_ ___ _ __ ___  \n" +
                    "&9 |  ___/ | '_ \\   \\___ \\| | | / __| __/ _ \\ '_ ` _ \\ \n" +
                    "&9 | |   | | | | |  ____) | |_| \\__ \\ ||  __/ | | | | |\n" +
                    "&9 |_|   |_|_| |_| |_____/ \\__, |___/\\__\\___|_| |_| |_|\n" +
                    "&9                          __/ |                      \n" +
                    "&9                         |___/                       \n"+
                    "&9Developed by itzpipe - V"+VERSION+"\n"+
                    "&r"));
        }else{
            getLogger().info(CC.translate("&r\n "+
                    "&9  _____ _          _____           _                 \n" +
                    "&9 |  __ (_)        / ____|         | |                \n" +
                    "&9 | |__) | _ __   | (___  _   _ ___| |_ ___ _ __ ___  \n" +
                    "&9 |  ___/ | '_ \\   \\___ \\| | | / __| __/ _ \\ '_ ` _ \\ \n" +
                    "&9 | |   | | | | |  ____) | |_| \\__ \\ ||  __/ | | | | |\n" +
                    "&9 |_|   |_|_| |_| |_____/ \\__, |___/\\__\\___|_| |_| |_|\n" +
                    "&9                          __/ |                      \n" +
                    "&9                         |___/                       \n"+
                    "&9Developed by itzpipe - V"+VERSION+"\n"+
                    "&r\n"+
                    "&cNot founded the plugin nLogin in /plugins folder.\n"+
                    "&r"));
        }

    }

    private void registerConfig() {
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }

        File configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
                try (InputStream is = getResourceAsStream("config.yml");
                     OutputStream os = new FileOutputStream(configFile)) {
                    ByteStreams.copy(is, os);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try{
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    public Configuration getConfig() {
        return config;
    }

    public void reloadConfig() {
        File configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            return;
        }
        try {
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void registerDataFile() {
        dataFile = new File(getDataFolder(), "playerdata.yml");
        if (!dataFile.exists()) {
            try {
                dataFile.createNewFile();
                try (InputStream is = getResourceAsStream("language.yml");
                     OutputStream os = new FileOutputStream(dataFile)) {
                    ByteStreams.copy(is, os);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            dataConfig = ConfigurationProvider.getProvider(YamlConfiguration.class).load(dataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Configuration getDataFile(){
        return dataConfig;
    }

    public void reloadDataFile() {
        File configFile = new File(getDataFolder(), "playerdata.yml");
        if (!configFile.exists()) {
            return;
        }
        try {
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(dataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveDataFile(){
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(dataConfig, dataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void registerLanguageFile() {
        languageFile = new File(getDataFolder(), "language.yml");
        if (!languageFile.exists()) {
            try {
                languageFile.createNewFile();
                try (InputStream is = getResourceAsStream("language.yml");
                     OutputStream os = new FileOutputStream(languageFile)) {
                    ByteStreams.copy(is, os);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            languageConfig = ConfigurationProvider.getProvider(YamlConfiguration.class).load(languageFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Configuration getLanguageFile(){
        return languageConfig;
    }

    public void reloadLanguageFile() {
        File configFile = new File(getDataFolder(), "language.yml");
        if (!configFile.exists()) {
            return;
        }
        try {
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(languageFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveLanguageFile(){
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(languageConfig, languageFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void registerCommands(){
        getProxy().getPluginManager().registerCommand(this, new PinCommand(this, pinManager));
        getProxy().getPluginManager().registerCommand(this, new PinSystemCommand(this, pinManager));
    }

    public void registerEvents(){
        getProxy().getPluginManager().registerListener(this, new PostLogin(this, pinManager));
    }

}
