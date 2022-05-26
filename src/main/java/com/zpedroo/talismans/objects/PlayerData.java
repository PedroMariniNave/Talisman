package com.zpedroo.talismans.objects;

import java.util.List;
import java.util.UUID;

public class PlayerData {

    private final UUID uuid;
    private int fragmentsAmount;
    private Talisman equippedTalisman;
    private final List<Talisman> unlockedTalismans;
    private boolean update;

    public PlayerData(UUID uuid, int fragmentsAmount, Talisman equippedTalisman, List<Talisman> unlockedTalismans) {
        this.uuid = uuid;
        this.fragmentsAmount = fragmentsAmount;
        this.equippedTalisman = equippedTalisman;
        this.unlockedTalismans = unlockedTalismans;
        this.update = false;
    }

    public UUID getUniqueId() {
        return uuid;
    }

    public int getFragmentsAmount() {
        return fragmentsAmount;
    }

    public Talisman getEquippedTalisman() {
        return equippedTalisman;
    }

    public List<Talisman> getUnlockedTalismans() {
        return unlockedTalismans;
    }

    public boolean isQueueUpdate() {
        return update;
    }

    public boolean hasEquippedTalisman() {
        return equippedTalisman != null;
    }

    public boolean hasTalisman(Talisman talisman) {
        return unlockedTalismans.contains(talisman);
    }

    public boolean isEquippedTalisman(Talisman talisman) {
        return equippedTalisman != null && equippedTalisman.equals(talisman);
    }

    public void addFragments(int amount) {
        this.setFragmentsAmount(fragmentsAmount + amount);
    }

    public void removeFragments(int amount) {
        this.setFragmentsAmount(fragmentsAmount - amount);
    }

    public void setFragmentsAmount(int amount) {
        this.fragmentsAmount = amount;
        this.update = true;
    }

    public void addTalisman(Talisman talisman) {
        this.unlockedTalismans.add(talisman);
        this.update = true;
    }

    public void setEquippedTalisman(Talisman equippedTalisman) {
        this.equippedTalisman = equippedTalisman;
        this.update = true;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }
}