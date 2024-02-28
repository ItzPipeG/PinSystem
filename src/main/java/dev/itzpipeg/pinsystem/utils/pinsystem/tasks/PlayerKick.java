package dev.itzpipeg.pinsystem.utils.pinsystem.tasks;

import dev.itzpipeg.pinsystem.Main;
import dev.itzpipeg.pinsystem.utils.CC;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.scheduler.ScheduledTask;

import java.util.concurrent.TimeUnit;

public class PlayerKick {

    private final Main plugin;
    private final ProxiedPlayer player;
    private ScheduledTask task;

    public PlayerKick(Main plugin, ProxiedPlayer player) {
        this.plugin = plugin;
        this.player = player;
    }

    public ScheduledTask startTimer() {
        ProxyServer proxy = plugin.getProxy();

        task = proxy.getScheduler().schedule(plugin, new Runnable() {
            @Override
            public void run() {
                if(player.isConnected()){
                    player.disconnect(new TextComponent(CC.translate(plugin.getLanguageFile().getString("DISCONNECT.ERROR"))));
                    task.cancel();
                    plugin.pinManager.playerKickTask.remove(player);
                }else{
                    task.cancel();
                }
            }
        }, plugin.getConfig().getInt("KICK_PLAYER_TIME"), TimeUnit.SECONDS);
        return null;
    }

    public void cancelTimer() {
        if (task != null) {
            task.cancel();
        }
    }
}
