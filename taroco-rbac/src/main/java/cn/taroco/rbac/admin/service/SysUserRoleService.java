package cn.taroco.rbac.admin.service;


import cn.taroco.common.utils.PageQuery;
import cn.taroco.rbac.admin.model.entity.SysUser;
import cn.taroco.rbac.admin.model.entity.SysUserRole;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 用户角色表 服务类
 * </p>
 *
 * @author liuht
 * @since 2017-10-29
 */
public interface SysUserRoleService extends IService<SysUserRole> {

    /**
     * 根据用户Id删除该用户的角色关系
     *
     * @param userId 用户ID
     * @return boolean
     */
    Boolean deleteByUserId(Integer userId);

    /**
     * 设置角色成员
     *
     * @param roleId
     * @param userIds
     * @return
     */
    Boolean insertRoleMembers(Integer roleId, Set<Integer> userIds);

    /**
     * 删除角色成员
     *
     * @param roleId
     * @param userIds
     * @return
     */
    Boolean deleteRoleMembers(Integer roleId, Set<Integer> userIds);

    /**
     * 查询已添加用户
     *
     * @param pageQuery
     * @param roleId
     * @param params
     * @return
     */
    IPage<SysUser> roleMembersAdded(PageQuery<SysUser> pageQuery, Integer roleId, Map<String, Object> params);

    /**
     * 查询未添加用户
     *
     * @param pageQuery
     * @param roleId
     * @param params
     * @return
     */
    IPage<SysUser> roleMembersNotin(PageQuery<SysUser> pageQuery, Integer roleId, Map<String, Object> params);
}
