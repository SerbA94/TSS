package ua.tss.service;

import java.io.IOException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import ua.tss.model.Product;
import ua.tss.repository.ProductRepository;



@Service
public class ProductService  {
    @Autowired
    private ProductRepository productRepository;
    

    @Transactional
    public void saveImageFile(Long id, MultipartFile file) {

    try {
        Product product = productRepository.findById(id).get();
        Byte[] byteObjects = new Byte[file.getBytes().length];
        int i = 0;
        for (byte b : file.getBytes()){
            byteObjects[i++] = b;
        }
        product.setImage(byteObjects);
        productRepository.save(product);
    } catch (IOException e) {
        e.printStackTrace();
    }
    }

   
}
