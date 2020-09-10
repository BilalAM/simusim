/**
 * MIT License
 * <p>
 * Copyright (c) 2019 Bilal Asif Mirza
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package logical;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.time.Duration;
import java.time.Instant;
import java.util.Random;

public class Tuple {
    private int tupleId;

    public void setDataLength(double dataLength) {
        this.dataLength = dataLength;
    }

    private double dataLength;
    private String source;
    private String destination;
    private String state;
    private Instant instant = null;


    public Tuple(){

    }
    public Tuple(int tupleId, String source, String destination,int seederValue) {
        this.tupleId = tupleId;
        this.source = source;
        this.destination = destination;
        this.setState(TupleState.SENSOR_TO_FOGNODE);
        this.setDataLength(randomGenerator(seederValue));
        instant = Instant.now();
    }

    public Tuple(int tupleId, String source, String destination,double dataLength) {
        this.tupleId = tupleId;
        this.source = source;
        this.destination = destination;
        this.setState(TupleState.SENSOR_TO_FOGNODE);
        this.setDataLength(dataLength);
    }

    public Duration objectAgeAsDuration () {
        Duration d = Duration.between ( instant , Instant.now () );
        return d;
    }

    public long getAgeInMilliseconds () {
        Duration d = this.objectAgeAsDuration ();
        return d.toMillis();
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
