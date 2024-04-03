package eu.soulsmc.lobbysystem.util;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class Config {

    public final File file;
    private final YamlConfiguration config;

    public Config() {
        File dir = new File("./plugins/Lobby/");

        if (!dir.exists()) {
            dir.mkdirs();
        }

        this.file = new File(dir, "config.yml");

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        this.config = YamlConfiguration.loadConfiguration(file);
        this.addDefaults();
    }

    private void addDefaults() {
        this.addDefault("BroadcastMessages", Arrays.asList("§7Unser Discord §8» §edc.SoulsMC.eu",
                "§7Unsere Webseite §8» §ewww.soulsmc.eu",
                "§7Unser Forum §8» §eforum.soulsmc.eu",
                "§7Bewerbe dich unter §eapply§8@§esoulsmc.eu§8!"));
        this.addDefault("Server" + "." + "DevServer", "devserver1");
        this.addDefault("Server" + "." + "BauServer", "bauserver1");
        this.addDefault("Server" + "." + "Lobby1", "lobby1");
        this.addDefault("Server" + "." + "Lobby2", "lobby2");
        this.addDefault("Server" + "." + "PremiumLobby1", "premiumlobby1");
        this.addDefault("Server" + "." + "SilentLobby1", "silentlobby1");
    }

    private void addDefault(String path, Object value) {
        if(this.config.contains(path)) {
            return;
        }

        this.config.set(path, value);
        this.save();
    }

    public YamlConfiguration getConfig() {
        return config;
    }

    public void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
