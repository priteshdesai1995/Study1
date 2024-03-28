/**
 * Copyright 2022 Brainvire - All rights reserved.
 */
package com.base.api.service.impl;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.exception.GenericJDBCException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.base.api.constants.Constants;
import com.base.api.entities.OfferUser;
import com.base.api.entities.PasswordResetToken;
import com.base.api.entities.PersonAddress;
import com.base.api.entities.Privilege;
import com.base.api.entities.User;
import com.base.api.entities.UserProfile;
import com.base.api.entities.UserRole;
import com.base.api.enums.UserStatus;
import com.base.api.exception.APIException;
import com.base.api.exception.PasswordException;
import com.base.api.exception.SignupException;
import com.base.api.exception.TokenNotFoundException;
import com.base.api.exception.UserNotFoundException;
import com.base.api.filter.UserFilter;
import com.base.api.gateway.util.Util;
import com.base.api.repository.OfferUserRepository;
import com.base.api.repository.PasswordResetTokenRepository;
import com.base.api.repository.UserRepository;
import com.base.api.request.dto.AddressDTO;
import com.base.api.request.dto.AdminAddUserDTO;
import com.base.api.request.dto.Mail;
import com.base.api.request.dto.ResetPassword;
import com.base.api.request.dto.UserSignupDTO;
import com.base.api.response.dto.SearchInfo;
import com.base.api.security.UserPrincipal;
import com.base.api.service.RoleService;
import com.base.api.service.UserService;

import io.jsonwebtoken.io.IOException;
import lombok.extern.slf4j.Slf4j;

/**
 * This class implements services for Users.
 * 
 * @author preyansh_prajapati
 * @author minesh_prajapati
 *
 */
@Service(value = "userService")
@Slf4j
public class UserServiceImpl implements UserDetailsService, UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleService roleService;

    @Autowired
    EntityManager entityManager;

    @Autowired
    private MapperUserService mapperUserService;

    @Autowired
    EmailSenderService senderService;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    private OfferUserRepository offerUserRepository;

    /**
     * Load user by username.
     *
     * @param username the username
     * @return the user details
     * @throws UsernameNotFoundException the username not found exception
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        log.info("UserServiceImpl : loadUserByUsername");
        User user = userRepository.findByUserNameAndStatus(username, UserStatus.ACTIVE);

        if (user == null) {
            log.error("User not found or user deactivated.");
            throw new UsernameNotFoundException("api.error.user.not.found");
        }
        UserPrincipal principle = new UserPrincipal(user, user.getAccountNonLocked(), user.getAccountNonExpired(),
                user.getCredentialsNonExpired(), user.getEnabled(), user.getId(), getAuthorities(user.getUserRole()));
        return principle;
    }

    /**
     * Find by user name.
     *
     * @param userName the user name
     * @return the user
     */
    @Override
    public User findByUserName(String userName) {

        log.info("UserServiceImpl : findByUserName");
        return userRepository.findByUserName(userName);
    }

    /**
     * Gets the authorities.
     *
     * @param role the role
     * @return the authorities
     */
    private Collection<? extends GrantedAuthority> getAuthorities(UserRole role) {
        return getGrantedAuthorities(getPrivileges(role));
    }

    /**
     * Gets the privileges.
     *
     * @param role the role
     * @return the privileges
     */
    private List<String> getPrivileges(UserRole role) {
        List<String> privileges = new ArrayList<>();
        List<Privilege> collection = new ArrayList<>();
        privileges.add(UserPrincipal.getRoleAuthority(role));
        collection.addAll(role.getPrivileges());
        for (Privilege item : collection) {
            privileges.add(item.getAuthority());
        }
        return privileges;
    }

    /**
     * Gets the granted authorities.
     *
     * @param privileges the privileges
     * @return the granted authorities
     */
    private List<GrantedAuthority> getGrantedAuthorities(List<String> privileges) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String privilege : privileges) {
            authorities.add(new SimpleGrantedAuthority(privilege));
        }
        return authorities;
    }

    /**
     * In this method first we extract address and then map in person entity Second
     * we signup (save the user in database and then send the email.)
     *
     * @param userSignupDTO the user signup DTO
     * @param role          the role
     * @return the user
     * @throws Exception the exception
     */
    @Override
    @Transactional
    public User addUser(UserSignupDTO userSignupDTO, String role) throws Exception {

        log.info("UserServiceImpl : signup");

        UserProfile userProfile = new UserProfile(userSignupDTO);

        List<UserRole> userRoleList = roleService.getAllRoles();

        String status = null;

        UserRole newUserRole = null;

        // This is to check the role in the database
        for (UserRole userRole : userRoleList) {
            if (userRole.getRoleName().equalsIgnoreCase(role)) {
                status = userRole.getStatus().toString();
                newUserRole = userRole;
            }
        }

        if (null == newUserRole || status.equalsIgnoreCase(UserStatus.INACTIVE.getStatus())) {
            log.error("No such role found ");
            throw new RuntimeException("no.such.role");
        }

        try {

            User user = new User(userSignupDTO);
            user.setPassword(passwordEncoder.encode(userSignupDTO.getPassword()));
            user.setUserRole(newUserRole);
            user.setUserProfile(userProfile);
            user.setStatus(UserStatus.valueOfCode("ACTIVE"));

            User newUser = userRepository.save(user);

            if (null == newUser || null == newUser.getId()) {
                log.error("exception while signing up");
                throw new SignupException("user.sign.up.fail");
            }
            log.info("new user created successfully : " + newUser);

            // TODO : call method to send email
            // registerOrChangePasswordEmail(userAfterSave, Constants.REGISTER);
            return newUser;
        } catch (SignupException exception) {
            log.error("exception while signing up");
            throw new SignupException("user.sign.up.fail");
        } catch (Exception e) {
            log.error("exception while signing up exception : " + e.getMessage());
            throw new Exception("user.sign.up.fail");
        }
    }

    /**
     * This method is called when user forgot the password.
     *
     * @param email the email
     * @return the boolean
     * @throws Exception the exception
     */
    @Override
    public Boolean sendEmailForForgotPassword(String email) throws Exception {

        Optional<User> userOptional = userRepository.findByEmail(email);

        if (!userOptional.isPresent()) {
            log.error("api error user not found : " + email);
            throw new UserNotFoundException("api.error.user.not.found");
        }
        try {
            createPasswordRequest(userOptional.get());
            log.info("email send for the forgot password");
            return true;
        } catch (Exception exception) {
            log.error("something went wrong while reseting the password");
            throw new Exception("something.went.wrong");
        }
    }

    /**
     * Creates the password reset token for user.
     *
     * @param user  the user
     * @param token This method creates token for password reset.
     */
    private void createPasswordResetTokenForUser(User user, String token) {
        PasswordResetToken passwordResetToken = new PasswordResetToken(token, user, false);
        passwordResetToken.setCreatedDate(LocalDateTime.now());
        passwordResetTokenRepository.save(passwordResetToken);
        log.info("Password token saved successfully with token : " + token);
    }

    /**
     * This is the method that only gets the isExpired value form the database.
     *
     * @param token the token
     * @return the boolean
     */
    @Override
    public Boolean findTokenIsExpired(String token) {

        log.info("UserServiceImpl : findTokenisExpired");

        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token);

        if (null == passwordResetToken) {
            log.error("Entered token : {} is not found.", token);
            throw new TokenNotFoundException("no.such.token.found");
        }

        return passwordResetToken.getIsExpired() ? true : false;
    }

    /**
     * Checks if is token expired.
     *
     * @param passwordResetToken the password reset token
     * @return This method check weather the given token is expired or not Return
     *         true if token is expired and return false if token is not expired
     */
    private boolean isTokenExpired(PasswordResetToken passwordResetToken) {
        LocalDateTime createdDate = passwordResetToken.getCreatedDate();
        Duration durationDifference = Duration.between(createdDate, LocalDateTime.now());

        if (durationDifference.toMinutes() >= Constants.EXPIRE_TOKEN_AFTER_MINUTES) {
            passwordResetToken.setIsExpired(true);
            passwordResetTokenRepository.save(passwordResetToken);
            log.info("Token is expired");
            return true;
        }

        log.info("Token is not expired");
        return false;

    }

    /**
     * This method check that if reset password token is validate or not. If token
     * is valid then it will return true else return false
     *
     * @param token the token
     * @return the boolean
     */
    @Override
    public Boolean validatePasswordResetToken(String token) {

        log.info("UserServiceImpl : validatePasswordResetToken");
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token);
        boolean isTokenExpired = isTokenExpired(passwordResetToken);

        if (null != passwordResetToken && !isTokenExpired) {
            log.info("Token is valid ");
            return true;
        }
        log.error("Token is invalid" + token);
        return false;
    }

    /**
     * This is the method that is suppose to send mail if user gets register TODO :
     * This method needes to be change.
     *
     * @param user                     the user
     * @param changePasswordOrRegister the change password or register
     * @return the string
     * @throws Exception the exception
     */
    @Override
    public String registerOrChangePasswordEmail(User user, String changePasswordOrRegister) throws Exception {

        log.info("UserServiceImpl : registerOrChangePasswordEmail");

        Map<String, Object> properties = new HashMap<String, Object>();
        properties.put(Constants.NAME,
                user.getUserProfile().getFirstName() + Constants.SPACE + user.getUserProfile().getLastName());
        properties.put(Constants.SIGN, user.getUserName());

        if (changePasswordOrRegister.equals(Constants.REGISTER)) {
            Mail.builder().from(Constants.MAIL_SENDER).to(user.getUserProfile().getEmail())
                    .htmlTemplate(new Mail.HtmlTemplate(Constants.REGISTER, properties)).subject("Welcome").build();
            return HttpStatus.OK.name();
        } else if (changePasswordOrRegister.equals(Constants.CHANGE_PASSWORD)) {
            Mail.builder().from(Constants.MAIL_SENDER).to(user.getUserProfile().getEmail())
                    .htmlTemplate(new Mail.HtmlTemplate(Constants.CHANGE_PASSWORD, properties))
                    .subject("change of password").build();
            return HttpStatus.OK.name();
        } else {
            throw new Exception("failed.to.send.mail");
        }
    }

    /**
     * Find user by role id.
     *
     * @param roleId the role id
     * @return the list
     */
    @Override
    public List<User> findUserByRoleId(UUID roleId) {
        log.info("UserServiceImpl : Start findUserByRoleId {}", roleId);
        List<User> users = userRepository.findUserByRoleId(roleId);
        if (users == null) {
            log.error("UserServiceImpl : findUserByRoleId {} not found", roleId);
            throw new APIException("user.not.found", HttpStatus.NOT_FOUND);
        }
        log.info("UserServiceImpl : End findUserByRoleId {}", roleId);
        return users;
    }

    /**
     * Change password.
     *
     * @param resetPassword the reset password
     * @param token         the token
     */
    @Transactional
    public void changePassword(ResetPassword resetPassword, String token) {
        log.info("UserServiceImpl : changePassowrd");

        if (findTokenIsExpired(token) || !validatePasswordResetToken(token)) {
            log.error("The token : " + token + " is expried or invalid");
            throw new TokenNotFoundException("token.not.valid");
        }

        if (!resetPassword.getPassword().equals(resetPassword.getConfirmPassword())) {
            log.error("password and confirm password are not same");
            throw new PasswordException("password.confirm.password.not.same");
        }
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token);
        UUID userId = passwordResetToken.getUser().getId();
        Optional<User> userOption = userRepository.findById(userId);
        User user = userOption.get();

        if (user == null) {
            log.error("Not able to fetch user from token " + token);
            throw new UserNotFoundException("not.able.to.fetch.user.from.given.token");
        }

        user.setPassword(passwordEncoder.encode(resetPassword.getConfirmPassword()));
        userRepository.save(user);
        passwordResetToken.setIsExpired(true);
        passwordResetTokenRepository.save(passwordResetToken);
        log.info("The new password is set successfully");
//		TODO : Call a method that will send email 
//		userService.registerOrChangePasswordEmail(user, Constants.CHANGE_PASSWORD);

    }

    /**
     * Find by user id.
     *
     * @param userId the user id
     * @return the user
     */
    @Override
    public User findByUserId(UUID userId) {
        return userRepository.findById(userId).get();
    }

    /**
     * Signup.
     *
     * @param userSignupDTO the user signup DTO
     * @param role          the role
     * @return the string
     */
    @Override
    public String signup(UserSignupDTO userSignupDTO, String role) {
        log.info("signup() method call...");
        Set<PersonAddress> addressEntityList = new HashSet<>();
        try {
            // Extract Address
            if (userSignupDTO.getAddress() != null) {
                for (AddressDTO addressDTO : userSignupDTO.getAddress()) {
                    addressEntityList.add(mapperUserService.mapAddressFromDTO(addressDTO));
                }
            }
            // Map Into Person Entity
            UserProfile profileEntity = mapperUserService.mapPersonFromDTO(userSignupDTO);
            if (addressEntityList.size() > 0) {
                profileEntity.getUserAddresses().addAll(addressEntityList);
            }
            // Find Patient ROLE
            UserRole roleEntity = roleService.getAllRoles().stream().filter(r -> r.getRoleName().equalsIgnoreCase(role))
                    .findAny().orElse(null);
//			if (null == roleEntity || roleEntity.getStatus().equalsIgnoreCase(UserStatus.INACTIVE.getStatus())) {

            if (null == roleEntity
                    || roleEntity.getStatus().toString().equalsIgnoreCase(UserStatus.INACTIVE.getStatus().toString())) {
                throw new RuntimeException("Role not found.");
            }
            // Finally User Signup
            User userEntity = mapperUserService.mapUserFromDTO(userSignupDTO);
            userEntity.setUserRole(roleEntity);
            userEntity.setUserProfile(profileEntity);
            User userAfterSave = userRepository.save(userEntity);
            if (userAfterSave != null && userAfterSave.getId() != null) {
                // Send Email
                String result = registerOrChangePasswordEmail(userAfterSave, "register");
                return result;
            }
        } catch (DataIntegrityViolationException ex) {
            log.error(ex.getMessage());
            return "User with same email address is already registered.";
        } catch (ConstraintViolationException ex) {
            log.error(ex.getMessage());
            return ex.getMessage();
        } catch (GenericJDBCException ex) {
            log.error(ex.getMessage());
            return ex.getMessage();
        } catch (Exception ex) {
            log.error(ex.getMessage());
            return ex.getMessage();
        }
        return null;
    }

    /**
     * Gets the user list.
     *
     * @param role the role
     * @return the user list
     */
    @Override
    public List<User> getUserList(String role) {
        switch (role) {
            case "ROLE_SUPER_ADMIN":
                return entityManager.createNamedQuery("user.select.by.role", User.class)
                        .setParameter("roleName", "ROLE_SUPER_ADMIN").getResultList();
            case "ROLE_ADMIN":
                return entityManager.createNamedQuery("user.select.by.role", User.class)
                        .setParameter("roleName", "ROLE_USER").getResultList();
        }
        return new ArrayList<User>();
    }

    /**
     * Send email.
     *
     * @param email the email
     * @return the string
     */
    @Override
    public String sendEmail(String email) {
        log.info("sendEmail() method call...");
        String result = null;
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional == null || !userOptional.isPresent()) {
            log.error("User not found.");
            return HttpStatus.NOT_FOUND.name();
        } else if (userOptional != null) {
            result = createPasswordRequest(userOptional.get());
        }
        return result;
    }

    /**
     * Creates the password request.
     *
     * @param user the user
     * @return the string
     */
    String createPasswordRequest(User user) {
        String token = UUID.randomUUID().toString();
        createPasswordResetTokenForUser(user, token);
        String result = null;
        result = constructResetTokenEmail(user, token);
        return result;
    }

    /**
     * Construct reset token email.
     *
     * @param user  the user
     * @param token the token
     * @return the string
     */
    String constructResetTokenEmail(User user, String token) {
        String url = "http://localhost:4200/#/password/reset?token=" + token;
        // Construct Email
        Map<String, Object> properties = new HashMap<String, Object>();
        properties.put("name", user.getUserProfile().getFirstName() + " " + user.getUserProfile().getLastName());
        properties.put("location", "India");
        properties.put("sign", "Pritesh Desai");
        properties.put("url", url);
        Mail mail = Mail.builder().from("pritesh.desai@brainvire.com").to(user.getUserProfile().getEmail())
                .htmlTemplate(new Mail.HtmlTemplate("sample", properties)).subject("Forgot Password").build();
        String result;
        result = senderService.sendEmail(mail);
        return result;
    }

    /**
     * Signup by admin.
     *
     * @param userSignupDTO the user signup DTO
     * @param role          the role
     * @return the string
     */
    @Override
    public String signupByAdmin(AdminAddUserDTO userSignupDTO, String role) {
        // TODO Auto-generated method stub
        log.info("signupByAdmin() method call...");
        Set<PersonAddress> addressEntityList = new HashSet<>();
        try {
            // Map Into Person Entity
            UserProfile profileEntity = mapperUserService.mapPersonFromDTOAdmin(userSignupDTO);

            // Find Patient ROLE
            UserRole roleEntity = roleService.getAllRoles().stream().filter(r -> r.getRoleName().equalsIgnoreCase(role))
                    .findAny().orElse(null);
            if (null == roleEntity
                    || roleEntity.getStatus().toString().equalsIgnoreCase(UserStatus.INACTIVE.getStatus().toString())) {
                throw new RuntimeException("Role not found.");
            }
            // Finally User Signup
            User userEntity = mapperUserService.mapUserFromAdminDTO(userSignupDTO);
            userEntity.setUserRole(roleEntity);
            userEntity.setUserProfile(profileEntity);
            User userAfterSave = userRepository.save(userEntity);
            if (userAfterSave != null && userAfterSave.getId() != null) {
                // Send Email
                String result = registerOrChangePasswordEmail(userAfterSave, "register");
                return result;
            }
        } catch (DataIntegrityViolationException ex) {
            log.error(ex.getMessage());
            return "User with same email address is already registered.";
        } catch (ConstraintViolationException ex) {
            log.error(ex.getMessage());
            return ex.getMessage();
        } catch (GenericJDBCException ex) {
            log.error(ex.getMessage());
            return ex.getMessage();
        } catch (Exception ex) {
            log.error(ex.getMessage());
            return ex.getMessage();
        }
        return null;
    }

    /**
     * Save CSV.
     *
     * @param file the file
     * @return the string
     */
    @Override
    public String saveCSV(MultipartFile file) {
        // TODO Auto-generated method stub
        try {
            String fileFormat = "text/csv";
            if (fileFormat.equals(file.getContentType())) {
                BufferedReader fileReader = new BufferedReader(new InputStreamReader(file.getInputStream(), "UTF-8"));
                CSVParser csvParser = new CSVParser(fileReader,
                        CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());
                List<CSVRecord> csvRecords = csvParser.getRecords();
                List<User> users = new ArrayList<User>();
                for (CSVRecord csvRecord : csvRecords) {
                    String s = csvRecord.get("Date of Birth");
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                    LocalDate date = LocalDate.parse(s, formatter);

                    User userEntity = new User(csvRecord.get("Full Name"), csvRecord.get("User Name"),
                            csvRecord.get("Email"), csvRecord.get("Gender"), date, csvRecord.get("Phone Number"),
                            csvRecord.get("Status"));
                    userEntity.setPassword(passwordEncoder.encode("test"));
                    users.add(userEntity);
                }

                UserRole roleEntity = roleService.getAllRoles().stream()
                        .filter(r -> r.getRoleName().equalsIgnoreCase("ROLE_SUPER_ADMIN")).findAny().orElse(null);
                if (null == roleEntity || roleEntity.getStatus().toString()
                        .equalsIgnoreCase(UserStatus.INACTIVE.getStatus().toString())) {
                    throw new RuntimeException("Role not found.");
                }
                for (User userEntity : users) {
                    userEntity.setUserRole(roleEntity);
                }
                userRepository.saveAll(users);
                return HttpStatus.OK.name();
            }
        } catch (IOException e) {
            log.error(e.getMessage());
            return e.getMessage();
        } catch (Exception e) {
            log.error(e.getMessage());
            return e.getMessage();
        }
        return null;
    }

    /**
     * Load.
     *
     * @param entities the entities
     * @return the byte array input stream
     */
    @Override
    public ByteArrayInputStream load(List<User> entities) {
        ByteArrayInputStream in = usersToCSV(entities);
        return in;
    }

    /**
     * Users to CSV.
     *
     * @param userEntities the user entities
     * @return the byte array input stream
     */
    public ByteArrayInputStream usersToCSV(List<User> userEntities) {
        final CSVFormat format = CSVFormat.DEFAULT.withHeader("Full Name", "User Name", "Email", "Gender",
                "Date of Birth", "Phone Number", "Status");
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
                CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), format);) {
            for (User entity : userEntities) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                String dateOfBirth = formatter.format(entity.getUserProfile().getDateOfBirth());
                List<Object> data = Arrays.asList(
                        entity.getUserProfile().getFirstName() + " " + entity.getUserProfile().getLastName(),
                        entity.getUserName(), entity.getUserProfile().getEmail(), entity.getUserProfile().getGender(),
                        dateOfBirth, entity.getUserProfile().getCellPhone(), entity.getStatus());
                csvPrinter.printRecord(data);
            }
            csvPrinter.flush();
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException("fail to import data to CSV file: " + e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Delete user.
     *
     * @param userId the user id
     * @return the string
     */
    @Override
    public String deleteUser(UUID userId) {
        try {
            List<OfferUser> offerList = offerUserRepository.findByUser(userRepository.getById(userId));
            for (OfferUser offerUser : offerList) {
                offerUserRepository.deleteById(offerUser.getId());
            }
            userRepository.deleteById(userId);
            return HttpStatus.OK.name();
        } catch (Exception e) {
            log.error(e.getMessage());
            return e.getMessage();
        }
    }

    /**
     * Search.
     *
     * @param userFilter the user filter
     * @param role       the role
     * @return the search info
     */
    @Override
    public SearchInfo search(UserFilter userFilter, String role) {
        String query = createCommonQuery(userFilter.getRoleName(), role);
        int count = getTotalUsers(query);
        if (userFilter.getFullName() != null && !userFilter.getFullName().isEmpty()) {
            query += "and UPPER(upr.firstName) LIKE UPPER('%" + userFilter.getFullName() + "%') ";
        }
        if (userFilter.getEmail() != null && !userFilter.getEmail().isEmpty()) {
            query += "and UPPER(upr.email) LIKE UPPER('%" + userFilter.getEmail() + "%') ";
        }
        if (userFilter.getStatus() != null && !userFilter.getStatus().isEmpty()) {
            query += "and UPPER(u.status) =  UPPER('" + userFilter.getStatus() + "')";
        }
        if (userFilter.getGender() != null && !userFilter.getGender().isEmpty()) {
            query += "and upr.gender LIKE '%" + userFilter.getGender() + "%'";
        }
        if (userFilter.getCellPhone() != null && !userFilter.getCellPhone().isEmpty()) {
            query += "and UPPER(upr.cellPhone) LIKE UPPER('%" + userFilter.getCellPhone() + "%') ";
        }
        String queryParam = Util.getFilterQuery(userFilter, query);

        List<User> results = entityManager.createQuery(queryParam)
                .setFirstResult(Integer.valueOf(userFilter.getStartRec()))
                .setMaxResults(Integer.valueOf(userFilter.getEndRec())).getResultList();
        SearchInfo info = new SearchInfo();
        info.setUsers(results);
        info.setCount(count);
        return info;
    }

    /**
     * Gets the total users.
     *
     * @param query the query
     * @return the total users
     */
    private int getTotalUsers(String query) {
        List<User> entities = entityManager.createQuery(query).getResultList();
        return entities.size();
    }

    /**
     * Creates the common query.
     *
     * @param roleNameFilter the role name filter
     * @param role           the role
     * @return the string
     */
    private String createCommonQuery(String roleNameFilter, String role) {
        String query = null;
        query = "select u from User u join u.userProfile upr join u.userRole ur ";
        if (role.equals("ROLE_SUPER_ADMIN")) {
            if (roleNameFilter != null) {
                query += "where ur.roleName ='" + roleNameFilter.toUpperCase() + "'";
            } else {
                query += "where ur.roleName = 'ROLE_SUPER_ADMIN'";
            }
        } else if (role.equals("ROLE_ADMIN")) {
            if (roleNameFilter != null && roleNameFilter.equals("ROLE_SUBADMIN")) {
                query += "where ur.roleName = 'ROLE_SUBADMIN' ";
            } else {
                query += "where ur.roleName = 'ROLE_USER' ";
            }
        }
        return query;
    }

    /**
     * Write excel.
     *
     * @param userDetails the user details
     * @param filename    the filename
     * @return the byte array input stream
     */
    @Override
    public ByteArrayInputStream writeExcel(List<User> userDetails, String filename) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Users");

            Row row = sheet.createRow(0);
            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
//			headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            // Creating header
            Cell cell = row.createCell(0);
            cell.setCellValue("Fullname");
            cell.setCellStyle(headerCellStyle);

            cell = row.createCell(1);
            cell.setCellValue("Username");
            cell.setCellStyle(headerCellStyle);

            cell = row.createCell(2);
            cell.setCellValue("Email");
            cell.setCellStyle(headerCellStyle);

            cell = row.createCell(3);
            cell.setCellValue("Cellphone");
            cell.setCellStyle(headerCellStyle);

            // Creating data rows for each customer
            for (int i = 0; i < userDetails.size(); i++) {
                Row dataRow = sheet.createRow(i + 1);
                dataRow.createCell(0).setCellValue(userDetails.get(i).getUserProfile().getFirstName() + " "
                        + userDetails.get(i).getUserProfile().getLastName());
                dataRow.createCell(1).setCellValue(userDetails.get(i).getUserName());
                dataRow.createCell(2).setCellValue(userDetails.get(i).getUserProfile().getEmail());
                dataRow.createCell(3).setCellValue(userDetails.get(i).getUserProfile().getCellPhone());
            }

            // Making size of column auto resize to fit with data
            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);
            sheet.autoSizeColumn(2);
            sheet.autoSizeColumn(3);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return new ByteArrayInputStream(outputStream.toByteArray());
        } catch (IOException | java.io.IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Update user by admin.
     *
     * @param addUserDTO the add user DTO
     * @param userId     the user id
     * @return the string
     */
    @Override
    public String updateUserByAdmin(AdminAddUserDTO addUserDTO, UUID userId) {
        try {
            // Map Into Person Entity
            User userEntity = findByUserId(userId);
            if (userEntity != null) {
                userEntity.getUserProfile().setFirstName(addUserDTO.getFirstName());
                userEntity.getUserProfile().setLastName(addUserDTO.getLastName());
                userEntity.setUserName(addUserDTO.getUserName());
                userEntity.getUserProfile().setCellPhone(addUserDTO.getCellPhone());
                userEntity.getUserProfile().setDateOfBirth(addUserDTO.getDateOfBirth());
                userEntity.getUserProfile().setEmail(addUserDTO.getEmail());
                userEntity.getUserProfile().setGender(addUserDTO.getGender());
                userRepository.save(userEntity);
                return HttpStatus.OK.name();
            } else {
                return "User not found.";
            }
        } catch (ConstraintViolationException ex) {
            log.error(ex.getMessage());
            return ex.getMessage();
        } catch (GenericJDBCException ex) {
            log.error(ex.getMessage());
            return ex.getMessage();
        } catch (Exception ex) {
            log.error(ex.getMessage());
            return ex.getMessage();
        }
    }

    /**
     * Find tokenis expired.
     *
     * @param token the token
     * @return the boolean
     */
    @Override
    public Boolean findTokenisExpired(String token) {
        log.info("UserServiceImpl : findTokenisExpired");

        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token);

        if (null == passwordResetToken) {
            log.error("Entered token : {} is not found.", token);
            throw new TokenNotFoundException("no.such.token.found");
        }

        return passwordResetToken.getIsExpired() ? true : false;
    }
}
