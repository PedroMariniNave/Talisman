package com.zpedroo.talismans;

import com.zpedroo.talismans.commands.TalismansCmd;
import com.zpedroo.talismans.listeners.PlayerGeneralListeners;
import com.zpedroo.talismans.listeners.WithdrawListeners;
import com.zpedroo.talismans.managers.DataManager;
import com.zpedroo.talismans.mysql.DBConnection;
import com.zpedroo.talismans.types.luck.LuckTalisman;
import com.zpedroo.talismans.types.mining.MiningTalisman;
import com.zpedroo.talismans.types.multiplier.MultiplierTalisman;
import com.zpedroo.talismans.utils.FileUtils;
import com.zpedroo.talismans.utils.menu.Menus;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;
import java.util.logging.Level;

import static com.zpedroo.talismans.utils.config.Settings.COMMAND;
import static com.zpedroo.talismans.utils.config.Settings.ALIASES;

public class Talismans extends JavaPlugin {

    private static Talismans instance;
    public static Talismans get() { return instance; }

    public void onEnable() {
        instance = this;
        new FileUtils(this);

        if (!isMySQLEnabled(getConfig())) {
            getLogger().log(Level.SEVERE, "MySQL are disabled! You need to enable it.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        new DBConnection(getConfig());
        new DataManager();
        new Menus();

        loadTalismans();
        registerListeners();
        registerCommand(COMMAND, ALIASES, new TalismansCmd());
    }

    public void onDisable() {
        if (!isMySQLEnabled(getConfig())) return;

        try {
            DataManager.getInstance().saveAllPlayersData();
            DBConnection.getInstance().closeConnection();
        } catch (Exception ex) {
            getLogger().log(Level.SEVERE, "An error occurred while trying to save data!");
            ex.printStackTrace();
        }
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new PlayerGeneralListeners(), this);
        getServer().getPluginManager().registerEvents(new WithdrawListeners(), this);
    }

    private void registerCommand(String command, List<String> aliases, CommandExecutor executor) {
        try {
            Constructor<PluginCommand> constructor = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
            constructor.setAccessible(true);

            PluginCommand pluginCmd = constructor.newInstance(command, this);
            pluginCmd.setAliases(aliases);
            pluginCmd.setExecutor(executor);

            Field field = Bukkit.getPluginManager().getClass().getDeclaredField("commandMap");
            field.setAccessible(true);
            CommandMap commandMap = (CommandMap) field.get(Bukkit.getPluginManager());
            commandMap.register(getName().toLowerCase(), pluginCmd);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void loadTalismans() {
        new LuckTalisman(this);
        new MultiplierTalisman(this);
        new MiningTalisman(this);
    }

    private boolean isMySQLEnabled(FileConfiguration file) {
        if (!file.contains("MySQL.enabled")) return false;

        return file.getBoolean("MySQL.enabled");
    }
}