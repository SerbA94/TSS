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
import ua.tss.service.ImageService;
import ua.tss.service.ProductService;

@Controller
@RequestMapping("image")
public class ImageController {

	@Autowired
    private ImageService imageService;

	@Autowired
	private ProductService productService;

	@GetMapping("/list")
	@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SUPERVISOR')")
    public String image(Model model) {
		model.addAttribute("products", productService.findAll());
		model.addAttribute("images", imageService.findAll());
		model.addAttribute("image", new Image());
        return "image-list";
    }

	@GetMapping("/product/{id}")
    public String productImages(@PathVariable("id") Long id,Model model) {
		Product product = productService.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + id));
		model.addAttribute("images", product.getImages());
		model.addAttribute("image", new Image());
        return "image-list";
    }

	@GetMapping("/update/{id}")
	@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SUPERVISOR')")
    public String updateForm(@PathVariable("id") Long id,Model model) {
		Image image = imageService.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid image Id:" + id));
		model.addAttribute("products", productService.findAll());
		model.addAttribute("image", image);
        return "image-update";
    }

	@PostMapping("/update")
	@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SUPERVISOR')")
    public String update(Image image,@RequestParam("productId") Long id) {
		Image imageFromDB = imageService.findById(image.getId())
				.orElseThrow(() -> new IllegalArgumentException("Invalid image Id:" + image.getId()));

		if(image.getName()!=null) {
			imageFromDB.setName(image.getName());
		}
		if(id!=null){
			Product product = productService.findById(id)
					.orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + id));
			imageFromDB.setProduct(product);
		}
		imageService.save(imageFromDB);
        return "redirect:/image/list";
    }



	@PostMapping("/upload")
	@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SUPERVISOR')")
    public String upload(@RequestParam("file") MultipartFile file,@RequestParam("id") Long id) {
        try {
        	if(id!=null) {
        		Product product = productService.findById(id)
        				.orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + id));
    			imageService.storeImage(file,product);
        	}else {
    			imageService.storeImage(file);
        	}
		} catch (IOException e) {
			e.printStackTrace();
	        return "image-list";
		}
        return "redirect:/image/list";
    }

    @PostMapping("/upload-multiple")
	@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SUPERVISOR')")
    public String multipleUpload(@RequestParam("files") MultipartFile[] files,@RequestParam("id") Long id) {
         Arrays.asList(files).stream().forEach(file -> upload(file,id));
         return "redirect:/image/list";

    }

    @GetMapping("/{id}")
    public void getImage(@PathVariable("id") Long id, HttpServletResponse response) throws IOException {
 		response.setContentType("image/jpeg");
 		Image image = imageService.getImage(String.valueOf(id));

 		InputStream inputStream = new ByteArrayInputStream(image.getData());
 		IOUtils.copy(inputStream, response.getOutputStream());
     }

	@GetMapping("/delete/{id}")
	@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SUPERVISOR')")
	public String delete(@PathVariable("id") Long id, Model model) {
		Image image = imageService.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid image Id:" + id));
		imageService.delete(image);
        return "redirect:/image/list";
	}



}
