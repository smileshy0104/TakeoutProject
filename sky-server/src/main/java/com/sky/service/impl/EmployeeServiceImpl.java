package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.PasswordConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.result.PageResult;
import com.sky.service.EmployeeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    @Override
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        //1、根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        // 密码比对
        // 对前端传来的明文密码进行md5加密，然后再进行比对
        // MD5加密
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!password.equals(employee.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (employee.getStatus() == StatusConstant.DISABLE) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        //3、返回实体对象
        return employee;
    }


    /**
     * 新增员工
     *
     * @param employeeDTO
     */
    @Override
    public void save(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();

        //对象属性拷贝
        BeanUtils.copyProperties(employeeDTO, employee);

        //设置账号的状态，默认正常状态 1表示正常 0表示锁定
        employee.setStatus(StatusConstant.ENABLE);

        //设置密码，默认密码123456
        employee.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes()));

        //设置当前记录的创建时间和修改时间
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());

        //设置当前记录创建人id和修改人id
        employee.setCreateUser(10L);//目前写个假数据，后期修改
        employee.setUpdateUser(10L);

        employeeMapper.insert(employee);//后续步骤定义
    }

    /**
     * 分页查询
     *
     * @param employeePageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO) {
        // select * from employee limit 0,10
        //开始分页查询
        PageHelper.startPage(employeePageQueryDTO.getPage(), employeePageQueryDTO.getPageSize());

        Page<Employee> page = employeeMapper.pageQuery(employeePageQueryDTO);//后续定义

        long total = page.getTotal();
        List<Employee> records = page.getResult();

        return new PageResult(total, records);
    }


    /**
     * 根据员工ID和状态更新员工账户的启用或禁用状态。
     * 此方法通过构建一个新的Employee对象来更新数据库中相应员工的状态。
     * 状态值通常为0（禁用）或1（启用），但此方法的具体实现细节留给了调用者。
     *
     * @param status 员工的新状态，通常为0（禁用）或1（启用）。
     * @param id 要更新状态的员工的唯一标识符。
     */
    public void startOrStop(Integer status, Long id) {
        // 根据传入的状态和ID构建一个新的Employee对象
        // 创建一个Employee对象，设置id和status属性
        // builder模式_使用构造器
        Employee employee = Employee.builder()
                .status(status)
                .id(id)
                .build();

        // 调用employeeMapper的update方法，更新数据库中相应员工的状态
        employeeMapper.update(employee);
    }


/**
 * 更新员工信息。
 * 该方法通过接收一个EmployeeDTO对象，将其属性值复制到一个新的Employee对象中，
 * 并更新员工的最后修改时间和修改人信息，最后通过employeeMapper更新数据库中的员工记录。
 *
 * @param employeeDTO 包含待更新员工信息的数据传输对象。
 * @return 本方法无返回值。
 */
    /**
     * 编辑员工信息
     * @param employeeDTO
     * @return
     */
    public void Update(EmployeeDTO employeeDTO){
        // 创建一个新的Employee对象，用于存储待更新的数据。
        Employee employee = new Employee();
        // 使用BeanUtils.copyProperties方法，将employeeDTO的属性值复制到employee对象中。
        // 对象属性拷贝
        BeanUtils.copyProperties(employeeDTO,employee);
        // 设置员工的最后修改时间为当前时间。
        employee.setUpdateTime(LocalDateTime.now());
        // 设置员工的修改人为当前操作用户，通过BaseContext获取当前用户的ID。
        // 设置修改人id，通过获取上下文的id
        employee.setUpdateUser(BaseContext.getCurrentId());
        // 调用employeeMapper的update方法，更新数据库中的员工记录。
        employeeMapper.update(employee);
    }


    /**
     * 根据id查询员工
     * @param id
     * @return
     */
    public Employee getById(Long id){
        return employeeMapper.getById(id);
    }
}
