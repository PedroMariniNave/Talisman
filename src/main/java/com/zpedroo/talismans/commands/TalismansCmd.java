package com.zpedroo.talismans.commands;

import com.zpedroo.talismans.enums.Types;
import com.zpedroo.talismans.managers.DataManager;
import com.zpedroo.talismans.objects.Talisman;
import com.zpedroo.talismans.utils.config.Settings;
import com.zpedroo.talismans.utils.menu.Menus;
import com.zpedroo.voltzspawners.utils.formatter.NumberFormatter;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class TalismansCmd implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = sender instanceof Player ? (Player) sender : null;
        if (args.length > 0) {
            switch (args[0].toUpperCase()) {
                case "FRAGMENT":
                    if (args.length < 3 || !sender.hasPermission(Settings.ADMIN_PERMISSION)) break;

                    Player target = Bukkit.getPlayer(args[1]);
                    if (target == null) break;

                    int amount = getNumberByString(args[2]);
                    if (amount <= 0) break;

                    ItemStack item = DataManager.getInstance().getFragmentItem(amount);

                    if (target.getInventory().firstEmpty() != -1) {
                        target.getInventory().addItem(item);
                    } else {
                        target.getWorld().dropItemNaturally(target.getLocation(), item);
                    }
                    return true;
            }
        }

        Menus.getInstance().openMainMenu(player);
        return false;
    }

    private int getNumberByString(String str) {
        int amount = 0;
        try {
            amount = Integer.parseInt(str);
        } catch (NumberFormatException ex) {
            // ignore
        }
        return amount;
    }
}