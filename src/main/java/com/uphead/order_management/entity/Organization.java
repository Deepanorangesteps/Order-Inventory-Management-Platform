package com.uphead.order_management.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "organizations")
public class Organization {
	
	  @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long organizationId;
	  
	  @Column(nullable = false, unique = true, length = 50)
	    private String code;

	    @Column(nullable = false, unique = true, length = 100)
	    private String name;


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

		
		

	
		public Long getOrganizationId() {
			return organizationId;
		}

		public void setOrganizationId(Long organizationId) {
			this.organizationId = organizationId;
		}

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
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

		public void setCreatedAt(LocalDateTime createdAt) {
			this.createdAt = createdAt;
		}

		public LocalDateTime getUpdatedAt() {
			return updatedAt;
		}

		public void setUpdatedAt(LocalDateTime updatedAt) {
			this.updatedAt = updatedAt;
		}

		@Override
		public String toString() {
			return "Organization [organizationId=" + organizationId + ", code=" + code + ", name=" + name + ", active="
					+ active + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + "]";
		}

		
	

	


}
