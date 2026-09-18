package mk.ukim.finki.traveltoursbackend.service.domain.impl;

import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import mk.ukim.finki.traveltoursbackend.model.domain.Destination;
import mk.ukim.finki.traveltoursbackend.repository.DestinationRepository;
import mk.ukim.finki.traveltoursbackend.service.domain.DestinationService;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DestinationServiceImpl implements DestinationService {

    private final DestinationRepository destinationRepository;

    public DestinationServiceImpl(DestinationRepository destinationRepository) {
        this.destinationRepository = destinationRepository;
    }

    @Override
    public List<Destination> findAll() {
        log.info("Finding all destinations");
        return destinationRepository.findAll();
    }

    @Override
    public Optional<Destination> findById(Long id) {
        log.info("Finding destination by id={}", id);
        return destinationRepository.findById(id);
    }

    @Override
    public Destination create(Destination destination) {
        Destination saved = destinationRepository.save(destination);
        log.info("Created destination id={} name='{}'", saved.getId(), saved.getName());
        return saved;
    }

    @Override
    public Optional<Destination> update(Long id, Destination destination) {
        return findById(id)
            .map((existingDestination) -> {
                existingDestination.setName(destination.getName());
                existingDestination.setDescription(destination.getDescription());
                Destination saved = destinationRepository.save(existingDestination);
                log.info("Updated destination id={} name='{}'", saved.getId(), saved.getName());
                return saved;
            });
    }

    @Override
    public Optional<Destination> deleteById(Long id) {
        Optional<Destination> destination = findById(id);
        destination.ifPresent(t -> {
            destinationRepository.delete(t);
            log.info("Deleted destination id={} name='{}'", t.getId(), t.getName());
        });
        return destination;
    }
}
