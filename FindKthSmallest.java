import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class FindKthSmallest {

	private static void printArray(int[] array) {
		for (int i = 0; i < array.length; i++) {
			System.out.print((i > 0 && i % 5 == 0 ? ", " : " ") + array[i]);
		}
		System.out.println();
	}

	public static void main(String[] args) {

		int[] array = getArray(90_000);
		int n = array.length;
		int k = n/2;
		System.out.println("Array before sorting...");
		Arrays.sort(array);
		long startTime = System.currentTimeMillis();
		int kthSmallestNumber = getKthSmallest(array, 0, n - 1, k);
		long endTime = System.currentTimeMillis();
		System.out.println("Array after sorting...");
		Arrays.sort(array);
		for (int i = 0; i < n; i++) {
			System.out.print(array[i] + " ");
		}
		System.out.println("\n\n" + k + "th Smallest  = " + kthSmallestNumber);
		System.out.println("Time taken to run algorithm = " + (endTime - startTime) + " milliseconds for an array of size = " + array.length);

	}

	private static int[] getArray(int size) {
		int[] array = new int[size];
		Set<Integer> set = new HashSet<Integer>();
		Random rand = new Random();
		for (int i = 0; i < size; i++) {
			int randomNumber = Math.abs(rand.nextInt(size * 4)) + 1;
			if (set.contains(randomNumber)) {
				i--;
				continue;
			}
			set.add(randomNumber);
			array[i] = randomNumber;
		}
		return array;
	}

	/**
	 * to get median of an array of size <= 5
	 * 
	 * @param array
	 * @param left
	 * @param right
	 * @return
	 */
	private static int getMedian(int[] array, int left, int right) {
		int size = right - left + 1;
		int[] subArray = new int[size];
		int k = 0;
		for (int i = left; i <= right; i++) {
			subArray[k++] = array[i];
		}
		Arrays.sort(subArray);
		return subArray[size / 2];
	}

	private static void swap(int[] array, int i, int j) {
		int temp = array[i];
		array[i] = array[j];
		array[j] = temp;
	}

	private static int getPartition(int[] array, int left, int right, int median) {
		int i = left;
		for (i = left; i <= right; i++) {
			if (array[i] == median) {
				break;
			}
		}
		int pivotIndex = i;
		swap(array, pivotIndex, right);
		int storeIndex = left;
		for (i = left; i < right; i++) {
			if (array[i] <= median) {
				swap(array, storeIndex, i);
				storeIndex++;
			}
		}
		swap(array, storeIndex, right);
		return storeIndex;
	}

	private static int getKthSmallest(int[] array, int left, int right, int k) {

		int size = right - left + 1;
		if (k > 0 && size >= k) {
			int[] median = new int[(size + 4) / 5];
			int i = 0;
			for (i = 0; i < median.length; i++) {
				int leftIndex = left + i * 5;
				int rightIndex = leftIndex + 4;
				if (rightIndex >= right) {
					rightIndex = right;
				}
				median[i] = getMedian(array, leftIndex, rightIndex);
			}

			int medianOfMedian = (i == 1) ? median[i - 1] : getKthSmallest(median, 0, i - 1, i / 2);
			int pivot = getPartition(array, left, right, medianOfMedian);
			if (pivot - left == k - 1) {
				return array[pivot];
			} else if (pivot - left > k - 1) {
				return getKthSmallest(array, left, pivot - 1, k);
			}
			return getKthSmallest(array, pivot + 1, right, k - (pivot - left) - 1);
		}
		return Integer.MAX_VALUE;
	}
}
