package com.meiye.repository.store;

import com.meiye.model.store.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Administrator on 2018/8/23 0023.
 */
@Repository
public interface BrandRepository extends JpaRepository<Brand,Long>{
}
