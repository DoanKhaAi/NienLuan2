package com.khaai.NuoiEm.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.khaai.NuoiEm.Entities.MyUser;
import com.khaai.NuoiEm.Repository.MyUserRepository;
import jakarta.transaction.Transactional;


@Service
public class MyUserDetailService implements UserDetailsService {

	@Autowired
	private MyUserRepository repo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	private static final String PASSWORD_PATTERN = 
	        "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[!@#$%^&*()-+=]).{6,10}$";
	
	private static final String EMAIL_PATTERN = 
	        "^[a-zA-Z0-9._%+-]+@(gmail|yahoo|outlook)\\.com$";
	    
	private static final Pattern pattern_password = Pattern.compile(PASSWORD_PATTERN);
	private static final Pattern pattern_email = Pattern.compile(EMAIL_PATTERN);
	
//NÀY DÀNH CHO PHÂN QUYỀN
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<MyUser> user = repo.findByUsername(username);
		if (user.isPresent()) {
			var userObj = user.get();
			return User.builder()
					.username(userObj.getUsername())
					.password(userObj.getPassword())
					.roles(getRoles(userObj))
					.build();
						
			
		}else {
			throw new UsernameNotFoundException(username);
		}
	}
	
	private String[] getRoles(MyUser user) {
		if (user.getRole() == null) {
			return new String[]{"USER"};
		}
		return user.getRole().split(",");
	}
	
//NÀY DÀNH CHO ĐĂNG KÝ
	@Transactional
	public String check(MyUser user, MultipartFile imageFile) throws IOException{
		if (repo.existsByUsername(user.getUsername())) {
            return "Username already exists";
        }
		
		if (!isPasswordValid(user.getPassword())) {
			 return "Password is not valid";
	    }
		
		if (repo.existsByEmail(user.getEmail()) || !isEmailValid(user.getEmail())) {
            return "Email is used or invalid";
        }		
	
		String file= saveUserImage(user.getUsername(), imageFile);
		return file;
	}
	
	@Transactional
	public String save(MyUser user) throws IOException{	
		user.setRole("USER");
		user.setPassword(passwordEncoder.encode(user.getPassword()));
        repo.save(user);
        return "User saved successfully";
	}

//SERVICE ĐỐI VỚI ADMIN
	
	public List<MyUser> listAll(){
		return (List<MyUser>) repo.findAll();
	}
	
	public Page<MyUser> listAll(int page, int size){
		PageRequest pageable = PageRequest.of(page, size);
		return  (Page<MyUser>) repo.findAll(pageable);
	}
	
	
	public void saveEdit(MyUser user) {
		MyUser user_edit= repo.findByUsername(user.getUsername())
				.orElseThrow(() -> new RuntimeException("Không tìm thấy Người dùng"));
		user_edit.setRole(user.getRole());
		repo.save(user_edit);
	}
	
//SERVICE ĐỐI VỚI USER
	
	@Transactional
	public String saveUserEdit(MyUser user) throws IOException{
		
		MyUser user_update = repo.findById(user.getUserID())
				.orElseThrow(() -> new RuntimeException("Không tìm thấy Người dùng"));
		
		
		String emailUpdate = user_update.getEmail();
		String emailOriginal = user.getEmail();
	
		if (emailUpdate != null && !emailUpdate.equals(emailOriginal)) {
			if (repo.existsByEmail(user.getEmail()) ||  !isEmailValid(user.getEmail())) {
	            return "Email is used or invalid";
	        }
			user_update.setEmail(user.getEmail());		
		}
		user_update.setFullname(user.getFullname());	
		if (user.getBirthday() !=null){
			user_update.setBirthday(user.getBirthday());}
		user_update.setGender(user.getGender());
        repo.save(user_update);
        return "User saved successfully";
	}
	
	public void saveUserEditImage(MyUser user, MultipartFile imageFile) throws IOException {
		MyUser user_edit=repo.findById(user.getUserID())
					.orElseThrow(() -> new RuntimeException("Không tìm thấy Người dùng"));
		String image=user_edit.getImage();
		if (!image.equals("default_user.jpg")) {
			Path filePath = Paths.get("src/main/resources/static/img/user/", image);
			Files.delete(filePath);
		}
		String file= saveUserImage(user.getUsername(), imageFile);
		user_edit.setImage(file);	
		repo.save(user_edit);	
	}
	
	public String saveUserEditPassword(MyUser user, String newpassword, String renewpassword) throws IOException {
		MyUser user_edit=repo.findById(user.getUserID())
					.orElseThrow(() -> new RuntimeException("Không tìm thấy Người dùng"));
		if (!passwordEncoder.matches(user.getPassword(), user_edit.getPassword())) 
			return "Password is incorrect";
		else if (!newpassword.equals(renewpassword))
			return "Password is not match";
		else if (!isPasswordValid(newpassword))
			return "Password is not valid";
		else {
			user_edit.setPassword(passwordEncoder.encode(newpassword));
			repo.save(user_edit);
			return "OK";
		}
	}
	
//PHƯƠNG THỨC DÙNG CHUNG
	
	public MyUser get(String username){
		Optional<MyUser> result =repo.findByUsername(username);
		if (result.isPresent()) {
			return result.get();
		}
		else return result.get();
	}
	
	public MyUser get(Integer id){
		Optional<MyUser> result =repo.findById(id);
		if (result.isPresent()) {
			return result.get();
		}
		else return result.get();
	}
	

	
	public boolean isPasswordValid(String password) {
	    if (password == null) {
	            return false;
	     }
	    return pattern_password.matcher(password).matches();
	 }
	
	public boolean isEmailValid(String email) {
        if (email == null) {
            return false;
        }
        return  pattern_email.matcher(email).matches();
    }
	
	


    public String saveUserImage(String username, MultipartFile imageFile) throws IOException {
    	
    	if (!imageFile.isEmpty()) {
	    	Path uploadPath = Paths.get("src/main/resources/static/img/user/");
	        String originalFileName = imageFile.getOriginalFilename();
	        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
	        String newFileName = username + fileExtension;
	
	        Path filePath = uploadPath.resolve(newFileName);
	        Files.copy(imageFile.getInputStream(), filePath);
	        return newFileName; 
        }
    	else return "default_user.jpg";
    }
	

}
