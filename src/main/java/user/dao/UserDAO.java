package user.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import user.bean.UserImageDTO;

@Mapper
public interface UserDAO {

	public void upload(List<UserImageDTO> userImageList);

	public List<UserImageDTO> getUploadList();

	public void delete(int imageSeq);

	public UserImageDTO getImage(int imageSeq);

	public void update(UserImageDTO userImageDTO);

	public String searchUUID(int seq);
}
