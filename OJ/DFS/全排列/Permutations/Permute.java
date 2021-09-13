
import java.util.ArrayList;
import java.util.List;

/**
 * 功能描述
 *
 * @since 2019-09-28
 */
class Permute {

    public List<List<Integer>> result = new ArrayList<>();

    public List<List<Integer>> permute(int[] nums) {

        trace(nums, 0, new ArrayList<>(nums.length));

        return result;
    }

    /**
     * @param nums  原始数组
     * @param index 规定是index而不是0为数组num的开头位置
     * @param list
     */
    public void trace(int[] nums, int index, List<Integer> list) {
        if (index == nums.length) {
            //递归结束，需要保存结果了
            List<Integer> r1 = new ArrayList<>(list);
            result.add(r1);
            return;
        }
        int tmp;
        for (int i = index; i < nums.length; i++) {
            //轮流做第一
            tmp = nums[i];
            nums[i] = nums[index];
            nums[index] = tmp;

            list.add(nums[index]);
            trace(nums, index + 1, list);
            list.remove(list.size() - 1);

            //做完第一还回到原位置
            tmp = nums[i];
            nums[i] = nums[index];
            nums[index] = tmp;
        }
    }


    public static void main(String[] args) {

        List<List<Integer>> res = new Permute().permute(new int[]{1, 2, 3});

        for (List<Integer> list : res) {
            for (int i : list) {
                System.out.print(i + "\t");
            }
            System.out.println();
        }

    }
}
