package eu.soulsmc.lobbysystem.manager;

import eu.soulsmc.lobbysystem.LobbySystem;
import eu.soulsmc.lobbysystem.util.ItemBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.translation.GlobalTranslator;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ItemsManager {

    private final LobbySystem lobbySystem;

    public ItemsManager(@NotNull LobbySystem lobbySystem) {
        this.lobbySystem = lobbySystem;
    }

    public Inventory navigatorInventory(@NotNull Player player) {
        Inventory inventory = this.lobbySystem.getServer().createInventory(null, 9 * 3, GlobalTranslator.render(
                this.getNavigatorTitle(), player.locale()));

        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayName(Component.empty()).build());
        }

        inventory.setItem(0, new ItemBuilder(Material.SLIME_BALL)
                .setDisplayName(Component.text("Jump & Run", NamedTextColor.RED)
                        .decoration(TextDecoration.ITALIC, false)).build());
        inventory.setItem(3, new ItemBuilder(Material.TNT)
                .setDisplayName(Component.text("TNTRun", NamedTextColor.RED)
                        .decoration(TextDecoration.ITALIC, false)).build());
        inventory.setItem(5, new ItemBuilder(Material.STICK)
                .setDisplayName(Component.text("TTT", NamedTextColor.RED)
                        .decoration(TextDecoration.ITALIC, false)).build());
        inventory.setItem(11, new ItemBuilder(Material.WOODEN_AXE)
                .setDisplayName(Component.text("GunGame", NamedTextColor.RED)
                        .decoration(TextDecoration.ITALIC, false)).addItemFlags(ItemFlag.values()).build());
        inventory.setItem(13, new ItemBuilder(Material.MAGMA_CREAM)
                .setDisplayName(Component.text("Spawn", NamedTextColor.RED)
                        .decoration(TextDecoration.ITALIC, false)).build());
        inventory.setItem(15, new ItemBuilder(Material.RED_BED)
                .setDisplayName(Component.text("BedWars", NamedTextColor.RED)
                        .decoration(TextDecoration.ITALIC, false)).build());
        inventory.setItem(18, new ItemBuilder(Material.MOSS_BLOCK)
                .setDisplayName(Component.text("CityBuild", NamedTextColor.RED)
                        .decoration(TextDecoration.ITALIC, false)).build());
        inventory.setItem(21, new ItemBuilder(Material.FISHING_ROD)
                .setDisplayName(Component.text("KnockIT", NamedTextColor.RED)
                        .decoration(TextDecoration.ITALIC, false)).build());
        inventory.setItem(23, new ItemBuilder(Material.BLAZE_ROD)
                .setDisplayName(Component.text("MLGRush", NamedTextColor.RED)
                        .decoration(TextDecoration.ITALIC, false)).build());

        if (player.hasPermission("lobbysystem.server.buildserver")) {
            inventory.setItem(8, new ItemBuilder(Material.GRASS_BLOCK)
                    .setDisplayName(GlobalTranslator.render(
                                    Component.translatable("lobby.inventory.navigator.server.buildserver"), player.locale())
                            .decoration(TextDecoration.ITALIC, false))
                    .build());
        }

        if (player.hasPermission("lobbysystem.server.developerserver")) {
            inventory.setItem(26, new ItemBuilder(Material.COMMAND_BLOCK)
                    .setDisplayName(GlobalTranslator.render(
                                    Component.translatable("lobby.inventory.navigator.server.developerserver"), player.locale())
                            .decoration(TextDecoration.ITALIC, false))
                    .build());
        }

        return inventory;
    }

    public Inventory cityBuildInventory(@NotNull Player player) {
        Inventory inventory = this.lobbySystem.getServer().createInventory(null, 9 * 3,
                this.getCityBuildTitle());

        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayName(Component.empty()).build());
        }

        List<String> cbServers = this.lobbySystem.getProxyManager().getServers().stream()
                .filter(serverName -> serverName.startsWith("cb"))
                .toList();

        int slot = 11;
        for (String cbServer : cbServers) {
            if (slot >= inventory.getSize()) {
                break;
            }

            inventory.setItem(slot, new ItemBuilder(Material.MOSS_BLOCK)
                    .setDisplayName(Component.text(cbServer, NamedTextColor.RED)
                            .decoration(TextDecoration.ITALIC, false))
                    .build());
            slot++;
        }

        inventory.setItem(18, new ItemBuilder(Material.ARROW)
                .setDisplayName(GlobalTranslator.render(Component.translatable(
                                "lobby.inventory.profile.extras.back.displayname")
                        .decoration(TextDecoration.ITALIC, false), player.locale()))
                .build());

        return inventory;
    }

    public Inventory lobbySwitcherInventory(@NotNull Player player) {
        Inventory inventory = this.lobbySystem.getServer().createInventory(null, 9 * 3, GlobalTranslator.render(
                this.getLobbySwitcherTitle(), player.locale()));

        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayName(Component.empty()).build());
        }

        inventory.setItem(10, new ItemBuilder(Material.GLOWSTONE_DUST)
                .setDisplayName(Component.text(this.lobbySystem.getConfiguration().getConfig()
                                .getString("Server" + "." + "Lobby1"), NamedTextColor.RED)
                        .decoration(TextDecoration.ITALIC, false)).build());
        inventory.setItem(11, new ItemBuilder(Material.GLOWSTONE_DUST)
                .setDisplayName(Component.text(this.lobbySystem.getConfiguration().getConfig()
                                .getString("Server" + "." + "Lobby2"), NamedTextColor.RED)
                        .decoration(TextDecoration.ITALIC, false)).build());

        if (player.hasPermission("lobbysystem.server.silentlobby")) {
            inventory.setItem(13, new ItemBuilder(Material.TNT)
                    .setDisplayName(Component.text(this.lobbySystem.getConfiguration().getConfig()
                                    .getString("Server" + "." + "SilentLobby1"), NamedTextColor.RED)
                            .decoration(TextDecoration.ITALIC, false)).build());
        }

        if (player.hasPermission("lobbysystem.server.premiumlobby")) {
            inventory.setItem(16, new ItemBuilder(Material.GOLD_BLOCK)
                    .setDisplayName(Component.text(this.lobbySystem.getConfiguration().getConfig()
                                    .getString("Server" + "." + "PremiumLobby1"), NamedTextColor.RED)
                            .decoration(TextDecoration.ITALIC, false)).build());
        }

        return inventory;
    }

    public Inventory profileInventory(@NotNull Player player) {
        Inventory inventory = this.lobbySystem.getServer().createInventory(null, 9 * 3, GlobalTranslator.render(
                this.getProfileTitle(), player.locale()));

        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayName(Component.empty()).build());
        }

        inventory.setItem(12, new ItemBuilder(Material.PLAYER_HEAD)
                .setDisplayName(GlobalTranslator.render(Component.translatable(
                                "lobby.inventory.profile.friends.displayname")
                        .decoration(TextDecoration.ITALIC, false), player.locale()))
                .setSkullOwner(player)
                .build());

        inventory.setItem(14, new ItemBuilder(Material.ENDER_CHEST)
                .setDisplayName(GlobalTranslator.render(Component.translatable(
                                "lobby.inventory.profile.extras.displayname")
                        .decoration(TextDecoration.ITALIC, false), player.locale()))
                .build());
        return inventory;
    }

    public Inventory extrasInventory(@NotNull Player player) {
        Inventory inventory = this.lobbySystem.getServer().createInventory(null, 9 * 3, GlobalTranslator.render(
                this.getExtrasTitle(), player.locale()));

        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayName(Component.empty()).build());
        }

        inventory.setItem(11, new ItemBuilder(Material.DIAMOND_BOOTS)
                .setDisplayName(GlobalTranslator.render(Component.translatable(
                                "lobby.inventory.profile.extras.boots.displayname")
                        .decoration(TextDecoration.ITALIC, false), player.locale()))
                .addItemFlags(ItemFlag.values())
                .build());

        inventory.setItem(13, new ItemBuilder(Material.CARVED_PUMPKIN)
                .setDisplayName(GlobalTranslator.render(Component.translatable(
                                "lobby.inventory.profile.extras.heads.displayname")
                        .decoration(TextDecoration.ITALIC, false), player.locale()))
                .addItemFlags(ItemFlag.values())
                .build());

        inventory.setItem(15, new ItemBuilder(Material.REDSTONE_BLOCK)
                .setDisplayName(GlobalTranslator.render(Component.translatable(
                                "lobby.inventory.profile.extras.gadgets.displayname")
                        .decoration(TextDecoration.ITALIC, false), player.locale()))
                .addItemFlags(ItemFlag.values())
                .build());

        inventory.setItem(18, new ItemBuilder(Material.ARROW)
                .setDisplayName(GlobalTranslator.render(Component.translatable(
                                "lobby.inventory.profile.extras.back.displayname")
                        .decoration(TextDecoration.ITALIC, false), player.locale()))
                .build());

        if (player.getInventory().getHelmet() != null || (player.getInventory().getBoots() != null
                && this.hasRankBoots(player))
                || player.getInventory().getItem(2).getType().equals(Material.FISHING_ROD)
                || player.getInventory().getItem(2).getType().equals(Material.ENDER_PEARL)) {
            inventory.setItem(26, new ItemBuilder(Material.BARRIER)
                    .setDisplayName(GlobalTranslator.render(Component.translatable(
                                    "lobby.inventory.profile.extras.reset.displayname")
                            .decoration(TextDecoration.ITALIC, false), player.locale()))
                    .addItemFlags(ItemFlag.values())
                    .build());
        }
        return inventory;
    }

    public boolean hasRankBoots(Player player) {
        String groupColored = this.lobbySystem.getLobbyManager().getGroupColored(player);
        Component rankBootsName = GlobalTranslator.render(
                Component.text(groupColored)
                        .append(Component.space())
                        .append(Component.translatable("lobby.inventory.boots.displayname")),
                player.locale()
        );

        return !rankBootsName.equals(player.getInventory().getBoots().getItemMeta().displayName());
    }

    public Inventory bootsInventory(@NotNull Player player) {
        Inventory inventory = this.lobbySystem.getServer().createInventory(null, 9 * 3, GlobalTranslator.render(
                this.getBootsTitle(), player.locale()));

        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayName(Component.empty()).build());
        }

        inventory.setItem(12, new ItemBuilder(Material.LEATHER_BOOTS)
                .setDisplayName(GlobalTranslator.render(Component.translatable(
                                "lobby.inventory.profile.extras.boots.love-boots.displayname")
                        .decoration(TextDecoration.ITALIC, false), player.locale()))
                .setColor(Color.fromRGB(255, 0, 0))
                .addItemFlags(ItemFlag.values())
                .build());

        inventory.setItem(13, new ItemBuilder(Material.LEATHER_BOOTS)
                .setDisplayName(GlobalTranslator.render(Component.translatable(
                                "lobby.inventory.profile.extras.boots.angry-boots.displayname")
                        .decoration(TextDecoration.ITALIC, false), player.locale()))
                .setColor(Color.fromRGB(99, 99, 99))
                .addItemFlags(ItemFlag.values())
                .build());

        inventory.setItem(14, new ItemBuilder(Material.LEATHER_BOOTS)
                .setDisplayName(GlobalTranslator.render(Component.translatable(
                                "lobby.inventory.profile.extras.boots.water-boots.displayname")
                        .decoration(TextDecoration.ITALIC, false), player.locale()))
                .setColor(Color.fromRGB(29, 97, 83))
                .addItemFlags(ItemFlag.values())
                .build());

        inventory.setItem(18, new ItemBuilder(Material.ARROW)
                .setDisplayName(GlobalTranslator.render(Component.translatable(
                                "lobby.inventory.profile.extras.back.displayname")
                        .decoration(TextDecoration.ITALIC, false), player.locale()))
                .build());

        return inventory;
    }

    public Inventory headsInventory(@NotNull Player player) {
        Inventory inventory = this.lobbySystem.getServer().createInventory(null, 9 * 3, GlobalTranslator.render(
                this.getHeadsTitle(), player.locale()));

        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayName(Component.empty()).build());
        }

        inventory.setItem(10, new ItemBuilder(Material.PLAYER_HEAD)
                .setDisplayName(Component.text("Razuuu", NamedTextColor.RED)
                        .decoration(TextDecoration.ITALIC, false))
                .setSkullOwner("Razuuu")
                .build());
        inventory.setItem(11, new ItemBuilder(Material.PLAYER_HEAD)
                .setDisplayName(Component.text("Lvcq_", NamedTextColor.RED)
                        .decoration(TextDecoration.ITALIC, false))
                .setSkullOwner("Lvcq_")
                .build());
        inventory.setItem(12, new ItemBuilder(Material.PLAYER_HEAD)
                .setDisplayName(Component.text("G8I", NamedTextColor.RED)
                        .decoration(TextDecoration.ITALIC, false))
                .setSkullOwner("G8I")
                .build());
        inventory.setItem(13, new ItemBuilder(Material.PLAYER_HEAD)
                .setDisplayName(Component.text("AbgegrieftHD", NamedTextColor.RED)
                        .decoration(TextDecoration.ITALIC, false))
                .setSkullOwner("AbgegrieftHD")
                .build());
        inventory.setItem(14, new ItemBuilder(Material.PLAYER_HEAD)
                .setDisplayName(Component.text("FlooTastisch", NamedTextColor.RED)
                        .decoration(TextDecoration.ITALIC, false))
                .setSkullOwner("FlooTastisch")
                .build());
        inventory.setItem(15, new ItemBuilder(Material.PLAYER_HEAD)
                .setDisplayName(Component.text("utmedkua", NamedTextColor.RED)
                        .decoration(TextDecoration.ITALIC, false))
                .setSkullOwner("utmedkua")
                .build());
        inventory.setItem(16, new ItemBuilder(Material.PLAYER_HEAD)
                .setDisplayName(Component.text("x33", NamedTextColor.RED)
                        .decoration(TextDecoration.ITALIC, false))
                .setSkullOwner("x33")
                .build());
        inventory.setItem(18, new ItemBuilder(Material.ARROW)
                .setDisplayName(GlobalTranslator.render(Component.translatable(
                                "lobby.inventory.profile.extras.back.displayname")
                        .decoration(TextDecoration.ITALIC, false), player.locale()))
                .build());

        return inventory;
    }

    public Inventory gadgetsInventory(@NotNull Player player) {
        Inventory inventory = this.lobbySystem.getServer().createInventory(null, 9 * 3, GlobalTranslator.render(
                this.getGadgetsTitle(), player.locale()));

        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayName(Component.empty()).build());
        }

        inventory.setItem(12, new ItemBuilder(Material.FISHING_ROD)
                .setDisplayName(GlobalTranslator.render(Component.translatable(
                                "lobby.inventory.profile.extras.gadgets.rod.displayname")
                        .decoration(TextDecoration.ITALIC, false), player.locale()))
                .build());
        inventory.setItem(14, new ItemBuilder(Material.ENDER_PEARL)
                .setDisplayName(GlobalTranslator.render(Component.translatable(
                                "lobby.inventory.profile.extras.gadgets.enderpearl.displayname")
                        .decoration(TextDecoration.ITALIC, false), player.locale()))
                .build());
        inventory.setItem(18, new ItemBuilder(Material.ARROW)
                .setDisplayName(GlobalTranslator.render(Component.translatable(
                                "lobby.inventory.profile.extras.back.displayname")
                        .decoration(TextDecoration.ITALIC, false), player.locale()))
                .build());

        return inventory;
    }

    public Component getNavigatorTitle() {
        return this.lobbySystem.getItemsPrefix()
                .append(Component.translatable("lobby.inventory.navigator.title"));
    }

    public Component getCityBuildTitle() {
        return this.lobbySystem.getItemsPrefix()
                .append(Component.text("CityBuild Server"));
    }

    public Component getLobbySwitcherTitle() {
        return this.lobbySystem.getItemsPrefix()
                .append(Component.translatable("lobby.inventory.lobby-switcher.title"));
    }

    public Component getProfileTitle() {
        return this.lobbySystem.getItemsPrefix()
                .append(Component.translatable("lobby.inventory.profile.title"));
    }

    public Component getExtrasTitle() {
        return this.lobbySystem.getItemsPrefix()
                .append(Component.translatable("lobby.inventory.profile.extras.title"));
    }

    public Component getBootsTitle() {
        return this.lobbySystem.getItemsPrefix()
                .append(Component.translatable("lobby.inventory.profile.extras.boots.title"));
    }

    public Component getHeadsTitle() {
        return this.lobbySystem.getItemsPrefix()
                .append(Component.translatable("lobby.inventory.profile.extras.heads.title"));
    }

    public Component getGadgetsTitle() {
        return this.lobbySystem.getItemsPrefix()
                .append(Component.translatable("lobby.inventory.profile.extras.gadgets.title"));
    }
}
