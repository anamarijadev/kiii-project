package mk.ukim.finki.traveltoursbackend.dto.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import mk.ukim.finki.traveltoursbackend.model.domain.Tour;

public record DisplayTourResponseDto(
    Long id,
    String title,
    String description,
    Long destinationId,
    BigDecimal price,
    Integer capacity,
    LocalDate startDate,
    LocalDate endDate
) {
    public static DisplayTourResponseDto from(Tour tour) {
        return new DisplayTourResponseDto(
            tour.getId(),
            tour.getTitle(),
            tour.getDescription(),
            tour.getDestination().getId(),
            tour.getPrice(),
            tour.getCapacity(),
            tour.getSchedule().getStartDate(),
            tour.getSchedule().getEndDate()
        );
    }

    public static List<DisplayTourResponseDto> from(List<Tour> tours) {
        return tours
            .stream()
            .map(DisplayTourResponseDto::from)
            .toList();
    }
}
