package com.klolarion.funding_project.domain.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CodeMaster {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "code_id")
    private Long codeId;
    @Column(unique = true, nullable = false, updatable = false)
    private int code;
    @Column(nullable = false)
    private String description;
    private String reference;

    public CodeMaster(int code, String description, String reference) {
        this.code = code;
        this.description = description;
        this.reference = reference;
    }
}
