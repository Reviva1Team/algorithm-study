package bbj;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import java.util.StringTokenizer;

public class bj13460 {
    private int min;
    private int n, m;
    private char[][] charArr;

    private static final int LIMIT = 10;

    class QueueNode {
        String status;
        Point red, blue;
        int arrow, count;
        public QueueNode(String status, Point red, Point blue, int arrow, int count) {
            this.status = status;
            this.red = red;
            this.blue = blue;
            this.arrow = arrow;
            this.count = count;
        }
        public String getStatus() { return status; }
        public Point getRed() { return red; }
        public Point getBlue() { return blue; }
        public int getArrow() { return arrow; }
        public int getCount() { return count; }
    }

    private void bfs(Queue<QueueNode> queue) {

        List<Integer> counts = new ArrayList<Integer>();
        
        // 이동할 네 가지 방향 정의 (상, 하, 좌, 우)
        int[] dx = {-1, 1, 0, 0};
        int[] dy = {0, 0, -1, 1};
        char[] ds = {'U', 'D', 'L', 'R'};

        while(queue.size() != 0) { 
            QueueNode qnode = queue.poll();
            String status = qnode.getStatus();
            Point red = qnode.getRed();
            Point blue = qnode.getBlue();
            int arrow = qnode.getArrow();
            int count = qnode.getCount();

            int rx = red.x;
            int ry = red.y;
            int bx = blue.x;
            int by = blue.y;
            boolean redgoal = false;
            boolean bluegoal = false;

            if (count > LIMIT) continue;
            if ((charArr[rx + dx[arrow]][ry + dy[arrow]] == '#' || rx + dx[arrow] == bx && ry + dy[arrow] == by)
                && (charArr[bx + dx[arrow]][by + dy[arrow]] == '#' || bx + dx[arrow] == rx && by + dy[arrow] == ry)) continue;


            while(!(charArr[rx + dx[arrow]][ry + dy[arrow]] == '#' || rx + dx[arrow] == bx && ry + dy[arrow] == by)
                || !(charArr[bx + dx[arrow]][by + dy[arrow]] == '#' || bx + dx[arrow] == rx && by + dy[arrow] == ry)) {
                
                // 빨간색 Check → 파란색 Check
                char[] order = {'R', 'B'};
                switch(arrow) {
                    case 0:     // → 상
                        if (ry > by) { order[0] = 'B'; order[1] = 'R'; }  // 파란색 Check → 빨간색 Check
                        break;
                    case 1:     // → 하
                        if (ry < by) { order[0] = 'B'; order[1] = 'R'; }  // 파란색 Check → 빨간색 Check
                        break;
                    case 2:     // → 좌
                        if (rx > bx) { order[0] = 'B'; order[1] = 'R'; }  // 파란색 Check → 빨간색 Check
                        break;
                    case 3:     // → 우
                        if (rx < bx) { order[0] = 'B'; order[1] = 'R'; }  // 파란색 Check → 빨간색 Check
                        break;
                }

                for (char target: order) {
                    switch(target) {
                        case 'R':
                            if (!(charArr[rx + dx[arrow]][ry + dy[arrow]] == '#' 
                                || rx + dx[arrow] == bx && ry + dy[arrow] == by)) {
                                rx = rx + dx[arrow];
                                ry = ry + dy[arrow];
                            }
                            break;
                        case 'B':
                            if (!(charArr[bx + dx[arrow]][by + dy[arrow]] == '#' 
                                || bx + dx[arrow] == rx && by + dy[arrow] == ry)) {
                                bx = bx + dx[arrow];
                                by = by + dy[arrow];
                            }
                            break;
                    }
                }

                if (charArr[rx][ry] == 'O') { redgoal = true; }
                if (charArr[bx][by] == 'O') { bluegoal = true; }
            }

            //System.out.println(String.format("[%s] : red (%d, %d) → %b, blue (%d, %d) → %b / count : %d", status + ds[arrow], rx, ry, redgoal, bx, by, bluegoal, count));
            
            if (!bluegoal) {
                if (charArr[bx + dx[arrow]][by + dy[arrow]] == 'O') continue;   // 한번 더 동일한 방향으로 굴렸을 때, 파란공이 빠져나갈 경우
                if (redgoal) counts.add(count);
                else {
                    for (int i = 0; i < 4; i++) {
                        if (arrow != i) queue.add(new QueueNode(status + ds[arrow], new Point(rx, ry), new Point(bx, by), i, count + 1));
                    }
                }
            }
        }

        min = counts.isEmpty() ? -1 : Collections.min(counts);
        if (min > LIMIT) min = -1;
        System.out.println(min);
    }

    public void solution() throws Exception {

        Queue<QueueNode> queue = new LinkedList<QueueNode>();
        Point red = new Point();
        Point blue = new Point();

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer num = new StringTokenizer(br.readLine());
        n = Integer.parseInt(num.nextToken());
        m = Integer.parseInt(num.nextToken());

        charArr = new char[n][m];
        for (int i = 0; i < n; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            String line = st.nextToken();
            for (int j = 0; j < m; j++) {
                charArr[i][j] = line.trim().charAt(j);
                if (charArr[i][j] == 'R') red = new Point(i, j);
                if (charArr[i][j] == 'B') blue = new Point(i, j);
            }
        }

        for (int i = 0; i < 4; i++) {
            queue.add(new QueueNode("", red, blue, i, 1));
        }

        bfs(queue);
    }

    public static void main(String[] args) throws Exception {
        new bj13460().solution();
    }
}
