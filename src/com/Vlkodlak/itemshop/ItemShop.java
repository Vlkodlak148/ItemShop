package com.Vlkodlak.itemshop;

import org.bukkit.plugin.java.JavaPlugin;
import java.util.logging.Logger;

public class ItemShop extends JavaPlugin {

    public final Logger logger = Logger.getLogger("Minecraft");

    @Override
    public void onDisable() {
        this.logger.info("[ItemShop] Plugin disabled.");
    }

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new SignListener(this), this);
        this.logger.info("[ItemShop] Plugin for 1.4.6 enabled! Created by Vlkodlak148");
    }
}