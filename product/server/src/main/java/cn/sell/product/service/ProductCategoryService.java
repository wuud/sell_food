package cn.sell.product.service;

import cn.sell.product.dao.ProductCategoryDao;
import cn.sell.product.model.ProductCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductCategoryService {

    @Autowired
    ProductCategoryDao productCategoryDao;

    public ProductCategory getById(Integer id) {
        Optional<ProductCategory> op = productCategoryDao.findById(id);
        return op.get();
    }

    public List<ProductCategory> findAll() {
        return productCategoryDao.findAll();
    }

    public void add(ProductCategory pc) {
        productCategoryDao.save(pc);
    }

    public void update(ProductCategory pc) {
        productCategoryDao.save(pc);
    }

    public List<ProductCategory> getByTypeIn(List<Integer> list) {
        return productCategoryDao.findByCategoryTypeIn(list);
    }

}
