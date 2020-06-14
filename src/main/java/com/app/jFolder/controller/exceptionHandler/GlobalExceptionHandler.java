package com.app.jFolder.controller.exceptionHandler;

import com.app.jFolder.exception.fileVersion.FileVersionExistException;
import com.app.jFolder.exception.fileVersion.FileVersionExtensionException;
import com.app.jFolder.exception.user.UserEmailAlreadyExistsException;
import com.app.jFolder.exception.user.UserNotAcceptException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(FileVersionExtensionException.class)
    public String fileVersionExtensionException(Model model
            , FileVersionExtensionException exc) {
        model.addAttribute("excpetionName", "Error file version extension");
        model.addAttribute("description", exc.getMessage());
        return "exceptionPage";
    }

    @ExceptionHandler(FileVersionExistException.class)
    public String fileVersionExistException(Model model
            , FileVersionExistException exc) {
        model.addAttribute("excpetionName", "File version already exist");
        model.addAttribute("description", exc.getMessage());
        return "exceptionPage";
    }

    @ExceptionHandler(UserNotAcceptException.class)
    public String userNotAcceptException(Model model
            , UserNotAcceptException exc) {
        model.addAttribute("excpetionName", "Your email wasn't accept");
        model.addAttribute("description", exc.getMessage());
        return "exceptionPage";
    }

    @ExceptionHandler(UserEmailAlreadyExistsException.class)
    public String userEmailAlreadyExistsException(Model model
            , UserEmailAlreadyExistsException exc) {
        model.addAttribute("excpetionName", "Email error");
        model.addAttribute("description", exc.getMessage());
        return "exceptionPage";
    }
}
