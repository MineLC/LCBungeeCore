package lc.bungeecore.entidades.database;

import lc.bungeecore.utilidades.Util;
import net.md_5.bungee.config.Configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.*;
import java.util.LinkedHashMap;

public class Database {

    private static String ip;
    private static String puerto;
    private static String dbNombre;
    private static String usuario;
    private static String contrasenia;

    public static Connection connection = null;

    public static void loadDatabaseConfig(Configuration c){
        ip = c.getString("MySQL.Address");
        puerto = c.getString("MySQL.Puerto");
        dbNombre = c.getString("MySQL.Database");
        usuario = c.getString("MySQL.Usuario");
        contrasenia = c.getString("MySQL.Password");
    }

    public static void conectar() throws SQLException {
        Util.console("&a[Core] Conectando con la base de datos...");
        Util.console("&a[Core] Host: &e"+ip+"&a:&e"+puerto);
        try {
            InetAddress address = InetAddress.getByName(ip);
            ip = address.getHostAddress();
        } catch (UnknownHostException e) {
            Util.console("&c[Core] Host desconocido.");
        }
        connection = DriverManager.getConnection("jdbc:mysql://" + ip + ":" + puerto + "/" + dbNombre + "?autoReconnect=true", usuario, contrasenia);
        crearTablas();
    }

    private static void crearTablas() throws SQLException {
        String update_info = "CREATE TABLE IF NOT EXISTS PlayerInfo (`ID` INTEGER AUTO_INCREMENT UNIQUE, `Player` VARCHAR(24) UNIQUE, `FirstSeen` DATETIME, `LastSeen` DATETIME, `Premium` BOOLEAN, `PremiumVerify` BOOLEAN, `LastIP` VARCHAR(16), `Skin` VARCHAR(24), `TimePlayed` INTEGER, PRIMARY KEY (`ID`), KEY (`Player`))";
        String update_coins = "CREATE TABLE IF NOT EXISTS LCoins (`ID` INTEGER AUTO_INCREMENT UNIQUE, `Player` VARCHAR(24) UNIQUE, `LCoins` INTEGER, PRIMARY KEY (`ID`), KEY (`Player`))";
        String update_vippoints = "CREATE TABLE IF NOT EXISTS VipPoints (`ID` INTEGER AUTO_INCREMENT UNIQUE, `Player` VARCHAR(24) UNIQUE, `VipPoints` INTEGER, PRIMARY KEY (`ID`), KEY (`Player`))";
        String update_lobbyOpciones = "CREATE TABLE IF NOT EXISTS Opciones_SVS (`ID` INTEGER AUTO_INCREMENT UNIQUE, `Player` VARCHAR(24) UNIQUE, `opc_enablePlayers` BOOLEAN, `opc_enableChat` BOOLEAN, `opc_enableStacker` BOOLEAN, `opc_glassColor` INTEGER, `opc_Effect` VARCHAR(16), `opc_arrowEffect` VARCHAR(16), PRIMARY KEY (`ID`), KEY (`Player`))";
        String update_HG = "CREATE TABLE IF NOT EXISTS SV_HG (`ID` INTEGER AUTO_INCREMENT UNIQUE, `Player` VARCHAR(24) UNIQUE, `stats_kills` INTEGER, `stats_deaths` INTEGER, `stats_topkillstreak` INTEGER, `stats_level` INTEGER, `stats_partidas_jugadas` INTEGER, `stats_partidas_ganadas` INTEGER, `Kit` VARCHAR(16), `placecolor` VARCHAR(16), `traileffect` VARCHAR(16), PRIMARY KEY (`ID`), KEY (`Player`))";
        String update_KitPVP = "CREATE TABLE IF NOT EXISTS SV_KITPVP (`ID` INTEGER AUTO_INCREMENT UNIQUE, `Player` VARCHAR(24) UNIQUE, `stats_kills` INTEGER, `stats_deaths` INTEGER, `stats_topkillstreak` INTEGER, `stats_level` INTEGER, `Kit` VARCHAR(16), PRIMARY KEY (`ID`), KEY (`Player`))";
        String update_PotPVP = "CREATE TABLE IF NOT EXISTS SV_POTPVP (`ID` INTEGER AUTO_INCREMENT UNIQUE, `Player` VARCHAR(24) UNIQUE, `stats_kills` INTEGER, `stats_deaths` INTEGER, `stats_topkillstreak` INTEGER, `stats_level` INTEGER, `stats_partidas_jugadas` INTEGER, `stats_partidas_ganadas` INTEGER, `Kit` VARCHAR(16), PRIMARY KEY (`ID`), KEY (`Player`))";
        String update_SkyWars = "CREATE TABLE IF NOT EXISTS SV_SKYWARS (`ID` INTEGER AUTO_INCREMENT UNIQUE, `Player` VARCHAR(24) UNIQUE, `stats_kills` INTEGER, `stats_deaths` INTEGER, `stats_topkillstreak` INTEGER, `stats_level` INTEGER, `stats_partidas_jugadas` INTEGER, `stats_partidas_ganadas` INTEGER, `Kit` VARCHAR(16), `glasscolor` VARCHAR(16), `traileffect` VARCHAR(16), PRIMARY KEY (`ID`), KEY (`Player`))";
        String update_PVPGames = "CREATE TABLE IF NOT EXISTS SV_PVPGAMES (`ID` INTEGER AUTO_INCREMENT UNIQUE, `Player` VARCHAR(24) UNIQUE, `stats_kills` INTEGER, `stats_deaths` INTEGER, `stats_topkillstreak` INTEGER, `stats_level` INTEGER, `stats_partidas_jugadas` INTEGER, `stats_partidas_ganadas` INTEGER, `stats_monuments_destroyed` INTEGER, `stats_wools_placed` INTEGER, `stats_cores_leakeds` INTEGER, `Kit` VARCHAR(16), PRIMARY KEY (`ID`), KEY (`Player`))";
        String update_bedwars = "CREATE TABLE IF NOT EXISTS SV_BEDWARS (`ID` INTEGER AUTO_INCREMENT UNIQUE, `Player` VARCHAR(24) UNIQUE, `stats_kills` INTEGER, `stats_deaths` INTEGER, `stats_topkillstreak` INTEGER, `stats_level` INTEGER, `stats_partidas_jugadas` INTEGER, `stats_partidas_ganadas` INTEGER, `Kit` VARCHAR(16), `glasscolor` VARCHAR(16), `traileffect` VARCHAR(16), PRIMARY KEY (`ID`), KEY (`Player`))";
        String update_chg = "CREATE TABLE IF NOT EXISTS CHGInfo (`ID` INTEGER AUTO_INCREMENT UNIQUE, `Player` VARCHAR(24) UNIQUE, `kills` INTEGER, `racha` INTEGER, `jugadas` INTEGER, `ganadas` INTEGER, `fama` INTEGER, `kit` VARCHAR(16), `rango` VARCHAR(16), `winner` BOOLEAN, PRIMARY KEY (`ID`), KEY (`Player`))";

        Statement update = connection.createStatement();
        update.execute(update_info);
        update.execute(update_bedwars);
        update.execute(update_chg);
        update.execute(update_HG);
        update.execute(update_coins);
        update.execute(update_KitPVP);
        update.execute(update_lobbyOpciones);
        update.execute(update_PotPVP);
        update.execute(update_vippoints);
        update.execute(update_SkyWars);
        update.execute(update_PVPGames);
        update.close();
    }


    public static void reconnect() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://" + ip + ":" + puerto + "/" + dbNombre + "?autoReconnect=true", usuario, contrasenia);
        } catch (Exception e) {
            Util.console("&c[Core] No se ha podido re-conectar con la base de datos, reintentando...");

        }
    }

    public static void checkearConexion() {
        try {
            if (connection == null || connection.isClosed()) {
                reconnect();
            }
        } catch (Exception e) {
            reconnect();
        }
    }


    public static void close(ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (final Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void close(PreparedStatement preparedStatement) {
        if (preparedStatement != null) {
            try {
                preparedStatement.close();
            } catch (Exception e) {
                checkearConexion();
            }
        } else {
            checkearConexion();
        }
    }



    public static LinkedHashMap<String, Integer> getTop(int limit, String toptype, String table) {
        LinkedHashMap<String, Integer> topScore = new LinkedHashMap<String, Integer>();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            StringBuilder queryBuilder = new StringBuilder();
            queryBuilder.append("SELECT ");
            queryBuilder.append("`Player`, "+toptype+" ");
            queryBuilder.append("FROM ");
            queryBuilder.append("`"+table+"` ");
            queryBuilder.append("ORDER BY "+toptype+" DESC LIMIT ?;");

            preparedStatement = connection.prepareStatement(queryBuilder.toString());
            preparedStatement.setInt(1, limit);
            resultSet = preparedStatement.executeQuery();

            while (resultSet != null && resultSet.next()) {
                topScore.put(resultSet.getString("Player"), resultSet.getInt(toptype));
            }
        } catch (final Exception Exception) {
            Exception.printStackTrace();
        } finally {
            close(resultSet);
            close(preparedStatement);
        }

        return topScore;
    }
}
