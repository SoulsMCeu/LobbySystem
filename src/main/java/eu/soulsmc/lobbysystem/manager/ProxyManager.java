package eu.soulsmc.lobbysystem.manager;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import eu.soulsmc.lobbysystem.LobbySystem;
import net.kyori.adventure.text.Component;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProxyManager implements PluginMessageListener {

    private final LobbySystem lobbySystem;
    private final Map<String, String> playerServer;
    private final Map<String, Boolean> serverStatus;
    private final List<String> serverList;

    public ProxyManager(@NotNull LobbySystem lobbySystem) {
        this.lobbySystem = lobbySystem;
        this.playerServer = new HashMap<>();
        this.serverStatus = new HashMap<>();
        this.serverList = new ArrayList<>();
    }

    @Override
    public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, byte @NotNull [] message) {
        if(!channel.equals("BungeeCord")) {
            return;
        }

        ByteArrayDataInput input = ByteStreams.newDataInput(message);
        String subChannel = input.readUTF();

        if(subChannel.equalsIgnoreCase("GetPlayerServer")) {
            String userName = input.readUTF();
            String serverName = input.readUTF();

            this.playerServer.put(userName, serverName);
        }

        if(subChannel.equalsIgnoreCase("PlayerCount")) {
            String serverName = input.readUTF();
            int playerCount = input.readInt();

            if(playerCount > 0) {
                this.serverStatus.put(serverName, true);
            }

            this.serverStatus.put(serverName, false);
        }

        if (subChannel.equals("GetServers")) {
            String[] servers = input.readUTF().split(", ");
            this.serverList.clear();
            this.serverList.addAll(Arrays.asList(servers));
        }
    }

    public void connect(@NotNull Player player, String server) {
        if(server == null) {
            return;
        }

        ByteArrayDataOutput byteArrayDataOutput = ByteStreams.newDataOutput();
        byteArrayDataOutput.writeUTF("Connect");
        byteArrayDataOutput.writeUTF(server);

        this.checkPlayerServer(player);
        this.lobbySystem.getServer().getScheduler().runTaskLater(this.lobbySystem, () -> {
            String serverName = this.getServer(player);
            if(server.equalsIgnoreCase(serverName)) {
                player.sendMessage(this.lobbySystem.getPrefix()
                        .append(Component.translatable("lobby.lobby-switcher.already-on-server")));
                player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 2.0f, 1.0f);
                player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 30, 30));
                player.closeInventory();
                return;
            }

            player.sendPluginMessage(this.lobbySystem, "BungeeCord", byteArrayDataOutput.toByteArray());
        }, 1L);
    }

    public void checkPlayerServer(@NotNull Player player) {
        ByteArrayDataOutput byteArrayDataOutput = ByteStreams.newDataOutput();
        byteArrayDataOutput.writeUTF("GetPlayerServer");
        byteArrayDataOutput.writeUTF(player.getName());
        player.sendPluginMessage(this.lobbySystem, "BungeeCord", byteArrayDataOutput.toByteArray());
    }

    private void checkServerStatus(@NotNull String server) {
        ByteArrayDataOutput byteArrayDataOutput = ByteStreams.newDataOutput();
        byteArrayDataOutput.writeUTF("PlayerCount");
        byteArrayDataOutput.writeUTF(server);
        this.lobbySystem.getServer().sendPluginMessage(this.lobbySystem, "BungeeCord", byteArrayDataOutput.toByteArray());
    }

    public String getServer(@NotNull Player player) {
        return this.playerServer.getOrDefault(player.getName(), "None");
    }

    public List<String> getServers() {
        ByteArrayDataOutput byteArrayDataOutput = ByteStreams.newDataOutput();
        byteArrayDataOutput.writeUTF("GetServers");
        this.lobbySystem.getServer().sendPluginMessage(this.lobbySystem, "BungeeCord", byteArrayDataOutput.toByteArray());
        return this.serverList;
    }

    public boolean isServerOnline(@NotNull String server) {
        this.checkServerStatus(server);
        return this.serverStatus.getOrDefault(server, false);
    }
}
