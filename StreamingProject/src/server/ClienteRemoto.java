package server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.Arrays;
import java.util.StringTokenizer;

import javax.swing.Timer;

public class ClienteRemoto implements ActionListener {
	
	 //RTSP variables
    //----------------
    //rtsp states
    final static int INIT = 0;
    final static int READY = 1;
    final static int PLAYING = 2;
    //rtsp message types
    final static int SETUP = 3;
    final static int PLAY = 4;
    final static int PAUSE = 5;
    final static int TEARDOWN = 6;
    final static int DESCRIBE = 7;
    final static int LOGIN = 8;
	
    static int state; //RTSP Server state == INIT or READY or PLAY
    Socket RTSPsocket; //socket used to send/receive RTSP messages
    
    //Video variables:
    //----------------
    int imagenb = 0; //image nb of the image currently transmitted
    VideoStream video; //VideoStream object used to access video frames
    static int MJPEG_TYPE = 26; //RTP payload type for MJPEG video
    static int FRAME_PERIOD = 100; //Frame period of the video to stream, in ms
    static int VIDEO_LENGTH = 500; //length of the video in frames

    
    private BufferedReader RTSPBufferedReader;
	private PrintWriter RTSPBufferedWriter;
	private AdminDb adminDb;
	static String VideoFileName; //video file requested from the client
    static int RTSP_ID = 123456; //ID of the RTSP session
    int RTSPSeqNb = 0; //Sequence number of RTSP messages within the session

    //RTP variables:
    //----------------
    DatagramSocket RTPsocket; //socket to be used to send and receive UDP packets
    DatagramPacket senddp; //UDP packet containing the video frames
    
    //RTCP variables
    //----------------
    static int RTCP_RCV_PORT = 19001; //port where the client will receive the RTP packets
    static int RTCP_PERIOD = 400;     //How often to check for control events
    DatagramSocket RTCPsocket;
    RtcpReceiver rtcpReceiver;
    int congestionLevel;
    
    Timer timer;    //timer used to send the images at the video frame rate
    byte[] buf;     //buffer used to store the images to send to the client 
    int sendDelay;  //the delay to send images over the wire. Ideally should be
                    //equal to the frame rate of the video file, but may be 
                    //adjusted when congestion is detected.

    InetAddress ClientIPAddr;   //Client IP address
    int RTP_dest_port = 0;      //destination port for RTP packets  (given by the RTSP Client)
    int RTSP_dest_port = 0;
    
    final static String CRLF = "\r\n";

    ImageTranslator imgTranslator;
	CongestionController cc;

	public ClienteRemoto (Socket socket) throws Exception{
		RTSPsocket = socket;
		RTSPBufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream())); 
		RTSPBufferedWriter = new PrintWriter(socket.getOutputStream(), true);
		ClientIPAddr=RTSPsocket.getInetAddress();
        state = INIT;
        adminDb= new AdminDb();

		//init RTP sending Timer
        sendDelay = FRAME_PERIOD;
        timer = new Timer(sendDelay, this);
        timer.setInitialDelay(0);
        timer.setCoalesce(true);
        
        //allocate memory for the sending buffer
        buf = new byte[20000]; 

        //init the RTCP packet receiver
        rtcpReceiver = new RtcpReceiver(RTCP_PERIOD, this);

        //Video encoding and quality
        imgTranslator = new ImageTranslator(0.8f);

		//init congestion controller
		cc = new CongestionController(600,this);
		
		new Thread(){
			@Override
			public void run() {
				while(true){
					try {
						procesar();
					} 
					catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

			private void procesar() throws Exception {
				if(state==INIT){
					String ln =RTSPBufferedReader.readLine();
					String[]sln = ln.split("::");
					if(ln.startsWith("REGISTRAR")){
						if(adminDb.registrarse(sln[1], sln[2])){
							RTSPBufferedWriter.write("OK");
						}
						else{
							RTSPBufferedWriter.write("ERROR");
						}
					}
					else if(ln.startsWith("INGRESAR")){
						if(adminDb.login(sln[1], sln[2])){
							RTSPBufferedWriter.write("OK");
						}
						else{
							RTSPBufferedWriter.write("ERROR");
						}
					}
					else{
						System.out.println(ln);
					}
				}
				//Wait for the SETUP message from the client
		        int request_type;
		        boolean done = false;
		        while(!done) {
		            request_type = parse_RTSP_request(); //blocking
		            if (request_type == SETUP) {
		                done = true;

		                //update RTSP state
		                state = READY;
		                System.out.println("New RTSP state: READY");
		             
		                //Send response
		                send_RTSP_response();
		             
		                try {
							//init the VideoStream object:
							video = new VideoStream(VideoFileName);

							//init RTP and RTCP sockets
							RTPsocket = new DatagramSocket();
							RTCPsocket = new DatagramSocket(RTCP_RCV_PORT);
						} catch (SocketException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		            }
		            
		        }

		        //loop to handle RTSP requests
		        while(true) {
		            //parse the request
		            request_type = parse_RTSP_request(); //blocking
		                
		            if ((request_type == PLAY) && (state == READY)) {
		                //send back response
		                send_RTSP_response();
		                //start timer
		                timer.start();
		                rtcpReceiver.startRcv();
		                //update state
		                state = PLAYING;
		                System.out.println("New RTSP state: PLAYING");
		            }
		            else if ((request_type == PAUSE) && (state == PLAYING)) {
		                //send back response
		                send_RTSP_response();
		                //stop timer
		                timer.stop();
		                rtcpReceiver.stopRcv();
		                //update state
		                state = READY;
		                System.out.println("New RTSP state: READY");
		            }
		            else if (request_type == TEARDOWN) {
		                //send back response
		                send_RTSP_response();
		                //stop timer
		                timer.stop();
		                rtcpReceiver.stopRcv();
		                //close sockets
		                try {
							RTSPsocket.close();
							RTPsocket.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

		                System.exit(0);
		            }
		            else if (request_type == DESCRIBE) {
		                System.out.println("Received DESCRIBE request");
		                send_RTSP_describe();
		            }
		        }
		        
			}
		}.start();
	}
	 //------------------------------------
    //Parse RTSP Request
    //------------------------------------
    private int parse_RTSP_request()
    {
        int request_type = -1;
        try { 
            //parse request line and extract the request_type:
            String RequestLine = RTSPBufferedReader.readLine();
            System.out.println("RTSP Server - Received from Client:");
            System.out.println(RequestLine);

            StringTokenizer tokens = new StringTokenizer(RequestLine);
            String request_type_string = tokens.nextToken();

            //convert to request_type structure:
            if ((new String(request_type_string)).compareTo("SETUP") == 0)
                request_type = SETUP;
            else if ((new String(request_type_string)).compareTo("PLAY") == 0)
                request_type = PLAY;
            else if ((new String(request_type_string)).compareTo("PAUSE") == 0)
                request_type = PAUSE;
            else if ((new String(request_type_string)).compareTo("TEARDOWN") == 0)
                request_type = TEARDOWN;
            else if ((new String(request_type_string)).compareTo("DESCRIBE") == 0)
                request_type = DESCRIBE;
            else if ((new String(request_type_string)).compareTo("LOGIN")== 0)
            	request_type=LOGIN;
            if (request_type == SETUP) {
                //extract VideoFileName from RequestLine
                VideoFileName = tokens.nextToken();
            }

            //parse the SeqNumLine and extract CSeq field
            String SeqNumLine = RTSPBufferedReader.readLine();
            System.out.println(SeqNumLine);
            tokens = new StringTokenizer(SeqNumLine);
            tokens.nextToken();
            RTSPSeqNb = Integer.parseInt(tokens.nextToken());
        
            //get LastLine
            String LastLine = RTSPBufferedReader.readLine();
            System.out.println(LastLine);

            tokens = new StringTokenizer(LastLine);
            if (request_type == SETUP) {
                //extract RTP_dest_port from LastLine
                for (int i=0; i<3; i++)
                    tokens.nextToken(); //skip unused stuff
                RTP_dest_port = Integer.parseInt(tokens.nextToken());
            }
            else if (request_type == DESCRIBE) {
                tokens.nextToken();
                String describeDataType = tokens.nextToken();
            }
            else {
                //otherwise LastLine will be the SessionId line
                tokens.nextToken(); //skip Session:
                RTSP_ID = Integer.parseInt(tokens.nextToken());
            }
        } catch(Exception ex) {
            System.out.println("Exception caught: "+ex);
            System.exit(0);
        }
      
        return(request_type);
    }

    // Creates a DESCRIBE response string in SDP format for current media
    private String describe() {
        StringWriter writer1 = new StringWriter();
        StringWriter writer2 = new StringWriter();
        
        // Write the body first so we can get the size later
        writer2.write("v=0" + CRLF);
        writer2.write("m=video " + RTSP_dest_port + " RTP/AVP " + MJPEG_TYPE + CRLF);
        writer2.write("a=control:streamid=" + RTSP_ID + CRLF);
        writer2.write("a=mimetype:string;\"video/MJPEG\"" + CRLF);
        String body = writer2.toString();

        writer1.write("Content-Base: " + VideoFileName + CRLF);
        writer1.write("Content-Type: " + "application/sdp" + CRLF);
        writer1.write("Content-Length: " + body.length() + CRLF);
        writer1.write(body);
        
        return writer1.toString();
    }

    //------------------------------------
    //Send RTSP Response
    //------------------------------------
    private void send_RTSP_response() {
        try {
            RTSPBufferedWriter.write("RTSP/1.0 200 OK"+CRLF);
            RTSPBufferedWriter.write("CSeq: "+RTSPSeqNb+CRLF);
            RTSPBufferedWriter.write("Session: "+RTSP_ID+CRLF);
            RTSPBufferedWriter.flush();
            System.out.println("RTSP Server - Sent response to Client.");
        } catch(Exception ex) {
            System.out.println("Exception caught: "+ex);
            System.exit(0);
        }
    }

    private void send_RTSP_describe() {
        String des = describe();
        try {
            RTSPBufferedWriter.write("RTSP/1.0 200 OK"+CRLF);
            RTSPBufferedWriter.write("CSeq: "+RTSPSeqNb+CRLF);
            RTSPBufferedWriter.write(des);
            RTSPBufferedWriter.flush();
            System.out.println("RTSP Server - Sent response to Client.");
        } catch(Exception ex) {
            System.out.println("Exception caught: "+ex);
            System.exit(0);
        }
    }
  //------------------------
    //Handler for timer
    //------------------------
    public void actionPerformed(ActionEvent e) {
        byte[] frame;

        //if the current image nb is less than the length of the video
        if (imagenb < VIDEO_LENGTH) {
            //update current imagenb
            imagenb++;

            try {
                //get next frame to send from the video, as well as its size
                int image_length = video.getnextframe(buf);

                //adjust quality of the image if there is congestion detected
                if (congestionLevel > 0) {
                    imgTranslator.setCompressionQuality(1.0f - congestionLevel * 0.2f);
                    frame = imgTranslator.compress(Arrays.copyOfRange(buf, 0, image_length));
                    image_length = frame.length;
                    System.arraycopy(frame, 0, buf, 0, image_length);
                }

                //Builds an RTPpacket object containing the frame
                RTPpacket rtp_packet = new RTPpacket(MJPEG_TYPE, imagenb, imagenb*FRAME_PERIOD, buf, image_length);
                
                //get to total length of the full rtp packet to send
                int packet_length = rtp_packet.getlength();

                //retrieve the packet bitstream and store it in an array of bytes
                byte[] packet_bits = new byte[packet_length];
                rtp_packet.getpacket(packet_bits);

                //send the packet as a DatagramPacket over the UDP socket 
                senddp = new DatagramPacket(packet_bits, packet_length, ClientIPAddr, RTP_dest_port);
                RTPsocket.send(senddp);

                System.out.println("Send frame #" + imagenb + ", Frame size: " + image_length + " (" + buf.length + ")");
                //print the header bitstream
                rtp_packet.printheader();

                System.out.println("Send frame #" + imagenb);
            }
            catch(Exception ex) {
                System.out.println("Exception caught: "+ex);
                System.exit(0);
            }
        }
        else {
            //if we have reached the end of the video file, stop the timer
            timer.stop();
            rtcpReceiver.stopRcv();
        }
    }

}
