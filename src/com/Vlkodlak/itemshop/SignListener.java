package com.Vlkodlak.itemshop;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class SignListener implements Listener {

    public SignListener(ItemShop instance) {}

    private Material getMatFromLine(String line) {
        try {
            String[] parts = line.split(" ");
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < parts.length - 1; i++) {
                sb.append(parts[i]).append(i < parts.length - 2 ? "_" : "");
            }
            return Material.getMaterial(sb.toString().toUpperCase());
        } catch (Exception e) {
            return null;
        }
    }

    private int getAmountFromLine(String line) {
        try {
            String[] parts = line.split(" ");
            return Integer.parseInt(parts[parts.length - 1]);
        } catch (Exception e) {
            return 0;
        }
    }

    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        String header = event.getLine(0);
        if (header.equalsIgnoreCase("[Shop]") || header.equalsIgnoreCase("[AdminShop]")) {
            Player player = event.getPlayer();
            boolean isAdmin = header.equalsIgnoreCase("[AdminShop]");

            if (isAdmin && !player.hasPermission("itemshop.admin")) {
                player.sendMessage("§cYou don't have permission to create Admin Shops!");
                event.setCancelled(true);
                event.getBlock().breakNaturally();
                return;
            }

            if (!isAdmin && !player.hasPermission("itemshop.create")) {
                player.sendMessage("§cYou don't have permission to create a shop!");
                event.setCancelled(true);
                event.getBlock().breakNaturally();
                return;
            }

            try {
                String[] sellData = event.getLine(1).split(":");
                String[] buyData = event.getLine(2).split(":");

                Material sellMat = Material.getMaterial(Integer.parseInt(sellData[0]));
                Material buyMat = Material.getMaterial(Integer.parseInt(buyData[0]));
                int sellAmt = Integer.parseInt(sellData[1]);
                int buyAmt = Integer.parseInt(buyData[1]);

                if (sellMat == null || buyMat == null) throw new Exception();

                event.setLine(0, isAdmin ? "§4[AdminShop]" : "§1[Shop]");
                event.setLine(1, sellMat.name().replace("_", " ") + " " + sellAmt);
                event.setLine(2, buyMat.name().replace("_", " ") + " " + buyAmt);
                event.setLine(3, isAdmin ? "§8Server" : "§8" + player.getName());

                player.sendMessage("§aShop created successfully!");

            } catch (Exception e) {
                player.sendMessage("§cInvalid format! Use ID:Amount (e.g. 264:1)");
                event.setCancelled(true);
                event.getBlock().breakNaturally();
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block b = event.getBlock();
        if (b.getType() == Material.SIGN_POST || b.getType() == Material.WALL_SIGN) {
            Sign sign = (Sign) b.getState();
            String header = sign.getLine(0);
            
            if (header.contains("[Shop]") || header.contains("[AdminShop]")) {
                Player player = event.getPlayer();
                String owner = sign.getLine(3).replace("§8", "");

                if (!player.getName().equals(owner) && !player.hasPermission("itemshop.admin")) {
                    player.sendMessage("§cThis shop belongs to " + owner + "! You cannot destroy it.");
                    event.setCancelled(true);
                }
            }
        }
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Block b = event.getClickedBlock();
        if (b.getType() == Material.SIGN_POST || b.getType() == Material.WALL_SIGN) {
            Sign sign = (Sign) b.getState();
            String header = sign.getLine(0);

            if (header.contains("[Shop]") || header.contains("[AdminShop]")) {
                Player player = event.getPlayer();
                boolean isAdmin = header.contains("[AdminShop]");
                
                if (!player.hasPermission("itemshop.use")) {
                    player.sendMessage("§cYou don't have permission to use shops!");
                    return;
                }

                try {
                    Material sellMat = getMatFromLine(sign.getLine(1));
                    int sellAmt = getAmountFromLine(sign.getLine(1));
                    Material buyMat = getMatFromLine(sign.getLine(2));
                    int buyAmt = getAmountFromLine(sign.getLine(2));

                    Inventory pInv = player.getInventory();
                    ItemStack toSell = new ItemStack(sellMat, sellAmt);
                    ItemStack toPay = new ItemStack(buyMat, buyAmt);

                    if (!pInv.containsAtLeast(toPay, buyAmt)) {
                        player.sendMessage("§cYou cannot afford this!");
                        return;
                    }

                    if (!isAdmin) {
                        Block under = b.getRelative(0, -1, 0);
                        if (under.getType() != Material.CHEST) {
                            player.sendMessage("§cError: No chest found below!");
                            return;
                        }

                        Chest chest = (Chest) under.getState();
                        Inventory cInv = chest.getInventory();

                        if (!cInv.containsAtLeast(toSell, sellAmt)) {
                            player.sendMessage("§cOut of stock!");
                            return;
                        }

                        cInv.removeItem(toSell);
                        cInv.addItem(toPay);
                    }

                    pInv.removeItem(toPay);
                    pInv.addItem(toSell);

                    player.updateInventory();
                    player.sendMessage("§aTrade successful!");

                } catch (Exception e) {}
            }
        }
    }
}