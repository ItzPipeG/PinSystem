package dev.itzpipeg.pinsystem.utils.pinsystem.commands;

import com.nickuc.login.api.nLoginAPI;
import dev.itzpipeg.pinsystem.Main;
import dev.itzpipeg.pinsystem.utils.CC;
import dev.itzpipeg.pinsystem.utils.pinsystem.PinManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.Title;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.regex.Pattern;

public class PinCommand extends Command {

    private Main plugin;

    private PinManager pinManager;

    public PinCommand(Main plugin, PinManager pinManager){
        super("pin");
        this.plugin = plugin;
        this.pinManager = pinManager;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) sender;
            if (!nLoginAPI.getApi().isRegistered(player.getName())) {
                return;
            }

            if(args.length > 0){
                if(args[0].equals("create")){
                    try{
                        if(!(pinManager.isPlayerLogged(player))){

                            String pin = args[1];

                            String regex = "\\d+";
                            Pattern pattern = Pattern.compile(regex);

                            if (pattern.matcher(pin).matches()) {
                                if (pin.length() == 4) {
                                    if(!(pinManager.havePlayerPin(player.getName()))){

                                        pinManager.createPlayerPin(player.getName(), pin);

                                        pinManager.setPlayerLogged(player);

                                        if(plugin.pinManager.playerKickTask.get(player) != null){
                                            plugin.pinManager.playerKickTask.get(player).cancel();
                                        }

                                        sender.sendMessage(new TextComponent(CC.translate(plugin.getLanguageFile().getString("PIN.CREATED"))));

                                        player.disconnect(new TextComponent(CC.translate(plugin.getLanguageFile().getString("DISCONNECT.PIN-CREATED"))));

                                    }else{
                                        sender.sendMessage(new TextComponent(CC.translate(plugin.getLanguageFile().getString("PIN.ALREADY-CREATED"))));
                                    }
                                } else {
                                    sender.sendMessage(new TextComponent(CC.translate(plugin.getLanguageFile().getString("PIN.TOO-LONG"))));
                                }

                            } else {
                                sender.sendMessage(new TextComponent(CC.translate(plugin.getLanguageFile().getString("PIN.CONTAINS-LETTERS"))));
                            }

                        }else{
                            sender.sendMessage(new TextComponent(CC.translate(plugin.getLanguageFile().getString("PIN.ALREADY-LOGGED"))));
                        }
                    }catch (ArrayIndexOutOfBoundsException e){
                        plugin.getLogger().info(CC.translate("/pin create <player>"));
                    }
                }else{

                    try{
                        if(!(pinManager.isPlayerLogged(player))){
                            if(pinManager.havePlayerPin(player.getName())){
                                String pin = args[0];

                                String regex = "\\d+";
                                Pattern pattern = Pattern.compile(regex);

                                if (pattern.matcher(pin).matches()) {
                                    if (pin.length() == 4) {

                                        if(pinManager.getPlayerPin(player.getName()).equals(pin)){

                                            Title title = plugin.getProxy().createTitle();

                                            title.title(new TextComponent(CC.translate(plugin.getLanguageFile().getString("LOGGED.TITLE"))));

                                            title.subTitle(new TextComponent(CC.translate(plugin.getLanguageFile().getString("LOGGED.SUBTITLE"))));

                                            title.fadeIn(20);
                                            title.stay(10);
                                            title.fadeOut(20);

                                            ((ProxiedPlayer) sender).sendTitle(title);

                                            sender.sendMessage(new TextComponent(CC.translate(plugin.getLanguageFile().getString("PIN.LOGGED-IN"))));
                                            pinManager.setPlayerLogged(player);

                                            /*
                                            if(plugin.pinManager.playerKickTask != null){
                                                plugin.pinManager.playerKickTask.get(player).cancel();
                                            }

                                             */

                                            if(plugin.getConfig().getBoolean("AUTH_SERVER_TOGGLE")){
                                                pinManager.sendPlayerToLobby(player);
                                            }

                                        }else{
                                            player.disconnect(new TextComponent(CC.translate(plugin.getLanguageFile().getString("DISCONNECT.PIN-NOT-VALID"))));
                                        }
                                    } else {
                                        sender.sendMessage(new TextComponent(CC.translate(plugin.getLanguageFile().getString("PIN.TOO-LONG"))));
                                    }
                                } else {
                                    sender.sendMessage(new TextComponent(CC.translate(plugin.getLanguageFile().getString("PIN.CONTAINS-LETTERS"))));
                                }
                            }else{
                                sender.sendMessage(new TextComponent(CC.translate(plugin.getLanguageFile().getString("PIN.CREATE-USE"))));
                            }
                        }else{
                            sender.sendMessage(new TextComponent(CC.translate(plugin.getLanguageFile().getString("PIN.ALREADY-LOGGED"))));
                        }
                    }catch (ArrayIndexOutOfBoundsException e){
                        plugin.getLogger().info(CC.translate("/pin <pin>"));
                    }
                }
            }
        } else {
            sender.sendMessage(new TextComponent("Este comando solo puede ser ejecutado por jugadores."));
        }
    }
}
