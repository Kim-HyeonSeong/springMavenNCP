package user.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import user.bean.UserImageDTO;
import user.service.NCPObjectStorageService;
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
	private String imageFileName;
	
   @GetMapping(value="/uploadForm")
   public String uploadForm() {
      return "/user/uploadForm";
   }
   
   //값을 받는거는 Get으로 받아야함!!
   @GetMapping(value="updateForm")
 	public String updateForm(@RequestParam String seq, Model model) {
	   model.getAttribute(seq);
	   return "/user/updateForm";
 	}
   
    @PostMapping(value="getImage")
	@ResponseBody
	public UserImageDTO getImage(@RequestParam String seq) {
		return userService.getImage(seq);
	}
    @PostMapping(value="update")
    @ResponseBody
	public void update(@ModelAttribute UserImageDTO userImageDTO,
						@RequestParam("img[]") List<MultipartFile> list, 
						HttpSession session) {   	
	    System.out.println("수정하려는 숫자 : " + userImageDTO.getSeq());
	    System.out.println("수정하려는 숫자 : " + userImageDTO.getImageName());
	    System.out.println("수정하려는 숫자 : " + userImageDTO.getImageContent());
	    
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
	    	  dto.setSeq(userImageDTO.getSeq());
	    	  dto.setImageName(userImageDTO.getImageName());   //상품명
	    	  dto.setImageContent(userImageDTO.getImageContent()); //상품 내용
	    	  dto.setImageFileName(fileName); //UUID
	    	  dto.setImageOriginalName(originalFileName);
	    	  
	    	  userImageList.add(dto);
	    	  System.out.println(userImageDTO.getImageName());
	      }  
	      String selectUUID = "storage/";
	      selectUUID += userService.update(userImageList);
	      objectStorageService.deleteFile(bucketName, selectUUID);
	      
    }
   
    @PostMapping(value="delete")
    @ResponseBody
	public void delete(@RequestParam String seq) {
	    System.out.println("지우려는 숫자 : " + seq);		
		 String selectUUID = "storage/";
		 selectUUID += userService.delete(seq);
		 System.out.println(bucketName);
		 System.out.println(selectUUID);
		objectStorageService.deleteFile(bucketName, selectUUID);
	}

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