<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="f" uri="http://www.springframework.org/tags/form" %>
<%String root = request.getContextPath();%>
<div class="pageContent">
    <form action="<%=root %>/harbour/anchorage/doAdd" method="post"
          onsubmit="return validateCallback(this , dialogAjaxDone);">
        <div class="pageFormContent" layoutH="56">
            <p>
                <label>锚地</label>
                <select id="anchorageEnum" name="anchorageEnum" type="text" readonly="readonly" class="required"
                        maxlength="20">
                    <option value="North">北锚地</option>
                    <option value="South">南锚地</option>
                    <option value="Ore">矿石锚地</option>
                    <option value="Oil">油轮锚地</option>
                </select>
            </p>
            <div class="divider"/>
            <p class="nowrap">
                <label>坐标1经纬度</label>
               <input name="point1X" type="text" class="required" maxlength="10"/>
               <input name="point1X" type="text" class="required" maxlength="10"/>
            </p>
            <div class="divider"/>
            <p class="nowrap">
                <label>坐标2经纬度</label>
                <input name="point2X" type="text" class="required" maxlength="10"/>
                <input name="point2Y" type="text" class="required" maxlength="10"/>
            </p>
            <div class="divider"/>
            <p class="nowrap">
                <label>坐标3经纬度</label>
                <input name="point3X" type="text" class="required" maxlength="10"/>
                <input name="point3Y" type="text" class="required" maxlength="10"/>
            </p>
            <div class="divider"/>
            <p class="nowrap">
                <label>坐标4经纬度</label>
                <input name="point4X" type="text" class="required" maxlength="10"/>
                <input name="point4Y" type="text" class="required" maxlength="10"/>
            </p>
            <div class="divider"/>
            <p class="nowrap">
                <label>坐标5经纬度</label>
                <input name="point5X" type="text" class="required" maxlength="10"/>
                <input name="point5Y" type="text" class="required" maxlength="10"/>
            </p>
            <div class="divider"/>
            <p class="nowrap">
                <label>坐标6经纬度</label>
                <input name="point6X" type="text" class="required" maxlength="10"/>
                <input name="point6Y" type="text" class="required" maxlength="10"/>
            </p>
            <div class="divider"/>
            <p class="nowrap">
                <label>锚位数量</label>
                <input name="size" type="text" class="required" maxlength="10"/>
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
