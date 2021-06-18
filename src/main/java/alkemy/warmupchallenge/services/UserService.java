package alkemy.warmupchallenge.services;

import alkemy.warmupchallenge.dtos.UserDto;
import alkemy.warmupchallenge.entities.UserEntity;
import alkemy.warmupchallenge.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    ModelMapper mapper;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                String.format("User doesn't exist: ", email)));
        return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), new ArrayList<>());
    }

    public UserDto createUser(UserDto userToCreate) {
        if(userRepository.findByEmail(userToCreate.getEmail()).isPresent()) throw new RuntimeException("Email is already registered.");
        UserEntity userEntity = UserEntity.builder()
                .email(userToCreate.getEmail())
                .encryptedPassword(bCryptPasswordEncoder.encode(userToCreate.getPassword()))
                .build();
        return mapper.map(userRepository.save(userEntity), UserDto.class);
    }

    public UserDto updateUserDto(long id, UserDto userToUpdate) {
        UserEntity userEntity = getUserById(id);
        userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(userToUpdate.getPassword()));
        return mapper.map(userRepository.save(userEntity), UserDto.class);
    }

    public void deleteUser(long id) {
        UserEntity userEntity = getUserById(id);
        userRepository.delete(userEntity);
    }

    public UserDto getUser(long id) {
        return mapper.map(getUserById(id), UserDto.class);
    }

    public UserEntity getUserById(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User doesn't exist."));
    }



}
