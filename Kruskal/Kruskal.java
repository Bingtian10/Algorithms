class Kruskal {
    class arrayComparator implements Comparator<int[]> {
        public int compare(int[] arr1, int[] arr2) {
            if(arr1[0] < arr2[0])
                return -1;
            else if(arr1[0] > arr2[0])
                return 1;
            else
                return 0;
        }
    }
    
    class UF {
        int[] uf_arr;
        //Initialize such that each tree points to itself.
        public UF(int n) {
            uf_arr = new int[n];
            for(int i = 0; i < n; i++) {
                uf_arr[i] = i;
            }
        }
        
        public int find(int x) {
            if(uf_arr[x] != x) {
                uf_arr[x] = find(uf_arr[x]);
                return uf_arr[x];
            }
            
            else {
                return x;
            }
        }
        
        public void union(int x, int y) {
            x = find(x);
            y = find(y);
            
            if(x == y)
                return;
            else
                uf_arr[y] = x;
        }
    }
    
    public int minCostConnectPoints(int[][] points) {
        PriorityQueue<int[]> pq = new PriorityQueue<int[]>(1000, new arrayComparator());
        //Compute all the edges weights in the graph
        for(int i = 0; i < points.length; i++) {
            for(int j = i+1; j < points.length; j++) {
                int val = Math.abs(points[i][0] - points[j][0]) + Math.abs(points[i][1] - points[j][1]);
                pq.add(new int[]{val, i, j});
            }
        }
        
        int ans = 0;
        int n = points.length;
        UF uf = new UF(n);
        //Kruskal algorithm
        while(!pq.isEmpty()) {
            int[] cur = pq.poll();
            int x = cur[1];
            int y = cur[2];
            if(uf.find(x) != uf.find(y)) {
                uf.union(uf.find(x), uf.find(y));
                ans += cur[0];
            }
        }
        return ans;
    }
}