import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Chessboard  extends JFrame implements MouseListener, ActionListener, WindowListener {
    public final static int WIDTH =920;
    public final static int HEIGHT =920;
    public final static int MARGIN =100;
    public final static int SPACING =(WIDTH-2*MARGIN)/18;
    public final static int BLACK =1;
    public final static int WHITE =2;
    public final static int GAME_TYPE_MANCHINE =2;
    public final static int GAME_TYPE_MANMAN =1; //下棋方式：人人对弈
    private  int[][] states=new int[19][19];
    private int curColor=BLACK;
    private int cnt=0;
    private JMenuBar menuBar;//菜单条
    private  JMenu gameMenu;//游戏菜单
    private JMenuItem menuItemManMan;//人人
    private JMenuItem menuItemManchine;//人机
    private JMenuItem menuItemUndo;//悔棋
    private JMenuItem menuSaveGame;//中局存盘
    private JMenuItem menuItemLoadGame;//读取存盘
    private  JMenuItem menuItemHistory;//历史战绩
    private JMenuItem menuItemExit;//退出程序
    private int gamType=GAME_TYPE_MANMAN;
    private String player1=null;//玩家一姓名
    private String player2=null;//玩家二姓名
    private Judge jedge=new Judge();
    private  Robot robot=new Robot();
    private  int[] lastBlackRow=new int[19];
    private  int[] lastBlackCol=new int[19];
    private  int[] lastWhiteRow=new int[19];

    private  int[] lastWhiteCol=new int[19];
//    private int lastBlackRow,lastBlackCol;
//    private int lastWhiteRow,lastWhiteCol;
    public Chessboard(){
        //将窗口与鼠标监听器实现对象相关联
        createMenu();
        this.addMouseListener(this);
        this.addWindowListener(this);
    }

    public void paint(Graphics g){
        super.paint(g);
        Color bgColor =new Color(200,200,200);
        g.setColor(bgColor);
        g.fillRect(MARGIN,MARGIN,WIDTH-2*MARGIN,HEIGHT-2*MARGIN);
        g.setColor(Color.BLACK);
        for(int i=0;i<19;i++){
            g.drawLine(MARGIN,MARGIN+i*SPACING,WIDTH-MARGIN,i*SPACING+MARGIN);
            g.drawLine(MARGIN+i*SPACING,MARGIN,i*SPACING+MARGIN,HEIGHT-MARGIN);
        }
        for(int i=0;i<19;i++){
            for(int j=0;j<19;j++){
                if(this.states[i][j]==0)  continue;
                if(this.states[i][j]==BLACK){
//                    System.out.println(1);
                    g.setColor(Color.BLACK);
                    int x=j*SPACING+MARGIN;
                    int y=i*SPACING+MARGIN;
                    g.fillOval(x-10,y-10,20,20);
                }else{
                    g.setColor(Color.WHITE);
                    int x=j*SPACING+MARGIN;
                    int y=i*SPACING+MARGIN;
                    g.fillOval(x-10,y-10,20,20);
                }
            }
        }
        //显示玩家姓名
        Font font=new Font("宋体",0,20);
        if(this.player1!=null){
            if(this.curColor==BLACK) g.setColor(Color.RED);
            else g.setColor(Color.BLACK);
            g.drawString(this.player1,40,110);
        }
        if(this.player2!=null){
            if(this.curColor==WHITE) g.setColor(Color.RED);
            else g.setColor(Color.BLACK);
            g.drawString(this.player2,850,110);
        }
    }
    private  void createMenu(){
        this.menuBar=new JMenuBar();
        this.setJMenuBar(menuBar);
        //创建游戏菜单，并加入菜单条去
        gameMenu=new JMenu("游戏");
        this.menuBar.add(gameMenu);
        //创建所有菜单项，并加入游戏菜单中去
        menuItemManMan =new JMenuItem("人人对弈");
        menuItemManMan.addActionListener(this);
        gameMenu.add(menuItemManMan);
        menuItemManchine= new JMenuItem("人机对弈");
        menuItemManchine.addActionListener(this);
        gameMenu.add(menuItemManchine);
        gameMenu.addSeparator();//添加一个分割条

        menuItemUndo =new JMenuItem("悔棋");
        menuItemUndo.addActionListener(this);
        gameMenu.add(menuItemUndo);
        menuSaveGame=new JMenuItem("中局存盘");
        menuSaveGame.addActionListener(this);
        gameMenu.add(menuSaveGame);
        menuItemLoadGame=new JMenuItem("读取存盘");
        menuItemLoadGame.addActionListener(this);
        gameMenu.add(menuItemLoadGame);
        menuItemHistory=new JMenuItem("历史战绩");
        menuItemHistory.addActionListener(this);
        gameMenu.add(menuItemHistory);
        gameMenu.addSeparator();

        menuItemExit=new JMenuItem("退出程序");
        menuItemExit.addActionListener(this);//左边是监听器
        gameMenu.add(menuItemExit);
    }

    public static void main(String[] args){
        Chessboard board=new Chessboard();
        board.setTitle("五子棋");
        board.setSize(WIDTH,HEIGHT);
        board.setLocationRelativeTo(null);//窗口居中
        board.setResizable(false);
        board.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//EXIT_ON_CLOSE是静态变量,作用是关闭程序进程
        board.setVisible(true);//窗口显示
    }
    //棋盘状态初始化
    public void reset(){
        for(int i=0;i<19;i++){
            for(int j=0;j<19;j++){
                this.states[i][j]=0;
            }
        }
        this.curColor=BLACK;
    }
    @Override
    public void mouseClicked(MouseEvent e) {//鼠标单击

        int[] rowcol=this.pixel2board(e.getX(),e.getY());cnt++;
        if(rowcol !=null){
            int row=rowcol[0];
            int col=rowcol[1];
            this.states[row][col]=curColor;
            if(curColor==BLACK){
                this.lastBlackRow[cnt]=row;
                this.lastBlackCol[cnt]=col;
            }
            else {
                this.lastWhiteRow[cnt]=row;
                this.lastWhiteCol[cnt]=col;
            }
            if(curColor==BLACK) curColor=WHITE;
            else curColor=BLACK;
            int result=jedge.doJudge(this.states);
            if(result==1){
                JOptionPane.showMessageDialog(this,"黑方胜利");
                this.saveHistory(1);
                reset();
                return;

            }
            else if(result==2){
                JOptionPane.showMessageDialog(this,"白方胜利");
                this.saveHistory(2);
                reset();
                return;
            }
            if(gamType==GAME_TYPE_MANCHINE){
                int[] robotresult=robot.think(states,row,col);
                cnt++;
                int rrow=robotresult[0],rcol=robotresult[1];
                this.states[rrow][rcol]=curColor;
                curColor=BLACK;
                this.lastWhiteRow[cnt]=rrow;
                this.lastWhiteCol[cnt]=rcol;
                result=jedge.doJudge(this.states);
                if(result==1){
                    JOptionPane.showMessageDialog(this,"黑方胜利");
                    this.saveHistory(1);
                    reset();
                    return;
                }
                else if(result==2){
                    JOptionPane.showMessageDialog(this,"白方胜利");
                    this.saveHistory(2);
                    reset();
                    return;
                }
            }
        }

        //System.out.println(tempx+","+tempy);
        this.repaint();

    }
  //保存历史记录1为黑放胜，2为白方胜
    private void saveHistory(int i) {
        //生成对弈时间
        Date now=new Date();
        SimpleDateFormat ftm=new SimpleDateFormat("yyyy-MM--dd hh:mm:ss");
        String timeText =ftm.format(now);
        //生成待保存到文件中的文本
        String text=timeText+":"+player1+","+player2+":"+(i==1?"黑方胜":"白方胜");

        //异常处理快
        try{
        //将text保存到文件中去
        File file=new File("D:\\JavaPrj\\HELLOWORD\\history.txt");//定义文件对象
        FileWriter w=new FileWriter(file,true);//添加写
        //定义缓冲写函数
        BufferedWriter writer=new BufferedWriter(w);
        writer.write(text+"\n");
        writer.close();
        }catch (Exception e){
            System.out.println("保存历史战绩失败了，失败原因："+e.getMessage());
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {//鼠标进入

    }

    @Override
    public void mouseReleased(MouseEvent e) {//鼠标离开

    }

    @Override
    public void mouseEntered(MouseEvent e) {//鼠标按下

    }

    @Override
    public void mouseExited(MouseEvent e) {//鼠标释放

    }

    private  int[] pixel2board(int x,int y){
        if(x < MARGIN || x > MARGIN + SPACING*18)
            return null;
        if(y < MARGIN || y > MARGIN + SPACING*18)
            return null;
        int col=(x-MARGIN)/SPACING;
        int t=(x-MARGIN)%SPACING;
        if(t>SPACING/2) col+=1;
        int row=(y-MARGIN)/SPACING;
        t=(y-MARGIN)%SPACING;
        if(t>SPACING/2) row+=1;
        return new int[]{row,col};
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==menuItemExit){
            int r=JOptionPane.showConfirmDialog(this,"系统将退出，确认？","提醒",JOptionPane.OK_CANCEL_OPTION);
            if(r==JOptionPane.OK_OPTION){
                this.dispose();
            }

        }else if(e.getSource()==menuItemManMan){
            this.gamType=GAME_TYPE_MANMAN;
            this.player1=JOptionPane.showInputDialog("请输入玩家一的姓名");
            this.player2=JOptionPane.showInputDialog("请输入玩家二的姓名");
            this.reset();
            this.repaint();
        }else if(e.getSource()==menuItemManchine){
            this.gamType=GAME_TYPE_MANCHINE;
            this.player1=JOptionPane.showInputDialog("请输入玩家一的姓名");
            this.player2="机器人";
            this.reset();
            this.repaint();
        }
        else if(e.getSource()==menuItemUndo){
            if(gamType==GAME_TYPE_MANMAN){
                if(curColor==BLACK&&this.lastWhiteRow[cnt]!=0&&this.lastWhiteCol[cnt]!=0){
                    this.states[lastWhiteRow[cnt]][lastWhiteCol[cnt]]=0;
                    cnt--;
                    curColor=WHITE;

                }
                else if(curColor==WHITE&&this.lastBlackRow[cnt]!=0&&this.lastBlackCol[cnt]!=0){
                    this.states[lastBlackRow[cnt]][lastBlackCol[cnt]]=0;
                    cnt--;
                    curColor=BLACK;
                }
            }
            else if(gamType==GAME_TYPE_MANCHINE){
                this.states[lastWhiteRow[cnt]][lastWhiteCol[cnt]]=0;
                this.states[lastBlackRow[cnt]][lastBlackCol[cnt]]=0;
                cnt--;
                curColor=BLACK;
            }
            this.repaint();
        }
        else if(e.getSource()==menuItemHistory){//历史菜单被点击了
            String content="",line="";
            try{
            File file=new File("D:\\JavaPrj\\HELLOWORD\\history.txt");
            FileReader r=new FileReader(file);
            BufferedReader reader=new BufferedReader(r);

            while(true){
                line =reader.readLine();
                if(line==null) break;
                content+=line+"\n";
            }
            reader.close();

            }catch (Exception ex){
                System.out.println("读取历史战绩失败:"+ex.getMessage());
                JOptionPane.showMessageDialog(this,"读取历史战绩失败:"+ex.getMessage());
            }
            JOptionPane.showMessageDialog(this,content);
        }
        else if(e.getSource()==menuSaveGame){
            this.saveBoardState();
        }
        else if(e.getSource()==menuItemLoadGame){
            this.loadBoardState();
        }
    }

    @Override
    public void windowOpened(WindowEvent e) {

    }
    //当窗口将要被关闭时，Java调用该函数
    @Override
    public void windowClosing(WindowEvent e) {
       int r=JOptionPane.showConfirmDialog(this,"系统将退出，确认？","提醒",JOptionPane.OK_CANCEL_OPTION);
       if(r==JOptionPane.CANCEL_OPTION){
           this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);//取消关闭

       }
       else {
           this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//关闭系统
       }
    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }
    //读取所有对应状态
    private void loadBoardState(){
        try{
            File file =new File("D:\\JavaPrj\\HELLOWORD\\board.txt");
            FileReader r=new FileReader(file);
            BufferedReader reader=new BufferedReader(r);
            String line =reader.readLine();
            this.gamType=Integer.parseInt(line);
            this.player1=reader.readLine();
            this.player2=reader.readLine();
            line =reader.readLine();
            this.curColor=Integer.parseInt(line);
            //读取棋盘状态
            for(int i=0;i<19;i++){
                line =reader.readLine();
                String[] column=line.split(",");
                for(int j=0;j<19;j++){
                   this.states[i][j]=Integer.parseInt(column[j]);
                }
            }
        }catch (Exception e){
            JOptionPane.showMessageDialog(this,"读取棋盘状态出错："+e.getMessage());
        }
        //刷新界面
        this.repaint();
    }
    //保存所有对应状态
    private  void saveBoardState(){
        try{
            File file =new File("D:\\JavaPrj\\HELLOWORD\\board.txt");
            FileWriter w=new FileWriter(file);
            BufferedWriter writer=new BufferedWriter(w);
            //写对应类型
            writer.write(this.gamType+"\n");
            //写名字
            writer.write(player1+"\n");
            writer.write(player2+"\n");
            //写当前落子颜色
            writer.write(this.curColor+"\n");
            //写棋盘状态
            StringBuffer buf=new StringBuffer();//当字符串频繁发生变化，则不能使用string，而应该使用StringBuffer
            for(int i=0;i<19;i++){
                for(int j=0;j<19;j++){
                    if(j<18) buf.append(this.states[i][j]+",");
                    else buf.append(this.states[i][j]);
                }
                buf.append("\n");
            }
            writer.write(buf.toString());
            writer.close();
        }catch (Exception e){
            JOptionPane.showMessageDialog(this,"中盘存放出错："+e.getMessage());
        }

    }
}
