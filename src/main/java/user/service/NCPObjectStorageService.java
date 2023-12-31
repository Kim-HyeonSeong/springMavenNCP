package user.service;

import java.io.InputStream;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import spring.conf.NaverConfiguration;

@Service
public class NCPObjectStorageService implements ObjectStorageService{
	final AmazonS3 s3;
	
	public NCPObjectStorageService(NaverConfiguration naverConfiguration) {
		s3 = AmazonS3ClientBuilder
				.standard()
				.withEndpointConfiguration(
						new AwsClientBuilder
							.EndpointConfiguration(naverConfiguration.getEndPoint(), 
									naverConfiguration.getRegionName())
						)
						.withCredentials(new AWSStaticCredentialsProvider(
									new BasicAWSCredentials(naverConfiguration.getAccesskey(),
															naverConfiguration.getSecretkey())
											)
						)
						.build();
	} 
	@Override
	public void deleteFile(String bucketName, String imageFileName) {
		System.out.println("버킷 네임 :");
		System.out.println(bucketName);
		System.out.println("이미지 파일네임 :");
		System.out.println(imageFileName);
		s3.deleteObject(bucketName, imageFileName);
		
		
		System.out.println("삭제완료!");
		
	}
	
	@Override
	public String uploadFile(String bucketName, String directoryPath, MultipartFile img) {
		if(img.isEmpty()) return null;
		
		try(InputStream fileIn = img.getInputStream()) {			
			//String fileName = img.getOriginalFilename();
			String fileName = UUID.randomUUID().toString();
			
			ObjectMetadata objectMetadata = new ObjectMetadata();
			objectMetadata.setContentType(img.getContentType());
			
			PutObjectRequest objectRequest =
					new PutObjectRequest(bucketName,
										 directoryPath + fileName,
										 fileIn,
										 objectMetadata).withCannedAcl(CannedAccessControlList.PublicRead);
			
			s3.putObject(objectRequest);
			
			return fileName;
			
		} catch (Exception e) {
			throw new RuntimeException("파일 업로드 오류", e);
		}
		
	}	
}
