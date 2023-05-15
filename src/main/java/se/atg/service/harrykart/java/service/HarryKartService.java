package se.atg.service.harrykart.java.service;

import org.springframework.stereotype.Service;
import se.atg.service.harrykart.java.config.HarryKartProperties;
import se.atg.service.harrykart.java.exception.ServiceError;
import se.atg.service.harrykart.java.model.HarryKart;
import se.atg.service.harrykart.java.model.Lane;
import se.atg.service.harrykart.java.model.Loop;
import se.atg.service.harrykart.java.model.Participant;
import se.atg.service.harrykart.java.rest.bo.HarryKartResponse;
import se.atg.service.harrykart.java.rest.bo.Rank;
import se.atg.service.harrykart.java.utils.XmlUtility;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class HarryKartService {

    private final HarryKartProperties harryKartProperties;
    private final XmlUtility xmlUtility;

    public HarryKartService(HarryKartProperties harryKartProperties, XmlUtility xmlUtility) {
        this.harryKartProperties = harryKartProperties;
        this.xmlUtility = xmlUtility;
    }

    public HarryKartResponse calculateHarryKartRankings(String harryKartRequest) {
        xmlUtility.validate(harryKartRequest, harryKartProperties.getSchemaFileName());
        HarryKart harryKart = xmlUtility.parseXml(harryKartRequest, HarryKart.class);
        validateNumberOfHarryKartLoops(harryKart);
        return calculateRankings(harryKart);
    }

    private HarryKartResponse calculateRankings(HarryKart harryKart){
        Map<String, Double> results = harryKart.getStartList()
                .stream()
                .collect(
                        Collectors.toMap(Participant::getName,
                                participant -> {
                                    return calculateTimePerParticipant(harryKart, participant);
                                })
                );
        List<Map.Entry<String, Double>> sortedResultSet = results.entrySet().stream()
                .sorted(Map.Entry.comparingByValue()).limit(harryKartProperties.getTopNParticipant()).collect(Collectors.toList());
        return mapToHarryKartResponse(sortedResultSet);
    }

    private Double calculateTimePerParticipant(HarryKart harryKart, Participant participant) {
        AtomicReference<Integer> accumulatedSpeed = new AtomicReference<>(10);
        AtomicReference<Double> accumulatedTime = new AtomicReference<>(Double.valueOf(harryKartProperties.getTrackLength() / participant.getBaseSpeed()));
        harryKart.getPowerUps().forEach(loop ->{
            Lane selectedLane = getLaneByParticipantAndLoop(participant, loop);
            accumulatedSpeed.updateAndGet(value -> value+selectedLane.getPowerValue());
            if(accumulatedSpeed.get() <= 0){
                accumulatedTime.updateAndGet(v-> Double.MAX_VALUE);
            }else{
                accumulatedTime.updateAndGet(v -> v + Double.valueOf(harryKartProperties.getTrackLength() / accumulatedSpeed.get()));
            }
        });
        return accumulatedTime.get();
    }

    private static Lane getLaneByParticipantAndLoop(Participant participant, Loop loop) {
        return loop.getLanes()
                .stream()
                .filter(lane -> lane.getNumber().equals(participant.getLane()))
                .findFirst()
                .orElseThrow(() -> ServiceError.NO_MATCHED_LANE_IN_POWER_UPS.buildException());
    }

    private HarryKartResponse mapToHarryKartResponse(List<Map.Entry<String, Double>> sortedResultSet) {
        List<Rank> rankingSet = IntStream.range(0, sortedResultSet.size())
                .mapToObj(elem -> Rank.builder().horse(sortedResultSet.get(elem).getKey()).position(elem+1).build())
                .sorted(Comparator.comparing(Rank::getPosition)).collect(Collectors.toList());
        return HarryKartResponse.builder().ranking(rankingSet).build();
    }

    private void validateNumberOfHarryKartLoops(HarryKart harryKart) {
        if (harryKart.getNumberOfLoops() != harryKart.getPowerUps().size() + 1) {
            throw ServiceError.INVALID_NUMBER_OF_LOOPS.buildException();
        }
    }
}
