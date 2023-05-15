package se.atg.service.harrykart.java.rest;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.atg.service.harrykart.java.rest.bo.HarryKartResponse;
import se.atg.service.harrykart.java.service.HarryKartService;

@RestController
@RequestMapping("/java/api")
@Validated
public class HarryKartController {

    private final HarryKartService harryKartService;

    public HarryKartController(HarryKartService harryKartService){
        this.harryKartService = harryKartService;
    }

    @PostMapping(path = "/play", consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HarryKartResponse> playHarryKart(@RequestBody String harryKartRequest) {
        return ResponseEntity.ok().body(harryKartService.calculateHarryKartRankings(harryKartRequest));
    }

}
