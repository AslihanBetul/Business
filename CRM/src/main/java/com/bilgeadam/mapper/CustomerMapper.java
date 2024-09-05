package com.bilgeadam.mapper;

import com.bilgeadam.dto.request.CustomerSaveDTO;
import com.bilgeadam.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CustomerMapper {

    CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);

    Customer customerSaveDTOToCustomer(CustomerSaveDTO customerSaveDTO);


}








