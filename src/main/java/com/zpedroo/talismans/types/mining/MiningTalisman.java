package com.zpedroo.talismans.types.mining;

import com.zpedroo.talismans.enums.Types;
import com.zpedroo.talismans.types.mining.listeners.MiningListeners;
import com.zpedroo.talismans.utils.FileUtils;
import com.zpedroo.talismans.utils.builder.ItemBuilder;
import com.zpedroo.talismans.utils.creator.TalismanCreator;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class MiningTalisman extends TalismanCreator {

    public MiningTalisman(Plugin plugin) {
        super(Types.MINING, ItemBuilder.build(FileUtils.get().getFile(FileUtils.Files.MINING).getFileConfiguration(), "Item").build());

        plugin.getServer().getPluginManager().registerEvents(new MiningListeners(), plugin);
    }

    public static class Settings {

        public static final int DIG_SPEED_LEVEL = FileUtils.get().getInt(FileUtils.Files.MINING, "Talisman-Settings.dig-speed-level");

        public static final List<String> COMPATIBLE_ITEMS = FileUtils.get().getStringList(FileUtils.Files.MINING, "Talisman-Settings.compatible-items");
    }
}