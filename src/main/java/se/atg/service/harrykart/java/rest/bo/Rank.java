package se.atg.service.harrykart.java.rest.bo;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Rank {

    Integer position;

    String horse;
}
