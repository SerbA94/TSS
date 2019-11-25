package ua.tss.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import ua.tss.model.Image;
import ua.tss.repository.ImageRepository;

@Service
public class ImageService {

	@Autowired
	private ImageRepository imageRepository;
	
    public Image storeImage(MultipartFile file) throws IOException {
    	
        String name = StringUtils.cleanPath(file.getOriginalFilename());	
        Image image = new Image(name, file.getBytes());	
        return imageRepository.save(image);
    }
    
    public Image getImage(String id) {    	
        return imageRepository.findById(Long.valueOf(id))
        		.orElseThrow(() -> new IllegalArgumentException("Invalid image Id:" + id));
    }

}
