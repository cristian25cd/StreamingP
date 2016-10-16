package server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

public class CongestionController implements ActionListener{
    private Timer ccTimer;
    int interval;   //interval to check traffic stats
    int prevLevel;  //previously sampled congestion level
    ClienteRemoto s;

    public CongestionController(int interval, ClienteRemoto s) {
        this.interval = interval;
        ccTimer = new Timer(interval, this);
        ccTimer.start();
        this.s=s;
    }

    public void actionPerformed(ActionEvent e) {

        //adjust the send rate
        if (prevLevel != s.congestionLevel) {
            s.sendDelay = ClienteRemoto.FRAME_PERIOD + s.congestionLevel * (int)(ClienteRemoto.FRAME_PERIOD * 0.1);
            s.timer.setDelay(s.sendDelay);
            prevLevel = s.congestionLevel;
            System.out.println("Send delay changed to: " + s.sendDelay);
        }
    }
}

