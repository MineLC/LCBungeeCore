package lc.bungeecore;

import lc.bungeecore.commands.Whitelist;
import lc.bungeecore.configuration.LCConfig;
import lc.bungeecore.entidades.database.Database;
import lc.bungeecore.listener.PlayerListener;
import lc.bungeecore.utilidades.Util;
import net.md_5.bungee.api.plugin.Plugin;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.TimeUnit;

public final class LCBungeeCore extends Plugin {

    public static String prefix = Util.color("&b&lMine&6&lLC &r");
    public static List<String> whitelistPlayers;
    public static boolean whitelist;
    private static LCBungeeCore instance;
    public static LCConfig databaseConfig;
    public static LCConfig whitelistConfig;
    public static boolean selfCrashing = false;

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        databaseConfig = new LCConfig("database", this);
        whitelistConfig = new LCConfig("whitelist", this);
        Database.loadDatabaseConfig(databaseConfig.getConfig());

        try {
            Database.conectar();
        } catch (SQLException e) {
            Util.console("&c[BungeeCore] No se ha podido conectar con la base de datos");
            Util.console("&c[BungeeCore] &4Adios!");
            selfCrashing = true;
            getProxy().stop();

        }
        Util.console("&a[BungeeCore] ¡Conexión realizada con la base de datos!");
        checkearConexion();
        getProxy().getPluginManager().registerListener(this, new PlayerListener());
        getProxy().getPluginManager().registerCommand(this, new Whitelist());
    }

    @Override
    public void onDisable() {
        if(!selfCrashing){
            Util.console("&c[BungeeCore] &4Adios!");
        }
    }
    private void checkearConexion() {
        getProxy().getScheduler().schedule(this, Database::checkearConexion,  1800L, 1800L, TimeUnit.SECONDS);
    }

    public static LCBungeeCore get() {
        return instance;
    }
}
