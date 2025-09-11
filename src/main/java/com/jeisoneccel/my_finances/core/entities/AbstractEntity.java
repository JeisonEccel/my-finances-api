package com.jeisoneccel.my_finances.core.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serial;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

@Getter
@Setter
@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
public abstract class AbstractEntity implements Serializable, BasicEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @CreationTimestamp
    @Column(
            name = "created_date",
            columnDefinition = "timestamp WITH TIME ZONE default CURRENT_TIMESTAMP",
            nullable = false,
            updatable = false
    )
    private ZonedDateTime createdDate;

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

}
