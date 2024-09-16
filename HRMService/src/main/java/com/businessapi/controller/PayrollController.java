package com.businessapi.controller;


import com.businessapi.service.PayrollService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import static com.businessapi.constants.EndPoints.*;

@RestController
@RequestMapping(PAYROLL)
@RequiredArgsConstructor
@CrossOrigin(origins = "*", methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT,RequestMethod.DELETE})
public class PayrollController {
    private  final PayrollService payrollService;
}
