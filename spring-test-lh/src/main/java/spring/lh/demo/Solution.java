package spring.lh.demo;



public class Solution {
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

		for (int i = 0; i < nums.length; i++) {
			// 快速判断，不符合就不需要继续向两侧遍历。
			if (isPeakElement(nums, i)) {
				int curLen = 1;
				int left = i - 1;
				int right = i + 1;

				// 向左拓展
				while (left >= 0 && nums[left] < nums[left + 1]) {
					curLen++;
					left--;
				}
				// 向右拓展
				while (right < nums.length && nums[right] < nums[right - 1]) {
					curLen++;
					right++;
				}
				maxLength = Math.max(maxLength, curLen);
			}
		}
		return maxLength;
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
		int[] arr = {2,5,2,1,5};
		System.out.println("====");

		System.out.println(maxLength(arr));

	}

}
