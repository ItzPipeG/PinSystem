package dev.itzpipeg.pinsystem.utils.pinsystem.commands;

import com.nickuc.login.api.nLoginAPI;
import dev.itzpipeg.pinsystem.Main;
import dev.itzpipeg.pinsystem.utils.CC;
import dev.itzpipeg.pinsystem.utils.pinsystem.PinManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.UUID;

public class PinSystemCommand extends Command {

    private Main plugin;

    private PinManager pinManager;

    public PinSystemCommand(Main plugin, PinManager pinManager){
        super("pinsystem");
        this.plugin = plugin;
        this.pinManager = pinManager;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(sender instanceof ProxiedPlayer){
            if (nLoginAPI.getApi().isRegistered(sender.getName())) {

                if(args.length == 0){

                    if(sender.hasPermission("pinsystem.admin")){
                        sender.sendMessage(CC.translate("&r"));
                        sender.sendMessage(CC.translate(" &b/pinsystem reload &7- &fReload configuration."));
                        sender.sendMessage(CC.translate(" &b/pinsystem version &7- &fPlugin version."));
                        sender.sendMessage(CC.translate("&r"));
                        sender.sendMessage(CC.translate(" &b/pinsystem change <player> <newPin> &7- &fChange player Pin."));
                        sender.sendMessage(CC.translate(" &b/pinsystem delete <player> &7- &fReset player pin."));
                        sender.sendMessage(CC.translate(" &b/pinsystem clearcache &7- &fClear player cache."));
                        sender.sendMessage(CC.translate("&r"));
                    }else{

                    }


                }else{
                    if(args.length > 0){
                        if(args[0].equals("reload")){
                            if(sender.hasPermission("pinsystem.admin")){
                                plugin.reloadConfig();
                                plugin.reloadLanguageFile();
                                sender.sendMessage(CC.translate(plugin.getLanguageFile().getString("RELOAD.SUCCESSFULLY")));
                            }
                        }

                        if(args[0].equals("clearcache")){
                            if(sender.hasPermission("pinsystem.admin")){
                                pinManager.clearPlayersCache();
                                sender.sendMessage(CC.translate(plugin.getLanguageFile().getString("CACHE-DELETED")));
                            }
                        }

                        if(args[0].equals("change")){
                            if(sender.hasPermission("pinsystem.admin")){
                                try{
                                    String player = args[1];
                                    String pin = args[2];

                                    pinManager.changePlayerPin(player, pin);

                                    for(String message : plugin.getLanguageFile().getStringList("PIN.CONSOLE-CHANGED")){
                                        sender.sendMessage(CC.translate(message
                                                .replaceAll("\\{pin}", pin)
                                                .replaceAll("\\{player}", player)));
                                    }
                                }catch (ArrayIndexOutOfBoundsException e){
                                    sender.sendMessage(CC.translate("/pin change <player> <pin>"));
                                }
                            }
                        }

                        if(args[0].equals("delete")){
                            if(sender.hasPermission("pinsystem.admin")){
                                try{
                                    String player = args[1];

                                    pinManager.deletePlayerPin(player);

                                    for(String message : plugin.getLanguageFile().getStringList("PIN.CONSOLE-DELETED")){
                                        sender.sendMessage(CC.translate(message
                                                .replaceAll("\\{player}", player)));
                                    }
                                }catch (ArrayIndexOutOfBoundsException e){
                                    sender.sendMessage(CC.translate("/pin delete <player>"));
                                }
                            }
                        }
                    }
                }
            }
        }else{

            if(args.length == 0){
                if(sender.hasPermission("pinsystem.admin")){
                    plugin.getLogger().info(CC.translate("&r"));
                    plugin.getLogger().info(CC.translate(" &b/pinsystem reload &7- &fReload configuration."));
                    plugin.getLogger().info(CC.translate(" &b/pinsystem version &7- &fPlugin version."));
                    plugin.getLogger().info(CC.translate("&r"));
                    plugin.getLogger().info(CC.translate(" &b/pinsystem change <player> <newPin> &7- &fChange player Pin."));
                    plugin.getLogger().info(CC.translate(" &b/pinsystem delete <player> &7- &fReset player pin."));
                    plugin.getLogger().info(CC.translate(" &b/pinsystem clearcache &7- &fClear player cache."));
                    plugin.getLogger().info(CC.translate("&r"));
                }else{

                }


            }else{
                if(args.length > 0){
                    if(args[0].equals("reload")){
                        plugin.reloadConfig();
                        plugin.reloadLanguageFile();
                        plugin.getLogger().info(CC.translate(plugin.getLanguageFile().getString("RELOAD.SUCCESSFULLY")));
                    }

                    if(args[0].equals("clearcache")){
                        pinManager.clearPlayersCache();
                        plugin.getLogger().info(CC.translate(plugin.getLanguageFile().getString("CACHE-DELETED")));
                    }

                    if(args[0].equals("change")){
                        try{
                            String player = args[1];
                            String pin = args[2];

                            pinManager.changePlayerPin(player, pin);

                            for(String message : plugin.getLanguageFile().getStringList("PIN.CONSOLE-CHANGED")){
                                plugin.getLogger().info(CC.translate(message
                                        .replaceAll("\\{pin}", pin)
                                        .replaceAll("\\{player}", player)));
                            }
                        }catch (ArrayIndexOutOfBoundsException e){
                            plugin.getLogger().info(CC.translate("/pin change <player> <pin>"));
                        }
                    }

                    if(args[0].equals("delete")){

                        try{
                            String player = args[1];

                            pinManager.deletePlayerPin(player);

                            for(String message : plugin.getLanguageFile().getStringList("PIN.CONSOLE-DELETED")){
                                plugin.getLogger().info(CC.translate(message
                                        .replaceAll("\\{player}", player)));
                            }
                        }catch (ArrayIndexOutOfBoundsException e){
                            plugin.getLogger().info(CC.translate("/pin delete <player>"));
                        }
                    }
                }
            }
        }
    }
}
