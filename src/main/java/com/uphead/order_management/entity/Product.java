package com.uphead.order_management.entity;

import java.math.BigDecimal;
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
@Table(name = "products",
 uniqueConstraints = {@UniqueConstraint(
 name = "uk_product_sku_organization",
 columnNames = {"sku", "organization_id"}
 )}
)
public class Product {
	
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long productId;

	    @ManyToOne(fetch = FetchType.LAZY, optional = false)
	    @JoinColumn(
	        name = "organization_id",
	        nullable = false,
	        foreignKey = @ForeignKey(name = "fk_product_organization")
	    )
	    private Organization organization;

	    @Column(nullable = false, length = 150)
	    private String name;

	    @Column(nullable = false, length = 100)
	    private String sku;

	    @Column(length = 500)
	    private String description;

	    @Column(nullable = false, precision = 15, scale = 2)
	    private BigDecimal price;

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

	   
	    public Long getProductId() {
			return productId;
		}

		public void setProductId(Long productId) {
			this.productId = productId;
		}

		public void setCreatedAt(LocalDateTime createdAt) {
			this.createdAt = createdAt;
		}

		public void setUpdatedAt(LocalDateTime updatedAt) {
			this.updatedAt = updatedAt;
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

	    public String getSku() {
	        return sku;
	    }

	    public void setSku(String sku) {
	        this.sku = sku;
	    }

	    public String getDescription() {
	        return description;
	    }

	    public void setDescription(String description) {
	        this.description = description;
	    }

	    public BigDecimal getPrice() {
	        return price;
	    }

	    public void setPrice(BigDecimal price) {
	        this.price = price;
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
