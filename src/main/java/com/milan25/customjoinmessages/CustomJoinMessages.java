package com.milan25.customjoinmessages;

import com.milan25.customjoinmessages.commands.CMCommand;
import com.milan25.customjoinmessages.events.CMEvents;
import org.bukkit.plugin.java.JavaPlugin;

public final class CustomJoinMessages extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        System.out.println("CustomMessages plugin ONLINE");
        this.getConfig().options().copyDefaults();
        this.saveDefaultConfig();
        this.getServer().getPluginManager().registerEvents(new CMEvents(this), this);
        this.getCommand("cm").setExecutor(new CMCommand(this));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
