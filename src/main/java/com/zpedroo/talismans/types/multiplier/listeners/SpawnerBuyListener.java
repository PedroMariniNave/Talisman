package com.zpedroo.talismans.types.multiplier.listeners;

import com.zpedroo.talismans.enums.Types;
import com.zpedroo.talismans.managers.DataManager;
import com.zpedroo.talismans.objects.PlayerData;
import com.zpedroo.talismans.types.multiplier.MultiplierTalisman;
import com.zpedroo.voltzspawners.api.SpawnerBuyEvent;
import com.zpedroo.voltzspawners.utils.formatter.NumberFormatter;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.math.BigInteger;
import java.util.concurrent.ThreadLocalRandom;

import static com.zpedroo.talismans.types.multiplier.MultiplierTalisman.Settings.*;

public class SpawnerBuyListener implements Listener {

    @EventHandler
    public void onSpawnerBuy(SpawnerBuyEvent event) {
        Player player = event.getPlayer();
        PlayerData data = DataManager.getInstance().getPlayerData(player);
        if (data.getEquippedTalisman() == null || data.getEquippedTalisman().getType() != Types.MULTIPLIER) return;

        ThreadLocalRandom random = ThreadLocalRandom.current();
        double number = random.nextDouble(0, 100);
        if (number > MULTIPLIER_CHANCE) return;

        BigInteger buyAmount = event.getAmount();

        double multiplier = random.nextDouble(0, MAX_MULTIPLIER);
        double extraAmount = buyAmount.doubleValue() * multiplier - buyAmount.doubleValue();
        if (extraAmount <= 0) return;

        final BigInteger price = event.getPrice();
        BigInteger finalExtraAmount = new BigInteger(String.format("%.0f", extraAmount));
        BigInteger finalAmount = buyAmount.add(finalExtraAmount);

        event.setAmount(finalAmount);
        event.setPrice(price);

        String[] placeholders = new String[]{
                "{amount}",
                "{multiplier}"
        };
        String[] replacers = new String[]{
                NumberFormatter.getInstance().format(finalExtraAmount),
                String.format("%.1f", multiplier)
        };

        sendMessagesAndTitle(player, placeholders, replacers);
    }

    private void sendMessagesAndTitle(Player player, String[] placeholders, String[] replacers) {
        if (IS_MULTIPLIER_MESSAGE_ENABLED) {
            player.sendMessage(replacePlaceholders(MultiplierTalisman.Messages.MULTIPLIER, placeholders, replacers));
        }

        if (IS_MULTIPLIER_TITLE_ENABLED) {
            player.sendTitle(
                    replacePlaceholders(MultiplierTalisman.Titles.MULTIPLIER[0], placeholders, replacers),
                    replacePlaceholders(MultiplierTalisman.Titles.MULTIPLIER[1], placeholders, replacers)
            );
        }
    }

    private String replacePlaceholders(String str, String[] placeholders, String[] replacers) {
        return StringUtils.replaceEach(str, placeholders, replacers);
    }
}