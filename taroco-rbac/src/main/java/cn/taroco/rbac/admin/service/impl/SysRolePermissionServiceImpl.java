package cn.taroco.rbac.admin.service.impl;

import cn.taroco.common.constants.CommonConstant;
import cn.taroco.common.utils.PageQuery;
import cn.taroco.rbac.admin.mapper.SysRolePermissionMapper;
import cn.taroco.rbac.admin.model.entity.SysPermission;
import cn.taroco.rbac.admin.model.entity.SysRolePermission;
import cn.taroco.rbac.admin.service.SysPermissionService;
import cn.taroco.rbac.admin.service.SysRolePermissionService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 角色权限 Service
 *
 * @author liuht
 * 2019/2/19 17:12
 */
@Service
public class SysRolePermissionServiceImpl
        extends ServiceImpl<SysRolePermissionMapper, SysRolePermission>
        implements SysRolePermissionService {

    @Autowired
    private SysRolePermissionMapper sysRolePermissionMapper;

    @Autowired
    private SysPermissionService sysPermissionService;

    @Override
    public Boolean insertRolePermissions(final Integer roleId, final Collection<Integer> permissionIds) {
        if (roleId == null || CollectionUtils.isEmpty(permissionIds)) {
            return false;
        }
        List<SysRolePermission> rolePermissions = new ArrayList<>(permissionIds.size());
        permissionIds.forEach(permissionId -> {
            final SysRolePermission permission = new SysRolePermission();
            permission.setRoleId(roleId);
            permission.setPermissionId(permissionId);
            if (this.getOne(new QueryWrapper<>(permission)) == null) {
                rolePermissions.add(permission);
            }
        });
        return this.saveBatch(rolePermissions);
    }

    @Override
    public Boolean deleteRolePermissions(final Integer roleId, final Set<Integer> permissionIds) {
        if (roleId == null || CollectionUtils.isEmpty(permissionIds)) {
            return false;
        }
        List<Integer> deleteIds = new ArrayList<>(permissionIds.size());
        permissionIds.forEach(permissionId -> {
            final SysRolePermission permission = new SysRolePermission();
            permission.setRoleId(roleId);
            permission.setPermissionId(permissionId);
            final SysRolePermission one = this.getOne(new QueryWrapper<>(permission));
            if (one != null) {
                deleteIds.add(one.getId());
            }
        });
        return this.removeByIds(deleteIds);
    }

    @Override
    public Set<String> getRolePermissions(final Integer roleId) {
        return sysRolePermissionMapper.getRolePermissions(roleId);
    }

    @Override
    public Set<String> getRolePermissions(final Set<Integer> roleIds) {
        if (CollectionUtils.isEmpty(roleIds)) {
            return Collections.emptySet();
        }
        return sysRolePermissionMapper.getRolePermissionsBatch(roleIds);
    }

    @Override
    public IPage<SysPermission> rolePermissionsAdded(final PageQuery<SysPermission> pageQuery, final Integer roleId, final Map<String, Object> params) {
        final Set<Integer> permissionIds = getPermissionIds(roleId);
        if (CollectionUtils.isEmpty(permissionIds)) {
            return new Page<>();
        }
        final SysPermission sysPermission = new SysPermission();
        sysPermission.setDelFlag(CommonConstant.STATUS_NORMAL);
        final LambdaQueryWrapper<SysPermission> wrapper = new LambdaQueryWrapper<>(sysPermission);
        wrapper.in(SysPermission::getId, permissionIds);
        final String perName = "perName";
        if (params.containsKey(perName) && !StringUtils.isEmpty(params.get(perName))) {
            wrapper.like(SysPermission::getName, params.get(perName));
        }
        return sysPermissionService.page(pageQuery, wrapper);
    }

    @Override
    public IPage<SysPermission> rolePermissionsNotin(final PageQuery<SysPermission> pageQuery, final Integer roleId, final Map<String, Object> params) {
        final Set<Integer> permissionIds = getPermissionIds(roleId);
        final SysPermission sysPermission = new SysPermission();
        sysPermission.setDelFlag(CommonConstant.STATUS_NORMAL);
        final LambdaQueryWrapper<SysPermission> wrapper = new LambdaQueryWrapper<>(sysPermission);
        if (!CollectionUtils.isEmpty(permissionIds)) {
            wrapper.notIn(SysPermission::getId, permissionIds);
        }
        final String perName = "perName";
        if (params.containsKey(perName) && !StringUtils.isEmpty(params.get(perName))) {
            wrapper.like(SysPermission::getName, params.get(perName));
        }
        return sysPermissionService.page(pageQuery, wrapper);
    }

    /**
     * 根据角色id 查询权限id
     *
     * @param roleId
     * @return
     */
    private Set<Integer> getPermissionIds(final Integer roleId) {
        final SysRolePermission rolePermission = new SysRolePermission();
        rolePermission.setRoleId(roleId);
        final List<SysRolePermission> rolePermissions = this.list(new QueryWrapper<>(rolePermission));
        if (CollectionUtils.isEmpty(rolePermissions)) {
            return Collections.emptySet();
        }
        return rolePermissions
                .stream()
                .map(SysRolePermission::getPermissionId)
                .collect(Collectors.toSet());
    }
}
