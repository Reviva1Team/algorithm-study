package bbj;

import java.lang.Math;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.StringTokenizer;

public class bj23289 {
    private int r, c, w;

    private static int k;

    class HeaterNode {
        Point heater;
        int arrow;
        public HeaterNode(Point heater, int arrow) {
            this.heater = heater;
            this.arrow = arrow;
        }
        public Point getHeater() { return heater; }
        public int getArrow() { return arrow; }
    }

    class WallNode {
        Point start, end;
        public WallNode(Point start, Point end) {
            this.start = start;
            this.end = end;
        }
        public Point getStart() { return start; }
        public Point getEnd() { return end; }
    }

    private static boolean check(List<Point> monitorNodes, int[][] temper) {
        for (Point monitor : monitorNodes) {
            if (temper[monitor.x][monitor.y] < k) {
                return true;
            }
        }
        return false;
    }

    private static boolean wall(List<WallNode> wallNodes, int startx, int starty, int endx, int endy) {
        for (WallNode node : wallNodes) {
            Point start = node.getStart();
            Point end = node.getEnd();
            if (start.x == startx && start.y == starty && end.x == endx && end.y == endy) return true;
            if (end.x == startx && end.y == starty && start.x == endx && start.y == endy) return true;
        }
        return false;
    }

    private void dfs(List<HeaterNode> heaterNodes, List<Point> monitorNodes, List<WallNode> wallNodes) {

        int[][] temper = new int[r][c];
        int count = 0;

        // 이동할 네 가지 방향 정의 (오른쪽 : 1 → 0, 왼쪽 : 2 → 1, 위 : 3 → 2, 아래 : 4 → 3)
        int[] dx = {0, 0, -1, 1};
        int[] dy = {1, -1, 0, 0};

        while (check(monitorNodes, temper)) {
            // 1. 집에 있는 모든 온풍기에서 바람이 한 번 나옴
            for (HeaterNode node : heaterNodes) {
                Point main = node.getHeater();
                int arrow = node.getArrow();

                int hx = main.x;
                int hy = main.y;
                int[][] visit = new int[r][c];

                Queue<HeaterNode> queue1 = new LinkedList<HeaterNode>();
                for (int i = 0; i < 5; i++) {
                    hx = hx + dx[arrow];
                    hy = hy + dy[arrow];
                    if (hx >= 0 && hx < r && hy >= 0 && hy < c) {
                        temper[hx][hy] = temper[hx][hy] + 5 - i;    // Default : 5 → 4 → 3 → 2 → 1
                        visit[hx][hy] = 1;
                        if (5 - i > 1) {    // Queue : 5 → 4 → 3 → 2
                            if (arrow == 0 || arrow == 1) { // → 우, 좌
                                if (!(wall(wallNodes, hx, hy, hx - 1, hy))) queue1.add(new HeaterNode(new Point(hx - 1, hy), arrow));
                                if (!(wall(wallNodes, hx, hy, hx + 1, hy))) queue1.add(new HeaterNode(new Point(hx + 1, hy), arrow));
                            } else {    // → 상, 하
                                if (!(wall(wallNodes, hx, hy, hx, hy - 1))) queue1.add(new HeaterNode(new Point(hx, hy - 1), arrow));
                                if (!(wall(wallNodes, hx, hy, hx, hy + 1))) queue1.add(new HeaterNode(new Point(hx, hy + 1), arrow));
                            }
                        }
                        if (wall(wallNodes, hx, hy, hx + dx[arrow], hy + dy[arrow])) break;  // 벽에 막혀있으면 빠져나온다
                    }
                }
                // System.out.println(String.format("1st → %d", count));
                // for (int i = 0; i < r; i++) {
                //     for (int j = 0; j < c; j++) {
                //         System.out.print(String.format("%d ", temper[i][j]));
                //     }
                //     System.out.println();
                // }
                
                while(queue1.size() != 0) {
                    HeaterNode qnode = queue1.poll();
                    Point sub = qnode.getHeater();
                    arrow = qnode.getArrow();

                    hx = sub.x;
                    hy = sub.y;

                    if (hx + dx[arrow] >= 0 && hx + dx[arrow] < r && hy + dy[arrow] >= 0 && hy + dy[arrow] < c 
                        && !(wall(wallNodes, hx, hy, hx + dx[arrow], hy + dy[arrow]))
                        && visit[hx + dx[arrow]][hy + dy[arrow]] == 0) {
                        hx = hx + dx[arrow];
                        hy = hy + dy[arrow];
                        if (arrow == 0 || arrow == 1) { // → 우, 좌
                            int value = 5 - Math.abs(main.y + dy[arrow] - hy);
                            temper[hx][hy] = temper[hx][hy] + value;
                            if (value > 1) {
                                if (!(wall(wallNodes, hx, hy, hx - 1, hy))) queue1.add(new HeaterNode(new Point(hx - 1, hy), arrow));
                                if (!(wall(wallNodes, hx, hy, hx + 1, hy))) queue1.add(new HeaterNode(new Point(hx + 1, hy), arrow));
                                queue1.add(new HeaterNode(new Point(hx, hy), arrow));
                            }
                        } else {    // → 상, 하
                            int value = 5 - Math.abs(main.x + dx[arrow] - hx);
                            temper[hx][hy] = temper[hx][hy] + value;
                            if (value > 1) {
                                if (!(wall(wallNodes, hx, hy, hx, hy - 1))) queue1.add(new HeaterNode(new Point(hx, hy - 1), arrow));
                                if (!(wall(wallNodes, hx, hy, hx, hy + 1))) queue1.add(new HeaterNode(new Point(hx, hy + 1), arrow));
                                queue1.add(new HeaterNode(new Point(hx, hy), arrow));
                            }
                        }
                        visit[hx][hy] = 1;  // 이미 방문함
                    }
                }
                // System.out.println(String.format("2nd → %d", count));
                // for (int i = 0; i < r; i++) {
                //     for (int j = 0; j < c; j++) {
                //         System.out.print(String.format("%d ", temper[i][j]));
                //     }
                //     System.out.println();
                // }
            }

            // System.out.println(String.format("init → %d", count));
            // for (int i = 0; i < r; i++) {
            //     for (int j = 0; j < c; j++) {
            //         System.out.print(String.format("%d ", temper[i][j]));
            //     }
            //     System.out.println();
            // }

            // 2. 온도가 조절됨
            Queue<HeaterNode> queue2 = new LinkedList<HeaterNode>();
            for (int i = 0; i < r; i++) {
                queue2.add(new HeaterNode(new Point(i, 0), 0));   // → 오른쪽(우)
            }
            for (int j = 0; j < c; j++) {
                queue2.add(new HeaterNode(new Point(0, j), 3));   // → 아래쪽(하)
            }

            int[][] calc = new int[r][c];
            while(queue2.size() != 0) { 
                HeaterNode qnode = queue2.poll();
                Point heater = qnode.getHeater();
                int arrow = qnode.getArrow();

                int hx = heater.x;
                int hy = heater.y;

                if (hx + dx[arrow] >= 0 && hx + dx[arrow] < r && hy + dy[arrow] >= 0 && hy + dy[arrow] < c) {
                    if (!(wall(wallNodes, hx, hy, hx + dx[arrow], hy + dy[arrow]))) {
                        if (temper[hx][hy] < temper[hx + dx[arrow]][hy + dy[arrow]]) {
                            int control = (temper[hx + dx[arrow]][hy + dy[arrow]] - temper[hx][hy]) / 4;
                            calc[hx][hy] = calc[hx][hy] + control;
                            calc[hx + dx[arrow]][hy + dy[arrow]] = calc[hx + dx[arrow]][hy + dy[arrow]] - control;
                        }
                        if (temper[hx][hy] > temper[hx + dx[arrow]][hy + dy[arrow]]) {
                            int control = (temper[hx][hy] - temper[hx + dx[arrow]][hy + dy[arrow]]) / 4;
                            calc[hx + dx[arrow]][hy + dy[arrow]] = calc[hx + dx[arrow]][hy + dy[arrow]] + control;
                            calc[hx][hy] = calc[hx][hy] - control;
                        }
                    }
                    queue2.add(new HeaterNode(new Point(hx + dx[arrow], hy + dy[arrow]), arrow));
                }
            }
            
            // → 재계산
            for (int i = 0; i < r; i++) {
                for (int j = 0; j < c; j++) {
                    temper[i][j] = temper[i][j] + calc[i][j];
                }
            }

            // System.out.println(String.format("control → %d", count));
            // for (int i = 0; i < r; i++) {
            //     for (int j = 0; j < c; j++) {
            //         System.out.print(String.format("%d ", temper[i][j]));
            //     }
            //     System.out.println();
            // }

            // 3. 온도가 1 이상인 가장 바깥쪽 칸의 온도가 1씩 감소
            for (int i = 0; i < r; i++) {
                for (int j = 0; j < c; j++) {
                    if ((i == 0 || i == r - 1 || j == 0 || j == c - 1) && temper[i][j] > 0) temper[i][j] -= 1;
                }
            }
            
            // 4. 초콜릿을 하나 먹는다.
            count++;

            // System.out.println(String.format("outlier → %d", count));
            // for (int i = 0; i < r; i++) {
            //     for (int j = 0; j < c; j++) {
            //         System.out.print(String.format("%d ", temper[i][j]));
            //     }
            //     System.out.println();
            // }

            if (count > 100) {
                break;
            }
        }

        System.out.println(String.format("final → %d", count));
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                System.out.print(String.format("%d ", temper[i][j]));
            }
            System.out.println();
        }

        System.out.println(count);
    }

    public void solution() throws Exception {

        List<HeaterNode> heaterNodes = new ArrayList<HeaterNode>();
        List<Point> monitorNodes = new ArrayList<Point>();
        List<WallNode> wallNodes = new ArrayList<WallNode>();

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer num1 = new StringTokenizer(br.readLine());
        r = Integer.parseInt(num1.nextToken());
        c = Integer.parseInt(num1.nextToken());
        k = Integer.parseInt(num1.nextToken());

        for (int i = 0; i < r; i++) {
            StringTokenizer line1 = new StringTokenizer(br.readLine());
            for (int j = 0; j < c; j++) {
                int target = Integer.parseInt(line1.nextToken());
                if (target >= 1 && target <= 4) heaterNodes.add(new HeaterNode(new Point(i, j), target-1));
                if (target == 5) monitorNodes.add(new Point(i, j));
            }
        }

        StringTokenizer num2 = new StringTokenizer(br.readLine());
        w = Integer.parseInt(num2.nextToken());

        for (int i = 0; i < w; i++) {
            StringTokenizer line2 = new StringTokenizer(br.readLine());
            int x = Integer.parseInt(line2.nextToken()) - 1;
            int y = Integer.parseInt(line2.nextToken()) - 1;
            int t = Integer.parseInt(line2.nextToken());
            if (t == 1) wallNodes.add(new WallNode(new Point(x, y), new Point(x, y+1)));
            else wallNodes.add(new WallNode(new Point(x, y), new Point(x-1, y)));
        }

        dfs(heaterNodes, monitorNodes, wallNodes);
    }

    public static void main(String[] args) throws Exception {
        new bj23289().solution();
    }
}