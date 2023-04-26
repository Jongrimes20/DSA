package Project3;

public class DisjointSet {
    private int s[];

    /*
     * Constructor
     */
    public DisjointSet(int numElements) {
        s = new int[numElements];
        for (int i=0; i < s.length; i++) {
            s[i] = -1;
        }
    }

    /* Union by rank 
     * @param root1 -the root of set 1
     * @param root2 - the root of set 2
     */
    public void union(int root1, int root2) {
        if (s[root2] < s[root1]) {
            s[root1] = root2;
        }else {
            if (s[root1] == s[root2]) {
                s[root1]--;
            }
            s[root2] = root1;
        }
    }

    /* Find (w/ path compression)
     * @param x - the ele being looked for
     * @return the set containg x
     */
    public int find(int x) {
        if (s[x] < 0){
            return x;
        }
        else{ return s[x] = find(s[x]); }
    }
}
