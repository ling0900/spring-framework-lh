package spring.lh.demo;



public class Solution2 {
	/**
	 * 代码中的类名、方法名、参数名已经指定，请勿修改，直接返回方法规定的值即可
	 *
	 * 最长山脉的长度
	 * @param nums int整型一维数组 每个元素表示一座山的高度
	 * @return int整型
	 */
	public static int maxLength (int[] nums) {
		// 定义结果
		int maxLength = 0;

		// 先验证长度
		if (nums == null || nums.length < 3) {
			return maxLength;
		}

		// 先找到第一个可能的峰
		for (int j = 0; j < nums.length; j++) {
			int i = j;
			while (nums[i] < nums[i+1]) {
				maxLength++;
				i++;
			}

			while (nums[i] > nums[i+1]) {
				i++;
				maxLength++;
			}
		}



		return maxLength == 0 ? 0 : maxLength + 1;
	}

	private static boolean isPeakElement(int[] arr, int index) {

		if (index == 0 || index >= arr.length - 1) {
			return false;
		}

		if (arr[index - 1] < arr[index] && arr[index] > arr[index + 1]) {
			return true;
		}

		return  false;
	}

	public static void main(String[] args) {
		int[] arr = {2,2,2,1,5,3};
		System.out.println("====");

		System.out.println(maxLength(arr));

	}

}
