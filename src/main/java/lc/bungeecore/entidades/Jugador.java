package lc.bungeecore.entidades;

import lc.bungeecore.entidades.minijuegos.CHGInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Jugador {
    private static final Map<String, Jugador> jugadores = new HashMap<>();

    private final String nombre;
    private UUID uuid;
    private ProxiedPlayer player;
    private int coins;
    private RangoInfo rankInfo;
    private CHGInfo chgInfo;

    public static Jugador getJugador(String nombre){
        if(jugadores.containsKey(nombre)) return jugadores.get(nombre);
        Jugador j = new Jugador(nombre);
        jugadores.put(nombre, j);
        return j;
    }

    public void setChgInfo(CHGInfo chgInfo) {
        this.chgInfo = chgInfo;
    }

    public static Jugador getJugador(ProxiedPlayer player){
        return getJugador(player.getName());
    }

    public CHGInfo getChgInfo() {
        return chgInfo;
    }

    private Jugador(String nombre){
        this.nombre = nombre;
    }

    private Jugador(ProxiedPlayer player){
        setPlayer(player);
        this.nombre = player.getName();
        setUuid(player.getUniqueId());
    }

    public String getNombre() {
        return nombre;
    }

    public ProxiedPlayer getPlayer() {
        return player;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setPlayer(ProxiedPlayer player) {
        this.player = player;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public RangoInfo getRankInfo() {
        return rankInfo;
    }

    public void setRankInfo(RangoInfo rankInfo) {
        this.rankInfo = rankInfo;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public boolean noTieneRango(){
        return getRankInfo().getRango().equals(Rango.DEFAULT);
    }

    public boolean isVIP(){
        return getRankInfo().getRango().equals(Rango.VIP) || isSVIP();
    }
    public boolean isSVIP(){
        return getRankInfo().getRango().equals(Rango.SVIP) || isELITE();
    }

    public boolean isELITE(){
        return getRankInfo().getRango().equals(Rango.ELITE) || isRUBY();
    }
    public boolean isRUBY(){
        return getRankInfo().getRango().equals(Rango.RUBY) || isHelper();
    }

    public boolean isHelper(){
        return getRankInfo().getRango().equals(Rango.HELPER) || isModerador();
    }

    public boolean isModerador(){
        return getRankInfo().getRango().equals(Rango.MOD) || isAdmin();
    }

    public boolean isMiniYT(){
        return getRankInfo().getRango().equals(Rango.MINIYT);
    }

    public boolean isYoutuber(){
        return getRankInfo().getRango().equals(Rango.YOUTUBER);
    }

    public boolean isCreadorDeContenido(){
        Rango r = getRankInfo().getRango();
        return r == Rango.YOUTUBER || r == Rango.STREAMER || r == Rango.MINIYT;
    }

    public boolean isStreamer(){
        return getRankInfo().getRango().equals(Rango.STREAMER);
    }

    public boolean isAdmin(){
        return getRankInfo().getRango().equals(Rango.ADMIN) ||
                getRankInfo().getRango().equals(Rango.OWNER);
    }
}
