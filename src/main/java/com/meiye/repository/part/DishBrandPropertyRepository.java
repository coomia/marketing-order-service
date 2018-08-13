package com.meiye.repository.part;

import com.meiye.model.part.DishBrandProperty;
import com.meiye.model.part.DishSetmealGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Shawns on 08/13/18.
 */
@Repository
public interface DishBrandPropertyRepository extends JpaRepository<DishBrandProperty,Long> {

}
