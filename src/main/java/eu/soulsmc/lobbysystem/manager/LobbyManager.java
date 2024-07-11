package eu.soulsmc.lobbysystem.manager;

import eu.soulsmc.lobbysystem.LobbySystem;
import eu.soulsmc.lobbysystem.util.ItemBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.translation.GlobalTranslator;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.jetbrains.annotations.NotNull;

import java.time.Year;
import java.util.*;

public class LobbyManager {

    private final LobbySystem lobbySystem;
    private final List<UUID> flyList;
    private final List<UUID> buildList;
    private final List<UUID> hideList;

    public LobbyManager(@NotNull LobbySystem lobbySystem) {
        this.lobbySystem = lobbySystem;
        this.flyList = new ArrayList<>();
        this.buildList = new ArrayList<>();
        this.hideList = new ArrayList<>();

        this.sendBroadCastMessages();
    }

    public void setGameRules() {
        this.lobbySystem.getServer().getWorlds().forEach(world -> this.lobbySystem.getServer().getScheduler().runTaskTimer(this.lobbySystem, () -> {
            world.setDifficulty(Difficulty.PEACEFUL);

            world.setTime(0);
            world.setThundering(false);
            world.setStorm(false);
            world.setPVP(false);
            world.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
            world.setGameRule(GameRule.MOB_GRIEFING, false);
            world.setGameRule(GameRule.KEEP_INVENTORY, false);
            world.setGameRule(GameRule.DO_MOB_SPAWNING, false);
            world.setGameRule(GameRule.NATURAL_REGENERATION, false);
        }, 1L, 1L));
    }

    public void setUpPlayer(@NotNull Player player) {
        player.setGameMode(GameMode.ADVENTURE);
        player.getInventory().clear();

        player.setHealthScale(20.0);
        player.setHealth(20.0);
        player.setFoodLevel(20);

        player.setLevel(Year.now().getValue());
        player.setExp(1.0f);
    }

    public void addLobbyItems(@NotNull Player player) {
        player.getInventory().setItem(1, new ItemBuilder(Material.PINK_DYE)
                .setDisplayName(GlobalTranslator.render(this.lobbySystem.getItemsPrefix()
                        .append(Component.translatable("lobby.items.hide-players.displayname",
                                        NamedTextColor.GRAY)
                        .decoration(TextDecoration.ITALIC, false)), player.locale()))
                .build());

        player.getInventory().setItem(2, new ItemBuilder(Material.BARRIER)
                .setDisplayName(GlobalTranslator.render(this.lobbySystem.getItemsPrefix()
                        .append(Component.translatable("lobby.items.gadget.displayname",
                                        NamedTextColor.GRAY)
                        .decoration(TextDecoration.ITALIC, false)), player.locale()))
                .build());
        player.getInventory().setItem(4, new ItemBuilder(Material.MUSIC_DISC_5)
                .setDisplayName(GlobalTranslator.render(this.lobbySystem.getItemsPrefix()
                        .append(Component.translatable("lobby.items.navigator.displayname",
                                        NamedTextColor.GRAY)
                        .decoration(TextDecoration.ITALIC, false)), player.locale()))
                .addItemFlags(ItemFlag.values())
                .build());

        player.getInventory().setItem(6, new ItemBuilder(Material.NETHER_STAR)
                .setDisplayName(GlobalTranslator.render(this.lobbySystem.getItemsPrefix()
                        .append(Component.translatable("lobby.items.lobby-switcher.displayname",
                                        NamedTextColor.GRAY)
                        .decoration(TextDecoration.ITALIC, false)), player.locale()))
                .build());

        player.getInventory().setItem(7, new ItemBuilder(Material.PLAYER_HEAD)
                .setDisplayName(GlobalTranslator.render(this.lobbySystem.getItemsPrefix()
                        .append(Component.translatable("lobby.items.profile.displayname",
                                        NamedTextColor.GRAY)
                        .decoration(TextDecoration.ITALIC, false)), player.locale()))
                .setSkullOwner(player)
                .build());

        if (!player.hasPermission("lobbysystem.item.flightfeather")) {
            return;
        }

        player.getInventory().setItem(31, new ItemBuilder(Material.FEATHER)
                .setDisplayName(GlobalTranslator.render(this.lobbySystem.getItemsPrefix()
                        .append(Component.translatable("lobby.items.flight-feather.displayname",
                                        NamedTextColor.GRAY)
                        .decoration(TextDecoration.ITALIC, false)), player.locale()))
                .build());

        this.setTeamBoots(player);
    }

    private void setTeamBoots(@NotNull Player player) {
        int[] color = getTeamBootsColor(player);
        player.getInventory().setBoots(new ItemBuilder(Material.LEATHER_BOOTS)
                .setDisplayName(
                        GlobalTranslator.render(
                                Component.text(this.getRank(player, true))
                                        .appendSpace()
                                        .append(Component
                                                .translatable("lobby.inventory.boots.displayname")),
                                player.locale()))
                .setColor(Color.fromRGB(color[0], color[1], color[2]))
                .addItemFlags(ItemFlag.values())
                .build());
    }

    public int[] getTeamBootsColor(@NotNull Player player) {
        final Map<String, int[]> rankColors = new HashMap<>();
        rankColors.put("Inhaber", new int[]{170, 0, 0});
        rankColors.put("Admin", new int[]{170, 0, 0});
        rankColors.put("Developer", new int[]{85, 255, 255});
        rankColors.put("Moderator", new int[]{255, 255, 75});
        rankColors.put("Supporter", new int[]{85, 255, 85});
        rankColors.put("Builder", new int[]{85, 255, 85});
        rankColors.put("Azubi", new int[]{0, 170, 170});

        return rankColors.get(getRank(player, false));
    }

    public void toggleFlyMode(@NotNull Player player) {
        if (this.flyList.contains(player.getUniqueId())) {
            this.flyList.remove(player.getUniqueId());
            player.setAllowFlight(false);
            player.setFlying(false);
            player.sendActionBar(Component.translatable("lobby.deactivate.flight-mode"));
        } else {
            this.flyList.add(player.getUniqueId());
            player.setAllowFlight(true);
            player.setFlying(false);
            player.sendActionBar(Component.translatable("lobby.activate.flight-mode"));
        }
    }

    public void toggleBuildMode(@NotNull Player player) {
        if (this.buildList.contains(player.getUniqueId())) {
            this.buildList.remove(player.getUniqueId());
        } else {
            this.buildList.add(player.getUniqueId());
        }
    }

    public void toggleHideMode(@NotNull Player player) {
        if (this.hideList.contains(player.getUniqueId())) {
            this.hideList.remove(player.getUniqueId());
            player.sendMessage(this.lobbySystem.getPrefix()
                    .append(Component.translatable("lobby.show-players", NamedTextColor.GRAY)));
            this.lobbySystem.getServer().getOnlinePlayers().forEach(player::showPlayer);
            player.playSound(player, Sound.ENTITY_CHICKEN_EGG, 2.0f, 1.0f);
        } else {
            this.hideList.add(player.getUniqueId());
            player.sendMessage(this.lobbySystem.getPrefix()
                    .append(Component.translatable("lobby.hide-players", NamedTextColor.GRAY)));
            this.lobbySystem.getServer().getOnlinePlayers().forEach(player::hidePlayer);
            player.playSound(player, Sound.ENTITY_CHICKEN_EGG, 2.0f, 1.0f);
        }
    }

    private void sendBroadCastMessages() {
        List<String> broadCastMessages = this.lobbySystem.getConfiguration().getConfig().getStringList("BroadcastMessages");

        this.lobbySystem.getServer().getScheduler().scheduleSyncRepeatingTask(this.lobbySystem, () -> {
            if (broadCastMessages.isEmpty()) {
                return;
            }

            Random random = new Random();
            int randomInt = random.nextInt(broadCastMessages.size());
            this.lobbySystem.getServer().getOnlinePlayers().forEach(players -> players.sendActionBar(Component.text(broadCastMessages.get(randomInt))));
        }, 10 * 20L, 10 * 20L);
    }

    public String getRank(Player player, boolean color) {
        String[][] ranks = {
                {"group.inhaber", "§4Inhaber"},
                {"group.admin", "§4Admin"},
                {"group.srdeveloper", "§bSrDeveloper"},
                {"group.developer", "§bDeveloper"},
                {"group.srmoderator", "§cSrModerator"},
                {"group.moderator", "§cModerator"},
                {"group.supporter", "§9Supporter"},
                {"group.builder", "§eBuilder"},
                {"group.azubi", "§3Azubi"},
                {"group.freund", "§aFreund"},
                {"group.youtube", "§5YouTube"},
                {"group.premiumplus", "§ePremium§a+"},
                {"group.iron", "§fIron"},
                {"group.premium", "§6Premium"},
                {"group.default", "§7Spieler"}
        };

        for (String[] rank : ranks) {
            if (player.hasPermission(rank[0])) {
                return color ? rank[1] : rank[1].substring(2);
            }
        }

        return color ? "§7Spieler" : "Spieler";
    }

    public List<UUID> getFlyList() {
        return flyList;
    }

    public List<UUID> getBuildList() {
        return buildList;
    }

    public List<UUID> getHideList() {
        return hideList;
    }
}
