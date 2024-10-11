package com.businessapi.services;

import com.businessapi.dto.request.BudgetSaveRequestDTO;
import com.businessapi.dto.request.BudgetUpdateRequestDTO;
import com.businessapi.dto.request.PageRequestDTO;
import com.businessapi.dto.response.BudgetByDepartmentResponseDTO;
import com.businessapi.dto.response.BudgetCategoryResponseDTO;
import com.businessapi.dto.response.BudgetFindAllResponseDTO;
import com.businessapi.dto.response.BudgetMergedByDepartmentResponseDTO;
import com.businessapi.entity.Budget;
import com.businessapi.entity.Department;
import com.businessapi.entity.Expense;
import com.businessapi.entity.enums.EBudgetCategory;
import com.businessapi.entity.enums.EStatus;
import com.businessapi.exception.ErrorType;
import com.businessapi.exception.FinanceServiceException;
import com.businessapi.repositories.BudgetRepository;
import com.businessapi.repositories.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BudgetService {
    private final BudgetRepository budgetRepository;
    private final DepartmentService departmentService;
    private final ExpenseService expenseService;

    @Transactional
    public Boolean save(BudgetSaveRequestDTO dto) {
        Department department = departmentService.getDepartmentById(dto.departmentId());
        department.getBudgets().size();
        Budget budget = Budget.builder()
                .department(department)
                .subAmount(dto.subAmount())
                .budgetCategory(dto.budgetCategory())
                .description(dto.description())
                .build();

        department.getBudgets().add(budget);
        budgetRepository.save(budget);
        return true;
    }

    public Boolean update(BudgetUpdateRequestDTO dto) {
        Department department = departmentService.getDepartmentById(dto.departmentId());
        Budget budget = budgetRepository.findById(dto.id()).orElseThrow(() -> new FinanceServiceException(ErrorType.BUDGET_NOT_FOUND));
        budget.setDepartment(department);
        budget.setSubAmount(dto.subAmount());
        budget.setBudgetCategory(dto.budgetCategory());
        budget.setDescription(dto.description());

        budget.setTotalAmount(budget.getTotalAmount().add(budget.getSubAmount()));
        budgetRepository.save(budget);
        return true;
    }

    public Boolean delete(Long id) {
        Budget budget = budgetRepository.findById(id).orElseThrow(() -> new FinanceServiceException(ErrorType.BUDGET_NOT_FOUND));
        budget.setStatus(EStatus.DELETED);

        budgetRepository.save(budget);

        return true;
    }

    public List<BudgetMergedByDepartmentResponseDTO> findAll(PageRequestDTO dto) {
        List<Department> departmentList = departmentService.findAll();
        List<BudgetMergedByDepartmentResponseDTO> budgetMergedByDepartmentResponseDTOS = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;
        BigDecimal spentAmount = BigDecimal.ZERO;
        for (Department department : departmentList) {
            List<Budget> budgets = department.getBudgets();
            List<Expense> expenses = department.getExpenses();
            for (Budget budget : budgets) {
                if (budget.getStatus().equals(EStatus.ACTIVE)) {
                    totalAmount = totalAmount.add(budget.getSubAmount());
                }
            }
            for (Expense expense : expenses) {
                if (expense.getDepartment().equals(department)) {
                    spentAmount = spentAmount.add(expense.getAmount());
                }
            }
            budgetMergedByDepartmentResponseDTOS.add(new BudgetMergedByDepartmentResponseDTO(department.getId(), totalAmount, spentAmount, department.getName()));
            totalAmount = BigDecimal.ZERO;
            spentAmount = BigDecimal.ZERO;
        }
        return budgetMergedByDepartmentResponseDTOS;
    }


    public Budget findById(Long id) {
        return budgetRepository.findById(id).orElseThrow(() -> new FinanceServiceException(ErrorType.BUDGET_NOT_FOUND));
    }

//    public Budget findByDepartment(String department) {
//        return budgetRepository.findByDepartment(department).getFirst();
//    }


    public Void getDepartments() {
//        List<Budget> budgets = budgetRepository.findAll();
//        List<DepartmentResponseDTO> departments = new ArrayList<>();
//        for (Budget budget : budgets) {
//            if (departments.stream().noneMatch(department -> department.department().equals(budget.getDepartment()))) {
//                departments.add(new DepartmentResponseDTO((long) (departments.size() + 1), budget.getDepartment()));
//            }
//        }
//        return departments;
        return null;
    }

//    public void setSpentAmount(String department, BigDecimal bigDecimal) {
//        Budget budget = budgetRepository.findByDepartment(department).getFirst();
//        budget.setSpentAmount(budget.getSpentAmount().add(bigDecimal));
//        budgetRepository.save(budget);
//    }

    public List<BudgetCategoryResponseDTO> getAllCategories() {
        List<EBudgetCategory> categories = new ArrayList<>(Arrays.asList(EBudgetCategory.values()));
        List<BudgetCategoryResponseDTO> categoryResponseDTOS = new ArrayList<>();
        for (long l = 0; l < categories.size(); l++) {
            categoryResponseDTOS.add(new BudgetCategoryResponseDTO(l, categories.get((int) l).name()));
        }
        return categoryResponseDTOS;
    }

//    public List<BudgetByDepartmentResponseDTO> findAllByDepartmentId(Long departmentId) {
//        Department department = departmentService.getDepartmentById(departmentId);
//        List<Budget> allBudgetsByDepartment = budgetRepository.findAllByDepartment(department);
//        allBudgetsByDepartment.removeIf(budget -> budget.getStatus().equals(EStatus.DELETED));
//        List<BudgetByDepartmentResponseDTO> budgetByDepartmentResponseDTOS = new ArrayList<>();
//        for (Budget budget : allBudgetsByDepartment) {
//            budgetByDepartmentResponseDTOS.add(new BudgetByDepartmentResponseDTO(budget.getId(), budget.getBudgetCategory(), budget.getSubAmount(), budget.getDescription()));
//        }
//        return budgetByDepartmentResponseDTOS;
//    }

    public List<BudgetByDepartmentResponseDTO> findAllByDepartmentName (String departmentName) {
        Department department = departmentService.getDepartmentByName(departmentName);
        if (department == null) {
            throw new FinanceServiceException(ErrorType.DEPARTMENT_NOT_FOUND);
        }
        List<Budget> allBudgetsByDepartment = budgetRepository.findAllByDepartment(department);
        allBudgetsByDepartment.removeIf(budget -> budget.getStatus().equals(EStatus.DELETED));
        List<BudgetByDepartmentResponseDTO> budgetByDepartmentResponseDTOS = new ArrayList<>();
        for (Budget budget : allBudgetsByDepartment) {
            budgetByDepartmentResponseDTOS.add(new BudgetByDepartmentResponseDTO(budget.getId(), budget.getBudgetCategory(), budget.getSubAmount(), budget.getDescription()));
        }
        return budgetByDepartmentResponseDTOS;
    }
}
