package shop.mtcoding.jobara.employee;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import shop.mtcoding.jobara.common.ex.CustomException;
import shop.mtcoding.jobara.common.util.Verify;
import shop.mtcoding.jobara.employee.dto.EmployeeReq.EmployeeJoinReqDto;
import shop.mtcoding.jobara.employee.dto.EmployeeReq.EmployeeLoginReqDto;
import shop.mtcoding.jobara.employee.dto.EmployeeReq.EmployeeUpdateReqDto;
import shop.mtcoding.jobara.employee.dto.EmployeeResp.EmployeeAndResumeRespDto;
import shop.mtcoding.jobara.employee.dto.EmployeeResp.EmployeeUpdateRespDto;
import shop.mtcoding.jobara.user.model.User;
import shop.mtcoding.jobara.user.vo.UserVo;

@Controller
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private HttpSession session;

    @GetMapping("/employee/joinForm")
    public String joinForm() {
        return "employee/joinForm";
    }

    @GetMapping("/employee/loginForm")
    public String loginForm() {
        return "employee/loginForm";
    }

    @GetMapping("/employee/list")
    public String employeeList(Model model) {
        UserVo principal = (UserVo) session.getAttribute("principal");
        List<EmployeeAndResumeRespDto> employeeListPS = employeeService.getEmployee();
        model.addAttribute("allEmployeeList", employeeListPS);
        model.addAttribute("principal", principal);
        if (principal != null) {
            if (principal.getRole().equals("company")) {
                List<EmployeeAndResumeRespDto> recommendEmployeeListPS = employeeService
                        .getRecommendEmployee(principal.getId());
                model.addAttribute("recommendEmployeeList", recommendEmployeeListPS);
            }
        }
        return "employee/list";
    }

    @GetMapping("/employee/{id}")
    public String employeeDetail(@PathVariable int id, Model model) {
        EmployeeAndResumeRespDto employeePS = employeeService.getEmployee(id);
        model.addAttribute("employee", employeePS);
        return "employee/detail";
    }

    @GetMapping("/employee/updateForm")
    public String updateForm(Model model) {
        UserVo principal = (UserVo) session.getAttribute("principal");
        Verify.validateObject(principal, "???????????? ???????????????.");
        if (!principal.getRole().equals("employee")) {
            throw new CustomException("????????? ????????????.", HttpStatus.FORBIDDEN);
        }
        EmployeeUpdateRespDto employeeUpdateRespDto = employeeService.getEmployeeUpdateRespDto(principal.getId());
        model.addAttribute("employeeDto", employeeUpdateRespDto);
        return "employee/updateForm";
    }

    @PostMapping("/employee/join")
    public String join(EmployeeJoinReqDto employeeJoinReqDto) {
        Verify.validateString(employeeJoinReqDto.getUsername(), "??????????????? ???????????????.");
        Verify.validateString(employeeJoinReqDto.getPassword(), "????????? ???????????????.");
        Verify.validateString(employeeJoinReqDto.getEmail(), "???????????? ???????????????.");
        employeeService.insertEmployee(employeeJoinReqDto);
        return "redirect:/loginForm";
    }

    @PostMapping("/employee/login")
    public String join(EmployeeLoginReqDto employeeLoginReqDto) {
        Verify.validateString(employeeLoginReqDto.getUsername(), "??????????????? ???????????????.");
        Verify.validateString(employeeLoginReqDto.getPassword(), "????????? ???????????????.");
        UserVo userVoPS = employeeService.getEmployee(employeeLoginReqDto);
        session.setAttribute("principal", userVoPS);
        return "redirect:/";
    }

    @PostMapping("/employee/update")
    public String update(EmployeeUpdateReqDto employeeUpdateReqDto, MultipartFile profile) {
        UserVo principal = (UserVo) session.getAttribute("principal");
        Verify.validateObject(principal, "???????????? ???????????????.", HttpStatus.UNAUTHORIZED, "/employee/loginForm");
        if (!principal.getRole().equals("employee")) {
            throw new CustomException("????????? ????????????.", HttpStatus.FORBIDDEN);
        }
        Verify.validateString(employeeUpdateReqDto.getPassword(), "????????? ???????????????.");
        Verify.validateString(employeeUpdateReqDto.getEmail(), "???????????? ???????????????.");
        Verify.validateString(employeeUpdateReqDto.getAddress(), "????????? ???????????????.");
        Verify.validateString(employeeUpdateReqDto.getDetailAddress(), "?????? ????????? ???????????????.");
        Verify.validateString(employeeUpdateReqDto.getTel(), "??????????????? ???????????????.");
        Verify.validateObject(employeeUpdateReqDto.getCareer(), "????????? ???????????????.");
        Verify.validateString(employeeUpdateReqDto.getEducation(), "????????? ???????????????.");

        UserVo UserVoPS = employeeService.updateEmpolyee(employeeUpdateReqDto, principal.getId(), profile);
        session.removeAttribute("principal");
        session.setAttribute("principal", UserVoPS);
        return "redirect:/";
    }
}
