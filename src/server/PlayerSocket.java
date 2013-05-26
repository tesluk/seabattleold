package server;

import aids.BattleMessage;
import game.Field;
import game.Game;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PlayerSocket extends Thread {

    Socket socket;
    Server parent;
    public String name;
    ////////////////////
    InputStream is;
    ObjectInputStream ois;
    OutputStream os;
    ObjectOutputStream oos;

    public PlayerSocket(Socket socket, Server parent, String name) {
        this.socket = socket;
        this.parent = parent;
        this.name = name;
    }

    @Override
    public void run() {
        try {
            is = socket.getInputStream();
            ois = new ObjectInputStream(is);
            os = socket.getOutputStream();
            oos = new ObjectOutputStream(os);
            BattleMessage mes;
            Field fld;
            while (true) {
                mes = readMessage();
                if (mes == null) {
                    break;
                }
                if (mes.type == BattleMessage.LOGIN) {
                    parent.registerPlayer(this, mes.fromName);
                }
                if (mes.type == BattleMessage.GAME_FIELD) {
                    fld = readField();
                    parent.addFieldToGame(this, mes, fld);
                }
                if (mes.type == BattleMessage.GAME_MOVE) {
                    parent.makeMove(this, mes);
                }
                if (mes.type == BattleMessage.GAME_QUEST) {
                    parent.sendQuestForGame(mes);
                }
                if (mes.type == BattleMessage.GAME_YES) {
                    parent.startNewGame(mes);
                }
                if (mes.type == BattleMessage.GAME_NO) {
                    parent.sendMessageByName(mes);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(PlayerSocket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public BattleMessage readMessage() {
        BattleMessage mes = null;
        try {
            mes = (BattleMessage) ois.readObject();
        } catch (ClassNotFoundException ex) {
            parent.kickPlayer(this);
        } catch (IOException ex) {
            parent.kickPlayer(this);
        }
        return mes;
    }

    public Field readField() {
        Field fld = null;
        try {
            fld = (Field) ois.readObject();
        } catch (ClassNotFoundException ex) {
            parent.kickPlayer(this);
        } catch (IOException ex) {
            parent.kickPlayer(this);
        }
        return fld;
    }

    public void sendMessage(BattleMessage mes) {
        try {
            oos.writeObject(mes);
            oos.flush();
        } catch (IOException ex) {
            parent.kickPlayer(this);
        }
    }

    public void sendOpponentField(BattleMessage mes, Field fld) {
        try {
            oos.writeObject(mes);
            oos.writeObject(fld);
            oos.flush();
        } catch (IOException ex) {
            parent.kickPlayer(this);
        }
    }
}
