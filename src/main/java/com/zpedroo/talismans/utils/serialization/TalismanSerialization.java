package com.zpedroo.talismans.utils.serialization;

import com.zpedroo.talismans.managers.DataManager;
import com.zpedroo.talismans.objects.Talisman;

import java.util.ArrayList;
import java.util.List;

public abstract class TalismanSerialization {

    public String serializeTalismans(List<Talisman> talismans) {
        StringBuilder builder = new StringBuilder();

        for (Talisman talisman : talismans) {
            builder.append(talisman.getName()).append(",");
        }

        return builder.toString();
    }

    public List<Talisman> deserializeTalismans(String serialized) {
        List<Talisman> ret = new ArrayList<>(4);
        String[] split = serialized.split(",");

        for (String talismanName : split) {
            Talisman talisman = DataManager.getInstance().getTalismanByName(talismanName);
            if (talisman != null) ret.add(talisman);
        }

        return ret;
    }
}