package R8000_DMR;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.sql.Array;
import java.util.*;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;


public class Controller implements Initializable {


    public Button btn_SetTx;
    public Button btn_SetRx;
    public ListView lst_Tested;
    public TextField txt_fileName;
    private R8000 r8000;

    public Tab tab_Test;
    public Tab tab_Tune;


    public ImageView img_Logo;
    public TextField txt_TxFreq;
    public TextField txt_RxFreq;
    public TextField txt_RadID;
    public TextField txt_FreqError;
    public TextField txt_Power;
    public TextField txt_SymDev;
    public TextField txt_FSKError;
    public TextField txt_MagError;
    public TextField txt_TuneFreq;
    public TextField txt_FreqErrorTune;


    public Button btn_Commit;
    public Button btn_Tx;
    public Button btn_Rx;
    public Button btn_startTune;
    public Button btn_setTuneFreq;

    private ArrayList<DMR_Subscriber> dataList;
    private ObservableList<String> history;
    private LinkedHashMap<Integer,String[]> dataMap;
    private String mode;

    private boolean isTesting;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        isTesting = false;
        r8000 = new R8000();

        r8000.initCommand("SET SYSTEM:Mode Request=Duplex");
        //r8000.initCommand("GO SYSTEM:DMR");
        modeCheck("DMR");
        //r8000.initCommand("SET TEST:Test Mode=DMR");

        dataList = new ArrayList<>();  //change to loading list from file
        history = FXCollections.observableArrayList();
        dataMap = new LinkedHashMap<>();
        lst_Tested.setItems(history);
        lst_Tested.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2){
               String radioHistory = (String) lst_Tested.getSelectionModel().getSelectedItem();
               //ToDo Write popup referencing radioHistory and extrapolating the data.
            }
        });

        txt_TxFreq.setText(r8000.sendCommand("GET RF:Monitor Frequency").substring(0,9));
        txt_RxFreq.setText(r8000.sendCommand("GET RF:Generate Frequency").substring(0,9));

    }

    public void commit(ActionEvent actionEvent) {
        DMR_Subscriber radio = new DMR_Subscriber(txt_RadID.getText(), txt_FreqError.getText(), txt_Power.getText(), txt_SymDev.getText(),
                txt_FSKError.getText(), txt_MagError.getText());
        String[] data = radio.toCSV().split(",");
            dataList.add(radio);
              //add-open file and append CSV value.
            dataMap.put(parseInt(txt_RadID.getText()),data);
            history.add(txt_RadID.getText());


            String filename = txt_fileName.getText();


            if (filename.substring(filename.length()-4).equalsIgnoreCase(".csv")) {
                CsvFileWriter.writeCsvFile(filename, radio.toCSV(), "Radio ID, Freq Error, Power, Symbol Dev, FSK Error, Mag Error");
            }else {
                filename+=".csv";
                txt_fileName.setText(filename);
                CsvFileWriter.writeCsvFile( filename, radio.toCSV(), "Radio ID, Freq Error, Power, Symbol Dev, FSK Error, Mag Error");
            }


    }

    public void testTx(ActionEvent actionEvent) {
        modeCheck("DMR");
        r8000.initCommand("GO SYSTEM:DMR");

        try {
            Thread.sleep(500L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        txt_RadID.setText(r8000.sendCommand("GET DMR:Source ID"));
        r8000.sendCommand("GET MONITOR:Input Level", 200L);
            String powerdbm = r8000.getLastResult()[1];
            double watts = (Math.pow(10, (Double.parseDouble(powerdbm)/10d))/1000d);
            System.out.println("Power in dbm= " + powerdbm);
            System.out.println("Power in Watts= " + watts);
        txt_Power.setText(String.valueOf(watts));

        txt_FreqError.setText(r8000.sendCommand("GET MONITOR:Frequency Error"));

        txt_SymDev.setText(r8000.sendCommand("GET DMR:Symbol Deviation"));

        txt_MagError.setText(r8000.sendCommand("GET DMR:Magnitude Error"));

        txt_FSKError.setText(r8000.sendCommand("GET DMR:FSK Error"));


    }

    public void textRx(ActionEvent actionEvent) {
    }

    public void setTxFreq(ActionEvent actionEvent) {

        r8000.initCommand("SET DISPLAY:Monitor Frequency=" + txt_TxFreq.getText() + " MHz");

    }

    public void setRxFreq(ActionEvent actionEvent) {

        r8000.initCommand("SET RF:Generate Frequency=" + txt_RxFreq.getText() + " MHz");

    }


    public void goTune(Event event) {
        modeCheck("Standard");
    }

    public void goTestDigital(Event event){
        modeCheck("DMR");
    }


    public void setTxTuneFreq(ActionEvent actionEvent) {

        r8000.initCommand("SET DISPLAY:Monitor Frequency=" + txt_TuneFreq.getText() + " MHz");
    }


    public void startTune(){

    //this button doesn't do anything.
        //Supposed to start a refresh of frequency error text field for radio tuning.

    }


    public void updateFreqError(){

            txt_FreqErrorTune.setText(r8000.sendCommand("GET MONITOR:Frequency Error",500L));

    }


    //Check to verify monitor is in correct mode.  Test is not case sensitive but the the command is.
    //Valid strings = DMR, Standard, P25, NXDN
    private void modeCheck(String desiredMode){
        if (r8000==null){
            return;
        }

        String currentMode = r8000.sendCommand("GET TEST:Test Mode");

        if (!desiredMode.equalsIgnoreCase(currentMode)){
            r8000.initCommand("SET TEST:Test Mode=" + desiredMode);
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }

    public void functionKeyPress(KeyEvent keyEvent) {

        if (keyEvent.getEventType() == KeyEvent.KEY_PRESSED && keyEvent.getCode() == KeyCode.F5){
            testTx(null);
        }else if (keyEvent.getEventType() == KeyEvent.KEY_PRESSED && keyEvent.getCode() == KeyCode.F4){
            commit(null);
        }
    }
}
