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
package testing;

import logical.Tuple;
import org.apache.commons.text.StringSubstitutor;
import physical.Sensor;

import java.util.*;

public class TestDriverUtils {

    public static List<Tuple> createTuples(int size,Random randomizer) {
        List<Tuple> tuples = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            tuples.add(new Tuple(i, "source-A", "source-B",randomizer.nextInt(1000)));
        }
        return sortByMIPS(tuples);
    }

    /**
     * SJF sorting mechanism.
     * @param listOfTuples
     * @return
     */
    public static List<Tuple> sortByMIPS(List<Tuple> listOfTuples) {
        Collections.sort(listOfTuples, new Comparator<Tuple>() {
            public int compare(Tuple t1, Tuple t2) {
                return Double.compare(t1.getDataLength(), t2.getDataLength());
            }
        });
        return listOfTuples;
    }
    public static Sensor createSensor(int id, String name) {
        Sensor sensor = new Sensor();
        sensor.setSensorName(name);
        return sensor;
    }

    public static String replaceString(String toBeReplaced, Map<String, String> map) {
        StringSubstitutor substitutor = new StringSubstitutor(map);
        return substitutor.replace(toBeReplaced);
    }

    public static double getAverageTime(List<Long> time ){
        long avg = 0;
        for(long l : time){
            avg += l;
        }
        if(avg < 1){
            return 0.0;
        }
        return avg/time.size();
    }

}
