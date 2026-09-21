package com.uphead.order_management.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
name = "customers", uniqueConstraints = { @UniqueConstraint(
name = "uk_customer_email_organization",  columnNames = {"email", "organization_id"}
        )
    }
)
public class Customer {
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name = "customer_id")
	    private Long customerId;

	    @ManyToOne(fetch = FetchType.LAZY, optional = false)
	    @JoinColumn(
	        name = "organization_id",
	        nullable = false,
	        foreignKey = @ForeignKey(name = "fk_customer_organization")
	    )
	    private Organization organization;

	    @Column(nullable = false, length = 150)
	    private String name;

	    @Column(nullable = false, length = 255)
	    private String email;

	    @Column(length = 30)
	    private String phone;

	    @Column(length = 500)
	    private String address;

	    @Column(nullable = false)
	    private Boolean active = true;

	    @Column(nullable = false, updatable = false)
	    private LocalDateTime createdAt;

	    @Column(nullable = false)
	    private LocalDateTime updatedAt;

	    @PrePersist
	    protected void onCreate() {
	        LocalDateTime now = LocalDateTime.now();
	        createdAt = now;
	        updatedAt = now;
	    }

	    @PreUpdate
	    protected void onUpdate() {
	        updatedAt = LocalDateTime.now();
	    }

	    public Long getCustomerId() {
	        return customerId;
	    }

	    public void setCustomerId(Long customerId) {
	        this.customerId = customerId;
	    }

	    public Organization getOrganization() {
	        return organization;
	    }

	    public void setOrganization(Organization organization) {
	        this.organization = organization;
	    }

	    public String getName() {
	        return name;
	    }

	    public void setName(String name) {
	        this.name = name;
	    }

	    public String getEmail() {
	        return email;
	    }

	    public void setEmail(String email) {
	        this.email = email;
	    }

	    public String getPhone() {
	        return phone;
	    }

	    public void setPhone(String phone) {
	        this.phone = phone;
	    }

	    public String getAddress() {
	        return address;
	    }

	    public void setAddress(String address) {
	        this.address = address;
	    }

	    public Boolean getActive() {
	        return active;
	    }

	    public void setActive(Boolean active) {
	        this.active = active;
	    }

	    public LocalDateTime getCreatedAt() {
	        return createdAt;
	    }

	    public LocalDateTime getUpdatedAt() {
	        return updatedAt;
	    }

}
