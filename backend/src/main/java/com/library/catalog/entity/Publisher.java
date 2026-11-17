package com.library.catalog.entity;

import com.library.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Table {@code publishers} (F03). Name is unique, case-insensitive. */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "publishers")
public class Publisher extends BaseEntity {

    @Column(nullable = false, length = 150)
    private String name;
}
