package com.milan25.customjoinmessages.commands;

import com.milan25.customjoinmessages.CustomJoinMessages;
import com.milan25.customjoinmessages.utils.Colors;
import dev.jorel.commandapi.annotations.Command;
import dev.jorel.commandapi.annotations.Default;
import dev.jorel.commandapi.annotations.Permission;
import dev.jorel.commandapi.annotations.Subcommand;
import dev.jorel.commandapi.annotations.arguments.AGreedyStringArgument;
import dev.jorel.commandapi.annotations.arguments.AMultiLiteralArgument;
import dev.jorel.commandapi.annotations.arguments.AOfflinePlayerArgument;
import jdk.jfr.Description;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

@Command("cm")
public class CMCommand {
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
            sb.append(ChatColor.translateAlternateColorCodes('&', "&A/cm set [player] join/leave [message] &F- Set join or leave message of another player.\n"));
            sb.append(ChatColor.translateAlternateColorCodes('&', "&A/cm show [player] &F- View custom messages of other players.\n"));
            sb.append(ChatColor.translateAlternateColorCodes('&', "&A/cm reset [player] &F- Reset player's messages to default.\n"));
            sb.append(ChatColor.translateAlternateColorCodes('&', "&A/cm reload &F- Reload configuration.\n"));
        }

        sb.append(ChatColor.translateAlternateColorCodes('&', "&A_________________________________\n"));

        player.sendMessage(sb.toString());
    }

    private static String prependDefaultColor(String str) {
        if (!str.contains("&")) {
            return "&e" + str;
        }
        return str;
    }

    private static void setPlayersMessage(Player source, OfflinePlayer target, String messageType, String message) {
        UUID playerId = target.getUniqueId();
        var plugin = CustomJoinMessages.getPlugin(CustomJoinMessages.class);

        if (messageType.equalsIgnoreCase("join")) {
            plugin.getConfig().createSection("saved_messages.join." + playerId);
            plugin.getConfig().set("saved_messages.join." + playerId, prependDefaultColor(message));
            plugin.saveConfig();
            source.sendMessage("Custom join message set to:\n" + prependDefaultColor(message));
        } else if (messageType.equalsIgnoreCase("leave")) {
            plugin.getConfig().createSection("saved_messages.leave." + playerId);
            plugin.getConfig().set("saved_messages.leave." + playerId, prependDefaultColor(message));
            plugin.saveConfig();
            source.sendMessage("Custom leave message set to:\n" + prependDefaultColor(message));
        } else {
            source.sendMessage("Usage: /cm set join/leave message");
        }
    }

    @Subcommand("set")
    @Description("Set your join or leave message, don't forget to include your name!")
    @Permission("custommessages.set")
    public static void cmSet(Player player, @AMultiLiteralArgument({"join", "leave"}) String messageType, @AGreedyStringArgument String message) {
        setPlayersMessage(player, player, messageType, message);
    }

    @Subcommand("set")
    @Description("Set join or leave message of another player.")
    @Permission("custommessages.admin")
    public static void cmSet(Player player, @AOfflinePlayerArgument OfflinePlayer targetPlayer, @AMultiLiteralArgument({"join", "leave"}) String messageType, @AGreedyStringArgument String message) {
        setPlayersMessage(player, targetPlayer, messageType, message);
    }

    private static void cmResetMessage(Player player, OfflinePlayer target) {
        UUID playerId = target.getUniqueId();
        var plugin = CustomJoinMessages.getPlugin(CustomJoinMessages.class);
        plugin.getConfig().set("saved_messages.join." + playerId, "");
        plugin.getConfig().set("saved_messages.leave." + playerId, "");
        plugin.saveConfig();
        player.sendMessage("Messages of " + target.getName() + " were reset to default.");
    }

    @Subcommand("reset")
    @Description("Reset another player's messages to default.")
    @Permission("custommessages.admin")
    public static void cmReset(Player player, @AOfflinePlayerArgument OfflinePlayer target) {
        cmResetMessage(player, target);
    }

    @Subcommand("reset")
    @Description("Reset your messages to default ones.")
    @Permission("custommessages.set")
    public static void cmResetSelf(Player player) {
        cmResetMessage(player, player);
    }

    private static void cmShowMessage(Player player, OfflinePlayer target) {
        if (target == null) {
            player.sendMessage("Player not found");
            return;
        }

        UUID playerId = target.getUniqueId();
        var plugin = CustomJoinMessages.getPlugin(CustomJoinMessages.class);

        String joinMessage = plugin.getConfig().getString("saved_messages.join." + playerId, "");
        if (joinMessage.isEmpty()) {
            joinMessage = "default join message";
        }
        String joinPreview = Colors.translateHexColorCodes("&#", "", joinMessage);
        joinPreview = ChatColor.translateAlternateColorCodes('&', joinPreview);

        String leaveMessage = plugin.getConfig().getString("saved_messages.leave." + playerId, "");
        if (leaveMessage.isEmpty()) {
            leaveMessage = "default leave message";
        }
        String leavePreview = Colors.translateHexColorCodes("&#", "", leaveMessage);
        leavePreview = ChatColor.translateAlternateColorCodes('&', leavePreview);

        player.sendMessage("Custom messages set by " + target.getName() + ":\n[Join] " + joinMessage + "\n[Join preview] " + joinPreview);
        player.sendMessage("[Leave] " + leaveMessage + "\n[Leave preview] " + leavePreview);
    }

    @Subcommand("show")
    @Description("View custom messages of other players.")
    @Permission("custommessages.admin")
    public static void cmShow(Player player, @AOfflinePlayerArgument OfflinePlayer target) {
        cmShowMessage(player, target);
    }

    @Subcommand("show")
    @Description("View your custom messages.")
    @Permission("custommessages.set")
    public static void cmShowSelf(Player player) {
        cmShowMessage(player, player);
    }

    @Subcommand("reload")
    @Description("Reload configuration.")
    @Permission("custommessages.admin")
    public static void cmReload(Player player) {
        CustomJoinMessages.getPlugin(CustomJoinMessages.class).reloadConfig();
        player.sendMessage("Config reloaded!");
    }
}
