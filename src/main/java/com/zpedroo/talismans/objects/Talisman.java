package com.zpedroo.talismans.objects;

import com.zpedroo.talismans.enums.Types;
import org.bukkit.inventory.ItemStack;

public class Talisman {

    private final Types type;
    private final ItemStack item;

    public Talisman(Types type, ItemStack item) {
        this.type = type;
        this.item = item;
    }

    public String getName() {
        return type.name();
    }

    public Types getType() {
        return type;
    }

    public ItemStack getItem() {
        return item.clone();
    }
}