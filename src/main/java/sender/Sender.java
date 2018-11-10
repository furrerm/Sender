package sender;

import javax.jms.*;
import javax.naming.InitialContext;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;


public class Sender {
    /*
    public static void main(String[] args) {

        try {   //Create and start connection
            InitialContext ctx = new InitialContext();

            QueueConnectionFactory f = (QueueConnectionFactory) ctx.lookup("jms/queueFactory");

            QueueConnection con = f.createQueueConnection();
            //producer.setDeliveryMode(DeliveryMode.PERSISTENT);

            con.start();
            //2) create queue session
            QueueSession ses = con.createQueueSession(false, Session.SESSION_TRANSACTED);
            //3) get the Queue object

            Queue t = (Queue) ctx.lookup("Queue1");

            //4)create QueueSender object
            QueueSender sender = ses.createSender(t);
            sender.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
            //sender.
            //5) create TextMessage object
            TextMessage msg = ses.createTextMessage();


            ObjectMessage objMessage = ses.createObjectMessage();
            MessageData data;

            //6) write message
            BufferedReader b = new BufferedReader(new InputStreamReader(System.in));



            int i = 1;
            while (true) {
                // System.out.println("Enter Msg, end to terminate:");
                String s = "d";//b.readLine();
                if (s.equals("end"))
                    break;
                msg.setText(s);
                //7) send message

                if (setInt.contains(i)) {
                    data = new MessageData(2);
                } else {
                    data = new MessageData(1);
                }
                objMessage.setObject(data);
                try {
                    sender.send(objMessage);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                System.out.println("msg is sent" + i);
                System.out.println("Message successfully sent.");
                ++i;
                Thread.sleep(1000);
            }

            //8) connection close
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    */
}
