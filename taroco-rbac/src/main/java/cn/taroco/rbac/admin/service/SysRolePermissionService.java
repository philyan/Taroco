package cn.taroco.rbac.admin.service;

import cn.taroco.common.utils.PageQuery;
import cn.taroco.rbac.admin.model.entity.SysPermission;
import cn.taroco.rbac.admin.model.entity.SysRolePermission;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * 角色权限
 *
 * @author liuht
 * 2019/2/19 17:10
 */
public interface SysRolePermissionService extends IService<SysRolePermission> {

    /**
     * 新增角色权限
     *
     * @param roleId        角色ID
     * @param permissionIds 权限ID列表
     * @return
     */
    Boolean insertRolePermissions(Integer roleId, Collection<Integer> permissionIds);

    /**
     * 删除角色权限
     *
     * @param roleId
     * @param permissionIds
     * @return
     */
    Boolean deleteRolePermissions(Integer roleId, Set<Integer> permissionIds);

    /**
     * 查询角色权限
     *
     * @param roleId
     * @return
     */
    Set<String> getRolePermissions(Integer roleId);

    /**
     * 查询角色权限
     *
     * @param roleIds 角色ID
     * @return
     */
    Set<String> getRolePermissions(Set<Integer> roleIds);

    /**
     * 查询已添加权限
     *
     * @param pageQuery
     * @param roleId
     * @param params
     * @return
     */
    IPage<SysPermission> rolePermissionsAdded(PageQuery<SysPermission> pageQuery, Integer roleId, Map<String, Object> params);

    /**
     * 查询未添加权限
     *
     * @param pageQuery
     * @param roleId
     * @param params
     * @return
     */
    IPage<SysPermission> rolePermissionsNotin(PageQuery<SysPermission> pageQuery, Integer roleId, Map<String, Object> params);
}
