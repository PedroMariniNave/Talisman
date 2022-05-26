package com.zpedroo.talismans.types.mining.listeners;

import com.zpedroo.talismans.Talismans;
import com.zpedroo.talismans.enums.Types;
import com.zpedroo.talismans.managers.DataManager;
import com.zpedroo.talismans.objects.PlayerData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import static com.zpedroo.talismans.types.mining.MiningTalisman.Settings.COMPATIBLE_ITEMS;
import static com.zpedroo.talismans.types.mining.MiningTalisman.Settings.DIG_SPEED_LEVEL;

public class MiningListeners implements Listener {

    @EventHandler
    public void onItemHeld(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        PlayerData data = DataManager.getInstance().getPlayerData(player);
        if (data.getEquippedTalisman() == null || data.getEquippedTalisman().getType() != Types.MINING) return;

        ItemStack item = player.getInventory().getItem(event.getNewSlot());
        if (item == null || !COMPATIBLE_ITEMS.contains(item.getType().toString())) {
            if (player.hasMetadata("MiningTalisman")) removeEffects(player);
            return;
        }

        giveEffects(player);
    }

    private void giveEffects(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 99999, DIG_SPEED_LEVEL - 1));
        player.setMetadata("MiningTalisman", new FixedMetadataValue(Talismans.get(), true));
    }

    private void removeEffects(Player player) {
        player.removePotionEffect(PotionEffectType.FAST_DIGGING);
        player.removeMetadata("MiningTalisman", Talismans.get());
    }
}