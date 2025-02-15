package com.services.costumeborrower;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadDB { 

    public static final Logger log = LoggerFactory.getLogger(LoadDB.class);

    @Bean
    CommandLineRunner initDatabase(StorageRepository repository) {
        return args -> {
            Storage storage = new Storage("Tuxedo", "B1", "M", 5);
            storage.addBorrower(new Borrower("John Doe", "asdf", 2, storage));
            storage.addBorrower(new Borrower("Jane Doe", "asdf", 1, storage));
            Storage storage2 = new Storage("Dress", "B2", "S", 3);
            List<Borrower> borrowers = new ArrayList<>();
            borrowers.add(new Borrower("John Doe", "asdf", 2, storage2));
            borrowers.add(new Borrower("Jane Doe", "asdf", 1, storage2));
            storage2.addBorrower(borrowers);
            log.info("Loading " + repository.save(new Storage("Tuxedo", "C2", "XL", 3)));
            log.info("Loading " + repository.save(new Storage("Dress", "C1", "M", 3)));
            log.info("Loading " + repository.save(new Storage("Vongola Costume", "B1", "L", 5)));
            log.info("Loading " + repository.save(new Storage("Vongola Costume", "B1", "S", 4)));
            log.info("Loading " + repository.save(storage));
            log.info("Loading " + repository.save(storage2));
        };
    }
}