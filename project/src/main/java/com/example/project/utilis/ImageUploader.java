package com.example.project.utilis;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Service
public class ImageUploader {
	@Autowired
    private Cloudinary cloudinary;
	
	
	public String uploadFile(MultipartFile file) throws IOException
	{
		
		if(file.isEmpty())
		{
			System.out.println("file is empty ");
			throw new IllegalArgumentException("File must not be empty");
		}
		
	
		
		Map uploadResult= null;
		
		try {
		
	     uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
	    		"resource_type", "auto",
				"folder","ImageFolder"
				));
		}catch(Exception  e)
		{
			System.out.println("error = " + e);
		}
		
		return uploadResult.get("secure_url").toString();
	}

}
