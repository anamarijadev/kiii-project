package mk.ukim.finki.traveltoursbackend.service.domain.impl;

import java.util.List;
import mk.ukim.finki.traveltoursbackend.model.domain.Tour;
import mk.ukim.finki.traveltoursbackend.model.domain.Reservation;
import mk.ukim.finki.traveltoursbackend.model.domain.User;
import mk.ukim.finki.traveltoursbackend.model.exception.TourCapacityIsFullException;
import mk.ukim.finki.traveltoursbackend.model.exception.UserHasAlreadyReservedTourException;
import mk.ukim.finki.traveltoursbackend.model.exception.UserHasNotReservedTourException;
import mk.ukim.finki.traveltoursbackend.repository.ReservationRepository;
import mk.ukim.finki.traveltoursbackend.service.domain.ReservationService;
import org.springframework.stereotype.Service;

@Service
public class ReservationServiceImpl implements ReservationService {
    private final ReservationRepository reservationRepository;

    public ReservationServiceImpl(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Override
    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    @Override
    public List<Reservation> findAllByUser(User user) {
        return reservationRepository.findAllByUser(user);
    }

    @Override
    public Boolean existsByTourAndUser(Tour tour, User user) {
        return reservationRepository.existsByTourAndUser(tour, user);
    }

    @Override
    public Integer getAvailableSpotsByTour(Tour tour) {
        return tour.getCapacity() - reservationRepository.countByTour(tour);
    }

    @Override
    public Reservation reserveByTourAndUser(Tour tour, User user) {
        if (existsByTourAndUser(tour, user)) {
            throw new UserHasAlreadyReservedTourException(tour.getId(), user.getUsername());
        }

        if (getAvailableSpotsByTour(tour) == 0) {
            throw new TourCapacityIsFullException(tour.getId());
        }

        Reservation reservation = new Reservation(tour, user);
        return reservationRepository.save(reservation);
    }

    @Override
    public Reservation cancelByTourAndUser(Tour tour, User user) {
        Reservation reservation = reservationRepository
            .findByTourAndUser(tour, user)
            .orElseThrow(() -> new UserHasNotReservedTourException(tour.getId(), user.getUsername()));
        reservationRepository.delete(reservation);
        return reservation;
    }
}
