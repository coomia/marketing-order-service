package com.meiye.repository.store;

import com.meiye.model.role.AuthRole;
import com.meiye.model.store.Commercial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Administrator on 2018/8/23 0023.
 */
@Repository
public interface StoreRepository extends JpaRepository<Commercial,Long> {
}
