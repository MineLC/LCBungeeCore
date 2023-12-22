package lc.bungeecore.utilidades;

import lc.bungeecore.entidades.Jugador;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

@SuppressWarnings("deprecation")
public class Util {
    public static String color(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static void sendMessage(CommandSender sender, String message){
        sender.sendMessage(color(message));
    }

    public static void console(String message){
        sendMessage(ProxyServer.getInstance().getConsole(), message);
    }

    public static String setPlaceholder(Jugador jugador, String string){
        return string.replaceAll("%prefix%", jugador.getRankInfo()
                .getRango().getAsPrefix()).replaceAll("%name%", jugador.getNombre());
    }

    public static void kick(ProxiedPlayer player, KickType kickType, CommandSender kicker, String... lines){
        player.disconnect(getKickMessage(kickType, kicker, lines));
    }

    public static String getKickMessage(KickType kickType, CommandSender kicker, String... lines){
        StringBuilder kickString = new StringBuilder();
        kickString.append("&b&lMINE&6&lLC").append("\n");
        switch (kickType){
            case BY_DEV: kickString.append("\n")
                    .append("&r&cHas sido expulsado por el desarrollador:")
                    .append("\n").append("&r&e").append(kicker.getName());
            case BY_MOD: kickString.append("\n")
                    .append("&r&cHas sido expulsado por el moderador:")
                    .append("\n").append("&r&b").append(kicker.getName());
            case BY_ADMIN: kickString.append("\n")
                    .append("&r&cHas sido expulsado por el administrador:")
                    .append("\n").append("&r&d").append(kicker.getName());
            case BY_SERVER: kickString.append("\n")
                    .append("&r&cHas sido expulsado por el servidor:");
        }
        kickString.append("\n\n");
        for(String line : lines) kickString.append("&r").append(line).append("\n");
        return color(kickString.toString());
    }
}
