package com.milan25.customjoinmessages.commands;

import java.util.Arrays;

import com.milan25.customjoinmessages.CustomJoinMessages;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CMCommand implements CommandExecutor {
    private final CustomJoinMessages plugin;

    public CMCommand(CustomJoinMessages plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player)sender;
            if (args.length == 0) {
                if (!player.hasPermission("custommessages.admin") && !player.hasPermission("custommessages.set")) {
                    player.sendMessage("You don't have permission to use this command!");
                } else {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&A_____CustomMessages commands:_____"));
                    if (player.hasPermission("custommessages.set")) {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&A/cm set join/leave [message] &F- Set your join/leave message"));
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&A/cm reset &F- Reset your messages to default ones"));
                    }

                    if (player.hasPermission("custommessages.admin")) {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&A/cm show [player] &F- View messages of other players"));
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&A/cm reset [player] &F- Reset someone's messages to default"));
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&A/cm reload &F- Reload configuration"));
                    }

                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&A__________________________________"));
                }
                return true;
            } else if (args[0].equalsIgnoreCase("set")) {
                if (player.hasPermission("custommessages.set")) {
                    if (args.length == 1) {
                        player.sendMessage("Specify a message type - 'join' or 'leave'");
                        return true;
                    }

                    String message;
                    if (args[1].equalsIgnoreCase("join")) {
                        this.plugin.getConfig().createSection("saved_messages.join." + player.getUniqueId());
                        message = String.join(" ", Arrays.copyOfRange(args, 2, args.length));
                        this.plugin.getConfig().set("saved_messages.join." + player.getUniqueId(), message);
                        this.plugin.saveConfig();
                        player.sendMessage("Custom join message set!");
                    } else if (args[1].equalsIgnoreCase("leave")) {
                        this.plugin.getConfig().createSection("saved_messages.leave." + player.getUniqueId());
                        message = String.join(" ", Arrays.copyOfRange(args, 2, args.length));
                        this.plugin.getConfig().set("saved_messages.leave." + player.getUniqueId(), message);
                        this.plugin.saveConfig();
                        player.sendMessage("Custom leave message set!");
                    } else {
                        player.sendMessage("Usage: /cm set join/leave message");
                    }
                } else {
                    player.sendMessage("You don't have permission to use this command!");
                }

                return true;
            } else {
                Player target;
                if (args[0].equalsIgnoreCase("show")) {
                    if (player.hasPermission("custommessages.admin")) {
                        if (args.length == 1) {
                            player.sendMessage("You need to specify a player! \nUsage: /cm show player");
                            return true;
                        }

                        target = player.getServer().getPlayer(args[1]);
                        if (target == null) {
                            player.sendMessage("No player with this name found!");
                            return true;
                        }

                        String join_message = this.plugin.getConfig().getString("saved_messages.join." + target.getUniqueId(), "default join message");
                        String leave_message = this.plugin.getConfig().getString("saved_messages.leave." + target.getUniqueId(), "default leave message");
                        player.sendMessage("Custom messages set by " + target.getName() + ":\nJoin: " + join_message + "\nLeave: " + leave_message);
                    } else {
                        player.sendMessage("You don't have permission to use this command!");
                    }

                    return true;
                } else if (args[0].equalsIgnoreCase("reload")) {
                    if (player.hasPermission("custommessages.admin")) {
                        this.plugin.reloadConfig();
                        sender.sendMessage("Config reloaded!");
                    } else {
                        player.sendMessage("You don't have permission to use this command!");
                    }

                    return true;
                } else if (!args[0].equalsIgnoreCase("reset")) {
                    return false;
                } else {
                    if (!player.hasPermission("custommessages.admin") && !player.hasPermission("custommessages.set")) {
                        player.sendMessage("You don't have permission to use this command!");
                    } else {
                        if (!player.hasPermission("custommessages.admin")) {
                            target = player;
                        } else if (args.length == 2) {
                            target = player;
                        } else {
                            target = player.getServer().getPlayer(args[1]);
                            if (target == null) {
                                player.sendMessage("No player with this name found!");
                                return true;
                            }
                        }

                        this.plugin.getConfig().set("saved_messages.join." + target.getUniqueId(), "");
                        this.plugin.getConfig().set("saved_messages.leave." + target.getUniqueId(), "");
                        this.plugin.saveConfig();
                        sender.sendMessage("Done!");
                    }

                    return true;
                }
            }
        } else {
            sender.sendMessage("This command can only be used by players!");
            return true;
        }
    }
}
