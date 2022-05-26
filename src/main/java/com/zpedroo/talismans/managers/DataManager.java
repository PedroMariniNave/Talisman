package com.zpedroo.talismans.managers;

import com.zpedroo.talismans.enums.Types;
import com.zpedroo.talismans.managers.cache.DataCache;
import com.zpedroo.talismans.mysql.DBConnection;
import com.zpedroo.talismans.objects.PlayerData;
import com.zpedroo.talismans.objects.Talisman;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;

public class DataManager {

    private static DataManager instance;
    public static DataManager getInstance() { return instance; }

    private final DataCache dataCache = new DataCache();

    public DataManager() {
        instance = this;
    }

    public PlayerData getPlayerData(Player player) {
        PlayerData data = dataCache.getPlayersData().get(player);
        if (data == null) {
            data = DBConnection.getInstance().getDBManager().getPlayerData(player);
            dataCache.getPlayersData().put(player, data);
        }

        return data;
    }

    public ItemStack getFragmentItem() {
        return dataCache.getFragmentItem();
    }

    public ItemStack getFragmentItem(int amount) {
        return dataCache.getFragmentItem(amount);
    }

    public Talisman getTalismanByType(Types types) {
        return dataCache.getTalismans().get(types);
    }

    public Talisman getTalismanByName(String name) {
        return Types.getByName(name) == null ? null : dataCache.getTalismans().get(Types.getByName(name));
    }

    public void savePlayerData(Player player) {
        PlayerData data = dataCache.getPlayersData().remove(player);
        if (data == null || !data.isQueueUpdate()) return;

        DBConnection.getInstance().getDBManager().savePlayerData(data);
        data.setUpdate(false);
    }

    public void saveAllPlayersData() {
        new HashSet<>(dataCache.getPlayersData().keySet()).forEach(this::savePlayerData);
    }

    public void cacheTalisman(Types types, Talisman talisman) {
        dataCache.getTalismans().put(types, talisman);
    }

    public DataCache getCache() {
        return dataCache;
    }
}