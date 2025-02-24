package uk.ac.soton.git.comp2211g17.model.query.util;

import java.util.Arrays;

public class QueryUtils {
	// TODO: use the more efficient method Konrad mentioned?
	public static int getMedian(int[] freq) {
		int target = Arrays.stream(freq).sum() / 2;
		int index = 0;
		while (target > 0) {
			target -= freq[index];
			index++;
		}

		return index - 1;
	}
//
//	public static Column<?>[] fromResult(Result<Record> result, Query query, Field<?>... jooqFields) {
//
//	}
}
