package cn.taroco.common.constants;

/**
 * Security 权限常量
 *
 * @author liuht
 */
public interface SecurityConstants {

    /**
     * basic
     */
    String BASIC = "basic";

    /**
     * basic
     */
    String BASIC_HEADER = "Basic ";

    /**
     * client_id
     */
    String CLIENT_ID = "client_id";

    /**
     * client_secret
     */
    String CLIENT_SECRET = "client_secret";

    /**
     * grant_type
     */
    String GRANT_TYPE = "grant_type";

    /**
     * AUTHORIZATION name
     */
    String AUTHORIZATION = "Authorization";

    /**
     * 用户信息头
     */
    String USER_HEADER = "x-user-name";

    /**
     * 角色信息头
     */
    String USER_ROLE_HEADER = "x-user-role";

    /**
     * 角色权限头
     */
    String USER_PERMISSION_HEADER = "x-user-permission";

    /**
     * 标签 header key
     */
    String HEADER_LABEL = "x-label";

    /**
     * 标签 header 分隔符
     */
    String HEADER_LABEL_SPLIT = ",";

    /**
     * 标签或 名称
     */
    String LABEL_OR = "labelOr";

    /**
     * 标签且 名称
     */
    String LABEL_AND = "labelAnd";

    /**
     * 权重key
     */
    String WEIGHT_KEY = "weight";

    /**
     * license key
     */
    String LICENSE_KEY = "license";

    /**
     * 项目的license
     */
    String LICENSE = "made by taroco";

    /**
     * 授权码模式
     */
    String AUTHORIZATION_CODE = "authorization_code";

    /**
     * 密码模式
     */
    String PASSWORD = "password";

    /**
     * 刷新token
     */
    String REFRESH_TOKEN = "refresh_token";

    /**
     * 传统oauth2 登录路径
     */
    String OAUTH_TOKEN_URL = "/oauth/token";

    /**
     * 手机号登录路径
     */
    String MOBILE_TOKEN_URL = "/oauth/mobile";

    /**
     * 默认的处理验证码的url前缀
     */
    String DEFAULT_VALIDATE_CODE_URL_PREFIX = "/code";

    /**
     * 默认生成图形验证码宽度
     */
    String DEFAULT_IMAGE_WIDTH = "100";

    /**
     * 默认生成图像验证码高度
     */
    String DEFAULT_IMAGE_HEIGHT = "40";

    /**
     * 默认生成图形验证码长度
     */
    String DEFAULT_IMAGE_LENGTH = "4";

    /**
     * 默认生成图形验证码过期时间
     */
    int DEFAULT_IMAGE_EXPIRE = 60;

    /**
     * 边框颜色，合法值： r,g,b (and optional alpha) 或者 white,black,blue.
     */
    String DEFAULT_COLOR_FONT = "blue";

    /**
     * 图片边框
     */
    String DEFAULT_IMAGE_BORDER = "no";

    /**
     * 默认图片间隔
     */
    String DEFAULT_CHAR_SPACE = "5";

    /**
     * 验证码文字大小
     */
    String DEFAULT_IMAGE_FONT_SIZE = "30";

    /**
     * sys_oauth_client_details 表的字段，不包括client_id、client_secret
     */
    String CLIENT_FIELDS = "client_id, client_secret, resource_ids, scope, "
            + "authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, "
            + "refresh_token_validity, additional_information, autoapprove";

    /**
     * JdbcClientDetailsService 查询语句
     */
    String BASE_FIND_STATEMENT = "select " + CLIENT_FIELDS
            + " from sys_oauth_client_details";

    /**
     * 默认的查询语句
     */
    String DEFAULT_FIND_STATEMENT = BASE_FIND_STATEMENT + " order by client_id";

    /**
     * 按条件client_id 查询
     */
    String DEFAULT_SELECT_STATEMENT = BASE_FIND_STATEMENT + " where client_id = ?";

}
