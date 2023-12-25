package lc.bungeecore.entidades.database;

import lc.bungeecore.LCBungeeCore;
import lc.bungeecore.entidades.Jugador;
import lc.bungeecore.utilidades.Util;
import net.md_5.bungee.api.ProxyServer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static lc.bungeecore.entidades.database.Database.close;
import static lc.bungeecore.entidades.database.Database.connection;

public class LCoinsQuery {

    public static void savePlayerCoins_ASYNC(final Jugador jug) {
        ProxyServer.getInstance().getScheduler().runAsync(LCBungeeCore.get(), () -> {
            PreparedStatement preparedStatement = null;
            try {
                String queryBuilder = "UPDATE `LCoins` SET " +
                        "`LCoins` = ? " +
                        "WHERE `Player` = ?;";

                preparedStatement = connection.prepareStatement(queryBuilder);
                preparedStatement.setInt(1, jug.getCoins());
                preparedStatement.setString(2, jug.getNombre());
                preparedStatement.executeUpdate();
            } catch (final Exception Exception) {
                Exception.printStackTrace();
            } finally {
                close(preparedStatement);
            }
        });
    }

    public static void savePlayerCoins(final Jugador jug) {
        PreparedStatement preparedStatement = null;
        try {
            String queryBuilder = "UPDATE `LCoins` SET " +
                    "`LCoins` = ? " +
                    "WHERE `Player` = ?;";

            preparedStatement = connection.prepareStatement(queryBuilder);
            preparedStatement.setInt(1, jug.getCoins());
            preparedStatement.setString(2, jug.getNombre());
            preparedStatement.executeUpdate();
        } catch (final Exception Exception) {
            Exception.printStackTrace();
        } finally {
            close(preparedStatement);
        }
    }

    public static void load_PlayerCoins(Jugador jugador) {
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
            Util.console("&c[Core] Excepcion cargando PlayerCoins de "+jugador.getNombre()+".");
        } finally {
            close(resultSet);
            close(preparedStatement);
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
                Util.console("&c[Core] Excepcion cargando PlayerCoins de "+jugador.getNombre()+". (ASYNC)");
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
