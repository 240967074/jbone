package com.majunwei.jbone.sys.server.api.impl;

import com.majunwei.jbone.sys.api.UserApi;
import com.majunwei.jbone.sys.api.model.UserInfoModel;
import com.majunwei.jbone.sys.api.model.UserModel;
import com.majunwei.jbone.sys.dao.domain.RbacPermissionEntity;
import com.majunwei.jbone.sys.dao.domain.RbacRoleEntity;
import com.majunwei.jbone.sys.dao.domain.RbacUserEntity;
import com.majunwei.jbone.sys.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class UserApiImpl implements UserApi {

    private Logger logger = LoggerFactory.getLogger(UserApiImpl.class);

    @Autowired
    private UserService userService;

    @RequestMapping("/getUserByName")
    public UserInfoModel getUserByName(String username) {
        RbacUserEntity userEntity = userService.findByUserName(username);
        UserInfoModel userInfoModel = new UserInfoModel();
        BeanUtils.copyProperties(userEntity,userInfoModel);
        logger.info("username : {}" , username);
        return userInfoModel;
    }
    @RequestMapping("/getUserDetail")
    public UserModel getUserDetailByName(String username){
        RbacUserEntity userEntity = userService.findByUserName(username);
        UserModel userModel = new UserModel();
        List<String> permissions = new ArrayList<String>();
        List<String> roles = new ArrayList<String>();
        List<RbacPermissionEntity> permissionEntities =  userEntity.getPermissions();
        List<RbacRoleEntity> roleEntities = userEntity.getRoles();
        if(permissionEntities != null && !permissionEntities.isEmpty()){
            for(RbacPermissionEntity permissionEntity : permissionEntities){
                permissions.add(permissionEntity.getPermissionValue());
            }
        }

        if(roleEntities != null && !roleEntities.isEmpty()){
            for(RbacRoleEntity roleEntity : roleEntities){
                roles.add(roleEntity.getName());

                List<RbacPermissionEntity> rolePermissions =  userEntity.getPermissions();

                if(rolePermissions != null && !roleEntities.isEmpty()){
                    for(RbacPermissionEntity permissionEntity : rolePermissions){
                        permissions.add(permissionEntity.getPermissionValue());
                    }
                }
            }
        }

        userModel.setRealname(userEntity.getRealname());
        userModel.setPermissions(permissions);
        userModel.setRoles(roles);
        userModel.setUsername(userEntity.getUsername());

        return userModel;
    }
}
