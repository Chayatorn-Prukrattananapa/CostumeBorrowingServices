package com.services.costumeborrower;

import org.springframework.web.server.ResponseStatusException;

import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class CostumeBorrowerController {
    private final StorageRepository costumeBorrowerRepository;
    private final BorrowerRepository borrowerRepository;
    private static final Logger log = LoggerFactory.getLogger(CostumeBorrowerController.class);

    CostumeBorrowerController(StorageRepository costumeBorrowerRepository, BorrowerRepository borrowerRepository) {
        this.costumeBorrowerRepository = costumeBorrowerRepository;
        this.borrowerRepository = borrowerRepository;
    }

    

    @GetMapping("/costume")
    public List<Storage> findAll() {
        return costumeBorrowerRepository.findAll();
    }
    
    @GetMapping("/costume/{id}")
    public Storage findCostume(@PathVariable Long id) {
        Optional<Storage> costume = costumeBorrowerRepository.findById(id);
        if (costume.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Costume not found");
        }
        return costume.get();
    }

    @PostMapping("/costume")
    public Storage newCostume (@RequestBody Storage costume){
        return costumeBorrowerRepository.save(costume);
    }

    @PutMapping("/costume/{id}")
    public Storage borrowCostume(@RequestBody Storage newCostume, @PathVariable Long id) {
        log.info("Received PUT request for costume ID: " + id);
        log.info("New costume details: " + newCostume.toString());
        return costumeBorrowerRepository.findById(id)
                .map(costume -> {
                    log.info("Updating existing costume with ID: " + id);
                    costume.setName(newCostume.getName());
                    costume.setSize(newCostume.getSize());
                    costume.setTotalAmount(newCostume.getTotalAmount());
                    costume.setLocation(newCostume.getLocation());
                    costume.setBorrower(newCostume.getBorrower());
                    costume.recalculateAmounts(); // Ensure amounts are recalculated
log.info("Updated costume details: " + costume.toString());
                    return costumeBorrowerRepository.save(costume);
                })
                .orElseGet(() -> {
                    log.info("Creating new costume as no existing costume found with ID: " + id);
                    newCostume.recalculateAmounts(); // Ensure amounts are recalculated
log.info("New costume details after recalculation: " + newCostume.toString());
                    return costumeBorrowerRepository.save(newCostume);
                });
    }
    
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/costume/{id}")
    @Transactional // Ensure that both repositories succeed or fail together.
    public void deleteCostume(@PathVariable Long id) {
        costumeBorrowerRepository.deleteById(id);
        borrowerRepository.deleteById(id);
    }

}
    
    