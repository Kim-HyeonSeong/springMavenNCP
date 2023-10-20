package user.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import user.bean.UserImageDTO;
import user.dao.UserDAO;

@Service
@Transactional
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserDAO userDAO;
	
	@Override
	public void upload(List<UserImageDTO> userImageList) {
			userDAO.upload(userImageList);	
	}

	@Override
	public List<UserImageDTO> getUploadList() {
		return userDAO.getUploadList();
	}

	@Override
	public String delete(String seq) {
		int imageSeq = Integer.parseInt(seq);
		String selectUUID =userDAO.searchUUID(imageSeq);
		userDAO.delete(imageSeq);
		
		return selectUUID;
	}

	@Override
	public UserImageDTO getImage(String seq) {
		int imageSeq = Integer.parseInt(seq);
		return userDAO.getImage(imageSeq);
	}

	@Override
	public String update(List<UserImageDTO> userImageList) {
		System.out.println("--------");
		System.out.println(userImageList.get(0).getImageName());
		System.out.println(userImageList.get(0).getImageContent());
		System.out.println(userImageList.get(0).getImageFileName());
		System.out.println(userImageList.get(0).getImageOriginalName());
		System.out.println(userImageList.get(0).getSeq());
		System.out.println(userImageList.get(0).getClass());
		
		String selectUUID =userDAO.searchUUID(userImageList.get(0).getSeq());
		
		userDAO.update(userImageList.get(0));
		
		return selectUUID;
	}
}
