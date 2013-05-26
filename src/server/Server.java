/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import aids.BattleMessage;
import game.Field;
import game.Game;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

    public static final int PORT = 2517;
    //////////////////////
    ServerSocket serverSocket;
    Socket tmpSocket;
    ArrayList<PlayerSocket> players;
    ArrayList<BattleMessage> statistics;
    ArrayList<Game> games;

    public Server() throws IOException {
        init();
        work();
    }

    private void init() throws IOException {
        serverSocket = new ServerSocket(PORT);
        players = new ArrayList<PlayerSocket>();
        statistics = new ArrayList<BattleMessage>();
        games = new ArrayList<Game>();
    }

    private void work() throws IOException {
        printToConsole("Game server started...");
        while (true) {
            tmpSocket = serverSocket.accept();
            addPlayer(tmpSocket);
        }
    }

    private void addPlayer(Socket s) {
        PlayerSocket tmp = new PlayerSocket(s, this, "NotLogined");
        players.add(tmp);
        tmp.start();
    }

    public void kickPlayer(PlayerSocket ps) {
        players.remove(ps);
        ps.interrupt();
        printToConsole("Player " + ps.name + " disconected");
        sendOfflineUserToAll(ps.name);
    }

    public void registerPlayer(PlayerSocket ps, String name) {
        if (isNameExists(name) || name.equals("Server") || name.equals("NotLogined")) {
            ps.sendMessage(new BattleMessage(BattleMessage.USER_EXISTS));
            return;
        }
        if (name.length() < 3) {
            ps.sendMessage(new BattleMessage(BattleMessage.INCORRECT_LOGIN));
            return;
        }
        printToConsole("Player " + name + " connected");
        ps.name = name;
        ps.sendMessage(new BattleMessage(BattleMessage.WELLCOME));
        sendOnlineUserToAll(name);
        getAllOnlineUsers(ps);
        getAllStatistics(ps);
    }

    public void sendOfflineUserToAll(String name) {
        for (int i = 0; i < players.size(); i++) {
            if (!players.get(i).name.equals(name)) {
                players.get(i).sendMessage(new BattleMessage(BattleMessage.USER_OFFLINE, name, players.get(i).name));
            }
        }
    }

    public void sendOnlineUserToAll(String name) {
        for (int i = 0; i < players.size(); i++) {
            if (!players.get(i).name.equals(name)) {
                players.get(i).sendMessage(new BattleMessage(BattleMessage.USER_ONLINE, name, players.get(i).name));
            }
        }
    }

    public void getAllOnlineUsers(PlayerSocket ps) {
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i) == ps) {
                continue;
            }
            ps.sendMessage(new BattleMessage(BattleMessage.USER_ONLINE, players.get(i).name, ps.name));
        }
    }

    private boolean isNameExists(String name) {
        boolean flag = false;
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).name.equals(name)) {
                flag = true;
            }
        }
        return flag;
    }

    public void getAllStatistics(PlayerSocket ps) {
        for (int i = 0; i < statistics.size(); i++) {
            ps.sendMessage(statistics.get(i));
        }
    }

    public void addStatistic(BattleMessage mes) {
        printToConsole(mes.getStatistic());
        statistics.add(mes);
        sendStatisticToAll(mes);
    }

    private void sendStatisticToAll(BattleMessage mes) {
        for (int i = 0; i < players.size(); i++) {
            if (!players.get(i).name.equals("NotLogined")) {
                players.get(i).sendMessage(mes);
            }
        }
    }

    public void printToConsole(String str) {
        System.out.println(str);
    }

    public void startNewGame(BattleMessage mes) {
        printToConsole(mes.toString());
        Game tmp = new Game(getByName(mes.fromName), getByName(mes.toName), this);
        games.add(tmp);
    }

    public void endGame(Game game) {
        games.remove(game);
    }

    public void addFieldToGame(PlayerSocket ps, BattleMessage mes, Field fld) {
        for (int i = 0; i < games.size(); i++) {
            if (games.get(i).isThisGame(mes)) {
                games.get(i).setField(ps, fld);
                return;
            }
        }
    }

    public void makeMove(PlayerSocket ps, BattleMessage mes) {
        for (int i = 0; i < games.size(); i++) {
            if (games.get(i).isThisGame(mes)) {
                games.get(i).makeMove(ps, mes);
                return;
            }
        }
    }

    public void sendQuestForGame(BattleMessage mes) {
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).name.equals(mes.toName)) {
                players.get(i).sendMessage(mes);
                return;
            }
        }
    }

    public void sendMessageByName(BattleMessage mes) {
        PlayerSocket ps = getByName(mes.toName);
        if (ps != null) {
            ps.sendMessage(mes);
        }
    }

    public PlayerSocket getByName(String name) {
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).name.equals(name)) {
                return players.get(i);
            }
        }
        return null;
    }

    public static void main(String[] a) throws IOException {
        new Server();
    }
}
