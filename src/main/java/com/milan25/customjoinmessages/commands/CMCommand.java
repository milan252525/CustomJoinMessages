package com.milan25.customjoinmessages.commands;

import com.milan25.customjoinmessages.CustomJoinMessages;
import dev.jorel.commandapi.annotations.Command;
import dev.jorel.commandapi.annotations.Default;
import dev.jorel.commandapi.annotations.Permission;
import dev.jorel.commandapi.annotations.Subcommand;
import dev.jorel.commandapi.annotations.arguments.AGreedyStringArgument;
import dev.jorel.commandapi.annotations.arguments.AMultiLiteralArgument;
import dev.jorel.commandapi.annotations.arguments.AOfflinePlayerArgument;
import dev.jorel.commandapi.annotations.arguments.AStringArgument;
import jdk.jfr.Description;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

@Command("cm")
public class CMCommand {

    private final CustomJoinMessages plugin;

    public CMCommand(CustomJoinMessages plugin) {
        this.plugin = plugin;
    }

    @Default
    public static void cm(Player player) {
        StringBuilder sb = new StringBuilder();
        sb.append(ChatColor.translateAlternateColorCodes('&', "&A_____CustomMessages commands:_____\n"));
        if (player.hasPermission("custommessages.set")) {
            sb.append(ChatColor.translateAlternateColorCodes('&', "&A/cm set join/leave [message] &F- Set your join or leave message. Don't forget to include your name.\n"));
            sb.append(ChatColor.translateAlternateColorCodes('&', "&A/cm show &F- View your custom messages.\n"));
            sb.append(ChatColor.translateAlternateColorCodes('&', "&A/cm reset &F- Reset your messages to default ones.\n"));
        }

        if (player.hasPermission("custommessages.admin")) {
            sb.append(ChatColor.translateAlternateColorCodes('&', "&A/cm set [player] join/leave [message] &F- Set joi nor leave message of another player.\n"));
            sb.append(ChatColor.translateAlternateColorCodes('&', "&A/cm show [player] &F- View custom messages of other players.\n"));
            sb.append(ChatColor.translateAlternateColorCodes('&', "&A/cm reset [player] &F- Reset player's messages to default.\n"));
            sb.append(ChatColor.translateAlternateColorCodes('&', "&A/cm reload &F- Reload configuration.\n"));
        }

        sb.append(ChatColor.translateAlternateColorCodes('&', "&A__________________________________\n"));

        player.sendMessage(sb.toString());
    }

    private void setPlayersMessage(Player source, OfflinePlayer target, String messageType, String message) {
        UUID playerId = target.getUniqueId();
        if (messageType.equalsIgnoreCase("join")) {
            this.plugin.getConfig().createSection("saved_messages.join." + playerId);
            this.plugin.getConfig().set("saved_messages.join." + playerId, message);
            this.plugin.saveConfig();
            source.sendMessage("Custom join message set to:\n" + message);
        } else if (messageType.equalsIgnoreCase("leave")) {
            this.plugin.getConfig().createSection("saved_messages.leave." + playerId);
            this.plugin.getConfig().set("saved_messages.leave." + playerId, message);
            this.plugin.saveConfig();
            source.sendMessage("Custom leave message set to:\n" + message);
        } else {
            source.sendMessage("Usage: /cm set join/leave message");
        }
    }

    @Subcommand("set")
    @Description("Set your join or leave message, don't forget to include your name!")
    @Permission("custommessages.set")
    public void cmSet(Player player, @AMultiLiteralArgument({"join", "leave"}) String messageType, @AGreedyStringArgument String message) {
        setPlayersMessage(player, player, messageType, message);
    }

    @Subcommand("set")
    @Description("Set join or leave message of another player.")
    @Permission("custommessages.set")
    public void cmSet(Player player, @AOfflinePlayerArgument OfflinePlayer targetPlayer, @AMultiLiteralArgument({"join", "leave"}) String messageType, @AGreedyStringArgument String message) {
        setPlayersMessage(player, targetPlayer, messageType, message);
    }

    @Subcommand("reset")
    @Description("Reset another player's messages to default.")
    @Permission("custommessages.admin")
    public void cmReset(Player player, @AOfflinePlayerArgument OfflinePlayer target) {
        UUID playerId = target.getUniqueId();
        this.plugin.getConfig().set("saved_messages.join." + playerId, "");
        this.plugin.getConfig().set("saved_messages.leave." + playerId, "");
        this.plugin.saveConfig();
        player.sendMessage("Messages of " + target.getName() + " were reset to default.");
    }

    @Subcommand("reset")
    @Description("Reset your messages to default ones.")
    @Permission("custommessages.set")
    public void cmReset(Player player) {
        this.cmReset(player, player);
    }

    @Subcommand("show")
    @Description("View custom messages of other players.")
    @Permission("custommessages.admin")
    public void cmShow(Player player, @AOfflinePlayerArgument OfflinePlayer target) {
        if (target == null) {
            player.sendMessage("Player not found");
            return;
        }

        String join_message = this.plugin.getConfig().getString("saved_messages.join." + target.getUniqueId(), "default join message");
        String leave_message = this.plugin.getConfig().getString("saved_messages.leave." + target.getUniqueId(), "default leave message");
        player.sendMessage("Custom messages set by " + target.getName() + ":\nJoin: " + join_message + "\nLeave: " + leave_message);
    }

    @Subcommand("show")
    @Description("View your custom messages.")
    @Permission("custommessages.set")
    public void cmShow(Player player) {
        cmShow(player, player);
    }

    @Subcommand("reload")
    @Description("Reload configuration.")
    @Permission("custommessages.admin")
    public void cmReload(Player player) {
        this.plugin.reloadConfig();
        player.sendMessage("Config reloaded!");
    }
}
