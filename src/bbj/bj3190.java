package bbj;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.StringTokenizer;

public class bj3190 {
    private int n, k, l;

    class SnakeNode {
        Point snake;
        int arrow;
        public SnakeNode(Point snake, int arrow) {
            this.snake = snake;
            this.arrow = arrow;
        }
        public Point getSnake() { return snake; }
        public int getArrow() { return arrow; }
        public void setSnake(Point snake) { this.snake = snake; }
        public void setArrow(int arrow) { this.arrow = arrow; }
    }

    class RotateNode {
        int t;
        char rotate;
        public RotateNode(int t, char rotate) {
            this.t = t;
            this.rotate = rotate;
        }
        public int getTime() { return t; }
        public char getRotate() { return rotate; }
    }

    class QueueNode {
        int index, arrow;
        public QueueNode(int index, int arrow) {
            this.index = index;
            this.arrow = arrow;
        }
        public int getIndex() { return index; }
        public int getArrow() { return arrow; }
        public void setIndex(int index) { this.index = index; }
        public void setArrow(int arrow) { this.arrow = arrow; }
    }

    private static boolean snakeCheck(List<SnakeNode> snakeNodes, int i, int j) {
        for (SnakeNode node : snakeNodes) {
            Point snake = node.getSnake();
            if (snake.x == i && snake.y == j) return true;
        }
        return false;
    }

    private static boolean appleCheck(List<Point> appleNodes, int i, int j) {
        for (Point apple : appleNodes) {
            if (apple.x == i && apple.y == j) return true;
        }
        return false;
    }

    private void run(List<Point> appleNodes, List<RotateNode> rotateNodes) {

        List<SnakeNode> snakeNodes = new ArrayList<SnakeNode>();

        // 이동할 네 가지 방향 정의 (상, 하, 좌, 우)
        int[] dx = {-1, 1, 0, 0};
        int[] dy = {0, 0, -1, 1};

        snakeNodes.add(new SnakeNode(new Point(0, 0), 3));  // 첫 시작은 오른쪽

        int time = 0;
        while (true) {

            Point head = snakeNodes.get(0).getSnake();
            int size = snakeNodes.size() - 1;

            SnakeNode inc = new SnakeNode(new Point(0, 0), 0);
            int appleindex = 0;
            boolean check = false;

            for (Point apple : appleNodes) {
                if (apple.x == head.x && apple.y == head.y) {
                    Point tail = snakeNodes.get(size).getSnake();
                    int arrow = snakeNodes.get(size).getArrow();    // (꼬리)에서 방향 정보를 가져옴
                    inc = new SnakeNode(new Point(tail.x, tail.y), arrow);
                    appleindex = appleNodes.indexOf(apple);
                    check = true;
                }
            }

            // 이전의 방향정보를 다음 노드로 이관 (Shift 처리)
            for (int i = snakeNodes.size() - 1; i > 0; i--) {
                snakeNodes.get(i).setArrow(snakeNodes.get(i-1).getArrow());     
            }

            for (RotateNode retateNode : rotateNodes) {
                int t = retateNode.getTime();
                if (time == t) {
                    char rotate = retateNode.getRotate();
                    int arrow = snakeNodes.get(0).getArrow();    // (머리)에서 방향 정보를 가져옴
                    //System.out.println(String.format("%s %d ", rotate, arrow));
                    switch(rotate) {
                        case 'L':
                            if (arrow == 0) arrow = 2;  // ↑ (상) / ← (좌)
                            else if (arrow == 1) arrow = 3;  // ↓ (하) / → (우)
                            else if (arrow == 2) arrow = 1;  // ← (좌) / ↓ (하)
                            else if (arrow == 3) arrow = 0;  // → (우) / ↑ (상)
                            break;
                        case 'D':
                            if (arrow == 0) arrow = 3;  // ↑ (상) / → (우)
                            else if (arrow == 1) arrow = 2;  // ↓ (하) / ← (좌)
                            else if (arrow == 2) arrow = 0;  // ← (좌) / ↑ (상)
                            else if (arrow == 3) arrow = 1;  // → (우) / ↓ (하)
                            break;
                    }
                    //System.out.println(String.format("%d ", arrow));
                    snakeNodes.get(0).setArrow(arrow);
                }
            }

            // 이동하기
            for (SnakeNode snakeNode : snakeNodes) {
                Point snake = snakeNode.getSnake();
                int arrow = snakeNode.getArrow();

                snakeNode.setSnake(new Point(snake.x + dx[arrow], snake.y + dy[arrow]));
                snakeNode.setArrow(arrow);
            }

            if (check) {
                snakeNodes.add(inc);
                appleNodes.remove(appleindex);
            }
            
            time++;

            int headx = snakeNodes.get(0).getSnake().x;
            int heady = snakeNodes.get(0).getSnake().y;

            boolean dead = false;
            for (int i = 1; i < snakeNodes.size(); i++) {
                int targetx = snakeNodes.get(i).getSnake().x;
                int targety = snakeNodes.get(i).getSnake().y;
                int arrow = snakeNodes.get(i).getArrow();
                if (headx == targetx - dx[arrow] && heady == targety - dy[arrow]) dead = true;
            }

            //System.out.println(String.format("head → (%d, %d), dead → %b", headx, heady, dead));
            if (headx < 0 || headx >= n || heady < 0 || heady >= n || dead) break;

            // System.out.println(String.format("time → %d", time));
            // for (int i = 0; i < n; i++) {
            //     for (int j = 0; j < n; j++) {
            //         if (snakeCheck(snakeNodes, i, j)) System.out.print("■ ");
            //         else if (appleCheck(appleNodes, i, j)) System.out.print("★ ");
            //         else System.out.print("□ ");
            //     }
            //     System.out.println();
            // }
        }

        System.out.println(time);
    }

    public void solution() throws Exception {

        List<Point> appleNodes = new ArrayList<Point>();
        List<RotateNode> rotateNodes = new ArrayList<RotateNode>();

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer num1 = new StringTokenizer(br.readLine());
        n = Integer.parseInt(num1.nextToken());
        StringTokenizer num2 = new StringTokenizer(br.readLine());
        k = Integer.parseInt(num2.nextToken());

        for (int i = 0; i < k; i++) {
            StringTokenizer line1 = new StringTokenizer(br.readLine());
            int x = Integer.parseInt(line1.nextToken()) - 1;
            int y = Integer.parseInt(line1.nextToken()) - 1;
            appleNodes.add(new Point(x, y));
        }
        
        StringTokenizer num3 = new StringTokenizer(br.readLine());
        l = Integer.parseInt(num3.nextToken());

        for (int i = 0; i < l; i++) {
            StringTokenizer line2 = new StringTokenizer(br.readLine());
            int t = Integer.parseInt(line2.nextToken());
            String rotate = line2.nextToken();
            rotateNodes.add(new RotateNode(t, rotate.charAt(0)));
        }

        run(appleNodes, rotateNodes);
    }

    public static void main(String[] args) throws Exception {
        new bj3190().solution();
    }
}