package com.milan25.customjoinmessages;

import com.milan25.customjoinmessages.commands.CMCommand;
import com.milan25.customjoinmessages.events.CMEvents;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import org.bukkit.plugin.java.JavaPlugin;

public final class CustomJoinMessages extends JavaPlugin {

    @Override
    public void onLoad() {
        CommandAPI.onLoad(new CommandAPIBukkitConfig(this));
        CommandAPI.registerCommand(CMCommand.class);
    }

    @Override
    public void onEnable() {
        CommandAPI.onEnable();

        this.getLogger().info("CustomMessages plugin loaded");

        this.getConfig().options().copyDefaults(true);
        this.saveDefaultConfig();

        this.getServer().getPluginManager().registerEvents(new CMEvents(this), this);
    }

    @Override
    public void onDisable() {
        CommandAPI.onDisable();
    }
}
