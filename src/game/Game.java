package game;

import ai.AI;
import aids.BattleMessage;
import javax.print.attribute.standard.Severity;
import org.omg.PortableServer.Servant;
import server.PlayerSocket;
import server.Server;

/**
 *
 * @author TAB
 */
public class Game {

    int ID;
    Field field1;
    Field field2;
    PlayerSocket player1;
    PlayerSocket player2;
    AI ai1;
    AI ai2;
    Server parent;

    public Game(PlayerSocket player1, PlayerSocket player2, Server parent) {
        this.player1 = player1;
        this.player2 = player2;
        this.parent = parent;
    }

    public boolean isThisGame(BattleMessage mes) {
        if (mes.fromName.equals(player1.name) && mes.toName.equals(player2.name)) {
            return true;
        }
        if (mes.fromName.equals(player2.name) && mes.toName.equals(player1.name)) {
            return true;
        }
        return false;
    }

    public void setField(PlayerSocket ps, Field fld) {
        if (ps == player1) {
            field1 = fld;
        }
        if (ps == player2) {
            field2 = fld;
        }
        if (field1 != null && field2 != null) {
            startGame();
        }
    }

    public void giveMove(PlayerSocket ps) {
        String op_name;
        if (ps.name.equals(player1.name)) {
            op_name = player2.name;
        } else {
            op_name = player1.name;
        }
        ps.sendMessage(new BattleMessage(BattleMessage.GAME_MAKE_MOVE, op_name, ps.name));
    }

    public void makeMove(PlayerSocket ps, BattleMessage mes) {
        int shotRes = 0;
        PlayerSocket opponent = null;
        if (ps == player1) {
            opponent = player2;
            shotRes = field2.makeShot(mes.x, mes.y);
            player2.sendMessage(new BattleMessage(BattleMessage.GAME_OPPONENT_MOVE,
                    player1.name, player2.name, mes.x, mes.y));
            System.out.println(field2.aliveShipsNumb());
            if (field2.aliveShipsNumb() == 0) {
                endGame(ps, opponent, field1.aliveShipsNumb(), field2.aliveShipsNumb());
            }
        }
        if (ps == player2) {
            opponent = player1;
            shotRes = field1.makeShot(mes.x, mes.y);
            player1.sendMessage(new BattleMessage(BattleMessage.GAME_OPPONENT_MOVE,
                    player2.name, player1.name, mes.x, mes.y));
            System.out.println(field1.aliveShipsNumb());
            if (field1.aliveShipsNumb() == 0) {
                endGame(ps, opponent, field2.aliveShipsNumb(), field1.aliveShipsNumb());
            }
        }
        System.out.println(shotRes);
        System.out.println(opponent.name);
        if (shotRes == 1) {
            giveMove(opponent);
        }
        if (shotRes >= 2 || shotRes == -1) {
            giveMove(ps);
        }
    }

    public void endGame(PlayerSocket winner, PlayerSocket loser, int winscore, int losscore) {
        winner.sendMessage(new BattleMessage(BattleMessage.GAME_WIN, loser.name, winner.name));
        loser.sendMessage(new BattleMessage(BattleMessage.GAME_LOSE, winner.name, loser.name));
        parent.addStatistic(new BattleMessage(BattleMessage.STATISTIC, winner.name, loser.name, winscore, losscore));
        parent.endGame(this);
    }

    public void startGame() {
        player1.sendOpponentField(new BattleMessage(BattleMessage.GAME_OPPONENT_FIELD,
                player2.name, player1.name), field2);
        player2.sendOpponentField(new BattleMessage(BattleMessage.GAME_OPPONENT_FIELD,
                player1.name, player2.name), field1);
        player1.sendMessage(new BattleMessage(BattleMessage.GAME_START,
                player2.name, player1.name));
        player2.sendMessage(new BattleMessage(BattleMessage.GAME_START,
                player1.name, player2.name));
        player1.sendMessage(new BattleMessage(BattleMessage.GAME_MAKE_MOVE,
                player2.name, player1.name));
    }
}
