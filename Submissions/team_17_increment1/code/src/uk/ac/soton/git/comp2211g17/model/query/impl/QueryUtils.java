package uk.ac.soton.git.comp2211g17.model.query.impl;

import java.util.Arrays;
import java.util.List;

public class QueryUtils {
	public static float[] toFloatArray(List<Float> floatList) {
		float[] arr = new float[floatList.size()];
		for (int i = 0; i < arr.length; i++) {
			arr[i] = floatList.get(i);
		}
		return arr;
	}

	public static int getMedian(int[] freq) {
		int target = Arrays.stream(freq).sum() / 2;
		int index = 0;
		while (target > 0) {
			target -= freq[index];
			index++;
		}

		return index - 1;
	}
}
