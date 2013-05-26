package client;

import aids.BattleMessage;
import game.Field;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client extends Thread {

    public static final int PORT = 2517;
    public static final String ADDRESS = "127.0.0.1";
    ///////////////
    Socket socket;
    InputStream is;
    ObjectInputStream ois;
    OutputStream os;
    ObjectOutputStream oos;
    LoginJForm loginForm;
    MainJForm mainForm;
    String name;

    public Client(LoginJForm ljf) throws UnknownHostException, IOException {
        loginForm = ljf;
        socket = new Socket(InetAddress.getByName(ADDRESS), PORT);
        os = socket.getOutputStream();
        oos = new ObjectOutputStream(os);
        oos.flush();
        is = socket.getInputStream();
        ois = new ObjectInputStream(is);
    }

    @Override
    public void run() {
        BattleMessage mes;
        while (true) {
            mes = getMessage();

            if (mes.type == BattleMessage.WELLCOME) {
                loginForm.openMainFrame();
            }
            if (mes.type == BattleMessage.INCORRECT_LOGIN) {
                loginForm.showMessage("Incorrect login!");
            }
            if (mes.type == BattleMessage.USER_EXISTS) {
                loginForm.showMessage("Player with this name already exists!");
                loginForm.randomLogin();
            }
            if (mes.type == BattleMessage.STATISTIC) {
                mainForm.addStatistic(mes);
            }
            if (mes.type == BattleMessage.USER_ONLINE) {
                mainForm.addOnlineUser(mes.fromName);
            }
            if (mes.type == BattleMessage.USER_OFFLINE) {
                mainForm.removeOnlineUser(mes.fromName);
            }
            if (mes.type == BattleMessage.GAME_QUEST) {
                mainForm.startGameQuest(mes);
            }
            if (mes.type == BattleMessage.GAME_NO) {
                mainForm.answerNo(mes);
            }            
            if (mes.type == BattleMessage.GAME_OPPONENT_FIELD) {
                mainForm.addField(mes, getField());
            }
            if (mes.type == BattleMessage.GAME_OPPONENT_MOVE) {
                mainForm.getGameJForm(mes).opponentMove(mes);
            }
            if (mes.type == BattleMessage.GAME_MAKE_MOVE) {
                mainForm.getGameJForm(mes).enableMoves();
            }
            
            if (mes.type == BattleMessage.GAME_WIN) {
                mainForm.winGame(mes);
            }
            if (mes.type == BattleMessage.GAME_LOSE) {
                mainForm.losGame(mes);
            }
            
        }

    }

    public BattleMessage getMessage() {
        BattleMessage mes = null;
        try {
            mes = (BattleMessage) ois.readObject();
        } catch (IOException ex) {
            loginForm.showMessage("Server not found!");
            System.exit(1);
        } catch (ClassNotFoundException ex) {
            loginForm.showMessage("Server not found!!");
            System.exit(2);
        }
        return mes;
    }

    public Field getField() {
        Field fld = null;
        try {
            fld = (Field) ois.readObject();
        } catch (IOException ex) {
            loginForm.showMessage("Server not found!");
            System.exit(1);
        } catch (ClassNotFoundException ex) {
            loginForm.showMessage("Server not found!!");
            System.exit(2);
        }
        return fld;
    }
    
    public void sendMessage(BattleMessage mes) {
        try {
            oos.writeObject(mes);
            oos.flush();
        } catch (IOException ex) {
            loginForm.showMessage("Problems with connection!");
            System.exit(3);
        }
    }

    public void sendField(BattleMessage mes, Field fld) {
        sendMessage(mes);
        try {
            oos.writeObject(fld);
            oos.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
//            loginForm.showMessage("Problems with connection!");
            System.exit(7);
        }
    }

    public void setMainForm(MainJForm mainForm) {
        this.mainForm = mainForm;
    }
}
