package org.cube.commons.exception;

import cn.dev33.satoken.exception.DisableServiceException;
import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.NotRoleException;
import org.cube.commons.constant.CommonConst;
import org.cube.commons.base.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.connection.PoolException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.List;

/**
 * 异常处理器 *
 *
 * @author xinwuy
 * @version V2.3.3
 * @since 2019-12-12
 */
@Slf4j
@RestControllerAdvice
public class CubeAppExceptionHandler {

    /**
     * 处理自定义异常
     */
    @ExceptionHandler(CubeAppException.class)
    public Result<?> handleRRException(CubeAppException e) {
        return Result.error(e.getMessage());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public Result<?> handlerNoFoundException(NoHandlerFoundException e) {
        return Result.error(HttpStatus.NOT_FOUND.value(), "路径不存在，请检查路径是否正确！");
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public Result<?> handleDuplicateKeyException(DuplicateKeyException e) {
        return Result.error("数据库中已存在该记录！");
    }

    @ExceptionHandler(Exception.class)
    public Result<?> handleException(Exception e) {
        log.error("程序异常：", e);
        String message = e.getMessage();
        if (e instanceof UndeclaredThrowableException) {
            message = ((UndeclaredThrowableException) e).getUndeclaredThrowable().getMessage();
        }
        return Result.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "操作失败：" + message, e.getStackTrace()[0]);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Result<?> HttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        StringBuilder sb = new StringBuilder();
        sb.append("不支持[");
        sb.append(e.getMethod());
        sb.append("[请求方法，");
        sb.append("支持以下：[");
        String[] methods = e.getSupportedMethods();
        if (methods != null) {
            for (int i = 0; i < methods.length; i++) {
                if (i == methods.length - 1) {
                    sb.append(methods[i]);
                    sb.append("]");
                } else {
                    sb.append(methods[i]);
                    sb.append("、");
                }
            }
        }
        return Result.error(HttpStatus.METHOD_NOT_ALLOWED.value(), sb.toString());
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public Result<?> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e) {
        return Result.error("文件大小超出[" + e.getMaxUploadSize() + "]限制，请压缩或降低文件质量! ");
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public Result<?> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        log.error("插入或更新数据违反完整性约束：", e);
        return Result.error("插入或更新数据违反完整性约束！");
    }

    @ExceptionHandler(PoolException.class)
    public Result<?> handlePoolException(PoolException e) {
        return Result.error("Redis连接异常！");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<String> errorMsg = new ArrayList<>();
        List<ObjectError> errorList = e.getBindingResult().getAllErrors();
        for (ObjectError error : errorList) {
            errorMsg.add(error.getDefaultMessage());
        }
        return Result.error(HttpStatus.BAD_REQUEST.value(), "参数有误！", errorMsg);
    }

    @ExceptionHandler(NotPermissionException.class)
    public Result<?> handleException(NotPermissionException e) {
        return Result.error(HttpStatus.UNAUTHORIZED.value(), "无此权限：" + e.getPermission() + "！");
    }

    @ExceptionHandler(NotLoginException.class)
    public Result<?> handleException(NotLoginException e) {
        return Result.error(CommonConst.NOT_LOGIN, e.getMessage() + "！");
    }

    @ExceptionHandler(NotRoleException.class)
    public Result<?> handleException(NotRoleException e) {
        return Result.error("无此角色：" + e.getRole() + "！");
    }

    @ExceptionHandler(DisableServiceException.class)
    public Result<?> handleException(DisableServiceException e) {
        return Result.error("账号被封禁：" + e.getDisableTime() + "秒后解封！");
    }
}
