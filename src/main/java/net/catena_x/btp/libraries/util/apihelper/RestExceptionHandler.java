package net.catena_x.btp.libraries.util.apihelper;

import net.catena_x.btp.libraries.util.apihelper.preparation.ApiResult;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.validation.constraints.NotNull;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(RestExceptionHandler.class);

    @Override protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        logger.info("HttpRequestMethodNotSupported: " + ex.getMethod() + " " + ex.getMessage());

        super.handleHttpRequestMethodNotSupported(ex, headers, status, request);
        return handle(headers, status, request, "Http request method not supported!");
    }

    @Override protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
            HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        logger.info("HttpMediaTypeNotSupported: " + ex.getContentType() + " " + ex.getMessage());

        super.handleHttpMediaTypeNotSupported(ex, headers, status, request);
        return handle(headers, status, request, "Http media type not supported!");
    }

    @Override protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(
            HttpMediaTypeNotAcceptableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        logger.info("HttpMediaTypeNotAcceptable: " + ex.getMessage());
        return handle(headers, status, request, "Http media type not acceptable!");
    }

    @Override protected ResponseEntity<Object> handleMissingPathVariable(
            MissingPathVariableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return handle(headers, status, request, "Missing path variable!");
    }

    @Override protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return handle(headers, status, request, "Missing servlet request parameter!");
    }

    @Override protected ResponseEntity<Object> handleServletRequestBindingException(
            ServletRequestBindingException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return handle(headers, status, request, "Servlet request binding error!");
    }

    @Override protected ResponseEntity<Object> handleConversionNotSupported(
            ConversionNotSupportedException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        logger.info("ConversionNotSupported: " + ex.getMessage());

        return handle(headers, status, request, "Conversion not supported!");
    }

    @Override protected ResponseEntity<Object> handleTypeMismatch(
            TypeMismatchException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return handle(headers, status, request, "Type missmatch!");
    }

    @Override protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        final ContentCachingRequestWrapper nativeRequest =
                (ContentCachingRequestWrapper)((ServletWebRequest)request).getNativeRequest();
        final String requestEntityAsString = new String(nativeRequest.getContentAsByteArray());

        logger.info("HttpMessageNotReadable: " + ex.getHttpInputMessage() + " " + ex.getMessage() + " \nBody: \n"
                + requestEntityAsString);

        return handle(headers, status, request, "Http message not readable!");
    }

    @Override protected ResponseEntity<Object> handleHttpMessageNotWritable(
            HttpMessageNotWritableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return handle(headers, status, request, "Http message not writable!");
    }

    @Override protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return handle(headers, status, request, "Method argument not valid!");
    }

    @Override protected ResponseEntity<Object> handleMissingServletRequestPart(
            MissingServletRequestPartException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return handle(headers, status, request, "Missing servlet request part!");
    }

    @Override protected ResponseEntity<Object> handleBindException(
            BindException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return handle(headers, status, request, "Bind error!");
    }

    @Override protected ResponseEntity<Object> handleNoHandlerFoundException(
            NoHandlerFoundException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return handle(headers, status, request, "No handler found!");
    }

    @Override @Nullable protected ResponseEntity<Object> handleAsyncRequestTimeoutException(
            AsyncRequestTimeoutException ex, HttpHeaders headers, HttpStatusCode status, WebRequest webRequest) {

        final ResponseEntity<Object> response = super.handleAsyncRequestTimeoutException(
                                                            ex, headers, status, webRequest);

        if(response == null){
            return null;
        }

        return handle(headers, status, webRequest, "Async request timed out!");
    }

    protected ResponseEntity<Object> handle(@NotNull final HttpHeaders headers,
                                            @NotNull final HttpStatusCode status,
                                            @NotNull WebRequest request,
                                            @NotNull final String error) {
        return handleExceptionInternal(null, ApiResult.failed(error), headers, status, request);
    }
}