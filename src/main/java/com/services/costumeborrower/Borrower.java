package com.services.costumeborrower;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class Borrower {
    private @Id @GeneratedValue Long id;
    private String name;
    private String contact;
    private Integer borrowedAmount;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "storage_id")
    private Storage storage;

    Borrower() {}

    Borrower(String name, String contact, Integer borrowedAmount, Storage storage) {
        this.name = name;
        this.contact = contact;
        this.borrowedAmount = borrowedAmount;
        this.storage = storage;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public Integer getBorrowedAmount() {
        return borrowedAmount;
    }

    public void setBorrowedAmount(Integer borrowedAmount) {
        this.borrowedAmount = borrowedAmount;
    }

    public Storage getStorage() {
        return storage;
    }

    public void setStorage(Storage storage) {
        this.storage = storage;
    }

}

