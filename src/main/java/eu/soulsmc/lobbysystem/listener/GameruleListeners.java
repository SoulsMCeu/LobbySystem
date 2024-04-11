package eu.soulsmc.lobbysystem.listener;

import com.destroystokyo.paper.event.player.PlayerPickupExperienceEvent;
import eu.soulsmc.lobbysystem.LobbySystem;
import eu.soulsmc.lobbysystem.manager.LobbyManager;
import eu.soulsmc.lobbysystem.scoreboard.LobbyScoreboard;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.time.Year;

public class GameruleListeners implements Listener {

    private final LobbySystem lobbySystem;
    private final LobbyManager lobbyManager;

    public GameruleListeners(@NotNull LobbySystem lobbySystem) {
        this.lobbySystem = lobbySystem;
        this.lobbyManager = lobbySystem.getLobbyManager();

        lobbyManager.setGameRules();
    }

    @EventHandler
    public void onJoin(@NotNull PlayerJoinEvent event) {
        Player player = event.getPlayer();
        event.joinMessage(null);

        if(this.lobbySystem.getLocationManager().existLocation("Spawn")) {
            player.teleport(this.lobbySystem.getLocationManager().getLocation("Spawn"));
        }

        this.lobbyManager.setUpPlayer(player);
        this.lobbyManager.addLobbyItems(player);

        player.setAllowFlight(true);
        player.setFlying(false);

        this.lobbySystem.getServer().getOnlinePlayers().forEach(players -> {
            if(this.lobbyManager.getHideList().contains(players.getUniqueId())) {
                players.hidePlayer(player);
            }
        });

        this.lobbySystem.getServer().getScheduler().runTaskLater(this.lobbySystem, () -> this.lobbySystem.getProxyManager().checkPlayerServer(player), 1L);
        this.lobbySystem.getServer().getScheduler().runTaskLater(this.lobbySystem, () -> new LobbyScoreboard(this.lobbySystem, player), 1L);
    }

    @EventHandler
    public void onQuit(@NotNull PlayerQuitEvent event) {
        Player player = event.getPlayer();
        event.quitMessage(null);
        this.lobbyManager.getFlyList().remove(player.getUniqueId());
        this.lobbyManager.getBuildList().remove(player.getUniqueId());
    }

    @EventHandler
    public void onPlace(@NotNull BlockPlaceEvent event) {
        Player player = event.getPlayer();

        if(this.lobbyManager.getBuildList().contains(player.getUniqueId())) {
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void onBreak(@NotNull BlockBreakEvent event) {
        Player player = event.getPlayer();

        if(this.lobbyManager.getBuildList().contains(player.getUniqueId())) {
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void onDamage(@NotNull EntityDamageEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onInteract(@NotNull PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if(this.lobbyManager.getBuildList().contains(player.getUniqueId())) {
            return;
        }

        if(event.getItem() != null && event.getItem().getType().equals(Material.FISHING_ROD)) {
            return;
        }

        if(event.getItem() != null && event.getItem().getType().equals(Material.ENDER_PEARL)) {
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void onInteract(@NotNull PlayerInteractAtEntityEvent event) {
        Player player = event.getPlayer();

        if(this.lobbyManager.getBuildList().contains(player.getUniqueId())) {
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void onLevelUP(@NotNull PlayerLevelChangeEvent event) {
        Player player = event.getPlayer();
        if(event.getNewLevel() != Year.now().getValue()) {
            player.setLevel(Year.now().getValue());
            player.setExp(1.0f);
        }
    }

    @EventHandler
    public void onFood(@NotNull FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onInventory(@NotNull InventoryClickEvent event) {
        if(!(event.getWhoClicked() instanceof Player player)) {
            return;
        }

        if(this.lobbyManager.getBuildList().contains(player.getUniqueId())) {
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void onDrop(@NotNull PlayerDropItemEvent event) {
        Player player = event.getPlayer();

        if(this.lobbyManager.getBuildList().contains(player.getUniqueId())) {
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void onPickUp(@NotNull PlayerPickupItemEvent event) {
        Player player = event.getPlayer();

        if(this.lobbyManager.getBuildList().contains(player.getUniqueId())) {
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void onWeather(@NotNull WeatherChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onSwap(@NotNull PlayerSwapHandItemsEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onXP(@NotNull PlayerPickupExperienceEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onHeldItem(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        Inventory inventory = player.getInventory();
        ItemStack itemStack = inventory.getItem(event.getNewSlot());

        if(itemStack == null) {
            return;
        }

        if(itemStack.getType().equals(Material.AIR)) {
            return;
        }

        event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.BLOCK_NOTE_BLOCK_BASEDRUM, 3.0F, 2.0F);
    }
}
