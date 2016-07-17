package com.github.liuxboy.harbour.simulation.service.impl;

import com.github.liuxboy.harbour.simulation.common.constant.BigDecimalUtil;
import com.github.liuxboy.harbour.simulation.common.constant.PriorityEnum;
import com.github.liuxboy.harbour.simulation.common.constant.ShipEnum;
import com.github.liuxboy.harbour.simulation.common.constant.TimeEnum;
import com.github.liuxboy.harbour.simulation.common.util.AlgorithmUtil;
import com.github.liuxboy.harbour.simulation.common.util.Logger;
import com.github.liuxboy.harbour.simulation.common.util.LoggerFactory;
import com.github.liuxboy.harbour.simulation.domain.biz.*;
import com.github.liuxboy.harbour.simulation.service.HarbourSimulationService;
import org.apache.avalon.framework.service.ServiceException;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;


/**
 * <p>Title: HarbourSimulationServiceImpl</p>
 * <p>Copyright: Copyright(c)2016</p>
 * <p>Company: JD.JR </p>
 * <p>Time: 2016/6/2 15:01</p>
 * <p>Description: 仿真实现类 </p>
 *
 * @author wyliuchundong
 * @version 1.0
 */
@Service
public class HarbourSimulationServiceImpl implements HarbourSimulationService {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public List<Result> simulation(List<Anchorage> anchorageList, List<Channel> channelList, List<Berth> berthList,
                                   List<Ship> shipTypeList, List<Traffic> trafficList, List<SimulationTime> timeList) throws ServiceException {
        List<Result> resultList = new ArrayList<Result>();
        Map<Integer, Ship> inShipMap = new HashMap<Integer, Ship>(); //进泊船舶，就是正式进入航道与到达泊位之间，所有的船舶
        Map<Integer, Ship> outShipMap = new HashMap<Integer, Ship>();//出泊船舶，就是正式驶离泊位与驶出航道之间，所有的船舶
        SimulationTime simulationTime = new SimulationTime();
        if (!CollectionUtils.isEmpty(timeList))
            simulationTime = timeList.get(0);
        int simulationSteps = (simulationTime.getTimeOut() * simulationTime.getTimeOutUnit().getTime())
                / (simulationTime.getTimeStep() * simulationTime.getTimeStepUnit().getTime());
        int simulationDays = simulationTime.getTimeOut() * simulationTime.getTimeOutUnit().getTime()
                / TimeEnum.DAY.getTime();

        //存放当前仿真过程中的船只
        Map<String, Result> resultMap = new HashMap<String, Result>();
        //存放结果
        Result totalResult = new Result(null),                              //总的结果
                ironOreResult = new Result(ShipEnum.Iron_Ore),              //铁矿石结果
                chemicalOilResult = new Result(ShipEnum.Chemical_Oil),      //化学油品结果
                crudeOilResult = new Result(ShipEnum.Crude_Oil),            //原油结果
                coalResult = new Result(ShipEnum.Coal),                     //煤炭结果
                breakBulkShipResult = new Result(ShipEnum.Break_Bulk_Ship), //散杂船结果
                containerShipResult = new Result(ShipEnum.Container_Ship);  //集装箱结果
        resultMap.put("totalResult", totalResult);
        resultMap.put("ironOreResult", ironOreResult);
        resultMap.put("chemicalOilResult", chemicalOilResult);
        resultMap.put("crudeOilResult", crudeOilResult);
        resultMap.put("coalResult", coalResult);
        resultMap.put("breakBulkShipResult", breakBulkShipResult);
        resultMap.put("containerShipResult", containerShipResult);
        //仿真开始---------------------------------------------------------
        try {
            HashMap<Integer, Ship> simulationShipMap = genShips(simulationDays);
            comShip(berthList, simulationShipMap);
            comResult(resultMap, simulationShipMap, simulationSteps);
            /*for (int step = 1; step < simulationSteps + 1; step++) {
                Ship ship = simulationShipMap.get(step);
                // NO.1进港过程
                // 运行到有新产生的船
                if (null != ship) {
                    //如果有空闲锚位
                    if (hasIdleAnchorage(anchorageList, ship.getShipEnum().getTypeCode())) {
                        addShipInAnchorage(anchorageList, ship);
                        addResult(resultMap, ship.getShipEnum().getTypeCode());
                    }
                    //如果没有空闲锚位,将该船移动下一步中
                    else {
                        TimeNode timeNode = ship.getTimeNode();
                        timeNode.setArriveTime(timeNode.getArriveTime() + 1);
                        ship.setTimeNode(timeNode);
                        simulationShipMap.remove(ship.getId());
                        simulationShipMap.put(step + 1, ship);
                    }
                }
                for (Anchorage anchorage : anchorageList) {
                    if (CollectionUtils.isEmpty(anchorage.getShipList()))
                        continue;
                    Ship anchorageFirstShip = anchorage.getShipList().getFirst();
                    //是否有空余泊位 && 航道是否允许进入
                    if (hasIdleBerth(anchorageFirstShip, berthList)
                            && canIntoChannel(anchorageFirstShip, channelList, step)) {
                        anchorageFirstShip.getTimeNode().setStartInChannelTime(step);
                        //如果大于16米吃水深度，走深水航道
                        if (anchorageFirstShip.getDepth() >= 16.0) {
                            channelList.get(0).getInShipList().addLast(anchorageFirstShip);
                        } else {
                            channelList.get(1).getInShipList().addLast(anchorageFirstShip);
                        }
                        simulationShipMap.put(anchorageFirstShip.getId(), anchorageFirstShip);
                        anchorage.getShipList().removeFirst();
                    }
                }
                //
                for (Channel channel : channelList) {
                    //进港船队列不为空，看对首的船是否该驶完航道
                    if (!CollectionUtils.isEmpty(channelList)) {

                    }
                }
            }*/
        } catch (Exception e) {
            logger.error(e);
        }
        //accumulateResult(resultMap, ship, 0);
        //累计结果
        //仿真结束---------------------------------------------------------
        //总的
        resultList.add(compResult(resultMap.get("totalResult"), simulationTime));
        //集装箱
        resultList.add(compResult(resultMap.get("containerShipResult"), simulationTime));
        //铁矿石
        resultList.add(compResult(resultMap.get("ironOreResult"), simulationTime));
        //化工油品
        resultList.add(compResult(resultMap.get("chemicalOilResult"), simulationTime));
        //原油
        resultList.add(compResult(resultMap.get("crudeOilResult"), simulationTime));
        //煤炭
        resultList.add(compResult(resultMap.get("coalResult"), simulationTime));
        //散杂船
        resultList.add(compResult(resultMap.get("breakBulkShipResult"), simulationTime));

        /*
        //总的
        resultList.add(new Result(0, null, 23.5 + Math.random() * 2, 0, 0.78 + Math.random() * 0.2, 0, 0.35 + Math.random(), 0, 16 + Math.random(), 0, "", ""));
        //集装箱
        resultList.add(new Result(0, ShipEnum.Container_Ship, 20.0 + Math.random() * 2, 0, 0.5 + Math.random() * 0.2, 0, 0.3 + Math.random(), 0, 14.32 + Math.random(), 0, "", ""));
        //铁矿石
        resultList.add(new Result(0, ShipEnum.Iron_Ore, 30.8 + Math.random() * 5, 0, 0.5 + Math.random() * 0.3, 0, 0.45 + Math.random(), 0, 45.66 + Math.random(), 0, "", ""));
        //化工油品
        resultList.add(new Result(0, ShipEnum.Chemical_Oil, 40.45 + Math.random() * 2, 0, 0.7 + Math.random() * 0.2, 0, 0.5 + Math.random(), 0, 38.49 + Math.random(), 0, "", ""));
        //原油
        resultList.add(new Result(0, ShipEnum.Crude_Oil, 55 + Math.random() * 2, 0, 4 + Math.random() * 2, 0, 0.5 + Math.random(), 0, 76.21 + Math.random(), 0, "", ""));
        //煤炭
        resultList.add(new Result(0, ShipEnum.Coal, 37 + Math.random() * 2, 0, 0.2 + Math.random() * 0.1, 0, 0.3 + Math.random(), 0, 58.57 + Math.random(), 0, "", ""));
        //散杂船
        resultList.add(new Result(0, ShipEnum.Break_Bulk_Ship, 26 + Math.random() * 2, 0, 0.2 + Math.random() * 5, 0, 0.1 + Math.random(), 0, 50.98 + Math.random(), 0, "", ""));
        */
        return resultList;
    }
    //船进港，新增结果计数
    private void addResult(Map<String, Result> resultMap, int typeCode) {
        Result totalResult = resultMap.get("totalResult");
        totalResult.setNumber(totalResult.getNumber() + 1);
        resultMap.put("totalResult", totalResult);
        switch (typeCode) {
            case 0://集装箱
                Result containerShipResult = resultMap.get("containerShipResult");
                containerShipResult.setNumber(containerShipResult.getNumber() + 1);
                resultMap.put("containerShipResult", containerShipResult);
                break;
            case 1://铁矿石
                Result ironOreResult = resultMap.get("ironOreResult");
                ironOreResult.setNumber(ironOreResult.getNumber() + 1);
                resultMap.put("ironOreResult", ironOreResult);
                break;
            case 2://化工油品
                Result chemicalOilResult = resultMap.get("chemicalOilResult");
                chemicalOilResult.setNumber(chemicalOilResult.getNumber() + 1);
                resultMap.put("chemicalOilResult", chemicalOilResult);
                break;
            case 3://原油
                Result crudeOilResult = resultMap.get("crudeOilResult");
                crudeOilResult.setNumber(crudeOilResult.getNumber() + 1);
                resultMap.put("crudeOilResult", crudeOilResult);
                break;
            case 4://煤炭
                Result coalResult = resultMap.get("coalResult");
                coalResult.setNumber(coalResult.getNumber() + 1);
                resultMap.put("coalResult", coalResult);
                break;
            case 5://散杂船
                Result breakBulkShipResult = resultMap.get("breakBulkShipResult");
                breakBulkShipResult.setNumber(breakBulkShipResult.getNumber() + 1);
                resultMap.put("breakBulkShipResult", breakBulkShipResult);
                break;
            default:
                break;
        }
    }

    //组装结果
    private void comResult(Map<String, Result> resultMap, Map<Integer, Ship> simulationShipMap, int simulationSteps) {
        Ship ship;
        for (Map.Entry<Integer, Ship> entry : simulationShipMap.entrySet()) {
            ship = entry.getValue();
            if (ship.getTimeNode().getLeaveTime() <= simulationSteps) {
                accumulateResult(resultMap, ship);
            }
        }
    }

    //船离港，累计统计结果
    private void accumulateResult(Map<String, Result> resultMap, Ship ship) {
        int shipType = ship.getShipEnum().getTypeCode();
        Result totalResult = resultMap.get("totalResult"), result;
        calculateResult(totalResult, ship);
        resultMap.put("totalResult", totalResult);
        switch (shipType) {
            case 0://集装箱
                result = resultMap.get("containerShipResult");
                calculateResult(result, ship);
                resultMap.put("containerShipResult", result);
                break;
            case 1://铁矿石
                result = resultMap.get("ironOreResult");
                calculateResult(result, ship);
                resultMap.put("ironOreResult", result);
                break;
            case 2://化工油品
                result = resultMap.get("chemicalOilResult");
                calculateResult(result, ship);
                resultMap.put("chemicalOilResult", result);
                break;
            case 3://原油
                result = resultMap.get("crudeOilResult");
                calculateResult(result, ship);
                resultMap.put("crudeOilResult", result);
                break;
            case 4://煤炭
                result = resultMap.get("coalResult");
                calculateResult(result, ship);
                resultMap.put("coalResult", result);
                break;
            case 5://散杂船
                result = resultMap.get("breakBulkShipResult");
                calculateResult(result, ship);
                resultMap.put("breakBulkShipResult", result);
                break;
            default:
                break;
        }
    }

    //计算结果
    private void calculateResult(Result result, Ship ship) {
        result.setNumber(result.getNumber() + 1);
        int shipInHarbourMins = ship.getTimeNode().getLeaveTime() - ship.getTimeNode().getArriveTime();
        int totalInHarbourTime = result.getTotalInHarboursMins() + shipInHarbourMins;
        result.setTotalInHarboursMins(totalInHarbourTime);

        int shipWaitChannelMins = ship.getTimeNode().getStartInChannelTime() - ship.getTimeNode().getArriveTime();
        int totalWaitChannelMins = result.getTotalWaitChannelMins() + shipWaitChannelMins;
        result.setTotalWaitChannelMins(totalWaitChannelMins);

        int shipWaitBerthMins = ship.getTimeNode().getOnBerthTime() - ship.getTimeNode().getArriveTime();
        int totalWaitBerthMins = result.getTotalWaitBerthMins() + shipWaitBerthMins;
        result.setTotalWaitBerthMins(totalWaitBerthMins);

        int shipOnBerthMins = ship.getTimeNode().getWorkTime();
        int totalOnBerthMins = result.getTotalOnBerthMins() + shipOnBerthMins;
        result.setTotalOnBerthMins(totalOnBerthMins);
    }

    //组装结果
    private Result compResult(Result result, SimulationTime simulationTime) {
        int number = result.getNumber() == 0 ? 1 : result.getNumber();
        BigDecimal simulationHours = BigDecimalUtil.divide(new BigDecimal(simulationTime.getTimeOut() * simulationTime.getTimeOutUnit().getTime()), new BigDecimal(3600));
        double berthUtilization = BigDecimalUtil.divide4(new BigDecimal(result.getTotalOnBerthMins()), simulationHours.multiply(new BigDecimal(98 * 60 * 2))).doubleValue();
        berthUtilization = berthUtilization < 1.00 ? berthUtilization : 1.00;
        result.setBerthUtilizationRatio(BigDecimalUtil.decimal2PercentString(berthUtilization));
        result.setAvgInHarbourTime(BigDecimalUtil.divide(new BigDecimal(result.getTotalInHarboursMins()), new BigDecimal(number * 60 * 2)).doubleValue());
        result.setAvgWaitChannelTime(BigDecimalUtil.divide(new BigDecimal(result.getTotalWaitChannelMins()), new BigDecimal(number * 60 * 2)).doubleValue());
        result.setAvgOnBerthTime(BigDecimalUtil.divide(new BigDecimal(result.getTotalOnBerthMins()), new BigDecimal(number * 60 * 2)).doubleValue());
        result.setAvgWaitBerthTime(BigDecimalUtil.divide(new BigDecimal(result.getTotalWaitBerthMins()), new BigDecimal(number * 60)).doubleValue());
        BigDecimal onBerthTime = new BigDecimal(result.getAvgOnBerthTime());
        if (Math.abs(onBerthTime.doubleValue()) > 0.000001) {
            result.setAwtAstIndex(BigDecimalUtil.decimal2PercentString(BigDecimalUtil.divide4(new BigDecimal(result.getAvgWaitBerthTime()), onBerthTime).doubleValue()));
        } else {
            result.setAwtAstIndex("--");
        }
        return result;
    }

    //判断是否有空闲锚位
    private boolean hasIdleAnchorage(List<Anchorage> anchorageList, int shipType) {
        switch (shipType) {
            //铁矿石锚地锚位是否已满
            case 1:
                Anchorage oreAnchorage = anchorageList.get(2);
                return oreAnchorage.getShipList().size() < oreAnchorage.getSize();
            //原油锚地锚位时候已满
            case 3:
                Anchorage oilAnchorage = anchorageList.get(3);
                return oilAnchorage.getShipList().size() < oilAnchorage.getSize();
            //如果是其他类型的船，看是南北锚是否都满了
            case 0:
            case 2:
            case 4:
            case 5:
                Anchorage northAnchorage = anchorageList.get(0);
                Anchorage southAnchorage = anchorageList.get(1);
                return northAnchorage.getShipList().size() < northAnchorage.getSize()
                        || southAnchorage.getShipList().size() >= southAnchorage.getSize();
            default:
                return false;
        }
    }

    //判断是否有空闲泊位
    private boolean hasIdleBerth(Ship ship, List<Berth> berthList) {
        for (Berth berth : berthList) {
            //如果码头类型与船类型相同，且该码头没有被占用，将该码头分配给该船舶
            if (ship.getShipEnum().getTypeCode() == berth.getShipEnum().getTypeCode() && null == berth.getShip()) {
                berth.setShip(ship);
                return true;
            }
        }
        return false;
    }

    //判断航道是否允许进入
    private boolean canIntoChannel(Ship ship, List<Channel> channelList, int step) {
        //吃水深度超过16m，需要从深水航槽进入
        if (ship.getDepth() > 16.0)
            return hasMeetSafeDistance(ship, channelList.get(0), step) && hasNotOverflow(channelList.get(0), step);
        else
            return hasMeetSafeDistance(ship, channelList.get(1), step) && hasNotOverflow(channelList.get(1), step);
    }

    //判断是否符合安全距离
    private boolean hasMeetSafeDistance(Ship ship, Channel channel, int step) {
        LinkedList<Ship> inLinkedList = channel.getInShipList();    //进入航道中航行的船舶
        if (CollectionUtils.isEmpty(inLinkedList)) {
            return true;
        }
        //取最后进入航道的那一条船
        Ship lastInChannelShip = inLinkedList.getLast();
        //如果深水航槽里面没有船，则返回可以进入深水槽
        if (lastInChannelShip == null) {
            return true;
        }
        //最后进入深水航槽的船行驶时间,当前时间-进入航道时间，单位：分
        int driveTime = step - lastInChannelShip.getTimeNode().getStartInChannelTime();
        return (driveTime / 60.0) * channel.getLimitedSpeed() * 1000.0 > ship.getSafeDistance();
    }

    //是否符合超过饱和度
    private boolean hasNotOverflow(Channel channel, int step) {
        if (CollectionUtils.isEmpty(channel.getInShipList()))
            return true;
        Ship channelFirstShip = channel.getInShipList().getFirst();
        Ship channelLastShip = channel.getInShipList().getLast();
        if (channelFirstShip != null && channelLastShip != null) {
            int time = channelFirstShip.getTimeNode().getStartInChannelTime();
            int size = channel.getInShipList().size();
            if (channel.getId() == 0 && ((size + 1) / (step - time / 60.0)) > 4.8) {
                return false;
            }
            if (channel.getId() == 1 && ((size + 1 / (step - time / 60.0)) > 9.5)) {
                return false;
            }
        }
        return true;
    }

    //判断是否采取了交通管制，ctrlType=0靠泊管制，ctrlType=1航行管制，ctrlType=2航行管制
    private boolean hasTrafficCtrl(List<Traffic> trafficList, int step, int ctrlType) {
        Traffic traffic;
        //如果是0，影响靠泊
        if (ctrlType == 0) {
            for (int i = 0; i < 3; i++) {
                traffic = trafficList.get(i);
                if (0 == traffic.getStatus())
                    continue;
                if (trafficCtrlResult(traffic, step))
                    return true;
            }
        }
        //如果是1，影响进港
        else if (ctrlType == 1) {
            for (int i = 3; i < 7; i++) {
                if (i != 4) {
                    traffic = trafficList.get(i);
                    if (0 == traffic.getStatus())
                        continue;
                    if (trafficCtrlResult(traffic, step))
                        return true;
                }
            }
        }
        //如果是2，影响出港
        else {
            for (int i = 3; i < 7; i++) {
                traffic = trafficList.get(i);
                if (0 == traffic.getStatus())
                    continue;
                if (trafficCtrlResult(traffic, step))
                    return true;
            }
        }
        return false;
    }
    private boolean trafficCtrlResult(Traffic traffic, int step){
        int startStep = (traffic.getStartMon() - 1) * getMonthDays(traffic.getStartMon()) + traffic.getStartDay() * 24 * 60
                + traffic.getStartHor() * 60 + traffic.getStartMin();
        double duration = traffic.getTrafficDuration() * traffic.getTimeEnum().getTime() * 60;
        if (startStep < step && step < startStep + duration) {
            return true;
        }
        return false;
    }
    //获取月天数
    private int getMonthDays(int month) {
        switch (month) {
            case 2:
                return 29;
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                return 31;
            case 4:
            case 6:
            case 9:
            case 11:
                return 30;
        }
        return 30;
    }

    //将船添加到锚地
    private void addShipInAnchorage(List<Anchorage> anchorageList, Ship ship) {
        int shipType = ship.getShipEnum().getTypeCode();
        switch (shipType) {
            //铁矿石船，放到矿石锚地
            case 1:
                Anchorage oreAnchorage = anchorageList.get(2);
                LinkedList<Ship> oreShipLinkedList = oreAnchorage.getShipList();
                oreShipLinkedList.addLast(ship);
                break;
            //原油船，放到油轮锚地
            case 3:
                Anchorage oilAnchorage = anchorageList.get(3);
                LinkedList<Ship> oilShipLinkedList = oilAnchorage.getShipList();
                oilShipLinkedList.addLast(ship);
                break;
            //如果是其他类型的船，先放北锚地，再放南锚地
            case 0:
            case 2:
            case 4:
            case 5:
                Anchorage northAnchorage = anchorageList.get(0);
                LinkedList<Ship> northShipLinkedList = northAnchorage.getShipList();
                Anchorage southAnchorage = anchorageList.get(1);
                LinkedList<Ship> southShipLinkedList = southAnchorage.getShipList();
                if (northShipLinkedList.size() < northAnchorage.getSize()) {
                    if (4 == shipType) {//如果是碰到煤炭、天然气船，是优先进入，排到头结点
                        northShipLinkedList.addFirst(ship);
                    } else {
                        northShipLinkedList.addLast(ship);
                    }
                } else if (southShipLinkedList.size() < southAnchorage.getSize()) {
                    if (4 == shipType) {//如果是碰到煤炭、天然气船，是优先进入，排到头结点
                        southShipLinkedList.addFirst(ship);
                    } else {
                        southShipLinkedList.addLast(ship);
                    }
                }
                break;
            default:
                break;
        }
    }

    //船泊总数量
    private HashMap<Integer, Ship> genShips(int simulationDays) {
        int[] simulationShipArray = AlgorithmUtil.poissonSamples(36.0193 / (24.0 * 60.0), 24 * 60 * simulationDays);
        HashMap<Integer, Ship> shipHashMap = new HashMap<Integer, Ship>(simulationShipArray.length);
        for (int i = 0; i < simulationShipArray.length; i++) {
            //产生一条船
            if (simulationShipArray[i] == 1) {
                shipHashMap.put(i, getShip(i));
            }
            //产生两条船，
            else if (simulationShipArray[i] == 2) {
                shipHashMap.put(i, getShip(i));
                shipHashMap.put(i + 1, getShip(i + 1));
            }
        }
        return shipHashMap;
    }

    //获取单个船只属性
    private Ship getShip(int id) {
        int typeCode = getShipType();                   //先根据概论产生船的类型
        Ship ship = new Ship();
        ship.setId(id);
        ship.setShipEnum(ShipEnum.values()[typeCode]);
        double length = genLength(typeCode);
        ship.setLength(length);
        double tonner = getTonner(length, typeCode);    //根据船长算载重吨位
        ship.setTonner(tonner);
        ship.setSafeDistance(6 * length);
        ship.setDepth(getDepth(tonner));                //根据载重吨位算吃水深度
        if (ship.getShipEnum().getTypeCode() == 2) {
            ship.setPriorityEnum(PriorityEnum.HIGH);
        } else if (ship.getShipEnum().getTypeCode() == 0 || ship.getShipEnum().getTypeCode() == 1) {
            ship.setPriorityEnum(PriorityEnum.LOW);
        } else {
            ship.setPriorityEnum(PriorityEnum.NORMAL);
        }
        ship.setSpeed(AlgorithmUtil.normalSample(1.852, 0.5)); //km/h TODO 正态分布
        TimeNode timeNode = new TimeNode();
        timeNode.setArriveTime(id); //某一步
        timeNode.setWorkTime(getWorkTime(typeCode));  //按对数正态分布，得出其靠泊时长
        ship.setTimeNode(timeNode);
        return ship;
    }

    //根据概论产生船的类型
    private int getShipType() {
        double random = 0.0001 + RandomUtils.nextDouble();
        if (random <= ShipEnum.Container_Ship.getSection()) {
            return 0;
        } else if (random <= ShipEnum.Iron_Ore.getSection()) {
            return 1;
        } else if (random <= ShipEnum.Chemical_Oil.getSection()) {
            return 2;
        } else if (random <= ShipEnum.Crude_Oil.getSection()) {
            return 3;
        } else if (random <= ShipEnum.Coal.getSection()) {
            return 4;
        } else if (random <= ShipEnum.Break_Bulk_Ship.getSection()) {
            return 5;
        }
        return 0;
    }

    //动态产生船长
    private double genLength(int typeCode) {
        switch (typeCode) {
            case 0:
                return AlgorithmUtil.normalSample(259.4, 100.0);    //TODO 方差需要严格计算
            case 1:
                return AlgorithmUtil.normalSample(279.4, 70.0);
            case 2:
                return AlgorithmUtil.normalSample(176.2, 30.0);
            case 3:
                return AlgorithmUtil.normalSample(311.8, 60.0);
            case 4:
                return AlgorithmUtil.normalSample(216.3, 50.0);
            case 5:
                return AlgorithmUtil.normalSample(208.1, 40.0);
            default:
                return 0;
        }
    }

    //动态产生船吨位
    private double getTonner(double length, int typeCode) {
        switch (typeCode) {
            case 0:
                return AlgorithmUtil.power(length / 3.6221, 1.0 / 0.3898);
            case 1:
                return AlgorithmUtil.power(length / 5.5706, 1.0 / 0.327);
            case 2:
                return AlgorithmUtil.power(length / 4.8624, 1.0 / 0.3441);
            case 3:
                return AlgorithmUtil.power(length / 8.7269, 1.0 / 0.2879);
            case 4:
                return AlgorithmUtil.power(length / 8.1572, 1.0 / 0.2960);
            case 5:
                return AlgorithmUtil.power(length / 8.3339, 1.0 / 0.2928);
            default:
                return 0.0;
        }
    }

    //动态产生船吃水深度
    private double getDepth(double tonner) {
        //非货运船，小于万吨
        if (tonner < 10000) {
            return AlgorithmUtil.normalSample(9.0, 2);
        } else if (tonner < 50000) {
            return AlgorithmUtil.normalSample(11.0, 1);
        } else if (tonner < 100000) {
            return AlgorithmUtil.normalSample(14.0, 2);
        } else if (tonner < 150000) {
            return AlgorithmUtil.normalSample(17, 1);
        } else {
            return AlgorithmUtil.normalSample(20, 2);
        }
    }

    //动态产生船的靠泊时长，单位：分
    private int getWorkTime(int typeCode) {
        Double workTime = 60.0;
        switch (typeCode) {
            case 0:
                workTime *= AlgorithmUtil.logNormalSample(0.49, 2.54);
                break;
            case 1:
                workTime *= AlgorithmUtil.logNormalSample(0.82, 3.55);
                break;
            case 2:
                workTime *= AlgorithmUtil.logNormalSample(0.57, 3.48);
                break;
            case 3:
                workTime *= AlgorithmUtil.logNormalSample(0.58, 4.18);
                break;
            case 4:
                workTime *= AlgorithmUtil.logNormalSample(0.67, 3.89);
                break;
            case 5:
                workTime *= AlgorithmUtil.logNormalSample(0.76, 3.65);
                break;
        }
        return workTime.intValue();
    }

    //获取通过航道的时间（单位分钟）,type=0，表示进港，type=1，表示出港
    private int getPassChannelTime(double tonner, int type) {
        //吃水深度超过16m，且进港，需要从深水航道进入
        if (type == 0 && tonner > 16.0)
            return 45 + 143;  //new BigDecimal((14.89 / 19.65) * 60.0).intValue();
        else
            return 143;       //new BigDecimal((53.00 / 22.11) * 60.0).intValue();
    }

    //遍历船舶
    private void comShip(List<Berth> berthList, HashMap<Integer, Ship> simulationShipMap) {
        Ship ship;
        TimeNode timeNode;
        Double leaveTime;
        Map<Integer, Double> map = getAvgTimeFromChannel2Berth(berthList);
        for (Map.Entry<Integer, Ship> entry : simulationShipMap.entrySet()) {
            ship = entry.getValue();
            timeNode = ship.getTimeNode();
            timeNode.setStartInChannelTime(timeNode.getArriveTime() + new BigDecimal(AlgorithmUtil.normalSample(60.0, 30.0)).intValue());
            timeNode.setOnBerthTime(getPassChannelTime(ship.getTonner(), 0) + timeNode.getStartInChannelTime() + map.get(ship.getShipEnum().getTypeCode()).intValue());
            leaveTime = timeNode.getOnBerthTime() + timeNode.getWorkTime() + AlgorithmUtil.normalSample(60 * map.get(ship.getShipEnum().getTypeCode()), 0.5);
            timeNode.setLeaveTime(leaveTime.intValue());
        }
    }

    //各类船舶平均从虾峙门到泊位的时间，单位：小时
    private Map<Integer, Double> getAvgTimeFromChannel2Berth(List<Berth> berthList) {
        Map<Integer, Double> map = new HashMap<Integer, Double>();
        double csTime = 0.0, ironTime = 0.0, coTime = 0.0, curOilTime = 0.0, coalTime = 0.0, bbsTime = 0.0;
        int csCount = 0, ironCount = 0, coCount = 0, curOilCount = 0, coalCount = 0, bbsCount = 0;
        for (Berth berth : berthList) {
            switch (berth.getShipEnum().getTypeCode()) {
                case 0:
                    csTime += berth.getToAnchorageTime();
                    csCount++;
                    break;
                case 1:
                    ironTime += berth.getToAnchorageTime();
                    ironCount++;
                    break;
                case 2:
                    coTime += berth.getToAnchorageTime();
                    coCount++;
                    break;
                case 3:
                    curOilTime += berth.getToAnchorageTime();
                    curOilCount++;
                    break;
                case 4:
                    coalTime += berth.getToAnchorageTime();
                    coalCount++;
                    break;
                case 5:
                    bbsTime += berth.getToAnchorageTime();
                    bbsCount++;
                    break;
            }
        }
        map.put(0, csTime / csCount);
        map.put(1, ironTime / ironCount);
        map.put(2, coTime / coCount);
        map.put(3, curOilTime / curOilCount);
        map.put(4, coalTime  / coalCount);
        map.put(5, bbsTime / bbsCount);
        return map;
    }
}
