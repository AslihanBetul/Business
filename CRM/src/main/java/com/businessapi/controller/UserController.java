package com.businessapi.controller;

import com.businessapi.dto.request.UserSaveTestDTO;
import com.businessapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


import static com.businessapi.constants.EndPoints.SAVE;
import static com.businessapi.constants.EndPoints.USER;

@RestController
@RequestMapping(USER)
@RequiredArgsConstructor
@CrossOrigin(origins = "*", methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT, RequestMethod.DELETE})
public class UserController {
    private final UserService userService;
    @PostMapping (SAVE)
    public boolean save(@RequestBody UserSaveTestDTO dto){
        userService.saveUserTest(dto);
        return true;
    }

}
