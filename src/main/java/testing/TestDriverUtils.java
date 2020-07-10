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
