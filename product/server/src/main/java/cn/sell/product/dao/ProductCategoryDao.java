package cn.sell.product.dao;

import cn.sell.product.model.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ProductCategoryDao extends JpaRepository<ProductCategory,Integer> {

    public List<ProductCategory> findByCategoryTypeIn(List<Integer> list);
}
