package sender;

import enums.DayTime;
import enums.Guis;
import enums.Koerbe;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

//import java.awt.*;
//import java.awt.event.ActionEvent;
import javax.jms.*;
import javax.naming.InitialContext;
import javax.swing.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

import enums.Randoms;

public class clientController implements Initializable {

    ObservableList<Randoms> listGraph = FXCollections.observableArrayList();
    ObservableList<DayTime> hoursFromList = FXCollections.observableArrayList();
    ObservableList<DayTime> hoursToList = FXCollections.observableArrayList();
    ObservableList<Koerbe> korbAuswahlList = FXCollections.observableArrayList();
    ObservableList<Guis> guiAuswahlList = FXCollections.observableArrayList();


    @FXML
    private ChoiceBox graph;
    @FXML
    private DatePicker datePickerVon;
    @FXML
    private DatePicker datePickerBis;
    @FXML
    private TextField messageCounter;
    @FXML
    private TextField sleepTimer;
    @FXML
    private ChoiceBox hoursFrom;
    @FXML
    private ChoiceBox hoursTo;
    @FXML
    private ListView korbAuswahl;
    @FXML
    private ListView guiAuswahl;
    @FXML
    private TextField neuerKorb;





    private ObjectMessage objMessage;
    private TopicPublisher sender;
    private TopicConnection con;


    private List<Integer> korbstaende;
    private LocalTime getLocalTime(int hour, int minute){
        if(hour == 24){
            return LocalTime.of(23,59);
        }
        return LocalTime.of(hour,0);
    }
    @FXML
    private void sendKorbstandUpdate(ActionEvent event){
        Korbstand korbstand = new Korbstand();
        korbstand.setGui(1);
        korbstand.setKorb("Korb1");
        korbstand.setTime(new Timestamp(System.currentTimeMillis()));
        korbstand.setKorbstand(150);

        try {
            objMessage.setObject(korbstand);
        } catch (JMSException e) {
            e.printStackTrace();
        }
        System.out.println("new Data");
        System.out.println(korbstand.toString());

        try {
            sender.send(objMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    @FXML
    private void  korbEinfuegen(ActionEvent event){
        if(neuerKorb.getCharacters() != null){
            System.out.println("fdsgdfgdsf");
            System.out.println(neuerKorb.getCharacters());

            String neuerKorbString = neuerKorb.getCharacters().toString();
            neuerKorbString = neuerKorbString.replaceAll("\\s+","_");

            korbAuswahl.getItems().add(neuerKorbString);
        }
    }
private int cou = 0;
    @FXML
    private void sendMsg(ActionEvent event) {

        cou = 0;

        long sleepTimer = Long.parseLong(this.sleepTimer.getText());
        int anzahlMessages = Integer.parseInt(this.messageCounter.getText());

        LocalTime startTime = getLocalTime(((DayTime)hoursFrom.getValue()).getHour(),0);
        LocalDateTime startDateTime = LocalDateTime.of(datePickerVon.getValue(),startTime);
        Timestamp startDateTimeStamp = Timestamp.valueOf(startDateTime);


        LocalTime endTime = getLocalTime(((DayTime)hoursTo.getValue()).getHour(),0);
        LocalDateTime endDateTime = LocalDateTime.of(datePickerBis.getValue(),endTime);
        Timestamp endDateTimeStamp = Timestamp.valueOf(endDateTime);

        korbstaende = new ArrayList<>();
        korbstaende.add(10);
        korbstaende.add(15);
        korbstaende.add(20);
        korbstaende.add(25);
        korbstaende.add(30);
        korbstaende.add(35);
        korbstaende.add(40);
        korbstaende.add(45);

        try {   //Create and start connection


            MessageData data;

            if (graph.getValue().equals(Randoms.SINGLE)) {

                UUID uuid = UUID.randomUUID();
                String randomUUIDString = uuid.toString();

                data = new MessageData(randomUUIDString);
                this.setMessageData(data);

                data.setTime(startDateTimeStamp);


                objMessage.setObject(data);
                System.out.println("new Data");
                System.out.println(data.toString());
                System.out.println("time for performanceanalysis = "+System.currentTimeMillis());
                try {
                    sender.send(objMessage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (graph.getValue().equals(Randoms.RANDOM)) {

                long timeStampStart = startDateTimeStamp.toInstant().toEpochMilli();
                long timeStampEnd = endDateTimeStamp.toInstant().toEpochMilli();


                for (Timestamp timestamp : this.getTimestams(timeStampStart, timeStampEnd, anzahlMessages)) {
                    UUID uuid = UUID.randomUUID();
                    String randomUUIDString = uuid.toString();

                    data = new MessageData(randomUUIDString);
                    this.setMessageData(data);

                    data.setTime(timestamp);

                    objMessage.setObject(data);
                    System.out.println("new Data");
                    System.out.println(data.toString());
                    System.out.println("time for performanceanalysis = "+System.currentTimeMillis());

                    try {
                        sender.publish(objMessage);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    System.out.println("Message successfully sent. and counter = "+cou);
                    cou++;
                    Thread.sleep(sleepTimer);
                }
            }

        } catch (Exception e) {
            System.out.println(e);
        }

    }

    private List<Timestamp> getTimestams(long startTime, long endTime, int anzahl) {

        long stepwidth = (endTime - startTime) / anzahl;
        long randomNumberWithinStepWidth;
        Timestamp randomTimeStamp;

        List<Timestamp> timeStamps = new ArrayList<>();

        for (int i = 1; i <= anzahl; ++i) {
            randomNumberWithinStepWidth = (long) (Math.random() * stepwidth);
            randomTimeStamp = new Timestamp(startTime + randomNumberWithinStepWidth + (i * stepwidth));
            System.out.println(randomTimeStamp.toString());

            Calendar cal = Calendar.getInstance();
            cal.setTime(randomTimeStamp); // compute start of the day for the timestamp
            cal.set(Calendar.HOUR_OF_DAY, randomTimeStamp.getHours());
            cal.set(Calendar.MINUTE, randomTimeStamp.getMinutes());
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            if (cal.getTimeInMillis() > System.currentTimeMillis()) {
               cal.setTime(new Timestamp(System.currentTimeMillis()));
            }

            timeStamps.add(new Timestamp(cal.getTimeInMillis()));
        }
        return timeStamps;
    }

    private void setMessageData(MessageData data) {
        ObservableList<String> ausgewaehlteKoerbe = korbAuswahl.getSelectionModel().getSelectedItems();
        int korbNumber = this.intGenerator(ausgewaehlteKoerbe.size()-1);

        ObservableList<Guis> ausgewaehlteGuis = guiAuswahl.getSelectionModel().getSelectedItems();
        int guiNumber = this.intGenerator(ausgewaehlteGuis.size()-1);


        data.setKorb(ausgewaehlteKoerbe.get(korbNumber).toString());
        data.setGui(ausgewaehlteGuis.get(guiNumber).getTechnischerSchluessel());

        data.setAmbulant(this.booleanGenerator());
        data.setStationaer(this.booleanGenerator());
        data.setPartnerartObergruppe(99);
        data.setPlz("6005");
        data.setIn(this.booleanGenerator());
        data.setOut((byte) Math.abs(1 - data.getIn()));

        /*Integer korbstandTemp = korbstaende.get(korbNumber);
        if (data.getIn() == 1) {
            korbstandTemp += 1;
        } else {
            korbstandTemp -= 1;
        }
*/
        //korbstaende.set(korbNumber, korbstandTemp);
//delete and rnew
        data.setKorbStand(5);
    }

    /**
     * @param anzahl
     * @param stunden
     * @return List enthaelt für jede stunde die eine willkürliche int Zahl die bestimmt wieviele Messages in dieser Stunde versendet werden.
     */
    private List<Integer> getRandomNumbers(int anzahl, int stunden) {
        List<Integer> list = new ArrayList<>();
        int randomNumber;
        int anzahlLeft = anzahl;


        for (int i = 0; i < stunden; ++i) {
            randomNumber = (int) (Math.random() * 2 * anzahl / (stunden - i));
            anzahl -= randomNumber;
            list.add(randomNumber);
        }
        return list;
    }

    private byte booleanGenerator() {
        return (byte) Math.round(Math.random());
    }

    private int intGenerator(int max) {
        return (int) (Math.random() * (max + 1));
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


            objMessage = ses.createObjectMessage();


        } catch (Exception e) {
            System.out.println(e);
        }

    }

    private void loadData() {

        listGraph.addAll(Randoms.values());
        graph.getItems().addAll(listGraph);
        graph.setValue(Randoms.SINGLE);

        hoursFromList.addAll(DayTime.values());
        hoursFrom.getItems().addAll(hoursFromList);
        hoursFrom.setValue(DayTime.ZERO);

        hoursToList.addAll(DayTime.values());
        hoursTo.getItems().addAll(hoursToList);
        hoursTo.setValue(DayTime.TWELVEPM);

        korbAuswahlList.addAll(Koerbe.values());
        List<String> korbStrings = korbAuswahlList.stream().map(a -> a.toString()).collect(Collectors.toList());
        korbAuswahl.getItems().addAll(korbStrings);
        korbAuswahl.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        korbAuswahl.getSelectionModel().selectAll();

        guiAuswahlList.addAll(Guis.values());
        guiAuswahl.getItems().addAll(guiAuswahlList);
        guiAuswahl.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        guiAuswahl.getSelectionModel().selectAll();

        datePickerVon.setValue(LocalDate.now());
        datePickerBis.setValue(LocalDate.now());

        sleepTimer.setText("100");
        messageCounter.setText("100");

    }
}
