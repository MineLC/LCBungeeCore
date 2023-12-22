package lc.bungeecore.entidades;

import lc.bungeecore.LCBungeeCore;
import lc.bungeecore.utilidades.Util;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.config.Configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.*;

public class Database {

    private static String ip;
    private static String puerto;
    private static String dbNombre;
    private static String usuario;
    private static String contrasenia;

    private static Connection connection = null;

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
        String update_rankInfo = "CREATE TABLE IF NOT EXISTS RangoInfo (`ID` INTEGER AUTO_INCREMENT UNIQUE, `Player` VARCHAR(24) UNIQUE, `Rank` VARCHAR(24), `Puntos` INTEGER, `Duration` BIGINT, `NameColor` VARCHAR(2), `hideRank` BOOLEAN, PRIMARY KEY (`ID`), KEY (`Player`))";
        String update_lobbyOpciones = "CREATE TABLE IF NOT EXISTS Opciones_SVS (`ID` INTEGER AUTO_INCREMENT UNIQUE, `Player` VARCHAR(24) UNIQUE, `opc_enablePlayers` BOOLEAN, `opc_enableChat` BOOLEAN, `opc_enableStacker` BOOLEAN, `opc_glassColor` INTEGER, `opc_Effect` VARCHAR(16), `opc_arrowEffect` VARCHAR(16), PRIMARY KEY (`ID`), KEY (`Player`))";
        String update_HG = "CREATE TABLE IF NOT EXISTS SV_HG (`ID` INTEGER AUTO_INCREMENT UNIQUE, `Player` VARCHAR(24) UNIQUE, `stats_kills` INTEGER, `stats_deaths` INTEGER, `stats_topkillstreak` INTEGER, `stats_level` INTEGER, `stats_partidas_jugadas` INTEGER, `stats_partidas_ganadas` INTEGER, `Kit` VARCHAR(16), `placecolor` VARCHAR(16), `traileffect` VARCHAR(16), PRIMARY KEY (`ID`), KEY (`Player`))";
        String update_KitPVP = "CREATE TABLE IF NOT EXISTS SV_KITPVP (`ID` INTEGER AUTO_INCREMENT UNIQUE, `Player` VARCHAR(24) UNIQUE, `stats_kills` INTEGER, `stats_deaths` INTEGER, `stats_topkillstreak` INTEGER, `stats_level` INTEGER, `Kit` VARCHAR(16), PRIMARY KEY (`ID`), KEY (`Player`))";
        String update_PotPVP = "CREATE TABLE IF NOT EXISTS SV_POTPVP (`ID` INTEGER AUTO_INCREMENT UNIQUE, `Player` VARCHAR(24) UNIQUE, `stats_kills` INTEGER, `stats_deaths` INTEGER, `stats_topkillstreak` INTEGER, `stats_level` INTEGER, `stats_partidas_jugadas` INTEGER, `stats_partidas_ganadas` INTEGER, `Kit` VARCHAR(16), PRIMARY KEY (`ID`), KEY (`Player`))";
        String update_SkyWars = "CREATE TABLE IF NOT EXISTS SV_SKYWARS (`ID` INTEGER AUTO_INCREMENT UNIQUE, `Player` VARCHAR(24) UNIQUE, `stats_kills` INTEGER, `stats_deaths` INTEGER, `stats_topkillstreak` INTEGER, `stats_level` INTEGER, `stats_partidas_jugadas` INTEGER, `stats_partidas_ganadas` INTEGER, `Kit` VARCHAR(16), `glasscolor` VARCHAR(16), `traileffect` VARCHAR(16), PRIMARY KEY (`ID`), KEY (`Player`))";
        String update_PVPGames = "CREATE TABLE IF NOT EXISTS SV_PVPGAMES (`ID` INTEGER AUTO_INCREMENT UNIQUE, `Player` VARCHAR(24) UNIQUE, `stats_kills` INTEGER, `stats_deaths` INTEGER, `stats_topkillstreak` INTEGER, `stats_level` INTEGER, `stats_partidas_jugadas` INTEGER, `stats_partidas_ganadas` INTEGER, `stats_monuments_destroyed` INTEGER, `stats_wools_placed` INTEGER, `stats_cores_leakeds` INTEGER, `Kit` VARCHAR(16), PRIMARY KEY (`ID`), KEY (`Player`))";
        String update_bedwars = "CREATE TABLE IF NOT EXISTS SV_BEDWARS (`ID` INTEGER AUTO_INCREMENT UNIQUE, `Player` VARCHAR(24) UNIQUE, `stats_kills` INTEGER, `stats_deaths` INTEGER, `stats_topkillstreak` INTEGER, `stats_level` INTEGER, `stats_partidas_jugadas` INTEGER, `stats_partidas_ganadas` INTEGER, `Kit` VARCHAR(16), `glasscolor` VARCHAR(16), `traileffect` VARCHAR(16), PRIMARY KEY (`ID`), KEY (`Player`))";
        String update_chg = "CREATE TABLE IF NOT EXISTS SV_CHG (`ID` INTEGER AUTO_INCREMENT UNIQUE, `Player` VARCHAR(24) UNIQUE, `stats_kills` INTEGER, `stats_deaths` INTEGER, `stats_topkillstreak` INTEGER, `stats_level` INTEGER, `stats_partidas_jugadas` INTEGER, `stats_partidas_ganadas` INTEGER, `Kit` VARCHAR(16), `placecolor` VARCHAR(16), `traileffect` VARCHAR(16), `winner` BOOLEAN, PRIMARY KEY (`ID`), KEY (`Player`))";

        Statement update = connection.createStatement();
        update.execute(update_info);
        update.execute(update_bedwars);
        update.execute(update_chg);
        update.execute(update_HG);
        update.execute(update_coins);
        update.execute(update_KitPVP);
        update.execute(update_lobbyOpciones);
        update.execute(update_PotPVP);
        update.execute(update_rankInfo);
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

    public static void load_PlayerRankInfo_ASYNC(Jugador jugador) {
        ProxyServer.getInstance().getScheduler().runAsync(LCBungeeCore.get(), () ->{
            PreparedStatement preparedStatement = null;
            ResultSet resultSet = null;
            try {
                String queryBuilder = "SELECT * FROM `RangoInfo` WHERE `Player` = ?;";
                preparedStatement = connection.prepareStatement(queryBuilder);
                preparedStatement.setString(1, jugador.getNombre());
                resultSet = preparedStatement.executeQuery();

                if (resultSet != null && resultSet.next()) {
                    jugador.setRankInfo(new RangoInfo(
                            Rango.valueOf(resultSet.getString("Rank")),
                                    resultSet.getLong("Duration"),
                                    resultSet.getBoolean("HideRank"),
                                    resultSet.getInt("Puntos"),
                                    resultSet.getString("NameColor")));
                }else{
                    createPlayerRankInfo(jugador);
                }
            } catch (SQLException Exception) {
                Util.console("&c[Core] Excepcion cargando PlayerRankInfo de "+jugador.getNombre()+". (ASYNC)");
            } finally {
                close(resultSet);
                close(preparedStatement);
            }


        });
    }

    private static void createPlayerRankInfo(Jugador jugador) {
        PreparedStatement statement = null;
        try {
            String queryBuilder = "INSERT INTO `RangoInfo` (`Player`, `Rank`, `Puntos`, `Duration`, `NameColor`, `HideRank`) VALUES (?, ?, ?, ?, ?, ?);";
            statement = connection.prepareStatement(queryBuilder);
            statement.setString(1, jugador.getNombre());
            statement.setString(2, Rango.DEFAULT.name().toUpperCase());
            statement.setInt(3, 0);
            statement.setLong(4, 0);
            statement.setString(5, "&7");
            statement.setBoolean(6, false);

            statement.executeUpdate();
        } catch (SQLException e) {
            Util.console("&c[Core] Excepcion creando PlayerRankInfo de "+jugador.getNombre()+".");
        }finally {
            close(statement);
        }
    }

    private static void close(ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (final Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static void close(PreparedStatement preparedStatement) {
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

    public static void load_PlayerCoins_ASYNC(Jugador jugador) {
        ProxyServer.getInstance().getScheduler().runAsync(LCBungeeCore.get(), () ->{
            PreparedStatement preparedStatement = null;
            ResultSet resultSet = null;
            try {
                String queryBuilder = "SELECT * FROM `LCoins` WHERE `Player` = ?;";
                preparedStatement = connection.prepareStatement(queryBuilder);
                preparedStatement.setString(1, jugador.getNombre());
                resultSet = preparedStatement.executeQuery();

                if (resultSet != null && resultSet.next()) {
                    jugador.setCoins(resultSet.getInt("LCoins"));
                }else{
                    createPlayerCoins(jugador);
                }
            } catch (SQLException Exception) {
                Util.console("&c[Core] Excepcion cargando PlayerRankInfo de "+jugador.getNombre()+". (ASYNC)");
            } finally {
                close(resultSet);
                close(preparedStatement);
            }


        });
    }

    private static void createPlayerCoins(Jugador jugador) {
        PreparedStatement statement = null;
        try {

            String queryBuilder = "INSERT INTO `LCoins` (`Player`, `LCoins`) VALUES (?, ?);";
            statement = connection.prepareStatement(queryBuilder);
            statement.setString(1, jugador.getNombre());
            statement.setInt(2, 100);

            statement.executeUpdate();
        } catch (SQLException e) {
            Util.console("&c[Core] Excepcion creando PlayerCoins de "+jugador.getNombre()+".");
        }finally {
            close(statement);
        }
    }
}
