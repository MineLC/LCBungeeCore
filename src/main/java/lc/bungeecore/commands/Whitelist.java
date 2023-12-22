package lc.bungeecore.commands;

import lc.bungeecore.LCBungeeCore;
import lc.bungeecore.entidades.Jugador;
import lc.bungeecore.utilidades.Util;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.ArrayList;
import java.util.List;

public class Whitelist extends Command implements TabExecutor {
    public Whitelist() {
        super("whitelist", null, "wl");
    }

    @SuppressWarnings("deprecation")
    @Override
    public void execute(CommandSender sender, String[] args) {
        if(sender instanceof ProxiedPlayer && !Jugador.getJugador((ProxiedPlayer) sender).isAdmin()){
            sender.sendMessage(LCBungeeCore.get().getMessagebyString("message.nopermission"));
            return;
        }
        if(args.length == 0){
            Util.sendMessage(sender, LCBungeeCore.prefix+"&cUsa:");
            Util.sendMessage(sender, LCBungeeCore.prefix+"&c/wl add <jugador>");
            Util.sendMessage(sender, LCBungeeCore.prefix+"&c/wl remove <jugador>");
            Util.sendMessage(sender, LCBungeeCore.prefix+"&c/wl list");
            Util.sendMessage(sender, LCBungeeCore.prefix+"&c/wl on");
            Util.sendMessage(sender, LCBungeeCore.prefix+"&c/wl off");
            return;
        }
        if(args[0].equalsIgnoreCase("add")){
            if(args.length >= 2){
                ProxiedPlayer pp = ProxyServer.getInstance().getPlayer(args[1]);
                if(pp == null || !pp.isConnected()){
                    Util.sendMessage(sender, LCBungeeCore.prefix+"&cJugador no encontrado.");
                    return;
                }
                if(LCBungeeCore.whitelistPlayers.contains(pp.getName().toLowerCase())){
                    Util.sendMessage(sender, LCBungeeCore.prefix+"&cEse jugador ya esta en la Whitelist.");
                    return;
                }
                LCBungeeCore.whitelistPlayers.add(pp.getName().toLowerCase());
                LCBungeeCore.whitelistConfig.getConfig().set("whitelist.players", LCBungeeCore.whitelistPlayers);
                LCBungeeCore.whitelistConfig.saveConfig();
                Util.sendMessage(sender, LCBungeeCore.prefix+"&e"+pp.getName()+" &aha sido añadido a la Whitelist éxitosamente.");
            }else{
                Util.sendMessage(sender, LCBungeeCore.prefix+"&c/wl add <jugador>");
            }
            return;
        }
        if(args[0].equalsIgnoreCase("remove")){
            if(args.length >= 2){
                ProxiedPlayer pp = ProxyServer.getInstance().getPlayer(args[1]);
                if(pp == null || !pp.isConnected()){
                    Util.sendMessage(sender, LCBungeeCore.prefix+"&cJugador no encontrado.");
                    return;
                }
                if(!LCBungeeCore.whitelistPlayers.contains(pp.getName().toLowerCase())){
                    Util.sendMessage(sender, LCBungeeCore.prefix+"&cEse jugador no esta en la Whitelist.");
                    return;
                }
                LCBungeeCore.whitelistPlayers.remove(pp.getName().toLowerCase());
                LCBungeeCore.whitelistConfig.getConfig().set("whitelist.players", LCBungeeCore.whitelistPlayers);
                LCBungeeCore.whitelistConfig.saveConfig();
                Util.sendMessage(sender, LCBungeeCore.prefix+"&e"+pp.getName()+" &aha sido removido de la Whitelist éxitosamente.");
            }else{
                Util.sendMessage(sender, LCBungeeCore.prefix+"&c/wl remove <jugador>");
            }
            return;
        }
        if(args[0].equalsIgnoreCase("list")){
            Util.sendMessage(sender, LCBungeeCore.prefix+"&e&m===================");
            Util.sendMessage(sender, LCBungeeCore.prefix+"&aJugadores en Whitelist:");
            if(LCBungeeCore.whitelistPlayers.isEmpty()) Util.sendMessage(sender, LCBungeeCore.prefix+"&8 - &cNadie XD");
            else
                for(String player : LCBungeeCore.whitelistPlayers){
                    Util.sendMessage(sender, LCBungeeCore.prefix+"&8 - &c"+player);
                }
            Util.sendMessage(sender, LCBungeeCore.prefix+"&e&m===================");
            return;
        }
        if(args[0].equalsIgnoreCase("on")){
            if(LCBungeeCore.whitelist){
                Util.sendMessage(sender, LCBungeeCore.prefix+"&c¡La Whitelist ya esta activada!");
                return;
            }
            LCBungeeCore.whitelist = true;
            LCBungeeCore.whitelistConfig.getConfig().set("whitelist.enable", LCBungeeCore.whitelistPlayers);
            LCBungeeCore.whitelistConfig.saveConfig();
            Util.sendMessage(sender, LCBungeeCore.prefix+"&aLa Whitelist ha sido activada.");
            return;
        }
        if(args[0].equalsIgnoreCase("off")){
            if(!LCBungeeCore.whitelist){
                Util.sendMessage(sender, LCBungeeCore.prefix+"&c¡La Whitelist no esta activada aún!");
                return;
            }
            LCBungeeCore.whitelist = false;
            LCBungeeCore.whitelistConfig.getConfig().set("whitelist.enable", LCBungeeCore.whitelistPlayers);
            LCBungeeCore.whitelistConfig.saveConfig();
            Util.sendMessage(sender, LCBungeeCore.prefix+"&aLa Whitelist ha sido desactivada.");
        }
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        List<String> ret = new ArrayList<>();
        if(args.length == 1){
            ret.add("add");
            ret.add("remove");
            ret.add("list");
            ret.add("on");
            ret.add("off");
            return ret;
        }else if(args.length == 2){
            if(args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("remove")){
                for(ProxiedPlayer pp : ProxyServer.getInstance().getPlayers()){
                    ret.add(pp.getName());
                }
                return ret;
            }
        }
        return ret;
    }
}
