package com.zpedroo.talismans.managers.cache;

import com.zpedroo.talismans.enums.Types;
import com.zpedroo.talismans.objects.PlayerData;
import com.zpedroo.talismans.objects.Talisman;
import com.zpedroo.talismans.utils.FileUtils;
import com.zpedroo.talismans.utils.builder.ItemBuilder;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class DataCache {

    private final Map<Player, PlayerData> playersData = new HashMap<>(32);
    private final Map<Types, Talisman> talismans = new HashMap<>(Types.values().length);
    private final ItemStack fragmentItem = getFragmentItemFromConfig();

    public Map<Player, PlayerData> getPlayersData() {
        return playersData;
    }

    public Map<Types, Talisman> getTalismans() {
        return talismans;
    }

    public ItemStack getFragmentItem() {
        NBTItem nbt = new NBTItem(fragmentItem.clone());
        nbt.addCompound("TalismanFragment");

        return nbt.getItem();
    }

    public ItemStack getFragmentItem(int amount) {
        ItemStack item = getFragmentItem();
        item.setAmount(amount);

        return item;
    }

    private ItemStack getFragmentItemFromConfig() {
        return ItemBuilder.build(FileUtils.get().getFile(FileUtils.Files.CONFIG).getFileConfiguration(), "Fragment-Item").build();
    }
}