package com.zpedroo.talismans.utils.config;

import com.zpedroo.talismans.utils.FileUtils;
import com.zpedroo.talismans.utils.color.Colorize;

import java.util.List;

public class Messages {

    public static final String INSUFFICIENT_FRAGMENTS = Colorize.getColored(FileUtils.get().getString(FileUtils.Files.CONFIG, "Messages.insufficient-fragments"));

    public static final String INVALID_AMOUNT = Colorize.getColored(FileUtils.get().getString(FileUtils.Files.CONFIG, "Messages.invalid-amount"));

    public static final List<String> CHOOSE_AMOUNT = Colorize.getColored(FileUtils.get().getStringList(FileUtils.Files.CONFIG, "Messages.choose-amount"));
}