public class Robot {
    public int[] think(int[][] states,int row,int col){
        for(int i=0;i<19;i++){
            for(int j=0;j<19;j++){
                if(states[i][j]!=0) continue;
                states[i][j]=2;
                boolean r=check(states,i,j,5);
                states[i][j]=0;
                if(r) return new int[]{i,j};
            }
        }

        for(int i=0;i<19;i++){
            for(int j=0;j<19;j++){
                if(states[i][j]!=0) continue;
                states[i][j]=1;
                boolean r=check(states,i,j,5);
                states[i][j]=0;
                if(r) return new int[]{i,j};
            }
        }

        for(int i=0;i<19;i++){
            for(int j=0;j<19;j++){
                if(states[i][j]!=0) continue;
                states[i][j]=1;
                boolean r=check(states,i,j,4);
                states[i][j]=0;
                if(r) return new int[]{i,j};
            }
        }

        for(int i=0;i<19;i++){
            for(int j=0;j<19;j++){
                if(states[i][j]!=0) continue;
                states[i][j]=2;
                boolean r=check(states,i,j,4);
                states[i][j]=0;
                if(r) return new int[]{i,j};
            }
        }
        for(int i=0;i<19;i++){
            for(int j=0;j<19;j++){
                if(states[i][j]!=0) continue;
                states[i][j]=2;
                boolean r=check(states,i,j,3);
                states[i][j]=0;
                if(r) return new int[]{i,j};
            }
        }
        for(int i=0;i<19;i++){
            for(int j=0;j<19;j++){
                if(states[i][j]!=0) continue;
                states[i][j]=1;
                boolean r=check(states,i,j,4);
                states[i][j]=0;
                if(r) return new int[]{i,j};
            }
        }
        for(int i=0;i<19;i++){
            for(int j=0;j<19;j++){
                if(states[i][j]!=0) continue;
                states[i][j]=2;
                boolean r=check(states,i,j,3);
                states[i][j]=0;
                if(r) return new int[]{i,j};
            }
        }





        for(int i=0;i<19;i++){
            for(int j=0;j<19;j++){
                if(states[i][j]!=0) continue;
                states[i][j]=1;
                boolean r=check(states,i,j,3);
                states[i][j]=0;
                if(r) return new int[]{i,j};
            }
        }

        for(int i=0;i<19;i++){
            for(int j=0;j<19;j++){
                if(states[i][j]!=0) continue;
                states[i][j]=2;
                boolean r=check(states,i,j,3);
                states[i][j]=0;
                if(r) return new int[]{i,j};
            }
        }

        for(int i=0;i<19;i++){
            for(int j=0;j<19;j++){
                if(states[i][j]!=0) continue;
                states[i][j]=1;
                boolean r=check(states,i,j,3);
                states[i][j]=0;
                if(r) return new int[]{i,j};
            }
        }
        for(int i=0;i<19;i++){
            for(int j=0;j<19;j++){
                if(states[i][j]!=0) continue;
                states[i][j]=2;
                boolean r=check(states,i,j,2);
                states[i][j]=0;
                if(r) return new int[]{i,j};
            }
        }


        for(int i=0;i<19;i++){
            for(int j=0;j<19;j++){
                if(states[i][j]!=0) continue;
                states[i][j]=1;
                boolean r=check(states,i,j,2);
                states[i][j]=0;
                if(r) return new int[]{i,j};
            }
        }

        for(int i=0;i<19;i++){
            for(int j=0;j<19;j++){
                if(states[i][j]==0) return new int[]{i,j};
            }
        }
        return null;
    }
    private boolean check(int[][] states,int row,int col,int count){
        if(states[row][col]==0) return false;
        int color=states[row][col];
        int n1=0;
        for(int t=col-1;t>=0;t--){
            if(states[row][t]==color) n1+=1;
            else break;
        }
        int n2=0;
        for(int t=col+1;t<19;t++){
            if(states[row][t]==color) n2+=1;
            else break;
        }
        if(n1+n2+1==count) return true;

        //垂直
         n1=0;
        for(int t=row-1;t>=0;t--){
            if(states[t][col]==color) n1+=1;
            else break;
        }
         n2=0;
        for(int t=row+1;t<19;t++){
            if(states[t][col]==color) n2+=1;
            else break;
        }
        if(n1+n2+1==count) return true;

        //左斜
        n1=0;
        int t=1;
        while(row-t>=0&&col-t>=0){
            if(states[row-t][col-t]==color) n1+=1;
            else break;
            t++;
        }

        n2=0;
        t=1;
        while(row+t<19&&col+t<19){
            if(states[row+t][col+t]==color) n2+=1;
            else break;
            t++;
        }
        if(n1+n2+1==count) return true;

        //右斜
        n1=0;
        t=1;
        while(row+t<19&&col-t>=0){
            if(states[row+t][col-t]==color) n1+=1;
            else break;
            t++;
        }
        n2=0;
        t=1;
        while(row-t>=0&&col+t<19){
            if(states[row-t][col+t]==color) n2+=1;
            else break;
            t++;
        }
        if(n1+n2+1==count) return true;
        return false;
    }

}
