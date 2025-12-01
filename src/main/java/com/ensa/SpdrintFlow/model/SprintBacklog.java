package com.ensa.SpdrintFlow.model;

import jakarta.persistence.*;

@Entity
@Table(name = "sprint_backlogs")
public class SprintBacklog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long backlogId;
    
    private String name;
    
    @ManyToOne
    @JoinColumn(name = "product_backlog_id")
    private ProductBacklog productBacklog;
    
    // Getters and Setters
    public Long getBacklogId() { return backlogId; }
    public void setBacklogId(Long backlogId) { this.backlogId = backlogId; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public ProductBacklog getProductBacklog() { return productBacklog; }
    public void setProductBacklog(ProductBacklog productBacklog) { this.productBacklog = productBacklog; }
}
