package eu.soulsmc.lobbysystem;

import eu.soulsmc.lobbysystem.command.BuildCommand;
import eu.soulsmc.lobbysystem.command.FishCommand;
import eu.soulsmc.lobbysystem.command.LobbySystemCommand;
import eu.soulsmc.lobbysystem.listener.GameruleListeners;
import eu.soulsmc.lobbysystem.listener.LobbyListeners;
import eu.soulsmc.lobbysystem.manager.ItemsManager;
import eu.soulsmc.lobbysystem.manager.LobbyManager;
import eu.soulsmc.lobbysystem.manager.LocationManager;
import eu.soulsmc.lobbysystem.manager.ProxyManager;
import eu.soulsmc.lobbysystem.util.Config;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.translation.GlobalTranslator;
import net.kyori.adventure.translation.TranslationRegistry;
import net.kyori.adventure.util.UTF8ResourceBundleControl;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.ResourceBundle;

public class LobbySystem extends JavaPlugin {

    private LobbyManager lobbyManager;
    private ItemsManager itemsManager;
    private LocationManager locationManager;
    private ProxyManager proxyManager;
    private Config config;

    private LuckPerms luckPerms;

    @Override
    public void onLoad() {
        super.onLoad();
        this.config = new Config();
    }

    @Override
    public void onEnable() {
        super.onEnable();
        this.registerTranslation();

        this.lobbyManager = new LobbyManager(this);
        this.locationManager = new LocationManager(this);
        this.proxyManager = new ProxyManager(this);
        this.itemsManager = new ItemsManager(this);

        try {
            luckPerms = LuckPermsProvider.get();
            getLogger().info("LuckPermsAPI is working!");
        } catch (IllegalStateException e) {
            e.printStackTrace();
            getLogger().severe("Please install LuckPerms to get working Prefixes!");
        }

        this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", this.proxyManager);
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        getCommand("fish").setExecutor(new FishCommand(this));
        getCommand("build").setExecutor(new BuildCommand(this));
        getCommand("lobbysystem").setExecutor(new LobbySystemCommand(this));

        PluginManager pluginManager = this.getServer().getPluginManager();
        pluginManager.registerEvents(new GameruleListeners(this), this);
        pluginManager.registerEvents(new LobbyListeners(this), this);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        this.getServer().getMessenger().unregisterIncomingPluginChannel(this, "BungeeCord", this.proxyManager);
        this.getServer().getMessenger().unregisterOutgoingPluginChannel(this, "BungeeCord");
    }

    public Component getPrefix() {
        return Component.translatable("prefix.text")
                .decoration(TextDecoration.ITALIC, false);
    }

    public Component getItemsPrefix() {
        return Component.translatable("server.text").appendSpace()
                .append(Component.translatable("lobby.items.character")
                        .decoration(TextDecoration.ITALIC, false));
    }

    @NotNull
    public Config getConfiguration() {
        return config;
    }

    @NotNull
    public LobbyManager getLobbyManager() {
        return lobbyManager;
    }

    @NotNull
    public ProxyManager getProxyManager() {
        return proxyManager;
    }

    @NotNull
    public LocationManager getLocationManager() {
        return locationManager;
    }

    @NotNull
    public ItemsManager getItemsManager() {
        return itemsManager;
    }

    @NotNull
    public LuckPerms getLuckPerms() {
        return luckPerms;
    }

    private void registerTranslation() {
        TranslationRegistry registry = TranslationRegistry.create(Key.key("lobby:localization"));
        for (Locale locale : Locale.getAvailableLocales()) {
            try {
                ResourceBundle bundle = ResourceBundle.getBundle("lobby", locale, UTF8ResourceBundleControl.get());
                registry.registerAll(locale, bundle, false);
            } catch (Exception ignored) {
                // Ignored
            }
        }

        GlobalTranslator.translator().addSource(registry);
    }
}
