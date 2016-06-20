<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%String root = request.getContextPath();%>
<head>
    <script src="<%=root %>/js/jquery.form.js" type="text/javascript"></script>
    <script src="<%=root %>/js/ajaxfileupload.js" type="text/javascript"></script>
</head>
<div class="pageContent">
   <form action="<%=root %>/harbour/channel/doUpdate" method="post" onsubmit="return validateCallback(this,dialogAjaxDone);">
        <div class="pageFormContent" layoutH="56">
            <p>
                <label>编号:</label>
                <input name="id" type="hidden" class="required" maxlength="50"
                       value="${channel.id}"/>
            </p>
            <p>
                <label>宽度:</label>
                <input name="followingNo" type="text" class="required" maxlength="20"
                       value="${channel.width}"/>
            </p>
            <p>
                <label>长度:</label>
                <input name="fansNo" type="text" class="required" maxlength="20"
                       value="${channel.length}"/>
            </p>
            <p>
                <label>深度:</label>
                <input name="subscribedFansNo" type="text" class="required" maxlength="20"
                       value="${channel.depth}"/>
            </p>
            <p>
                <label>限速</label>
                <input name="subscribeNo" type="text" class="required" maxlength="20"
                       value="${channel.limitedSpeed}"/>
            </p>
            <p>
                <label>通航模式</label>
                <select id="passEnum" name="passEnum" type="text" readonly="readonly" class="required"
                        maxlength="20">
                    <option value="OneWay" <c:if test="${channel.passEnum.typeCode == 0}">selected</c:if>>单向</option>
                    <option value="BothWay" <c:if test="${channel.passEnum.typeCode == 1}">selected</c:if>>双向</option>
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