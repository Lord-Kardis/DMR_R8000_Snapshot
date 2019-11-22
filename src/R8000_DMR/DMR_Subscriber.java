package R8000_DMR;

public class DMR_Subscriber {

    private String radioID;
    private String freqError;
    private String power;
    private String symbolDev;
    private String fskError;
    private String magError;



    public DMR_Subscriber(String radioID, String freqError, String power, String symbolDev, String fskError, String magError) {
        this.radioID = radioID;
        this.freqError = freqError;
        this.power = power;
        this.symbolDev = symbolDev;
        this.fskError = fskError;
        this.magError = magError;
    }

    public String getRadioID() {
        return radioID;
    }

    public void setRadioID(String radioID) {
        this.radioID = radioID;
    }

    public String getFreqError() {
        return freqError;
    }

    public void setFreqError(String freqError) {
        this.freqError = freqError;
    }

    public String getPower() {
        return power;
    }

    public void setPower(String power) {
        this.power = power;
    }

    public String getSymbolDev() {
        return symbolDev;
    }

    public void setSymbolDev(String symbolDev) {
        this.symbolDev = symbolDev;
    }

    public String getFskError() {
        return fskError;
    }

    public void setFskError(String fskError) {
        this.fskError = fskError;
    }

    public String getMagError() {
        return magError;
    }

    public void setMagError(String magError) {
        this.magError = magError;
    }

    @Override
    public String toString() {
        return "DMR_Subscriber{" +
                "radioID='" + radioID + '\'' +
                ", freqError='" + freqError + '\'' +
                ", power='" + power + '\'' +
                ", symbolDev='" + symbolDev + '\'' +
                ", fskError='" + fskError + '\'' +
                ", magError='" + magError + '\'' +
                '}';
    }

    public String toCSV(){
        return   radioID + "," +
                freqError + "," +
                power + "," +
                symbolDev + "," +
                fskError + "," +
                magError;
    }
}
