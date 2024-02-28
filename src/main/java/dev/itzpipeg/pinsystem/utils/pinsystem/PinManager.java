package dev.itzpipeg.pinsystem.utils.pinsystem;

import dev.itzpipeg.pinsystem.Main;
import dev.itzpipeg.pinsystem.utils.CC;
import dev.itzpipeg.pinsystem.utils.pinsystem.tasks.PinDeleteCache;
import dev.itzpipeg.pinsystem.utils.pinsystem.tasks.PlayerKick;
import net.md_5.bungee.api.Title;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.scheduler.ScheduledTask;

import java.util.HashMap;
import java.util.Map;

public class PinManager {
    private Main plugin;
    public Map<String, Boolean> pinConfiguredPlayers;

    public Map<String, ScheduledTask> playerKickTask;

    public PinManager(Main plugin) {
        this.plugin = plugin;
        this.pinConfiguredPlayers = new HashMap<>();
    }


    public Boolean havePlayerPin(String player){
        if(plugin.getDataFile().contains(String.valueOf(player))){
            return true;
        }else{
            return false;
        }
    }

    public void createPlayerPin(String player, String pin){
        plugin.getDataFile().set(player + ".name", player);
        plugin.getDataFile().set(String.valueOf(player) + ".pin", pin);
        plugin.saveDataFile();
    }

    public void deletePlayerPin(String player){
        plugin.getDataFile().set(String.valueOf(player), null);
        plugin.saveDataFile();
        pinConfiguredPlayers.clear();

        if(plugin.getProxy().getPlayer(player) != null && plugin.getProxy().getPlayer(player).isConnected()){
            plugin.getProxy().getPlayer(player).disconnect(CC.translate(plugin.getLanguageFile().getString("DISCONNECT.PIN-CHANGED")));
        }
    }

    public void changePlayerPin(String player, String newPin){

        plugin.getDataFile().set(player + ".pin", newPin);
        plugin.saveDataFile();
        pinConfiguredPlayers.clear();

        if(plugin.getProxy().getPlayer(player) != null && plugin.getProxy().getPlayer(player).isConnected()){
            plugin.getProxy().getPlayer(player).disconnect(CC.translate(plugin.getLanguageFile().getString("DISCONNECT.PIN-CHANGED")));
        }
    }

    public String getPlayerPin(String player){
        return String.valueOf(plugin.getDataFile().get(player + ".pin"));
    }

    public void setPlayerLogged(ProxiedPlayer player) {
        String playerName = player.getName();
        pinConfiguredPlayers.put(playerName, true);

        new PinDeleteCache(this.plugin, player).startTimer();
    }

    public boolean isPlayerLogged(ProxiedPlayer player) {
        String playerName = player.getName();
        return pinConfiguredPlayers.containsKey(playerName);
    }

    public void clearPlayerCache(ProxiedPlayer player) {
        String playerName = player.getName();
        pinConfiguredPlayers.remove(playerName);
    }

    public void clearPlayersCache() {
        pinConfiguredPlayers.clear();
    }

    public void sendPlayerToAuth(ProxiedPlayer player){
        if(plugin.getConfig().getStringList("AUTH-SERVER.SERVER") != null){
            for (String serverName : plugin.getConfig().getStringList("AUTH-SERVER")) {
                if (plugin.getProxy().getServerInfo(serverName) != null) {
                    player.connect(plugin.getProxy().getServerInfo(serverName));
                    break;
                }else{
                    player.disconnect(CC.translate(plugin.getLanguageFile().getString("DISCONNECT.ERROR")));
                    plugin.getLogger().info(CC.translate("&r"));
                    plugin.getLogger().info(CC.translate(" &cERROR! Warning"));
                    plugin.getLogger().info(CC.translate("&r"));
                    plugin.getLogger().info(CC.translate("&cThe Auth server has not been found (check the config.yml)"));
                    plugin.getLogger().info(CC.translate("&r"));
                }
            }
        }else{
            player.disconnect(CC.translate(plugin.getLanguageFile().getString("DISCONNECT.ERROR")));
        }
    }
    public void sendPlayerToLobby(ProxiedPlayer player){
        if(plugin.getConfig().getStringList("LOBBY-SERVER.SERVER") != null){
            for (String serverName : plugin.getConfig().getStringList("LOBBY-SERVER")) {
                if (plugin.getProxy().getServerInfo(serverName) != null) {
                    player.connect(plugin.getProxy().getServerInfo(serverName));
                    break;
                }else{
                    player.disconnect(CC.translate(plugin.getLanguageFile().getString("DISCONNECT.ERROR")));
                    plugin.getLogger().info(CC.translate("&r"));
                    plugin.getLogger().info(CC.translate(" &cERROR! Warning"));
                    plugin.getLogger().info(CC.translate("&r"));
                    plugin.getLogger().info(CC.translate("&cThe Lobby server has not been found (check the config.yml)"));
                    plugin.getLogger().info(CC.translate("&r"));
                }
            }
        }else{
            player.disconnect(CC.translate(plugin.getLanguageFile().getString("DISCONNECT.ERROR")));
        }
    }

    public void createPlayerKickTask(ProxiedPlayer player){

        PlayerKick task = (PlayerKick) new PlayerKick(this.plugin, player).startTimer();

        playerKickTask.put(player.getName(), (ScheduledTask) task);
    }
}
