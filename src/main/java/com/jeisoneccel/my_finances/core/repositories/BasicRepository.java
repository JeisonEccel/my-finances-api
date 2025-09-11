package com.jeisoneccel.my_finances.core.repositories;

import com.jeisoneccel.my_finances.core.entities.BasicEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BasicRepository<E extends BasicEntity>
        extends JpaRepository<E, String>, JpaSpecificationExecutor<E> {

}
