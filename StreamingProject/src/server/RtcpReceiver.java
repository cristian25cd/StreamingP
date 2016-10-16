package server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.DatagramPacket;

import javax.swing.Timer;

public class RtcpReceiver implements ActionListener {
    private Timer rtcpTimer;
    private byte[] rtcpBuf;
    int interval;
    ClienteRemoto s;

    public RtcpReceiver(int interval, ClienteRemoto clienteRemoto) {
        //set timer with interval for receiving packets
        this.interval = interval;
        rtcpTimer = new Timer(interval, this);
        rtcpTimer.setInitialDelay(0);
        rtcpTimer.setCoalesce(true);

        //allocate buffer for receiving RTCP packets
        rtcpBuf = new byte[512];
        
        this.s=clienteRemoto;
    }

    public void actionPerformed(ActionEvent e) {
        //Construct a DatagramPacket to receive data from the UDP socket
        DatagramPacket dp = new DatagramPacket(rtcpBuf, rtcpBuf.length);
        float fractionLost;

        try {
            s.RTCPsocket.receive(dp);   // Blocking
            RTCPpacket rtcpPkt = new RTCPpacket(dp.getData(), dp.getLength());
            System.out.println("[RTCP] " + rtcpPkt);

            //set congestion level between 0 to 4
            fractionLost = rtcpPkt.fractionLost;
            if (fractionLost >= 0 && fractionLost <= 0.01) {
                s.congestionLevel = 0;    //less than 0.01 assume negligible
            }
            else if (fractionLost > 0.01 && fractionLost <= 0.25) {
                s.congestionLevel = 1;
            }
            else if (fractionLost > 0.25 && fractionLost <= 0.5) {
                s.congestionLevel = 2;
            }
            else if (fractionLost > 0.5 && fractionLost <= 0.75) {
                s.congestionLevel = 3;
            }
            else {
                s.congestionLevel = 4;
            }
        }
        catch (InterruptedIOException iioe) {
            System.out.println("Nothing to read");
        }
        catch (IOException ioe) {
            System.out.println("Exception caught: "+ioe);
        }
    }

    public void startRcv() {
        rtcpTimer.start();
    }

    public void stopRcv() {
        rtcpTimer.stop();
    }
}
