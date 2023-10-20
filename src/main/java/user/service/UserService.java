package user.service;

import java.util.List;

import user.bean.UserImageDTO;

public interface UserService {

	public void upload(List<UserImageDTO> userImageList);

	public List<UserImageDTO> getUploadList();

	public String delete(String seq);

	public UserImageDTO getImage(String seq);

	public String update(List<UserImageDTO> userImageList);
	
}
