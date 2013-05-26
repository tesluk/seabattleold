package aids;

import java.io.Serializable;

public class BattleMessage implements Serializable {

    public static final int USER_EXISTS = 1;
    public static final int INCORRECT_LOGIN = 2;
    public static final int LOGIN = 3;
    public static final int WELLCOME = 4;
    public static final int GAME_MOVE = 5;
    public static final int GAME_START = 6;
    public static final int GAME_WIN = 7;
    public static final int GAME_LOSE = 8;
    public static final int GAME_QUEST = 9;
    public static final int GAME_YES = 10;
    public static final int GAME_NO = 11;
    public static final int GAME_MAKE_MOVE = 12;
    public static final int GAME_OPPONENT_MOVE = 13;
    public static final int GAME_OPPONENT_FIELD = 20;
    public static final int GAME_FIELD = 21;
    public static final int GET_STATISTIC = 14;
    public static final int STATISTIC = 15;
    public static final int GAME_READY = 16;
    public static final int USER_ONLINE = 17;
    public static final int USER_OFFLINE = 18;
    ///////////////////////
    public int type;
    public int x;
    public int y;
    public String fromName;
    public String toName;

    public BattleMessage(int type) {
        this.type = type;
    }

    public BattleMessage(int type, String fromName, String toName) {
        this.type = type;
        this.fromName = fromName;
        this.toName = toName;
    }

    public BattleMessage(int type, String fromName, String toName,
            int x, int y) {
        this.type = type;
        this.fromName = fromName;
        this.toName = toName;
        this.x = x;
        this.y = y;
    }

    public void setCoordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public String getStatistic() {
        return fromName + " - " + toName + "   " + String.valueOf(x) + " : " + String.valueOf(y);
    }

    @Override
    public String toString() {
        return this.fromName + " - " + this.toName + " ^ " + String.valueOf(this.type);
    }
    
    
}
