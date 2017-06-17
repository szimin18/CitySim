package pl.egu.agh.citysim.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
import static java.util.stream.Collectors.toList;

@Data
@ToString(of = {"coordinates", "name"})
@EqualsAndHashCode(of = {"coordinates", "name"})
@RequiredArgsConstructor
public class Crossing {
    private static final double INITIAL_LIGHTS_TIME_MILISECONDS = 3000;

    private final Coordinates coordinates;
    private final String name;
    private List<Road> inRoads = newArrayList();
    private Map<Crossing, Road> outRoads = newHashMap();
    private List<Double> lightsTimes = newArrayList();

    private int greenIndex = 0;
    private double greenPassedMiliseconds = 0;

    public void initialize() {
        lightsTimes.addAll(Stream.generate(() -> INITIAL_LIGHTS_TIME_MILISECONDS).limit(inRoads.size()).collect(toList()));
    }

    public void passed(final double miliseconds) {
        if (!lightsTimes.isEmpty()) {
            double maxGreenTime = lightsTimes.get(greenIndex);
            greenPassedMiliseconds += miliseconds;
            while (greenPassedMiliseconds > maxGreenTime) {
                greenPassedMiliseconds -= maxGreenTime;
                greenIndex = (greenIndex + 1) % lightsTimes.size();
                maxGreenTime = lightsTimes.get(greenIndex);
            }
        }
    }

    public boolean isGreen(final Road road) {
        return inRoads.indexOf(road) == greenIndex;
    }
}
