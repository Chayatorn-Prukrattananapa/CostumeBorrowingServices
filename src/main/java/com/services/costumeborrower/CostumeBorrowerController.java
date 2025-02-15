package com.services.costumeborrower;

import org.springframework.web.server.ResponseStatusException;

import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class CostumeBorrowerController {
    private final StorageRepository costumeBorrowerRepository;
    private final BorrowerRepository borrowerRepository;

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
        return costumeBorrowerRepository.findById(id)
                .map(costume -> {
                    costume.setName(newCostume.getName());
                    costume.setSize(newCostume.getSize());
                    costume.setTotalAmount(newCostume.getTotalAmount());
                    costume.setLocation(newCostume.getLocation());
                    costume.setBorrower(newCostume.getBorrower());
                    costume.recalculateAmounts(); // Ensure amounts are recalculated
                    return costumeBorrowerRepository.save(costume);
                })
                .orElseGet(() -> {
                    newCostume.recalculateAmounts(); // Ensure amounts are recalculated
                    return costumeBorrowerRepository.save(newCostume);
                });
    }
    
    @PutMapping("/costume/{id}/addBorrower")
    public Storage addBorrowerToCostume(@RequestBody Borrower newBorrower, @PathVariable Long id) {
        return costumeBorrowerRepository.findById(id)
                .map(costume -> {
                    newBorrower.setStorage(costume);
                    borrowerRepository.save(newBorrower);
                    costume.addBorrower(newBorrower);
                    costume.recalculateAmounts(); // Ensure amounts are recalculated
                    return costumeBorrowerRepository.save(costume);
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Costume not found"));
    }

    @PutMapping("/costume/{id}/removeBorrower")
    public Storage removeBorrowerFromCostume(@RequestBody Borrower borrowerToRemove, @PathVariable Long id) {
        return costumeBorrowerRepository.findById(id)
                .map(costume -> {
                    Optional<Borrower> borrower = borrowerRepository.findById(borrowerToRemove.getId());
                    if (borrower.isPresent()) {
                        costume.removeBorrower(borrower.get());
                        borrowerRepository.delete(borrower.get());
                        costume.recalculateAmounts(); // Ensure amounts are recalculated
                        return costumeBorrowerRepository.save(costume);
                    } else {
                        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Borrower not found");
                    }
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Costume not found"));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/costume/{id}")
    @Transactional // Ensure that both repositories succeed or fail together.
    public void deleteCostume(@PathVariable Long id) {
        costumeBorrowerRepository.deleteById(id);
        borrowerRepository.deleteById(id);
    }

}

