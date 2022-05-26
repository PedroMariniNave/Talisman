package com.zpedroo.talismans.listeners;

import com.zpedroo.talismans.managers.DataManager;
import com.zpedroo.talismans.objects.PlayerData;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerGeneralListeners implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onQuit(PlayerQuitEvent event) {
        DataManager.getInstance().savePlayerData(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK && event.getAction() != Action.RIGHT_CLICK_AIR) return;
        if (event.getItem() == null || event.getItem().getType().equals(Material.AIR)) return;

        ItemStack item = event.getItem().clone();
        NBTItem nbt = new NBTItem(item);
        if (!nbt.hasKey("TalismanFragment")) return;

        event.setCancelled(true);

        Player player = event.getPlayer();
        PlayerData data = DataManager.getInstance().getPlayerData(player);
        if (data == null) return;

        int amount = player.isSneaking() ? item.getAmount() : 1;
        item.setAmount(amount);
        player.getInventory().removeItem(item);

        data.addFragments(amount);
        player.playSound(player.getLocation(), Sound.ITEM_PICKUP, 0.5f, 10f);
    }
}