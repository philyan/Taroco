package cn.taroco.rbac.admin.service.impl;

import cn.taroco.common.constants.CommonConstant;
import cn.taroco.common.utils.PageQuery;
import cn.taroco.rbac.admin.mapper.SysUserRoleMapper;
import cn.taroco.rbac.admin.model.entity.SysUser;
import cn.taroco.rbac.admin.model.entity.SysUserRole;
import cn.taroco.rbac.admin.service.SysUserRoleService;
import cn.taroco.rbac.admin.service.SysUserService;
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
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户角色表 服务实现类
 * </p>
 *
 * @author liuht
 * @since 2017-10-29
 */
@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements SysUserRoleService {

    @Autowired
    private SysUserService sysUserService;

    @Override
    public Boolean deleteByUserId(Integer userId) {
        return baseMapper.deleteByUserId(userId);
    }

    @Override
    public Boolean insertRoleMembers(final Integer roleId, final Set<Integer> userIds) {
        if (roleId == null || CollectionUtils.isEmpty(userIds)) {
            return false;
        }
        final List<SysUserRole> results = new ArrayList<>(userIds.size());
        for (Integer userId : userIds) {
            final SysUserRole userRole = new SysUserRole();
            userRole.setRoleId(roleId);
            userRole.setUserId(userId);
            if (this.getOne(new QueryWrapper<>(userRole)) == null) {
                results.add(userRole);
            }
        }
        return this.saveBatch(results);
    }

    @Override
    public Boolean deleteRoleMembers(final Integer roleId, final Set<Integer> userIds) {
        if (roleId == null || CollectionUtils.isEmpty(userIds)) {
            return false;
        }
        for (Integer userId : userIds) {
            final SysUserRole userRole = new SysUserRole();
            userRole.setRoleId(roleId);
            userRole.setUserId(userId);
            if (this.getOne(new QueryWrapper<>(userRole)) != null) {
                this.deleteByUserId(userId);
            }
        }
        return true;
    }

    @Override
    public IPage<SysUser> roleMembersAdded(final PageQuery<SysUser> pageQuery, final Integer roleId, final Map<String, Object> params) {
        final Set<Integer> userIds = getUserIds(roleId);
        if (CollectionUtils.isEmpty(userIds)) {
            return new Page<>();
        }
        final SysUser sysUser = new SysUser();
        sysUser.setDelFlag(CommonConstant.STATUS_NORMAL);
        final LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>(sysUser);
        wrapper.in(SysUser::getUserId, userIds);
        final String username = "username";
        if (params.containsKey(username) && !StringUtils.isEmpty(params.get(username))) {
            wrapper.like(SysUser::getUsername, params.get(username));
        }
        return sysUserService.page(pageQuery, wrapper);
    }

    @Override
    public IPage<SysUser> roleMembersNotin(final PageQuery<SysUser> pageQuery, final Integer roleId, final Map<String, Object> params) {
        final Set<Integer> userIds = getUserIds(roleId);
        final SysUser sysUser = new SysUser();
        sysUser.setDelFlag(CommonConstant.STATUS_NORMAL);
        final LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>(sysUser);
        if (!CollectionUtils.isEmpty(userIds)) {
            wrapper.notIn(SysUser::getUserId, userIds);
        }
        final String username = "username";
        if (params.containsKey(username) && !StringUtils.isEmpty(params.get(username))) {
            wrapper.like(SysUser::getUsername, params.get(username));
        }
        return sysUserService.page(pageQuery, wrapper);
    }

    /**
     * 根据角色id 查询用户id
     *
     * @param roleId
     * @return
     */
    private Set<Integer> getUserIds(final Integer roleId) {
        final SysUserRole userRole = new SysUserRole();
        userRole.setRoleId(roleId);
        final List<SysUserRole> sysUserRoles = this.list(new QueryWrapper<>(userRole));
        if (CollectionUtils.isEmpty(sysUserRoles)) {
            return Collections.emptySet();
        }
        return sysUserRoles
                .stream()
                .map(SysUserRole::getUserId)
                .collect(Collectors.toSet());
    }
}
