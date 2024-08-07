package eu.soulsmc.lobbysystem.scoreboard;

import eu.soulsmc.lobbysystem.LobbySystem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.translation.GlobalTranslator;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class LobbyScoreboard extends ScoreboardBuilder {

    public LobbyScoreboard(@NotNull LobbySystem lobbySystem, @NotNull Player player) {
        super(player, GlobalTranslator.render(Component.translatable(
                "server.text"), player.locale()), lobbySystem);
    }

    @Override
    public void createScoreboard() {

        this.lobbySystem.getServer().getScheduler().runTaskLater(this.lobbySystem, () -> {
            this.setScore(Component.text("§8§m                          §1"), 12);
            this.setScore(GlobalTranslator.render(Component.translatable(
                    "lobby.scoreboard.score.your.rank"), player.locale()),11);
            this.setScore(Component.text(" »", NamedTextColor.DARK_GRAY)
                    .appendSpace().append(Component.text(this.lobbySystem.getLobbyManager().getGroupColored(player))), 10);
            this.setScore(Component.text(ChatColor.DARK_GREEN.toString()), 9);
            this.setScore(GlobalTranslator.render(Component.translatable(
                    "lobby.scoreboard.score.lobby"), player.locale()),8);
            this.setScore(Component.text(" »", NamedTextColor.DARK_GRAY)
                    .appendSpace().append(Component.text(this.lobbySystem.getProxyManager()
                            .getServer(player), NamedTextColor.AQUA)), 7);
            this.setScore(Component.text(ChatColor.DARK_PURPLE.toString()), 6);
            this.setScore(GlobalTranslator.render(Component.translatable(
                    "lobby.scoreboard.score.discord"), player.locale()),5);
            this.setScore(Component.text(" »", NamedTextColor.DARK_GRAY).appendSpace()
                    .append(GlobalTranslator.render(Component.translatable(
                            "lobby.scoreboard.score.url.discord"), player.locale())), 4);
            this.setScore(Component.text(ChatColor.DARK_AQUA.toString()), 3);
            this.setScore(GlobalTranslator.render(Component.translatable(
                    "lobby.scoreboard.score.website"), player.locale()),2);
            this.setScore(Component.text(" »", NamedTextColor.DARK_GRAY).appendSpace()
                    .append(GlobalTranslator.render(Component.translatable(
                            "lobby.scoreboard.score.url.website"), player.locale())), 1);
            this.setScore(Component.text("§8§m                          §2"), 0);
        }, 1L);
    }

    @Override
    public void update() {
    }
}
