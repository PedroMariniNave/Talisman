package com.zpedroo.talismans.utils.menu;

import com.zpedroo.talismans.listeners.WithdrawListeners;
import com.zpedroo.talismans.managers.DataManager;
import com.zpedroo.talismans.objects.PlayerData;
import com.zpedroo.talismans.objects.Talisman;
import com.zpedroo.talismans.utils.FileUtils;
import com.zpedroo.talismans.utils.builder.InventoryBuilder;
import com.zpedroo.talismans.utils.builder.InventoryUtils;
import com.zpedroo.talismans.utils.builder.ItemBuilder;
import com.zpedroo.talismans.utils.color.Colorize;
import com.zpedroo.talismans.utils.config.Messages;
import com.zpedroo.voltzspawners.utils.formatter.NumberFormatter;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Menus extends InventoryUtils {

    private static Menus instance;
    public static Menus getInstance() { return instance; }

    public Menus() {
        instance = this;
    }

    public void openMainMenu(Player player) {
        FileUtils.Files file = FileUtils.Files.MAIN;

        String title = Colorize.getColored(FileUtils.get().getString(file, "Inventory.title"));
        int size = FileUtils.get().getInt(file, "Inventory.size");

        InventoryBuilder inventory = new InventoryBuilder(title, size);
        PlayerData data = DataManager.getInstance().getPlayerData(player);

        for (String items : FileUtils.get().getSection(file, "Inventory.items")) {
            String action = FileUtils.get().getString(file, "Inventory.items." + items + ".action");
            String talismanName = FileUtils.get().getString(file, "Inventory.items." + items + ".talisman");
            int unlockPrice = FileUtils.get().getInt(file, "Inventory.items." + items + ".unlock-price");
            Talisman talisman = DataManager.getInstance().getTalismanByName(talismanName);
            String typeToGet = data.hasTalisman(talisman) ? (data.isEquippedTalisman(talisman) ? "enabled" : "disabled") : "locked";

            ItemStack item = null;
            String[] placeholders = new String[]{
                    "{player}",
                    "{fragments_amount}",
                    "{unlock_price}"
            };
            String[] replacers = new String[]{
                    player.getName(),
                    NumberFormatter.getInstance().formatDecimal(data.getFragmentsAmount()),
                    NumberFormatter.getInstance().formatDecimal(unlockPrice)
            };
            if (FileUtils.get().getFile(file).getFileConfiguration().contains("Inventory.items." + items + "." + typeToGet)) {
                item = ItemBuilder.build(
                        FileUtils.get().getFile(file).getFileConfiguration(), "Inventory.items." + items + "." + typeToGet, placeholders, replacers
                ).build();
            } else {
                item = ItemBuilder.build(
                        FileUtils.get().getFile(file).getFileConfiguration(), "Inventory.items." + items, placeholders, replacers
                ).build();
            }

            int slot = FileUtils.get().getInt(file, "Inventory.items." + items + ".slot");

            inventory.addItem(item, slot, () -> {
                if (talisman == null) {
                    switch (action.toUpperCase()) {
                        case "WITHDRAW":
                            inventory.close(player);

                            for (String msg : Messages.CHOOSE_AMOUNT) {
                                player.sendMessage(msg);
                            }

                            WithdrawListeners.getWithdrawingPlayers().add(player);
                            break;
                    }
                    return;
                }

                if (!data.hasTalisman(talisman)) {
                    unlockTalisman(player, data, unlockPrice, talisman);
                    return;
                }

                if (data.isEquippedTalisman(talisman)) {
                    data.setEquippedTalisman(null);
                } else {
                    data.setEquippedTalisman(talisman);
                }
                openMainMenu(player);
            }, InventoryUtils.ActionType.ALL_CLICKS);
        }

        inventory.open(player);
    }

    private void unlockTalisman(Player player, PlayerData data, int unlockPrice, Talisman talisman) {
        if (data.getFragmentsAmount() < unlockPrice) {
            player.sendMessage(StringUtils.replaceEach(Messages.INSUFFICIENT_FRAGMENTS, new String[]{
                    "{has}",
                    "{need}"
            }, new String[]{
                    NumberFormatter.getInstance().formatDecimal(data.getFragmentsAmount()),
                    NumberFormatter.getInstance().formatDecimal(unlockPrice)
            }));
            return;
        }

        data.removeFragments(unlockPrice);
        data.addTalisman(talisman);
        data.setEquippedTalisman(talisman);
        openMainMenu(player);
    }
}