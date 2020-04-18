package logical;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.concurrent.ThreadLocalRandom;

public class Tuple {
    private int tupleId;
    private double dataLength;
    private String source;
    private String destination;
    private String state;

    public Tuple(){

    }
    public Tuple(int tupleId, String source, String destination) {
        this.tupleId = tupleId;
        this.dataLength = ThreadLocalRandom.current().nextDouble(100,500);
        this.source = source;
        this.destination = destination;
        this.setState(TupleState.SENSOR_TO_FOGNODE);
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
