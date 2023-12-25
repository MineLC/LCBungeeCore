package lc.bungeecore.entidades.minijuegos;

public class CHGInfo {

    private CHGRank rank = CHGRank.NUEVO;
    private int kills = 0;
    private int wins = 0;
    private int playeds = 0;
    private boolean winner = false;
    private String kit;
    private int racha;
    private int fama;

    public CHGInfo(CHGRank rank, int kills, int wins, int playeds, boolean winner, String kit, int racha, int fama) {
        this.rank = rank;
        this.kills = kills;
        this.wins = wins;
        this.playeds = playeds;
        this.winner = winner;
        this.kit = kit;
        this.racha = racha;
        this.fama = fama;
    }

    public CHGRank getRank() {
        return rank;
    }

    public void setRank(CHGRank rank) {
        this.rank = rank;
    }


    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getPlayeds() {
        return playeds;
    }

    public void setPlayeds(int playeds) {
        this.playeds = playeds;
    }

    public boolean isWinner() {
        return winner;
    }

    public void setWinner(boolean winner) {
        this.winner = winner;
    }

    public String getKit() {
        return kit;
    }

    public void setKit(String kit) {
        this.kit = kit;
    }

    public int getRacha() {
        return racha;
    }

    public void setRacha(int racha) {
        this.racha = racha;
    }

    public int getFama() {
        return fama;
    }

    public void setFama(int fama) {
        this.fama = fama;
    }
}