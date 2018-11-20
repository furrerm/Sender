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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
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

    private List<Integer> randomNumbers;

    @FXML
    private void sendMsg(ActionEvent event){
        randomNumbers = this.getRandomNumbers(100,24);

        try {   //Create and start connection


            MessageData data;

            if(graph.getValue().toString().equals("1")) {
                int i = 0;

                while (i < (Integer) menge.getValue()) {
                    //7) send message
                    data = new MessageData((Integer) i);

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
            } else if(graph.getValue().toString().equals("2")){

                int korbstand = 20; //willkürlicher anfangsstand
                short faktor = 1;

                for(Integer i : randomNumbers){
                    System.out.println("i = "+i);
                }
                int o = 0;
                for(Integer i : randomNumbers){
                    faktor *= faktorGenerator();
                    int z = 0;
                    List<Integer> hourDivider = getRandomNumbers(60, i);
                    for(Integer a : hourDivider){
                        System.out.println("a = "+a);

                    }
                    int min = 0;
                    ++o;
                    for(int i2 = 0; i2 < i; i2++){
                        korbstand += faktor;
                        data = new MessageData(i);

                        data.setKorbStand(korbstand);
                        data.setKorb("Korb"+this.intGenerator(7));
                        data.setGui(this.intGenerator(5));
                        data.setAmbulant(this.booleanGenerator());
                        data.setStationaer(this.booleanGenerator());
                        data.setPartnerartObergruppe(99);
                        data.setPlz("6005");
                        data.setIn(this.booleanGenerator());
                        data.setOut(this.booleanGenerator());

                        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

                        Calendar cal = Calendar.getInstance();
                        cal.setTime(timestamp); // compute start of the day for the timestamp
                        cal.set(Calendar.HOUR_OF_DAY, o);
                        cal.set(Calendar.MINUTE, min);
                        cal.set(Calendar.SECOND, 0);
                        cal.set(Calendar.MILLISECOND, 0);

                        min+= hourDivider.get(z++);

                        data.setTime(new Timestamp(cal.getTimeInMillis()));

                        objMessage.setObject(data);


                        try {
                            //sender.send(txtMsg);
                            sender.send(objMessage);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        System.out.println("msg is sent" + i2);
                        System.out.println("Message successfully sent.");
                        //++i;
                        Thread.sleep(200);
                    }
                }

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }
    private List<Integer> getRandomNumbers(int anzahl, int stunden){
        List<Integer> list = new ArrayList<>();
        int randomNumber;
        int anzahlLeft = anzahl;



        for(int i = 0; i < stunden; ++i){
            randomNumber = (int)(Math.random()*2*anzahl / (stunden - i));
            anzahl -= randomNumber;
            list.add(randomNumber);
        }
        return list;
    }
    private byte booleanGenerator(){
        return (byte)Math.round(Math.random());
    }
    private int intGenerator(int max){
        return (int)(Math.random()*(max+1));
    }
    private short faktorGenerator(){
        if(this.booleanGenerator() == 0){
            return -1;
        }
        return 1;
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
