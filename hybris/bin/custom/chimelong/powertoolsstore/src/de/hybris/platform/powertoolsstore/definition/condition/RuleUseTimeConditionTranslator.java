package de.hybris.platform.powertoolsstore.definition.condition;

import com.google.common.collect.Lists;
import de.hybris.platform.ruledefinitions.conditions.AbstractRuleConditionTranslator;
import de.hybris.platform.ruledefinitions.conditions.builders.IrConditions;
import de.hybris.platform.ruledefinitions.conditions.builders.RuleIrAttributeConditionBuilder;
import de.hybris.platform.ruledefinitions.conditions.builders.RuleIrAttributeRelConditionBuilder;
import de.hybris.platform.ruledefinitions.conditions.builders.RuleIrGroupConditionBuilder;
import de.hybris.platform.ruleengineservices.compiler.*;
import de.hybris.platform.ruleengineservices.rao.CartRAO;
import de.hybris.platform.ruleengineservices.rao.OrderEntryRAO;
import de.hybris.platform.ruleengineservices.rule.data.RuleConditionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleConditionDefinitionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleParameterData;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created on 09/05/2018.
 */
public class RuleUseTimeConditionTranslator extends AbstractRuleConditionTranslator {
    @Override
    public RuleIrCondition translate(RuleCompilerContext context, RuleConditionData condition, RuleConditionDefinitionData conditionDefinition) {
        Map<String, RuleParameterData> conditionParameters = condition.getParameters();
        RuleParameterData startTimeParameter = (RuleParameterData) conditionParameters.get("useStartTime");
        RuleParameterData endTimeParameter = (RuleParameterData) conditionParameters.get("useEndTime");

        if (this.verifyAllPresent(new Object[]{startTimeParameter, endTimeParameter})) {
            Date startTime = (Date) startTimeParameter.getValue();
            Date endTime = (Date) endTimeParameter.getValue();
            if (this.verifyAllPresent(new Object[]{startTime, endTime})) {
                return this.getUseTimeConditions(context, startTime, endTime);
            }

        }

        return IrConditions.newIrRuleFalseCondition();
    }

    private RuleIrCondition getUseTimeConditions(RuleCompilerContext context, Date startTime, Date endTime) {
        RuleIrGroupCondition irGroupCondition = RuleIrGroupConditionBuilder.newGroupConditionOf(RuleIrGroupOperator.AND).build();
        this.addUseTimeConditions(context, startTime, endTime, irGroupCondition);
        return irGroupCondition;
    }

    private void addUseTimeConditions(RuleCompilerContext context, Date startTime, Date endTime, RuleIrGroupCondition irGroupCondition) {
        String orderEntryRaoVariable = context.generateVariable(OrderEntryRAO.class);
        String cartRaoVariable = context.generateVariable(CartRAO.class);
        List<RuleIrCondition> conditions = Lists.newArrayList();
        RuleIrGroupCondition irUseTimeGroupCondition = RuleIrGroupConditionBuilder.newGroupConditionOf(RuleIrGroupOperator.AND).withChildren(conditions).build();
        conditions.add(RuleIrAttributeConditionBuilder.newAttributeConditionFor(orderEntryRaoVariable).withAttribute("useStartTime").withOperator(RuleIrAttributeOperator.GREATER_THAN_OR_EQUAL).withValue(startTime).build());
        conditions.add(RuleIrAttributeConditionBuilder.newAttributeConditionFor(orderEntryRaoVariable).withAttribute("useEndTime").withOperator(RuleIrAttributeOperator.LESS_THAN_OR_EQUAL).withValue(endTime).build());
        conditions.add(RuleIrAttributeRelConditionBuilder.newAttributeRelationConditionFor(cartRaoVariable).withAttribute("entries").withOperator(RuleIrAttributeOperator.CONTAINS).withTargetVariable(orderEntryRaoVariable).build());
        irGroupCondition.getChildren().add(irUseTimeGroupCondition);
    }
}
