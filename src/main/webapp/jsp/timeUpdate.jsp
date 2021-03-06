<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%String root = request.getContextPath();%>
<head>
    <script src="<%=root %>/js/jquery.form.js" type="text/javascript"></script>
    <script src="<%=root %>/js/ajaxfileupload.js" type="text/javascript"></script>
</head>
<div class="pageContent">
    <form action="<%=root %>/harbour/time/doUpdate" method="post"
          onsubmit="return validateCallback(this,dialogAjaxDone);">
        <div class="pageFormContent" layoutH="56">
            <input name="id" type="hidden" class="required" maxlength="50" style="width:180px"
                   value="${simulationTime.id}"/>
            <p>
                <label>仿真时间总长</label>
                <input name="timeOut" type="text" class="required" maxlength="10"
                    value="${simulationTime.timeOut}"/>
                <select id="timeOutUnit" name="timeOutUnit" type="text" readonly="readonly" class="required"
                        maxlength="20">
                    <option value="YER" <c:if test="${simulationTime.timeOutUnit.name() eq 'YER'}">selected</c:if> >年</option>
                    <option value="MON" <c:if test="${simulationTime.timeOutUnit.name() eq 'MON'}">selected</c:if> >月</option>
                    <option value="WEK" <c:if test="${simulationTime.timeOutUnit.name() eq 'WEK'}">selected</c:if> >周</option>
                    <option value="DAY" <c:if test="${simulationTime.timeOutUnit.name() eq 'DAY'}">selected</c:if> >日</option>
                </select>
            </p>
            <div class="divider"/>
            <p>
                <label>仿真时间步长</label>
                <input name="timeStep" type="text" class="required" maxlength="10"
                       value="${simulationTime.timeStep}"/>
                <select id="timeStepUnit" name="timeStepUnit" type="text" readonly="readonly" class="required"
                        maxlength="20">
                    <option value="HOR" <c:if test="${simulationTime.timeStepUnit.unit eq '时'}">selected</c:if> >时</option>
                    <option value="MIN" <c:if test="${simulationTime.timeStepUnit.unit eq '分'}">selected</c:if> >分</option>
                </select>
            </p>
        </div>
        <div class="formBar">
            <ul>
                <li>
                    <div class="buttonActive">
                        <div class="buttonContent">
                            <button type="submit">保存</button>
                        </div>
                    </div>
                </li>
                <li>
                    <div class="button">
                        <div class="buttonContent">
                            <button type="button" class="close">取消</button>
                        </div>
                    </div>
                </li>
            </ul>
        </div>
    </form>
</div>