
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 功能描述
 *
 * @since 2019-09-28
 */
class PermuteUnique {

    public List<List<Integer>> result = new ArrayList<>();
    public Set<String> set = new HashSet<>();

    public List<List<Integer>> permuteUnique(int[] nums) {
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
//            System.out.println(list);
//            if (!set.contains(list.toString())) {
//                List<Integer> r1 = new ArrayList<>(list);
//                result.add(r1);
//                set.add(list.toString());
//            }

            result.add(new ArrayList<>(list));
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

        // 1.1.2.2
        List<List<Integer>> res = new PermuteUnique().permuteUnique(new int[]{1, 1, 3});

        for (List<Integer> list : res) {
            for (int i : list) {
                System.out.print(i + "\t");
            }
            System.out.println();
        }

    }


}
