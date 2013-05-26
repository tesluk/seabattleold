package ai;

import game.Field;

import com.sun.xml.internal.ws.util.StringUtils;

/**
 * 
 * @author Tab
 * 
 */
public class Statistic {

	public int[][] count;
	public int max;
	public int min;

	public Statistic() {
		count = new int[10][10];
		max = 0;
		min = 0;
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				count[i][j] = 0;
			}
		}
	}

	public Statistic(int[][] _count) {
		count = _count;
		max = Integer.MIN_VALUE;
		min = Integer.MAX_VALUE;
		findMinMax();
	}

	private void findMinMax() {
		for (int i = 0; i < count.length; i++) {
			for (int j = 0; j < count[i].length; j++) {
				max = max < count[i][j] ? count[i][j] : max;
				min = min > count[i][j] ? count[i][j] : min;
			}
		}
	}

	public void plusField(Field field) throws Exception {
		if (count.length != field.getIntField().length) {
			throw new Exception("Wrong field size " + count.length);
		}
		for (int i = 0; i < count.length; i++) {
			for (int j = 0; j < count[j].length; j++) {
				count[i][j] += field.getIntField()[i][j] == Field.SHIP ? 1
						: 0;
			}
		}
		findMinMax();
	}

	public static Statistic parseStatistic(String str) throws Exception {
		if (str.equals("0")) {
			return new Statistic();
		}
		String[] parts = str.split(",");
		int res[][] = parseArray(parts);
		return new Statistic(res);
	}

	private static int[][] parseArray(String[] arr) throws Exception {
		int n = (int) Math.sqrt(arr.length);

		System.out.println(n);
		if (arr.length % n != 0) {
			throw new Exception("Wrong statistic size!" + arr.length);
		}
		int[][] res = new int[n][n];
		for (int i = 0; i < arr.length; i++) {
			res[i / n][i % n] = Integer.parseInt(arr[i]);
		}
		return res;
	}

	public String getString() {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < count.length; i++) {
			for (int j = 0; j < count.length; j++) {
				builder.append(String.valueOf(count[i][j]));
				if (i + j != count.length * 2 - 2) {
					builder.append(",");
				}
			}
		}
		return builder.toString();
	}

	public static void main(String[] args) throws Exception {
		String str = "1,2,3,4,5,6,7,8,9,0,11,12,13,14,15,16";
		Statistic st = Statistic.parseStatistic(str);

		int[][] res = st.count;

		System.out.println(st.max + " " + st.min);

		for (int i = 0; i < res.length; i++) {
			for (int j = 0; j < res[i].length; j++) {
				System.out.print(res[i][j] + " ");
			}
			System.out.println();
		}

	}

}
