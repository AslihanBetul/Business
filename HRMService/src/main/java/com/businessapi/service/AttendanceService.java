package com.businessapi.service;

import com.businessapi.dto.request.AttendanceSaveRequestDTO;
import com.businessapi.dto.request.AttendanceUpdateRequestDTO;
import com.businessapi.dto.response.AttendanceResponseDTO;
import com.businessapi.entity.Attendance;
import com.businessapi.exception.ErrorType;
import com.businessapi.exception.HRMException;
import com.businessapi.repository.AttendanceRepository;
import com.businessapi.utility.enums.EStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AttendanceService {
    private final AttendanceRepository attendanceRepository;

    public Boolean save(AttendanceSaveRequestDTO dto) {
        Attendance attendance=Attendance.builder()
                .employeeId(dto.employeeId())
                .checkInDateTime(dto.checkInDateTime())
                .checkOutDateTime(dto.checkOutDateTime())
                .status(EStatus.ACTIVE)

        .build();
        attendanceRepository.save(attendance);
        return true;
    }

    public Boolean update(AttendanceUpdateRequestDTO dto) {
        Attendance attendance = attendanceRepository.findById(dto.id()).orElseThrow(() -> new HRMException(ErrorType.NOT_FOUNDED_ATTENDANCE));
        attendance.setCheckOutDateTime(dto.checkOutDateTime()!=null ? dto.checkOutDateTime():attendance.getCheckOutDateTime());
        attendance.setCheckInDateTime(dto.checkInDateTime()!=null ? dto.checkInDateTime():attendance.getCheckInDateTime());
        attendance.setEmployeeId(dto.employeeId()!=null ? dto.employeeId():attendance.getEmployeeId());
        attendanceRepository.save(attendance);
        return true;
    }

    public AttendanceResponseDTO findById(Long id) {
        Attendance attendance = attendanceRepository.findById(id).orElseThrow(() -> new HRMException(ErrorType.NOT_FOUNDED_ATTENDANCE));
        return AttendanceResponseDTO.builder()
               .employeeId(attendance.getEmployeeId())
               .checkInDateTime(attendance.getCheckInDateTime())
               .checkOutDateTime(attendance.getCheckOutDateTime())
               .build();
    }

    public List<AttendanceResponseDTO> findAll() {
        List<Attendance> attendanceList = attendanceRepository.findAll();
        List<AttendanceResponseDTO> attendanceResponseDTOList=new ArrayList<>();
        attendanceList.forEach(attendance ->
                attendanceResponseDTOList.add(AttendanceResponseDTO.builder()
                       .employeeId(attendance.getEmployeeId())
                       .checkInDateTime(attendance.getCheckInDateTime())
                       .checkOutDateTime(attendance.getCheckOutDateTime())
                       .build())
        );
        return attendanceResponseDTOList;

    }

    public Boolean delete(Long id) {
        Attendance attendance = attendanceRepository.findById(id).orElseThrow(() -> new HRMException(ErrorType.NOT_FOUNDED_ATTENDANCE));
        attendance.setStatus(EStatus.PASSIVE);
        attendanceRepository.save(attendance);
        return true;

    }
}
