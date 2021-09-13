
import java.util.ArrayList;
import java.util.List;

/**
 * Solved
 *
 * Created by lianglitu on 16/10/11.
 *
 * 类型：全排列
 * 前提：假设每个元素都不同，即保证了不会有排列结果重复的情况
 */
public class Solution {

    public static int tmp = 0;

    public List<List<Integer>> permute(int[] nums) {

        List<List<Integer>> res = new ArrayList<>();
        List<Integer> prefix = new ArrayList<>();

        tracing2(0, nums.length - 1, "", nums);
        tracing(0,nums.length-1,prefix,nums,res);

        return res;

    }

    public void tracing(int start, int end, List<Integer> prefix, int[] nums,
                        List<List<Integer>> res) {
        if (start > end) {
            res.add(prefix);
            return;
        }

        for (int i = start; i <= end; i++) {
            tmp = nums[i];
            nums[i] = nums[start];
            nums[start] = tmp;

            List<Integer> _prefix = new ArrayList<>(prefix);
            _prefix.add(nums[start]);
            tracing(start + 1, end, _prefix, nums, res);

            tmp = nums[i];
            nums[i] = nums[start];
            nums[start] = tmp;

        }


    }

    /**
     * 全排列,训练用，一串数字用String表示
     *
     * 深度优先搜索，第归
     *
     * @param start  需要全排列的序列的第一个元素的下标
     * @param end    需要全排列的序列的最后一个元素的下标
     * @param prefix 前缀，已经排列好的序列
     * @param nums
     */
    public static void tracing2(int start, int end, String prefix, int[] nums) {
        if (start > end) {
            System.out.println(prefix);
            return;
        }

        for (int i = start; i <= end; i++) {
            /**
             * 在这个序列中，每一个元素依次和第一个元素交换位置，作为打头的元素
             */
            tmp = nums[i];
            nums[i] = nums[start];
            nums[start] = tmp;

            /**
             * 确定打头的元素后，对剩余的元素全排列
             */
            tracing2(start + 1, end, prefix + nums[start], nums);

            tmp = nums[i];
            nums[i] = nums[start];
            nums[start] = tmp;

        }

    }


    public static void main(String[] args) {

        Solution solution = new Solution();
        List<List<Integer>> res = solution.permute(new int[]{1, 2,3});

        for (List<Integer> list:res)
        {
            for (int i:list)
            {
                System.out.print(i+"\t");
            }
            System.out.println();
        }
    }
}
