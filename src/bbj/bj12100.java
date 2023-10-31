package bbj;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.StringTokenizer;

public class bj12100 {
    private int max;
    private int n;
    
    private static final int LIMIT = 5;

    class QueueNode {
        String status;
        int arrow, count;
        int[][] block;
        public QueueNode(String status, int arrow, int count, int[][] block) {
            this.status = status;
            this.arrow = arrow;
            this.count = count;
            this.block = Arrays.stream(block).map(int[]::clone).toArray(int[][]::new);
        }
        public String getStatus() { return status; }
        public int getArrow() { return arrow; }
        public int getCount() { return count; }
        public int[][] getBlock() { return block; }
    }

    private static int max(int[][] arrays) {
        return Arrays.stream(arrays).flatMapToInt(Arrays::stream).max().orElseThrow();
    }

    private void bfs(Queue<QueueNode> queue) {

        Set<Integer> maxs = new HashSet<Integer>();
        
        // 이동할 네 가지 방향 정의 (상, 하, 좌, 우)
        int[] dx = {-1, 1, 0, 0};
        int[] dy = {0, 0, -1, 1};
        char[] ds = {'U', 'D', 'L', 'R'};

        while(queue.size() != 0) { 
            QueueNode qnode = queue.poll();
            String status = qnode.getStatus();
            int arrow = qnode.getArrow();
            int count = qnode.getCount();
            int[][] block = qnode.getBlock();
            int[][] visit = new int[n][n];

            if (count > LIMIT)  {
                maxs.add(max(block));
                continue;
            }

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                        switch(arrow) {
                            case 0:     // → 상
                                if (j + dx[arrow] >= 0) {
                                    //System.out.println(String.format("[%s] origin (%d, %d) : %d → new (%d, %d) : %d", ds[arrow], j, i, block[j][i], j + dx[arrow], i + dy[arrow], block[j + dx[arrow]][i + dy[arrow]]));
                                    if (block[j + dx[arrow]][i + dy[arrow]] == block[j][i] && block[j][i] != 0 && visit[j + dx[arrow]][i + dy[arrow]] == 0) {
                                        block[j + dx[arrow]][i + dy[arrow]] = block[j][i] * 2;
                                        block[j][i] = 0;
                                        visit[j + dx[arrow]][i + dy[arrow]] = 1;
                                        j -= 1;
                                        continue;
                                    }
                                    if (block[j + dx[arrow]][i + dy[arrow]] == 0 && block[j][i] != 0) {
                                        block[j + dx[arrow]][i + dy[arrow]] = block[j][i];
                                        block[j][i] = 0;
                                        j -= 2;
                                        continue;
                                    }
                                }
                                break;
                            case 1:     // → 하
                                if (n - j - 1 + dx[arrow] < n) {
                                    //System.out.println(String.format("[%s] origin (%d, %d) : %d → new (%d, %d) : %d", ds[arrow], n - j - 1, n - i - 1, block[n - j - 1][n - i - 1], n - j - 1 + dx[arrow], n - i - 1 + dy[arrow], block[n - j - 1 + dx[arrow]][n - i - 1 + dy[arrow]]));
                                    if (block[n - j - 1 + dx[arrow]][n - i - 1 + dy[arrow]] == block[n - j - 1][n - i - 1] && block[n - j - 1][n - i - 1] != 0 && visit[n - j - 1 + dx[arrow]][n - i - 1 + dy[arrow]] == 0) {
                                        block[n - j - 1 + dx[arrow]][n - i - 1 + dy[arrow]] = block[n - j - 1][n - i - 1] * 2;
                                        block[n - j - 1][n - i - 1] = 0;
                                        visit[n - j - 1 + dx[arrow]][n - i - 1 + dy[arrow]] = 1;
                                        j -= 1;
                                        continue;
                                    }
                                    if (block[n - j - 1 + dx[arrow]][n - i - 1 + dy[arrow]] == 0 && block[n - j - 1][n - i - 1] != 0) {
                                        block[n - j - 1 + dx[arrow]][n - i - 1 + dy[arrow]] = block[n - j - 1][n - i - 1];
                                        block[n - j - 1][n - i - 1] = 0;
                                        j -= 2;
                                        continue;
                                    }
                                }
                                break;
                            case 2:     // → 좌
                                if (j + dy[arrow] >= 0) {
                                    //System.out.println(String.format("[%s] origin (%d, %d) : %d → new (%d, %d) : %d", ds[arrow], i, j, block[i][j], i + dx[arrow], j + dy[arrow], block[i + dx[arrow]][j + dy[arrow]]));
                                    if (block[i + dx[arrow]][j + dy[arrow]] == block[i][j] && block[i][j] != 0 && visit[i + dx[arrow]][j + dy[arrow]] == 0) {
                                        block[i + dx[arrow]][j + dy[arrow]] = block[i][j] * 2;
                                        block[i][j] = 0;
                                        visit[i + dx[arrow]][j + dy[arrow]] = 1;
                                        j -= 1;
                                        continue;
                                    }
                                    if (block[i + dx[arrow]][j + dy[arrow]] == 0 && block[i][j] != 0) {
                                        block[i + dx[arrow]][j + dy[arrow]] = block[i][j];
                                        block[i][j] = 0;
                                        j -= 2;
                                        continue;
                                    }
                                }
                                break;
                            case 3:     // → 우
                                if (n - j - 1 + dy[arrow] < n) {
                                    //System.out.println(String.format("[%s] origin (%d, %d) : %d → new (%d, %d) : %d", ds[arrow], n - i - 1, n - j - 1, block[n - i - 1][n - j - 1], n - i - 1 + dx[arrow], n - j - 1 + dy[arrow], block[n - i - 1 + dx[arrow]][n - j - 1 + dy[arrow]]));
                                    if (block[n - i - 1 + dx[arrow]][n - j - 1 + dy[arrow]] == block[n - i - 1][n - j - 1] && block[n - i - 1][n - j - 1] != 0 && visit[n - i - 1 + dx[arrow]][n - j - 1 + dy[arrow]] == 0) {
                                        block[n - i - 1 + dx[arrow]][n - j - 1 + dy[arrow]] = block[n - i - 1][n - j - 1] * 2;
                                        block[n - i - 1][n - j - 1] = 0;
                                        visit[n - i - 1 + dx[arrow]][n - j - 1 + dy[arrow]] = 1;
                                        j -= 1;
                                        continue;
                                    }
                                    if (block[n - i - 1 + dx[arrow]][n - j - 1 + dy[arrow]] == 0 && block[n - i - 1][n - j - 1] != 0) {
                                        block[n - i - 1 + dx[arrow]][n - j - 1 + dy[arrow]] = block[n - i - 1][n - j - 1];
                                        block[n - i - 1][n - j - 1] = 0;
                                        j -= 2;
                                        continue;
                                    }
                                }
                                break;
                    }
                }
            }

            // if ("R".equals(status + ds[arrow]) || "RD".equals(status + ds[arrow]) || "RDL".equals(status + ds[arrow])) {
            //     System.out.println(String.format("[%s] count : %d", status + ds[arrow], count));
            //     for (int i = 0; i < n; i++) {
            //         for (int j = 0; j < n; j++) {
            //             if (visit[i][j] == 1) System.out.print(String.format("(%d) ", block[i][j]));
            //             else System.out.print(String.format("%d ", block[i][j]));
            //         }
            //         System.out.println();
            //     }
            // }

            // if (max(block) == 128) {
            //     System.out.println(String.format("[%s] count : %d", status + ds[arrow], count));
            //     for (int i = 0; i < n; i++) {
            //         for (int j = 0; j < n; j++) {
            //             System.out.print(String.format("%d ", block[i][j]));
            //         }
            //         System.out.println();
            //     }
            // }

            for (int i = 0; i < 4; i++) {   // → 같은 방향 한번 이상 진행 가능하게 하기
                queue.add(new QueueNode(status + ds[arrow], i, count + 1, block));
            }
        }

        //System.out.println(maxs);

        max = Collections.max(maxs);
        System.out.println(max);
    }

    public void solution() throws Exception {

        Queue<QueueNode> queue = new LinkedList<QueueNode>();
        
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer num = new StringTokenizer(br.readLine());

        n = Integer.parseInt(num.nextToken());

        int[][] blockArr = new int[n][n];
        for (int i = 0; i < n; i++) {
            StringTokenizer line = new StringTokenizer(br.readLine());
            for (int j = 0; j < n; j++) {
                blockArr[i][j] = Integer.parseInt(line.nextToken());
            }
        }

        for (int i = 0; i < 4; i++) {
            queue.add(new QueueNode("", i, 1, blockArr));
        }

        bfs(queue);
    }

    public static void main(String[] args) throws Exception {
        new bj12100().solution();
    }
}