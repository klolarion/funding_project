package com.klolarion.funding_project.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;



@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseTime{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productId;

    @Column(nullable = false, unique = true)
    @Size(max = 50)
    private String productName;
    private Long price;
    private int stock;

    private boolean restock;
    private boolean saleFinished;

    public Product(String productName, Long price, int stock) {
        this.productName = productName;
        this.price = price;
        this.stock = stock;
        this.restock = false;
        this.saleFinished = false;
    }
}
