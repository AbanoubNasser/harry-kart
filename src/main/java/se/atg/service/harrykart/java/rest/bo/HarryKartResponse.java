package se.atg.service.harrykart.java.rest.bo;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class HarryKartResponse {

    List<Rank> ranking;
}
