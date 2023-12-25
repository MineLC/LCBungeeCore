package lc.bungeecore.entidades.database;

import lc.bungeecore.LCBungeeCore;
import lc.bungeecore.entidades.Jugador;
import lc.bungeecore.entidades.Rango;
import lc.bungeecore.entidades.RangoInfo;
import lc.bungeecore.utilidades.Util;
import net.md_5.bungee.api.ProxyServer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static lc.bungeecore.entidades.database.Database.close;
import static lc.bungeecore.entidades.database.Database.connection;

public class RankInfoQuery {

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
                            Rango.valueOf(resultSet.getString("Rank").toUpperCase()),
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

    public static void load_PlayerRankInfo(Jugador jugador) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            String queryBuilder = "SELECT * FROM `RangoInfo` WHERE `Player` = ?;";
            preparedStatement = connection.prepareStatement(queryBuilder);
            preparedStatement.setString(1, jugador.getNombre());
            resultSet = preparedStatement.executeQuery();

            if (resultSet != null && resultSet.next()) {
                jugador.setRankInfo(new RangoInfo(
                        Rango.valueOf(resultSet.getString("Rank").toUpperCase()),
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

    }


    public static void savePlayerRango(final Jugador jug) {
        RangoInfo info = jug.getRankInfo();
        ProxyServer.getInstance().getScheduler().runAsync(LCBungeeCore.get(), () -> {
            PreparedStatement preparedStatement = null;
            try {
                StringBuilder queryBuilder = new StringBuilder();
                queryBuilder.append("UPDATE `RangoInfo` SET ");
                queryBuilder.append("`Rank` = ?, `Puntos` = ?, `Duration` = ?, ");
                queryBuilder.append("`NameColor` = ?, `hideRank` = ? ");
                queryBuilder.append("WHERE `Player` = ?;");

                preparedStatement = connection.prepareStatement(queryBuilder.toString());
                preparedStatement.setString(1, info.getRango().name());
                preparedStatement.setInt(2, info.getPuntos());
                preparedStatement.setLong(3, info.getDuration());
                preparedStatement.setString(4, info.getNameColor());
                preparedStatement.setBoolean(5, info.isHideRank());
                preparedStatement.setString(6, jug.getNombre());
                preparedStatement.executeUpdate();
            } catch (final Exception Exception) {
                Exception.printStackTrace();
            } finally {
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

            jugador.setRankInfo(new RangoInfo(Rango.DEFAULT, 0, false, 0, "&7"));
            statement.executeUpdate();
        } catch (SQLException e) {
            Util.console("&c[Core] Excepcion creando PlayerRankInfo de "+jugador.getNombre()+".");
        }finally {
            close(statement);
        }
    }

}
