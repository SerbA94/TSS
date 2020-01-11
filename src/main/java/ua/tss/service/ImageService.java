package ua.tss.service;

import java.io.IOException;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import ua.tss.model.Image;
import ua.tss.model.Product;
import ua.tss.repository.ImageRepository;

@Service
@Transactional
public class ImageService {

	@Autowired
	private ImageRepository imageRepository;

    public Image storeImage(MultipartFile file) throws IOException {

        String name = StringUtils.cleanPath(file.getOriginalFilename());
        Image image = new Image(name, file.getBytes());
        return imageRepository.save(image);
    }

    public Image storeImage(MultipartFile file,Product product) throws IOException {

        String name = StringUtils.cleanPath(file.getOriginalFilename());
        Image image = new Image(name, file.getBytes(),product);
        return imageRepository.save(image);
    }

    public Image getImage(String id) {
        return imageRepository.findById(Long.valueOf(id))
        		.orElseThrow(() -> new IllegalArgumentException("Invalid image Id:" + id));
    }

    public void delete(Image image) {
        this.imageRepository.delete(image);
    }

    public Iterable<Image> findAll() {
        return this.imageRepository.findAll();
    }

    public Optional<Image> findById(Long id) {
        return this.imageRepository.findById(id);
    }

    public Image save(Image image) {
        return this.imageRepository.save(image);
    }



}
