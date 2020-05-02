package testing;

import logical.Tuple;
import org.apache.commons.text.StringSubstitutor;
import physical.Sensor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TestDriverUtils {

    public static List<Tuple> createTuples(int size) {
        List<Tuple> tuples = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            tuples.add(new Tuple(i, "source-A", "source-B"));
        }
        return tuples;
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

}
