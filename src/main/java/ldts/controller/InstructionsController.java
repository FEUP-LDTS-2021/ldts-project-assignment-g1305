package ldts.controller;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;
import ldts.model.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InstructionsController {

    boolean on = true;
    private Screen screen;
    private Game game;
    List<Order> orderQueue;
    List<Order> undoOrderQueue = new ArrayList<>();

    public InstructionsController(Screen scr, Game gm){
        screen = scr;
        game = gm;
        orderQueue = new ArrayList<>();
        on = true;
    }

    public void run() throws IOException {
        while(on){
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            inputReceiver();
            try {
                draw();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void draw() throws IOException{
        screen.clear();

        TextGraphics screenGraphics = screen.newTextGraphics();
        screenGraphics.setBackgroundColor(TextColor.Factory.fromString("#3A3A3A"));
        screenGraphics.fillRectangle(new TerminalPosition(0, 0), new TerminalSize(80, 40), ' ');

        screenGraphics.putString(10,3, checkDifficultyChanges());

        screenGraphics.putString(10,10,"0) Increase Difficulty");
        screenGraphics.putString(10,15,"1) Decrease Difficulty");
        screenGraphics.putString(10,20,"2) Undo Changes");
        screenGraphics.putString(10,25,"3) Exit");
        screen.refresh();
    }

    public void inputReceiver() throws IOException{
        if (keyController.isZeroPressed()) {
            Order order = new HighDifficultyOrder(game);
            orderQueue.add(order);
            Order undoOrder = new LowDifficultyOrder(game);
            undoOrderQueue.add(undoOrder);
        }
        else if (keyController.isOnePressed()) {
            Order order = new LowDifficultyOrder(game);
            orderQueue.add(order);
            Order undoOrder = new HighDifficultyOrder(game);
            undoOrderQueue.add(undoOrder);
        }
        if (keyController.isTwoPressed()) {
            orderQueue.clear();
            processOrders(undoOrderQueue);
            undoOrderQueue.clear();
        }
        if (keyController.isThreePressed()) {
            processOrders(orderQueue);
            on=false;
        }
    }

    public String checkDifficultyChanges(){
        int counter = 0;
        for(Order order:orderQueue){
            if(order instanceof HighDifficultyOrder ){
                counter-=1;
            }
            else if(order instanceof LowDifficultyOrder){
                counter+=1;
            }
        }
        if(counter>0){
            return "Difficulty will be lower";
        }
        else if(counter<0){
            return "Difficulty will be higher";
        }
        else{
            return "Difficulty will remain the same";
        }
    }

    public void processOrders(List<Order> ordersQueue){
        for(Order order:ordersQueue){
            order.execute();
        }
    }
    public void setOn(){
        on = true;
    }
}