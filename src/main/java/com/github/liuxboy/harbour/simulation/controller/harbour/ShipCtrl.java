package com.github.liuxboy.harbour.simulation.controller.harbour;

import com.github.liuxboy.harbour.simulation.common.util.AjaxResultUtil;
import com.github.liuxboy.harbour.simulation.domain.biz.Berth;
import com.github.liuxboy.harbour.simulation.domain.biz.Channel;
import com.github.liuxboy.harbour.simulation.domain.biz.Ship;
import com.github.liuxboy.harbour.simulation.service.InitialService;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Title: ShipCtrl</p>
 * <p>Copyright: Copyright(c)2016</p>
 * <p>Company: JD.JR </p>
 * <p>Time: 2016/6/16 13:46</p>
 * <p>Description: 船舶设置拦截器 </p>
 *
 * @author wyliuchundong
 * @version 1.0
 */
@Controller
@RequestMapping(value = "/ship")
public class ShipCtrl {
    @Resource
    HttpServletRequest httpServletRequest;
    @Resource
    HttpSession httpSession;
    @Resource
    InitialService initialService;

    @RequestMapping(value = "/toList")
    public String toList() {
        Object obj = httpSession.getAttribute("shipList");
        List<Ship> shipList = obj != null ? (List) obj : new ArrayList<Ship>();
        if (CollectionUtils.isEmpty(shipList)) {
            shipList = initialService.getShipList();
        }
        httpSession.setAttribute("shipList", shipList);
        return "/shipList";
    }

    @RequestMapping(value = "/toAdd")
    public String toAdd() {
        return "/harbour/shipAdd";
    }

    @RequestMapping(value = "/doAdd")
    @ResponseBody
    public String doAdd(@ModelAttribute("ship") Ship ship) {
        Object obj = httpSession.getAttribute("shipList");
        List<Ship> shipList = obj != null ? (List) obj : new ArrayList<Ship>();
        ship.setId(shipList.size());
        shipList.add(ship);
        httpSession.setAttribute("shipList", shipList);
        return AjaxResultUtil.success();
    }

    @RequestMapping(value = "/showDetail/{id}")
    public String showDetail(@PathVariable("id") int id) {
        Object obj = httpSession.getAttribute("shipList");
        List<Ship> shipList = obj != null ? (List) obj : new ArrayList<Ship>();
        Ship ship = new Ship();
        if (!CollectionUtils.isEmpty(shipList)) {
            ship = shipList.get(id);
        }
        httpServletRequest.setAttribute("ship", ship);
        return "/shipUpdate";
    }

    @RequestMapping(value = "/doUpdate")
    @ResponseBody
    public String doUpdate(@ModelAttribute("ship") Ship ship) {
        Object obj = httpSession.getAttribute("shipList");
        List<Ship> shipList = obj != null ? (List) obj : new ArrayList<Ship>();
        shipList.set(ship.getId(), ship);
        httpSession.setAttribute("shipList", shipList);
        return AjaxResultUtil.success();
    }

    @RequestMapping(value = "/delete/{id}")
    @ResponseBody
    public String delete(@PathVariable("id") int id) {
        Object obj = httpSession.getAttribute("shipList");
        List<Ship> shipList = obj != null ? (List) obj : new ArrayList<Ship>();
        if (!CollectionUtils.isEmpty(shipList)) {
            shipList.remove(id);
        }
        httpSession.setAttribute("shipList", shipList);
        return AjaxResultUtil.success();
    }
}
