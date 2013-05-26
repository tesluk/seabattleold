/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import java.io.Serializable;

/**
 *
 * @author user
 */
public class Ship implements Serializable{

    boolean horizontal;
    int lifes;
    int size;

    public Ship(boolean horizontal, int size) {
        this.horizontal = horizontal;
        this.lifes = size;
        this.size = size;
    }

    public boolean isAlive() {
        if (lifes == 0){
            return false;
        }
        return true;
    }

    public void makeShot() {
        lifes -= 1;
    }
    
    public void rotate(){
        horizontal = !horizontal;
    }

    public int getSize() {
        return size;
    }
    
    
}
