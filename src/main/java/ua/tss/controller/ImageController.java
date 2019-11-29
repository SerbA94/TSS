package ua.tss.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import ua.tss.model.Image;
import ua.tss.model.Product;
import ua.tss.repository.ImageRepository;
import ua.tss.repository.ProductRepository;
import ua.tss.service.ImageService;

@Controller
@RequestMapping("image")
public class ImageController {
	
	@Autowired
    private ImageService imageService;
	
	@Autowired
	private ProductRepository productRepository;
	
	private final ImageRepository imageRepository;
	
	@Autowired
	public ImageController(ImageRepository imageRepository) {
		this.imageRepository = imageRepository;
	}
	
	@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SUPERVISOR')")
	@GetMapping("/")
    public String image(Model model) {
		model.addAttribute("images", imageRepository.findAll());
		model.addAttribute("image", new Image());
        return "image-upload";        
    }
	
	
	// add null checking for id
	// add product list realization in view for id choosing 
 	@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SUPERVISOR')")
	@GetMapping("/product/{id}")
    public String productImages(@PathVariable("id") Long id,Model model) {
		Product product = productRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + id));
		model.addAttribute("images", product.getImages());
		model.addAttribute("image", new Image());
        return "image-upload";        
    }
	
	@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SUPERVISOR')")
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
	
	// remove upload realization with upProd realization
	@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SUPERVISOR')")
	@PostMapping("/upprod")
    public String upProd(@RequestParam("file") MultipartFile file,@RequestParam("id") Long id) {
		Product product = productRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + id));
        try {
			imageService.storeImage(file,product);
		} catch (IOException e) {
			e.printStackTrace();
	        return "image-upload";
		} 
        return "redirect:/image/";        
    }

	// add id realization from upProd
	@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SUPERVISOR')")
    @PostMapping("/upload-multiple")
    public String multipleUpload(@RequestParam("files") MultipartFile[] files) {
         Arrays.asList(files).stream().forEach(file -> upload(file));
         return "redirect:/image/";        

    }
    
	@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SUPERVISOR')")
    @GetMapping("/{id}")
    public void getImage(@PathVariable("id") Long id, HttpServletResponse response) throws IOException {
 		response.setContentType("image/jpeg");
 		Image image = imageService.getImage(String.valueOf(id));
 		
 		InputStream inputStream = new ByteArrayInputStream(image.getData());
 		IOUtils.copy(inputStream, response.getOutputStream());
     }
    
	@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SUPERVISOR')")
	@GetMapping("/delete/{id}")
	public String delete(@PathVariable("id") Long id, Model model) {
		Image image = imageRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid image Id:" + id));
		imageRepository.delete(image);
        return "redirect:/image/";        
	}
	

}
