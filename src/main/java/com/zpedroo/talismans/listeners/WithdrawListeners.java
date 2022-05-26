package com.zpedroo.talismans.listeners;

import com.zpedroo.talismans.managers.DataManager;
import com.zpedroo.talismans.objects.PlayerData;
import com.zpedroo.talismans.utils.config.Messages;
import com.zpedroo.voltzspawners.utils.formatter.NumberFormatter;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class WithdrawListeners implements Listener {

    private static final List<Player> withdrawingPlayers = new ArrayList<>(8);

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onChat(AsyncPlayerChatEvent event) {
        if (!withdrawingPlayers.contains(event.getPlayer())) return;

        event.setCancelled(true);

        Player player = event.getPlayer();
        PlayerData data = DataManager.getInstance().getPlayerData(player);
        withdrawingPlayers.remove(player);

        String message = event.getMessage();
        int fragmentsAmount = data.getFragmentsAmount();
        int amount = StringUtils.equals(message, "*") ? fragmentsAmount : NumberFormatter.getInstance().filter(message).intValue();
        if (amount <= 0) {
            player.sendMessage(Messages.INVALID_AMOUNT);
            return;
        }

        if (fragmentsAmount < amount) {
            player.sendMessage(StringUtils.replaceEach(Messages.INSUFFICIENT_FRAGMENTS, new String[]{
                    "{has}",
                    "{need}"
            }, new String[]{
                    NumberFormatter.getInstance().formatDecimal(fragmentsAmount),
                    NumberFormatter.getInstance().formatDecimal(amount)
            }));
            return;
        }

        data.removeFragments(amount);

        ItemStack item = DataManager.getInstance().getFragmentItem(amount);
        if (player.getInventory().firstEmpty() != -1) {
            player.getInventory().addItem(item);
        } else {
            player.getWorld().dropItemNaturally(player.getLocation(), item);
        }
    }

    public static List<Player> getWithdrawingPlayers() {
        return withdrawingPlayers;
    }
}