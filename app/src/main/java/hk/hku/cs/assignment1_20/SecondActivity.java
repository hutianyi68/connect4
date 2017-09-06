package hk.hku.cs.assignment1_20;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by hutia on 10/30/2016.
 */

public class SecondActivity extends Activity {
    private static int GRID_WIDTH = 7;
    private static int GRID_HEIGHT = 6;
    private int layoutLeft, layoutRight, layoutWidth, layoutHeight, layoutGrid;
    private int stateGame=1;
    private int step=0;
    private int stateWin=0;
    private int winStep=0;

    private int statePiece[][] = new int[GRID_WIDTH][GRID_HEIGHT];
    private int gameSequence[] = new int[GRID_HEIGHT*GRID_WIDTH];
    private int winSequence[] = new int[GRID_WIDTH*GRID_HEIGHT*4];

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_activity);

        RelativeLayout chessboard = new RelativeLayout(this);
        chessboard.setBackgroundResource(R.drawable.background);

        LinearLayout buttonLayout = new LinearLayout(this);
        LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        buttonLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        buttonLayout.setId(100);
        Button newGameButton = new Button(this);
        newGameButton.setText("NEW GAME");
        newGameButton.setTextSize(14);
        LinearLayout.LayoutParams newGameButtonRule = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        newGameButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                newGame();
            }
        });
        Button retractButton = new Button(this);
        retractButton.setText("RETRACT");
        LinearLayout.LayoutParams retractButtonRule = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        retractButtonRule.setMargins(20,0,0,0);
        retractButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (step>0) {
                    retractGame();
                }
            }
        });
        buttonLayout.addView(newGameButton,newGameButtonRule);
        buttonLayout.addView(retractButton,retractButtonRule);

        RelativeLayout mainBoardLayout = new RelativeLayout(this);
        RelativeLayout.LayoutParams mainBoardLayoutRule = new RelativeLayout.LayoutParams(
                (int)getWindowManager().getDefaultDisplay().getWidth()-20,
                (int)((getWindowManager().getDefaultDisplay().getWidth()-20)/7*6)
        );
        mainBoardLayout.setId(101);
        mainBoardLayoutRule.setMargins(10,10,0,0);
        mainBoardLayout.setBackgroundResource(R.drawable.background2);
        mainBoardLayoutRule.addRule(RelativeLayout.BELOW,buttonLayout.getId());


        //Display display = getWindowManager().getDefaultDisplay().getSize(size);
        //layoutWidth = 460;
        //layoutHeight = (int)(layoutWidth/7*6);
        //layoutGrid = (int)(layoutWidth/7);
        layoutWidth = (int)getWindowManager().getDefaultDisplay().getWidth()-20;
        layoutHeight = (int)((getWindowManager().getDefaultDisplay().getWidth()-20)/7*6);
        layoutGrid = (int)((getWindowManager().getDefaultDisplay().getWidth()-20)/7);
        System.out.println(layoutWidth+"********"+layoutHeight+"^^^^^^^^"+layoutGrid);

        for (int i=0;i<GRID_WIDTH;i++){
            for (int j=0;j<GRID_HEIGHT;j++){
                RelativeLayout pieceLayout = new RelativeLayout(this);
                RelativeLayout.LayoutParams pieceLayoutRule = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                );
                pieceLayoutRule.setMargins(i*layoutGrid+10,j*layoutGrid+10,0,0);
                //pieceLayoutRule.setMargins(65+10,65+10,0,0);

                ImageView img = new ImageView(this);
                ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                        layoutGrid-20,
                        layoutGrid-20
                );
                img.setImageResource(R.drawable.empty_t);
                img.setId(i+1+(5-j)*7);
                pieceLayout.addView(img,lp);
                mainBoardLayout.addView(pieceLayout,pieceLayoutRule);
            }
        }
        mainBoardLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP && stateWin==0){
                    int x = (int)event.getX();
                    int y = (int)event.getY();
                    if (y<=layoutHeight){
                        drawPiece((int)x/layoutGrid);
                    }
                }
                return true;
            }
        });

        RelativeLayout stateLayout = new RelativeLayout(this);
        RelativeLayout.LayoutParams stateLayoutRule = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        stateLayoutRule.addRule(RelativeLayout.BELOW,mainBoardLayout.getId());
        stateLayoutRule.addRule(RelativeLayout.CENTER_HORIZONTAL);
        stateLayoutRule.setMargins(0,20,0,0);

        TextView stateTV = new TextView(this);
        LinearLayout.LayoutParams stateTVRule = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        stateTV.setTextSize(30);
        stateTV.setId(1000);
        stateTV.setText("Red Turn !  ");
        RelativeLayout stateTVLayout = new RelativeLayout(this);
        RelativeLayout.LayoutParams stateTVLayoutRule = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        stateTVLayout.addView(stateTV,stateTVRule);
        stateTVLayout.setId(102);

        ImageView stateChess = new ImageView(this);
        LinearLayout.LayoutParams stateChessRule = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        stateChess.setImageResource(R.drawable.red_t);
        stateChess.setId(1001);
        RelativeLayout stateChessLayout = new RelativeLayout(this);
        RelativeLayout.LayoutParams stateChessLayoutRule = new RelativeLayout.LayoutParams(
                layoutGrid-20,
                layoutGrid-20
        );
        stateChessLayoutRule.addRule(RelativeLayout.RIGHT_OF,stateTVLayout.getId());
        stateChessLayoutRule.setMargins(40,0,0,0);
        stateChessLayout.addView(stateChess,stateChessRule);

        stateLayout.addView(stateTVLayout,stateTVLayoutRule);
        stateLayout.addView(stateChessLayout,stateChessLayoutRule);

        chessboard.addView(buttonLayout,buttonLayoutParams);
        chessboard.addView(mainBoardLayout,mainBoardLayoutRule);
        chessboard.addView(stateLayout,stateLayoutRule);
        setContentView(chessboard);

    }

    private void drawPiece(int place){
        for (int i=0;i<GRID_HEIGHT;i++){
            if(statePiece[place][i]==0){
                statePiece[place][i] = stateGame;
                ImageView changeImage = (ImageView)this.findViewById(place+i*7+1);
                if (stateGame == 1) {
                    changeImage.setImageResource(R.drawable.red_t);
                    checkWin(place+i*7+1);
                    stateGame =2;
                }
                else {
                    changeImage.setImageResource(R.drawable.green_t);
                    checkWin(place+i*7+1);
                    stateGame=1;
                }
                if (stateWin == 0 && step == 41){
                    stateWin=3;
                }
                changeTV();
                changeStateChess();
                gameSequence[step] = place+i*7+1;
                step++;
                break;
            }
        }
    }

    private void checkWin(int place){
        for(int y_place=0;y_place<GRID_HEIGHT;y_place++){
            for (int x_place=0;x_place<GRID_WIDTH;x_place++){
                if (statePiece[x_place][y_place] != 0) {
                    if (y_place < 3 && (statePiece[x_place][y_place] == statePiece[x_place][y_place + 1] && statePiece[x_place][y_place] == statePiece[x_place][y_place + 2] && statePiece[x_place][y_place] == statePiece[x_place][y_place + 3]))
                        setState(x_place, y_place, 0, 1);
                    if ((x_place < 4 && y_place < 3) && (statePiece[x_place][y_place] == statePiece[x_place + 1][y_place + 1] && statePiece[x_place][y_place] == statePiece[x_place + 2][y_place + 2] && statePiece[x_place][y_place] == statePiece[x_place + 3][y_place + 3]))
                        setState(x_place, y_place, 1, 1);
                    if (x_place < 4 && (statePiece[x_place][y_place] == statePiece[x_place + 1][y_place] && statePiece[x_place][y_place] == statePiece[x_place + 2][y_place] && statePiece[x_place][y_place] == statePiece[x_place + 3][y_place]))
                        setState(x_place, y_place, 1, 0);
                    if ((x_place < 4 && y_place > 2) && (statePiece[x_place][y_place] == statePiece[x_place + 1][y_place - 1] && statePiece[x_place][y_place] == statePiece[x_place + 2][y_place - 2] && statePiece[x_place][y_place] == statePiece[x_place + 3][y_place - 3]))
                        setState(x_place, y_place, 1, -1);
                    if (y_place > 2 && (statePiece[x_place][y_place] == statePiece[x_place][y_place - 1] && statePiece[x_place][y_place] == statePiece[x_place][y_place - 2] && statePiece[x_place][y_place] == statePiece[x_place][y_place - 3]))
                        setState(x_place, y_place, 0, -1);
                    if ((x_place > 2 && y_place > 2) && (statePiece[x_place][y_place] == statePiece[x_place - 1][y_place - 1] && statePiece[x_place][y_place] == statePiece[x_place - 2][y_place - 2] && statePiece[x_place][y_place] == statePiece[x_place - 3][y_place - 3]))
                        setState(x_place, y_place, -1, -1);
                    if (x_place > 2 && (statePiece[x_place][y_place] == statePiece[x_place - 1][y_place] && statePiece[x_place][y_place] == statePiece[x_place - 2][y_place] && statePiece[x_place][y_place] == statePiece[x_place - 3][y_place]))
                        setState(x_place, y_place, -1, 0);
                    if ((x_place > 2 && y_place < 3) && (statePiece[x_place][y_place] == statePiece[x_place - 1][y_place + 1] && statePiece[x_place][y_place] == statePiece[x_place - 2][y_place + 2] && statePiece[x_place][y_place] == statePiece[x_place - 3][y_place + 3]))
                        setState(x_place, y_place, -1, 1);
                }
            }
        }
    }

    private void setState(int x_place, int y_place, int change_x, int change_y){
        for (int i=0;i<4;i++){
            if (stateGame == 1){
                ImageView changeImage = (ImageView)this.findViewById(x_place+i*change_x+7*(y_place+i*change_y)+1);
                changeImage.setImageResource(R.drawable.red_wint);
                stateWin = 1;
            }
            else{
                ImageView changeImage = (ImageView)this.findViewById(x_place+i*change_x+7*(y_place+i*change_y)+1);
                changeImage.setImageResource(R.drawable.green_wint);
                stateWin = 2;
            }
            winSequence[winStep] = x_place+i*change_x+7*(y_place+i*change_y)+1;
            winStep++;
        }
        changeTV();
        changeStateChess();
    }

    private void changeTV(){
        TextView stateTV = (TextView)this.findViewById(1000);
        switch (stateWin){
            case 0: switch (stateGame){
                case 2: stateTV.setText("Green Turn !"); break;
                case 1: stateTV.setText("Red Turn !  "); break;
            }break;
            case 1: stateTV.setText("Red Win!"); break;
            case 2: stateTV.setText("Green Win!");break;
            case 3: stateTV.setText("Draw !"); break;
        }
    }

    private void changeStateChess(){
        ImageView stateChess = (ImageView)this.findViewById(1001);
        switch (stateWin){
            case 0: switch (stateGame){
                case 2: stateChess.setImageResource(R.drawable.green_t); break;
                case 1: stateChess.setImageResource(R.drawable.red_t); break;
            }break;
            case 1: stateChess.setImageResource(R.drawable.red_wint); break;
            case 2: stateChess.setImageResource(R.drawable.green_wint);break;
            case 3: stateChess.setImageResource(R.drawable.empty_t); break;
        }
    }

    private void newGame(){
        for (int i=0;i<GRID_HEIGHT;i++){
            for(int j=0;j<GRID_WIDTH;j++){
                ImageView changeImage = (ImageView)this.findViewById(j+i*7+1);
                changeImage.setImageResource(R.drawable.empty_t);
                statePiece[j][i]=0;
                gameSequence[j+i*7]=0;
            }
        }
        for (int i=0;i<winStep;i++){
            winSequence[i] = 0;
        }
        winStep=0;
        stateGame = 1;
        stateWin = 0;
        step = 0;
        changeTV();
        changeStateChess();
    }

    private void retractGame(){
        if (stateWin > 0){
            for (int i=0;i<winStep;i++){
                ImageView reverseImage = (ImageView)this.findViewById(winSequence[i]);
                if (stateWin == 1){
                    reverseImage.setImageResource(R.drawable.red_t);
                }else{
                    reverseImage.setImageResource(R.drawable.green_t);
                }
                winSequence[i] = 0;
            }
            winStep = 0;
            stateWin = 0;
        }
        if (stateGame == 2){
            stateGame = 1;
        }else{
            stateGame = 2;
        }
        step--;
        int y_axis = (gameSequence[step]-1)/7;
        int x_axis = gameSequence[step] - 7*y_axis-1;
        statePiece[x_axis][y_axis] = 0;
        ImageView changeImage = (ImageView)this.findViewById(gameSequence[step]);
        changeImage.setImageResource(R.drawable.empty_t);
        gameSequence[step] = 0;
        changeTV();
        changeStateChess();
    }
}
