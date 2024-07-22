package proj.concert.service.mapper;



import proj.concert.common.dto.ConcertSummaryDTO;
import proj.concert.service.domain.Concert;
import proj.concert.service.domain.Performer;
import java.util.ArrayList;
import java.util.List;


/**
 * Mapper class to convert from domain-model to DTO objects representing concert.
 */


public class ConcertMapper {
    //method to convert concertdto to concert
    public static Concert toDomainModel(proj.concert.common.dto.ConcertDTO dtoConcert) {
        Concert fullConcert = new Concert(dtoConcert.getId(),
                dtoConcert.getTitle(),
                dtoConcert.getImageName(),
                dtoConcert.getBlurb());
        return fullConcert;
    }
    // convert concert object to concertdto object
    public static proj.concert.common.dto.ConcertDTO toDto(Concert c) {
        proj.concert.common.dto.ConcertDTO dtoConcert = new proj.concert.common.dto.ConcertDTO(
                c.getId(),
                c.getTitle(),
                c.getImageName(),
                c.getBlurb());
        List<proj.concert.common.dto.PerformerDTO> performerDtos = new ArrayList<>();
        for (Performer performer: c.getPerformers()) {
            proj.concert.common.dto.PerformerDTO performerDto = PerformerMapper.toDto(performer);
            performerDtos.add(performerDto);
        }
        dtoConcert.setPerformers(performerDtos);
        dtoConcert.getDates().addAll(c.getDates());
        return dtoConcert;
    }

    public static ConcertSummaryDTO toSummaryDto(Concert concert) {
        return new ConcertSummaryDTO(
                concert.getId(),
                concert.getTitle(),
                concert.getImageName()
        );
    }

}

