package ai;

import game.Field;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import aids.BattleMessage;

public class AI {

	public Field ownField;
	public Statistic enemyStat;

	public AI(Field aiField, Statistic stat) {
		ownField = aiField;
		enemyStat = stat;
	}

	public BattleMessage shot(BattleMessage mes) {

		ownField.makeShot(x, y);
		
		int before = ownField.getShipsCount();
		boolean res = ownField.doShot(mes.point.x, mes.point.y);
		int after = ownField.getShipsCount();

		if (!res) {
			return new BattleMessage(MessConst.SHOT_FAIL, mes.point);
		}
		if (before == after) {
			return new BattleMessage(MessConst.SHOT_HIT, mes.point);
		}
		if (after == 0) {
			return new BattleMessage(MessConst.LOSE, mes.point);
		}

		return new BattleMessage(MessConst.SHOT_KILL, mes.point);
	}

	public BattleMessage getShot(Field enemyField) {
		int[][] pos = getPossibilities(enemyField.getField());
		int max = Integer.MIN_VALUE;

		for (int i = 0; i < pos.length; i++) {
			for (int j = 0; j < pos.length; j++) {
				System.out.print(pos[i][j] + " ");
			}
			System.out.println();
		}

		double[][] coefs = new double[pos.length][pos.length];

		for (int i = 0; i < pos.length; i++) {
			for (int j = 0; j < pos.length; j++) {
				coefs[i][j] = ((double) (enemyStat.count[i][j] - enemyStat.min))
						/ ((double) (enemyStat.max - enemyStat.min));
			}
		}

		for (int i = 0; i < pos.length; i++) {
			for (int j = 0; j < pos.length; j++) {
				System.out.print(coefs[i][j] + " ");
			}
			System.out.println();
		}

		for (int i = 0; i < pos.length; i++) {
			for (int j = 0; j < pos.length; j++) {
				pos[i][j] = (int) (pos[i][j] * (1 + 10.0 * coefs[i][j]));
				max = max < pos[i][j] ? pos[i][j] : max;
			}
		}

		ArrayList<Point> variants = new ArrayList<Point>();
		for (int i = 0; i < pos.length; i++) {
			for (int j = 0; j < pos.length; j++) {
				if (pos[i][j] == max)
					variants.add(new Point(i, j));
			}
		}

		for (int i = 0; i < pos.length; i++) {
			for (int j = 0; j < pos.length; j++) {
				System.out.print(pos[i][j] + " ");
			}
			System.out.println();
		}

		int bestOne = new Random().nextInt();
		Point bestPoint = variants.get(bestOne % variants.size());

		return new BattleMessage(MessConst.SHOT, bestPoint);
	}

	private int[][] getPossibilities(byte[][] bs) {
		int[][] res = new int[bs.length][bs.length];
		for (int i = 0; i < bs.length; i++) {
			for (int j = 0; j < bs.length; j++) {
				res[i][j] = calcPoss(bs, i, j);
			}
		}
		return res;
	}

	private int calcPoss(byte[][] bs, int x, int y) {
		int res = 0;
		if (bs[x][y] != Field.FREE_CELL) {
			return res;
		}
		int tx, ty;
		// Up
		for (int i = 1; i < 4; i++) {
			tx = x - i;
			if (tx < 0)
				break;
			if (bs[tx][y] == Field.STRUCKED_FREE_CELL)
				break;
			if (bs[tx][y] == Field.FREE_CELL)
				res += i;
			if (bs[tx][y] == Field.STRUCKED_CELL_WITH_SHEEP)
				res += 5 * (4 - i);
		}

		// Down
		for (int i = 1; i < 4; i++) {
			tx = x + i;
			if (tx >= bs.length)
				break;
			if (bs[tx][y] == Field.STRUCKED_FREE_CELL)
				break;
			if (bs[tx][y] == Field.FREE_CELL)
				res += i;
			if (bs[tx][y] == Field.STRUCKED_CELL_WITH_SHEEP)
				res += 5 * (4 - i);
		}

		// Left
		for (int i = 1; i < 4; i++) {
			ty = y - i;
			if (ty < 0)
				break;
			if (bs[x][ty] == Field.STRUCKED_FREE_CELL)
				break;
			if (bs[x][ty] == Field.FREE_CELL)
				res += i;
			if (bs[x][ty] == Field.STRUCKED_CELL_WITH_SHEEP)
				res += 5 * (4 - i);
		}

		// Right
		for (int i = 1; i < 4; i++) {
			ty = y + i;
			if (ty >= bs.length)
				break;
			if (bs[x][ty] == Field.STRUCKED_FREE_CELL)
				break;
			if (bs[x][ty] == Field.FREE_CELL)
				res += i;
			if (bs[x][ty] == Field.STRUCKED_CELL_WITH_SHEEP)
				res += 5 * (4 - i);
		}
		return res;
	}

	public static void main(String[] args) {
		int[][] starr = { { 5, 0, 0, 0, 1 }, { 3, 0, 0, 0, 0 },
				{ 9, 1, 1, 1, 1 }, { 4, 4, 4, 4, 4 }, { 51, 1, 4, 1, 2 } };
		Statistic st = new Statistic(starr);

		byte[][] efi = { { 1, 0, 2, 0, 1 }, { 1, 0, 2, 0, 0 },
				{ 1, 1, 0, 1, 1 }, { 1, 1, 0, 0, 0 }, { 1, 1, 0, 1, 0 } };
		byte[][] ufi = { { 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0 } };

		Field field = new Field();
		field.setField(efi);

		AI ai = new AI(field, st);

		int[][] res = ai.getPossibilities(ufi);

		for (int i = 0; i < res.length; i++) {
			for (int j = 0; j < res.length; j++) {
				System.out.print(res[i][j] + " ");
			}
			System.out.println();
		}

		Field enemy = new Field();
		enemy.setField(efi);
		System.out.println(ai.getShot(enemy));

	}

}
