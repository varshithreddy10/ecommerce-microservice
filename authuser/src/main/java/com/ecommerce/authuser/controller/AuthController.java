package com.ecommerce.authuser.controller;



import com.ecommerce.authuser.dto.LoginDto;
import com.ecommerce.authuser.dto.LoginResponseDto;
import com.ecommerce.authuser.dto.SignUpDto;
import com.ecommerce.authuser.dto.UserDto;
import com.ecommerce.authuser.springsecurity.securityservice.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController
{

// . .
    private final AuthService authService;



    @PostMapping("/signup")
    public ResponseEntity<UserDto> signUp(@RequestBody SignUpDto signUpDto)
    {
        log.info("control entered the signup in the authcontroller with the signupdto "+signUpDto);
        UserDto userDto = authService.signUp(signUpDto);
        log.info("execution of the signup is completed and the response is "+signUpDto);

        return new ResponseEntity<>(userDto , HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginDto loginDto, HttpServletRequest request,
                                                  HttpServletResponse response)
    {


        log.info("control entered the login method in the controller using the logindto "+loginDto);
        LoginResponseDto loginResponseDto = authService.login(loginDto);
        log.info("execution of the login method is completed and the response is "+loginResponseDto);
        System.out.println("====================================================================================================================================================================================================================================");

        return new ResponseEntity<>(loginResponseDto , HttpStatus.OK);
    }

    @GetMapping("/test")
    public ResponseEntity<String> test()
    {
        String message = "melanie hicks, dani danials, bunny colby, bunny madison";

        return new ResponseEntity<>(message , HttpStatus.OK);
    }


}
