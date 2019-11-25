package ua.tss.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import ua.tss.model.Image;
import ua.tss.repository.ImageRepository;
import ua.tss.service.ImageService;

@Controller
@RequestMapping("image")
public class ImageController {
	
	@Autowired
    private ImageService imageService;
	
	private final ImageRepository imageRepository;
	
	@Autowired
	public ImageController(ImageRepository imageRepository) {
		this.imageRepository = imageRepository;
	}
	
	@GetMapping("/")
    public String image(Model model) {
		model.addAttribute("images", imageRepository.findAll());
		model.addAttribute("image", new Image());
        return "image-upload";        
    }
	

	@PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file) {
        try {
			imageService.storeImage(file);
		} catch (IOException e) {
			e.printStackTrace();
	        return "image-upload";
		} 
        return "redirect:/image/";        
    }

    @PostMapping("/upload-multiple")
    public String multipleUpload(@RequestParam("files") MultipartFile[] files) {
         Arrays.asList(files).stream().forEach(file -> upload(file));
         return "redirect:/image/";        

    }
    
    @GetMapping("/{id}")
    public void getImage(@PathVariable Long id, HttpServletResponse response) throws IOException {
 		response.setContentType("image/jpeg");
 		Image image = imageService.getImage(String.valueOf(id));
 		
 		InputStream inputStream = new ByteArrayInputStream(image.getData());
 		IOUtils.copy(inputStream, response.getOutputStream());
     }

}
