package com.lee.arphoto.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 随机工具
 */
public class RandomUtil {

	private static final Log Log = LogFactory.getLog(RandomUtil.class);

	private static Random random = new Random(System.currentTimeMillis());

	/**
	 * 饼图随机
	 * 
	 * @param list
	 * @param totalOdds
	 *            总的权重
	 * @return
	 */
	public static <T> T randomSelect(Collection<T> list, int totalOdds, Odds<T> odds) {
		double rdm = ThreadLocalRandom.current().nextInt(totalOdds);
		double oddsSum = 0;
		for (T t : list) {
			oddsSum += odds.getOdds(t);
			if (rdm < oddsSum) {
				return t;
			}
		}
		Log.error("randomSelect Fail, rdm:" + rdm + "  totalOdds:" + totalOdds + "  oddsSum:" + oddsSum);
		return null;
	}

	/**
	 * 
	 * @param map
	 * @param totalOdds
	 * @return
	 */
	public static int randomSelect(Map<Integer, Integer> map) {
		int totalOdds = 0;
		for (int ratio : map.values()) {
			totalOdds += ratio;
		}

		double rdm = ThreadLocalRandom.current().nextInt(totalOdds);
		int oddsSum = 0;
		for (Entry<Integer, Integer> t : map.entrySet()) { // TODO 固定Set顺序
			oddsSum += t.getValue();
			if (rdm < oddsSum) {
				return t.getKey();
			}
		}
		Log.error("RandomReward fail, rdm:" + rdm + "  totalOdds:" + totalOdds + "  oddsSum:" + oddsSum);
		return 0;
	}

	public interface Odds<T> {
		public double getOdds(T obj);
	}

	/**
	 * 获取1 ~ num 范围内的随机数
	 * 
	 * @param num
	 * @return
	 */
	public static int random1BetweenNum(int num) {
		return ThreadLocalRandom.current().nextInt(num) + 1;
	}

	/**
	 * 获取num以内的随机数，从0开始，不包括num
	 * 
	 * @param num
	 * @return
	 */
	public static int random0BetweenNum(int num) {
		return ThreadLocalRandom.current().nextInt(num);
	}

	/**
	 * 获取唯一的UUID数字
	 * 
	 * @return
	 */
	public static long getUUID() {
		return Math.abs(UUID.randomUUID().getMostSignificantBits());
	}

	/**
	 * 获取唯一的UUID数字的字符串
	 * 
	 * @return
	 */
	public static String getUUIDStr() {
		return String.valueOf(getUUID());
	}

	/**
	 * 获取唯一的UUID字符串
	 * 
	 * @return
	 */
	public static String getGUID() {
		return UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
	}

	/**
	 * 获取随机数
	 * 
	 * @param from
	 * @param to
	 * @return
	 */
	public static final int random(int from, int to) {
		if (from >= to) {
			return from;
		}
		return random.nextInt(to - from) + from;
	}

	/**
	 * 从指定范围的中抽取不重复的数字
	 * 
	 * @param limit
	 *            范围(不包括)
	 * @param num
	 * @return
	 */
	public static int[] random4Arr(int limit, int num) {
		if (limit < num)
			return null;
		else {
			int[] end = new int[num];
			Random random = new Random();
			StringBuilder temp = new StringBuilder();
			for (int i = 0; i < num; i++) {
				int ran = random.nextInt(limit);
				while (temp.indexOf(ran + ":") > -1) {
					ran = random.nextInt(limit);
				}
				temp.append(ran + ":");
				end[i] = ran;
			}
			return end;
		}
	}

	/**
	 * 从指定范围中抽取指定不重复范围的值
	 * 
	 * @param include
	 * @param notInclude
	 * @return
	 */
	public static byte random4Arr(byte[] include, byte[] notInclude) {
		if (include.length <= 0) {
			return 0;
		}
		int lenght = include.length;
		byte temp = 0;
		while (true) {
			int rand = random(0, lenght);
			temp = include[rand];
			if (notInclude != null && notInclude.length > 0) {
				Arrays.sort(notInclude);
				int rs = Arrays.binarySearch(notInclude, temp);
				if (rs < 0) {
					break;
				}
			} else {
				break;
			}
		}
		return temp;
	}

	/**
	 * 指定范围随机数
	 * 
	 * @param min
	 * @param max
	 * @return
	 */
	public static int nextInt4Stock(int min, int max) {
		int rank = max - min;
		if (rank < 0) {
			rank = 0 - rank;
			min = max;
		}
		return random.nextInt(rank) + min;
	}

	public static int randomInt(int limit) {
		return random.nextInt(limit);
	}

	/**
	 * 在指定范围内随机概率
	 * 
	 * @param prob
	 * @param total
	 * @return
	 */
	public static final boolean probability(int prob, int total) {
		if (prob >= total) {
			return true;
		}
		if (prob == 0) {
			return false;
		}
		int rndV = random.nextInt(total) + 1;

		if (rndV <= prob) {
			return true;
		}
		return false;
	}

	/**
	 * 在指定范围内随机概率
	 * 
	 * @param prob
	 * @param minVal
	 * @param maxVal
	 * @return
	 */
	public static final boolean probability(int prob, int minVal, int maxVal) {
		if (prob >= maxVal) {
			return true;
		}
		if (prob <= minVal) {
			return false;
		}
		if (random.nextInt(maxVal - minVal - 1) <= prob) {
			return true;
		}
		return false;
	}

	public static void main(String[] args) {
		int reCalCount = 0;

		Set<String> strs = new HashSet<>();
		int i = 0;
		for (;;) {
			String s = getRandStringEx(6);
			if (strs.contains(s)) {
				if (reCalCount >= 1000) {
					System.out.println(i + "次出现重复");
					break;
				} else {
					reCalCount++;
					continue;
				}
			}

			strs.add(s);
			i++;
		}
	}

	/**
	 * 产生随机字符串，长度由参数指定。
	 * 
	 * @param length
	 *            产生的字符串的长度
	 * @return 已产生的字符串
	 */
	public static String getRandStringExNum(int length) {
		byte[] charList = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h',
				'i', 'j', 'k', 'l', 'm', 'n', 'o', 'k' };
		byte[] rev = new byte[length];
		Random f = new Random();
		for (int i = 0; i < length; i++) {
			rev[i] = charList[Math.abs(f.nextInt()) % length];
		}
		return new String(rev);
	}

	/**
	 * 产生随机字符串，长度由参数指定。
	 * 
	 * @param length
	 *            产生的字符串的长度
	 * @return 已产生的字符串
	 */
	public static String getRandStringEx(int length) {
		byte[] charList = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
		byte[] rev = new byte[length];
		Random f = new Random();
		for (int i = 0; i < length; i++) {
			rev[i] = charList[Math.abs(f.nextInt()) % length];
		}
		return new String(rev);
	}
}
