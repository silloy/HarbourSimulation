package com.github.liuxboy.harbour.simulation.domain.biz;

/**
 * <p>Title: PassRule</p>
 * <p>Copyright: Copyright(c)2016</p>
 * <p>Company: JD.JR </p>
 * <p>Time: 2016/6/4 12:06</p>
 * <p>Description: 通航条件 </p>
 *
 * @author wyliuchundong
 * @version 1.0
 */

import com.github.liuxboy.harbour.simulation.common.constant.PassRuleEnum;

/**
 * （1.1）先干线，后支线；先航道后锚地，确保航道通畅。适当控制锚地船舶的起锚时间，防止出现交通流密度过大导致拥堵；
 * （即优先安排在深水航道的船进入，然后安排在锚地的船舶进入）
 * （1.2）正常以船舶到L1报告线的ETA或引水上船时间先后顺序安排进口，当有两艘或两艘以上的船舶ETA或者引水上船时间相同时，则根据各船实际位置，离口门较近或者航速较快的优先安排进口，并尽早告知各船其在编队中的位置；
 * （按照到港时间的先后顺序安排进港，如时间相同则安排离口较近或航速较快的船舶优先进入航道）
 * （1.3）VLCC（油轮）、超大型矿船等船舶因操纵不便，其进口时其他船舶不得妨碍；
 * （即油轮和超大型船在进口时不允许其他船舶在该时刻进入）
 * （1.4）煤炭、天然气等重要保障资源船舶优先。
 * （液化气船具有优先进入权）
 */
public class PassRule {
    //通航规则编号
    private int id;
    //通过规则
    private PassRuleEnum passRuleEnum;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public PassRuleEnum getPassRuleEnum() {
        return passRuleEnum;
    }

    public void setPassRuleEnum(PassRuleEnum passRuleEnum) {
        this.passRuleEnum = passRuleEnum;
    }

    @Override
    public String toString() {
        final StringBuilder stbd = new StringBuilder("PassRule{");
        stbd.append("\"id\":").append(id);
        stbd.append(",\"passRuleEnum\":").append(passRuleEnum);
        stbd.append('}');
        stbd.append(super.toString());
        return stbd.toString();
    }
}
