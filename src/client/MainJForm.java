/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * MainJForm.java
 *
 * Created on 16.04.2012, 11:17:33
 */
package client;

import aids.BattleMessage;
import game.Field;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

/**
 *
 * @author TAB
 */
public class MainJForm extends javax.swing.JFrame {
    
    ArrayList<String> onlineUsers;
    ArrayList<GameJForm> games;
    Client client;

    /** Creates new form MainJForm */
    public MainJForm(Client c) {
        initComponents();
        this.client = c;
        init();
    }
    
    private void init() {
        onlineUsers = new ArrayList<String>();
        games = new ArrayList<GameJForm>();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        jList1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jList1MouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jList1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 243, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void jList1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jList1MouseClicked
    if (evt.getClickCount() != 2) {
        return;
    }
    String selected = (String) jList1.getSelectedValue();
    if (selected != null) {
        client.sendMessage(new BattleMessage(BattleMessage.GAME_QUEST, client.name, selected));
        startNewGame(selected);
    }
}//GEN-LAST:event_jList1MouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainJForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainJForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainJForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainJForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            
            @Override
            public void run() {
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList jList1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea jTextArea1;
    // End of variables declaration//GEN-END:variables

    public void addStatistic(BattleMessage mes) {
        jTextArea1.setText(jTextArea1.getText() + mes.getStatistic() + "\n");
    }
    
    public void addOnlineUser(String name) {
        if (name.equals(client.name)) {
            return;
        }
        onlineUsers.add(name);
        updateUserList();
    }
    
    public void removeOnlineUser(String name) {
        if (name.equals(client.name)) {
            return;
        }
        onlineUsers.remove(name);
        updateUserList();
    }
    
    public void startGameQuest(BattleMessage mes) {
        if (isGameExists(mes)) {
            return;
        }
        int answ = JOptionPane.showConfirmDialog(null, "Player " + mes.fromName
                + " want to play with you. Start new game?", "New game",
                JOptionPane.YES_NO_OPTION);
        if (answ == JOptionPane.YES_OPTION) {
            startNewGame(mes.fromName);
            client.sendMessage(new BattleMessage(BattleMessage.GAME_YES, client.name, mes.fromName));
            return;
        }
        client.sendMessage(new BattleMessage(BattleMessage.GAME_NO, client.name, mes.fromName));
    }
    
    public boolean isGameExists(BattleMessage mes) {
        for (int i = 0; i < games.size(); i++) {
            if (games.get(i).opponent_name.equals(mes.fromName)) {
                return true;
            }
        }
        return false;
    }
    
    public void answerNo(BattleMessage mes) {
        showMessage("Player " + mes.fromName + " don't want play with you");
        removeGameJForm(getGameJForm(mes));
    }
    
    public void removeGameJForm(GameJForm gjf){
        gjf.setVisible(false);
        games.remove(gjf);
    }
    
    public GameJForm getGameJForm(BattleMessage mes){
        System.out.println(mes.toString());
        for (int i = 0; i < games.size(); i++) {
            if (games.get(i).opponent_name.equals(mes.fromName)) {
                return games.get(i);
            }
        }
        return null;
    }
    
    public void showMessage(String str) {
        JOptionPane.showMessageDialog(this, str);
    }
    
    public void updateUserList() {
        DefaultListModel<String> ob = new DefaultListModel<String>();
        for (int i = 0; i < onlineUsers.size(); i++) {
            ob.addElement(onlineUsers.get(i));
        }
        jList1.setModel(ob);
    }
    
    public void addField(BattleMessage mes, Field fld){
        GameJForm tmp = getGameJForm(mes);
        tmp.opponentReady(fld);
    }
    
    public void startNewGame(String name) {
        for (int i = 0; i < games.size(); i++) {
            if (games.get(i).opponent_name.equals(name)) {
                return;
            }
        }
        GameJForm tmp = new GameJForm(this, client);
        tmp.opponent_name = name;
        games.add(tmp);
    }
    
    public void winGame(BattleMessage mes){
        showMessage("You win!");
        removeGameJForm(getGameJForm(mes));
    }
    
    public void losGame(BattleMessage mes){
        showMessage("You lose!");
        removeGameJForm(getGameJForm(mes));
    }
}
