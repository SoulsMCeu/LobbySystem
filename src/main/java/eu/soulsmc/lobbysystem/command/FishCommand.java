package eu.soulsmc.lobbysystem.command;

import eu.soulsmc.lobbysystem.LobbySystem;
import eu.soulsmc.lobbysystem.util.ItemBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class FishCommand implements CommandExecutor, TabCompleter {

    private final LobbySystem lobbySystem;

    public FishCommand(@NotNull LobbySystem lobbySystem) {
        this.lobbySystem = lobbySystem;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, @NotNull String[] args) {

        if(!(sender instanceof Player player)) {
            return true;
        }

        String displayName = "§1F§2i§3s§4h";
        ItemStack itemStack = new ItemBuilder(Material.TROPICAL_FISH)
                .setDisplayName(Component.text(displayName))
                .build();

        for(int i = 0; i < 36; i++) {
            if(player.getInventory().getItem(i) != null) {
                continue;
            }

            player.getInventory().setItem(i, itemStack);
        }

        this.lobbySystem.getServer().getScheduler().runTaskLater(this.lobbySystem, () -> player.getInventory().remove(Material.TROPICAL_FISH), 60L);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command,
                                                @NotNull String label, @NotNull String[] args) {
        return Collections.emptyList();
    }
}
