package lc.bungeecore.entidades.database;


import lc.bungeecore.LCBungeeCore;
import lc.bungeecore.entidades.Jugador;
import lc.bungeecore.entidades.minijuegos.CHGInfo;
import lc.bungeecore.entidades.minijuegos.CHGRank;
import lc.bungeecore.utilidades.Util;
import net.md_5.bungee.api.ProxyServer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static lc.bungeecore.entidades.database.Database.close;
import static lc.bungeecore.entidades.database.Database.connection;

public class CHGInfoQuery {

    public static void load_PlayerCHGInfo_ASYNC(Jugador jugador) {
        ProxyServer.getInstance().getScheduler().runAsync(LCBungeeCore.get(), () ->{
            PreparedStatement preparedStatement = null;
            ResultSet resultSet = null;
            try {
                String queryBuilder = "SELECT * FROM `CHGInfo` WHERE `Player` = ?;";
                preparedStatement = connection.prepareStatement(queryBuilder);
                preparedStatement.setString(1, jugador.getNombre());
                resultSet = preparedStatement.executeQuery();

                if (resultSet != null && resultSet.next()) {
                    jugador.setChgInfo(new CHGInfo(
                            CHGRank.valueOf(resultSet.getString("rango")),
                            resultSet.getInt("kills"),
                            resultSet.getInt("ganadas"),
                            resultSet.getInt("jugadas"),
                            resultSet.getBoolean("winner"),
                            resultSet.getString("kit"),
                            resultSet.getInt("racha"),
                            resultSet.getInt("fama")
                    ));
                }else{
                    createPlayerCHGInfo(jugador);
                }
            } catch (SQLException Exception) {
                Util.console("&c[Core] Excepcion cargando PlayerCHGInfo de "+jugador.getNombre()+". (ASYNC)");
            } finally {
                close(resultSet);
                close(preparedStatement);
            }


        });
    }


    public static void saveCHGInfo(final Jugador jug) {
        CHGInfo info = jug.getChgInfo();
        ProxyServer.getInstance().getScheduler().runAsync(LCBungeeCore.get(), () -> {
            PreparedStatement preparedStatement = null;
            try {
                String queryBuilder = "UPDATE `CHGInfo` SET " +
                        "`kills` = ?, `ganadas` = ?, `racha` = ?, " +
                        "`rango` = ?, `jugadas` = ?, `kit` = ?, `fama` = ?, `winner` = ?" +
                        "WHERE `Player` = ?;";

                preparedStatement = connection.prepareStatement(queryBuilder);
                preparedStatement.setInt(1, info.getKills());
                preparedStatement.setInt(2, info.getWins());
                preparedStatement.setInt(3, info.getRacha());
                preparedStatement.setString(4, info.getRank().name());
                preparedStatement.setInt(5, info.getPlayeds());
                preparedStatement.setString(6, info.getKit());
                preparedStatement.setInt(7, info.getFama());
                preparedStatement.setBoolean(8, info.isWinner());
                preparedStatement.setString(9, jug.getNombre());
                preparedStatement.executeUpdate();
            } catch (final Exception Exception) {
                Exception.printStackTrace();
            } finally {
                close(preparedStatement);
            }
        });
    }

    public static void load_PlayerCHGInfo(Jugador jugador) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            String queryBuilder = "SELECT * FROM `CHGInfo` WHERE `Player` = ?;";
            preparedStatement = connection.prepareStatement(queryBuilder);
            preparedStatement.setString(1, jugador.getNombre());
            resultSet = preparedStatement.executeQuery();

            if (resultSet != null && resultSet.next()) {
                jugador.setChgInfo(new CHGInfo(
                        CHGRank.valueOf(resultSet.getString("rango")),
                        resultSet.getInt("kills"),
                        resultSet.getInt("ganadas"),
                        resultSet.getInt("jugadas"),
                        resultSet.getBoolean("winner"),
                        resultSet.getString("kit"),
                        resultSet.getInt("racha"),
                        resultSet.getInt("fama")
                ));
            }else{
                createPlayerCHGInfo(jugador);
            }
        } catch (SQLException Exception) {
            Util.console("&c[Core] Excepcion cargando PlayerCHGInfo de "+jugador.getNombre()+". (ASYNC)");
        } finally {
            close(resultSet);
            close(preparedStatement);
        }


    }

    private static void createPlayerCHGInfo(Jugador jugador) {
        PreparedStatement statement = null;
        try {
            String queryBuilder = "INSERT INTO `CHGInfo` (`Player`, `kills`, `rango`, `kit`, `ganadas`, `jugadas`, `winner`, `fama`, `racha`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
            statement = connection.prepareStatement(queryBuilder);
            statement.setString(1, jugador.getNombre());
            statement.setInt(2, 0);
            statement.setString(3, "NUEVO");
            statement.setString(4, "default");
            statement.setInt(5, 0);
            statement.setInt(6, 0);
            statement.setBoolean(7, false);
            statement.setInt(8, 0);
            statement.setInt(9, 0);

            jugador.setChgInfo(new CHGInfo(CHGRank.NUEVO, 0, 0, 0, false, "default",0, 0));
            statement.executeUpdate();
        } catch (SQLException e) {
            throw  new RuntimeException(e);
        }finally {
            close(statement);
        }
    }

}
