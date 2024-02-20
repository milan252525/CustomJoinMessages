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
        if (playerJoinMessage != "") {
            String message = Colors.translateHexColorCodes("&#", "", playerJoinMessage);
            message = ChatColor.translateAlternateColorCodes('&', message);
            event.setJoinMessage(message);
        }

    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        String playerLeaveMessage = this.plugin.getConfig().getString("saved_messages.leave." + player.getUniqueId(), "");
        if (playerLeaveMessage != "") {
            String message = Colors.translateHexColorCodes("&#", "", playerLeaveMessage);
            message = ChatColor.translateAlternateColorCodes('&', message);
            event.setQuitMessage(message);
        }

    }
}