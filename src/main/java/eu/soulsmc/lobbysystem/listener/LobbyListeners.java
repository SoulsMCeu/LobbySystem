package eu.soulsmc.lobbysystem.listener;

import eu.soulsmc.lobbysystem.LobbySystem;
import eu.soulsmc.lobbysystem.manager.ItemsManager;
import eu.soulsmc.lobbysystem.util.ItemBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.translation.GlobalTranslator;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LobbyListeners implements Listener {

    private final LobbySystem lobbySystem;
    private final ItemsManager itemsManager;
    private final List<UUID> cooldownList;

    public LobbyListeners(@NotNull LobbySystem lobbySystem) {
        this.lobbySystem = lobbySystem;
        this.itemsManager = lobbySystem.getItemsManager();
        this.cooldownList = new ArrayList<>();
    }

    @EventHandler
    public void onDoubleJump(@NotNull PlayerToggleFlightEvent event) {
        Player player = event.getPlayer();

        if(player.getGameMode().equals(GameMode.CREATIVE)) {
            return;
        }

        if(player.getGameMode().equals(GameMode.SPECTATOR)) {
            return;
        }

        if(player.isFlying()) {
            return;
        }

        if(player.isSwimming()) {
            return;
        }

        if(this.lobbySystem.getLobbyManager().getFlyList().contains(player.getUniqueId())) {
            return;
        }

        event.setCancelled(true);
        player.setAllowFlight(false);

        Vector vector = player.getLocation().getDirection().normalize();
        vector.setY(Math.max(0.5D, vector.getY())).multiply(2);
        player.setVelocity(vector);

        player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_FLAP, 2.0f, 1.0f);
    }

    @EventHandler
    public void onMove(@NotNull PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if(player.getGameMode().equals(GameMode.CREATIVE)) {
            return;
        }

        if(player.getGameMode().equals(GameMode.SPECTATOR)) {
            return;
        }

        if(player.isFlying()) {
            return;
        }

        if(player.getLocation().subtract(0, 1, 0).getBlock().getType().equals(Material.AIR)) {
            return;
        }

        player.setAllowFlight(true);
    }

    @EventHandler
    public void onJumpPad(@NotNull PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Block block = player.getLocation().getBlock();

        if(block.getType().equals(Material.AIR)) {
            return;
        }

        if(!block.getType().equals(Material.STONE_PRESSURE_PLATE)) {
            return;
        }

        if(!block.getLocation().subtract(0, 1,0).getBlock().getType().equals(Material.IRON_BLOCK)) {
            return;
        }

        Vector vector = player.getLocation().getDirection().multiply(3D).setY(1);
        player.setVelocity(vector);
        player.getWorld().playEffect(player.getLocation().add(0.0D, 0.0D, 0.0D), Effect.ENDER_SIGNAL, 1);
        player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_FLAP, 1.0f, 1.0f);
    }

    @EventHandler
    public void onBoots(@NotNull PlayerMoveEvent event) {
        Player player = event.getPlayer();
        ItemStack boots = player.getInventory().getBoots();

        if(boots == null) {
            return;
        }

        if(!boots.getType().equals(Material.LEATHER_BOOTS)) {
            return;
        }

        Location from = event.getFrom();
        Location to = event.getTo();

        if(from.getX() == to.getX() && from.getY() == to.getY() && from.getZ() == to.getZ()) {
            return;
        }

        if(player.isSwimming()) {
            return;
        }

        if(!boots.getType().equals(Material.LEATHER_BOOTS)) {
            return;
        }

        LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) boots.getItemMeta();
        switch (leatherArmorMeta.getColor().asRGB()) {
            case 16711680 -> {
                Particle.DustOptions dustOptions = new Particle.DustOptions(Color.fromRGB(255, 0, 0), 1f);
                player.spawnParticle(Particle.DUST, player.getLocation(), 20, dustOptions);
            }
            case 6513507 -> {
                Particle.DustOptions dustOptions = new Particle.DustOptions(Color.fromRGB(99, 99, 99), 1f);
                player.spawnParticle(Particle.DUST, player.getLocation(), 20, dustOptions);
            }
            case 1925459 -> {
                Particle.DustOptions dustOptions = new Particle.DustOptions(Color.fromRGB(29, 97, 83), 1f);
                player.spawnParticle(Particle.DUST, player.getLocation(), 20, dustOptions);
            }
        }
    }

    @EventHandler
    public void onNavigator(@NotNull PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack itemStack = event.getItem();

        if(event.getAction().isLeftClick()) {
            return;
        }

        if(itemStack == null) {
            return;
        }

        if(itemStack.getType().equals(Material.AIR)) {
            return;
        }

        if(!itemStack.getType().equals(Material.MUSIC_DISC_5)) {
            return;
        }

        if(!itemStack.hasItemMeta()) {
            return;
        }

        Inventory inventory = this.itemsManager.navigatorInventory(player);
        player.openInventory(inventory);
    }

    @EventHandler
    public void onNavigatorClick(@NotNull InventoryClickEvent event) {
        if(!(event.getWhoClicked() instanceof Player player)) {
            return;
        }

        if(event.getClickedInventory() == null) {
            return;
        }

        String title = LegacyComponentSerializer.legacySection().serialize(event.getView().title());
        String inventoryTitle = LegacyComponentSerializer.legacySection().serialize(GlobalTranslator.render
                (this.itemsManager.getNavigatorTitle(), player.locale()));
        if (!inventoryTitle.equals(title)) {
            return;
        }

        event.setCancelled(true);

        ItemStack itemStack = event.getCurrentItem();

        if(itemStack == null) {
            return;
        }

        if(itemStack.getType().equals(Material.AIR)) {
            return;
        }

        if(itemStack.getType().equals(Material.GRAY_STAINED_GLASS_PANE)) {
            return;
        }

        switch (itemStack.getType()) {
            case MAGMA_CREAM -> this.teleportPlayer(player, "Spawn");
            case WOODEN_AXE -> this.teleportPlayer(player, "GunGame");
            case RED_BED -> this.teleportPlayer(player, "Bedwars");
            case TNT -> this.teleportPlayer(player, "TNTRun");
            case STICK -> this.teleportPlayer(player, "TTT");
            case FISHING_ROD -> this.teleportPlayer(player, "KnockIT");
            case BLAZE_ROD -> this.teleportPlayer(player, "MLGRush");
            case SLIME_BALL -> this.teleportPlayer(player,  "Jump&Run");
            case COMMAND_BLOCK -> this.lobbySystem.getProxyManager().connect(player, this.lobbySystem.getConfiguration()
                    .getConfig().getString("Server" + "." + "DevServer"));
            case GRASS_BLOCK -> this.lobbySystem.getProxyManager().connect(player, this.lobbySystem.getConfiguration()
                    .getConfig().getString("Server" + "." + "BauServer"));
        }
    }

    private void teleportPlayer(@NotNull Player player, @NotNull String name) {
        if(!this.lobbySystem.getLocationManager().existLocation(name)) {
            player.sendMessage(this.lobbySystem.getPrefix()
                    .append(Component.translatable("lobby.inventory.navigator.invalid.location", NamedTextColor.GRAY)));
            player.playSound(player, Sound.BLOCK_ANVIL_PLACE, 1.0f, 1.0f);
            return;
        }

        Location location = this.lobbySystem.getLocationManager().getLocation(name);
        player.teleport(location);
        player.playEffect(player.getLocation(), Effect.ENDER_SIGNAL, 1);
        player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 2.0f, 1.0f);
        player.closeInventory();
    }

    @EventHandler
    public void onLobbySwitcher(@NotNull PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack itemStack = event.getItem();

        if(event.getAction().isLeftClick()) {
            return;
        }

        if(itemStack == null) {
            return;
        }

        if(itemStack.getType().equals(Material.AIR)) {
            return;
        }

        if(!itemStack.getType().equals(Material.NETHER_STAR)) {
            return;
        }

        if(!itemStack.hasItemMeta()) {
            return;
        }

        Inventory inventory = this.itemsManager.lobbySwitcherInventory(player);
        player.openInventory(inventory);
    }

    @EventHandler
    public void onLobbySwitcherClick(@NotNull InventoryClickEvent event) {
        if(!(event.getWhoClicked() instanceof Player player)) {
            return;
        }

        if(event.getClickedInventory() == null) {
            return;
        }

        String title = LegacyComponentSerializer.legacySection().serialize(event.getView().title());
        String inventoryTitle = LegacyComponentSerializer.legacySection().serialize(GlobalTranslator.render
                (this.itemsManager.getLobbySwitcherTitle(), player.locale()));
        if (!inventoryTitle.equals(title)) {
            return;
        }

        event.setCancelled(true);

        ItemStack itemStack = event.getCurrentItem();

        if(itemStack == null) {
            return;
        }

        if(itemStack.getType().equals(Material.AIR)) {
            return;
        }

        if(itemStack.getType().equals(Material.GRAY_STAINED_GLASS_PANE)) {
            return;
        }

        String displayName = itemStack.getItemMeta().getDisplayName();
        switch (itemStack.getType()) {
            case GLOWSTONE_DUST -> {
                if (displayName.equals("§c" + this.lobbySystem.getConfiguration().getConfig().getString("Server" + "." + "Lobby1")) ||
                        displayName.equals("§c" + this.lobbySystem.getConfiguration().getConfig().getString("Server" + "." + "Lobby2"))) {
                    this.lobbySystem.getProxyManager().connect(player, displayName.replace("§c", ""));
                }
            }
            case TNT ->  this.lobbySystem.getProxyManager().connect(player, this.lobbySystem.getConfiguration().getConfig()
                    .getString("Server" + "." + "SilentLobby1"));
            case GOLD_BLOCK ->  this.lobbySystem.getProxyManager().connect(player, this.lobbySystem.getConfiguration().getConfig()
                    .getString("Server" + "." + "PremiumLobby1#"));
        }
    }

    @EventHandler
    public void onHider(@NotNull PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack itemStack = event.getItem();

        if(event.getAction().isLeftClick()) {
            return;
        }

        if(itemStack == null) {
            return;
        }

        if(itemStack.getType().equals(Material.AIR)) {
            return;
        }

        if(!itemStack.getType().equals(Material.PINK_DYE)) {
            return;
        }

        if(!itemStack.hasItemMeta()) {
            return;
        }

        if(this.cooldownList.contains(player.getUniqueId())) {
            return;
        }

        this.cooldownList.add(player.getUniqueId());
        player.setCooldown(itemStack.getType(), 3 * 20);
        this.lobbySystem.getServer().getScheduler().runTaskLater(this.lobbySystem, () -> this.cooldownList.remove(player.getUniqueId()), 3 * 20L);
        this.lobbySystem.getLobbyManager().toggleHideMode(player);
    }

    @EventHandler
    public void onFlyFeather(@NotNull InventoryClickEvent event) {
        if(!(event.getWhoClicked() instanceof Player player)) {
            return;
        }

        if(event.getClickedInventory() == null) {
            return;
        }

        if(!event.getClickedInventory().getType().equals(InventoryType.PLAYER)) {
            return;
        }

        ItemStack itemStack = event.getCurrentItem();

        if(itemStack == null) {
            return;
        }

        if(itemStack.getType().equals(Material.AIR)) {
            return;
        }

        if(!itemStack.getType().equals(Material.FEATHER)) {
            return;
        }

        this.lobbySystem.getLobbyManager().toggleFlyMode(player);
        player.closeInventory();
    }

    @EventHandler
    public void onFish(@NotNull PlayerFishEvent event) {
        Player player = event.getPlayer();

        if(!event.getState().equals(PlayerFishEvent.State.IN_GROUND)) {
            return;
        }

        FishHook fishHook = event.getHook();
        Location hookLocation = fishHook.getLocation().getBlock().getLocation();
        Location locationBelowHook = hookLocation.clone().subtract(0, 1,0);
        Block blockBelowHook = locationBelowHook.getBlock();

        if(blockBelowHook.getType().equals(Material.AIR)) {
            return;
        }

        if(blockBelowHook.getType().equals(Material.WATER)) {
            return;
        }

        Location playerLocation = player.getLocation();

        double distance = playerLocation.distance(hookLocation);
        Vector vector = playerLocation.getDirection();
        vector.setY(0);
        player.setVelocity(vector.normalize().multiply(distance));

        player.playSound(playerLocation, Sound.ENTITY_BLAZE_HURT, 1.0f, 1.0f);
        player.playEffect(playerLocation, Effect.BOW_FIRE, 1);
    }

    @EventHandler
    public void onEnderpearl(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack itemStack = event.getItem();

        if(event.getAction().isLeftClick()) {
            return;
        }

        if(itemStack == null) {
            return;
        }

        if(itemStack.getType().equals(Material.AIR)) {
            return;
        }

        if(!itemStack.getType().equals(Material.ENDER_PEARL)) {
            return;
        }

        if(!itemStack.hasItemMeta()) {
            return;
        }

        this.lobbySystem.getServer().getScheduler().runTaskLater(
                this.lobbySystem, () -> this.getEnderpearlItem(player), 1L);
    }

    @EventHandler
    public void onProfile(@NotNull PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack itemStack = event.getItem();

        if(event.getAction().isLeftClick()) {
            return;
        }

        if(itemStack == null) {
            return;
        }

        if(itemStack.getType().equals(Material.AIR)) {
            return;
        }

        if(!itemStack.getType().equals(Material.PLAYER_HEAD)) {
            return;
        }

        if(!itemStack.hasItemMeta()) {
            return;
        }

        Inventory inventory = this.itemsManager.profileInventory(player);
        player.openInventory(inventory);
    }

    @EventHandler
    public void onProfileClick(@NotNull InventoryClickEvent event) {
        if(!(event.getWhoClicked() instanceof Player player)) {
            return;
        }

        if(event.getClickedInventory() == null) {
            return;
        }

        String title = LegacyComponentSerializer.legacySection().serialize(event.getView().title());
        String inventoryTitle = LegacyComponentSerializer.legacySection().serialize(GlobalTranslator.render
                (this.itemsManager.getProfileTitle(), player.locale()));

        if (!inventoryTitle.equals(title)) {
            return;
        }

        event.setCancelled(true);

        ItemStack itemStack = event.getCurrentItem();

        if(itemStack == null) {
            return;
        }

        if(itemStack.getType().equals(Material.AIR)) {
            return;
        }

        if(itemStack.getType().equals(Material.GRAY_STAINED_GLASS_PANE)) {
            return;
        }

        if(!itemStack.getType().equals(Material.ENDER_CHEST)) {
            return;
        }

        player.openInventory(this.lobbySystem.getItemsManager().extrasInventory(player));
    }

    @EventHandler
    public void onExtrasClick(@NotNull InventoryClickEvent event) {
        if(!(event.getWhoClicked() instanceof Player player)) {
            return;
        }

        if(event.getClickedInventory() == null) {
            return;
        }

        String title = LegacyComponentSerializer.legacySection().serialize(event.getView().title());
        String inventoryTitle = LegacyComponentSerializer.legacySection().serialize(GlobalTranslator.render
                (this.itemsManager.getExtrasTitle(), player.locale()));
        if (!inventoryTitle.startsWith(title)) {
            return;
        }
        event.setCancelled(true);

        ItemStack itemStack = event.getCurrentItem();

        if(itemStack == null) {
            return;
        }

        if(itemStack.getType().equals(Material.AIR)) {
            return;
        }

        switch (itemStack.getType()) {
            case ARROW -> player.openInventory(this.lobbySystem.getItemsManager().profileInventory(player));
            case DIAMOND_BOOTS -> player.openInventory(this.lobbySystem.getItemsManager().bootsInventory(player));
            case CARVED_PUMPKIN -> player.openInventory(this.lobbySystem.getItemsManager().headsInventory(player));
            case REDSTONE_BLOCK -> player.openInventory(this.lobbySystem.getItemsManager().gadgetsInventory(player));
            case BARRIER -> {
                player.closeInventory();
                player.getInventory().clear();
                this.lobbySystem.getLobbyManager().addLobbyItems(player);
            }
        }

        player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 2.0f, 1.0f);
    }

    @EventHandler
    public void onBootsClick(@NotNull InventoryClickEvent event) {
        if(!(event.getWhoClicked() instanceof Player player)) {
            return;
        }

        if(event.getClickedInventory() == null) {
            return;
        }

        String title = LegacyComponentSerializer.legacySection().serialize(event.getView().title());
        String inventoryTitle = LegacyComponentSerializer.legacySection().serialize(GlobalTranslator.render
                (this.itemsManager.getBootsTitle(), player.locale()));
        if (!inventoryTitle.equals(title)) {
            return;
        }

        event.setCancelled(true);

        ItemStack itemStack = event.getCurrentItem();

        if(itemStack == null) {
            return;
        }

        if(itemStack.getType().equals(Material.ARROW)) {
            player.openInventory(this.lobbySystem.getItemsManager().extrasInventory(player));
        }

        if(itemStack.getType().equals(Material.AIR)) {
            return;
        }

        if(!itemStack.getType().equals(Material.LEATHER_BOOTS)) {
            return;
        }

        player.closeInventory();
        switch (event.getSlot()) {
            case 12 -> player.getInventory().setBoots(new ItemBuilder(Material.LEATHER_BOOTS)
                    .setDisplayName(GlobalTranslator.render(Component.translatable(
                                    "lobby.inventory.profile.extras.boots.love-boots.displayname")
                            .decoration(TextDecoration.ITALIC, false), player.locale()))
                    .setColor(Color.fromRGB(255, 0, 0))
                    .addItemFlags(ItemFlag.values())
                    .build());
            case 13 -> player.getInventory().setBoots(new ItemBuilder(Material.LEATHER_BOOTS)
                    .setDisplayName(GlobalTranslator.render(Component.translatable(
                                    "lobby.inventory.profile.extras.boots.angry-boots.displayname")
                            .decoration(TextDecoration.ITALIC, false), player.locale()))
                    .setColor(Color.fromRGB(99, 99, 99))
                    .addItemFlags(ItemFlag.values())
                    .build());
            case 14 -> player.getInventory().setBoots(new ItemBuilder(Material.LEATHER_BOOTS)
                    .setDisplayName(GlobalTranslator.render(Component.translatable(
                                    "lobby.inventory.profile.extras.boots.water-boots.displayname")
                            .decoration(TextDecoration.ITALIC, false), player.locale()))
                    .setColor(Color.fromRGB(29, 97, 83))
                    .addItemFlags(ItemFlag.values())
                    .build());
        }
    }

    @EventHandler
    public void onHeadsClick(@NotNull InventoryClickEvent event) {
        if(!(event.getWhoClicked() instanceof Player player)) {
            return;
        }

        if(event.getClickedInventory() == null) {
            return;
        }

        String title = LegacyComponentSerializer.legacySection().serialize(event.getView().title());
        String inventoryTitle = LegacyComponentSerializer.legacySection().serialize(GlobalTranslator.render
                (this.itemsManager.getHeadsTitle(), player.locale()));
        if (!inventoryTitle.equals(title)) {
            return;
        }

        event.setCancelled(true);

        ItemStack itemStack = event.getCurrentItem();

        if(itemStack == null) {
            return;
        }

        if(itemStack.getType().equals(Material.ARROW)) {
            player.openInventory(this.lobbySystem.getItemsManager().extrasInventory(player));
        }

        if(itemStack.getType().equals(Material.AIR)) {
            return;
        }

        if(!itemStack.getType().equals(Material.PLAYER_HEAD)) {
            return;
        }

        player.closeInventory();
        SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();
        String playerName = skullMeta.getOwner();

        player.getInventory().setHelmet( new ItemBuilder(Material.PLAYER_HEAD)
                .setDisplayName(Component.text(playerName)
                        .decoration(TextDecoration.ITALIC, false))
                .setSkullOwner(playerName)
                .build());
    }

    @EventHandler
    public void onGadgetsClick(@NotNull InventoryClickEvent event) {
        if(!(event.getWhoClicked() instanceof Player player)) {
            return;
        }

        if(event.getClickedInventory() == null) {
            return;
        }

        String title = LegacyComponentSerializer.legacySection().serialize(event.getView().title());
        String inventoryTitle = LegacyComponentSerializer.legacySection().serialize(GlobalTranslator.render
                (this.itemsManager.getGadgetsTitle(), player.locale()));
        if (!inventoryTitle.equals(title)) {
            return;
        }

        event.setCancelled(true);

        ItemStack itemStack = event.getCurrentItem();

        if(itemStack == null) {
            return;
        }

        if(itemStack.getType().equals(Material.ARROW)) {
            player.openInventory(this.lobbySystem.getItemsManager().extrasInventory(player));
        }

        if(itemStack.getType().equals(Material.AIR)) {
            return;
        }

        if(!itemStack.getType().equals(Material.FISHING_ROD) && !itemStack.getType().equals(Material.ENDER_PEARL)) {
            return;
        }

        player.closeInventory();
        if (itemStack.getType().equals(Material.FISHING_ROD)) {
            player.getInventory().setItem(2, new ItemBuilder(Material.FISHING_ROD)
                    .setDisplayName(GlobalTranslator.render(this.lobbySystem.getItemsPrefix()
                            .append(Component.translatable("lobby.items.gadget.rod.displayname", NamedTextColor.GRAY))
                            .decoration(TextDecoration.ITALIC, false), player.locale()))
                    .setUnbreakable(true)
                    .addItemFlags(ItemFlag.HIDE_UNBREAKABLE)
                    .build());
        } else if (itemStack.getType().equals(Material.ENDER_PEARL)) {
            this.getEnderpearlItem(player);
        }
    }

    private void getEnderpearlItem(@NotNull Player player) {
        player.getInventory().setItem(2, new ItemBuilder(Material.ENDER_PEARL)
                .setDisplayName(GlobalTranslator.render(this.lobbySystem.getItemsPrefix()
                        .append(Component.translatable("lobby.items.gadget.enderpearl.displayname", NamedTextColor.GRAY))
                        .decoration(TextDecoration.ITALIC, false), player.locale()))
                .setUnbreakable(true)
                .addItemFlags(ItemFlag.HIDE_UNBREAKABLE)
                .build());
    }
}
