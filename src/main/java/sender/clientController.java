package sender;

import enums.DayTime;
import enums.Guis;
import enums.Koerbe;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

//import java.awt.*;
//import java.awt.event.ActionEvent;
import javax.jms.*;
import javax.naming.InitialContext;
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
    @FXML
    private TextField korbstandId;
    @FXML
    private Button sendMessageButton;
    @FXML
    private Label wartezeitLabel;
    @FXML
    private Label anzahlNachrichtenLabel;
    @FXML
    private Label datumBisLabel;
    @FXML
    private Label neuerKorbnameLabel;
    @FXML
    private Button korbHinzufuegenButton;
    @FXML
    private Label korbstandLabel;
    @FXML
    private Button senKorbstandButton;
    @FXML
    private Label datumVonLabel;

    private int measuredKorbstand = 0;
    private ObjectMessage objMessage;
    private TopicPublisher sender;
    private TopicConnection connection;

    @FXML
    private void adjustGui(Randoms value){

        if(value.equals(Randoms.SINGLE)){
            datePickerBis.setVisible(false);
            hoursTo.setVisible(false);
            messageCounter.setVisible(false);
            sendMessageButton.setVisible(true);
            sleepTimer.setVisible(false);
            wartezeitLabel.setVisible(false);
            anzahlNachrichtenLabel.setVisible(false);
            datumBisLabel.setVisible(false);
            neuerKorbnameLabel.setVisible(true);
            neuerKorb.setVisible(true);
            korbHinzufuegenButton.setVisible(true);
            korbstandLabel.setVisible(false);
            korbstandId.setVisible(false);
            senKorbstandButton.setVisible(false);
            datumVonLabel.setText("Zeit");
            korbAuswahl.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            guiAuswahl.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        } else if(value.equals(Randoms.RANDOM)){
            datePickerBis.setVisible(true);
            hoursTo.setVisible(true);
            messageCounter.setVisible(true);
            sendMessageButton.setVisible(true);
            sleepTimer.setVisible(true);
            wartezeitLabel.setVisible(true);
            anzahlNachrichtenLabel.setVisible(true);
            datumBisLabel.setVisible(true);
            neuerKorbnameLabel.setVisible(true);
            neuerKorb.setVisible(true);
            korbHinzufuegenButton.setVisible(true);
            korbstandLabel.setVisible(false);
            korbstandId.setVisible(false);
            senKorbstandButton.setVisible(false);
            datumVonLabel.setText("Von");
            korbAuswahl.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            guiAuswahl.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        } else if(value.equals(Randoms.KORBSTAND)){
            datePickerBis.setVisible(false);
            hoursTo.setVisible(false);
            messageCounter.setVisible(false);
            sendMessageButton.setVisible(false);
            sleepTimer.setVisible(false);
            wartezeitLabel.setVisible(false);
            anzahlNachrichtenLabel.setVisible(false);
            datumBisLabel.setVisible(false);
            neuerKorbnameLabel.setVisible(false);
            neuerKorb.setVisible(false);
            korbHinzufuegenButton.setVisible(false);
            korbstandLabel.setVisible(true);
            korbstandId.setVisible(true);
            senKorbstandButton.setVisible(true);
            datumVonLabel.setText("Zeit");
            korbAuswahl.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            guiAuswahl.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        }


    }

    @FXML
    private void sendKorbstandUpdate(ActionEvent event) {

        ObservableList<String> ausgewaehlteKoerbe = korbAuswahl.getSelectionModel().getSelectedItems();

        ObservableList<Guis> ausgewaehlteGuis = guiAuswahl.getSelectionModel().getSelectedItems();

        Korbstand korbstand = new Korbstand();
        korbstand.setGui(ausgewaehlteGuis.get(0).getTechnischerSchluessel());
        korbstand.setKorb(ausgewaehlteKoerbe.get(0).toString());

        LocalTime startTime = getLocalTime(((DayTime) hoursFrom.getValue()).getHour(), 0);
        LocalDateTime startDateTime = LocalDateTime.of(datePickerVon.getValue(), startTime);
        Timestamp startDateTimeStamp = Timestamp.valueOf(startDateTime);

        korbstand.setTime(startDateTimeStamp);
        korbstand.setKorbstand(Integer.parseInt(korbstandId.getText()));

        try {
            objMessage.setObject(korbstand);
        } catch (JMSException e) {
            e.printStackTrace();
        }

        try {
            sender.send(objMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void korbEinfuegen(ActionEvent event) {
        if (neuerKorb.getCharacters() != null) {

            String neuerKorbString = neuerKorb.getCharacters().toString();
            neuerKorbString = neuerKorbString.replaceAll("\\s+", "_");

            korbAuswahl.getItems().add(neuerKorbString);
        }
    }

    @FXML
    private void sendMsg(ActionEvent event) {

        long sleepTimer = Long.parseLong(this.sleepTimer.getText());
        int anzahlMessages = Integer.parseInt(this.messageCounter.getText());

        LocalTime startTime = getLocalTime(((DayTime) hoursFrom.getValue()).getHour(), 0);
        LocalDateTime startDateTime = LocalDateTime.of(datePickerVon.getValue(), startTime);
        Timestamp startDateTimeStamp = Timestamp.valueOf(startDateTime);

        LocalTime endTime = getLocalTime(((DayTime) hoursTo.getValue()).getHour(), 0);
        LocalDateTime endDateTime = LocalDateTime.of(datePickerBis.getValue(), endTime);
        Timestamp endDateTimeStamp = Timestamp.valueOf(endDateTime);

        try {

            MessageData data;

            if (graph.getValue().equals(Randoms.SINGLE)) {

                UUID uuid = UUID.randomUUID();
                String randomUUIDString = uuid.toString();

                data = new MessageData(randomUUIDString);
                this.setMessageData(data);

                data.setTime(startDateTimeStamp);

                objMessage.setObject(data);

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
                    try {
                        sender.publish(objMessage);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Thread.sleep(sleepTimer);
                }
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private LocalTime getLocalTime(int hour, int minute) {
        if (hour == 24) {
            return LocalTime.of(23, 59);
        }
        return LocalTime.of(hour, 0);
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
        int korbNumber = this.intGenerator(ausgewaehlteKoerbe.size() - 1);

        ObservableList<Guis> ausgewaehlteGuis = guiAuswahl.getSelectionModel().getSelectedItems();
        int guiNumber = this.intGenerator(ausgewaehlteGuis.size() - 1);


        data.setKorb(ausgewaehlteKoerbe.get(korbNumber).toString());
        data.setGui(ausgewaehlteGuis.get(guiNumber).getTechnischerSchluessel());

        data.setAmbulant(this.booleanGenerator());
        data.setStationaer(this.booleanGenerator());
        data.setPartnerartObergruppe(99);
        data.setPlz("6005");

        byte isMessageIncoming = this.booleanGenerator();
        if(measuredKorbstand == 0){
            isMessageIncoming = 1;
        }
        data.setIn(isMessageIncoming);
        data.setOut((byte) Math.abs(1 - data.getIn()));
        measuredKorbstand += data.getIn();
        measuredKorbstand -= data.getOut();

        data.setKorbStand(5);
    }

    private byte booleanGenerator() {
        return (byte) Math.round(Math.random());
    }

    private int intGenerator(int max) {
        return (int) (Math.random() * (max + 1));
    }

    public void initialize(URL location, ResourceBundle resources) {
        loadData();

        try {
            InitialContext ctx = new InitialContext();
            TopicConnectionFactory f = (TopicConnectionFactory) ctx.lookup("jms/topicFactory");
            connection = f.createTopicConnection();
            connection.start();
            TopicSession ses = connection.createTopicSession(false, Session.SESSION_TRANSACTED);
            Topic t = (Topic) ctx.lookup("Topic1");
            sender = ses.createPublisher(t);
            sender.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

            objMessage = ses.createObjectMessage();

        } catch (Exception e) {
            System.out.println(e);
        }
        korbstandId.textProperty().addListener((ChangeListener<String>) (ov, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                korbstandId.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        sleepTimer.textProperty().addListener((ChangeListener<String>) (ov, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                sleepTimer.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        messageCounter.textProperty().addListener((ChangeListener<String>) (ov, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                messageCounter.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        graph.valueProperty().addListener((ChangeListener<Randoms>) (ov, oldValue, newValue) -> {
            adjustGui(newValue);
        });
        adjustGui(Randoms.SINGLE);
        guiAuswahlList.addListener((ListChangeListener<Guis>) (lsit) -> {
            this.measuredKorbstand = 0;
        });
        korbAuswahlList.addListener((ListChangeListener<Koerbe>) (list) -> {
            this.measuredKorbstand = 0;
        });
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
        korbAuswahl.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        korbAuswahl.getSelectionModel().select(0);

        guiAuswahlList.addAll(Guis.values());
        guiAuswahl.getItems().addAll(guiAuswahlList);
        guiAuswahl.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        guiAuswahl.getSelectionModel().select(0);

        datePickerVon.setValue(LocalDate.now());
        datePickerBis.setValue(LocalDate.now());

        sleepTimer.setText("100");
        messageCounter.setText("100");
    }
}
