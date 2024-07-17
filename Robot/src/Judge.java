public class Judge {
    public  int doJudge(int[][] states){
        int r=0;
        for(int i=0;i<19;i++){
            for(int j=0;j<19;j++){
                r=judgeHorizontal(states,i,j);//水平
                if(r>0) return r;
                r=judgeVertal(states,i,j);//垂直
                if(r>0) return r;
                r=judgeLeftLean(states,i,j);//左斜线
                if(r>0) return r;
                r=judgeRightLean(states,i,j);//右斜线
                if(r>0) return r;
            }
        }
        return 0;
    }

    private int judgeRightLean(int[][] states, int i, int j) {
        int color=states[i][j];
        if(color==0) return 0;
        int n1=0;
        for(int t=i-1;t>=0;t--){
            if(states[t][j]==color) n1+=1;
            else break;
        }
        int n2=0;
        for(int t=i+1;t<19;t++){
            if(states[t][j]==color) n2+=1;
            else break;
        }
        if(n1+n2+1==5) return color;
        else return 0;
    }

    private int judgeLeftLean(int[][] states, int i, int j) {
        int color=states[i][j];
        if(color==0) return 0;
        int n1=0;
        for(int t=i-1,x=j;t>=0;t--){
            if(states[t][--x]==color) n1+=1;
            else break;
        }
        int n2=0;
        for(int t=i+1,x=j;t<19;t++){
            if(states[t][++x]==color) n2+=1;
            else break;
        }
        if(n1+n2+1==5) return color;
        else return 0;
    }

    private int judgeVertal(int[][] states, int i, int j) {
        int color=states[i][j];
        if(color==0) return 0;
        int n1=0;
        for(int t=i-1,x=j;t>=0;t--){
            if(states[t][++x]==color) n1+=1;
            else break;
        }
        int n2=0;
        for(int t=i+1,x=j;t<19;t++){
            if(states[t][--x]==color) n2+=1;
            else break;
        }
        if(n1+n2+1==5) return color;
        else return 0;
    }

    private int judgeHorizontal(int[][] states, int i, int j) {
        int color=states[i][j];
        if(color==0) return 0;
        int n1=0;
        for(int t=j-1;t>=0;t--){
            if(states[i][t]==color) n1+=1;
            else break;
        }
        int n2=0;
        for(int t=j+1;t<19;t++){
            if(states[i][t]==color) n2+=1;
            else break;
        }
        if(n1+n2+1==5) return color;
        else return 0;
    }

}
