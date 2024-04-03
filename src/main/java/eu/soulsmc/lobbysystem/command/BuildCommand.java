package eu.soulsmc.lobbysystem.command;

import eu.soulsmc.lobbysystem.LobbySystem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class BuildCommand implements CommandExecutor, TabCompleter {

    private final LobbySystem lobbySystem;

    public BuildCommand(@NotNull LobbySystem lobbySystem) {
        this.lobbySystem = lobbySystem;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, @NotNull String[] args) {

        if(!(sender instanceof Player player)) {
            return true;
        }

        if(!player.hasPermission("lobbysystem.command.build")) {
            player.sendMessage(this.lobbySystem.getPrefix()
                    .append(Component.translatable("command.build.invalid.permission", NamedTextColor.RED)));
            return true;
        }

        if(args.length == 1 && player.hasPermission("lobbysystem.command.build.other")) {
            Player target = this.lobbySystem.getServer().getPlayer(args[0]);

            if(target == null) {
                player.sendMessage(this.lobbySystem.getPrefix()
                        .append(Component.translatable("command.build.invalid.player", NamedTextColor.RED)));
                return true;
            }

            this.lobbySystem.getLobbyManager().toggleBuildMode(target);
            if(this.lobbySystem.getLobbyManager().getBuildList().contains(target.getUniqueId())) {
                target.getInventory().clear();
                target.setGameMode(GameMode.CREATIVE);
                target.sendMessage(this.lobbySystem.getPrefix()
                        .append(Component.translatable("command.build.activated", NamedTextColor.GREEN)));
                player.sendMessage(this.lobbySystem.getPrefix()
                        .append(Component.translatable("command.build.activate.other", NamedTextColor.GRAY)
                                .arguments(Component.text(target.getName(), NamedTextColor.GREEN))));
                return true;
            }

            target.getInventory().clear();
            target.setGameMode(GameMode.ADVENTURE);
            this.lobbySystem.getLobbyManager().addLobbyItems(target);
            target.sendMessage(this.lobbySystem.getPrefix()
                    .append(Component.translatable("command.build.deactivated", NamedTextColor.GREEN)));
            player.sendMessage(this.lobbySystem.getPrefix()
                    .append(Component.translatable("command.build.deactivate.other", NamedTextColor.GRAY)
                            .arguments(Component.text(target.getName(), NamedTextColor.RED))));
            return true;
        }

        this.lobbySystem.getLobbyManager().toggleBuildMode(player);
        if(this.lobbySystem.getLobbyManager().getBuildList().contains(player.getUniqueId())) {
            player.getInventory().clear();
            player.setGameMode(GameMode.CREATIVE);
            player.sendMessage(this.lobbySystem.getPrefix()
                    .append(Component.translatable("command.build.activate", NamedTextColor.GREEN)));
            return true;
        }

        player.getInventory().clear();
        player.setGameMode(GameMode.ADVENTURE);
        this.lobbySystem.getLobbyManager().addLobbyItems(player);
        player.sendMessage(this.lobbySystem.getPrefix()
                .append(Component.translatable("command.build.deactivate", NamedTextColor.GREEN)));
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command,
                                                @NotNull String label, @NotNull String[] args) {
        return Collections.emptyList();
    }
}
