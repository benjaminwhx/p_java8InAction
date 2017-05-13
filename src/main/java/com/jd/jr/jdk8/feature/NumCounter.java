package com.jd.jr.jdk8.feature;

/**
 * User: 吴海旭
 * Date: 2017-05-13
 * Time: 下午4:56
 */
public class NumCounter {

	private int num;
	private int sum;
	// 是否当前是个完整的数字
	private boolean isWholeNum;

	public NumCounter(int num, int sum, boolean isWholeNum) {
		this.num = num;
		this.sum = sum;
		this.isWholeNum = isWholeNum;
	}

	public NumCounter accumulate(Character c) {
		if (Character.isDigit(c)) {
			return isWholeNum ? new NumCounter(Integer.parseInt("" + c), getSum(), false) :
					new NumCounter(Integer.parseInt("" + num + c), sum, false);
		} else {
			return new NumCounter(0, getSum(), true);
		}
	}

	public NumCounter combine(NumCounter numCounter) {
		return new NumCounter(numCounter.num, this.getSum() + numCounter.getSum(), numCounter.isWholeNum);
	}

	public int getSum() {
		return sum + num;
	}
}
