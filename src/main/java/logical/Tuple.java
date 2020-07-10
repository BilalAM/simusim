package logical;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Tuple {
    private int tupleId;

    public void setDataLength(double dataLength) {
        this.dataLength = dataLength;
    }

    private double dataLength;
    private String source;
    private String destination;
    private String state;

    public Tuple(){

    }
    public Tuple(int tupleId, String source, String destination,int seederValue) {
        this.tupleId = tupleId;
        this.source = source;
        this.destination = destination;
        this.setState(TupleState.SENSOR_TO_FOGNODE);
        this.setDataLength(randomGenerator(seederValue));
    }

    public Tuple(int tupleId, String source, String destination,double dataLength) {
        this.tupleId = tupleId;
        this.source = source;
        this.destination = destination;
        this.setState(TupleState.SENSOR_TO_FOGNODE);
        this.setDataLength(dataLength);
    }



    double randomGenerator(long seed) {
        Random generator = new Random(seed);
        double num = generator.nextInt(1000);
        return num;
    }

    private int calculateNextValue(int seederValue){
        return (seederValue/10)*100;
    }

    public int getTupleId() {
        return tupleId;
    }

    public void setTupleId(int tupleId) {
        this.tupleId = tupleId;
    }

    public double getDataLength() {
        return dataLength;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }


    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }


    @Override
    public String toString(){
        return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
                .append("ID",tupleId)
                .append("source",source)
                .append("destination",destination)
                .append("data-length",dataLength)
                .toString();
    }


    public void setState(TupleState tupleState) {
        this.state = tupleState.toString();
    }
}
