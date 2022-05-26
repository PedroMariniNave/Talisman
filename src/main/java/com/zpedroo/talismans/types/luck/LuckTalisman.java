package com.zpedroo.talismans.types.luck;

import com.zpedroo.talismans.enums.Types;
import com.zpedroo.talismans.types.luck.listeners.SpawnerBuyListener;
import com.zpedroo.talismans.utils.FileUtils;
import com.zpedroo.talismans.utils.builder.ItemBuilder;
import com.zpedroo.talismans.utils.color.Colorize;
import com.zpedroo.talismans.utils.creator.TalismanCreator;
import org.bukkit.plugin.Plugin;

public class LuckTalisman extends TalismanCreator {

    public LuckTalisman(Plugin plugin) {
        super(Types.LUCK, ItemBuilder.build(FileUtils.get().getFile(FileUtils.Files.LUCK).getFileConfiguration(), "Item").build());

        plugin.getServer().getPluginManager().registerEvents(new SpawnerBuyListener(), plugin);
    }

    public static class Settings {

        public static final double CASHBACK_CHANCE = FileUtils.get().getDouble(FileUtils.Files.LUCK, "Talisman-Settings.cashback-chance");

        public static final int MAX_CASHBACK = FileUtils.get().getInt(FileUtils.Files.LUCK, "Talisman-Settings.max-cashback");

        public static final boolean IS_CASHBACK_MESSAGE_ENABLED = FileUtils.get().getBoolean(FileUtils.Files.LUCK, "Cashback-Message.enabled");

        public static final boolean IS_CASHBACK_TITLE_ENABLED = FileUtils.get().getBoolean(FileUtils.Files.LUCK, "Cashback-Title.enabled");
    }

    public static class Messages {

        public static final String CASHBACK = Colorize.getColored(FileUtils.get().getString(FileUtils.Files.LUCK, "Cashback-Message.message"));
    }

    public static class Titles {

        public static final String[] CASHBACK = new String[]{
                Colorize.getColored(FileUtils.get().getString(FileUtils.Files.LUCK, "Cashback-Title.title")),
                Colorize.getColored(FileUtils.get().getString(FileUtils.Files.LUCK, "Cashback-Title.subtitle"))
        };
    }
}