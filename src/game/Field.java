/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

/**
 * 
 * @author TAB
 */
public class Field implements Serializable {

	public static final int FREE = 0;
	public static final int DOT = 1;
	public static final int SHIP = 2;
	public static final int CHIP = 3;
	public static final int FIRE = 4;
	int N = 10;
	int[][] field;
	Ship[][] shipsField;
	ArrayList<Ship> ships;

	public Field() {
		ships = new ArrayList<Ship>();
		shipsField = new Ship[N][N];
		field = new int[N][N];
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				shipsField[i][j] = null;
				field[i][j] = FREE;
			}
		}
	}

	public void placeShip(Ship ship, int x, int y) {
		ships.add(ship);
		if (ship.horizontal) {
			for (int i = 0; i < ship.size; i++) {
				field[x + i][y] = SHIP;
				shipsField[x + i][y] = ship;
			}
		} else {
			for (int i = 0; i < ship.size; i++) {
				field[x][y + i] = SHIP;
				shipsField[x][y + i] = ship;
			}
		}
	}

	public boolean isPosiblePlace(Ship ship, int x, int y) {
		if (ship.horizontal) {
			if (x + ship.size > N) {
				return false;
			}
			for (int i = -1; i <= ship.size; i++) {
				if (!isNoShip(x + i, y) || !isNoShip(x + i, y - 1)
						|| !isNoShip(x + i, y + 1)) {
					return false;
				}
			}
		} else {
			if (y + ship.size > N) {
				return false;
			}
			for (int i = -1; i <= ship.size; i++) {
				if (!isNoShip(x, y + i) || !isNoShip(x - 1, y + i)
						|| !isNoShip(x + 1, y + i)) {
					return false;
				}
			}
		}
		return true;
	}

	public int aliveShipsNumb() {
		int res = 0;
		for (int i = 0; i < ships.size(); i++) {
			if (ships.get(i).isAlive()) {
				res++;
			}
		}
		return res;
	}

	public boolean isEmpty(int x, int y) {
		if (x < 0 || x >= N || y < 0 || y >= N) {
			return false;
		}
		if (field[x][y] != FREE) {
			return false;
		}
		return true;
	}

	public boolean isNoShip(int x, int y) {
		if (x < 0 || x >= N || y < 0 || y >= N) {
			return true;
		}
		if (field[x][y] != FREE) {
			return false;
		}
		return true;
	}

	public void randomField() {
		clearField();
		boolean flag = true;
		flag = flag && placeRandomShip(4);
		flag = flag && placeRandomShip(3);
		flag = flag && placeRandomShip(3);
		flag = flag && placeRandomShip(2);
		flag = flag && placeRandomShip(2);
		flag = flag && placeRandomShip(2);
		flag = flag && placeRandomShip(1);
		flag = flag && placeRandomShip(1);
		flag = flag && placeRandomShip(1);
		flag = flag && placeRandomShip(1);
		if (!flag) {
			randomField();
		}
	}

	public void clearField() {
		ships = new ArrayList<Ship>();
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				shipsField[i][j] = null;
				field[i][j] = FREE;
			}
		}
	}

	public boolean placeRandomShip(int size) {
		Random r = new Random();
		int x = r.nextInt(N);
		int y = r.nextInt(N);
		Ship s = new Ship(r.nextBoolean(), size);
		int count = 0;
		while (count < 100) {
			if (isPosiblePlace(s, x, y)) {
				placeShip(s, x, y);
				return true;
			}
			s.horizontal = r.nextBoolean();
			x = r.nextInt(N);
			y = r.nextInt(N);
			count++;
		}
		return false;
	}

	public void killShip(Ship s) {
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (shipsField[i][j] == s) {
					field[i][j] = CHIP;
				}
			}
		}
	}

	public void removeShip(Ship s) {
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (shipsField[i][j] == s) {
					field[i][j] = FREE;
					shipsField[i][j] = null;
				}
			}
		}
		ships.remove(s);
	}

	public void removeAllShips() {
		for (int i = ships.size(); i > 0; i--) {
			removeShip(ships.get(i - 1));
		}
	}

	public boolean isShip(int x, int y) {
		if (shipsField != null) {
			return true;
		}
		return false;
	}

	public Ship getShip(int x, int y) {
		return shipsField[x][y];
	}

	public int makeShot(int x, int y) {
		if (field[x][y] == CHIP || field[x][y] == DOT || field[x][y] == FIRE) {
			return -1;
		}
		if (field[x][y] == FREE) {
			field[x][y] = DOT;
			return 1;
		}
		if (field[x][y] == SHIP) {
			field[x][y] = FIRE;
			shipsField[x][y].makeShot();
			if (!shipsField[x][y].isAlive()) {
				killShip(shipsField[x][y]);
				return 3;
			}
			return 2;
		}
		return -2;
	}

	public int[][] getIntField() {
		return field;
	}
}
