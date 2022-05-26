package com.zpedroo.talismans.types.luck.listeners;

import com.zpedroo.talismans.enums.Types;
import com.zpedroo.talismans.managers.DataManager;
import com.zpedroo.talismans.objects.PlayerData;
import com.zpedroo.talismans.types.luck.LuckTalisman;
import com.zpedroo.voltzspawners.api.SpawnerBuyEvent;
import com.zpedroo.voltzspawners.utils.formatter.NumberFormatter;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.math.BigInteger;
import java.util.concurrent.ThreadLocalRandom;

import static com.zpedroo.talismans.types.luck.LuckTalisman.Settings.*;

public class SpawnerBuyListener implements Listener {

    @EventHandler
    public void onSpawnerBuy(SpawnerBuyEvent event) {
        Player player = event.getPlayer();
        PlayerData data = DataManager.getInstance().getPlayerData(player);
        if (data.getEquippedTalisman() == null || data.getEquippedTalisman().getType() != Types.LUCK) return;

        ThreadLocalRandom random = ThreadLocalRandom.current();
        double number = random.nextDouble(0, 100);
        if (number > CASHBACK_CHANCE) return;

        BigInteger price = event.getPrice();

        int cashbackPercentage = random.nextInt(0, MAX_CASHBACK);
        BigInteger cashback = price.multiply(BigInteger.valueOf(cashbackPercentage)).divide(BigInteger.valueOf(100L));
        if (cashback.signum() <= 0) return;

        event.setPrice(price.subtract(cashback));

        String[] placeholders = new String[]{
                "{amount}",
                "{percentage}"
        };
        String[] replacers = new String[]{
                NumberFormatter.getInstance().format(cashback),
                String.valueOf(cashbackPercentage)
        };

        sendMessagesAndTitle(player, placeholders, replacers);
    }

    private void sendMessagesAndTitle(Player player, String[] placeholders, String[] replacers) {
        if (IS_CASHBACK_MESSAGE_ENABLED) {
            player.sendMessage(replacePlaceholders(LuckTalisman.Messages.CASHBACK, placeholders, replacers));
        }

        if (IS_CASHBACK_TITLE_ENABLED) {
            player.sendTitle(
                    replacePlaceholders(LuckTalisman.Titles.CASHBACK[0], placeholders, replacers),
                    replacePlaceholders(LuckTalisman.Titles.CASHBACK[1], placeholders, replacers)
            );
        }
    }

    private String replacePlaceholders(String str, String[] placeholders, String[] replacers) {
        return StringUtils.replaceEach(str, placeholders, replacers);
    }
}