package year2024.month04.day15;

import java.io.*;
import java.util.*;

public class BOJ2933 {
	private static int r, c;
	private static char[][] cave;
	private static boolean[][] visited;
	
	private static int[] dr = {-1, 1, 0, 0};
	private static int[] dc = {0, 0, -1, 1};
	private static List<int[]> upper = new ArrayList<>();
	private static List<int[]> lower = new ArrayList<>();
	private static Queue<int[]> q = new LinkedList<>();

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringBuilder sb = new StringBuilder();
		StringTokenizer st;
		
		st = new StringTokenizer(br.readLine());
		r = Integer.parseInt(st.nextToken());
		c = Integer.parseInt(st.nextToken());
		cave = new char[r][c];
		visited = new boolean[r][c];
		
		for(int i = 0; i < r; i++) {
			cave[i] = br.readLine().toCharArray();
		}
		
		int n = Integer.parseInt(br.readLine());
		st = new StringTokenizer(br.readLine());
		for(int i = 0; i < n; i++) {
			int h = Integer.parseInt(st.nextToken());
			int[] result = operation((r - h), (i % 2 == 0));
			fallCluster(result);
		}
		
		for(int i = 0; i < r; i++) {
			for(int j = 0; j < c; j++) {
				sb.append(cave[i][j]);
			}
			sb.append("\n");
		}
		
		System.out.println(sb.toString());
		br.close();
	}
	
	public static int[] operation(int idx, boolean isLeft) {
		int i = isLeft ? 0 : c - 1;
		int end = isLeft ? c - 1 : 0;
		int di = isLeft ? 1 : -1;
		
		while(true) {
			if(cave[idx][i] == 'x') {
				cave[idx][i] = '.';
				return new int[] {idx, i};
			}
			if(i == end) break;
			i += di;
		}
		
		return null;
	}
	
	public static void fallCluster(int[] result) {
		if(result == null) return;
		
		int d = 101;
		for(int i = 0; i < r; i++) {
			Arrays.fill(visited[i], false);
		}
		
		lower.clear();
		upper.clear();
		for(int i = 0; i < c; i++) {
			if(cave[r - 1][i] == '.' || visited[r - 1][i]) continue;
			bfs(r - 1, i, lower);
		}
		
		for(int i = 0; i < 4; i++) {
			int nowRow = result[0] + dr[i];
			int nowCol = result[1] + dc[i];
			if(nowRow < 0 || nowRow >= r || nowCol < 0 || nowCol >= c) continue;
			if(cave[nowRow][nowCol] == '.' || visited[nowRow][nowCol]) continue;
			bfs(nowRow, nowCol, upper);
		}
		
		for(int i = 0; i < c; i++) {
			int lowerMin = r;
			for(int[] pos : lower) {
				if(pos[1] == i) {
					lowerMin = Math.min(lowerMin, pos[0]);
				}
			}
			int upperMax = -1;
			for(int[] pos : upper) {
				if(pos[1] == i) {
					upperMax = Math.max(upperMax, pos[0]);
				}
			}
			
			if(upperMax == -1) continue;
			d = Math.min(d, lowerMin - upperMax - 1);
		}

		if(d == 0 || d == 101) return;
		
		Collections.sort(upper, (o1, o2)->(o2[0] - o1[0]));
		
		for(int[] pos : upper) {
			cave[pos[0] + d][pos[1]] = 'x';
			cave[pos[0]][pos[1]] = '.';
		}
	}
	
	public static void bfs(int row, int col, List<int[]> cluster) {
		int[] start = new int[] {row, col};
		q.add(start);
		cluster.add(start);
		visited[row][col] = true;
		
		while(!q.isEmpty()) {
			int[] now = q.poll();
			for(int i = 0; i < 4; i++) {
				int nextRow = now[0] + dr[i];
				int nextCol = now[1] + dc[i];
				if(nextRow < 0 || nextRow >= r || nextCol < 0 || nextCol >= c) continue;
				if(cave[nextRow][nextCol] == '.' || visited[nextRow][nextCol]) continue;
				
				int[] next = new int[] {nextRow, nextCol};
				q.add(next);
				cluster.add(next);
				visited[nextRow][nextCol] = true;
			}
		}
	}
}
