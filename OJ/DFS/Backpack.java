
import java.util.Scanner;

/**
 * @date Created on 2018/8/19
 */
public class Backpack {

    public static int n, w;
    public static int[] v;

    public static int count;

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        while (in.hasNext()) {
            n = in.nextInt();
            w = in.nextInt();
            v = new int[n];
            for (int i = 0; i < n; i++) {
                v[i] = in.nextInt();
            }
            System.out.println(doJob());
        }
    }

    public static int doJob() {

        count = 0;
//        DFS(v, 0, 1);
        DFS2(0, 1);

        return count;
    }

    public static void DFS(int[] v, int index, int result) {
        if (index > n || result > w) {
            return;
        }
        if (index == n) {
            count++;
            return;
        }

        DFS(v, index + 1, result + v[index]);
        DFS(v, index + 1, result);

    }

    public static void DFS2(int t, int sum) {
        if (sum > w) {
            return;
        }

        if (t == n) {
            count++;
            return;
        }

        DFS2(t + 1, (sum + v[t]));

        DFS2(t + 1, sum);
    }

}
