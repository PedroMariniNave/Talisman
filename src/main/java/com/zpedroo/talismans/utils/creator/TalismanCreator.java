package com.zpedroo.talismans.utils.creator;

import com.zpedroo.talismans.enums.Types;
import com.zpedroo.talismans.managers.DataManager;
import com.zpedroo.talismans.objects.Talisman;
import org.bukkit.inventory.ItemStack;

public abstract class TalismanCreator {

    private final Types types;
    private final ItemStack item;

    public TalismanCreator(Types type, ItemStack item) {
        this.types = type;
        this.item = item;
        this.cache();
    }

    public Types getType() {
        return types;
    }

    public ItemStack getItem() {
        return item;
    }

    private void cache() {
        DataManager.getInstance().cacheTalisman(types, new Talisman(types, item));
    }
}