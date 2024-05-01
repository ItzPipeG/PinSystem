package dev.itzpipeg.pinsystem.utils.pinsystem.events;

import com.nickuc.login.api.event.bungee.auth.AuthenticateEvent;
import com.nickuc.login.api.event.bungee.auth.PremiumLoginEvent;
import com.nickuc.login.api.event.internal.bungee.BungeeCancellableEvent;
import com.nickuc.login.api.nLoginAPI;
import dev.itzpipeg.pinsystem.Main;
import dev.itzpipeg.pinsystem.utils.CC;
import dev.itzpipeg.pinsystem.utils.pinsystem.PinManager;
import dev.itzpipeg.pinsystem.utils.pinsystem.tasks.PlayerKick;
import jdk.internal.org.jline.utils.ShutdownHooks;
import net.md_5.bungee.api.Title;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ListenerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.scheduler.ScheduledTask;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

import java.util.concurrent.TimeUnit;

public class PostLogin implements Listener {
    private Main plugin;
    private PinManager pinManager;

    private static Title title;

    public PostLogin(Main plugin, PinManager pinManager){
        this.plugin = plugin;
        this.pinManager = pinManager;
    }


    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PostLoginEvent event){

        ProxiedPlayer player = (ProxiedPlayer) event.getPlayer();

        if(player.hasPermission("pinsystem.login")){
            if (!pinManager.havePlayerPin(player.getName())) {
                player.sendMessage("Debes registrarte usando /pin create [nuevoPIN] para acceder.");
            }

            if(plugin.getConfig().getBoolean("AUTH_SERVER_TOGGLE")){
                pinManager.sendPlayerToAuth(player);
            }
        }else{
            if(pinManager.isPlayerLogged(player)){
                pinManager.clearPlayerCache(player);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerLogin(AuthenticateEvent event) {

        Title title = plugin.getProxy().createTitle();
        if(event.getPlayer().hasPermission("pinsystem.login")){

            if(!(pinManager.isPlayerLogged(event.getPlayer()))){

                    if(pinManager.havePlayerPin(event.getPlayer().getName())){

                        title.title(new TextComponent(CC.translate(plugin.getLanguageFile().getString("TITLE.USE-PIN.TITLE"))));

                        title.subTitle(new TextComponent(CC.translate(plugin.getLanguageFile().getString("TITLE.USE-PIN.SUBTITLE"))));

                        title.fadeIn(20);
                        title.stay(1000);
                        title.fadeOut(20);

                        event.getPlayer().sendTitle(title);

                        //plugin.pinManager.createPlayerKickTask(event.getPlayer());


                    }else{

                        title.title(new TextComponent(CC.translate(plugin.getLanguageFile().getString("TITLE.PIN-CREATE.TITLE"))));

                        title.subTitle(new TextComponent(CC.translate(plugin.getLanguageFile().getString("TITLE.PIN-CREATE.SUBTITLE"))));

                        title.fadeIn(20);
                        title.stay(1000);
                        title.fadeOut(20);

                        event.getPlayer().sendTitle(title);

                        //plugin.pinManager.createPlayerKickTask(event.getPlayer());
                    }

            }else{

                if(plugin.getConfig().getBoolean("AUTH_SERVER_TOGGLE")){
                    pinManager.sendPlayerToLobby(event.getPlayer());
                }
            }
        }else{
            if(plugin.getConfig().getBoolean("AUTH_SERVER_TOGGLE")){
                pinManager.sendPlayerToLobby(event.getPlayer());
            }
        }

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerLogin(PremiumLoginEvent event) {

        Title title = plugin.getProxy().createTitle();
        if(event.getPlayer().hasPermission("pinsystem.login")){

            if(!(pinManager.isPlayerLogged(event.getPlayer()))){

                if(pinManager.havePlayerPin(event.getPlayer().getName())){

                    title.title(new TextComponent(CC.translate(plugin.getLanguageFile().getString("TITLE.USE-PIN.TITLE"))));

                    title.subTitle(new TextComponent(CC.translate(plugin.getLanguageFile().getString("TITLE.USE-PIN.SUBTITLE"))));

                    title.fadeIn(20);
                    title.stay(1000);
                    title.fadeOut(20);

                    event.getPlayer().sendTitle(title);

                    //plugin.pinManager.createPlayerKickTask(event.getPlayer());


                }else{

                    title.title(new TextComponent(CC.translate(plugin.getLanguageFile().getString("TITLE.PIN-CREATE.TITLE"))));

                    title.subTitle(new TextComponent(CC.translate(plugin.getLanguageFile().getString("TITLE.PIN-CREATE.SUBTITLE"))));

                    title.fadeIn(20);
                    title.stay(1000);
                    title.fadeOut(20);

                    event.getPlayer().sendTitle(title);

                    //plugin.pinManager.createPlayerKickTask(event.getPlayer());
                }

            }else{

                if(plugin.getConfig().getBoolean("AUTH_SERVER_TOGGLE")){
                    pinManager.sendPlayerToLobby(event.getPlayer());
                }
            }
        }else{
            if(plugin.getConfig().getBoolean("AUTH_SERVER_TOGGLE")){
                pinManager.sendPlayerToLobby(event.getPlayer());
            }
        }

    }



    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerChat(ChatEvent event) {
        ProxiedPlayer player = (ProxiedPlayer) event.getSender();

        String message = event.getMessage();

        if(player.hasPermission("pinsystem.login")){

            if(nLoginAPI.getApi().isPremium(player.getName())){
                if(!(pinManager.isPlayerLogged(player))){
                    if (!(message.contains("/pin"))) {
                        event.setCancelled(true);
                    }
                }
            }

            if(nLoginAPI.getApi().isAuthenticated(player.getName())){
                if(!(pinManager.isPlayerLogged(player))){
                    if (!(message.contains("/pin"))) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }
}
