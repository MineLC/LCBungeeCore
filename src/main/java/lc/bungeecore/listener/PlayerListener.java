package lc.bungeecore.listener;

import lc.bungeecore.LCBungeeCore;
import lc.bungeecore.entidades.Database;
import lc.bungeecore.entidades.Jugador;
import lc.bungeecore.utilidades.KickType;
import lc.bungeecore.utilidades.Util;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

@SuppressWarnings("deprecation")
public class PlayerListener implements Listener {

    @EventHandler
    public void onPreLogin(PreLoginEvent event){
        String name = event.getConnection().getName();
        if(LCBungeeCore.whitelist) {
            if (!LCBungeeCore.whitelistPlayers.contains(name.toLowerCase())) {
                event.setCancelled(true);
                event.setCancelReason(Util.getKickMessage(KickType.BY_SERVER, null, "&ePor ahora no..."));
                return;
            }
        }
        Jugador jugador = Jugador.getJugador(name);
        Database.load_PlayerRankInfo_ASYNC(jugador);
        Database.load_PlayerCoins_ASYNC(jugador);
    }

    @EventHandler
    public void onJoin(PostLoginEvent event){
        Jugador.getJugador(event.getPlayer().getName()).setBungeecordInstance(event.getPlayer());
    }

}
