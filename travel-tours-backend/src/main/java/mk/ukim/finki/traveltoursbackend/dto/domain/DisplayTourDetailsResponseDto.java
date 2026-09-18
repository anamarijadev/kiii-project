package mk.ukim.finki.traveltoursbackend.dto.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import mk.ukim.finki.traveltoursbackend.model.domain.Tour;

public record DisplayTourDetailsResponseDto(
    Long id,
    String title,
    String description,
    DisplayDestinationResponseDto destination,
    BigDecimal price,
    Integer capacity,
    LocalDate startDate,
    LocalDate endDate,
    Long durationInDays
) {
    public static DisplayTourDetailsResponseDto from(Tour tour) {
        return new DisplayTourDetailsResponseDto(
            tour.getId(),
            tour.getTitle(),
            tour.getDescription(),
            DisplayDestinationResponseDto.from(tour.getDestination()),
            tour.getPrice(),
            tour.getCapacity(),
            tour.getSchedule().getStartDate(),
            tour.getSchedule().getEndDate(),
            tour.getSchedule().getDurationInDays()
        );
    }
}
