package com.zpedroo.talismans.types.multiplier;

import com.zpedroo.talismans.enums.Types;
import com.zpedroo.talismans.types.multiplier.listeners.SpawnerBuyListener;
import com.zpedroo.talismans.utils.FileUtils;
import com.zpedroo.talismans.utils.builder.ItemBuilder;
import com.zpedroo.talismans.utils.color.Colorize;
import com.zpedroo.talismans.utils.creator.TalismanCreator;
import org.bukkit.plugin.Plugin;

public class MultiplierTalisman extends TalismanCreator {

    public MultiplierTalisman(Plugin plugin) {
        super(Types.MULTIPLIER, ItemBuilder.build(FileUtils.get().getFile(FileUtils.Files.MULTIPLIER).getFileConfiguration(), "Item").build());

        plugin.getServer().getPluginManager().registerEvents(new SpawnerBuyListener(), plugin);
    }

    public static class Settings {

        public static final double MULTIPLIER_CHANCE = FileUtils.get().getDouble(FileUtils.Files.MULTIPLIER, "Talisman-Settings.multiplier-chance");

        public static final int MAX_MULTIPLIER = FileUtils.get().getInt(FileUtils.Files.MULTIPLIER, "Talisman-Settings.max-multiplier");

        public static final boolean IS_MULTIPLIER_MESSAGE_ENABLED = FileUtils.get().getBoolean(FileUtils.Files.MULTIPLIER, "Multiplier-Message.enabled");

        public static final boolean IS_MULTIPLIER_TITLE_ENABLED = FileUtils.get().getBoolean(FileUtils.Files.MULTIPLIER, "Multiplier-Title.enabled");
    }

    public static class Messages {

        public static final String MULTIPLIER = Colorize.getColored(FileUtils.get().getString(FileUtils.Files.MULTIPLIER, "Multiplier-Message.message"));
    }

    public static class Titles {

        public static final String[] MULTIPLIER = new String[]{
                Colorize.getColored(FileUtils.get().getString(FileUtils.Files.MULTIPLIER, "Multiplier-Title.title")),
                Colorize.getColored(FileUtils.get().getString(FileUtils.Files.MULTIPLIER, "Multiplier-Title.subtitle"))
        };
    }
}