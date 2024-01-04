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
    private int vippoints;

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

    public void setVippoints(int vippoints) {
        this.vippoints = vippoints;
    }

    public int getVippoints() {
        return vippoints;
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

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public boolean noTieneRango(){
        return player.hasPermission("DEFAULT");
    }

    public boolean isVIP(){
        return player.hasPermission("VIP") || isSVIP();
    }
    public boolean isSVIP(){

        return player.hasPermission("SVIP") || isELITE();
    }

    public boolean isELITE(){

        return player.hasPermission("ELITE") || isRUBY();
    }
    public boolean isRUBY(){

        return player.hasPermission("RUBY") || isHelper();
    }

    public boolean isHelper(){

        return player.hasPermission("HELPER") || isModerador();
    }

    public boolean isModerador(){

        return player.hasPermission("MOD") || isAdmin();
    }

    public boolean isMiniYT(){

        return player.hasPermission("MINIYT");
    }

    public boolean isYoutuber(){

        return player.hasPermission("YOUTUBER");
    }

    public boolean isCreadorDeContenido(){

        return player.hasPermission("YOUTUBER") || player.hasPermission("STREAMER") || player.hasPermission("MINIYT");
    }

    public boolean isStreamer(){

        return player.hasPermission("STREAMER");
    }

    public boolean isAdmin(){

        return player.hasPermission("ADMIN") ||
                player.hasPermission("OWNER");
    }
}