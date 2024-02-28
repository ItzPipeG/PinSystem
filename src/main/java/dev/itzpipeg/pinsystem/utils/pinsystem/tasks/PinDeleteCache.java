package dev.itzpipeg.pinsystem.utils.pinsystem.tasks;

import dev.itzpipeg.pinsystem.Main;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.scheduler.ScheduledTask;

import java.util.concurrent.TimeUnit;

public class PinDeleteCache {

    private final Main plugin;
    private final ProxiedPlayer player;
    private ScheduledTask task;

    public PinDeleteCache(Main plugin, ProxiedPlayer player) {
        this.plugin = plugin;
        this.player = player;
    }

    public void startTimer() {
        ProxyServer proxy = plugin.getProxy();

        task = proxy.getScheduler().schedule(plugin, new Runnable() {
            @Override
            public void run() {
                plugin.pinManager.pinConfiguredPlayers.remove(player.getName());
                task.cancel();
            }
        }, plugin.getConfig().getInt("DELETE_PLAYER_CACHE_TIME"), TimeUnit.MINUTES);
    }

    public void cancelTimer() {
        if (task != null) {
            task.cancel();
        }
    }
}
