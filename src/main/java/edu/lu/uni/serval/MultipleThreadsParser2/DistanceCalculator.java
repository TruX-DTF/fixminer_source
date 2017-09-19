package edu.lu.uni.serval.MultipleThreadsParser2;

public class DistanceCalculator {
	
	public static double calculateDistance(DistanceFunction distanceType, Double[] targetPoint, Double[] selfPoint) {
		double distance = 0;
		switch (distanceType) {
		case EUCLIDEAN:
			distance = DistanceCalculator.euclideanDistance(targetPoint, selfPoint);
			break;
		case COSINESIMILARITY:
			distance = DistanceCalculator.cosineSimilarityDistance(targetPoint, selfPoint);
			break;
		case MANHATTAN:
			distance = DistanceCalculator.manhattanDistance(targetPoint, selfPoint);
			break;
		default:
			distance = DistanceCalculator.euclideanDistance(targetPoint, selfPoint);
			break;
		}
		return distance;
	}

	public static double euclideanDistance(Double[] targetPoint, Double[] selfPoint) {
		double sum = 0.0;

		for (int i = 0, length = targetPoint.length; i < length; i++) {
			double diff = targetPoint[i] - selfPoint[i];
			sum += diff * diff;
		}
		return Math.sqrt(sum);
	}

	public static Double cosineSimilarityDistance(Double[] targetPoint, Double[] selfPoint) {
		Double sim = 0.0d;
		int length = targetPoint.length;
		double dot = 0.0d;
		double mag1 = 0.0d;
		double mag2 = 0.0d;

		for (int i = 0; i < length; i ++) {
			dot += targetPoint[i] * selfPoint[i];
			mag1 += Math.pow(targetPoint[i], 2);
			mag2 += Math.pow(selfPoint[i], 2);
		}

		sim = dot / (Math.sqrt(mag1) * Math.sqrt(mag2));
		return sim;
	}

	public static Double manhattanDistance(Double[] targetPoint, Double[] selfPoint) {
		double result = 0.0;
		for (int i = 0; i < targetPoint.length; i++) {
			result += Math.abs(selfPoint[i] - targetPoint[i]);
		}
		return result;
	}

	public static Double minkowskiDistance(Double[] targetPoint, Double[] selfPoint) {
		return null;
	}
	
	public static Double jaccardSimilarity(Double[] targetPoint, Double[] selfPoint) {
		return null;
	}

}

