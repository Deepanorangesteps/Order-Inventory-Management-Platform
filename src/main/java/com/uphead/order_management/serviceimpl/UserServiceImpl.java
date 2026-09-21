package com.uphead.order_management.serviceimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.uphead.order_management.entity.Organization;
import com.uphead.order_management.entity.User;
import com.uphead.order_management.repository.OrganizationRepository;
import com.uphead.order_management.repository.UserRepository;
import com.uphead.order_management.request.UserRequest;
import com.uphead.order_management.response.UserResponse;
import com.uphead.order_management.security.CurrentUserService;
import com.uphead.order_management.service.UserService;

@Service
public class UserServiceImpl implements UserService{

	private static final Logger log =
            LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final OrganizationRepository organizationRepository;
    private final PasswordEncoder passwordEncoder;
    private final CurrentUserService currentUserService;

    public UserServiceImpl(
            UserRepository userRepository,
            OrganizationRepository organizationRepository,
            PasswordEncoder passwordEncoder,
            CurrentUserService currentUserService) {

        this.userRepository = userRepository;
        this.organizationRepository = organizationRepository;
        this.passwordEncoder = passwordEncoder;
        this.currentUserService = currentUserService;
    }
	@Override
	public User createUser(UserRequest request) {
		log.info(
                "Creating user. email={},  role={}",
                request.getEmail(),
                request.getRole()
        );
		Long organizationId =
		        currentUserService.getCurrentOrganizationId();
		Optional<Organization> organization =
                organizationRepository.findByOrganizationId(organizationId);
		
		if(userRepository.existsByEmailAndOrganization_Code(request.getEmail(), organization.get().getCode())) {
			throw new RuntimeException(
                    "User with this email already exists in the organization"
            );
		}
		   User user = new User();

	        user.setName(request.getName());
	        user.setEmail(request.getEmail());
	        user.setPassword(
	                passwordEncoder.encode(request.getPassword())
	        );
	        user.setRole(request.getRole());
	        user.setOrganization(organization.get());
	        user.setActive(true);

	        User savedUser = userRepository.save(user);
       
		return savedUser;
	}

	@Override
	public UserResponse getUserById(Long id) {
		 Long organizationId =
		            currentUserService.getCurrentOrganizationId();
	    Optional<User> userOpt  = userRepository
                .findByUserId(id, organizationId);
	    if(userOpt.isEmpty()) {
	    	throw new RuntimeException("User not found");
	    }
	    
	    User user = userOpt.get();
	    UserResponse response = new UserResponse();

        response.setId(user.getUserId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());
        response.setOrganizationId(
                user.getOrganization().getOrganizationId()
        );
        response.setActive(user.getActive());
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());
                
		return response;
	}
	@Override
	public List<UserResponse> getAllUsers() {
		Long organizationId =
	            currentUserService.getCurrentOrganizationId();
		List<User> userResList = userRepository.findAllByOrganization_OrganizationId(organizationId);
		List<UserResponse> userResponseList = new ArrayList<>();
        if(!userResList.isEmpty() && userResList.size()>0) {
        	for(User user :userResList) {
        		UserResponse response = new UserResponse();
        		response.setId(user.getUserId());
                response.setName(user.getName());
                response.setEmail(user.getEmail());
                response.setRole(user.getRole());
                response.setOrganizationId(
                        user.getOrganization().getOrganizationId()
                );
                response.setActive(user.getActive());
                response.setCreatedAt(user.getCreatedAt());
                response.setUpdatedAt(user.getUpdatedAt());
                userResponseList.add(response);
    		}
        }
		return userResponseList;
		
	}

}
