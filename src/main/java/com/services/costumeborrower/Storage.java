package com.services.costumeborrower;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class Storage {
    private @Id @GeneratedValue long id;
    private String name;
    private String size;
    private Integer totalAmount;
    private String location;
    private Integer available;
    private Integer totalBorrowedAmount;
    /**
     * orphanRemoval = true is used to remove the child entity when it is removed from the parent entity 
     * cascade = CascadeType.ALL is used to propagate the changes made to the parent entity to the child entity
    */
    @JsonManagedReference
    @OneToMany(mappedBy = "storage", cascade = CascadeType.ALL, orphanRemoval = true) 
    private List<Borrower> borrower;
    
    Storage() {}

    Storage(String name, String location, String size, Integer totalAmount) {
        this.name = name;
        this.size = size;
        this.totalAmount = totalAmount;
        this.location = location;
        this.available = totalAmount;
        this.totalBorrowedAmount = 0;
        borrower = new ArrayList<>();
    }

    Storage(String name, String location, String size, Integer totalAmount, List<Borrower> borrower) {
        this.name = name;
        this.size = size;
        this.totalAmount = totalAmount;
        this.location = location;
        this.borrower = borrower;
        this.totalBorrowedAmount = 0;
        for(Borrower list : borrower) {
            this.totalBorrowedAmount += list.getBorrowedAmount();
        }
        this.available = this.totalAmount - this.totalBorrowedAmount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Integer getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Integer totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<Borrower> getBorrower() {
        return borrower;
    }

    public void setBorrower(List<Borrower> borrower) {
        if (this.borrower == null) {
            this.borrower = new ArrayList<>();
        } else {
            this.borrower.clear();
        }
for (Borrower b : borrower) {
this.borrower.add(b);
            b.setStorage(this); // Ensure the storage reference is set
        }
        recalculateAmounts();
    }

    public Integer getAvailable() {
        return available;
    }

    public void setAvailable(Integer available) {
        this.available = available;
    }

    public Integer getTotalBorrowedAmount() {
        return totalBorrowedAmount;
    }

    public void setTotalBorrowedAmount(Integer totalBorrowedAmount) {
        this.totalBorrowedAmount = totalBorrowedAmount;
    }

    public Integer calculateTotalBorrowedAmount() {
        return borrower.stream().mapToInt(Borrower::getBorrowedAmount).sum();
    }

    public Integer calculateAvailable() {
        return totalAmount - calculateTotalBorrowedAmount();
    }

    public void addBorrower(List<Borrower> borrower) {
        for (Borrower list : borrower) {
            this.addBorrower(list); // Ensure the storage reference is set
}
    }

    public void removeBorrower(List<Borrower> borrower) {
        for (Borrower list : borrower) {
            this.removeBorrower(list);// Ensure the storage reference is removed
        }
        recalculateAmounts();
    }
    public void addBorrower(Borrower borrower) {
        this.borrower.add(borrower);
        borrower.setStorage(this); // Ensure the storage reference is set
        recalculateAmounts();
    }

    public void removeBorrower(Borrower borrower) {
        this.borrower.remove(borrower);
        borrower.setStorage(null); // Ensure the storage reference is removed
        recalculateAmounts();
    }

    void recalculateAmounts() {
        this.totalBorrowedAmount = calculateTotalBorrowedAmount();
        this.available = calculateAvailable();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (id ^ (id >>> 32));
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((size == null) ? 0 : size.hashCode());
        result = prime * result + ((totalAmount == null) ? 0 : totalAmount.hashCode());
        result = prime * result + ((location == null) ? 0 : location.hashCode());
        result = prime * result + ((borrower == null) ? 0 : borrower.hashCode());
        result = prime * result + ((available == null) ? 0 : available.hashCode());
        result = prime * result + ((totalBorrowedAmount == null) ? 0 : totalBorrowedAmount.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Storage other = (Storage) obj;
        if (id != other.id)
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (size == null) {
            if (other.size != null)
                return false;
        } else if (!size.equals(other.size))
            return false;
        if (totalAmount == null) {
            if (other.totalAmount != null)
                return false;
        } else if (!totalAmount.equals(other.totalAmount))
            return false;
        if (location == null) {
            if (other.location != null)
                return false;
        } else if (!location.equals(other.location))
            return false;
        if (borrower == null) {
            if (other.borrower != null)
                return false;
        } else if (!borrower.equals(other.borrower))
            return false;
        if (available == null) {
            if (other.available != null)
                return false;
        } else if (!available.equals(other.available))
            return false;
        if (totalBorrowedAmount == null) {
            if (other.totalBorrowedAmount != null)
                return false;
        } else if (!totalBorrowedAmount.equals(other.totalBorrowedAmount))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Storage [id=" + id + ", name=" + name + ", size=" + size + ", totalAmount=" + totalAmount + ", location="
                + location + ", borrower=" + borrower + ", available=" + available + ", totalBorrowedAmount="
                + totalBorrowedAmount + "]";
    }
}
