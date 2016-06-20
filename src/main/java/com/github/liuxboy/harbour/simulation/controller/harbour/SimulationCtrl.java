package com.github.liuxboy.harbour.simulation.controller.harbour;

import com.github.liuxboy.harbour.simulation.common.util.Logger;
import com.github.liuxboy.harbour.simulation.common.util.LoggerFactory;
import com.github.liuxboy.harbour.simulation.domain.biz.Result;
import com.github.liuxboy.harbour.simulation.service.HarbourSimulationService;
import org.apache.avalon.framework.service.ServiceException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * <p>Title: SimulationCtrl</p>
 * <p>Copyright: Copyright(c)2016</p>
 * <p>Company: JD.JR </p>
 * <p>Time: 2016/6/17 14:52</p>
 * <p>Description: 描述 </p>
 *
 * @author wyliuchundong
 * @version 1.0
 */
@Controller
@RequestMapping(value = "/simulation")
public class SimulationCtrl {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Resource
    HttpServletRequest httpServletRequest;
    @Resource
    HttpSession httpSession;
    @Resource
    private HarbourSimulationService harbourSimulationService;

    @RequestMapping(value = "/toList")
    public String toList() {
        httpServletRequest.setAttribute("shipList", httpSession.getAttribute("shipList"));
        return "/harbour/simulation";
    }

    @RequestMapping(value = "/start")
    @ResponseBody
    public String start() {
        try {
            List<Result> resultList = harbourSimulationService.simulation();
            httpServletRequest.setAttribute("resultList", resultList);
            httpServletRequest.setAttribute("flag", 1);
            logger.info("resultList:", resultList);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return "/harbour/simulation";
    }
}