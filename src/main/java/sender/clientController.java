package sender;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;

//import java.awt.*;
//import java.awt.event.ActionEvent;
import javax.jms.*;
import javax.naming.InitialContext;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ResourceBundle;

public class clientController implements Initializable{

    ObservableList<Integer> listGraph = FXCollections.observableArrayList();
    ObservableList<Integer> listMenge = FXCollections.observableArrayList();

    @FXML
    private ChoiceBox graph;

    @FXML
    private ChoiceBox menge;

    @FXML
    private Button button;

    private ObjectMessage objMessage;
    //private QueueSender sender;
    //private QueueConnection con;
    private TopicPublisher sender;
    private TopicConnection con;

    private TextMessage txtMsg;

    @FXML
    private void sendMsg(ActionEvent event){
        try {   //Create and start connection

            MessageData data;
            int i = 0;

            while (i < (Integer)menge.getValue()) {

                //7) send message
                data = new MessageData((Integer)i);

                objMessage.setObject(data);
                //txtMsg.setText("hallo vom Client" +i);

                try {
                    //sender.send(txtMsg);
                    sender.send(objMessage);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                System.out.println("msg is sent" + i);
                System.out.println("Message successfully sent.");
                ++i;
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void initialize(URL location, ResourceBundle resources) {
        loadData();

        try {   //Create and start connection
            InitialContext ctx = new InitialContext();
            TopicConnectionFactory f = (TopicConnectionFactory) ctx.lookup("jms/topicFactory");
            //QueueConnectionFactory f = (QueueConnectionFactory) ctx.lookup("jms/topicFactory");
            con = f.createTopicConnection();
            //con = f.createQueueConnection();
            //producer.setDeliveryMode(DeliveryMode.PERSISTENT);

            con.start();
            //2) create queue session
            //QueueSession ses = con.createQueueSession(false, Session.SESSION_TRANSACTED);
            TopicSession ses = con.createTopicSession(false, Session.SESSION_TRANSACTED);
            //3) get the Queue object

            Topic t = (Topic) ctx.lookup("Topic1");

            //4)create QueueSender object
            //sender = ses.createSender(t);
            sender = ses.createPublisher(t);

            sender.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
            //sender.


            objMessage = ses.createObjectMessage();
            txtMsg = ses.createTextMessage();

        } catch (Exception e) {
            System.out.println(e);
        }
    }
    private void loadData(){

        listGraph.addAll(1,2);
        graph.getItems().addAll(listGraph);
        graph.setValue(1);


        listMenge.addAll(1,2,3,4,5,6,7,8,9);
        menge.getItems().addAll(listMenge);
        menge.setValue(5);

    }
@FXML
    public void closeClean(ActionEvent actionEvent) {
        try {
            con.close();
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
