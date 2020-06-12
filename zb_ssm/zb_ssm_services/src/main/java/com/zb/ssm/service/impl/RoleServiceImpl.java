package com.zb.ssm.service.impl;

import com.zb.ssm.dao.IRoleDao;
import com.zb.ssm.domain.Permission;
import com.zb.ssm.domain.Role;
import com.zb.ssm.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@Transactional
public class RoleServiceImpl implements IRoleService {
    @Autowired
    private IRoleDao roleDao;
    @Override
    public List<Role> findAll() throws Exception {
        return roleDao.findAll();
    }

    @Override
    public void save(Role role) throws Exception {
        roleDao.save(role);
    }

    @Override
    public Role findById(String roleId) throws Exception {
        return roleDao.findById(roleId);
    }

    @Override
    public List<Permission> findRoleByIdAndAllPermission(String roleId) throws Exception {
        return roleDao.findRoleByIdAndAllPermission(roleId);
    }

    @Override
    public void addPermissionToRole(String roleId, String[] ids) throws Exception {
        for(String permissionId:ids){
            roleDao.addPermissionToRole(roleId,permissionId);
        }
    }


}
