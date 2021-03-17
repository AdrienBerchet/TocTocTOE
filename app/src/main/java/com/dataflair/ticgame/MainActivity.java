package com.dataflair.ticgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    private int grid_size;
    TableLayout gameBoard;
    TextView txt_turn;
    TextView txt_earth;
    TextView txt_trump;
    TextView txt_bolsonaro;
    String [][] my_board;
    String turn;
    Integer earthScore=0;
    Integer trumpScore=0;
    Integer bolsonaroScore=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        grid_size = Integer.parseInt(getString(R.string.size_of_board));
        my_board = new String [grid_size][grid_size];
        gameBoard = (TableLayout) findViewById(R.id.mainBoard);
        txt_turn = (TextView) findViewById(R.id.turn);
        txt_earth = (TextView) findViewById(R.id.earth);
        txt_trump = (TextView) findViewById(R.id.trump);
        txt_bolsonaro = (TextView) findViewById(R.id.bolsonaro);


        earthScore = earthScore;
        trumpScore = trumpScore;
        bolsonaroScore = bolsonaroScore;


        resetBoard();
        txt_turn.setText("Turn: "+turn);
        txt_earth.setText("Earth: "+ earthScore);
        txt_trump.setText("Trump: "+trumpScore);
        txt_bolsonaro.setText("Bolsonaro: "+bolsonaroScore);

        for(int i = 0; i< gameBoard.getChildCount(); i++){
            TableRow row = (TableRow) gameBoard.getChildAt(i);
            for(int j = 0; j<row.getChildCount(); j++){
                TextView tv = (TextView) row.getChildAt(j);
                tv.setText(R.string.none);
                tv.setOnClickListener(Move(i, j, tv));
            }
        }

        Button reset_btn = (Button) findViewById(R.id.reset);
        reset_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent current = getIntent();
                    finish();
                    startActivity(current);
            }
        });
    }

    protected void resetBoard(){
        turn = "Trump";
        for(int i = 0; i< grid_size; i++){
            for(int j = 0; j< grid_size; j++){
                my_board[i][j] = " ";
            }
        }
    }

    protected int gameStatus(){

        //0 Continue
        //1 X Wins
        //2 O Wins
        //-1 Draw

        int rowX = 0, colX = 0, rowO = 0, colO = 0;
        for(int i = 0; i< grid_size; i++){
            if(check_Row_Equality(i,"Trump"))
                return 1;
            if(check_Column_Equality(i, "Trump"))
                return 1;
            if(check_Row_Equality(i,"Bolsonaro"))
                return 2;
            if(check_Column_Equality(i,"Bolsonaro"))
                return 2;
            if(check_Diagonal("Trump"))
                return 1;
            if(check_Diagonal("Bolsonaro"))
                return 2;
        }

        boolean boardFull = true;
        for(int i = 0; i< grid_size; i++){
            for(int j = 0; j< grid_size; j++){
                if(my_board[i][j]==" ")
                    boardFull = false;
            }
        }
        if(boardFull)
            return -1;
        else return 0;
    }

    protected boolean check_Diagonal(String player){
        int count_Equal1 = 0,count_Equal2 = 0;
        for(int i = 0; i< grid_size; i++)
            if(my_board[i][i]==player)
                count_Equal1++;
        for(int i = 0; i< grid_size; i++)
            if(my_board[i][grid_size -1-i]==player)
                count_Equal2++;
        if(count_Equal1== grid_size || count_Equal2== grid_size)
            return true;
        else return false;
    }

    protected boolean check_Row_Equality(int r, String player){
        int count_Equal=0;
        for(int i = 0; i< grid_size; i++){
            if(my_board[r][i]==player)
                count_Equal++;
        }

        if(count_Equal== grid_size)
            return true;
        else
            return false;
    }

    protected boolean check_Column_Equality(int c, String player){
        int count_Equal=0;
        for(int i = 0; i< grid_size; i++){
            if(my_board[i][c]==player)
                count_Equal++;
        }

        if(count_Equal== grid_size)
            return true;
        else
            return false;
    }

    protected boolean Cell_Set(int r, int c){
        return !(my_board[r][c]==" ");
    }

    protected void stopMatch(){
        if (earthScore<=-3 || earthScore>=3) {
            for(int i = 0; i< gameBoard.getChildCount(); i++){
                TableRow row = (TableRow) gameBoard.getChildAt(i);
                    for(int j = 0; j<row.getChildCount(); j++) {
                        TextView tv = (TextView) row.getChildAt(j);
                        tv.setOnClickListener(null);
                    }
            }
        } else {
            grid_size = Integer.parseInt(getString(R.string.size_of_board));
            my_board = new String [grid_size][grid_size];
            gameBoard = (TableLayout) findViewById(R.id.mainBoard);
            resetBoard();
            for(int i = 0; i< gameBoard.getChildCount(); i++){
                TableRow row = (TableRow) gameBoard.getChildAt(i);
                for(int j = 0; j<row.getChildCount(); j++){
                    TextView tv = (TextView) row.getChildAt(j);
                    tv.setText(R.string.none);
                    tv.setBackground(getResources().getDrawable(R.drawable.earth));
                    tv.setOnClickListener(Move(i, j, tv));
                }
            }
        }

    }

    View.OnClickListener Move(final int r, final int c, final View tv){

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!Cell_Set(r,c)) {
                    my_board[r][c] = turn;
                    if (turn == "Trump") {
                        /*tv.setText(R.string.X);*/
                        tv.setBackground(getResources().getDrawable(R.drawable.donald));
                        turn = "Bolsonaro";
                    } else if (turn == "Bolsonaro") {
                        /*tv.setText(R.string.O);*/
                        tv.setBackground(getResources().getDrawable(R.drawable.jair));
                        turn = "Trump";
                    }
                    if (gameStatus() == 0) {
                        txt_turn.setText("Turn: Player " + turn);
                    }
                    else if(gameStatus() == -1){
                        if (earthScore<2){
                            txt_turn.setText("This is a Draw match : Earth Wins");
                            earthScore++;
                            txt_earth.setText("Earth: " + earthScore);
                        } else {
                            earthScore++;
                            txt_turn.setText("Earth is saved !");
                        }
                        stopMatch();
                    }
                    else{
                        if (earthScore>-2) {
                            txt_turn.setText(turn + " and Earth Loses!");
                            earthScore--;
                            txt_earth.setText("Earth: " + earthScore);
                            if (turn == "Trump") {
                                bolsonaroScore++;
                                txt_bolsonaro.setText("Bolsonaro: " + bolsonaroScore);
                            } else {
                                trumpScore++;
                                txt_trump.setText("Trump: " + trumpScore);
                            }
                            stopMatch();

                        } else {
                            earthScore--;
                            txt_turn.setText(" Earth is Destroy !");
                            txt_earth.setText("Earth: " + earthScore);
                            if (trumpScore>bolsonaroScore) {
                                txt_trump.setText("Trump has destroyed it !");
                            } else if (trumpScore<bolsonaroScore){
                                txt_bolsonaro.setText("Bolsonaro has destroyed it !");
                            } else {
                                txt_trump.setText("Both have destroyed it !");
                                txt_bolsonaro.setText("Both have destroyed it !");
                            }
                            stopMatch();
                        }
                    }
                }
                else{
                    txt_turn.setText(txt_turn.getText()+" Choose an Empty Call");
                }
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_board, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}