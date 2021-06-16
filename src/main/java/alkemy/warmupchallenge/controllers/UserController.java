package alkemy.warmupchallenge.controllers;

import alkemy.warmupchallenge.dtos.UserDto;
import alkemy.warmupchallenge.models.requests.UserRegisterRequest;
import alkemy.warmupchallenge.models.responses.OperationStatusModel;
import alkemy.warmupchallenge.models.responses.UserResponse;
import alkemy.warmupchallenge.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("api/v1/auth")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    ModelMapper mapper;

    @GetMapping(path = "/{id}")
    public UserResponse getUser(@PathVariable long id) {
        return mapper.map(userService.getUser(id), UserResponse.class);
    }

    @PostMapping(path = "/sign_up")
    public UserResponse createUser(@Valid @RequestBody UserRegisterRequest userRegisterRequest) {
        UserDto userDto = userService.createUser(mapper.map(userRegisterRequest, UserDto.class));
        return mapper.map(userDto, UserResponse.class);
    }


    @DeleteMapping(path = "/{id}")
    public OperationStatusModel deleteUser(@PathVariable long id) {
        OperationStatusModel operationStatusModel = new OperationStatusModel();
        operationStatusModel.setName("DELETE");
        userService.deleteUser(id);
        operationStatusModel.setResult("SUCCESS");
        return operationStatusModel;
    }





}
