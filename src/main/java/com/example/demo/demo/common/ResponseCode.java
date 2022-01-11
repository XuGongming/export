package com.example.demo.demo.common;

/**
 * @author xugm
 * @create 2022/1/11 9:30
 */
public enum ResponseCode {
    SUCCESS("0", "ok"),
    SYSTEM_ERROR("10001", "系统出现异常，请稍后重试。"),
    API_PERMISSION_DENIED("10002", "对不起，您没有权限访问，请联系管理员。"),
    API_UN_SUPPORT_MEDIA_TYPE("10003", "不支持的MediaType (%s)"),
    API_PARAM_ERROR("10004", "参数值格式不正确"),
    API_PARAM_TYPE_ERROR("10005", "参数%s只能是%s类型数据"),
    API_BODY_IS_EMPTY("10006", "请求体不能为空"),
    API_UN_SUPPORT_METHOD("10007", "不支持%s请求方式"),
    API_NOT_EXISTS("10008", "请请求资源[%s] - [%s]不存在"),
    API_PARAM_IS_EMPTY("10009", "参数%s不能为空"),
    API_CALL_FAILED("10011", "第三方接口调用失败"),
    USER_ERROR("20001", "用户名或密码不正确"),
    USER_NOT_EXISTS("20002", "用户不存在"),
    USER_DISABLED("20003", "账号目前为禁用状态，无法登录"),
    USER_TOKEN_EXPIRED("20004", "登录已经失效，请重新登录。"),
    USER_TOKEN_INVALID("20005", "登录信息非法，请重新登录。"),
    USER_NOT_LOGIN("20006", "您尚未登录，请先登录。"),
    USER_NO_PERMISSION("20007", "您没有操作权限，请联系管理员。"),
    USER_LOGIN_NAME_EMPTY("20008", "用户登录名不能为空"),
    USER_LOGIN_NAME_INVALID("20009", "用户登录名不正确，%s"),
    USER_LOGIN_NAME_EXISTS("20010", "用户登录名已经存在"),
    USER_LOGIN_PASSWORD_EMPTY("20011", "用户密码不能为空"),
    USER_LOGIN_PASSWORD_INVALID("20012", "用户密码不正确"),
    USER_LOCKED("20013", "密码输入错误%s次，账号将冻结%s小时！"),
    USER_LOGIN_CODE_ERROR("20014", "请输入正确的验证码"),
    USER_DEFAULT_PASSWORD("20015", "请修改默认密码"),
    USER_LOGIN_PASSWORD_ERROR("20016", "密码输入错误%s次，输入密码错误%s次账号将被冻结%s小时！"),
    USER_MOBILE_NOT_EXISTS("20017", "手机号不存在"),
    USER_MOBILE_INVALID("20018", "请输入正确的手机号"),
    USER_LOGIN_PASSWORD_INCONSISTENT("20019", "两次输入的密码不一致"),
    USER_LOGIN_PASSWORD_FORMAT_INVALID("20020", "请设置符合规则的密码"),
    USER_NAME_OR_MOBILE_NOT_EXIST("20021", "用户名或手机号不存在"),
    USER_INCOMPLETE("20022", "账号信息不完整，无法登录，请联络管理员"),
    USER_WAIT_FOR_VERIFICATION("20023", "待验证"),
    DATA_FIELD_IS_NULL("30001", "%s不能为空"),
    DATA_IS_EXISTS("30002", "%s已经存在"),
    DATA_NOT_EXISTS("30003", "%s不存在"),
    DATA_RELATION_CAN_NOT_DELETE("30004", "%s有%s，不能删除"),
    DATA_NAME_IS_EXISTS("30005", "%s名称重复"),
    DATA_CAN_NOT_COMMIT("30006", "数据不能提交"),
    REDIS_SERVICE_IS_DOWN("40001", "缓存服务器不可用。"),
    REDIS_SERVICE_TIMEOUT("40002", "缓存服务器操作超时。"),
    SERVICE_IS_DOWN("50001", "%s服务不可用");

    private String code;
    private String message;

    private ResponseCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }
}
