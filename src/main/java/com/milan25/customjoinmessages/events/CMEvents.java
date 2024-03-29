package com.milan25.customjoinmessages.events;

import com.milan25.customjoinmessages.CustomJoinMessages;
import com.milan25.customjoinmessages.utils.Colors;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class CMEvents implements Listener {
    private final CustomJoinMessages plugin;

    public CMEvents(CustomJoinMessages plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String playerJoinMessage = this.plugin.getConfig().getString("saved_messages.join." + player.getUniqueId(), "");

        if (playerJoinMessage.isEmpty()) {
            String defaultMessage = this.plugin.getConfig().getString("custom_join_message", "");

            if (!defaultMessage.isEmpty()) {
                playerJoinMessage = defaultMessage.replace("{NAME}", player.getName());
            }
        }

        String message = Colors.translateHexColorCodes("&#", "", playerJoinMessage);
        message = ChatColor.translateAlternateColorCodes('&', message);

        if (!message.isEmpty()) {
            event.setJoinMessage(message);
        }
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        String playerLeaveMessage = this.plugin.getConfig().getString("saved_messages.leave." + player.getUniqueId(), "");

        if (playerLeaveMessage.isEmpty()) {
            String defaultMessage = this.plugin.getConfig().getString("custom_leave_message", "");

            if (!defaultMessage.isEmpty()) {
                playerLeaveMessage = defaultMessage.replace("{NAME}", player.getName());
            }
        }

        String message = Colors.translateHexColorCodes("&#", "", playerLeaveMessage);
        message = ChatColor.translateAlternateColorCodes('&', message);

        if (!message.isEmpty()) {
            event.setQuitMessage(message);
        }
    }
}