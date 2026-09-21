package com.uphead.order_management.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(   name = "orders",uniqueConstraints = {@UniqueConstraint(
 name = "uk_order_number_organization",columnNames = {"order_number", "organization_id"}
  )}
)
public class Order {
	
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name = "order_id")
	    private Long orderId;

	    @ManyToOne(fetch = FetchType.LAZY, optional = false)
	    @JoinColumn(
	        name = "organization_id",
	        nullable = false,
	        foreignKey = @ForeignKey(name = "fk_order_organization")
	    )
	    private Organization organization;

	    @ManyToOne(fetch = FetchType.LAZY, optional = false)
	    @JoinColumn(
	        name = "customer_id",
	        nullable = false,
	        foreignKey = @ForeignKey(name = "fk_order_customer")
	    )
	    private Customer customer;

	    @Column(name = "order_number", nullable = false, length = 50)
	    private String orderNumber;

	    @Enumerated(EnumType.STRING)
	    @Column(nullable = false, length = 30)
	    private OrderStatus status = OrderStatus.PENDING;

	    @Column(
	        name = "total_amount",
	        nullable = false,
	        precision = 15,
	        scale = 2
	    )
	    private BigDecimal totalAmount = BigDecimal.ZERO;

	    @Column(nullable = false, updatable = false)
	    private LocalDateTime createdAt;

	    @Column(nullable = false)
	    private LocalDateTime updatedAt;

	    @OneToMany(
	        mappedBy = "order",
	        cascade = CascadeType.ALL,
	        orphanRemoval = true
	    )
	    private List<OrderItem> orderItems = new ArrayList<>();

	    @PrePersist
	    protected void onCreate() {

	        LocalDateTime now = LocalDateTime.now();

	        createdAt = now;
	        updatedAt = now;

	        if (status == null) {
	            status = OrderStatus.PENDING;
	        }

	        if (totalAmount == null) {
	            totalAmount = BigDecimal.ZERO;
	        }
	    }

	    @PreUpdate
	    protected void onUpdate() {
	        updatedAt = LocalDateTime.now();
	    }

	    public Long getOrderId() {
	        return orderId;
	    }

	    public void setOrderId(Long orderId) {
	        this.orderId = orderId;
	    }

	    public Organization getOrganization() {
	        return organization;
	    }

	    public void setOrganization(Organization organization) {
	        this.organization = organization;
	    }

	    public Customer getCustomer() {
	        return customer;
	    }

	    public void setCustomer(Customer customer) {
	        this.customer = customer;
	    }

	    public String getOrderNumber() {
	        return orderNumber;
	    }

	    public void setOrderNumber(String orderNumber) {
	        this.orderNumber = orderNumber;
	    }

	    public OrderStatus getStatus() {
	        return status;
	    }

	    public void setStatus(OrderStatus status) {
	        this.status = status;
	    }

	    public BigDecimal getTotalAmount() {
	        return totalAmount;
	    }

	    public void setTotalAmount(BigDecimal totalAmount) {
	        this.totalAmount = totalAmount;
	    }

	    public LocalDateTime getCreatedAt() {
	        return createdAt;
	    }

	    public LocalDateTime getUpdatedAt() {
	        return updatedAt;
	    }

	    public List<OrderItem> getOrderItems() {
	        return orderItems;
	    }

	    public void setOrderItems(List<OrderItem> orderItems) {
	        this.orderItems = orderItems;
	    }

	    public void addOrderItem(OrderItem orderItem) {

	        orderItems.add(orderItem);

	        orderItem.setOrder(this);
	    }

	    public void removeOrderItem(OrderItem orderItem) {

	        orderItems.remove(orderItem);

	        orderItem.setOrder(null);
	    }

}
