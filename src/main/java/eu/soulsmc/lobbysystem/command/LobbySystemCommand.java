package eu.soulsmc.lobbysystem.command;

import eu.soulsmc.lobbysystem.LobbySystem;
import eu.soulsmc.lobbysystem.manager.LocationManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class LobbySystemCommand implements CommandExecutor, TabCompleter {

    private final LobbySystem lobbySystem;
    private final LocationManager locationManager;

    public LobbySystemCommand(@NotNull LobbySystem lobbySystem) {
        this.lobbySystem = lobbySystem;
        this.locationManager = lobbySystem.getLocationManager();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, @NotNull String[] args) {

        if(!(sender instanceof Player player)) {
            return true;
        }

        if(!player.hasPermission("lobbysystem.command.lobbysystem")) {
            player.sendMessage(this.lobbySystem.getPrefix()
                    .append(Component.translatable("command.lobbysystem.invalid.permission")));
            return true;
        }

        if(args.length != 2) {
            player.sendMessage(this.lobbySystem.getPrefix()
                    .append(Component.translatable("command.lobbysystem.invalid.usages")));
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "set" -> {
                Location location = player.getLocation();
                String name = args[1];

                if(this.locationManager.existLocation(name)) {
                    player.sendMessage(this.lobbySystem.getPrefix()
                            .append(Component.translatable("command.lobbysystem.set.invalid.name", NamedTextColor.YELLOW)));
                    return true;
                }

                this.locationManager.setLocation(name, location);
                player.sendMessage(this.lobbySystem.getPrefix()
                        .append(Component.translatable("command.lobbysystem.set", NamedTextColor.YELLOW)
                                .arguments(Component.text(name))));
                return true;
            }

            case "remove" -> {
                String name = args[1];

                if(!this.locationManager.existLocation(name)) {
                    player.sendMessage(this.lobbySystem.getPrefix()
                            .append(Component.translatable("command.lobbysystem.remove.invalid.name")));
                    return true;
                }

                this.locationManager.removeLocation(name);
                player.sendMessage(this.lobbySystem.getPrefix()
                        .append(Component.translatable("command.lobbysystem.remove", NamedTextColor.YELLOW)
                                .arguments(Component.text(name))));
            }

            case "teleport", "tp" -> {
                String name = args[1];

                if(!this.locationManager.existLocation(name)) {
                    player.sendMessage(this.lobbySystem.getPrefix()
                            .append(Component.translatable("command.lobbysystem.teleport.invalid.name")));
                    return true;
                }

                Location location = this.locationManager.getLocation(name);
                player.teleport(location);
                player.sendMessage(this.lobbySystem.getPrefix()
                        .append(Component.translatable("command.lobbysystem.teleport", NamedTextColor.YELLOW)
                                .arguments(Component.text(name))));
            }

            default -> player.sendMessage(this.lobbySystem.getPrefix()
                    .append(Component.translatable("command.lobbysystem.invalid.usages")));
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command,
                                                @NotNull String label, @NotNull String[] args) {
        switch (args.length) {
            case 1 -> {
                return Stream.of("set", "remove", "teleport", "tp").toList().stream().filter(s -> s.startsWith(args[0])).toList();
            }

            case 2 -> {
                if(args[0].equalsIgnoreCase("set")) {
                    return this.locationManager.getSpawnLocations().stream().filter(s -> s.startsWith(args[1])).toList();
                }

                return this.locationManager.getLocations().stream().filter(s -> s.startsWith(args[1])).toList();
            }

            default -> {
                return Collections.emptyList();
            }
        }
    }
}
