package eu.soulsmc.lobbysystem.manager;

import eu.soulsmc.lobbysystem.LobbySystem;
import eu.soulsmc.lobbysystem.util.Config;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class LocationManager {

    private final LobbySystem lobbySystem;
    private final Config config;

    public LocationManager(@NotNull LobbySystem lobbySystem) {
        this.lobbySystem = lobbySystem;
        this.config = lobbySystem.getConfiguration();
    }

    public void setLocation(@NotNull String name, @NotNull Location location) {
        this.config.getConfig().set("Locations" + "." + name + "." + "X", location.getBlockX());
        this.config.getConfig().set("Locations" + "." + name + "." + "Y", location.getBlockY());
        this.config.getConfig().set("Locations" + "." + name + "." + "Z", location.getBlockZ());
        this.config.getConfig().set("Locations" + "." + name + "." + "Yaw", location.getYaw());
        this.config.getConfig().set("Locations" + "." + name + "." + "Pitch", location.getPitch());
        this.config.save();
    }

    public void removeLocation(@NotNull String name) {
        this.config.getConfig().set("Locations" + "." + name, null);
        this.config.save();
    }

    public Location getLocation(@NotNull String name) {
        double x = this.config.getConfig().getDouble("Locations" + "." + name + "." + "X");
        double y = this.config.getConfig().getDouble("Locations" + "." + name + "." + "Y");
        double z = this.config.getConfig().getDouble("Locations" + "." + name + "." + "Z");
        float yaw = (float) this.config.getConfig().getDouble("Locations" + "." + name + "." + "Yaw");
        float pitch = (float) this.config.getConfig().getDouble("Locations" + "." + name + "." + "Pitch");

        return new Location(this.lobbySystem.getServer().getWorld("world"), x + 0.5, y + 1, z + 0.5, yaw, pitch);
    }

    public boolean existLocation(@NotNull String name) {
        return this.config.getConfig().contains("Locations" + "." + name);
    }

    public List<String> getLocations() {
        ConfigurationSection section = this.config.getConfig().getConfigurationSection("Locations");

        if(section == null) {
            return Collections.emptyList();
        }

        return new ArrayList<>(section.getKeys(false));
    }

    public List<String> getSpawnLocations() {
       return Stream.of("Spawn", "CityBuild", "GunGame", "Bedwars", "TNTRun", "TTT", "KnockIT", "MLGRush", "Jump&Run").toList();
    }
}
