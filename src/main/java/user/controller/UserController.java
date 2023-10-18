package user.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import user.bean.UserImageDTO;
import user.service.ObjectStorageService;
import user.service.UserService;

@Controller
@RequestMapping(value="user")
public class UserController {

	@Autowired
	private UserService userService;
	@Autowired
	private ObjectStorageService objectStorageService;
	
	private String bucketName= "bitcamp-edu-bucket-95";
	
   @GetMapping(value="/uploadForm")
   public String uploadForm() {
      return "/user/uploadForm";
   }
   
   
	/*
	 * // MappingJackson2HttpMessageConverter 가 jackson 라이브러리를 이용해 // 자바 객체를 JSON
	 * 문자열로 변환하여 클라이언트로 보낸다. // 이 컨버터를 사용하면 굳이 UTF-8 변환을 설정할 필요가 없다. // 즉 produces =
	 * "application/json;charset=UTF-8" 를 설정하지 않아도 된다
	 * 
	 * @PostMapping(value="/upload", produces = "application/json;charset=UTF-8")
	 * 
	 * @ResponseBody public String upload(@ModelAttribute UserImageDTO userImageDTO,
	 * 
	 * @RequestParam("img[]") List<MultipartFile> list, HttpSession session) { //
	 * HttpSession을 사용 - 가상 폴더에 안올리고 실제 폴더에 올림 // 실제 폴더 String filePath =
	 * session.getServletContext().getRealPath("/WEB-INF/storage");
	 * System.out.println("실제폴더 = " + filePath);
	 * 
	 * File file; String originalFileName;
	 * 
	 * //파일명만 모아서 DB로 보내기 List<UserImageDTO> userImageList = new
	 * ArrayList<UserImageDTO>();
	 * 
	 * for(MultipartFile img : list) { originalFileName = img.getOriginalFilename();
	 * 
	 * file = new File(filePath, originalFileName);
	 * 
	 * try { img.transferTo(file); } catch(IOException e) { e.printStackTrace(); }
	 * 
	 * UserImageDTO dto = new UserImageDTO();
	 * dto.setImageName(userImageDTO.getImageName()); //상품명
	 * dto.setImageContent(userImageDTO.getImageContent()); //상품 내용
	 * dto.setImageFileName(""); //UUID dto.setImageOriginalName(originalFileName);
	 * 
	 * userImageList.add(dto);
	 * 
	 * }//for
	 * 
	 * //DB userService.upload(userImageList);
	 * 
	 * return "이미지 등록 완료"; }
	 */
// MappingJackson2HttpMessageConverter 가 jackson 라이브러리를 이용해
   // 자바 객체를 JSON 문자열로 변환하여 클라이언트로 보낸다.
   // 이 컨버터를 사용하면 굳이 UTF-8 변환을 설정할 필요가 없다.
   // 즉 produces = "application/json;charset=UTF-8" 를 설정하지 않아도 된다
   @PostMapping(value="/upload", produces = "application/json;charset=UTF-8")
   @ResponseBody
   public String upload(@ModelAttribute UserImageDTO userImageDTO, 
                   @RequestParam("img[]") List<MultipartFile> list, 
                   HttpSession session) {
                  // HttpSession을 사용 - 가상 폴더에 안올리고 실제 폴더에 올림
      // 실제 폴더
      String filePath = session.getServletContext().getRealPath("/WEB-INF/storage");
      System.out.println("실제폴더 = " + filePath);
      
      File file;
      String originalFileName;
      String fileName;
      
      //파일명만 모아서 DB로 보내기
      List<UserImageDTO> userImageList = new ArrayList<UserImageDTO>();
      
      for(MultipartFile img : list) {
    	  originalFileName = img.getOriginalFilename();
    	  System.out.println(originalFileName);
    	  
    	  fileName = objectStorageService.uploadFile(bucketName, "storage/", img);
    	  
    	  file = new File(filePath, originalFileName);
    	  
    	  try {	
    		  img.transferTo(file);
    	  } catch(IOException e) {
    		  e.printStackTrace();
    	  }
    	  
    	  UserImageDTO dto = new UserImageDTO();
    	  dto.setImageName(userImageDTO.getImageName());   //상품명
    	  dto.setImageContent(userImageDTO.getImageContent()); //상품 내용
    	  dto.setImageFileName(fileName); //UUID
    	  dto.setImageOriginalName(originalFileName);
    	  
    	  userImageList.add(dto);
    	  
      }//for
      
      //DB
      userService.upload(userImageList);
      
      return "이미지 등록 완료";
   }
   
   
   @GetMapping(value="uploadList")
   public String uploadList() {
	   return "/user/uploadList";
   }
   
   @PostMapping(value="getUploadList")
   @ResponseBody
   public List<UserImageDTO> getUploadList() {
	   return userService.getUploadList();
   }
   
}