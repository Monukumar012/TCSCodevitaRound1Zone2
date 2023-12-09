import java.util.*;


public class Main{

// 1st
    // static void solve(){
    //     int n=sc.nextInt();
    //     int data[][]=new int[n][3];
    //     for(int i=0;i<n;i++){
    //         data[i][0]=sc.nextInt(); // quantity
    //         data[i][1]=sc.nextInt();  // buy day
    //         data[i][2]=sc.nextInt();  // sell day
    //     }
    //     int m=sc.nextInt();
    //     int stockDayData[][]=new int[n][m];
    //     for(int i=0;i<n;i++){
    //         for(int j=0;j<m;j++){
    //             stockDayData[i][j]=sc.nextInt();
    //         }
    //     }
    //     int ansDay =sc.nextInt();
    //     long real=0,unreal=0;
    //     for(int i=0;i<n;i++){
    //         int q=data[i][0],bd=data[i][1],sd=data[i][2];
    //         if(bd>ansDay)continue;
    //         if(sd>ansDay || sd==0){
    //             // System.out.println(stockDayData[i][ansDay-1] + " -> "+ stockDayData[i][bd-1]);
    //             unreal+=q*1L*(stockDayData[i][ansDay-1]-stockDayData[i][bd-1]);
    //         }
    //         else{
    //             // System.out.println(stockDayData[i][ansDay-1] + " -> "+ stockDayData[i][bd-1]);
    //             real+=q*1L*(stockDayData[i][sd-1]-stockDayData[i][bd-1]);
    //         }
    //     }

    //     System.out.print(real);
    //     System.out.println();
    //     System.out.print(unreal);
    // }
    

//2nd
    // static void solve(String s, int a, int b){
    //     int n=s.length();
    //     char ch[]=s.toCharArray();
    //     Arrays.sort(ch);
    //     // System.out.println(new String(ch));
    //     for(char c:s.toCharArray()){
    //         if(c!='0' && c!='1'){
    //             System.out.print("INVALID");
    //             return;
    //         }
    //     }
    //     int cnta=0,cntb=0;
    //     for(int i=0;i<n;i++){
    //         if(s.charAt(i)!=ch[i])cnta++;  // for 01 
    //         if(s.charAt(i)!=ch[n-1-i])cntb++; //for 10
    //     }
    //     if(a==b){
    //         System.out.print(Math.min(cnta,cntb));
    //     }
    //     else if(a<b){
    //         System.out.print(cnta);
    //     }
    //     else{
    //         System.out.print(cntb);
    //     }
   
    // }

     

// 3rd
    public static SegmentTreeMin segMin;
    public static SegmentTreeMax segMax;
    static long dp[][];

    // for min id
    static long f(int i,int n, int p, int rate[], int ids[]){
        if(i>=n-1){
            int min = segMin.query(p,i,ids);
            return rate[min];
        }
        if(dp[i][p]!=-1)return dp[i][p];

        long notcut= f(i+1, n, p, rate, ids);
        int min = segMin.query(p,i,ids);
        long cut = (long)rate[min]+f(i+1, n, i+1, rate, ids);
        
        return dp[i][p]=Math.max(notcut, cut);
    }

    // for max id
    static long f2(int i,int n, int p, int rate[], int ids[]){
        if(i>=n-1){
            int max = segMax.query(p,i,ids);
            return rate[max];
        }
        if(dp[i][p]!=-1)return dp[i][p];

        long notcut= f2(i+1, n, p, rate, ids);
        int max = segMax.query(p,i,ids);

        long cut = (long)rate[max]+f2(i+1, n, i+1, rate, ids);
        
        return dp[i][p]=Math.max(notcut, cut);
    }

    static void solve(){
        int n=sc.nextInt();
        dp=new long[n][n];
        for(long d[]:dp)Arrays.fill(d,-1);
        int ids[]=new int[n];
        int rate[]=new int[n];
        for (int i = 0; i < n; i++) {
            String s = sc.next();
            String[] parts = s.split(":");

            int id = Integer.parseInt(parts[0].trim());
            int rating = Integer.parseInt(parts[1].trim());
            ids[i]=id;
            rate[i]=rating;
        }
        segMax=new SegmentTreeMax(n);
        segMin=new SegmentTreeMin(n);
        segMax.build(ids);
        segMin.build(ids);

        long ans1=f(0,n,0,rate,ids);
        for(long d[]:dp)Arrays.fill(d,-1);
        long ans2=f2(0,n,0,rate,ids);
        // long ans=tabu(rate,ids,n);
        System.out.print(Math.max(ans1,ans2));
        // System.out.println();
    }

    public static void main(String[] args) {
        // int t=sc.nextInt();
        // String arr[]=new String[t];
        // int ab[][]=new int[t][2];
        // for(int i=0;i<t;i++){
        //     arr[i]=sc.next();
        //     ab[i][0]=sc.nextInt();
        //     ab[i][1]=sc.nextInt();
        // }

        // for(int i=0;i<t;i++){
        //     solve(arr[i],ab[i][0],ab[i][1]);
        //     if(i!=t-1)System.out.println();
        // }
        solve();
    }
    // Mix xxx = new Mix();
    public static Scanner sc = new Scanner(System.in);

}

class SegmentTreeMax{
    public int segmentArr[];
    public int N;
    SegmentTreeMax(int n){
        N=n;
        segmentArr=new int[4*n+1];
    }

    public void build(int arr[]){
        build(0,0,N-1,arr);
    }

    public int query(int l, int r,int arr[]){
        return query(0,0,N-1,l,r,arr);
    }

    public void update(int i,int val){
        update(0,0,N-1,i,val);
    }

    public void build(int ind,int low, int high, int arr[]){
        if(low==high){
            segmentArr[ind]=low;
            return;
        }

        int mid=(low+high)/2;
        build(2*ind+1,low,mid,arr);
        build(2*ind+2,mid+1,high,arr);

        int ind1=segmentArr[2*ind+1];
        int ind2=segmentArr[2*ind+2];

        segmentArr[ind]=arr[ind1]>arr[ind2] ? ind1 : ind2;
    }

    public int query(int ind,int low, int high,int l, int r,int arr[]){
        // Complete Overlap [l   low   high   r]
        if(low>=l && high<=r)return segmentArr[ind];

        // No Overlap  [low   high  l   r]  || [l  r  low   high]
        if(r<low || l>high)return -1;

        // Partially overlap   [low  l  high   r]  || [l   low   r   high]
        int mid=low+(high-low)/2;
        int left=query(2*ind+1,low,mid,l,r,arr);
        int right = query(2*ind+2,mid+1,high,l,r,arr);
        
        if(left==-1)return right;
        if(right==-1)return left;
        return arr[left]>arr[right] ? left : right;
    }

    public void update(int ind, int low, int high, int i, int val){
        if(low==high){
            segmentArr[ind]=val;
            return;
        }

        int mid=low+(high-low)/2;

        // Given pos in left
        if(i<=mid) update(2*ind+1, low, mid, i, val);
        // Given pos in right
        else update(2*ind+2, mid+1, high, i, val);

        // Update root after updating [2*ind+1] or [2*ind+2]
        segmentArr[ind]=Math.max(segmentArr[2*ind+1], segmentArr[2*ind+2]);
    }

}

class SegmentTreeMin{
    public int segmentArr[];
    public int N;
    SegmentTreeMin(int n){
        N=n;
        segmentArr=new int[4*n+1];
    }

    public void build(int arr[]){
        build(0,0,N-1,arr);
    }

    public int query(int l, int r,int arr[]){
        return query(0,0,N-1,l,r,arr);
    }

    public void update(int i,int val){
        update(0,0,N-1,i,val);
    }

    public void build(int ind,int low, int high, int arr[]){
        if(low==high){
            segmentArr[ind]=low;
            return;
        }

        int mid=(low+high)/2;
        build(2*ind+1,low,mid,arr);
        build(2*ind+2,mid+1,high,arr);

        int ind1=segmentArr[2*ind+1];
        int ind2=segmentArr[2*ind+2];

        segmentArr[ind]=arr[ind1]<arr[ind2] ? ind1 : ind2;
    }

    public int query(int ind,int low, int high,int l, int r,int arr[]){
        // Complete Overlap [l   low   high   r]
        if(low>=l && high<=r)return segmentArr[ind];

        // No Overlap  [low   high  l   r]  || [l  r  low   high]
        if(r<low || l>high)return -1;

        // Partially overlap   [low  l  high   r]  || [l   low   r   high]
        int mid=low+(high-low)/2;
        int left=query(2*ind+1,low,mid,l,r,arr);
        int right = query(2*ind+2,mid+1,high,l,r,arr);

        if(left==-1)return right;
        if(right==-1)return left;
        return arr[left]<arr[right] ? left : right;
    }

    public void update(int ind, int low, int high, int i, int val){
        if(low==high){
            segmentArr[ind]=val;
            return;
        }

        int mid=low+(high-low)/2;

        // Given pos in left
        if(i<=mid) update(2*ind+1, low, mid, i, val);
        // Given pos in right
        else update(2*ind+2, mid+1, high, i, val);

        // Update root after updating [2*ind+1] or [2*ind+2]
        segmentArr[ind]=Math.max(segmentArr[2*ind+1], segmentArr[2*ind+2]);
    }

}
